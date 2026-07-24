# Intervals — Complete Pattern Guide

All interval problems reduce to one of three operations:
merge overlapping intervals, insert a new interval, or count overlaps.

---

## The Core Invariant

Always sort by start time first. After sorting, overlap detection is O(1):
two intervals [a,b] and [c,d] overlap iff `c <= b` (i.e., the second starts
before the first ends).

---

## Pattern 1: Merge Intervals (#56)

```java
public int[][] merge(int[][] intervals) {
    Arrays.sort(intervals, (a,b) -> a[0]-b[0]);
    List<int[]> result = new ArrayList<>();

    for (int[] interval : intervals) {
        if (result.isEmpty() || interval[0] > result.get(result.size()-1)[1]) {
            result.add(interval);          // no overlap: add new
        } else {
            result.get(result.size()-1)[1] =
                Math.max(result.get(result.size()-1)[1], interval[1]);  // merge
        }
    }
    return result.toArray(new int[0][]);
}
```

---

## Pattern 2: Insert Interval (#57)

```java
public int[][] insert(int[][] intervals, int[] newInterval) {
    List<int[]> result = new ArrayList<>();
    int i = 0, n = intervals.length;

    // Add all intervals ending before newInterval starts
    while (i < n && intervals[i][1] < newInterval[0])
        result.add(intervals[i++]);

    // Merge all overlapping intervals with newInterval
    while (i < n && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
        newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
        i++;
    }
    result.add(newInterval);

    // Add remaining intervals
    while (i < n) result.add(intervals[i++]);
    return result.toArray(new int[0][]);
}
```

---

## Pattern 3: Meeting Rooms II — Minimum Rooms (#253)

```java
public int minMeetingRooms(int[][] intervals) {
    // Sweep line: separate start and end events
    int n = intervals.length;
    int[] starts = new int[n], ends = new int[n];
    for (int i = 0; i < n; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
    Arrays.sort(starts); Arrays.sort(ends);

    int rooms = 0, maxRooms = 0, j = 0;
    for (int i = 0; i < n; i++) {
        if (starts[i] < ends[j]) {
            rooms++;          // new meeting starts before oldest ends: need new room
        } else {
            j++;              // oldest meeting ended: reuse its room
        }
        maxRooms = Math.max(maxRooms, rooms);
    }
    return maxRooms;
}
```

---

## Pattern 4: Non-Overlapping Intervals (#435) — Minimum Removals

Greedily keep intervals ending earliest. Remove the rest.

```java
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a,b) -> a[1]-b[1]);  // sort by END time
    int count = 0, prevEnd = Integer.MIN_VALUE;
    for (int[] interval : intervals) {
        if (interval[0] >= prevEnd) {
            prevEnd = interval[1];  // keep this interval
        } else {
            count++;  // remove this interval (it overlaps and ends later)
        }
    }
    return count;
}
```

---

## When to Sort by Start vs End

| Problem | Sort by | Reason |
|---------|---------|--------|
| Merge intervals | Start | Merge left to right |
| Insert interval | Already sorted | Linear scan |
| Min meeting rooms | Both (separately) | Sweep line |
| Non-overlapping (min removals) | End | Greedy: keep earliest-ending |
| Activity selection (max non-overlapping) | End | Same greedy |

---

## Extensions

| Problem | Pattern |
|---------|---------|
| #452 Minimum arrows to burst balloons | Non-overlapping count |
| #986 Interval list intersections | Two-pointer on two sorted lists |
| #1288 Remove covered intervals | Sort by start desc, end asc |
| #759 Employee free time | Merge all intervals, find gaps |
| #352 Data stream as disjoint intervals | TreeMap of sorted intervals |

---

## Beyond Merging: the Measure of a Union

The patterns above all *output intervals*. A different question — **how much total length do they
cover?** — has its own name, its own optimal algorithm, and a matching lower bound:

```
[1,4] ∪ [2,6] ∪ [8,10]  =  [1,6] ∪ [8,10]  =  7
```

That's **Klee's measure problem**, and its event-sweep formulation generalises to maximum
concurrency, gap-finding, "covered at least k times," and the 2-D rectangle-union area.

> See **[Klee's Algorithm](#guides/KLEES_ALGORITHM)** — including why O(n log n) is provably
> optimal, and why the segment tree was invented to solve the 2-D case.
