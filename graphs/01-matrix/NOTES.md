# 01 Matrix — Notes & Intuition

**LeetCode #542** | Graphs / Multi-Source BFS / DP | Medium

---

## Problem

Given an m×n binary matrix, return the distance of the nearest 0 for each cell.

```
[[0,0,0],[0,1,0],[1,1,1]]  →  [[0,0,0],[0,1,0],[1,2,1]]
```

---

## Why NOT BFS from Each 1?

BFS from each `1` to find its nearest `0` costs O(mn) per cell → O(m²n²) total.
For a 1000×1000 grid: 10^12 operations — too slow.

---

## Approach 1 — Multi-Source BFS (Invert the Problem)

Instead of asking "where is the nearest 0 from this 1?",
ask "how far can each 0 reach?"

Seed ALL zeros into the queue at distance 0. BFS propagates outward:
- Distance 1 cells = adjacent to a zero
- Distance 2 cells = adjacent to distance-1 cells (not already assigned)
- Distance k cells = k hops from the nearest zero

First time BFS reaches a cell = shortest distance from any zero. ✓

```java
// Init: zeros at 0, ones at MAX_VALUE
for each (r,c): if mat[r][c]==0: dist=0, enqueue; else dist=MAX_VALUE

// BFS: propagate distance outward
while queue not empty:
    cell = dequeue
    for each unvisited neighbour (nr,nc):  // MAX_VALUE = unvisited
        dist[nr][nc] = dist[cell] + 1
        enqueue (nr,nc)
```

---

## Approach 2 — Two-Pass DP (O(1) Extra Space)

The nearest zero must come from one of four quadrants. Two passes cover all:

```
Pass 1 (top-left → bottom-right):
  dist[r][c] = min(dist[r-1][c], dist[r][c-1]) + 1
  Captures: zeros above or to the left

Pass 2 (bottom-right → top-left):
  dist[r][c] = min(dist[r][c], dist[r+1][c]+1, dist[r][c+1]+1)
  Captures: zeros below or to the right
```

**Why INF = rows+cols (not Integer.MAX_VALUE)?**
Max distance in an m×n grid = m+n-2. `Integer.MAX_VALUE + 1` overflows to negative.
`rows+cols` is always safe.

---

## Comparison

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Multi-source BFS | O(mn) | O(mn) queue | Intuitive, extensible |
| Two-pass DP | O(mn) | O(1) extra | No queue, faster constant |

---

## Full Traces

**BFS — `[[0,0,0],[0,1,0],[1,1,1]]`**

```
Queue init: (0,0),(0,1),(0,2),(1,0),(1,2)
dist = [[0,0,0],[0,MAX,0],[MAX,MAX,MAX]]

(0,1)→(1,1): dist=1, enqueue
(1,0)→(2,0): dist=1, enqueue
(1,2)→(2,2): dist=1, enqueue
(1,1)→(2,1): dist=2, enqueue

dist = [[0,0,0],[0,1,0],[1,2,1]] ✓
```

**DP — `[[0,0,0],[0,1,0],[1,1,1]]`**

```
Pass 1 (top-left):
  (1,1): min(dist[0][1]+1, dist[1][0]+1) = min(1,1) = 1
  (2,0): min(dist[1][0]+1, INF)          = 1
  (2,1): min(dist[1][1]+1, dist[2][0]+1) = min(2,2) = 2
  (2,2): min(dist[1][2]+1, dist[2][1]+1) = min(1,3) = 1

Pass 2 (bottom-right): no improvements needed

dist = [[0,0,0],[0,1,0],[1,2,1]] ✓
```

---

## Edge Cases

| Grid | Output | Reason |
|------|--------|--------|
| All zeros | all 0s | Every cell is at distance 0 |
| Single zero corner | distance increases from corner | BFS/DP propagates outward |
| Single cell `[0]` | `[[0]]` | Distance to itself |
| Single cell `[1]` | problem guarantees ≥1 zero | Not a valid test case |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| 8-directional | Diagonals count | Add 4 diagonal dirs to BFS |
| Weighted distances | Moving costs differ | Dijkstra (weighted BFS) |
| Distance to nearest 1 | Invert roles | Seed all 1s, propagate to 0s |
| 3D grid | Depth dimension | Same BFS with 6 directions |
| Euclidean distance | Not Manhattan | Exact EDT algorithm (Borgefors 1986) |
| k nearest zeros | Multiple sources | Track k closest per cell |

---

## Connection to Rotting Oranges (#994)

| Feature | 01 Matrix (#542) | Rotting Oranges (#994) |
|---------|-----------------|----------------------|
| Sources | All zeros | All rotten oranges |
| Goal | Distance per cell | Time for last to rot |
| Level tracking | Not needed (store dist directly) | Needed (count minutes) |
| Return value | dist[][] matrix | Single integer |
| Impossible case | Always possible | -1 if isolated fresh |

Both use multi-source BFS — the difference is in what is recorded and returned.
