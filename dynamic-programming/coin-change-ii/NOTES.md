# Coin Change II — Notes & Intuition

**LeetCode #518** | Dynamic Programming (counting) | Medium
Unlimited coins. Count the **combinations** (order-independent) that make up the amount.

---

## Problem

Given an `amount` and coin denominations (infinite supply of each), return the number of
**combinations** that sum to the amount. `[1,2,2]` and `[2,1,2]` are the *same* combination.

```
Input:  amount = 5, coins = [1,2,5]
Output: 4          ([5], [2,2,1], [2,1,1,1], [1,1,1,1,1])
```

---

## The DP

`dp[a]` = number of combinations making amount `a`. `dp[0] = 1` (one way to make 0 — pick
nothing). The recurrence is `dp[a] += dp[a - coin]`, and the **loop order is the whole
point**:

```java
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;
    for (int coin : coins)                    // coin OUTER
        for (int a = coin; a <= amount; a++)  // amount INNER
            dp[a] += dp[a - coin];
    return dp[amount];
}
```

**O(amount · n)** time, **O(amount)** space.

---

## Why Coin-Outer Gives Combinations

Putting **coins on the outside** commits to considering coin `1` fully, then coin `2`, then
coin `5` — each coin type is introduced once, in a fixed order. So every combination is
built in one canonical order (non-decreasing by the outer loop), and `[2,2,1]` is counted
once — never also as `[1,2,2]` or `[2,1,2]`.

This is the **exact mirror of [Combination Sum IV #377](#dynamic-programming/combination-sum-iv)**
— same recurrence, swapped loops:

| | Loop order | Counts |
|--|-----------|--------|
| **Coin Change II (#518)** | coin **outer**, amount inner | **combinations** (order-independent) |
| **Combination Sum IV (#377)** | amount **outer**, coin inner | permutations (order matters) |

In combinatorics terms this is **partitions vs compositions** (see Research & Foundations).

---

## Full Trace — `amount = 5`, `coins = [1,2,5]`

| after coin | dp[0..5] |
|-----------|----------|
| init | `1 0 0 0 0 0` |
| 1 | `1 1 1 1 1 1` |
| 2 | `1 1 2 2 3 3` |
| 5 | `1 1 2 2 3 4` |

`dp[5] = 4`. ✓

---

## Edge Cases

| Case | Result |
|------|--------|
| amount = 0 | 1 (the empty combination) |
| no coin divides toward amount | 0 |
| single coin dividing amount | 1 |
| large counts | can exceed int — problem guarantees the answer fits |

## The Coin / Combination Family

| Problem | Question | Tool |
|---------|----------|------|
| **Coin Change II #518** | *count* combinations | **DP, coin-outer** |
| [Combination Sum IV #377](#dynamic-programming/combination-sum-iv) | *count* ordered sequences | DP, amount-outer |
| [Combination Sum #39](#dynamic-programming/combination-sum) / [II #40](#dynamic-programming/combination-sum-ii) | *list* combinations | backtracking |
| [Coin Change #322](#dynamic-programming/coin-change) | *min* coins | DP |
| [Inverse Coin Change #3592](#dynamic-programming/inverse-coin-change) | *recover the coins* from the counts | invert this DP |

**The through-line:** counting combinations is a DP with the **coin loop outside** — the
order-independent twin of Combination Sum IV, and the forward direction that Inverse Coin
Change reverses.
