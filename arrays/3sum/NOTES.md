# 3Sum — Notes & Intuition

**LeetCode #15** | Arrays / Two Pointers | Medium

---

## Problem

Given an integer array `nums`, return all unique triplets `[a, b, c]` such
that `a + b + c == 0`. No duplicate triplets in the result.

```
Input:  [-1, 0, 1, 2, -1, -4]
Output: [[-1, -1, 2], [-1, 0, 1]]
```

---

## Approaches

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute force (three loops) | O(n³) | O(1) | Too slow |
| HashSet per pair | O(n²) | O(n) | Duplicate handling is tricky |
| **Sort + Two Pointers** | **O(n²)** | **O(1)** | ✓ Canonical solution |

---

## Core Insight — Fix One, Two-Sum the Rest

Fix `nums[i]`. The problem reduces to finding a pair in `nums[i+1..n-1]`
summing to `-nums[i]` — a two-sum on a sorted array.

Two-sum on a sorted array is O(n) with two pointers:
- `left` starts just after `i`, `right` at the end
- sum too small → `left++` (increase sum)
- sum too large → `right--` (decrease sum)
- sum == target → record, advance both, skip duplicates

---

## The Algorithm

```java
Arrays.sort(nums);

for (int i = 0; i < nums.length - 2; i++) {
    if (i > 0 && nums[i] == nums[i-1]) continue;  // skip dup i
    if (nums[i] > 0) break;                        // early exit

    int left = i+1, right = nums.length-1;
    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        if (sum == 0) {
            result.add(Arrays.asList(nums[i], nums[left], nums[right]));
            while (left < right && nums[left]  == nums[left+1])  left++;
            while (left < right && nums[right] == nums[right-1]) right--;
            left++; right--;
        } else if (sum < 0) left++;
        else right--;
    }
}
```

---

## Duplicate Skipping — Three Places

Duplicates must be skipped at three points to avoid duplicate triplets
in the output:

### 1. Outer loop — skip duplicate `i`
```java
if (i > 0 && nums[i] == nums[i-1]) continue;
```
If `nums[i]` is the same as the previous fixed element, all valid
triplets for this `i` were already found.

### 2 & 3. Inner loop — skip duplicate `left` and `right`
```java
while (left < right && nums[left]  == nums[left+1])  left++;
while (left < right && nums[right] == nums[right-1]) right--;
```
These run **only after finding a valid triplet** — advancing past
any repeated values so the next `left++`/`right--` lands on a new value.

**Why only after a valid triplet?**
If we skip before checking, we'd miss valid combinations where
duplicate values are part of different triplets.

---

## Early Termination

```java
if (nums[i] > 0) break;
```
After sorting, if `nums[i] > 0` then `nums[left] >= nums[i] > 0`
and `nums[right] >= nums[left] > 0` — the sum can never be zero.

---

## Full Trace

`nums = [-1, 0, 1, 2, -1, -4]` → sorted: `[-4, -1, -1, 0, 1, 2]`

| i | nums[i] | left | right | sum | action |
|---|---------|------|-------|-----|--------|
| 0 | -4 | 1(-1) | 5(2) | -3 | left++ |
| 0 | -4 | 2(-1) | 5(2) | -3 | left++ |
| 0 | -4 | 3(0) | 5(2) | -2 | left++ |
| 0 | -4 | 4(1) | 5(2) | -1 | left++ → done |
| 1 | -1 | 2(-1) | 5(2) | 0 | **add [-1,-1,2]**, skip → left=3,right=4 |
| 1 | -1 | 3(0) | 4(1) | 0 | **add [-1,0,1]** |
| 2 | -1 | — | — | — | dup of i=1 → skip |
| 3 | 0 | 4(1) | 5(2) | 3 | right-- → done |

Result: `[[-1,-1,2], [-1,0,1]]` ✓

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[0,0,0]` | `[[0,0,0]]` | Single valid triplet, deduplication works |
| `[0,1,1]` | `[]` | No triplet sums to 0 |
| `[-2,0,0,2,2]` | `[[-2,0,2]]` | Duplicates skipped correctly |
| `[1,2,3]` | `[]` | All positive, early termination |

---

## Complexity

| | |
|--|--|
| Time | O(n²) — O(n log n) sort + O(n) × O(n) two-pointer passes |
| Space | O(1) extra — in-place sort, output list excluded |
