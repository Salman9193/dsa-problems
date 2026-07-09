# Number Of Islands — Research & Foundations

Counting connected components on a grid — the classic **flood fill**, implemented as BFS or DFS over adjacent land cells.

- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.
- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. The linear-time **depth-first search** foundations, including connected-component identification.

**Why it matters here:** Each island is a connected component of ‘land’ cells; a BFS/DFS flood fill from each unvisited land cell counts them.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
