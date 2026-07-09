# Course Schedule — Research & Foundations

Detecting whether a dependency graph (a DAG requirement) has a cycle — the feasibility side of topological sorting.

- **A. B. Kahn (1962), “Topological sorting of large networks,”** *Communications of the ACM* 5(11):558–562. The in-degree / queue topological-sort algorithm.
- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. Linear-time DFS foundations — cycle detection, strongly connected components, and bridges / biconnected components.

**Why it matters here:** Courses can be ordered iff the prerequisite graph is acyclic; Kahn’s in-degree peeling (or DFS) detects the cycle that would make it impossible.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
