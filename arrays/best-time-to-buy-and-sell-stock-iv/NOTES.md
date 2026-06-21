# Best Time to Buy and Sell Stock IV — Notes & Intuition

**LeetCode #188** | Dynamic Programming | Hard

---

## Problem

Given prices[] and integer k, make at most k transactions. Find maximum profit.

```
k=2, [2,4,1,2,3]  →  4   (buy@1,sell@4, buy@1,sell@3 = +3+2=no... buy@2sell@4=2, buy@1sell@3=2 → 4)
k=2, [3,2,6,5,0,3] →  7  (buy@2,sell@6 +4, buy@0,sell@3 +3)
```

---

## The Full Stock Series Pattern

All stock problems use the same buy/sell state machine:

```
buy[i]  = max portfolio value after i-th buy
sell[i] = max portfolio value after i-th sell

Transitions:
  buy[0]  = max(buy[0],  -price)
  sell[0] = max(sell[0], buy[0] + price)
  buy[i]  = max(buy[i],  sell[i-1] - price)   i > 0
  sell[i] = max(sell[i], buy[i]    + price)
```

| Problem | k | buy/sell arrays | Extra constraint |
|---------|---|----------------|-----------------|
| #121 | 1 | 1 pair | — |
| #122 | ∞ | greedy | — |
| #123 | 2 | 2 pairs (4 vars) | — |
| **#188** | **k** | **k pairs** | **k >= n/2 → greedy** |
| #309 | ∞ | 2 pairs | Cooldown after sell |
| #714 | ∞ | 2 pairs | Fee per transaction |

---

## The Critical Optimisation — k >= n/2 Fallback

If k ≥ ⌊n/2⌋, we can make as many non-overlapping transactions as exist.
At most ⌊n/2⌋ buy/sell pairs fit in n days, so larger k gives no extra power.
Use the O(n) greedy instead of O(nk) DP — critical when k=10⁹.

```java
if (k >= n / 2) {
    // greedy: sum all positive diffs
    int profit = 0;
    for (int i = 1; i < n; i++)
        if (prices[i] > prices[i-1]) profit += prices[i] - prices[i-1];
    return profit;
}
```

---

## Full Algorithm

```java
int[] buy = new int[k]; Arrays.fill(buy, Integer.MIN_VALUE);
int[] sell = new int[k];

for (int price : prices) {
    buy[0]  = Math.max(buy[0],  -price);
    sell[0] = Math.max(sell[0], buy[0] + price);
    for (int i = 1; i < k; i++) {
        buy[i]  = Math.max(buy[i],  sell[i-1] - price);
        sell[i] = Math.max(sell[i], buy[i]    + price);
    }
}
return sell[k-1];
```

---

## Why This Is Correct

The i-th transaction's buy is "funded" by the (i-1)-th sell: `sell[i-1] - price`.
All states are updated in dependency order within each price iteration —
each state reflects "the best possible given all prices up to today."

---

## Complexity

| | |
|--|--|
| Time | O(n × k), or O(n) when k ≥ n/2 |
| Space | O(k) — two arrays |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| k=0 | 0 | No transactions allowed |
| k >= n/2 | greedy result | Effectively unlimited |
| All decreasing | 0 | No profitable trade |
| k=1 | same as #121 | Single transaction |
