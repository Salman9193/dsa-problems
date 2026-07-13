# Longest Increasing Subsequence — Research & Foundations

LIS in O(n log n) via patience sorting + binary search, with deep combinatorial roots (Young tableaux, the RSK correspondence).

- **C. Schensted (1961), “Longest increasing and decreasing subsequences,”** *Canadian Journal of Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3. The combinatorial foundation (via Young tableaux / the RSK correspondence) of LIS. https://doi.org/10.4153/CJM-1961-015-3
- **M. L. Fredman (1975), “On computing the length of longest increasing subsequences,”** *Discrete Mathematics* 11(1):29–35. Analyses the O(n log n) patience-sorting LIS algorithm (which Fredman credits to Knuth). https://doi.org/10.1016/0012-365X(75)90103-X
- **D. Aldous & P. Diaconis (1999), “Longest increasing subsequences: from patience sorting to the Baik–Deift–Johansson theorem,”** *Bulletin of the AMS* 36(4):413–432. Ties the O(n log n) LIS algorithm to **patience sorting**. https://doi.org/10.1090/S0273-0979-99-00796-X

**Why it matters here:** The O(n log n) method is patience sorting (Fredman 1975, Aldous–Diaconis 1999); Schensted (1961) is the combinatorial foundation via increasing/decreasing subsequences and tableaux.

---

## Order Theory — Why Patience Sorting Is *Optimal*

Model the sequence as a poset: `(i, nums[i]) ≺ (j, nums[j])` iff `i < j` **and** `nums[i] < nums[j]`. Then a **chain** is an increasing subsequence and an **antichain** is a decreasing one — so **LIS = the longest chain**. Two dual theorems explain why the greedy is optimal:

- **L. Mirsky (1971), “A dual of Dilworth's decomposition theorem,”** *American Mathematical Monthly* 78(8):876–877. DOI: 10.2307/2316481. *Longest chain = the minimum number of antichains partitioning the poset.* Here: **LIS length = the minimum number of decreasing subsequences covering the sequence.** Each patience **pile is a decreasing subsequence**, so patience sorting is a **constructive proof of Mirsky's theorem** — which is precisely why the pile count is optimal and not merely a good heuristic. https://doi.org/10.2307/2316481

- **R. P. Dilworth (1950), “A Decomposition Theorem for Partially Ordered Sets,”** *Annals of Mathematics* 51(1):161–166. DOI: 10.2307/1969503. The **dual** statement: *largest antichain = the minimum number of chains covering the poset* — i.e. the longest **decreasing** subsequence equals the minimum number of **increasing** subsequences needed to cover. https://doi.org/10.2307/1969503

  > **Which theorem applies depends on poset orientation.** With comparability defined as `i < j` **and** `nums[i] < nums[j]`, the LIS is the longest *chain* and **Mirsky's** theorem applies. With the reversed relation (`i < j` and `nums[i] > nums[j]`), the LIS is the largest *antichain* and **Dilworth's** theorem applies. Both give the identical conclusion — LIS = the minimum number of decreasing subsequences covering the sequence — so citing either is legitimate; they are duals.

- **What these theorems actually do:** they compute nothing. They provide a **min-max duality** certifying that the greedy is optimal. Patience sorting yields `k` piles; since each pile is decreasing, an increasing subsequence takes at most one element per pile, so **LIS ≤ k** (pigeonhole); and the back-pointers construct an increasing subsequence of length exactly `k`, so **LIS ≥ k**. The piles are simultaneously an answer *and* a certificate of optimality — the same structure as an LP dual or a min-cut certifying a max-flow.

- **P. Erdős & G. Szekeres (1935), “A combinatorial problem in geometry,”** *Compositio Mathematica* 2:463–470. Any sequence of `(r−1)(s−1)+1` distinct reals contains an increasing subsequence of length `r` or a decreasing one of length `s`. It is a **two-line corollary of the chain/antichain duality**: if the LIS were ≤ `r−1`, Mirsky covers the sequence with ≤ `r−1` decreasing subsequences, and pigeonhole forces one of length ≥ `s`. http://eudml.org/doc/88611

**Related in this repo:** [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) is the same **longest chain in a poset** problem, with divisibility as the partial order.

*Citations verified against Canad. J. Math / Discrete Math / Bull. AMS / Annals of Mathematics / Amer. Math. Monthly / Compositio Mathematica records this session — not from memory.*
