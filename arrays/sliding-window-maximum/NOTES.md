# Sliding Window Maximum — Notes & Intuition

**LeetCode #239** | Arrays / Monotonic Deque | Hard

---

## Problem

Given array `nums` and window size `k`, return the maximum of each
sliding window as it moves from left to right.

```
nums = [1,3,-1,-3,5,3,6,7], k = 3
Output: [3,3,5,5,6,7]
```

---

## Why Naive Fails

For each of n-k+1 windows, scan k elements → O(nk).
For n=100,000, k=50,000: 5 billion operations.

---

## The Monotonic Decreasing Deque

Maintain a deque of **indices** where **values are decreasing** front-to-back.
The front always holds the index of the current window's maximum.

```java
Deque<Integer> dq = new ArrayDeque<>();  // indices, values decreasing

for (int i = 0; i < n; i++) {
    // 1. Evict expired index from front
    if (!dq.isEmpty() && dq.peekFirst() < i - k + 1)
        dq.pollFirst();

    // 2. Evict smaller values from back (they'll never be max)
    while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i])
        dq.pollLast();

    // 3. Add current index
    dq.offerLast(i);

    // 4. Record window max
    if (i >= k - 1) result[i - k + 1] = nums[dq.peekFirst()];
}
```

---

## Why Discard Smaller Elements?

When index `j < i` and `nums[j] <= nums[i]`:
- `nums[i]` is **at least as large** as `nums[j]`
- `nums[i]` **expires later** than `nums[j]` (it's further right)
- Therefore `nums[j]` **can never be the maximum** of any future window
  while `nums[i]` is present — safe to discard forever

This is the core insight: elements that are both **smaller** and **older**
are permanently useless. Remove them immediately.

---

## Deque State Invariants

At any point in the algorithm:
1. **Window bounds:** `dq.front()` is always within `[i-k+1, i]`
2. **Monotone decreasing:** values from front → back are non-increasing
3. **Current max:** `nums[dq.front()]` = max of current window

---

## Full Trace — `[1,3,-1,-3,5,3,6,7]`, k=3

| i | nums[i] | deque indices | deque values | result |
|---|---------|--------------|--------------|--------|
| 0 | 1 | [0] | [1] | — |
| 1 | 3 | [1] | [3] | — |
| 2 | -1 | [1,2] | [3,-1] | **3** |
| 3 | -3 | [1,2,3] | [3,-1,-3] | **3** |
| 4 | 5 | [4] | [5] | **5** |
| 5 | 3 | [4,5] | [5,3] | **5** |
| 6 | 6 | [6] | [6] | **6** |
| 7 | 7 | [7] | [7] | **7** |

Output: `[3,3,5,5,6,7]` ✓

---

## Why `<=` Not `<` When Removing From Back

```java
while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i])
    dq.pollLast();
```

Using `<=` removes equal values — keeps only the **rightmost** occurrence.
The rightmost equal value stays in the window longer → it's the better
candidate. Using `<` would leave stale equal values on the deque.

---

## Complexity

| | |
|--|--|
| Time | O(n) — each index pushed and popped at most once |
| Space | O(k) — deque holds at most k indices |

---

## Monotonic Deque / Stack Family

| Problem | Structure | Invariant | Front/Top = |
|---------|-----------|-----------|-------------|
| #239 Sliding Window Max | Decreasing deque | values decrease | current max |
| Sliding Window Min | Increasing deque | values increase | current min |
| #84 Largest Rectangle | Increasing stack | heights increase | pending left boundary |
| #496 Next Greater Element | Decreasing stack | values decrease | awaiting greater |
| #739 Daily Temperatures | Decreasing stack | values decrease | awaiting warmer day |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| k=1 | nums itself | Window of 1, max = element |
| k=n | [max(nums)] | Single window covering all |
| All same | same value repeated | Deque always has one element |
| Decreasing array | every window's first element | Front always expires, new front = leftmost in window |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Sliding window minimum | Find minimum instead of maximum | Monotonic INCREASING deque |
| Sliding window average | Mean over window | Running sum / k; O(n) without deque |
| Sliding window median | Median over window (#480) | Two heaps (max-heap for left half, min-heap for right) |
| Variable window size | Window changes dynamically | Reprocess with new k; or maintain a sorted multiset |
| 2D sliding window maximum | Max in k×k subgrid | Two-pass: row-wise then column-wise sliding window max |
| Sum of subarray minimums (#907) | Sum of min of every subarray | Monotonic stack; contribution technique |
| Number of subarrays with max > k | Count subarrays | Monotonic deque + two-pointer |

**2D sliding window max:** Apply the 1D algorithm twice — first across each row (producing a row-wise max matrix), then down each column. Time: O(mn) — the same deque approach applied twice.

**Morphological dilation connection:** This 2D extension is exactly OpenCV's `cv::dilate()` — the separable row+column approach. Each pass runs in O(n) using the monotonic deque, making the full 2D dilation O(mn) instead of O(mn×k²). See USE_CASES.md.

**Monotonic structure generalisation:** The deque maintains a monotonically decreasing sequence. This same structure — "if the new element is better than the tail, discard the tail" — appears in: sliding window max, next greater element, daily temperatures, largest rectangle in histogram, and trapping rain water (stack approach).
