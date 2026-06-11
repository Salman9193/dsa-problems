# Best Time to Buy and Sell Stock — Notes & Intuition

**LeetCode #121** | Arrays / Greedy | Easy

---

## Problem

Given an array `prices` where `prices[i]` is the stock price on day `i`,
find the maximum profit from a single buy-then-sell transaction.
Return `0` if no profit is possible.

```
Input:  [7, 1, 5, 3, 6, 4]  →  5   (buy day 2 at 1, sell day 5 at 6)
Input:  [7, 6, 4, 3, 1]     →  0   (prices only fall)
```

---

## Brute Force — O(n²)

Check every (buy, sell) pair where buy < sell:
```java
for (int i = 0; i < n; i++)
    for (int j = i+1; j < n; j++)
        maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
```
Too slow for n = 100,000.

---

## One-Pass Insight — Prefix Minimum

For any sell day `i`, the maximum profit is:
```
profit(i) = prices[i] - min(prices[0..i-1])
```

We never need to look further back than the running minimum because:
- A lower minimum always gives a better or equal profit
- The minimum must come before the sell day (buy before sell)

So we scan once, maintaining:
- `minPrice` — the smallest price seen so far (best buy day)
- `maxProfit` — the best profit seen so far

```java
for (int price : prices) {
    if (price < minPrice)  minPrice = price;
    else  maxProfit = Math.max(maxProfit, price - minPrice);
}
```

---

## Is This Greedy or DP?

Both views lead to the same O(n) / O(1) algorithm:

**Greedy:** at each step, keep the cheapest buy price seen so far.
Moving to a higher minimum can never improve future profits.
Interchange argument holds → greedy is provably correct.

**DP:**
```
dp[i] = max profit achievable by selling on or before day i
dp[i] = max(dp[i-1], prices[i] - minPrice[0..i-1])
```
Only the previous state is needed → collapses to O(1) space = greedy scan.

See `GREEDY_VS_DP.md` for the general framework.

---

## Full Trace — `[7, 1, 5, 3, 6, 4]`

| day | price | minPrice | profit today | maxProfit |
|-----|-------|----------|--------------|-----------|
| 0 | 7 | 7 | — | 0 |
| 1 | 1 | **1** | — | 0 |
| 2 | 5 | 1 | 4 | 4 |
| 3 | 3 | 1 | 2 | 4 |
| 4 | 6 | 1 | **5** | **5** |
| 5 | 4 | 1 | 3 | 5 |

Return `5` ✓

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1]` | `0` | Only one day, can't sell |
| `[2, 1]` | `0` | Price only falls |
| `[1, 2]` | `1` | Buy day 0, sell day 1 |
| `[3, 3, 3]` | `0` | No profit, flat prices |

---

## The Stock Problem Series

| Problem | Constraint | Approach |
|---------|-----------|----------|
| #121 (this) | One transaction | O(n) prefix min scan |
| #122 Best Time II | Unlimited transactions | Greedy: sum all positive diffs |
| #123 Best Time III | At most 2 transactions | DP with 4 states |
| #188 Best Time IV | At most k transactions | DP O(nk) |
| #309 With Cooldown | Cooldown after sell | DP with 3 states |
| #714 With Fee | Transaction fee per trade | DP with 2 states |

---

## Complexity

| | |
|--|--|
| Time | O(n) — single pass |
| Space | O(1) — two variables |
