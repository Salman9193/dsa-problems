# Find If Path Exists — Research & Foundations

A single connectivity query between two vertices — answerable by union-find (are they in the same set?) or a BFS/DFS reachability search.

- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884
- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of breadth-first search for shortest paths in unweighted graphs.

**Why it matters here:** Whether a path exists is a same-component test (union-find) or a traversal from source to target (BFS/DFS).

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
