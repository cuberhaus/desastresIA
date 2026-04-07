"""Board: holds the problem instance (centres, groups) and precomputed distances.

Mirrors the Java ``Desastres.board`` class.
"""

from __future__ import annotations

import math

from .models import Centro, Grupo


class Board:
    __slots__ = (
        "centros",
        "grupos",
        "helicopters",
        "num_helicopters",
        "_dist_cg",
        "_dist_gg",
    )

    def __init__(self, centros: list[Centro], grupos: list[Grupo]) -> None:
        self.centros = centros
        self.grupos = grupos

        self.helicopters: list[int] = []
        for i, c in enumerate(centros):
            for _ in range(c.n_helicopters):
                self.helicopters.append(i)
        self.num_helicopters = len(self.helicopters)

        self._dist_cg = self._precalc_center_group()
        self._dist_gg = self._precalc_group_group()

    # ── distance queries ──────────────────────────────────────────────

    def dist_center_group(self, center_id: int, group_id: int) -> float:
        return self._dist_cg[center_id][group_id]

    def dist_group_group(self, g1: int, g2: int) -> float:
        return self._dist_gg[g1][g2]

    def get_centro_for_helicopter(self, heli_id: int) -> int:
        return self.helicopters[heli_id]

    # ── precomputation ────────────────────────────────────────────────

    def _precalc_center_group(self) -> list[list[float]]:
        matrix: list[list[float]] = []
        for c in self.centros:
            row: list[float] = []
            for g in self.grupos:
                row.append(_euclidean(c.x, c.y, g.x, g.y))
            matrix.append(row)
        return matrix

    def _precalc_group_group(self) -> list[list[float]]:
        n = len(self.grupos)
        matrix: list[list[float]] = [[0.0] * n for _ in range(n)]
        for i in range(n):
            gi = self.grupos[i]
            for j in range(i + 1, n):
                gj = self.grupos[j]
                d = _euclidean(gi.x, gi.y, gj.x, gj.y)
                matrix[i][j] = d
                matrix[j][i] = d
        return matrix


def _euclidean(x1: int, y1: int, x2: int, y2: int) -> float:
    return math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)
