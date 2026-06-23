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

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| At most 2 transactions (#123) | Two buy/sell pairs | 4-state DP: buy1,sell1,buy2,sell2 |
| Unlimited transactions (#122) | Any number of trades | Greedy: sum all positive daily diffs |
| At most k transactions (#188) | k buy/sell pairs | k-state DP arrays |
| With cooldown (#309) | 1-day cooldown after selling | 3-state DP: held, sold, rest |
| With fee (#714) | Fee per transaction | 2-state DP: held, cash |
| Short selling allowed | Can sell before buying | Negate all prices; same prefix min |
| Continuous compounding | Percentage gains | Maximise log(price[sell]/price[buy]) — same prefix min on log prices |

**The unified state machine:** All stock variants use the same buy/sell state machine with different numbers of states and transition costs. The k-state generalisation (see #188 NOTES.md) covers all of them as special cases.

**Why no DP needed for k=1:** With one transaction, there are no competing choices between transactions — the first buy date and first sell date are independent. This eliminates the "interference" between states that makes k>1 require DP.
