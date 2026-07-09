# Pacific Atlantic Water Flow — Research & Foundations

Cells that can reach both oceans — **multi-source reachability** via BFS/DFS from each ocean’s border, then intersection.

- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.
- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. The linear-time **depth-first search** foundations, including connected-component identification.

**Why it matters here:** Flooding inward from each ocean’s edges marks all cells that can drain to it; the answer is the intersection of the two reachable sets.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
