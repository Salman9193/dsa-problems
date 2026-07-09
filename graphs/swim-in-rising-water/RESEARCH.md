# Swim In Rising Water — Research & Foundations

A minimax-path problem on a grid: minimise the maximum cell value on a route. Solved by Dijkstra-on-the-bottleneck, or by union-find with increasing thresholds.

- **E. W. Dijkstra (1959), “A note on two problems in connexion with graphs,”** *Numerische Mathematik* 1:269–271. DOI: 10.1007/BF01386390. Introduces the shortest-path algorithm (Problem 2) — and, in the same paper, an MST algorithm (Problem 1). https://doi.org/10.1007/BF01386390
- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884

**Why it matters here:** The earliest time you can cross is the minimum over paths of the max cell — a bottleneck shortest path (Dijkstra) or an incremental-connectivity query (union-find).

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
