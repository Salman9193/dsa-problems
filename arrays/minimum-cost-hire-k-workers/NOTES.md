# Minimum Cost to Hire K Workers — Notes & Intuition

**LeetCode #857** | Arrays / Greedy / Heap | Hard

---

## Problem

Given `n` workers with `quality[i]` and `wage[i]`, hire exactly `k` workers such that:
1. All workers are paid at a uniform rate (wage per unit quality)
2. Each worker gets at least their minimum `wage[i]`

Find the minimum total wage.

```
quality=[10,20,5], wage=[70,50,30], k=2  →  105.0
```

---

## Step 1 — Formalise the Constraints

If the group is paid at rate `r` (per unit quality):
- Worker i gets paid: `r × quality[i]`
- Minimum wage constraint: `r × quality[i] >= wage[i]`
- Therefore: `r >= wage[i] / quality[i]` for every hired worker

The rate `r` must satisfy ALL workers' constraints:
```
r = max(wage[i] / quality[i])  over all hired workers i
```

The worker with the **highest ratio** sets the rate — call them the **captain**.

---

## Step 2 — Fix the Captain, Optimise Co-Workers

Total pay = `r × (sum of all qualities in group)`

For a fixed captain (fixed `r`), to minimise total pay, minimise `sum of qualities`.
→ Pick the k-1 co-workers with **smallest quality** from all eligible workers
  (those with ratio ≤ captain's ratio — their wage constraints are automatically satisfied).

---

## Step 3 — Iterate Over All Possible Captains

Sort workers by `wage/quality` ratio ascending. For each worker as the captain:
- All previously-seen workers have lower ratio → eligible co-workers
- Maintain a max-heap of size k to track k smallest qualities seen so far
- Compute cost = captain's ratio × qualitySum

```java
Arrays.sort(workers, by ratio);
PriorityQueue<Double> maxHeap = new PriorityQueue<>(reverseOrder());
double qualitySum = 0, minCost = MAX;

for each (ratio, quality) in sorted workers:
    maxHeap.offer(quality); qualitySum += quality;
    if (maxHeap.size() > k): qualitySum -= maxHeap.poll();  // remove largest
    if (maxHeap.size() == k): minCost = min(minCost, ratio * qualitySum);
```

---

## Why Max-Heap for K Smallest?

To track the k SMALLEST qualities, use a max-heap of size k:
- When heap size exceeds k → remove the MAXIMUM (the heap's top)
- Remaining k elements are always the k smallest seen so far

This is the **standard "k smallest elements" heap trick**.

---

## Full Trace — `quality=[10,20,5], wage=[70,50,30], k=2`

```
Ratios: w0=70/10=7.0, w1=50/20=2.5, w2=30/5=6.0
Sorted by ratio: [(2.5,20), (6.0,5), (7.0,10)]

Step 1: ratio=2.5, qual=20
  heap=[20], sum=20, size=1 < k → skip

Step 2: ratio=6.0, qual=5
  heap=[20,5], sum=25, size=2 == k
  cost = 6.0 × 25 = 150

Step 3: ratio=7.0, qual=10
  heap=[20,5,10], sum=35, size=3 > k
  remove max(20): heap=[10,5], sum=15
  cost = 7.0 × 15 = 105
  minCost = min(150, 105) = 105

return 105.0 ✓
Interpretation: hire w2 (quality=5, pay=42) and w0 (quality=10, pay=70)?
  Wait — rate = 7.0, pay w0 = 7×10 = 70 ✓, pay w2 = 7×5 = 35 ✓, total = 105 ✓
```

---

## Correctness Argument

Every optimal solution has one captain (max ratio worker). When we process
that captain in sorted order, all co-workers have already passed through
the heap. The heap holds the k smallest qualities from all prior workers
→ the optimal co-workers are always considered.

No better group is missed because:
- Any group of k must have one captain
- All other members have lower ratio than the captain
- The heap captures all of them and keeps the k cheapest (by quality)

---

## Complexity

| | |
|--|--|
| Time | O(n log n) — sort + O(n log k) heap ops |
| Space | O(n + k) |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Minimum quality threshold | Workers must have quality ≥ q_min | Pre-filter; same algorithm |
| Fractional workers | Can hire partial workers | LP relaxation; greedy optimal |
| Multiple job types | Different proportionality groups | Separate sort + heap per group |
| Online workers | Workers arrive in stream | Maintain sorted structure dynamically |
| Fixed total budget | Budget ≤ B | Binary search on k + this greedy |
| Maximise quality with fixed budget | Dual problem | Sort by quality/wage; max-heap on wage |

---

## Related Problems

| Problem | Connection |
|---------|-----------|
| #973 K Closest Points | K smallest by distance — same max-heap trick |
| #215 Kth Largest Element | Heap-based selection |
| #630 Course Schedule III | Greedy + max-heap for resource selection |
| #1383 Maximum Performance of Team | Same pattern: sort by one metric, heap on other |
