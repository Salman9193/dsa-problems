# Critical Connections — Research & Foundations

Finding all bridges (edges whose removal disconnects the graph) — solved by Tarjan’s DFS-based bridge/biconnected-components algorithm using discovery and low-link times.

- **R. E. Tarjan (1972), “Depth-first search and linear graph algorithms,”** *SIAM Journal on Computing* 1(2):146–160. Linear-time DFS foundations — cycle detection, strongly connected components, and bridges / biconnected components.

**Why it matters here:** A bridge is an edge with no back-edge alternative; Tarjan’s linear-time DFS with low-link values identifies exactly these critical connections.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
