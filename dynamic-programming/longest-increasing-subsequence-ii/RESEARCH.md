# Longest Increasing Subsequence II — Research & Foundations

This is LIS under a **window constraint**, which breaks the greedy and forces a
**data-structure-accelerated DP**: a range-max query over the value axis. *Citations verified
against Canad. J. Math / Discrete Math / Software: Practice & Experience / CMU technical-report
records this session — not from memory.*

- **J. L. Bentley (1977), "Solutions to Klee's rectangle problems."** Unpublished technical
  report / manuscript, Department of Computer Science, Carnegie-Mellon University, Pittsburgh.
  The origin of the **segment tree** — the structure that answers the range-max query in
  O(log M) and turns this DP from O(n²) into O(n log M). (Widely cited as the segment tree's
  first appearance; it was never formally published, so it is cited as a CMU technical report —
  see de Berg et al., *Computational Geometry*, for the standard modern treatment.)

- **P. M. Fenwick (1994), "A New Data Structure for Cumulative Frequency Tables,"** *Software:
  Practice and Experience* 24(3):327–336. DOI: 10.1002/spe.4380240306. The **binary indexed
  tree**, the common alternative to a segment tree for this problem.
  https://doi.org/10.1002/spe.4380240306

- **C. Schensted (1961), "Longest increasing and decreasing subsequences,"** *Canadian Journal of
  Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3, and **M. L. Fredman (1975)**, *Discrete
  Mathematics* 11(1):29–35. The LIS lineage — and the O(n log n) **patience sorting** algorithm
  that this problem's window constraint **invalidates**.
  https://doi.org/10.4153/CJM-1961-015-3

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The recurrence
  `dp[x] = 1 + max(dp over a window)` is Bellman DP; the segment tree only *accelerates the
  max*, it doesn't change the recurrence.

**Why it matters here:** plain LIS achieves O(n log n) via **patience sorting**, which is valid
only because *a smaller tail is always at least as extendable*. The window constraint destroys
that: a smaller tail can fall **below** the window's lower bound and be **useless**. So the
greedy is not merely weaker here — its central assumption is **backwards**.

Sub-quadratic time is recovered through a **different structural lever**: the eligible
predecessors form a **contiguous interval on the value axis**, and contiguous intervals admit
**range-max queries**. Hence the segment tree.

> **The generalisable lesson:** "can I beat O(n²)?" has at least two distinct answers —
> exploit **poset dimension** (patience sorting: [LIS #300](#dynamic-programming/longest-increasing-subsequence),
> [Russian Doll #354](#dynamic-programming/russian-doll-envelopes)), or exploit **interval
> structure** (range-max query: this problem). And sometimes neither lever exists —
> [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) is stuck at
> O(n²).

---

## A Note on Transitivity (the "Longest Upgrade Path" variant)

The per-element-window variant (upgrade `j → i` iff `m[i] ≤ v[j] < v[i]`) has a relation that is
**not transitive**: `1 → 5` and `5 → 10` can both be legal while `1 → 10` is not. It is therefore
a **longest path in a DAG**, *not* a longest chain in a **poset**.

That distinction matters for what theory applies: **Dilworth (1950)** and **Mirsky (1971)** —
the chain/antichain duality used to justify patience sorting in
[LIS #300](#dynamic-programming/longest-increasing-subsequence) — require a genuine *partial
order*, and simply **do not apply** here. The DP is unaffected, because longest-path-in-a-DAG
(*linearize, then relax*) never required transitivity in the first place.

**Related in this repo:** [LIS #300](#dynamic-programming/longest-increasing-subsequence),
[Number of LIS #673](#dynamic-programming/number-of-lis),
[Russian Doll #354](#dynamic-programming/russian-doll-envelopes),
[Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset), and
[Parallel Courses III #2050](#graphs/parallel-courses-iii) (weighted longest path in a DAG).
