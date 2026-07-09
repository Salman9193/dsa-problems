# LCA Deepest Leaves — Research & Foundations

The LCA of a tree’s deepest leaves — a single-pass variant returning both a subtree’s depth and its deepest leaves’ common ancestor.

- **D. Harel & R. E. Tarjan (1984), “Fast algorithms for finding nearest common ancestors,”** *SIAM Journal on Computing* 13(2):338–355. DOI: 10.1137/0213024. Constant-time LCA queries after linear preprocessing. https://doi.org/10.1137/0213024
- **M. A. Bender & M. Farach-Colton (2000), “The LCA problem revisited,”** *LATIN 2000*, LNCS 1776, pp. 88–94, Springer. A simplified O(1)-query LCA via reduction to range-minimum query — the practical modern approach.

**Why it matters here:** It folds an LCA computation into one DFS; the general LCA problem it specialises has O(1)-query solutions after linear preprocessing (Harel–Tarjan; Bender–Farach-Colton).

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
