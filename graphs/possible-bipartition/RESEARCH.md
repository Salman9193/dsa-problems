# Possible Bipartition — Research & Foundations

Splitting people into two groups under ‘dislike’ constraints — a **bipartiteness test** (2-colouring) on the dislike graph.

- **Bipartite characterisation (2-colourability).** A graph is bipartite iff it contains no odd cycle; testing reduces to a BFS/DFS **2-colouring**. This classical result traces to D. Kőnig’s foundational graph theory (1930s). Overview: https://en.wikipedia.org/wiki/Bipartite_graph
- **E. F. Moore (1959), “The shortest path through a maze,”** *Proceedings of the International Symposium on Switching Theory 1957*, Part II, pp. 285–292, Harvard University Press. The origin of **breadth-first search** — exploring a grid/graph in layers to find shortest paths in unweighted graphs.

**Why it matters here:** A valid split exists iff the dislike graph is bipartite; BFS/DFS 2-colouring finds it or exposes the odd cycle that makes it impossible.

*BFS (Moore 1959) and DFS (Tarjan 1972) citations verified against published records this session — not from memory.*
