# Longest Increasing Subsequence — Real-World Use Cases

LIS is a fundamental measure of sequence structure, connecting card games,
computational biology, and financial trend analysis.

---

## 1. Patience Sorting — Card Game and Algorithm Equivalence

LIS is exactly equivalent to the patience sorting card game. Given a sequence,
the minimum number of piles needed in patience sorting equals the LIS length.
This connection proves optimality via Dilworth's theorem (minimum number of
decreasing subsequences = LIS length).

The patience sorting algorithm is the foundation of the O(n log n) solution.
Patience sorting is the fastest card-game-based method for sorting real decks,
and expected number of piles is approximately 2√n for a random permutation.

### Reference
- **Princeton Patience Sorting Lecture (COS 423):**
  https://www.cs.princeton.edu/courses/archive/spr05/cos423/lectures/patience.pdf
  "Strong duality: min number of piles = max length of IS. Greedy finds both.
  Efficient O(n log n) implementation via binary search on pile tops."

- **arXiv:1712.09230 — Space-Efficient LIS via Patience Sorting:**
  https://arxiv.org/pdf/1712.09230
  "Patience Sorting computes LIS in O(n log n) time. Element τ(i) is in pile j
  iff the longest IS ending at τ(i) has length j — the tails array invariant."

---

## 2. Computational Biology — DNA Sequence Alignment

LIS is a subroutine in genome alignment. When aligning two DNA sequences,
finding matching subsequences in order corresponds to LIS on the match
positions. LIS is LCS when one string is monotonically increasing.

### Reference
- **arXiv:2112.05106 — Estimating LIS in Nearly Optimal Time:**
  https://arxiv.org/pdf/2112.05106
  "LIS is often a subroutine in LCS algorithms: when strings are only mildly
  repetitive, and in approximation algorithms. LCS between two strings reduces
  to LIS when one string is monotonically increasing."

---

## 3. Stock Market — Uptrend Stability Analysis

LIS applied to stock price sequences measures uptrend stability. The LIS of
a price sequence captures the longest continuous bullish subsequence; gaps
between LIS elements indicate growth intensity.

### Reference
- **ResearchGate — Enumerating LIS and Patience Sorting:**
  https://www.researchgate.net/publication/222708429_Enumerating_longest_increasing_subsequences_and_patience_sorting
  "Although both sequences may have the same LIS length, growth intensity
  differs by gap sizes in LIS. LIS applied to stock open prices measures
  uptrend stability for trading day analysis."

---

## Further Reading

- LIS Wikipedia: https://en.wikipedia.org/wiki/Longest_increasing_subsequence
- Patience sorting (Princeton): https://www.cs.princeton.edu/courses/archive/spr05/cos423/lectures/patience.pdf
- Space-efficient LIS (arXiv): https://arxiv.org/pdf/1712.09230
