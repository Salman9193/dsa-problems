# Kth Largest Element in an Array — Notes & Intuition

**LeetCode #215** | Heap / Quickselect | Medium

---

## Problem

Find the kth largest element in an unsorted array (not distinct).

```
[3,2,1,5,6,4], k=2 → 5
[3,2,3,1,2,4,5,5,6], k=4 → 4
```

---

## Approach 1 — Min-Heap of Size k

```
Maintain min-heap of the k largest elements seen.
Heap root = kth largest.

for each num:
    heap.add(num)
    if heap.size() > k: heap.poll()  // remove smallest

return heap.peek()
```

**Invariant:** heap always contains the k largest elements seen so far.

---

## Approach 2 — Quickselect

Partition like QuickSort. The pivot lands at its FINAL sorted position.
If pivot index == n-k: found kth largest. Else recurse into correct half.

**Average O(n)** — each recursion halves the problem size on average.
**Worst O(n²)** — if pivot always lands at edge (fix: randomise pivot choice).

---

## When to Use Which

| | Min-Heap O(n log k) | Quickselect O(n) avg |
|--|--------------------|--------------------|
| Streaming data | ✓ Yes | ✗ No |
| Guaranteed bound | ✓ Yes | ✗ No (worst O(n²)) |
| Small k (k << n) | ✓ Very efficient | OK |
| In-place | ✗ O(k) space | ✓ O(1) |
| Static array | OK | ✓ Faster on average |

---

## Top K Pattern

Min-heap of size k solves the general "top k" pattern:
- Top K frequent elements (#347) — heap on frequencies
- K closest points to origin (#973) — heap on distances
- Merge K sorted lists (#23) — heap on head values

---

## Complexity

Min-heap: Time O(n log k) · Space O(k)
Quickselect: Time O(n) avg · Space O(1)
