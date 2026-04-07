"""Port of DesastresSuccessorFunction1–6.

Each function takes a state (Assignment) and a Board, and yields
``(new_state, description)`` pairs.  For HC (functions 1–5) all
neighbours are generated.  For SA (function 6) a single random
neighbour is produced.
"""

from __future__ import annotations

import random
from typing import Callable, Iterator

from .board import Board
from .state import Assignment, clone_state, swap_groups, reassign_general, reassign_reduced

Successor = tuple[Assignment, str]
SuccessorFn = Callable[[Assignment, Board], Iterator[Successor]]


def sf1_swap(state: Assignment, board: Board) -> Iterator[Successor]:
    """Full SWAP enumeration — O(G^2)."""
    H = len(state)
    for i in range(H):
        for j in range(i, H):
            for k in range(len(state[i])):
                for l in range(len(state[j])):
                    if i != j or k != l:
                        new = clone_state(state)
                        swap_groups(new, i, k, j, l)
                        yield new, f"swap [{i}][{k}] <-> [{j}][{l}]"


def sf2_reassign_general(state: Assignment, board: Board) -> Iterator[Successor]:
    """Reassign any group to any position in any helicopter — O(G*H*G)."""
    H = len(state)
    for i in range(H):
        for j in range(H):
            for k in range(len(state[i])):
                if not state[j]:
                    l = 0
                    if i != j or k != l:
                        new = clone_state(state)
                        reassign_general(new, i, k, j, l)
                        yield new, f"reassign [{i}][{k}] -> [{j}][{l}]"
                else:
                    for l in range(len(state[j])):
                        if i != j or k != l:
                            new = clone_state(state)
                            reassign_general(new, i, k, j, l)
                            yield new, f"reassign [{i}][{k}] -> [{j}][{l}]"


def sf3_reassign_reduced(state: Assignment, board: Board) -> Iterator[Successor]:
    """Move last element of one helicopter to end of another — O(H^2)."""
    H = len(state)
    for i in range(H):
        for j in range(H):
            if i != j and state[i]:
                new = clone_state(state)
                reassign_reduced(new, i, j)
                yield new, f"reduced [{i}] -> [{j}]"


def sf4_swap_general(state: Assignment, board: Board) -> Iterator[Successor]:
    """SWAP + General combined."""
    yield from sf1_swap(state, board)
    yield from sf2_reassign_general(state, board)


def sf5_swap_reduced(state: Assignment, board: Board) -> Iterator[Successor]:
    """SWAP + Reduced combined."""
    yield from sf1_swap(state, board)
    yield from sf3_reassign_reduced(state, board)


def sf6_sa_random(state: Assignment, board: Board) -> Iterator[Successor]:
    """Random single neighbour for Simulated Annealing.

    Randomly picks swap (50 %) or general reassign (50 %).
    Note: the original Java code uses nextInt(2) so the reduced branch
    (choose_op == 2) is dead code.  We replicate that behaviour.
    """
    rng = random.Random()
    H = len(state)
    choose_op = rng.randint(0, 1)

    if choose_op == 0:
        while True:
            i = rng.randint(0, H - 1)
            j = rng.randint(0, H - 1)
            if state[i] and state[j]:
                k = rng.randint(0, len(state[i]) - 1)
                l = rng.randint(0, len(state[j]) - 1)
                if i != j or k != l:
                    break
        new = clone_state(state)
        swap_groups(new, i, k, j, l)
        yield new, f"SA swap [{i}][{k}] <-> [{j}][{l}]"

    else:
        while True:
            i = rng.randint(0, H - 1)
            j = rng.randint(0, H - 1)
            if state[i]:
                k = rng.randint(0, len(state[i]) - 1)
                sz = len(state[j])
                l = rng.randint(0, sz - 1) if sz > 0 else 0
                if i != j or k != l:
                    break
        new = clone_state(state)
        reassign_general(new, i, k, j, l)
        yield new, f"SA reassign [{i}][{k}] -> [{j}][{l}]"


SUCCESSOR_FUNCTIONS: dict[int, SuccessorFn] = {
    1: sf1_swap,
    2: sf2_reassign_general,
    3: sf3_reassign_reduced,
    4: sf4_swap_general,
    5: sf5_swap_reduced,
    6: sf6_sa_random,
}
