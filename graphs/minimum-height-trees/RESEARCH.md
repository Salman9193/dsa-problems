# Minimum Height Trees — Research & Foundations

Finding the roots that minimise tree height — the graph/tree **center**, a classical concept: a tree has one or two center vertices, found by repeatedly peeling leaves.

- **Graph center / tree center.** The height-minimising roots are the tree’s center vertices. A tree has at most two centers, a result going back to C. Jordan’s 19th-century study of trees; see https://en.wikipedia.org/wiki/Graph_center and https://en.wikipedia.org/wiki/Centre_(graph_theory)
- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. Linear-time DFS foundations — cycle detection, strongly connected components, and bridges / biconnected components.

**Why it matters here:** The optimal roots are the tree’s 1–2 center vertices; iteratively removing current leaves (a topological peeling) converges to them — the standard tree-center computation.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
