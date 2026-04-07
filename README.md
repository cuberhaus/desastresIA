# Desastres IA

Local search project for the Artificial Intelligence course (IA) at FIB-UPC. The problem models a disaster-relief scenario where helicopters must be assigned to rescue groups of injured people from multiple centers, optimizing rescue time.

## Overview

The project uses the AIMA (Artificial Intelligence: A Modern Approach) framework to implement and compare local search algorithms:

- **Hill Climbing (HC)** with multiple successor functions (swap, reassign, combined)
- **Simulated Annealing (SA)**

Several heuristic functions are provided to evaluate solutions (weighted total time, priority-based rescue, etc.), along with different initial state generators (random, all-to-one, greedy).

## Structure

```
Desastres/src/
├── main.java                       # Entry point with algorithm/heuristic selectors
├── Desastres/                      # Core domain classes
│   ├── board.java                  # Board state representation
│   ├── estado.java                 # State representation
│   ├── Helicopter.java             # Helicopter model
│   ├── PairDH.java                 # Auxiliary pair structure
│   ├── DesastresGoalTest.java      # Goal test
│   ├── DesastresHeuristicFunction[1-3].java
│   └── DesastresSuccessorFunction[1-6].java
├── AIMA.jar                        # AIMA library
└── Desastres.jar                   # Compiled project
Aima/                               # AIMA library source
python_scripts/
├── plots.py                        # Boxplot generation from experiment data
├── jarToCSV.py                     # Converts JAR experiment output to CSV
├── csv/                            # Raw experiment results (TSV)
├── experimento*/                   # Generated plots per experiment
└── requirements.txt                # Python dependencies
docs/
├── PracticaBusqueda-local.pdf      # Assignment specification
└── javadoc/                        # Generated API documentation
```

## Web App

A full-stack web solver with an interactive 2D map and experiment dashboard — compare hill climbing vs simulated annealing with multiple heuristics and successor functions.

**Stack:** Solid.js (Vite) + HTML5 Canvas + FastAPI backend

### Quick Start

```bash
# Docker (recommended)
docker compose up -d        # http://localhost:8083

# Dev mode
make web-dev                # Backend :8083, Vite dev server
```

### Features

- Interactive 2D map with centers, groups, and helicopter routes rendered on Canvas
- Real-time solver execution with configurable algorithm, heuristic, and successor function
- Experiment dashboard for batch runs with boxplot visualization
- Draggable seed, helicopter count, and center/group parameters

### Web Structure

```
web/
├── frontend/          # Solid.js + Vite + Canvas
│   └── src/
│       ├── components/        # Solver map, controls, experiment dashboard
│       └── styles/            # Dark theme CSS
├── backend/           # FastAPI wrapping Java AIMA engine
│   └── app.py
└── requirements.txt
```

## Tech Stack

- **Java** with the AIMA library for search algorithms
- **Python** (matplotlib, pandas, numpy) for experiment visualization
- **Solid.js** + **Canvas** for the interactive web frontend
- **FastAPI** for the web backend
