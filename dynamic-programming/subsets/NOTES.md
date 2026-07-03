# Subsets — Notes & Intuition

**LeetCode #78** | Backtracking | Medium

---

## Problem

Return all possible subsets (power set) of a distinct integer array.

```
[1,2,3] → [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

---

## The Backtracking Template

```
backtrack(start, current):
    record(current)   ← add current state to result
    for i from start to n-1:
        current.add(nums[i])          // choose
        backtrack(i+1, current)       // explore
        current.remove(last)          // un-choose (backtrack)
```

`start` prevents duplicates by only considering elements after the current index.

---

## Decision Tree

```
              []
           /      \
         [1]       []
        /   \      / \
     [1,2]  [1]  [2]  []
      /   |   |   |    |
[1,2,3] [1,2][1,3][2,3][3]
```

---

## Three Approaches

| | Backtracking | Cascading | Bitmask |
|--|-------------|-----------|---------|
| Time | O(n×2^n) | O(n×2^n) | O(n×2^n) |
| Space | O(n) stack | O(n×2^n) | O(n×2^n) |
| Generalises? | Yes — any constraint | Only simple inclusion | No |

Use backtracking for anything beyond simple subsets.

---

## Complexity

Time O(n×2^n) · Space O(n) call stack (+ O(n×2^n) output)
