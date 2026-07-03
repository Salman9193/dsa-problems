# Find Median from Data Stream — Notes & Intuition

**LeetCode #295** | Design / Heap | Hard

---

## Problem

Design a data structure supporting:
- `addNum(int num)` — add a number from the data stream
- `findMedian()` — return the current median

```
addNum(1), addNum(2), findMedian() → 1.5
addNum(3), findMedian() → 2.0
```

---

## Two-Heap Design

```
lo = max-heap (smaller half)   hi = min-heap (larger half)

Invariant: lo.size() >= hi.size(), and lo.size() - hi.size() <= 1
           Every element in lo <= every element in hi

Median:
  lo.size() > hi.size() → lo.peek()
  equal sizes → (lo.peek() + hi.peek()) / 2.0
```

---

## addNum — The "Route Through lo" Trick

```java
lo.offer(num);           // add to lo (small half)
hi.offer(lo.poll());     // move lo's max to hi (balance values)
if (lo.size() < hi.size())
    lo.offer(hi.poll()); // re-balance size
```

This three-line pattern maintains both the value invariant (lo ≤ hi)
and the size invariant (lo.size >= hi.size) without explicit comparisons.

---

## Why Not Sort or Use an Array?

| Approach | addNum | findMedian |
|----------|--------|-----------|
| Sorted array | O(n) | O(1) |
| Two heaps | O(log n) | O(1) |
| Order statistics tree | O(log n) | O(log n) |

Two heaps is optimal: O(log n) insert, O(1) query.

---

## Complexity

addNum: O(log n) · findMedian: O(1) · Space: O(n)
