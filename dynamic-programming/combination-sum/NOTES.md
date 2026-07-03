# Combination Sum — Notes & Intuition

**LeetCode #39** | Backtracking | Medium

---

## Problem

Find all combinations of candidates (reusable, distinct) summing to target.

```
candidates=[2,3,6,7], target=7 → [[2,2,3],[7]]
candidates=[2,3], target=8 → [[2,2,2,2],[2,3,3],[3,5]...wait 5 not in candidates]
```

---

## Key Decisions

**Reuse:** pass same index `i` (not `i+1`) — can pick same candidate again.
**No duplicates in output:** `start` index prevents re-picking earlier candidates.
**Pruning:** sort candidates; if `candidates[i] > remaining`, break.

```
backtrack(remaining, start):
    if remaining == 0: record; return
    for i from start to n-1:
        if candidates[i] > remaining: BREAK  ← pruning
        pick candidates[i]
        backtrack(remaining - candidates[i], i)  ← i, not i+1
        unpick
```

---

## Variants

| Problem | Change from #39 |
|---------|----------------|
| #39 Combination Sum | Candidates reusable, no duplicates in candidates |
| #40 Combination Sum II | Candidates NOT reusable, may have duplicates → sort + skip |
| #216 Combination Sum III | Fixed k picks from 1-9 |

---

## Complexity

Time O(n^(t/min)) · Space O(t/min) stack depth
