# Partition Equal Subset Sum — Notes & Intuition

**LeetCode #416** | Dynamic Programming / 0-1 Knapsack | Medium

---

## Problem

Given an integer array `nums`, can it be partitioned into two subsets
with equal sum?

```
[1, 5, 11, 5]  →  true   ([1,5,5] and [11])
[1, 2, 3, 5]   →  false  (sum=11, odd)
```

---

## Step 1 — Reduction

Two equal subsets → each has sum `total/2`.
If `total` is odd → impossible immediately.
Otherwise: **"Does any subset sum to total/2?"** — this is subset sum / 0/1 knapsack.

---

## Step 2 — Why Greedy Fails

```
nums=[3,3,3,4,5], target=9

Greedy (pick largest that fits):
  pick 5 → remaining=4
  pick 3 → remaining=1
  pick 3 → remaining=-2 (skip)
  no valid completion

But {3,3,3} = 9 ✓  ← greedy missed it
```

An early "locally good" choice can block the globally valid solution.
This is the hallmark of a DP problem. See `GREEDY_VS_DP.md`.

---

## Step 3 — Why DP? Overlapping Subproblems

The recursive decision tree shares subproblems:

```
target=11, nums=[1,5,11,5]

            (11)
       inc1/    \exc1
        (10)     (11)
    inc5/ \exc5  inc5/ \exc5
    (5)   (10)   (6)   (11)  ← (11) appears again!
```

Same subproblem (can we reach sum X with remaining elements?) reappears
→ memoisation → DP.

---

## Step 4 — The DP

```
dp[s] = true if some subset of nums seen so far sums to exactly s

Base:       dp[0] = true   (empty subset)
Transition: dp[s] = dp[s] OR dp[s - num]   (exclude or include num)
Answer:     dp[target]
```

---

## Step 5 — Right-to-Left Update (Critical!)

```java
for (int num : nums) {
    for (int s = target; s >= num; s--) {   // RIGHT TO LEFT
        dp[s] = dp[s] || dp[s - num];
    }
}
```

### Why right-to-left?

Left-to-right bug:
```
num=5, dp=[T,T,F,F,F,F,F,F,F,F,F,F]

s=5: dp[5] |= dp[0]=T → dp[5]=T
s=10: dp[10] |= dp[5]=T → dp[10]=T  ← dp[5] was updated THIS iteration!
→ num=5 used TWICE — wrong (unbounded knapsack)
```

Right-to-left fix:
```
s=10: dp[10] |= dp[5]=F (not yet updated) ← reads previous state
s=5:  dp[5]  |= dp[0]=T → dp[5]=T
→ num=5 used at most once ✓
```

---

## Full Trace — `[1,5,11,5]`, target=11

| Step | dp array |
|------|----------|
| Init | `[T,F,F,F,F,F,F,F,F,F,F,F]` |
| num=1 | `[T,T,F,F,F,F,F,F,F,F,F,F]` |
| num=5 | `[T,T,F,F,F,T,T,F,F,F,F,F]` |
| num=11 | dp[11]\|=dp[0]=T → **return true** ✓ |

---

## Complexity

| | |
|--|--|
| Time | O(n × target) — pseudo-polynomial |
| Space | O(target) — 1D boolean array |

**Pseudo-polynomial** means: polynomial in n AND in the numeric value of sum.
For large values (sum ≈ 10⁹), this becomes intractable — that's why subset sum
is NP-complete in general.

---

## Variants Using the Same Pattern

| Problem | Variant |
|---------|---------|
| #416 Partition Equal Subset Sum | Can we reach target? (boolean) |
| #494 Target Sum | How many ways to reach target? (count) |
| #322 Coin Change | Fewest items to reach target? (min count) |
| #518 Coin Change II | How many ways? (unbounded, left-to-right) |
| Classic 0/1 Knapsack | Maximise value within weight limit |

The right-to-left trick applies to all 0/1 variants.
Left-to-right is correct for unbounded variants (#518).

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Minimum subset sum difference (#2035) | Minimise |S1 - S2|, not require equality | DP up to total/2; find largest reachable sum ≤ total/2 |
| Partition into k equal subsets (#698) | k subsets each summing to total/k | Backtracking with bitmask DP; NP-hard for k≥3 |
| Last Stone Weight II (#1049) | Minimum possible weight after smashing | Same as minimum subset sum difference |
| Target Sum (#494) | Count ways to assign +/- | Same DP with counting instead of boolean |
| Partition with max size | Each subset has ≤ k elements | Add element count dimension to DP |
| Multiset partitioning | Elements can repeat | Already handled by 0/1 DP; frequency counts |

**Minimum difference extension:** Run the standard 0/1 knapsack DP up to `total/2`. The largest reachable sum `s ≤ total/2` gives minimum difference = `total - 2s`. This is the same DP array; just find the rightmost `true` entry at the end instead of checking a specific target.

**k=3 partition is NP-complete:** For k≥3, the problem is NP-complete (reduces from 3-Partition). The DP approach is O(n × total) which is pseudo-polynomial — works only when total is small. For large values, use backtracking with pruning. See COMPLEXITY_THEORY.md.

**Bitset optimisation:** For large n but small total, represent `dp[]` as a bitset. Then `dp |= dp << num` in O(total/64) per element — a 64× speedup using 64-bit word operations. Used in competitive programming for n × total up to ~10⁸.
