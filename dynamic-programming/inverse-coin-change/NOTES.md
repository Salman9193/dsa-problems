# Inverse Coin Change — Notes & Intuition

**LeetCode #3592** | Dynamic Programming (inverse counting) | Medium
Given the "number of ways" array, recover the coin denominations that produced it.

---

## Problem

You're given a 1-indexed array `numWays`, where `numWays[i]` is the number of ways to make
total `i` from an infinite supply of some fixed denominations (each a positive integer
≤ `numWays.length`). The denominations were lost — return the sorted set of denominations
that produces this array, or an empty array if none exists.

```
Input:  numWays = [0,1,0,2,0,3,0,4,0,5]
Output: [2,4,6]
        amount 2: 1 way  [2]
        amount 4: 2 ways [2,2], [4]
        amount 6: 3 ways [2,2,2], [2,4], [6]
```

This is the **inverse of [Coin Change II #518](#dynamic-programming/coin-change-ii)** — that
problem maps *coins → counts*; this one recovers *counts → coins*.

---

## The Key Insight — One New Coin Bumps Its Own Count by Exactly 1

Recall Coin Change II's DP: for each coin `c`, `for a = c..n: dp[a] += dp[a-c]`. The fact
that inverts it:

> **When you introduce a new coin `c` larger than every coin found so far, `numWays[c]`
> increases by exactly 1** — the new way is the singleton `[c]` (the only way to hit exactly
> `c` with a copy of `c` is one copy).

Every other amount's count also shifts, but amount `c` itself rises by precisely 1. So sweep
amounts upward, rebuilding the count array `cur` from coins found so far, and at each `i`
compare:

- `numWays[i] == cur[i]` → `i` is **not** a coin.
- `numWays[i] == cur[i] + 1` → `i` **is** a coin: record it and apply its forward update.
- anything else → **impossible**, return `[]`.

Because coins are found in increasing order and a coin affects only amounts `≥` itself,
`cur[i]` is **finalized** by the time you reach `i` — the decision is forced.

---

## The Algorithm

```java
public List<Integer> findCoins(int[] numWays) {
    int n = numWays.length;
    long[] cur = new long[n + 1];
    cur[0] = 1;                                  // base: one way to make 0
    List<Integer> coins = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
        long want = numWays[i - 1];              // 1-indexed input
        if (want == cur[i]) {
            // not a coin
        } else if (want == cur[i] + 1) {
            coins.add(i);
            for (int a = i; a <= n; a++) cur[a] += cur[a - i];   // Coin Change II update
        } else {
            return new ArrayList<>();            // no valid coin set
        }
    }
    return coins;                                // already sorted (increasing i)
}
```

---

## Full Trace — `numWays = [0,1,0,2,0,3,0,4,0,5]`

| i | cur[i] before | numWays[i] | decision |
|---|---------------|-----------|----------|
| 1 | 0 | 0 | not a coin |
| 2 | 0 | 1 | **coin** (0+1) → update |
| 4 | 1 | 2 | **coin** (1+1) → update |
| 6 | 2 | 3 | **coin** (2+1) → update |
| 8 | 4 | 4 | not a coin |
| 10 | 5 | 5 | not a coin |

(odd `i` stay 0 throughout.) Result `[2,4,6]`. ✓

---

## Complexity

| | Time | Space |
|--|------|-------|
| Sweep + per-coin DP update | O(n²) | O(n) |

Use `long` for `cur` — the counts can exceed `int` during the sweep.

---

## Why It Can Be Impossible

`numWays[i]` must be either `cur[i]` (no coin) or `cur[i] + 1` (coin `i`). Any other value —
too many ways, or a jump of more than one — cannot be produced by *any* denomination set, so
the answer is `[]`. Example: `numWays[i]` smaller than `cur[i]` is impossible, because adding
coins never *decreases* a count.

**The through-line:** run Coin Change II in reverse — sweep amounts upward, and wherever the
observed count is exactly one more than what the discovered coins produce, that amount is a
denomination. It **inverts the generating function** `∏ 1/(1−x^c)` to recover the `c`'s
(see Research & Foundations).
