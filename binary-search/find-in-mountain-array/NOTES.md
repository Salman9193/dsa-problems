# Find in Mountain Array — Notes & Intuition

**LeetCode #1095** | Binary Search | Hard
Constraint: call `mountainArr.get()` at most 100 times.

---

## Problem

Given a mountain array (values increase to a peak then decrease), find
the minimum index where `mountainArr.get(index) == target`.
Return -1 if not found.

```
[1,2,3,4,5,3,1], target=3  →  2  (index 2, not 5)
[0,1,2,4,2,1,0], target=3  →  -1
```

---

## Why The Call Limit Forces Binary Search

For n=10,000 with a 100-call limit: linear scan uses O(n) = 10,000 calls.
Binary search uses O(log n) ≈ 14 calls. We need ~3 binary searches → ~42 calls total.

---

## Three-Step Strategy

```
Step 1: Find peak index         → O(log n) calls
Step 2: Binary search left half → O(log n) calls  [ascending — standard]
Step 3: Binary search right half → O(log n) calls [descending — flipped comparisons]
```

Search left first → guarantees minimum index if target appears on both sides.

---

## Step 1 — Finding the Peak

At the peak, `arr[mid] > arr[mid-1]` AND `arr[mid] > arr[mid+1]`.

Binary search on slope direction:

```java
if (arr.get(mid) < arr.get(mid + 1))
    left = mid + 1;   // still ascending → peak is to the right
else
    right = mid;      // at or past peak → peak is here or left
```

This converges to the peak in O(log n) calls.

---

## Step 2 — Ascending Half (Standard Binary Search)

```java
if      (val == target) return mid;
else if (val < target)  left  = mid + 1;   // need larger → go right
else                    right = mid - 1;
```

---

## Step 3 — Descending Half (Flipped Comparisons)

In a descending array, going **right** means going to **smaller** values:

```java
if      (val == target) return mid;
else if (val > target)  left  = mid + 1;   // need smaller → go RIGHT
else                    right = mid - 1;   // need larger  → go LEFT
```

---

## Why Left Half First?

Left half indices are always < right half indices.
If target exists in both halves, we want the leftmost → search left first.
If found in left half, return immediately — guaranteed minimum.

---

## Full Trace — `[1,2,3,4,5,3,1]`, target=3

**Step 1 — Peak:**
```
left=0, right=6
mid=3: arr[3]=4 < arr[4]=5 → left=4
mid=4: arr[4]=5 > arr[5]=3 → right=4
peak = 4 (value=5)
```

**Step 2 — Ascending [0,4]:**
```
left=0, right=4
mid=2: arr[2]=3 == target → return 2 ✓
```

---

## API Call Budget

| Operation | Calls |
|-----------|-------|
| Find peak | log₂(n) ≈ 14 |
| Search left half | log₂(n/2) ≈ 13 |
| Search right half | log₂(n/2) ≈ 13 |
| **Total** | **~40 calls** (limit: 100) |

---

## Binary Search Family — Unimodal Arrays

| Problem | Array shape | Binary search finds |
|---------|------------|---------------------|
| #162 Find Peak Element | Has local peak | Any local peak (compare neighbors) |
| **#1095 Find in Mountain** | Single global peak | Peak + target in each half |
| #33 Search Rotated | Sorted then rotated | Which half is sorted |
| #153 Find Min in Rotated | Sorted then rotated | Rotation breakpoint |

The peak-finding binary search pattern (compare `arr[mid]` vs `arr[mid+1]`)
appears in all unimodal problems.

---

## Edge Cases

| Input | Target | Output | Reason |
|-------|--------|--------|--------|
| `[1,2,1]` | 2 | 1 | Target is the peak |
| `[1,2,1]` | 1 | 0 | Left half found first |
| `[3,5,3]` | 3 | 0 | Same value on both sides — left wins |
| `[1,5,2]` | 4 | -1 | Not in array |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Find peak element (#162) | Any local peak, not global | Simpler: one binary search, no target needed |
| Multiple peaks | Find all peaks | Linear scan O(n); no binary search shortcut |
| Find in bitonic array (general) | Same as mountain | This solution exactly |
| k calls limit varies | Budget is different | Adjust: 3 searches must fit within k calls |
| Floating-point mountain | Continuous function | Golden section search or ternary search |
| 2D mountain | Peak in 2D matrix | Find peak column then peak row; O(n log n) |

**2D peak finding:** Walk to the largest neighbour from any cell — guaranteed to find a peak in O(n) steps on an n×n grid. But for "find a specific value in a mountain matrix," binary search applies per row.

**The three binary search templates used here:**
1. Find peak: `arr[mid] < arr[mid+1]` → `left = mid + 1`
2. Find target in ascending: `arr[mid] < target` → `left = mid + 1`
3. Find target in descending: `arr[mid] > target` → `left = mid + 1`

These three templates cover 90% of binary search variants. Recognising which one applies is the core skill for binary search problems.
