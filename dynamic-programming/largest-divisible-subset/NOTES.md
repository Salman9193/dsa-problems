# Largest Divisible Subset — Notes & Intuition

**LeetCode #368** | Dynamic Programming (LIS variant) | Medium
Distinct positive integers. Return a largest subset where every pair is divisible one way.

---

## Problem

Given distinct positive integers, return the **largest** subset in which every pair
`(a, b)` satisfies `a % b == 0` or `b % a == 0`. Any valid answer of maximum size is fine.

```
Input:  [1, 2, 3]        Output: [1, 2]   (or [1, 3])
Input:  [1, 2, 4, 8]     Output: [1, 2, 4, 8]
```

---

## The Key Insight — Sort, and It Becomes LIS

The "every pair" condition looks quadratic to satisfy, but **sorting reduces it to a
chain condition**. If a valid subset is sorted `a₁ < a₂ < … < aₖ`, the only way each pair
can divide is `a₁ | a₂ | … | aₖ`. And divisibility is **transitive**: if `a₁ | a₂` and
`a₂ | a₃` then `a₁ | a₃`. So a chain where each element divides the next automatically
makes *every* pair divisible.

So after sorting, the problem is exactly **Longest Increasing Subsequence** — with "is
divisible by" in place of "is greater than" — plus you must **reconstruct** the actual
subset, so you keep parent pointers.

---

## The DP + Reconstruction

- `dp[i]` = size of the largest divisible subset **ending at** `nums[i]` (after sorting).
- `prev[i]` = the previous index in that chain (for rebuilding the subset).
- Transition: for `j < i`, if `nums[i] % nums[j] == 0` and extending `j` beats the current
  best, set `dp[i] = dp[j] + 1`, `prev[i] = j`.
- Track the index with the largest `dp`, then walk `prev` pointers back and reverse.

```java
Arrays.sort(nums);
int[] dp = new int[n], prev = new int[n];
Arrays.fill(dp, 1); Arrays.fill(prev, -1);
int best = 0;
for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++)
        if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) { dp[i] = dp[j] + 1; prev[i] = j; }
    if (dp[i] > dp[best]) best = i;
}
// reconstruct: for (int i = best; i >= 0; i = prev[i]) add nums[i]; then reverse
```

---

## Full Trace — [1, 2, 4, 8]

Sorted already. Each divides the next, so `dp = [1, 2, 3, 4]`, `prev = [-1, 0, 1, 2]`,
`best = 3` (value 8). Walking `prev`: `8 → 4 → 2 → 1` → reversed `[1, 2, 4, 8]`. ✓

For `[1, 2, 3]`: `dp = [1, 2, 2]` (3 isn't divisible by 2), so the answer is `[1, 2]` or
`[1, 3]` — both size 2.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Brute force (check all subsets) | O(2ⁿ · n²) | — |
| Sort + LIS-style DP | **O(n²)** | O(n) |

Sorting is O(n log n); the LIS-style double loop dominates at O(n²).

---

## Why Patience Sorting Does NOT Work Here (unlike LIS)

Since sorting turns this into an "LIS-style" DP, the natural question is: *can't we use the
O(n log n) patience-sorting greedy from [LIS #300](#dynamic-programming/longest-increasing-subsequence)?*

**No — and the reason is the heart of this problem.**

Patience sorting keeps only the **smallest tail per chain length**, which is sound because `<`
is a **total order** and therefore guarantees:

> *A smaller tail is always at least as extendable as a larger one.*

**Divisibility is a partial order, and that guarantee is false.** Chains ending at `2` and at
`3` are **incomparable** — neither dominates:

- `4` extends the `2`-chain, but not the `3`-chain.
- `9` extends the `3`-chain, but not the `2`-chain.

So "keep the smallest tail" discards the tail you actually needed:

```
nums = [2, 3, 9]
  greedy-tails: keeps 2 (smaller), discards 3 → 9 can attach to nothing → answer 1  ✗
  correct:      [3, 9]                                                   → answer 2  ✓
```

To stay correct you would have to keep **every pairwise-incomparable tail** — a whole
*antichain* of tails per length — and there is no total order on them to binary search. The
`log n` dies.

**The structural statement:** patience sorting works when the poset has **dimension 2** (a pair
of total orders, so one can be "sorted away"). Divisibility is not such an order.

| Problem | Poset | Patience sort? |
|---------|-------|----------------|
| [LIS #300](#dynamic-programming/longest-increasing-subsequence) | position × value (2-D) | ✅ O(n log n) |
| [Russian Doll #354](#dynamic-programming/russian-doll-envelopes) | width × height (2-D) | ✅ O(n log n) *after the sort trick* |
| **Largest Divisible Subset #368** | divisibility (higher dimension) | ❌ **stuck at O(n²)** |

**Whether the greedy works is a property of the poset's dimension, not of the problem's
wording.** (See Research & Foundations.)

---

## What This Problem Really Is — and Where You *Can* Do Better

It's the **longest path in a DAG**: nodes are the numbers, with an edge `d → v` whenever
`d | v`. Sorting ascending is a free topological order, and the O(n²) DP is the standard
longest-path relaxation. **The O(n²) is the cost of *discovering* the edges** (testing all
pairs), not of the DP itself.

So avoid the pairwise test — **enumerate divisors** instead:

```java
Arrays.sort(nums);
Map<Integer, Integer> dp = new HashMap<>();      // value -> best chain length ending there
int best = 0;
for (int v : nums) {
    int cur = 0;
    for (int d = 1; (long) d * d <= v; d++) {    // all divisors of v in O(√v)
        if (v % d != 0) continue;
        if (dp.containsKey(d))     cur = Math.max(cur, dp.get(d));
        if (dp.containsKey(v / d)) cur = Math.max(cur, dp.get(v / d));
    }
    dp.put(v, cur + 1);
    best = Math.max(best, cur + 1);
}
```

**O(n · √M)** where `M` is the maximum value — and which one wins depends on the regime:

| Regime | Better choice |
|--------|---------------|
| LeetCode's limits (`n ≤ 1000`, values ≤ 2·10⁹) | **O(n²) DP** — `n² = 10⁶` beats `n·√M ≈ 4.5·10⁷` |
| Large `n`, small values (e.g. `n = 10⁶`, values ≤ 10⁵) | **divisor enumeration** (or a sieve) |

So there *is* a faster algorithm hiding here — it just depends on the **value bound**, not on
sequence structure. That's why the standard answer stays O(n²).

---

## Edge Cases

| Case | Handling |
|------|----------|
| single element | that element is a valid subset of size 1 |
| no two divide | answer is any single element |
| `1` present | 1 divides everything, so it can start any chain |
| chain of powers (1,2,4,…) | the whole array is the answer |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return only the size | drop reconstruction | keep `dp` only |
| Count such subsets | how many maximum chains | add a `count[]` alongside `dp[]` |
| Russian Doll Envelopes (#354) | 2-D nesting order | sort cleverly, then LIS on the second dimension |
| Longest chain in a general poset | any partial order | same DP with the poset's comparability test |

**The through-line:** sort so divisibility becomes a chain, run LIS with `%` instead of
`<`, and follow parent pointers to rebuild the subset — computing the **longest chain in
the divisibility partial order** (see Research & Foundations).
