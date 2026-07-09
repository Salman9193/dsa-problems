# Redundant Connection — Research & Foundations

Find the edge that creates a cycle in a graph built by adding edges one at a time — an incremental cycle-detection problem solved with union-find.

- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884

**Why it matters here:** Adding an edge whose endpoints are already in the same set closes a cycle; union-find detects that first offending edge in near-linear time.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
