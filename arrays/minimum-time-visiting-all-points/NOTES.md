# Minimum Time Visiting All Points — Notes & Intuition

**LeetCode #1266** | Arrays / Math | Easy

---

## Problem

Given points on a 2D grid, visit them in order using 8-directional movement
(up, down, left, right, and 4 diagonals). Return the minimum time to visit all.

```
[[1,1],[3,4],[-1,0]]  →  7
[[3,2],[-2,2]]        →  5
```

---

## Key Insight — Chebyshev Distance

With 8-directional movement, the minimum steps from point A to point B:

```
max(|x2 - x1|, |y2 - y1|)
```

This is the **Chebyshev distance** (also called the L∞ norm).

### Why?

Each diagonal step reduces BOTH the horizontal and vertical distance by 1 simultaneously.

```
From (1,1) to (3,4): dx=2, dy=3

Step 1: diagonal → (2,2), remaining dx=1, dy=2
Step 2: diagonal → (3,3), remaining dx=0, dy=1
Step 3: up       → (3,4), remaining dx=0, dy=0

Total = 3 steps = max(2,3) ✓
```

**General strategy:**
- Move diagonally `min(dx, dy)` times — covers both axes simultaneously
- Move straight `|dx - dy|` times — covers remaining distance on larger axis
- Total = `min(dx,dy) + |dx-dy| = max(dx,dy)`

---

## Why max(dx,dy) Is a Lower Bound

Each step reduces `max(remaining_dx, remaining_dy)` by **at most 1**:
- Diagonal move: both dx and dy decrease by 1 → max decreases by 1
- Straight move: one dimension decreases by 1 → max decreases by at most 1

So you need **at least** max(dx, dy) steps. Since we can achieve it → it's optimal.

---

## The Three Distance Metrics

| Metric | Formula | Grid movement |
|--------|---------|--------------|
| Manhattan | `|dx| + |dy|` | 4-directional (no diagonals) |
| Euclidean | `√(dx² + dy²)` | Continuous straight line |
| **Chebyshev** | **`max(|dx|, |dy|)`** | **8-directional (diagonals allowed)** |

**Fun fact:** Chebyshev distance = Manhattan distance after a 45° coordinate
rotation: substitute `u = x + y`, `v = x - y`. Under this transformation,
the L∞ ball becomes an L1 ball.

---

## Algorithm

```java
int time = 0;
for (int i = 1; i < points.length; i++) {
    int dx = Math.abs(points[i][0] - points[i-1][0]);
    int dy = Math.abs(points[i][1] - points[i-1][1]);
    time += Math.max(dx, dy);
}
return time;
```

Sum the Chebyshev distances between consecutive points.

---

## Full Traces

**`[[1,1],[3,4],[-1,0]]`**

| segment | from | to | dx | dy | max(dx,dy) |
|---------|------|-----|----|----|------------|
| 1 | (1,1) | (3,4) | 2 | 3 | **3** |
| 2 | (3,4) | (-1,0) | 4 | 4 | **4** |

Total = 7 ✓

**`[[3,2],[-2,2]]`**

| segment | from | to | dx | dy | max(dx,dy) |
|---------|------|-----|----|----|------------|
| 1 | (3,2) | (-2,2) | 5 | 0 | **5** |

Total = 5 ✓

---

## Complexity

| | |
|--|--|
| Time | O(n) — single pass over n-1 segments |
| Space | O(1) — two integer variables |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Single point `[[0,0]]` | 0 | No movement needed |
| Same point twice | 0 | max(0,0)=0 |
| Horizontal only `[[0,0],[5,0]]` | 5 | dy=0, max(5,0)=5 |
| Diagonal `[[0,0],[3,3]]` | 3 | dx=dy=3, max=3 |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Optimal visit order | Choose which point to visit next | TSP (Travelling Salesman) — NP-hard; use DP for small n |
| 4-directional movement | No diagonals | Manhattan distance instead of Chebyshev |
| Obstacles on grid | Some cells blocked | BFS for actual shortest path; not just distance formula |
| 3D space | Three coordinates | Chebyshev generalises: max(|dx|, |dy|, |dz|) |
| Weighted movement | Different costs per direction | Dijkstra's algorithm |
| Multiple agents | k robots visiting all points | Assign subsets; minimise max Chebyshev distance |
| Return to start | Must come back after last point | Add distance from last point back to first |

**Chebyshev to Manhattan transformation:** Chebyshev distance max(|dx|, |dy|) equals Manhattan distance after a 45° coordinate rotation: substitute u=x+y, v=x-y. Under this transformation, L∞ becomes L1. This bijection is useful for range queries on Chebyshev distance — convert to Manhattan and use standard range trees.

**When Chebyshev fails:** If the grid has obstacles, the formula `max(|dx|, |dy|)` is no longer valid — the actual shortest path may be longer. In those cases, run BFS with 8-directional movement. The formula is a lower bound; BFS gives the exact answer.
