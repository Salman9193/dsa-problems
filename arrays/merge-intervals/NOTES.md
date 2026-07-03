# Merge Intervals — Notes & Intuition

**LeetCode #56** | Arrays / Intervals | Medium

---

## Problem

Merge all overlapping intervals.

```
[[1,3],[2,6],[8,10],[15,18]] → [[1,6],[8,10],[15,18]]
[[1,4],[4,5]]               → [[1,5]]  (touching = overlapping)
```

---

## Algorithm

```
Sort by start time
For each interval:
    if no overlap with last merged: append
    else: extend last merged's end = max(last.end, curr.end)
```

**Overlap condition:** `curr.start <= last.end`
(includes touching: [1,4] and [4,5] → [1,5])

---

## The Intervals Family

| Problem | Core operation |
|---------|---------------|
| #56 Merge Intervals | Merge overlapping |
| #57 Insert Interval | Insert one, merge resulting overlaps |
| #435 Non-overlapping | Min removals for no overlaps (greedy: sort by end) |
| #252/#253 Meeting Rooms | Can attend all? / Min rooms needed |
| #1235 Maximum Profit in Job Scheduling | Weighted interval scheduling (DP + binary search) |

---

## Complexity

Time O(n log n) · Space O(n)
