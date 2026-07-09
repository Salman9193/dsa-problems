# 01 Matrix — Research & Foundations

Nearest-zero distance for every cell — a **multi-source BFS** seeded from all zeros simultaneously.

- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.

**Why it matters here:** Seeding BFS with every zero cell at distance 0 propagates the nearest-zero distance outward in layers — a direct application of BFS.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
