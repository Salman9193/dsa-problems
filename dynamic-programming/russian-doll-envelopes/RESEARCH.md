# Russian Doll Envelopes — Research & Foundations

Nesting is a **2-dimensional partial order** (width × height). That dimension is exactly why the
problem reduces to LIS and admits the O(n log n) patience greedy — a fact with a precise
order-theoretic name. *Citations verified against Amer. J. Math / Canad. J. Math / Discrete Math /
Bull. AMS records this session — not from memory.*

- **B. Dushnik & E. W. Miller (1941), "Partially ordered sets,"** *American Journal of
  Mathematics* 63(3):600–610. DOI: 10.2307/2371374. Introduced the **order dimension** (the
  Dushnik–Miller dimension): the least number of total orders whose intersection is the poset.
  The nesting relation here is the intersection of two total orders (width, height) — it has
  **dimension 2** — which is precisely the structural reason sorting on one coordinate reduces
  the problem to a total-order LIS on the other. https://doi.org/10.2307/2371374

- **C. Schensted (1961), "Longest increasing and decreasing subsequences,"** *Canadian Journal
  of Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3. The combinatorial foundation of the
  LIS the problem reduces to. https://doi.org/10.4153/CJM-1961-015-3

- **M. L. Fredman (1975), "On computing the length of longest increasing subsequences,"**
  *Discrete Mathematics* 11(1):29–35, and **D. Aldous & P. Diaconis (1999)**, *Bulletin of the
  AMS* 36(4):413–432. The O(n log n) **patience sorting** algorithm applied to the collapsed
  height sequence. https://doi.org/10.1090/S0273-0979-99-00796-X

**Why it matters here:** because the nesting poset has **dimension 2**, one coordinate can be
"sorted away," leaving a *totally ordered* LIS on the other — so the greedy-plus-binary-search
is valid. The descending tiebreak on equal widths is what enforces *strict* dominance in the
collapsed dimension.

**The contrast that makes this cluster coherent:**
[Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) is the *same*
"longest chain in a poset" problem, but divisibility is **not** a 2-dimensional order — no sort
collapses it, the "smallest tail is most extendable" property fails, and the patience greedy is
invalid (it is stuck at O(n²)). **Whether patience sorting applies is a property of the poset's
dimension**, not of the problem's phrasing.

**Related in this repo:** [LIS #300](#dynamic-programming/longest-increasing-subsequence)
(chains, Dilworth/Mirsky duality), [Number of LIS #673](#dynamic-programming/number-of-lis)
(counting maximum chains).
