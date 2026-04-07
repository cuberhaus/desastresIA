"""Hill Climbing and Simulated Annealing solvers.

Mirrors the AIMA ``HillClimbingSearch`` and ``SimulatedAnnealingSearch``
classes used by the Java project.

HC neighbour evaluation and experiment batches are parallelised across
CPU cores using ``ProcessPoolExecutor``.
"""

from __future__ import annotations

import math
import os
import random
import time
from concurrent.futures import ProcessPoolExecutor
from typing import Callable

from .board import Board
from .heuristics import compute_helicopter_times, heuristic_1, heuristic_2, heuristic_3
from .models import RouteSegment, SolveResult
from .state import Assignment, make_initial_state
from .successors import SUCCESSOR_FUNCTIONS, sf6_sa_random

HeuristicFn = Callable[[Assignment, Board], float]

HEURISTIC_FUNCTIONS: dict[int, HeuristicFn] = {
    1: heuristic_1,
    2: heuristic_2,
    3: heuristic_3,
}

_NUM_WORKERS = max(1, min(os.cpu_count() or 4, 8))
_PARALLEL_THRESHOLD = 800


# ── worker helpers (module-level for pickling) ────────────────────────

_w_board: Board | None = None
_w_hf: HeuristicFn | None = None


def _init_hc_worker(centros_data: list[dict], grupos_data: list[dict], hf_id: int) -> None:
    """Called once per worker process to reconstruct the Board."""
    global _w_board, _w_hf
    from .models import Centro, Grupo

    _w_board = Board(
        [Centro(**d) for d in centros_data],
        [Grupo(**d) for d in grupos_data],
    )
    _w_hf = HEURISTIC_FUNCTIONS[hf_id]


def _eval_batch(states: list[Assignment]) -> list[float]:
    """Evaluate heuristic for a batch of states in a worker process."""
    assert _w_board is not None and _w_hf is not None
    return [_w_hf(s, _w_board) for s in states]


def _serialize_board(board: Board) -> tuple[list[dict], list[dict]]:
    return (
        [{"x": c.x, "y": c.y, "n_helicopters": c.n_helicopters} for c in board.centros],
        [{"x": g.x, "y": g.y, "priority": g.priority, "n_personas": g.n_personas} for g in board.grupos],
    )


# ── Hill Climbing ─────────────────────────────────────────────────────

def hill_climbing(
    board: Board,
    initial: Assignment,
    successor_fn_id: int,
    heuristic_fn_id: int,
    *,
    parallel: bool = True,
) -> SolveResult:
    hf = HEURISTIC_FUNCTIONS[heuristic_fn_id]
    sf = SUCCESSOR_FUNCTIONS[successor_fn_id]
    h_initial = hf(initial, board)

    current = initial
    current_h = h_initial
    nodes = 0
    pool: ProcessPoolExecutor | None = None

    t0 = time.perf_counter()

    try:
        while True:
            neighbours = list(sf(current, board))
            if not neighbours:
                break

            states = [n for n, _ in neighbours]
            nodes += len(states)

            use_parallel = parallel and len(states) >= _PARALLEL_THRESHOLD
            if use_parallel and pool is None:
                c_ser, g_ser = _serialize_board(board)
                pool = ProcessPoolExecutor(
                    max_workers=_NUM_WORKERS,
                    initializer=_init_hc_worker,
                    initargs=(c_ser, g_ser, heuristic_fn_id),
                )

            if pool is not None and use_parallel:
                best_h, best_state = _parallel_best(pool, states, current_h)
            else:
                best_h = current_h
                best_state = None
                for state in states:
                    h = hf(state, board)
                    if h < best_h:
                        best_h = h
                        best_state = state

            if best_state is None:
                break

            current = best_state
            current_h = best_h
    finally:
        if pool is not None:
            pool.shutdown(wait=False)

    elapsed = (time.perf_counter() - t0) * 1000
    heli_times = compute_helicopter_times(current, board)
    routes = build_routes(current, board)

    return SolveResult(
        assignment=current,
        heuristic_initial=h_initial,
        heuristic_final=current_h,
        exec_time_ms=elapsed,
        nodes_expanded=nodes,
        helicopter_times=heli_times,
        routes=routes,
    )


def _parallel_best(
    pool: ProcessPoolExecutor,
    states: list[Assignment],
    current_h: float,
) -> tuple[float, Assignment | None]:
    chunk_size = max(1, len(states) // _NUM_WORKERS)
    chunks = [states[i : i + chunk_size] for i in range(0, len(states), chunk_size)]

    best_h = current_h
    best_state: Assignment | None = None
    offset = 0

    for batch_scores in pool.map(_eval_batch, chunks):
        for h in batch_scores:
            if h < best_h:
                best_h = h
                best_state = states[offset]
            offset += 1

    return best_h, best_state


# ── Simulated Annealing ──────────────────────────────────────────────

def simulated_annealing(
    board: Board,
    initial: Assignment,
    heuristic_fn_id: int,
    steps: int = 20000,
    stiter: int = 5,
    k: int = 125,
    lambda_: float = 1.0,
) -> SolveResult:
    hf = HEURISTIC_FUNCTIONS[heuristic_fn_id]
    h_initial = hf(initial, board)

    current = initial
    current_h = h_initial
    nodes = 0
    trace: list[float] = []

    rng = random.Random()
    t0 = time.perf_counter()

    for step in range(steps):
        temperature = k * math.exp(-lambda_ * step / steps)
        if temperature < 1e-10:
            break

        for _ in range(stiter):
            neighbours = list(sf6_sa_random(current, board))
            if not neighbours:
                continue
            neighbour, _desc = neighbours[0]
            nodes += 1
            h = hf(neighbour, board)
            delta = h - current_h

            if delta < 0 or rng.random() < math.exp(-delta / temperature):
                current = neighbour
                current_h = h

        if step % max(1, steps // 200) == 0:
            trace.append(current_h)

    elapsed = (time.perf_counter() - t0) * 1000
    heli_times = compute_helicopter_times(current, board)
    routes = build_routes(current, board)

    return SolveResult(
        assignment=current,
        heuristic_initial=h_initial,
        heuristic_final=current_h,
        exec_time_ms=elapsed,
        nodes_expanded=nodes,
        helicopter_times=heli_times,
        routes=routes,
        trace=trace,
    )


# ── experiment task (module-level for ProcessPoolExecutor) ────────────

def run_experiment_task(args: tuple) -> dict:
    """Run a single (seed, config) combination.  Called in a worker process."""
    seed, cfg, n_centros, n_heli, n_grupos, init_state_mode = args
    from .generator import generate_centros, generate_grupos

    centros = generate_centros(n_centros, n_heli, seed)
    grupos = generate_grupos(n_grupos, seed)
    board = Board(centros, grupos)
    initial = make_initial_state(board, init_state_mode, seed=seed)

    if cfg["algorithm"] == "hc":
        res = hill_climbing(
            board, initial, cfg["successor_fn"], cfg["heuristic_fn"], parallel=False
        )
    else:
        res = simulated_annealing(
            board,
            initial,
            cfg["heuristic_fn"],
            steps=cfg.get("sa_steps", 20000),
            stiter=cfg.get("sa_stiter", 5),
            k=cfg.get("sa_k", 125),
            lambda_=cfg.get("sa_lambda", 1.0),
        )

    return {
        "label": cfg["label"],
        "seed": seed,
        "heuristic_final": round(res.heuristic_final, 2),
        "exec_time_ms": round(res.exec_time_ms, 1),
        "nodes_expanded": res.nodes_expanded,
    }


# ── route geometry builder ────────────────────────────────────────────

def build_routes(state: Assignment, board: Board) -> list[list[RouteSegment]]:
    """Convert an assignment into drawable route segments per helicopter."""
    from .heuristics import MAX_CAPACITY, MAX_GROUPS_PER_TRIP, SPEED

    all_routes: list[list[RouteSegment]] = []

    for heli_id, groups in enumerate(state):
        segs: list[RouteSegment] = []
        if not groups:
            all_routes.append(segs)
            continue

        center_id = board.get_centro_for_helicopter(heli_id)
        cx, cy = board.centros[center_id].x, board.centros[center_id].y

        capacity = 0
        last_x, last_y = cx, cy
        last_group = -1
        n_trip_groups = 0
        trip = 0

        for j, gid in enumerate(groups):
            g = board.grupos[gid]
            gx, gy = g.x, g.y
            is_last = j == len(groups) - 1

            if not is_last:
                if capacity + g.n_personas <= MAX_CAPACITY and n_trip_groups < MAX_GROUPS_PER_TRIP:
                    capacity += g.n_personas
                    n_trip_groups += 1
                    segs.append(RouteSegment(
                        from_x=last_x, from_y=last_y, to_x=gx, to_y=gy,
                        group_id=gid, trip_number=trip, pickup_people=g.n_personas,
                    ))
                    last_x, last_y = gx, gy
                    last_group = gid
                else:
                    segs.append(RouteSegment(
                        from_x=last_x, from_y=last_y, to_x=cx, to_y=cy,
                        group_id=-1, trip_number=trip, pickup_people=0, is_return=True,
                    ))
                    trip += 1
                    capacity = g.n_personas
                    n_trip_groups = 1
                    segs.append(RouteSegment(
                        from_x=cx, from_y=cy, to_x=gx, to_y=gy,
                        group_id=gid, trip_number=trip, pickup_people=g.n_personas,
                    ))
                    last_x, last_y = gx, gy
                    last_group = gid
            elif capacity + g.n_personas <= MAX_CAPACITY and n_trip_groups < MAX_GROUPS_PER_TRIP:
                segs.append(RouteSegment(
                    from_x=last_x, from_y=last_y, to_x=gx, to_y=gy,
                    group_id=gid, trip_number=trip, pickup_people=g.n_personas,
                ))
                segs.append(RouteSegment(
                    from_x=gx, from_y=gy, to_x=cx, to_y=cy,
                    group_id=-1, trip_number=trip, pickup_people=0, is_return=True,
                ))
            else:
                segs.append(RouteSegment(
                    from_x=last_x, from_y=last_y, to_x=cx, to_y=cy,
                    group_id=-1, trip_number=trip, pickup_people=0, is_return=True,
                ))
                trip += 1
                segs.append(RouteSegment(
                    from_x=cx, from_y=cy, to_x=gx, to_y=gy,
                    group_id=gid, trip_number=trip, pickup_people=g.n_personas,
                ))
                segs.append(RouteSegment(
                    from_x=gx, from_y=gy, to_x=cx, to_y=cy,
                    group_id=-1, trip_number=trip, pickup_people=0, is_return=True,
                ))

        all_routes.append(segs)

    return all_routes
