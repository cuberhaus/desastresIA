"""FastAPI application for desastresIA web solver."""

from __future__ import annotations

import asyncio
import os
from concurrent.futures import ProcessPoolExecutor, ThreadPoolExecutor
from dataclasses import asdict
from pathlib import Path
from typing import Literal

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel, Field

from .board import Board
from .generator import generate_centros, generate_grupos
from .solver import hill_climbing, run_experiment_task, simulated_annealing
from .state import make_initial_state

_NUM_WORKERS = max(1, min(os.cpu_count() or 4, 8))
_thread_pool = ThreadPoolExecutor(max_workers=2)

app = FastAPI(title="desastresIA")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


# ── request / response models ────────────────────────────────────────

class SolveRequest(BaseModel):
    seed: int = 1000
    n_grupos: int = Field(50, ge=2, le=500)
    n_centros: int = Field(5, ge=1, le=20)
    n_helicopters_per_center: int = Field(1, ge=1, le=5)
    algorithm: Literal["hc", "sa"] = "hc"
    successor_fn: int = Field(5, ge=1, le=6)
    heuristic_fn: int = Field(1, ge=1, le=3)
    initial_state: Literal["random", "all_to_one", "greedy"] = "greedy"
    sa_steps: int = Field(20000, ge=100, le=500000)
    sa_stiter: int = Field(5, ge=1, le=50)
    sa_k: int = Field(125, ge=1, le=10000)
    sa_lambda: float = Field(1.0, ge=0.01, le=100.0)


class ExperimentConfig(BaseModel):
    label: str
    algorithm: Literal["hc", "sa"] = "hc"
    successor_fn: int = Field(4, ge=1, le=6)
    heuristic_fn: int = Field(1, ge=1, le=3)
    sa_steps: int = 20000
    sa_stiter: int = 5
    sa_k: int = 125
    sa_lambda: float = 1.0


class ExperimentRequest(BaseModel):
    seed_start: int = 1000
    seed_end: int = 1005
    n_grupos: int = Field(50, ge=2, le=500)
    n_centros: int = Field(5, ge=1, le=20)
    n_helicopters_per_center: int = Field(1, ge=1, le=5)
    initial_state: Literal["random", "all_to_one", "greedy"] = "greedy"
    configs: list[ExperimentConfig] = Field(default_factory=list)


# ── endpoints ─────────────────────────────────────────────────────────

@app.get("/api/status")
async def status():
    return {"status": "ok"}


class GenerateRequest(BaseModel):
    seed: int = 1000
    n_grupos: int = Field(50, ge=2, le=500)
    n_centros: int = Field(5, ge=1, le=20)
    n_helicopters_per_center: int = Field(1, ge=1, le=5)


@app.post("/api/generate")
async def generate(req: GenerateRequest):
    centros = generate_centros(req.n_centros, req.n_helicopters_per_center, req.seed)
    grupos = generate_grupos(req.n_grupos, req.seed)
    return {
        "centros": [{"x": c.x, "y": c.y, "n_helicopters": c.n_helicopters} for c in centros],
        "grupos": [
            {"x": g.x, "y": g.y, "priority": g.priority, "n_personas": g.n_personas}
            for g in grupos
        ],
    }


@app.post("/api/solve")
async def solve(req: SolveRequest):
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(_thread_pool, _run_solve, req)
    return result


@app.post("/api/experiment")
async def experiment(req: ExperimentRequest):
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(_thread_pool, _run_experiment, req)
    return result


# ── blocking solve logic (runs in thread pool) ───────────────────────

def _run_solve(req: SolveRequest) -> dict:
    centros = generate_centros(req.n_centros, req.n_helicopters_per_center, req.seed)
    grupos = generate_grupos(req.n_grupos, req.seed)
    board = Board(centros, grupos)
    initial = make_initial_state(board, req.initial_state, seed=req.seed)

    if req.algorithm == "hc":
        result = hill_climbing(board, initial, req.successor_fn, req.heuristic_fn)
    else:
        result = simulated_annealing(
            board, initial, req.heuristic_fn,
            steps=req.sa_steps, stiter=req.sa_stiter,
            k=req.sa_k, lambda_=req.sa_lambda,
        )

    return {
        "centros": [{"x": c.x, "y": c.y, "n_helicopters": c.n_helicopters} for c in centros],
        "grupos": [
            {"x": g.x, "y": g.y, "priority": g.priority, "n_personas": g.n_personas}
            for g in grupos
        ],
        "assignment": result.assignment,
        "heuristic_initial": round(result.heuristic_initial, 2),
        "heuristic_final": round(result.heuristic_final, 2),
        "exec_time_ms": round(result.exec_time_ms, 1),
        "nodes_expanded": result.nodes_expanded,
        "helicopter_times": [round(t, 2) for t in result.helicopter_times],
        "routes": [
            [asdict(seg) for seg in heli_route]
            for heli_route in result.routes
        ],
        "trace": [round(v, 2) for v in result.trace],
    }


def _run_experiment(req: ExperimentRequest) -> dict:
    if not req.configs:
        raise HTTPException(400, "At least one config is required")

    tasks: list[tuple] = []
    for seed in range(req.seed_start, req.seed_end):
        for cfg in req.configs:
            cfg_dict = {
                "label": cfg.label,
                "algorithm": cfg.algorithm,
                "successor_fn": cfg.successor_fn,
                "heuristic_fn": cfg.heuristic_fn,
                "sa_steps": cfg.sa_steps,
                "sa_stiter": cfg.sa_stiter,
                "sa_k": cfg.sa_k,
                "sa_lambda": cfg.sa_lambda,
            }
            tasks.append((
                seed, cfg_dict, req.n_centros,
                req.n_helicopters_per_center, req.n_grupos, req.initial_state,
            ))

    results: dict[str, list[dict]] = {cfg.label: [] for cfg in req.configs}

    with ProcessPoolExecutor(max_workers=_NUM_WORKERS) as pool:
        for row in pool.map(run_experiment_task, tasks):
            results[row["label"]].append({
                "seed": row["seed"],
                "heuristic_final": row["heuristic_final"],
                "exec_time_ms": row["exec_time_ms"],
                "nodes_expanded": row["nodes_expanded"],
            })

    return {"configs": [c.label for c in req.configs], "results": results}


# ── static file serving (production) ─────────────────────────────────

STATIC_DIR = Path(__file__).resolve().parent.parent / "frontend" / "dist"

if STATIC_DIR.is_dir():
    app.mount("/", StaticFiles(directory=str(STATIC_DIR), html=True), name="static")
