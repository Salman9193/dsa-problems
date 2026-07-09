# Clone Graph — Research & Foundations

Deep-copying a graph — a standard **graph traversal** (BFS or DFS) that visits every node once while mapping originals to copies.

- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.
- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. The linear-time **depth-first search** foundations, including connected-component identification.

**Why it matters here:** A single BFS/DFS with a visited-map builds the clone: create each node on first visit, then wire up its neighbours — a direct use of graph traversal.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
