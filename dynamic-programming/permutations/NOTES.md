# Permutations — Notes & Intuition

**LeetCode #46** | Backtracking | Medium

---

## Problem

Return all permutations of distinct integers.

```
[1,2,3] → [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
```

---

## Backtracking with visited[]

```
backtrack(curr, visited):
    if curr.size() == n: record(curr); return
    for each i in 0..n-1:
        if visited[i]: skip
        visited[i] = true; curr.add(nums[i])
        backtrack(curr, visited)
        visited[i] = false; curr.remove(last)  ← backtrack
```

---

## Subsets vs Permutations

| | Subsets | Permutations |
|--|---------|-------------|
| Result size | 2^n | n! |
| Parameter | `start` (move forward) | `visited[]` (any unused) |
| Goal | Include/exclude each | Order all elements |

---

## #47 Permutations II (Duplicates)

Sort first. Skip: `if (i > 0 && nums[i] == nums[i-1] && !visited[i-1]) continue`
This ensures each duplicate permutation is generated exactly once.

---

## Complexity

Time O(n! × n) · Space O(n)
