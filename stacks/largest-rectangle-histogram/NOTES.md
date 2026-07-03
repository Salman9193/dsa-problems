# Largest Rectangle in Histogram — Notes & Intuition

**LeetCode #84** | Stack / Monotonic Stack | Hard

---

## Problem

Find the area of the largest rectangle in a histogram.

```
heights=[2,1,5,6,2,3] → 10  (bars 5 and 6, width=2, height=5)
```

---

## Key Insight

For each bar, find the farthest left and right it can extend at its height
(until hitting a shorter bar). Area = height × span.

---

## Monotonic Increasing Stack

Stack maintains bar indices in INCREASING height order.
When bar i breaks the order (i.e., heights[i] < heights[stack.top]):
- Pop the taller bar: it can't extend further right
- Compute its rectangle: height × (right boundary - left boundary - 1)

```
for i from 0 to n (include sentinel 0 at n):
    while stack not empty AND heights[i] < heights[stack.top]:
        height = heights[stack.pop()]
        width = (stack empty) ? i : i - stack.peek() - 1
        maxArea = max(maxArea, height × width)
    stack.push(i)
```

**Sentinel at end (height=0):** flushes all remaining bars from the stack.

---

## Daily Temperatures vs Largest Rectangle

| | Daily Temperatures | Largest Rectangle |
|--|-------------------|------------------|
| Stack order | Decreasing | **Increasing** |
| Resolves when | Finding GREATER | Finding SMALLER |
| Pattern | "Next greater" | "Next smaller" |

---

## Extends To

- **#85 Maximal Rectangle** — apply histogram algorithm row by row
- **#42 Trapping Rain Water** — alternative monotonic stack approach

---

## Complexity

Time O(n) · Space O(n)
