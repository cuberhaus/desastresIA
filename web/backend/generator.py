"""Reimplementation of IA.Desastres.Centros and IA.Desastres.Grupos.

The original Java library generates random centres and groups from a seed.
We replicate the same structure: random 2D positions on a grid, random
priorities and group sizes, all controlled by a single seed.

Note: exact seed parity with Java's Random is NOT guaranteed — the
statistical properties (uniform coordinates, priority distribution,
group sizes) are identical.
"""

from __future__ import annotations

import random

from .models import Centro, Grupo

_COORD_MAX = 100


def generate_centros(
    n: int, helicopters_per_center: int, seed: int
) -> list[Centro]:
    rng = random.Random(seed)
    centros: list[Centro] = []
    for _ in range(n):
        x = rng.randint(0, _COORD_MAX)
        y = rng.randint(0, _COORD_MAX)
        centros.append(Centro(x=x, y=y, n_helicopters=helicopters_per_center))
    return centros


def generate_grupos(n: int, seed: int) -> list[Grupo]:
    rng = random.Random(seed)
    grupos: list[Grupo] = []
    for _ in range(n):
        x = rng.randint(0, _COORD_MAX)
        y = rng.randint(0, _COORD_MAX)
        priority = rng.randint(0, 1)  # 0 normal, 1 high
        n_personas = rng.randint(1, 15)
        grupos.append(Grupo(x=x, y=y, priority=priority, n_personas=n_personas))
    return grupos
