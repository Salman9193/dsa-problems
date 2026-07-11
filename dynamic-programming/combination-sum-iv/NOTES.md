# Combination Sum IV — Notes & Intuition

**LeetCode #377** | Dynamic Programming (counting) | Medium
Count sequences summing to target — **order matters**, so it's really counting *ordered*
sequences (compositions), not combinations.

---

## The Two Gotchas in the Name

Despite "Combination Sum," this problem is **not** backtracking enumeration:

1. It asks for a **count**, not the actual sequences → it's a **DP**.
2. **Order matters** — `(1,2,1)` and `(2,1,1)` count as *different*. So it counts **ordered
   sequences (permutations / integer compositions)**, not combinations.

```
Input:  nums = [1,2,3], target = 4
Output: 7
        (1,1,1,1) (1,1,2) (1,2,1) (2,1,1) (1,3) (3,1) (2,2)
```

---

## The DP

`dp[t]` = number of ordered sequences summing to `t`. To form `t`, consider **every
possible last element** `num` and add the ways to make the remainder:

```
dp[t] = Σ over num ≤ t of dp[t - num]
dp[0] = 1                      (the empty sequence)
```

```java
int[] dp = new int[target + 1];
dp[0] = 1;
for (int t = 1; t <= target; t++)       // amount OUTER
    for (int num : nums)                // candidate INNER
        if (num <= t) dp[t] += dp[t - num];
return dp[target];
```

**O(target · n)** time, **O(target)** space.

---

## The Key Insight — Loop Order Encodes Order-Sensitivity

This is the exact mirror of *Coin Change II* (#518). The **only difference is which loop is
outer**:

| Problem | Counts | Loop order | Why |
|---------|--------|-----------|-----|
| **Coin Change II (#518)** | **combinations** (order-independent) | candidates **outer**, amount inner | each value contributes in one fixed position → each multiset once |
| **Combination Sum IV (#377)** | **permutations** (order matters) | amount **outer**, candidates inner | every `num` is tried as the *last* element for every `t` → all orderings counted |

**Amount-outer counts ordered sequences; candidate-outer counts combinations.** Same
recurrence, swapped loops, different meaning — the single most useful thing to remember from
the whole coin/combination family.

---

## Trace — `nums = [1,2,3]`, target `4`

| t | dp[t] = Σ dp[t−num] | value |
|---|---------------------|-------|
| 0 | — | 1 |
| 1 | dp[0] | 1 |
| 2 | dp[1] + dp[0] | 2 |
| 3 | dp[2] + dp[1] + dp[0] | 4 |
| 4 | dp[3] + dp[2] + dp[1] | 7 |

`dp[4] = 4 + 2 + 1 = 7`. ✓

---

## The Follow-Up — Negative Numbers

*What if `nums` may contain negatives?* Then sequences can be infinitely long
(`+1, −1, +1, …`), so the count is unbounded. You'd need an extra constraint — e.g. a cap on
sequence length — which turns the DP two-dimensional: `dp[len][t]`.

## Overflow

The counts grow quickly; intermediate sums can overflow 32-bit `int` even when the final
answer fits. LeetCode guarantees the answer fits in an `int`; the accepted fixes are to let
the addition wrap or accumulate in `long`.

---

## Where It Sits in the Family

| Problem | Question | Tool |
|---------|----------|------|
| [Combination Sum #39](#dynamic-programming/combination-sum) | *list* combinations (reuse) | backtracking |
| [Combination Sum II #40](#dynamic-programming/combination-sum-ii) | *list* combinations (use once, dedupe) | backtracking |
| Coin Change II #518 | *count* combinations | DP, candidate-outer |
| **Combination Sum IV #377** | *count* ordered sequences | **DP, amount-outer** |
| [Coin Change #322](#dynamic-programming/coin-change) | *min* coins | DP |

**Special case:** with `nums = {1, 2}` this counts the ways to climb `n` stairs one or two
at a time — the {1,2}-restricted compositions, which are the **Fibonacci** numbers
(Climbing Stairs, #70).

**The through-line:** it's a counting DP for **integer compositions** (ordered sums) —
`dp[t] = Σ dp[t−num]` with the amount loop outside, which is exactly what makes order count.
