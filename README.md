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

## Tech Stack

- **Java** with the AIMA library for search algorithms
- **Python** (matplotlib, pandas, numpy) for experiment visualization
