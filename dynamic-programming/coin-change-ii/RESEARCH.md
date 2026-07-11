# Coin Change II — Research & Foundations

The count this problem computes has a precise classical name — the **denumerant** — and a
generating function due to Euler. *Citations verified against the restricted-partition /
denumerant literature this session — not from memory.*

- **L. Euler, *Introductio in analysin infinitorum* (1748).** Founded the theory of
  partitions via **generating functions**. The number of ways to write `amount` as an
  unordered sum of parts from a set `A` is the coefficient of `x^amount` in

  ```
  ∏  1 / (1 − x^c)        over the coins c ∈ A
  ```

  which is exactly what `dp[amount]` evaluates — Coin Change II is the DP form of Euler's
  product.

- **J. J. Sylvester (1857, 1882), on the partition of numbers.** Introduced the term
  **denumerant** `d(t; a₁,…,a_r)` — the number of non-negative integer solutions of
  `a₁x₁ + … + a_r x_r = t`, i.e. the number of ways to make change for `t` — and studied it
  in depth (Sylvester waves, the restricted partition function). This problem *is* the
  denumerant.

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The recurrence
  `dp[a] = dp[a] + dp[a − coin]` accumulated over coins is Bellman dynamic programming; it
  computes the generating-function coefficients without expanding the product.

- **R. L. Graham, D. E. Knuth & O. Patashnik, *Concrete Mathematics*, 2nd ed.,
  Addison-Wesley, 1994.** Accessible modern treatment of making change via generating
  functions.

**Why it matters here:** `dp[amount]` is the denumerant of `amount` with respect to `coins`
— the coefficient of `x^amount` in Euler's product `∏ 1/(1−x^c)`. The coin-outer loop order
is what makes the DP count **partitions** (unordered), in contrast to the amount-outer order
that counts **compositions** (ordered).

**Related in this repo:** the ordered twin
[Combination Sum IV #377](#dynamic-programming/combination-sum-iv) (compositions), the
min-cost cousin [Coin Change #322](#dynamic-programming/coin-change), and the inverse problem
[Inverse Coin Change #3592](#dynamic-programming/inverse-coin-change), which recovers the
coins by inverting this generating function.
