# Best Time to Buy and Sell Stock III — Notes & Intuition

**LeetCode #123** | Dynamic Programming | Hard

---

## Problem

Given prices[], make at most 2 transactions (buy→sell, buy→sell).
Cannot hold 2 positions simultaneously. Find maximum profit.

```
[3,3,5,0,0,3,1,4]  →  6   (buy@0,sell@3, buy@1,sell@4)
[1,2,3,4,5]        →  4   (single transaction buy@1,sell@5)
[7,6,4,3,1]        →  0   (no profitable trade)
```

---

## Why Not Greedy?

With k=2 limit, taking an early small profit may block a larger combined gain.
Greedy fails — we need DP to track how many transactions we've used.

---

## 4-State DP

Track the best portfolio value at each of 4 stages:

```
buy1  — after buying  for the 1st time  (initialised to MIN_VALUE: "not bought yet")
sell1 — after selling for the 1st time
buy2  — after buying  for the 2nd time
sell2 — after selling for the 2nd time  ← final answer
```

```java
buy1  = max(buy1,  -price)         // buy at cheapest
sell1 = max(sell1, buy1 + price)   // sell for best gain
buy2  = max(buy2,  sell1 - price)  // 2nd buy "funded" by 1st profit
sell2 = max(sell2, buy2 + price)   // 2nd sell
```

---

## The Key Insight: `buy2 = sell1 - price`

`sell1 - price` means: "I made `sell1` profit from the first transaction,
and now I'm spending `price` for the second buy."

This effectively chains the two transactions — `sell2` captures their
combined profit automatically.

---

## Why Updating In-Order Is Safe

In one pass, updating buy1→sell1→buy2→sell2 is correct because:
- `buy1` at price[i] = best buy on days 0..i ✓
- `sell1` = best sell using best buy from 0..i ✓
- `buy2` = best 2nd buy after best 1st transaction up to day i ✓
- `sell2` = best 2nd sell using above ✓

Each update uses the freshest state from the current day — safe because
we want "the best possible" for each state considering today.

---

## Full Trace — `[3,3,5,0,0,3,1,4]`

| price | buy1 | sell1 | buy2 | sell2 |
|-------|------|-------|------|-------|
| 3 | -3 | 0 | -3 | 0 |
| 3 | -3 | 0 | -3 | 0 |
| 5 | -3 | 2 | -3 | 2 |
| 0 | 0 | 2 | 2 | 2 |
| 0 | 0 | 2 | 2 | 2 |
| 3 | 0 | 3 | 2 | 5 |
| 1 | 0 | 3 | 2 | 5 |
| 4 | 0 | 4 | 2 | **6** |

Result: `6` ✓

---

## Complexity

| | |
|--|--|
| Time | O(n) — single pass |
| Space | O(1) — 4 variables |

---

## The Stock Series

| Problem | k | Space | Approach |
|---------|---|-------|----------|
| #121 | 1 | O(1) | Prefix min |
| #122 | ∞ | O(1) | Greedy |
| **#123** | **2** | **O(1)** | **4-state DP** |
| #188 | k | O(k) | k-state DP |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| At most k transactions (#188) | Generalise to k | k-state DP arrays; O(nk) |
| With cooldown | 1-day rest after each sell | Add `rest` state between sell2 and buy2 |
| With transaction fee | Fee per transaction | Subtract fee from each sell |
| With percentage fee | Fee = f% of sell price | Multiply sell price by (1-f) |
| Exactly 2 transactions | Must make exactly 2 | Initialise buy1 to first price's negative |
| Know the prices in advance | Already the case | Offline — DP is optimal |

**4-state machine diagram:**
```
Start → [buy1] → [sell1] → [buy2] → [sell2]

Transitions:
  buy1  ← -price          (spend to buy)
  sell1 ← buy1 + price    (gain from first sale)
  buy2  ← sell1 - price   (spend again, funded by first profit)
  sell2 ← buy2 + price    (gain from second sale = total profit)
```

**Extending to k:** Replace the 4 variables with two arrays of length k. The i-th `buy` and `sell` represent the best portfolio values after the i-th transaction's buy and sell events. See #188 for the full generalisation.
