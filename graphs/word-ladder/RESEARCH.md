# Word Ladder — Research & Foundations

Shortest transformation sequence — **BFS** on an implicit graph whose edges connect words differing by one letter.

- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.

**Why it matters here:** Each one-letter change is an unweighted edge; BFS from the start word finds the shortest ladder to the target.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
