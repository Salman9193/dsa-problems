# Best Time to Buy and Sell Stock II — Notes & Intuition

**LeetCode #122** | Arrays / Greedy | Medium

---

## Problem

Given prices[], you may make as many transactions as you like
(but must sell before buying again). Find maximum profit.

```
[7,1,5,3,6,4]  →  7   (buy@1 sell@5 +4, buy@3 sell@6 +3)
[1,2,3,4,5]    →  4   (buy@1 sell@5, or sum all diffs)
[7,6,4,3,1]    →  0   (prices only fall)
```

---

## The Greedy Insight

With unlimited transactions, any multi-day profit decomposes into
consecutive 1-day profits without loss:

```
buy day 0, sell day 3:
  profit = p[3]-p[0]
         = (p[1]-p[0]) + (p[2]-p[1]) + (p[3]-p[2])
         = sum of daily diffs
```

So collecting every positive daily difference captures ALL possible profit.

```java
for (int i = 1; i < prices.length; i++)
    if (prices[i] > prices[i-1])
        profit += prices[i] - prices[i-1];
```

---

## Why Greedy Works Here

The interchange argument: taking a positive daily diff never prevents
taking another — each day's gain is independent. No transaction limit
means no trade-off between capturing one gain vs another.

This is the one stock problem where greedy is provably optimal.
For k-limited problems (#123, #188), greedy fails because an early
gain might block a larger later gain within the k-transaction budget.

See `GREEDY_VS_DP.md` for the full framework.

---

## Full Trace — `[7,1,5,3,6,4]`

| day | price | diff | take? | profit |
|-----|-------|------|-------|--------|
| 0 | 7 | — | — | 0 |
| 1 | 1 | -6 | no | 0 |
| 2 | 5 | +4 | yes | 4 |
| 3 | 3 | -2 | no | 4 |
| 4 | 6 | +3 | yes | 7 |
| 5 | 4 | -2 | no | 7 |

Result: `7` ✓

---

## Complexity

| | |
|--|--|
| Time | O(n) |
| Space | O(1) |

---

## The Stock Series

| Problem | Constraint | Approach |
|---------|-----------|----------|
| #121 | k=1 | Prefix min scan |
| **#122** | **k=∞** | **Greedy: sum positive diffs** |
| #123 | k=2 | 4-state DP |
| #188 | k=any | k-state DP |
| #309 | k=∞, cooldown | 3-state DP |
| #714 | k=∞, fee | 2-state DP |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| With transaction fee (#714) | Fee per transaction | 2-state DP: `held = max(held, cash-price)`, `cash = max(cash, held+price-fee)` |
| With cooldown (#309) | 1-day cooldown after selling | 3-state DP: held, sold, rest |
| At most k transactions (#188) | Limit number of trades | k-state DP; greedy no longer applies |
| Short selling allowed | Can sell then buy | Negate prices; greedy still works |
| With borrow cost | Interest on borrowed shares | Modify profit formula |
| Multiple stocks | Buy any stock each day | Independent greedy per stock |

**Why greedy works only for k=∞:** With unlimited transactions, each positive daily diff is an independent opportunity — taking one never blocks another. With k-limited transactions, an early gain might consume a "transaction slot" that would have been better used later. This breaks the exchange argument and forces DP.

**The decomposition proof in one line:** Any multi-day profitable trade decomposes into a sum of consecutive 1-day profitable trades with no loss of profit. This is why summing all positive diffs captures all possible profit — the greedy claim.
