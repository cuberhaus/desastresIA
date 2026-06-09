# desastresIA

FIB-UPC AI coursework: a local-search planner that assigns helicopters to rescue groups of injured people across multiple centers. The Java core uses the AIMA framework to run Hill Climbing and Simulated Annealing over several successor functions, heuristics, and initial-state generators; Python scripts plot experiment results and a small web app exposes an interactive solver.

## Architecture
- Java solver in [Desastres/src/main.java](Desastres/src/main.java) with domain classes under [Desastres/src/Desastres/](Desastres/src/Desastres/) (`board`, `estado`, `Helicopter`, `DesastresGoalTest`, `DesastresHeuristicFunction[1-3]`, `DesastresSuccessorFunction[1-6]`).
- AIMA library bundled as [Desastres/src/AIMA.jar](Desastres/src/AIMA.jar); compiled output at [Desastres/src/Desastres.jar](Desastres/src/Desastres.jar). AIMA sources mirrored under [Aima/](Aima/).
- Experiment tooling in [python_scripts/](python_scripts/) (`plots.py`, `jarToCSV.py`, raw TSV under `csv/`).
- Web app in [web/](web/): FastAPI backend ([web/backend/app.py](web/backend/app.py)) wrapping the Java jar, Solid.js + Canvas frontend in [web/frontend/](web/frontend/).

## Build and Test
- Run solver directly: `java -jar Desastres/src/Desastres.jar [seed] [ngrupos] [ncentros] [nhelicopters] [successorfunc gensolini heuristicfunc] | [lambda k steps stitter gensolini heuristicfunc]`. Selector legend lives in [LEEME.TXT](LEEME.TXT).
- Web stack: `make install`, then `make dev` (backend :8083, Vite frontend) or `make docker-up` for the containerized build at http://localhost:8083.

## Conventions
- Frozen coursework — Spanish identifiers and filenames (`estado`, `gensolini`, `LEEME.TXT`) are intentional; do not rename or "clean up".

## Pitfalls
- Inactive course project; treat as read-only unless explicitly asked to modify.
- The web backend shells out to `Desastres.jar`; keep the jar in place and rebuild it (not just the `.java` sources) when changing solver code.
- Experiments depend on the CLI `seed` argument for reproducibility — always pass it when regenerating TSVs in `python_scripts/csv/`.

See [README.md](README.md) for full setup and [LEEME.TXT](LEEME.TXT) for the original CLI reference.
