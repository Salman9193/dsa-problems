# Kth Largest Element — Research & Foundations

Finding the k-th largest element without fully sorting — the **selection problem**, solved by Quickselect (O(n) average) or median-of-medians (O(n) worst case).

- **C. A. R. Hoare (1961), “Algorithm 65: Find,”** *Communications of the ACM* 4(7):321–322. **Quickselect** — find the k-th smallest by partitioning around a pivot and recursing into one side, O(n) average.
- **M. Blum, R. W. Floyd, V. Pratt, R. L. Rivest & R. E. Tarjan (1973), “Time bounds for selection,”** *Journal of Computer and System Sciences* 7(4):448–461. DOI: 10.1016/S0022-0000(73)80033-9. The **median-of-medians (BFPRT)** algorithm — worst-case O(n) selection. https://doi.org/10.1016/S0022-0000(73)80033-9

**Why it matters here:** Quickselect (Hoare 1961) partitions and recurses into one side for O(n) average; BFPRT (1973) guarantees O(n) worst case — the two classic answers to this problem.

*Selection / 3SUM citations verified against JCSS / Computational Geometry / CACM records this session — not from memory.*
