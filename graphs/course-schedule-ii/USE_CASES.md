# Course Schedule II — Real-World Use Cases

Course Schedule II returns the actual topological order — the "in what order?"
answer that real systems need. Every production use case of topological sort
needs the order, not just the feasibility check.

---

## 1. Parallel Job Scheduling — Build Levels and Critical Path

The Kahn's BFS levels in Course Schedule II directly model parallel scheduling:
each BFS level = one round of tasks that can execute simultaneously because all
their dependencies are already complete.

Job scheduling with precedence constraints arises in build systems,
manufacturing cells, and distributed execution frameworks. Given a DAG whose
vertices represent jobs and edges encode prerequisites, the objective is to
compute each job's earliest completion time. The standard sequential approach
is a topological sort (Kahn's algorithm): process jobs in dependency order,
computing each completion time from the maximum of its predecessors. The
parallel variant processes one topological level per round, advancing all jobs
in a level simultaneously before starting the next.

```
Course Schedule II levels:        Parallel scheduler:
  Level 0: courses with inDegree=0  ↔  jobs with no prerequisites (start immediately)
  Level 1: unlocked after Level 0   ↔  jobs ready after round 1 completes
  Level k: minimum k levels deep    ↔  earliest start time = round k
  Number of levels                  ↔  minimum makespan (total rounds)
```

### Reference

- **arXiv:2603.13147 — A common parallel framework for LLP combinatorial problems:**
  https://arxiv.org/pdf/2603.13147
  "The standard sequential approach is a topological sort (Kahn's algorithm):
  process jobs in dependency order, computing each completion time from the
  maximum of its predecessors. The parallel variant processes one topological
  level per round, advancing all jobs in a level simultaneously."

---

## 2. Build Systems — Compilation Order

Every build system needs the actual compilation order, not just cycle detection.
Knowing "can we build?" (Course Schedule I) is insufficient — the build system
needs to know which module to compile first, second, third.

Tools like Make, Bazel, Gradle, and npm topologically sort their build targets
so each target is compiled only after all its dependencies. The topological
order IS the build order.

### Kahn's vs DFS in production build systems

Both DFS postorder and Kahn's algorithm produce valid topological orders, but
they differ in how they handle the process internally. The DFS version relies
on recursion and naturally builds the order by working backward through
completed calls. Kahn's algorithm uses an iterative queue-driven process that
starts from nodes with no incoming edges — preferred in production for its
explicit cycle detection and iterative (non-recursive) nature.

The DFS approach is efficient for dependency graphs in build systems (Make,
CMake), scheduling (OS process handling), and function call ordering in
compilers. The recursive approach naturally ensures that each node is
processed after its dependencies.

### References

- **Managing Dependencies with Topological Sorting in Java (Medium):**
  https://medium.com/@AlexanderObregon/managing-dependencies-with-topological-sorting-in-java-956a026a90d3
  "Both DFS and Kahn's algorithm produce valid topological orders. The DFS
  version relies on recursion and works backward through completed calls.
  Kahn's uses an iterative queue-driven process starting from nodes with no
  incoming edges."

- **Topological Sort Calculator (Kahn's + DFS):**
  https://miniwebtool.com/topological-sort-calculator/
  "Kahn's lexicographic gives a unique, reproducible order; insertion order
  preserves input ordering; DFS post-order gives the classic depth-first method.
  The parallel variant processes one topological level per round."

- **Topological Sorting in DAGs (upGrad):**
  https://www.upgrad.com/blog/topological-sorting-in-dags/
  "Efficient for dependency graphs in build systems (Make, CMake), scheduling
  (OS process handling), and function call ordering in compilers. Recursive
  approach naturally ensures each node is processed after its dependencies."

---

## 3. Package Installation Order — npm, pip, apt

Package managers don't just detect circular dependencies — they produce the
actual installation order. npm resolves the dependency tree and installs
packages in reverse topological order (deepest dependencies first):

```
npm install react
  → resolve: react depends on [loose-cannon, js-tokens, ...]
  → topological sort of dependency DAG
  → install in order: js-tokens first, then loose-cannon, then react
```

The installation sequence IS the topological order from Course Schedule II.

---

## 4. Spreadsheet Recalculation Order

When a cell's value changes, the spreadsheet computes the topological order of
all affected downstream cells and recomputes them in that order:

```
A1 = 5
B1 = A1 * 2    (depends on A1)
C1 = B1 + A1   (depends on A1 and B1)
D1 = C1 / 2    (depends on C1)
```

When A1 changes: topological order = [A1, B1, C1, D1].
Recompute in this exact sequence — computing C1 before B1 would give stale results.

This is Course Schedule II applied to cell dependencies — the order matters
as much as the feasibility.

---

## 5. Kubernetes Deployment Ordering

Kubernetes startup ordering (init containers, readiness probes, service
dependencies) requires the actual startup sequence, not just cycle detection.

Service mesh orchestration determines which microservices start first based
on their declared dependencies — the topological order of the service graph.
A service that depends on a database must start after the database is ready.

---

## Kahn's BFS Levels — The Parallelism Bonus

The Kahn's BFS level structure gives one extra piece of information beyond
the ordering: the **minimum number of parallel rounds** (semesters, sprints,
build stages) needed:

```
Level 0: {0}    → Semester 1 (take course 0)
Level 1: {1,2}  → Semester 2 (take 1 and 2 simultaneously)
Level 2: {3}    → Semester 3 (take 3)
→ Minimum 3 semesters regardless of how many courses per semester
```

This extends directly to:
- **#1136 Parallel Courses** — minimum weeks with unlimited parallel execution
- **PERT/CPM project scheduling** — critical path = longest chain of dependencies
- **CI/CD pipeline stages** — how many stages minimum before shipping

---

## Summary

| Domain | Node = | Edge = | Topological order = |
|--------|--------|--------|---------------------|
| Course Schedule II (#210) | Course | Prerequisite | Valid study plan |
| Build system | Module | Dependency | Compilation sequence |
| Package manager | Package | Dependency | Installation order |
| Spreadsheet | Cell | Reference | Recomputation sequence |
| Kubernetes | Service | Startup dependency | Deployment order |
| Parallel scheduler | Job | Prerequisite | Earliest-start schedule |

---

## Further Reading

- Parallel job scheduling (arXiv): https://arxiv.org/pdf/2603.13147
- Topological sort calculator: https://miniwebtool.com/topological-sort-calculator/
- Managing dependencies in Java: https://medium.com/@AlexanderObregon/managing-dependencies-with-topological-sorting-in-java-956a026a90d3
- Graph algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
- Course Schedule I (#207): see `graphs/course-schedule/`
