# Two Sum II — Input Array Is Sorted — Notes & Intuition

**LeetCode #167** | Arrays / Two Pointers | Medium
Constraint: O(1) extra space.

---

## Problem

Given a **1-indexed** sorted array and a target, return the indices of
the two numbers that add up to the target. Exactly one solution exists.

```
numbers = [2, 7, 11, 15], target = 9  →  [1, 2]
numbers = [2, 3, 4],      target = 6  →  [1, 3]
numbers = [-1, 0],        target = -1 →  [1, 2]
```

---

## Two Sum I vs Two Sum II

| | Two Sum I | Two Sum II (this) |
|--|-----------|-------------------|
| Array order | Unsorted | Sorted |
| Approach | HashMap | Two pointers |
| Time | O(n) | O(n) |
| Space | O(n) | **O(1)** |

The sorted order is the key — it provides directional information that
makes two pointers possible without any extra memory.

---

## Two Pointer Insight

Start at both ends. The sum either:
- **Equals target** → return both indices
- **Is too small** → need a larger number → `left++`
- **Is too large** → need a smaller number → `right--`

```java
int left = 0, right = numbers.length - 1;
while (left < right) {
    int sum = numbers[left] + numbers[right];
    if      (sum == target) return new int[]{left+1, right+1};
    else if (sum < target)  left++;
    else                    right--;
}
```

---

## Correctness — The Pruning Argument

When `sum < target` and we move `left++`, we don't just skip one pair.
We eliminate ALL pairs `(left, j)` for every `j <= right`:

```
numbers[left] + numbers[j]
  <= numbers[left] + numbers[right]   (since j <= right and array is sorted)
  < target                            (since current sum < target)
```

None of those pairs can work. We eliminate an entire column of the
implicit n×n pair matrix in O(1).

Symmetric argument: when `sum > target` and we move `right--`, we
eliminate all pairs `(i, right)` for every `i >= left`.

Each step eliminates at least one row or column → at most 2n steps
total → O(n).

---

## Visual — n×n Pair Matrix

```
numbers = [2, 3, 7, 11], target = 10

         2    3    7    11
    2  [ 4]  [ 5]  [9]  [13]
    3       [ 6]  [10]* [14]
    7              [14]  [18]
   11                    [22]

Two pointers trace:
  l=0(2), r=3(11): sum=13>10 → r--
  l=0(2), r=2(7):  sum=9<10  → l++
  l=1(3), r=2(7):  sum=10==10 → return [2,3] ✓

Each step eliminates a row or column.
```

---

## Full Trace — `[2,7,11,15]`, target=9

| left | numbers[left] | right | numbers[right] | sum | action |
|------|--------------|-------|----------------|-----|--------|
| 0 | 2 | 3 | 15 | 17 | right-- |
| 0 | 2 | 2 | 11 | 13 | right-- |
| 0 | 2 | 1 | 7 | 9 | **return [1,2]** |

---

## Edge Cases

| Input | Target | Output | Reason |
|-------|--------|--------|--------|
| `[2,7]` | `9` | `[1,2]` | Only two elements |
| `[-1,0]` | `-1` | `[1,2]` | Negative numbers |
| `[1,2,3,4,5]` | `9` | `[4,5]` | 4+5=9, last two |
| `[0,0]` | `0` | `[1,2]` | Both zeros |

---

## Complexity

| | |
|--|--|
| Time | O(n) — each pointer moves at most n steps |
| Space | O(1) — two integer variables only |

---

## Two Sum Problem Family

| Problem | Array | Best Approach | Space |
|---------|-------|--------------|-------|
| #1 Two Sum | Unsorted | HashMap | O(n) |
| #167 Two Sum II | Sorted | Two pointers | O(1) |
| #15 3Sum | Unsorted | Sort + two pointers | O(1) |
| #18 4Sum | Unsorted | Sort + two pointers × 2 | O(1) |
| #653 Two Sum IV BST | BST | Inorder + two pointers | O(n) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Unsorted input (#1 Two Sum) | No sorted guarantee | HashMap: O(n) time, O(n) space |
| 3Sum (#15) | Three numbers summing to 0 | Fix one, two-pointer on rest |
| 4Sum (#18) | Four numbers | Fix two O(n²), two-pointer O(n) → O(n³) |
| Two Sum in a BST (#653) | BST, not array | Inorder to get sorted array, then two pointers |
| Two Sum with absolute difference ≤ k | Relaxed equality | Two pointers with tolerance window |
| Count pairs summing to target | All pairs, not just one | Two pointers; count when sum == target |
| Two Sum in circular array | Array wraps | Try both halves as "beginning" |

**Sort-merge join equivalence:** As noted in USE_CASES.md, two-sum-II on sorted arrays IS the sort-merge join algorithm. The pruning argument (eliminating a full row or column of the n×n pair matrix per step) is the same correctness argument as why merge join is O(n+m) not O(nm).

**Follow-up in interviews:** "What if we needed all pairs, not just one?" → Two pointers still work but count instead of return. "What if values can repeat?" → Handle duplicates the same way as in 3Sum: skip repeated values after finding a valid pair.
