"""Port of DesastresHeuristicFunction1–3.

All three share the same helicopter-time simulation; they differ only in
how the per-helicopter times are aggregated into a single scalar.
"""

from __future__ import annotations

from .board import Board
from .state import Assignment

SPEED = 1.66667  # km/min
MAX_CAPACITY = 15
MAX_GROUPS_PER_TRIP = 3
COOLDOWN = 10  # minutes between trips


def simulate_helicopter(
    heli_id: int, groups: list[int], board: Board, *, track_priority: bool = False
) -> tuple[float, float]:
    """Simulate one helicopter's schedule and return (total_time, priority_time).

    priority_time is the running total at the moment each trip carrying a
    priority group returns to base (only meaningful when track_priority=True).
    """
    if not groups:
        return 0.0, 0.0

    center = board.get_centro_for_helicopter(heli_id)
    capacity = 0
    time = 0.0
    ttotal_running = 0.0
    last_group = -1
    n_trip_groups = 0
    priority_travel = False
    priority_time = 0.0

    for j, gid in enumerate(groups):
        g = board.grupos[gid]
        is_last = j == len(groups) - 1

        if not is_last:
            if capacity + g.n_personas <= MAX_CAPACITY and n_trip_groups < MAX_GROUPS_PER_TRIP:
                # Group fits in current trip
                capacity += g.n_personas
                n_trip_groups += 1
                travel, pickup = _pickup(board, center, last_group, gid, g)
                time += travel + pickup
                if track_priority:
                    ttotal_running += travel + pickup
                    if g.priority == 1:
                        priority_travel = True
                last_group = gid
            else:
                # Trip full — return to base, cooldown, start new trip
                ret = board.dist_center_group(center, last_group) / SPEED
                time += ret + COOLDOWN
                if track_priority:
                    ttotal_running += ret
                    if priority_travel:
                        priority_time = ttotal_running
                        priority_travel = False
                    ttotal_running += COOLDOWN

                capacity = 0
                n_trip_groups = 1

                depart = board.dist_center_group(center, gid) / SPEED
                tpp = 2 if g.priority == 1 else 1
                pickup = g.n_personas * tpp
                time += depart + pickup
                if track_priority:
                    ttotal_running += depart + pickup
                    if g.priority == 1:
                        priority_travel = True
                last_group = gid

        elif capacity + g.n_personas <= MAX_CAPACITY and n_trip_groups < MAX_GROUPS_PER_TRIP:
            # Last group fits — pick up and return
            capacity += g.n_personas
            n_trip_groups += 1
            travel, pickup = _pickup(board, center, last_group, gid, g)
            time += travel + pickup
            if track_priority:
                ttotal_running += travel + pickup
                if g.priority == 1:
                    priority_travel = True

            ret = board.dist_center_group(center, gid) / SPEED
            time += ret
            if track_priority:
                ttotal_running += ret
                if priority_travel:
                    priority_time = ttotal_running
                    priority_travel = False
            last_group = gid

        else:
            # Last group doesn't fit — return, cooldown, pick up, return
            ret1 = board.dist_center_group(center, last_group) / SPEED
            time += ret1
            if track_priority:
                ttotal_running += ret1
                if priority_travel:
                    priority_time = ttotal_running
                    priority_travel = False

            time += COOLDOWN

            depart = board.dist_center_group(center, gid) / SPEED
            tpp = 2 if g.priority == 1 else 1
            pickup = g.n_personas * tpp
            time += depart + pickup
            if track_priority:
                ttotal_running += depart + pickup
                if g.priority == 1:
                    priority_travel = True

            capacity = 0
            n_trip_groups = 1

            ret2 = board.dist_center_group(center, gid) / SPEED
            time += ret2
            if track_priority:
                ttotal_running += ret2
                if priority_travel:
                    priority_time = ttotal_running
                    priority_travel = False

    return time, priority_time


def _pickup(board, center, last_group, gid, g):
    if last_group == -1:
        travel = board.dist_center_group(center, gid) / SPEED
    else:
        travel = board.dist_group_group(last_group, gid) / SPEED
    tpp = 2 if g.priority == 1 else 1
    return travel, g.n_personas * tpp


# ── public heuristic functions ────────────────────────────────────────

def heuristic_1(state: Assignment, board: Board) -> float:
    """Weighted total time — balances load across helicopters."""
    times: list[float] = []
    group_counts: list[int] = []
    total = 0.0
    tmax = 0.0
    n_total_groups = len(board.grupos)

    for i, groups in enumerate(state):
        t, _ = simulate_helicopter(i, groups, board)
        times.append(t)
        group_counts.append(len(groups))
        total += t
        if t > tmax:
            tmax = t

    if tmax == 0:
        return 0.0

    aux = sum(
        t * (gc / n_total_groups)
        for t, gc in zip(times, group_counts)
        if t != 0
    )
    ponderacion = 1.0 - (total / board.num_helicopters) / tmax
    return total + aux * ponderacion


def heuristic_2(state: Assignment, board: Board) -> float:
    """Plain sum of all helicopter times."""
    total = 0.0
    for i, groups in enumerate(state):
        t, _ = simulate_helicopter(i, groups, board)
        total += t
    return total


def heuristic_3(state: Assignment, board: Board) -> float:
    """Weighted total time + 128 * priority rescue time."""
    times: list[float] = []
    group_counts: list[int] = []
    total = 0.0
    tmax = 0.0
    priority_time = 0.0
    n_total_groups = len(board.grupos)

    for i, groups in enumerate(state):
        t, pt = simulate_helicopter(i, groups, board, track_priority=True)
        times.append(t)
        group_counts.append(len(groups))
        total += t
        if t > tmax:
            tmax = t
        if pt > priority_time:
            priority_time = pt

    if tmax == 0:
        return 0.0

    aux = sum(
        t * (gc / n_total_groups)
        for t, gc in zip(times, group_counts)
        if t != 0
    )
    ponderacion = 1.0 - (total / board.num_helicopters) / tmax
    return total + aux * ponderacion + priority_time * 128


def compute_helicopter_times(state: Assignment, board: Board) -> list[float]:
    """Return per-helicopter elapsed times (for results display)."""
    return [
        simulate_helicopter(i, groups, board)[0]
        for i, groups in enumerate(state)
    ]
