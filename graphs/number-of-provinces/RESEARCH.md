# Number Of Provinces — Research & Foundations

Counting connected components of an undirected graph — the canonical union-find (or DFS) task.

- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884

**Why it matters here:** Each province is a connected component; union-find merges directly-connected cities and the number of distinct roots is the answer.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
