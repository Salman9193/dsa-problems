# Sqrt(x) — Research & Foundations

Computing ⌊√n⌋ with integer arithmetic — solvable by binary search on the answer or, with faster convergence, by Newton’s method.

- **Newton’s method (Newton–Raphson).** Integer square root by the iteration `x ← (x + n/x)/2` is the classical Newton’s method for √n, converging quadratically; binary search on the answer is the search-based alternative. Overview: https://en.wikipedia.org/wiki/Newton%27s_method and https://en.wikipedia.org/wiki/Integer_square_root

**Why it matters here:** The iteration x ← (x + n/x)/2 is Newton’s method for √n and converges quadratically; binary search on [0, n] is the O(log n) search-based alternative.

*Citations verified against ACM TOMS / CACM / BIT records this session — not from memory.*
