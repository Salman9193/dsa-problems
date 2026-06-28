# Pacific Atlantic Water Flow — Notes & Intuition

**LeetCode #417** | Graphs / BFS / Multi-Source | Medium

---

## Problem

Given an m×n elevation grid, find all cells from which water can flow to
BOTH the Pacific Ocean (top + left borders) AND the Atlantic Ocean
(bottom + right borders). Water flows downhill (≤) or on equal ground.

---

## The Key Insight — Reverse the Problem

**Naive (too slow):** BFS from each cell, check if it reaches both oceans.
O(mn) per cell × mn cells = O(m²n²).

**Smart (O(mn)):** reverse the question.
```
Forward:  "can cell X flow DOWN to ocean?"
Reverse:  "can the ocean flow UP to cell X?"
```
These are logically equivalent — X drains to ocean iff ocean can "reach" X uphill.

Run two BFS from ocean borders using the **uphill (≥)** condition:
- Pacific BFS: seed all top-row + left-column cells
- Atlantic BFS: seed all bottom-row + right-column cells

Answer = cells marked by BOTH BFS runs.

---

## Why ≥ (Not ≤) in Reverse BFS?

```
Forward water flow:   A → B  if height[B] ≤ height[A]  (B is lower or equal)
Reverse ocean reach:  ocean → A  if height[A] ≥ height[ocean_cell]  (A is higher or equal)
```

From the ocean border (height h), we expand to neighbours with height ≥ h.
Those higher cells are the ones that can drain DOWN to the border cell.

---

## Algorithm Skeleton

```java
// Seed both queues from borders
for each Pacific border cell:  pacific[r][c]=true; pacQueue.offer(...)
for each Atlantic border cell: atlantic[r][c]=true; atlQueue.offer(...)

// Run uphill BFS for each ocean
bfs(heights, pacQueue,  pacific)   // condition: height[nr][nc] >= height[curr]
bfs(heights, atlQueue, atlantic)

// Collect intersection
for (r,c): if pacific[r][c] && atlantic[r][c]: result.add([r,c])
```

---

## Full Trace — `[[1,2,3],[8,9,4],[7,6,5]]`

```
Pacific seeds (top + left): (0,0)=1,(0,1)=2,(0,2)=3,(1,0)=8,(2,0)=7
Pacific BFS uphill:
  (1,0)=8 → (1,1)=9≥8 ✓ → pacific reaches all cells

Atlantic seeds (right + bottom): (0,2)=3,(1,2)=4,(2,2)=5,(2,1)=6,(2,0)=7
Atlantic BFS uphill:
  (2,2)=5 → (2,1)=6≥5 ✓
  (2,1)=6 → (2,0)=7≥6 ✓, (1,1)=9≥6 ✓
  (1,2)=4 → (0,2)=3<4 ✗
  Atlantic = {(0,2),(1,2),(2,0),(2,1),(2,2),(1,1)}

Intersection = {(0,2),(1,1),(2,0),(2,1),(2,2)} ✓
```

---

## Why Not DFS?

DFS works equally well here — same O(mn) complexity. BFS used in the
solution above because it's more natural for "spread from borders" problems.
DFS recursive implementation is simpler in code but risks stack overflow
for very large grids (m×n = 10⁶).

---

## Complexity

| | |
|--|--|
| Time | O(mn) — each cell visited at most twice |
| Space | O(mn) — two visited arrays + two queues |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| 1×1 grid | `[[0,0]]` | Touches both borders |
| All same height | All cells | All can flow everywhere |
| Monotone increasing (top-left to bottom-right) | Top-right + bottom-left corner region | Height gradient dictates flow |
| Single row | All cells | Top = Pacific, bottom = Atlantic, same row |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| 8-directional flow | Diagonals allowed | Add 4 diagonal dirs |
| Weighted flow | Cost to cross each cell | Dijkstra (priority queue) |
| k oceans | Multiple ocean boundaries | k BFS runs; intersect all |
| 3D terrain | Add depth dimension | Same BFS with 6 directions |
| Flow accumulation | Count how many cells drain into each cell | DFS counting children |
| Drainage divide | Find cells on the exact boundary | Cells in both but with adjacent cells in only one |

---

## Connection to Hydrology

This problem is the algorithmic version of watershed delineation:
- heights[] = Digital Elevation Model (DEM) raster
- Pacific border = one ocean/basin outlet
- Atlantic border = another ocean/basin outlet
- Result = drainage divide cells (cells draining to both basins)

See USE_CASES.md for the full GIS connection.
