# Find Eventual Safe States — Research & Foundations

Identifying nodes that cannot reach a cycle — a cycle-detection / topological-peeling computation on a directed graph.

- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. Linear-time DFS foundations — cycle detection, strongly connected components, and bridges / biconnected components.
- **A. B. Kahn (1962), “Topological sorting of large networks,”** *Communications of the ACM* 5(11):558–562. The in-degree / queue topological-sort algorithm.

**Why it matters here:** A node is safe iff every path from it terminates; DFS colouring (or topological peeling of the reverse graph) separates safe nodes from those trapped in or leading to cycles.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
