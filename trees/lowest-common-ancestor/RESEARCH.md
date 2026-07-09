# Lowest Common Ancestor — Research & Foundations

The deepest node that is an ancestor of two given nodes. One query is a simple O(n) walk; the theory is about answering *many* queries fast after preprocessing.

- **R. E. Tarjan (1979), “Applications of path compression on balanced trees,”** *Journal of the ACM* 26(4):690–715. Includes the classic **offline LCA** algorithm via union-find.
- **D. Harel & R. E. Tarjan (1984), “Fast algorithms for finding nearest common ancestors,”** *SIAM Journal on Computing* 13(2):338–355. DOI: 10.1137/0213024. Constant-time LCA queries after linear preprocessing. https://doi.org/10.1137/0213024
- **M. A. Bender & M. Farach-Colton (2000), “The LCA problem revisited,”** *LATIN 2000*, LNCS 1776, pp. 88–94, Springer. A simplified O(1)-query LCA via reduction to range-minimum query — the practical modern approach.

**Why it matters here:** Beyond the direct walk, LCA has a rich literature: Tarjan’s offline union-find method (1979) and the Harel–Tarjan / Bender–Farach-Colton O(1)-query-after-linear-preprocessing results (via range-minimum query).

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
