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
