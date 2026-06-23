# Coin Change — Notes & Intuition

**LeetCode #322** | Dynamic Programming | Medium

---

## Problem

Given an array of coin denominations and a target amount, return the minimum
number of coins needed to make up that amount. Return -1 if impossible.

```
Input:  coins = [1, 5, 10, 25],  amount = 36
Output: 3   →  25 + 10 + 1

Input:  coins = [2],  amount = 3
Output: -1  →  impossible
```

---

## Why Greedy Fails

The instinct — always pick the largest coin that fits — works for US coins
but breaks for arbitrary denominations:

```
coins = [1, 3, 4],  amount = 6
Greedy: 4 + 1 + 1 = 3 coins  ✗
DP:     3 + 3     = 2 coins  ✓
```

Greedy makes locally optimal choices that aren't globally optimal.
DP considers all sub-problems and builds the global optimum from them.

---

## The DP Recurrence

Define `dp[i]` = minimum coins needed to make amount `i`.

**Base case:** `dp[0] = 0` — zero coins needed for amount 0.

**Recurrence:** for each amount `i` and each coin `c`:
```
if c <= i:
    dp[i] = min(dp[i], dp[i - c] + 1)
```

**Intuition:** if coin `c` is the last coin used to reach amount `i`,
you need `dp[i-c]` coins for the remainder, plus 1 for this coin.

---

## Why `amount + 1` as Infinity?

- Maximum coins ever needed for `amount` = `amount` itself (all 1-coins).
- So `amount + 1` is safely unreachable — a sentinel for "not yet achievable."
- At the end, if `dp[amount]` is still `amount + 1`, the amount is impossible → return -1.

Using `Integer.MAX_VALUE` would cause overflow when adding 1 (`MAX_VALUE + 1` wraps negative).

---

## Full Trace

`coins = [1, 5, 10, 25]`, `amount = 11`

| i | coin tried | dp[i-c]+1 | dp[i] |
|---|------------|-----------|-------|
| 1 | 1 | dp[0]+1=1 | 1 |
| 2 | 1 | dp[1]+1=2 | 2 |
| 3 | 1 | dp[2]+1=3 | 3 |
| 4 | 1 | dp[3]+1=4 | 4 |
| 5 | 1,5 | dp[4]+1=5, dp[0]+1=1 | **1** |
| 6 | 1,5 | dp[5]+1=2, dp[1]+1=2 | 2 |
| 10 | 1,5,10 | ..., dp[0]+1=1 | **1** |
| 11 | 1,5,10 | dp[10]+1=2, ... | **2** |

Result: `2` (10 + 1) ✓

---

## Bottom-Up vs Top-Down

### Bottom-Up (Tabulation) — implemented above
- Fills the dp array from 0 to amount.
- O(amount × coins) time, O(amount) space.
- No recursion stack — preferred for large amounts.

### Top-Down (Memoisation)
```java
Map<Integer, Integer> memo = new HashMap<>();

int solve(int[] coins, int rem) {
    if (rem < 0) return -1;
    if (rem == 0) return 0;
    if (memo.containsKey(rem)) return memo.get(rem);

    int min = Integer.MAX_VALUE;
    for (int c : coins) {
        int res = solve(coins, rem - c);
        if (res >= 0) min = Math.min(min, res + 1);
    }
    memo.put(rem, min == Integer.MAX_VALUE ? -1 : min);
    return memo.get(rem);
}
```

Same complexity, slightly more overhead from recursion and HashMap.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `amount = 0` | `0` | Base case |
| `coins = [2], amount = 3` | `-1` | Impossible — odd amount, only even coin |
| `coins = [1], amount = 10000` | `10000` | All 1-coins |
| `coins = [1,2,5], amount = 11` | `3` | 5+5+1 |

---

## Complexity

| | |
|--|--|
| Time | O(amount × \|coins\|) |
| Space | O(amount) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Count ways (#518 Coin Change II) | Count distinct combinations | Change `min` to `+=`; left-to-right (unbounded) |
| Print the coins used | Return the coins, not count | Track parent array alongside dp |
| Coin Change with limited supply | Each coin available k times | Bounded knapsack; binary grouping trick |
| Minimum coins with transaction fee | Fee per coin used | Add fee to coin value in transition |
| Fractional coins | Can use fractions of a coin | Greedy suffices — sort by value/weight, take greedily |
| Multi-currency | Different coin sets in different regions | Separate DP per region; merge at borders |

**Why unbounded (left→right) here:** Coins can be reused any number of times — reading `dp[s-coin]` after updating it is intentional. This is the only difference from the 0/1 version.

**Scaling:** For amount = 10⁸ and a small coin set, the O(amount × n) DP is intractable. In practice, use BFS (shortest path on implicit graph) which terminates early, or heuristic beam search.
