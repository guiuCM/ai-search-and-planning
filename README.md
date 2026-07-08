# Artificial Intelligence - Search & Planning

A comprehensive collection of Artificial Intelligence implementations focusing on Heuristic Search, Knowledge-Based Systems, and Automated Planning.

This repository is structured into three distinct modules, each tackling complex decision-making and optimization problems using classical and modern AI paradigms.

## Architecture & Modules

### 1. `01-heuristic-search` (Local Search & Optimization)
Optimization of a logistics and distribution network using Hill Climbing and Simulated Annealing algorithms.
* **Language:** Java
* **Framework:** AIMA (Artificial Intelligence: A Modern Approach)
* **Core Concepts:** State space exploration, Heuristic function design, Combinatorial optimization.
* **Execution:** Compile `src/Main.java` with the included `lib/AIMA.jar` dependency. Run the interactive console to execute different optimization experiments.

### 2. `02-expert-system-clips` (Knowledge-Based Systems)
Development of an expert system capable of making complex inferences based on logical rules, integrated with a custom domain ontology.
* **Language:** CLIPS
* **Core Concepts:** Rule-based architectures, Forward/Backward Chaining, RDF Ontologies.
* **Execution:** Load the `.clp` rule files from `src/` into a CLIPS environment to query the knowledge base.

### 3. `03-automated-planning-pddl` (Automated Planning)
Modeling and solving an automated planning problem for intelligent agents operating within a dynamic hotel environment.
* **Language:** PDDL (Planning Domain Definition Language) / Python
* **Solver:** Metric-FF (`ff.exe` included in `bin/`)
* **Core Concepts:** STRIPS/ADL action schemas, State Transitions, Metric constraints & Resource management.
* **Execution:** Run the FF planner via CLI by passing the domain and generated problem files:
  ```bash
  ./bin/ff.exe -o domains/domini_base.pddl -f problems/problema_base.pddl