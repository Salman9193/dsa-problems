# Path Minimum Effort — Research & Foundations

A minimax (bottleneck) path problem: minimise the largest single step. Solved by a Dijkstra-style relaxation that carries the path’s max edge instead of its sum.

- **E. W. Dijkstra (1959), “A note on two problems in connexion with graphs,”** *Numerische Mathematik* 1:269–271. DOI: 10.1007/BF01386390. Introduces the shortest-path algorithm (Problem 2) — and, in the same paper, an MST algorithm (Problem 1). https://doi.org/10.1007/BF01386390

**Why it matters here:** Replacing ‘sum of weights’ with ‘max weight so far’ in Dijkstra’s relaxation yields the minimum-effort (bottleneck) path.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
