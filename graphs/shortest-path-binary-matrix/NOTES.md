# Shortest Path in Binary Matrix — Notes & Intuition

**LeetCode #1091** | Graphs / BFS / Shortest Path | Medium

---

## Problem

Find the shortest clear path from `(0,0)` to `(n-1,n-1)` in an n×n binary
matrix. Clear = cell value is 0. Movement is 8-directional. Return path
length (cell count) or -1 if impossible.

```
[[0,1],[1,0]]          →  2  (diagonal)
[[0,0,0],[1,1,0],[1,1,0]]  →  4
[[1,0,0],[1,1,0],[1,1,0]]  →  -1  (start blocked)
```

---

## How This Differs from 01 Matrix (#542)

| Feature | 01 Matrix | Shortest Path Binary Matrix |
|---------|-----------|----------------------------|
| Movement | 4-directional | **8-directional** |
| Goal | Distance matrix (all cells) | **Single shortest path** |
| BFS type | **Multi-source** (all zeros) | **Single-source** (from (0,0)) |
| Return | `int[][]` distance array | **Single integer** |

---

## Algorithm — Single-Source BFS

```java
// Edge cases
if (grid[0][0] == 1 || grid[n-1][n-1] == 1) return -1;
if (n == 1) return 1;

// BFS with 8 directions
queue.offer({0, 0, 1}); grid[0][0] = 1;  // mark on enqueue

while (!queue.isEmpty()):
    (r, c, dist) = dequeue
    for each of 8 neighbours (nr, nc):
        if blocked or out-of-bounds: skip
        if (nr,nc) == destination: return dist + 1
        mark visited; enqueue (nr, nc, dist+1)

return -1
```

---

## Why BFS (Not DFS)?

BFS guarantees the **first time it reaches the destination is the shortest path**.
DFS might find a long path before discovering a short one — wrong answer.

BFS explores all paths of length k before exploring any path of length k+1.
So the first time `(n-1, n-1)` is seen = via the shortest possible path. ✓

---

## Why Check Destination Among Neighbours (Not After Dequeue)?

Checking when adding to queue (`dist + 1`) saves enqueuing and then
dequeuing the destination — one BFS iteration saved.
Both approaches give the same answer; checking at enqueue is slightly faster.

---

## Why 8-Directional?

```
4-dir shortest path through n×n clear grid: 2n-1 cells (right then down)
8-dir shortest path through n×n clear grid: n cells (pure diagonal)
```

Diagonal moves cost the same as horizontal/vertical. This models realistic
movement on a chessboard (king's moves) or unit movement in games.

---

## Full Trace — `[[0,0,0],[1,1,0],[1,1,0]]`

```
Init: queue=[(0,0,1)], grid[0][0]=1

Process (0,0,1):
  (0,1)=0 → enqueue (0,1,2), mark
  others blocked/OOB

Process (0,1,2):
  (0,2)=0 → enqueue (0,2,3), mark
  (1,2)=0 → enqueue (1,2,3), mark

Process (0,2,3): (1,2) already marked

Process (1,2,3):
  (2,2)=0, is destination → return 3+1 = 4 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(n²) — each cell visited at most once |
| Space | O(n²) — queue + in-place grid marking |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `grid[0][0]==1` | -1 | Start blocked |
| `grid[n-1][n-1]==1` | -1 | End blocked |
| n=1, `grid=[[0]]` | 1 | Start = end = 1 cell |
| Fully clear grid | n | Diagonal sweep: n cells |
| Fully blocked (except start/end) | -1 | No passable path |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return actual path | Record parent pointers | BFS with parent array; backtrack |
| Weighted grid | Each cell has a cost | Dijkstra (priority queue) |
| Multiple targets | Find nearest of k targets | Multi-source BFS from targets |
| A* optimisation | Heuristic to guide search | Add `dist + heuristic(r,c)` priority |
| 4-directional only | No diagonal | Remove 4 diagonal directions from dirs |
| Immutable grid | Can't mark grid | Use separate `boolean[][] visited` |

---

## Connection to Lee Algorithm

This problem IS the Lee algorithm (C.Y. Lee, 1961):
- BFS wavefront expansion from source
- Distance labels assigned to each reachable cell
- Backtrack from destination to reconstruct path
- Originally designed for PCB wire routing on a grid

See USE_CASES.md for the full historical context.
