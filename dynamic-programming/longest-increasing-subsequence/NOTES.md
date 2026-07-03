# Longest Increasing Subsequence — Notes & Intuition

**LeetCode #300** | Dynamic Programming / Binary Search | Medium

---

## Problem

Find the length of the longest strictly increasing subsequence.
(Not necessarily contiguous.)

```
[10,9,2,5,3,7,101,18] → 4   ([2,3,7,101] or [2,3,7,18])
[0,1,0,3,2,3]         → 4
[7,7,7,7,7]           → 1
```

---

## Approach 1 — O(n²) DP

```
dp[i] = length of longest IS ending at index i
dp[i] = max(dp[j]+1) for all j<i where nums[j]<nums[i]
Base: dp[i] = 1 (just the element itself)
```

Intuition: for each element, scan backwards for all smaller elements that
can precede it, take the best.

---

## Approach 2 — O(n log n) Patience Sorting

**The tails array:** `tails[k]` = smallest possible tail of any IS of length k+1.

For each `num`:
- Binary search for first `tails[pos] >= num`
- If `pos == tails.size()`: num extends the longest IS → append
- Else: replace `tails[pos] = num` → smaller tail, more room to grow

```
nums=[2,5,3,7]:
  2: tails=[2]
  5: tails=[2,5]        (5 > 2 → extend)
  3: tails=[2,3]        (3 replaces 5 at pos=1 → smaller tail for IS of length 2)
  7: tails=[2,3,7]      (7 > 3 → extend)
return 3 ✓
```

**Key caveat:** `tails` is NOT the actual LIS — it's a structural artefact
of the greedy. To reconstruct the LIS, use the O(n²) DP with parent pointers.

---

## The Patience Sorting Connection

LIS ≡ patience sorting card game:
- Deal cards one at a time
- Place each card on leftmost pile whose top card ≥ current card
- If no such pile: start new pile
- Number of piles = LIS length

This is identical to the tails array approach. Both are optimal by Dilworth's
theorem: minimum number of decreasing subsequences = LIS length.

---

## Comparison

| | O(n²) DP | O(n log n) Patience |
|--|----------|---------------------|
| Time | O(n²) | O(n log n) |
| Space | O(n) | O(n) |
| Reconstruct LIS? | Yes (parent pointers) | No (need extra work) |
| Use when | n ≤ 1000 | n ≤ 100,000 |

---

## Complexity

O(n²) DP: Time O(n²) · Space O(n)
Patience: Time O(n log n) · Space O(n)

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #354 Russian Doll Envelopes | 2D LIS | Sort by width↑, height↓; LIS on height |
| #673 Number of LIS | Count all LIS | O(n²) DP tracking count + length |
| Longest Non-Decreasing Subsequence | Allow equal | Change < to <= in binary search |
| Longest Decreasing Subsequence | Reverse direction | Negate all values, run LIS |
