"""State representation and initial-state generators.

Mirrors the Java ``Desastres.estado`` class.  A state is a list of lists:
``assignment[helicopter_id]`` is the ordered list of group IDs that
helicopter must visit.
"""

from __future__ import annotations

import copy
import heapq
import random
from typing import Literal

from .board import Board

Assignment = list[list[int]]


def make_initial_state(
    board: Board,
    mode: Literal["random", "all_to_one", "greedy"],
    seed: int | None = None,
) -> Assignment:
    n_groups = len(board.grupos)
    n_heli = board.num_helicopters
    if mode == "random":
        return _random_state(n_groups, n_heli, seed)
    elif mode == "all_to_one":
        return _all_to_one(n_groups, n_heli)
    else:
        return _greedy_state(n_groups, n_heli, board)


def clone_state(state: Assignment) -> Assignment:
    return [row[:] for row in state]


# ── operators ─────────────────────────────────────────────────────────

def swap_groups(
    state: Assignment, hi: int, gi: int, hj: int, gj: int
) -> None:
    state[hi][gi], state[hj][gj] = state[hj][gj], state[hi][gi]


def reassign_general(
    state: Assignment, src_h: int, src_pos: int, dst_h: int, dst_pos: int
) -> None:
    val = state[src_h].pop(src_pos)
    state[dst_h].insert(dst_pos, val)


def reassign_reduced(state: Assignment, src_h: int, dst_h: int) -> None:
    if state[src_h]:
        state[dst_h].append(state[src_h].pop())


# ── initial-state generators ─────────────────────────────────────────

def _random_state(
    n_groups: int, n_heli: int, seed: int | None
) -> Assignment:
    rng = random.Random(seed)
    assignment: Assignment = [[] for _ in range(n_heli)]
    remaining = list(range(n_groups))
    rng.shuffle(remaining)
    for g in remaining:
        h = rng.randint(0, n_heli - 1)
        assignment[h].append(g)
    return assignment


def _all_to_one(n_groups: int, n_heli: int) -> Assignment:
    assignment: Assignment = [[] for _ in range(n_heli)]
    for g in range(n_groups):
        assignment[0].append(g)
    return assignment


def _greedy_state(n_groups: int, n_heli: int, board: Board) -> Assignment:
    """Port of estado.gen_estado_inicial_greedy — nearest-feasible-group
    heuristic using a priority queue of helicopters keyed by elapsed time."""

    assignment: Assignment = [[] for _ in range(n_heli)]

    # Priority queue entries: (elapsed_time, counter, heli_state)
    # counter breaks ties deterministically.
    counter = 0
    heap: list[tuple[float, int, dict]] = []

    heli_idx = 0
    for ci, centro in enumerate(board.centros):
        for _ in range(centro.n_helicopters):
            entry = {
                "heli_id": heli_idx,
                "center_id": ci,
                "pos_id": ci,
                "at_center": True,
                "n_groups": 0,
                "n_personas": 0,
            }
            heapq.heappush(heap, (0.0, counter, entry))
            counter += 1
            heli_idx += 1

    remaining = set(range(n_groups))

    while remaining:
        elapsed, _, heli = heapq.heappop(heap)

        best_group = _closest_group(board, heli, remaining)

        if (
            best_group == -1
            or heli["n_groups"] == 3
            or heli["n_personas"] >= 15
        ):
            # Return to base
            if not heli["at_center"] and heli["pos_id"] >= 0:
                dist_back = board.dist_center_group(
                    heli["center_id"], heli["pos_id"]
                )
                elapsed += dist_back / 1.66667 + 10  # cooldown
            heli["n_groups"] = 0
            heli["n_personas"] = 0
            heli["pos_id"] = heli["center_id"]
            heli["at_center"] = True
            counter += 1
            heapq.heappush(heap, (elapsed, counter, heli))
        else:
            g = board.grupos[best_group]
            if heli["at_center"]:
                dist = board.dist_center_group(heli["center_id"], best_group)
            else:
                dist = board.dist_group_group(heli["pos_id"], best_group)

            time_per_person = 2 if g.priority == 1 else 1
            pickup_time = g.n_personas * time_per_person
            elapsed += dist / 1.66667 + pickup_time

            assignment[heli["heli_id"]].append(best_group)
            remaining.discard(best_group)

            heli["n_personas"] += g.n_personas
            heli["n_groups"] += 1
            heli["at_center"] = False
            heli["pos_id"] = best_group
            counter += 1
            heapq.heappush(heap, (elapsed, counter, heli))

    return assignment


def _closest_group(
    board: Board, heli: dict, remaining: set[int]
) -> int:
    best_dist = float("inf")
    best_id = -1
    for gid in remaining:
        g = board.grupos[gid]
        if heli["n_personas"] + g.n_personas > 15:
            continue
        if heli["at_center"]:
            d = board.dist_center_group(heli["center_id"], gid)
        else:
            d = board.dist_group_group(heli["pos_id"], gid)
        if d < best_dist:
            best_dist = d
            best_id = gid
    return best_id
