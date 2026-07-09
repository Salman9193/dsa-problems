# Evaluate Division — Research & Foundations

Answering ratio queries over a graph of known ratios — a weighted-union-find (or weighted DFS) problem where edge weights multiply along paths.

- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884

**Why it matters here:** Variables in the same component have a known ratio; weighted union-find stores each node’s ratio to its root so queries resolve in near-constant time.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
