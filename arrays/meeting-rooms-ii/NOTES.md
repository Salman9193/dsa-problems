# Meeting Rooms II — Notes & Intuition

**LeetCode #253** | Intervals / Heap | Medium

---

## Problem

Given meeting time intervals, find the minimum number of conference rooms required.

```
[[0,30],[5,10],[15,20]] → 2
[[7,10],[2,4]]          → 1
```

---

## Core Insight

Min rooms = max number of concurrent meetings at any point in time.

---

## Approach 1 — Min-Heap of End Times

```
Sort by start time.
Min-heap tracks end times of currently active meetings.

For each meeting [s, e]:
    if heap.peek() <= s: reuse room (poll earliest ending)
    add e to heap

return heap.size()  // number of rooms in use
```

---

## Approach 2 — Sweep Line

```
Separate starts[] and ends[]. Sort both.
Scan starts with pointer e into ends:
    if starts[s] < ends[e]: new meeting starts before any ends → +1 room
    else: a meeting ended → reuse room (advance e)
```

Cleaner for counting; doesn't tell you WHICH rooms are reused.

---

## Connection to Merge Intervals

Meeting Rooms II is the "how many simultaneous intervals" question.
Merge Intervals answers "what are the merged ranges."
Both use sort-by-start as the first step.

---

## Complexity

Time O(n log n) · Space O(n)
