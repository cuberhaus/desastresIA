from __future__ import annotations

from dataclasses import dataclass, field


@dataclass(slots=True)
class Centro:
    x: int
    y: int
    n_helicopters: int


@dataclass(slots=True)
class Grupo:
    x: int
    y: int
    priority: int  # 1 = high priority, 0 = normal
    n_personas: int


@dataclass(slots=True)
class SolveResult:
    assignment: list[list[int]]
    heuristic_initial: float
    heuristic_final: float
    exec_time_ms: float
    nodes_expanded: int
    helicopter_times: list[float]
    routes: list[list[RouteSegment]]
    trace: list[float] = field(default_factory=list)


@dataclass(slots=True)
class RouteSegment:
    from_x: float
    from_y: float
    to_x: float
    to_y: float
    group_id: int
    trip_number: int
    pickup_people: int
    is_return: bool = False


@dataclass(slots=True)
class ExperimentRun:
    config_label: str
    heuristic_final: float
    exec_time_ms: float
    nodes_expanded: int


@dataclass(slots=True)
class ExperimentResult:
    runs: list[ExperimentRun]
    configs: list[str]
