# Car Pooling — Notes & Intuition

**LeetCode #1094** | Arrays / Sweep Line | Medium

---

## Problem

A car with `capacity` seats. Given trips `[numPassengers, from, to]`,
determine if all trips can be completed without exceeding capacity.

```
trips = [[2,1,5],[3,3,7]], capacity = 4  →  false  (at stop 3: 2+3=5 > 4)
trips = [[2,1,5],[3,3,7]], capacity = 5  →  true   (5 == 5, OK)
trips = [[2,1,5],[3,5,7]], capacity = 3  →  true   (drop-off at 5, then board)
```

---

## The Sweep Line Insight

Think of each trip as two events on a timeline:
- At `from`: passengers **board** (+numPassengers)
- At `to`: passengers **alight** (-numPassengers)

Sort all events by stop number, sweep left to right, maintain a running
passenger count. If it ever exceeds `capacity` → impossible.

This is the **difference array / sweep line** pattern — transforms 2D
interval overlap into a 1D running sum problem.

---

## Approach 1 — Difference Array (O(n))

Stop numbers are bounded by [0, 1000]. Use an array as a bucket:

```java
int[] stops = new int[1001];
for (int[] trip : trips) {
    stops[trip[1]] += trip[0];   // board at from
    stops[trip[2]] -= trip[0];   // alight at to
}
int passengers = 0;
for (int delta : stops) {
    passengers += delta;
    if (passengers > capacity) return false;
}
```

O(n) to fill, O(R) to scan (R=1001 constant) → effectively O(n).

---

## Approach 2 — TreeMap (O(n log n))

For unbounded stop numbers — TreeMap keeps events sorted automatically:

```java
TreeMap<Integer, Integer> events = new TreeMap<>();
for (int[] trip : trips) {
    events.merge(trip[1],  trip[0], Integer::sum);
    events.merge(trip[2], -trip[0], Integer::sum);
}
int passengers = 0;
for (int delta : events.values()) {
    passengers += delta;
    if (passengers > capacity) return false;
}
```

---

## Why `to` Is Exclusive — Alight Before Boarding

Passengers alight AT `to`, so new passengers CAN board at the same stop.
The difference array handles this correctly:

```
trips = [[2,1,5],[3,5,7]], capacity=3

stops[1] += 2  → board trip 1
stops[5] -= 2  → alight trip 1
stops[5] += 3  → board trip 2
stops[7] -= 3  → alight trip 2

At stop 5: running sum = (prev 2) + (-2 + 3) = 3  ← both processed together ✓
```

Both the drop-off and the new boarding are accumulated at index 5 simultaneously,
giving the correct count after both events.

---

## Full Trace — `[[2,1,5],[3,3,7]]`, capacity=4

```
stops array:
  idx: 0  1  2  3  4  5  6  7
  val: 0 +2  0 +3  0 -2  0 -3

Running sum:
  idx=1: 0+2=2  → 2 ≤ 4 ✓
  idx=3: 2+3=5  → 5 > 4 ✗  → return false
```

---

## The Sweep Line Family

All reduce to: sort events → sweep → track running aggregate.

| Problem | Events | Running value | Goal |
|---------|--------|--------------|------|
| Car Pooling (#1094) | board/alight | passengers in car | check ≤ capacity |
| Meeting Rooms II (#253) | start/end | concurrent meetings | find max (min rooms) |
| Merge Intervals (#56) | start/end | active intervals | merge overlaps |
| Skyline Problem (#218) | building start/end | current max height | track outline |
| Range Addition (#370) | +k at l, -k at r+1 | cumulative sum | apply all updates |

---

## Tie-Breaking: End Before Start at Same Stop

When two events occur at the same stop, process **alight before board**
(allowing passengers to exit and free seats before new ones board).

In the difference array approach, this is automatic — both events are
summed at the same index before the running sum is checked.

In the TreeMap approach, this requires careful event encoding:
```java
// Sort: at same time, alight (-1) before board (+1)
events.sort((a, b) -> a[0] != b[0] ? a[0]-b[0] : a[1]-b[1]);
```

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Difference array | O(n + R) | O(R) |
| TreeMap | O(n log n) | O(n) |

R = max stop number = 1001 (constant here).
