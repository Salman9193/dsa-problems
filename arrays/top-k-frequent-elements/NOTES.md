# Top K Frequent Elements — Notes & Intuition

**LeetCode #347** | Arrays / HashMap | Medium  
Follow-up constraint: better than O(n log n).

---

## Problem

Given an integer array and a number `k`, return the `k` most frequent elements.

```
Input:  nums = [1,1,1,2,2,3],  k = 2
Output: [1, 2]
```

---

## Approach 1 — Sort by Frequency (Baseline)

```java
Map<Integer, Integer> freq = new HashMap<>();
for (int n : nums) freq.merge(n, 1, Integer::sum);
// sort entries by frequency descending, take first k
```

O(n log n) — violates the follow-up constraint. Establishes the baseline.

---

## Approach 2 — Min-Heap of Size k

Maintain a min-heap keyed by frequency. For each unique element:
- Push to heap.
- If heap size exceeds k, pop the minimum frequency element.

At the end, the heap holds exactly the k most frequent elements.

```java
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> freq.get(a) - freq.get(b)   // min by frequency
);
for (int num : freq.keySet()) {
    heap.offer(num);
    if (heap.size() > k) heap.poll();
}
```

**Why min-heap, not max-heap?**
A max-heap would give the most frequent at the root — but we'd need to
store all n elements and extract k times: O(n + k log n).
A min-heap of size k evicts the *least* frequent as we scan, keeping only
the top k at any point: O(n log k). Better when k << n.

**Complexity:** O(n log k) time, O(n + k) space.

---

## Approach 3 — Bucket Sort (Optimal)

**Key insight:** frequency of any element is bounded by `n`.
Use an array where `bucket[f]` holds all elements with frequency `f`.
Scan from `n` down to 1, collect until we have k elements.

```java
List<Integer>[] bucket = new List[nums.length + 1];
for (int num : freq.keySet()) {
    int f = freq.get(num);
    if (bucket[f] == null) bucket[f] = new ArrayList<>();
    bucket[f].add(num);
}
// scan high → low
for (int f = bucket.length - 1; f >= 0 && idx < k; f--) {
    if (bucket[f] != null)
        for (int num : bucket[f]) result[idx++] = num;
}
```

**Why O(n):**
Frequencies are integers in the range [1, n] — a bounded, known range.
Array indexing is O(1) vs O(log k) per heap operation.
This is the same principle that makes counting sort beat comparison sort
for bounded integer ranges.

**Complexity:** O(n) time, O(n) space.

---

## Approach Comparison

| Approach | Time | Space | Best when |
|----------|------|-------|-----------|
| Sort by freq | O(n log n) | O(n) | Simplest, small n |
| Min-heap | O(n log k) | O(n+k) | Large n, small k |
| Bucket sort | O(n) | O(n) | Always — optimal |

---

## Full Trace — Bucket Sort

`nums = [1,1,1,2,2,3]`, `k = 2`

```
freq = {1:3, 2:2, 3:1}

bucket[1] = [3]      ← elements with frequency 1
bucket[2] = [2]      ← elements with frequency 2
bucket[3] = [1]      ← elements with frequency 3

Scan from f=6 down:
  f=3 → [1]   → result=[1],   idx=1
  f=2 → [2]   → result=[1,2], idx=2 → done ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1]`, k=1 | `[1]` | Single element |
| All same: `[1,1,1]`, k=1 | `[1]` | Only one unique element |
| All unique: `[1,2,3]`, k=2 | any 2 | All freq=1, bucket[1] has all |
| k=n (all elements) | all elements | Return entire frequency-sorted array |

---

## Quick Select Alternative (O(n) average)

Partial sort using Quickselect finds the k-th largest frequency in O(n)
average, O(n²) worst case. More complex to implement; bucket sort is
simpler and strictly O(n) — prefer bucket sort unless space is constrained.

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Top k frequent words (#692) | Strings with alphabetical tie-breaking | Min-heap with custom comparator |
| Streaming top-k | Elements arrive continuously | Heavy Hitters / Count-Min Sketch (approximate) |
| Top-k in sliding window | Only last n elements count | Deque + HashMap with expiry tracking |
| Exact k-th most frequent | Single element, not top-k list | QuickSelect on frequency array |
| Space-constrained | Can't store all frequencies | Misra-Gries algorithm: approximate heavy hitters in O(1) space |
| Distributed | Elements across shards | Partial counts per shard → merge → select top-k |

**QuickSelect vs sort:** For exact top-k, bucket sort is O(n) but QuickSelect on the frequency array is O(n) average. Both beat heap's O(n log k) when k is large.

**Heavy Hitters (streaming):** The Misra-Gries algorithm finds all elements appearing more than n/k times using O(k) space — used in network traffic analysis to find elephant flows without storing all packet counts.
