# Is Graph Bipartite — Research & Foundations

Testing 2-colourability — a BFS/DFS that attempts to two-colour the graph, failing exactly when an odd cycle exists.

- **Bipartite characterisation (2-colourability).** A graph is bipartite iff it contains no odd cycle; testing reduces to a BFS/DFS **2-colouring**. This classical result traces to D. Kőnig’s foundational graph theory (1930s). Overview: https://en.wikipedia.org/wiki/Bipartite_graph
- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.

**Why it matters here:** The graph is bipartite iff a BFS/DFS 2-colouring assigns no edge two same-coloured endpoints — the odd-cycle characterisation in action.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
