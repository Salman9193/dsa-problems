# Longest Increasing Subsequence — Research & Foundations

LIS in O(n log n) via patience sorting + binary search, with deep combinatorial roots (Young tableaux, the RSK correspondence).

- **C. Schensted (1961), “Longest increasing and decreasing subsequences,”** *Canadian Journal of Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3. The combinatorial foundation (via Young tableaux / the RSK correspondence) of LIS. https://doi.org/10.4153/CJM-1961-015-3
- **M. L. Fredman (1975), “On computing the length of longest increasing subsequences,”** *Discrete Mathematics* 11(1):29–35. Analyses the O(n log n) patience-sorting LIS algorithm (which Fredman credits to Knuth). https://doi.org/10.1016/0012-365X(75)90103-X
- **D. Aldous & P. Diaconis (1999), “Longest increasing subsequences: from patience sorting to the Baik–Deift–Johansson theorem,”** *Bulletin of the AMS* 36(4):413–432. Ties the O(n log n) LIS algorithm to **patience sorting**. https://doi.org/10.1090/S0273-0979-99-00796-X

**Why it matters here:** The O(n log n) method is patience sorting (Fredman 1975, Aldous–Diaconis 1999); Schensted (1961) is the combinatorial foundation via increasing/decreasing subsequences and tableaux.

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
