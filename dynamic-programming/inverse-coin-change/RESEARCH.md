# Inverse Coin Change — Research & Foundations

This problem **inverts** the generating function behind Coin Change II — recovering the coin
set from the sequence of way-counts (the denumerants). Its foundation is therefore the same
classical partition/denumerant theory, read in reverse. *Citations verified against the
restricted-partition / denumerant literature this session — not from memory.*

- **L. Euler, *Introductio in analysin infinitorum* (1748).** The way-count sequence is the
  coefficient sequence of Euler's partition generating function

  ```
  ∏  1 / (1 − x^c)        over the coins c
  ```

  The forward direction (coins → counts) is expanding this product; **this problem is the
  inverse — reading the factors `(1 − x^c)⁻¹` (i.e. the coins) back off the coefficients.**

- **J. J. Sylvester (1857, 1882).** Named and studied the **denumerant** `d(t; a₁,…,a_r)` —
  the number of representations of `t` as a non-negative combination of the parts. The input
  array here is precisely the denumerant sequence; the task is to recover its parts.

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The reconstruction
  reuses the Coin Change II forward recurrence `cur[a] += cur[a − c]` as it commits to each
  discovered coin — Bellman dynamic programming run incrementally.

**Why it matters here:** each coin `c` contributes a factor `1/(1−x^c)` to Euler's product,
and the effect of that factor first shows up as a `+1` at coefficient `x^c` (the singleton
`[c]`). Sweeping the coefficients and detecting those unit bumps recovers the factors — a
concrete, discrete instance of **generating-function inversion**. Most coefficient sequences
aren't realizable, so the procedure also decides *existence* (returning `[]` otherwise).

> Note: this is a recent (2025) competitive-programming problem; there isn't a single paper
> on this exact inverse formulation. The honest foundation is the Euler/Sylvester
> generating-function theory it inverts.

**Related in this repo:** the forward problem
[Coin Change II #518](#dynamic-programming/coin-change-ii), the ordered analogue
[Combination Sum IV #377](#dynamic-programming/combination-sum-iv), and the optimization
cousin [Coin Change #322](#dynamic-programming/coin-change).
