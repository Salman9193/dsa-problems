# Combination Sum IV — Research & Foundations

Despite the name, this counts **integer compositions** — ordered sums of positive integers —
with parts restricted to `nums`. That object has a precise combinatorial literature, and the
`dp[t] = Σ dp[t−num]` recurrence is textbook dynamic programming. *Citations verified against
the combinatorics-of-compositions literature this session — not from memory.*

- **P. A. MacMahon, *Combinatory Analysis*, Cambridge University Press, 1915–1916.** The
  classical foundation of **compositions** (ordered sums) vs. partitions (unordered).
  MacMahon's bijection (place a cut or join in each of the `n−1` gaps) proves there are
  **2ⁿ⁻¹** compositions of `n` — this problem is the *restricted* count where the parts must
  come from `nums`.

- **S. Heubach & T. Mansour (2004), "Compositions of n with parts in a set,"** *Congressus
  Numerantium* 164:127–143. Studies **A-restricted compositions** — compositions of `n`
  whose parts lie in a fixed set `A` — which is *exactly* what Combination Sum IV counts
  (with `A = nums`).

- **S. Heubach & T. Mansour (2009), *Combinatorics of Compositions and Words*, CRC Press
  (Discrete Mathematics and its Applications).** The dedicated modern reference on
  compositions, including restricted parts and generating-function methods.

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The linear
  recurrence `dp[t] = Σ dp[t−num]` (optimal/overlapping subproblems over sub-targets) is
  Bellman dynamic programming.

**Why it matters here:** the answer is the number of `nums`-restricted compositions of
`target`. Because a composition is *ordered*, the DP puts the **amount loop outside** so
every part is tried as the last element for every sub-target — counting orderings
separately. Swapping the loops counts *partitions/combinations* instead (Coin Change II).

**Related in this repo:** the enumeration siblings
[Combination Sum #39](#dynamic-programming/combination-sum) and
[Combination Sum II #40](#dynamic-programming/combination-sum-ii) (backtracking), and the
optimization cousin [Coin Change #322](#dynamic-programming/coin-change). With `nums = {1,2}`
this reduces to the Fibonacci-counted **Climbing Stairs** (#70).
