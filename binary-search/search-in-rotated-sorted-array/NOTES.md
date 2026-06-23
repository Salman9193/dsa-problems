# Search in Rotated Sorted Array — Notes & Intuition

**LeetCode #33** | Binary Search | Medium  
Constraint: must run in **O(log n)** time.

---

## Problem

A sorted array has been rotated at some unknown pivot index. Given a target,
return its index or -1 if not found.

```
Input:  nums = [4, 5, 6, 7, 0, 1, 2],  target = 0
Output: 4

Input:  nums = [4, 5, 6, 7, 0, 1, 2],  target = 3
Output: -1
```

The rotation means the array is split into two sorted halves:
```
[4, 5, 6, 7, | 0, 1, 2]
 ← sorted  →   ← sorted →
```

---

## Why Naive Binary Search Fails

Standard binary search assumes the entire array is sorted.
Here, comparing `target` to `nums[mid]` alone isn't enough — you don't
know which side the target is on without knowing where the rotation is.

---

## The Key Insight

> **At every midpoint, at least one half is always sorted.**

This is the invariant that makes binary search possible. You don't need
to find the pivot — you just need to:
1. Determine which half is sorted.
2. Check if the target lies within that sorted half.
3. Discard the other half.

---

## The Two Cases

After computing `mid`, exactly one case holds:

### Case 1 — Left half is sorted: `nums[left] <= nums[mid]`

```
[4, 5, 6, 7, | 0, 1, 2]
 left    mid
```

- If `target ∈ [nums[left], nums[mid])` → search left (`right = mid - 1`)
- Else → search right (`left = mid + 1`)

### Case 2 — Right half is sorted: `nums[left] > nums[mid]`

```
[6, 7, 0, 1, 2, 3, 4]
 left    mid
```

- If `target ∈ (nums[mid], nums[right]]` → search right (`left = mid + 1`)
- Else → search left (`right = mid - 1`)

---

## Boundary Conditions — Why `<=` and `<` Matter

```java
// Case 1: target in left half?
target >= nums[left] && target < nums[mid]   // strict < on right: mid itself already checked
```

```java
// Case 2: target in right half?
target > nums[mid] && target <= nums[right]  // strict > on left: mid already checked
```

`nums[mid]` is checked at the top of the loop (`if nums[mid] == target`),
so both range checks exclude `mid` to avoid off-by-one errors.

---

## Full Trace

`nums = [4, 5, 6, 7, 0, 1, 2]`, `target = 0`

| Iteration | left | right | mid | nums[mid] | Decision |
|-----------|------|-------|-----|-----------|----------|
| 1 | 0 | 6 | 3 | 7 | Left [4,7] sorted; 0 ∉ [4,7) → right half |
| 2 | 4 | 6 | 5 | 1 | Left [0,1] sorted; 0 ∈ [0,1) → left half |
| 3 | 4 | 4 | 4 | 0 | **Found at index 4** ✓ |

---

## Edge Cases

| Input | Target | Output | Reason |
|-------|--------|--------|--------|
| `[1]` | 1 | 0 | Single element |
| `[1, 3]` | 3 | 1 | Two elements |
| `[1, 2, 3, 4, 5]` | 3 | 2 | No rotation — Case 1 always |
| `[3, 1, 2]` | 1 | 1 | Right half sorted — Case 2 |
| `[4, 5, 6, 7, 0, 1, 2]` | 3 | -1 | Not present |

---

## Complexity

| | |
|--|--|
| Time | O(log n) — one extra comparison per binary search step |
| Space | O(1) |

---

## Variant: Find Minimum in Rotated Sorted Array (LeetCode #153)

The same "which half is sorted" logic can be used to find the pivot
(minimum element) in O(log n). The minimum is always at the start of
the unsorted half.

```java
// If left half is sorted, minimum is in right half
if (nums[left] <= nums[mid]) left = mid + 1;
else right = mid;
```

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| With duplicates (#81) | Array may contain duplicates | Handle `nums[left] == nums[mid]` by shrinking left |
| Find minimum in rotated (#153) | Find pivot, not target | Binary search on which half is sorted |
| Count occurrences of target | Multiple identical targets | Binary search for leftmost + rightmost occurrence |
| Rotation count unknown | Already the base case | This solution handles it |
| Multiple rotations | Array rotated multiple times | No longer solvable with binary search alone |
| k-rotation point | Find where rotation happened | Modified binary search converging on the break |

**Critical edge case for duplicates (#81):** When `nums[left] == nums[mid] == nums[right]`, we can't determine which half is sorted. We must shrink both pointers by 1: `left++; right--`. This degrades worst case to O(n) for arrays of all-identical elements.

**Amortised note:** Standard binary search is O(log n) because we eliminate half the search space per step. With the duplicate case, we eliminate at minimum 2 elements per step — still O(n) worst case but O(log n) average.
