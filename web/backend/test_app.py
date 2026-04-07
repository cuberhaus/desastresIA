"""Tests for the desastresIA FastAPI backend."""

import pytest
from fastapi.testclient import TestClient

from .app import app

client = TestClient(app)


def test_status():
    r = client.get("/api/status")
    assert r.status_code == 200
    assert r.json()["status"] == "ok"


def test_generate_default():
    r = client.post("/api/generate", json={})
    assert r.status_code == 200
    data = r.json()
    assert "centros" in data
    assert "grupos" in data
    assert len(data["centros"]) == 5
    assert len(data["grupos"]) == 50


def test_generate_custom():
    r = client.post("/api/generate", json={
        "seed": 42, "n_grupos": 10, "n_centros": 2, "n_helicopters_per_center": 2,
    })
    assert r.status_code == 200
    data = r.json()
    assert len(data["centros"]) == 2
    assert len(data["grupos"]) == 10


def test_generate_deterministic():
    payload = {"seed": 99, "n_grupos": 20, "n_centros": 3}
    r1 = client.post("/api/generate", json=payload)
    r2 = client.post("/api/generate", json=payload)
    assert r1.json() == r2.json()


def test_generate_centro_has_fields():
    r = client.post("/api/generate", json={"n_centros": 1, "n_grupos": 2})
    c = r.json()["centros"][0]
    assert "x" in c and "y" in c and "n_helicopters" in c


def test_generate_grupo_has_fields():
    r = client.post("/api/generate", json={"n_centros": 1, "n_grupos": 2})
    g = r.json()["grupos"][0]
    assert "x" in g and "y" in g and "priority" in g and "n_personas" in g


def test_solve_hc():
    r = client.post("/api/solve", json={
        "seed": 42, "n_grupos": 10, "n_centros": 2,
        "algorithm": "hc", "successor_fn": 5, "heuristic_fn": 1,
    })
    assert r.status_code == 200
    data = r.json()
    assert "assignment" in data
    assert "heuristic_initial" in data
    assert "heuristic_final" in data
    assert "exec_time_ms" in data
    assert data["heuristic_final"] <= data["heuristic_initial"]


def test_solve_sa():
    r = client.post("/api/solve", json={
        "seed": 42, "n_grupos": 10, "n_centros": 2,
        "algorithm": "sa", "sa_steps": 500, "sa_stiter": 2,
    })
    assert r.status_code == 200
    data = r.json()
    assert "assignment" in data
    assert "trace" in data
    assert len(data["trace"]) > 0


def test_solve_returns_routes():
    r = client.post("/api/solve", json={
        "seed": 42, "n_grupos": 5, "n_centros": 2,
    })
    data = r.json()
    assert "routes" in data
    assert "helicopter_times" in data


def test_experiment_empty_configs():
    r = client.post("/api/experiment", json={
        "seed_start": 1, "seed_end": 2, "configs": [],
    })
    assert r.status_code == 400


def test_experiment():
    r = client.post("/api/experiment", json={
        "seed_start": 1, "seed_end": 3,
        "n_grupos": 10, "n_centros": 2,
        "configs": [
            {"label": "hc_test", "algorithm": "hc", "successor_fn": 5, "heuristic_fn": 1},
        ],
    })
    assert r.status_code == 200
    data = r.json()
    assert "configs" in data
    assert "results" in data
    assert "hc_test" in data["results"]
    assert len(data["results"]["hc_test"]) == 2
