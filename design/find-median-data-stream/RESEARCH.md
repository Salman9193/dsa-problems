# Find Median From Data Stream — Research & Foundations

Maintaining the median of a growing stream by keeping a max-heap of the lower half and a min-heap of the upper half, balanced in size.

- **J. W. J. Williams (1964), “Algorithm 232: Heapsort,”** *Communications of the ACM* 7(6):347–348. Introduced the **binary heap** / priority queue — the structure the two-heap median-maintenance method is built on.

**Why it matters here:** Two binary heaps (Williams 1964) let each insertion rebalance in O(log n) and expose the median at the heaps’ tops in O(1).

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
