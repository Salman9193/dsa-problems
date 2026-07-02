# Swim in Rising Water — Notes & Intuition

**LeetCode #778** | Graphs / Dijkstra's / Binary Search / DSU | Hard

---

## Problem

At time t, water covers all cells with elevation ≤ t. Find the minimum t
such that a path exists from (0,0) to (n-1,n-1) entirely through cells
with elevation ≤ t.

```
grid = [[0,2],[1,3]]  →  3
```

---

## This IS the Minimax Path Problem

Find the path that minimises the MAXIMUM elevation along it.

```
Swim in Rising Water:       minimize max grid[r][c]  along path  (node weight)
Path with Min Effort #1631: minimize max |Δheight|   along path  (edge weight)
```

Both are **minimax path problems** — the answer is the minimum over all
paths of the maximum weight encountered. The same three algorithms apply.

---

## Why max() Is a Valid Dijkstra Relaxation

```java
newT = Math.max(t, grid[nr][nc])
```

Dijkstra's correctness requires cost is non-decreasing as path extends.
`max(t, grid[nr][nc]) >= t` always → non-decreasing → greedy invariant holds.
First time destination is popped = optimal answer.

---

## Three Approaches

### 1. Modified Dijkstra (Preferred)

```
dist[r][c] = minimum max elevation to reach (r,c)
Priority queue: min-heap by elevation
Relaxation: newT = max(current, grid[nr][nc])
Early exit: return on first pop of (n-1,n-1)
```

### 2. Binary Search + BFS

Binary search on t. Check if path exists using only cells ≤ t via BFS.
```
lo = grid[0][0]  (must include start)
hi = n*n-1       (max possible elevation)
```
Monotone: if swimable at t, also at t+1 → binary search valid.

### 3. Kruskal's DSU

Sort cells by elevation. Add in order, unioning with added neighbours.
Return elevation when (0,0) and (n-1,n-1) first connect.

**Why:** MST-minimax theorem — minimax path lies on the MST.
Kruskal's processes cells in weight order; the first cell that connects
source to destination IS the bottleneck of the optimal path.

---

## Comparison Table

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Modified Dijkstra | O(n² log n) | O(n²) | Single pass, early exit — **preferred** |
| Binary Search + BFS | O(n² log n) | O(n²) | Clean separation: feasibility vs optimisation |
| Kruskal's DSU | O(n² log n) | O(n²) | Elegant MST insight |

---

## How This Relates to Other Graph Problems

| Problem | Cost function | Constraint | Algorithm |
|---------|--------------|-----------|-----------|
| #743 Network Delay Time | Sum of edge weights | None | Standard Dijkstra |
| #787 Cheapest Flights | Sum of edge weights | k hops | K-limited Bellman-Ford |
| #1631 Path Min Effort | Max edge weight (|Δh|) | None | Modified Dijkstra |
| #778 Swim in Rising Water | Max node weight | None | Modified Dijkstra |

---

## Full Trace — `[[0,2],[1,3]]`

```
dist=[[0,INF],[INF,INF]], pq=[(0,0,0)]

Pop (0,0,0):
  →(0,1): newT=max(0,2)=2 → dist[0][1]=2
  →(1,0): newT=max(0,1)=1 → dist[1][0]=1

Pop (1,1,0):
  →(1,1): newT=max(1,3)=3 → dist[1][1]=3

Pop (2,0,1):
  →(1,1): max(2,3)=3, not < 3 → skip

Pop (3,1,1): destination! → return 3 ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| 1×1 grid | grid[0][0] | Already at destination |
| All same elevation | that elevation | Any path works |
| Monotone increasing grid | grid[n-1][n-1] | Must pass through highest cell |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| With diagonal moves | 8-directional | Add 4 diagonal directions |
| 3D terrain | Volume of water | 3D BFS/Dijkstra |
| Multiple water sources | Sources at all borders | Multi-source BFS |
| Minimise total water (sum) | Standard shortest path | Standard Dijkstra |
