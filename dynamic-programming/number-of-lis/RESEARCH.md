# Number of LIS — Research & Foundations

This counts **maximum chains** in the LIS poset. The counting technique — carry a count array,
reset on strict improvement, accumulate on ties — is the standard way to enumerate optimal
solutions of a DP, and it appears identically in shortest-path counting. *Citations verified
this session — not from memory.*

- **C. Schensted (1961), "Longest increasing and decreasing subsequences,"** *Canadian Journal
  of Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3. The combinatorial foundation of
  increasing subsequences (Young tableaux / RSK), the object being counted here.
  https://doi.org/10.4153/CJM-1961-015-3

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The `len[]`
  recurrence is Bellman DP; the parallel `cnt[]` array is the standard *solution-counting*
  extension of a DP — the same pattern used to count shortest paths.

- **R. P. Dilworth (1950), *Annals of Mathematics* 51(1):161–166 (DOI: 10.2307/1969503)** and
  **L. Mirsky (1971), *Amer. Math. Monthly* 78(8):876–877 (DOI: 10.2307/2316481).** The
  chain/antichain duality that makes "longest chain" the right frame: this problem counts the
  **maximum chains** of that poset. https://doi.org/10.2307/1969503

**Why it matters here:** the LIS is the longest **chain** in the poset
`(i, nums[i]) ≺ (j, nums[j]) iff i < j and nums[i] < nums[j]`, and this problem counts *how
many* maximum chains it has. The reset/accumulate rule is exactly the DP-counting principle: a
**strictly better** sub-solution invalidates the previously counted ways (overwrite), whereas an
**equally good** one contributes additional distinct ways (add).

> Note: unlike the *length* of the LIS, the O(n log n) patience greedy does **not** directly
> give the count — the standard O(n log n) counting solution needs a segment tree / BIT storing
> `(maxLen, count)` pairs keyed by value. The O(n²) DP is the expected interview answer.

**Related in this repo:** [LIS #300](#dynamic-programming/longest-increasing-subsequence),
[Russian Doll Envelopes #354](#dynamic-programming/russian-doll-envelopes), and
[Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) — the identical
counting rule works there by swapping the comparability test.
