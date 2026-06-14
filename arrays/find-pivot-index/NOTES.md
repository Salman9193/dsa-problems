# Find Pivot Index — Notes & Intuition

**LeetCode #724** | Arrays / Prefix Sum | Easy

---

## Problem

Find the index where the sum of all numbers to the left equals the sum
of all numbers to the right. Return -1 if no such index exists.

```
[1, 7, 3, 6, 5, 6]  →  3   (left=11, right=11)
[1, 2, 3]            →  -1  (no pivot)
[2, 1, -1]           →  0   (left=0 empty, right=0)
```

---

## Core Insight — Eliminate the Right Sum Array

Naive approach: precompute prefix sums and suffix sums, then scan.
That's O(n) time but O(n) space for two arrays.

Better: we know `rightSum = totalSum - leftSum - nums[i]`.

So the pivot condition `leftSum == rightSum` becomes:
```
leftSum == totalSum - leftSum - nums[i]
2 * leftSum + nums[i] == totalSum
```

This lets us check the pivot with only two variables.

---

## Algorithm

```java
int total = 0;
for (int n : nums) total += n;     // Pass 1: compute total sum

int leftSum = 0;
for (int i = 0; i < nums.length; i++) {
    if (leftSum == total - leftSum - nums[i]) return i;  // pivot?
    leftSum += nums[i];            // extend leftSum AFTER checking
}
return -1;
```

### Why check BEFORE adding nums[i]?

At the start of iteration `i`:
- `leftSum` = sum of `nums[0..i-1]` — everything to the LEFT of `i`
- `total - leftSum - nums[i]` = sum of `nums[i+1..n-1]` — everything to the RIGHT

If we added `nums[i]` to `leftSum` first, it would include the pivot
element itself, making both the left sum and the subtraction wrong.

---

## Full Traces

**`[1, 7, 3, 6, 5, 6]`** → total=28

| i | nums[i] | leftSum | rightSum | pivot? |
|---|---------|---------|----------|--------|
| 0 | 1 | 0 | 27 | no |
| 1 | 7 | 1 | 20 | no |
| 2 | 3 | 8 | 17 | no |
| 3 | 6 | 11 | **11** | **yes → return 3** |

**`[2, 1, -1]`** → total=2

| i | nums[i] | leftSum | rightSum | pivot? |
|---|---------|---------|----------|--------|
| 0 | 2 | 0 | **0** | **yes → return 0** |

Note: index 0 is valid — the "left" of the first element is empty (sum=0).

---

## Prefix Sum Family

All these problems decompose per-position values into left and right contributions:

| Problem | Left value | Right value | Condition |
|---------|-----------|-------------|-----------|
| #724 Find Pivot Index | prefix sum | suffix sum | left == right |
| #238 Product Except Self | prefix product | suffix product | multiply |
| #42 Trapping Rain Water | prefix max | suffix max | min(left,right) - height |
| #560 Subarray Sum Equals K | prefix sum | HashMap lookup | sum == k |
| #303 Range Sum Query | prefix sum array | subtraction | range query |

---

## Complexity

| | |
|--|--|
| Time | O(n) — two passes |
| Space | O(1) — two variables |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1]` | `0` | Single element — both sides empty (sum=0) |
| `[0,0,0]` | `0` | All zeros — every index is a pivot; return first |
| `[-1,-1,-1,-1,-1,0]` | `2` | Negative numbers work naturally |
| `[1,2,3]` | `-1` | No pivot exists |
