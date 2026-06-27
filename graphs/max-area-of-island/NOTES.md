# Max Area of Island — Notes & Intuition

**LeetCode #695** | Graphs / DFS / BFS | Medium

---

## Problem

Given a binary grid (1=land, 0=water), return the maximum area of any island.
Islands are 4-directionally connected groups of land cells.

```
[[0,0,1,0,0,0,0,1,0,0,0,0,0],    →  6
 [0,0,0,0,0,0,0,1,1,1,0,0,0],
 ...
 ]

[[0,0,0,0,0,0,0,0]]               →  0
```

---

## The Insight — DFS That Returns Area

Same flood-fill as Number of Islands (#200). The single change:
DFS returns `int` (area) instead of `void`.

```java
// #200 Number of Islands:  count islands
if (grid[r][c] == 1) { islands++; dfs(grid, r, c); }

// #695 Max Area of Island: measure area
if (grid[r][c] == 1) maxArea = Math.max(maxArea, dfs(grid, r, c));
```

The DFS accumulates area as it unwinds:

```java
private int dfs(int[][] grid, int r, int c) {
    if (out-of-bounds or water) return 0;
    grid[r][c] = 0;  // sink
    return 1 + dfs(up) + dfs(down) + dfs(left) + dfs(right);
}
```

Each reachable cell contributes exactly 1 to the total.

---

## Why `return 1 + ...` Works

The recursion forms a tree rooted at the starting cell.
Each node contributes 1. The sum of all nodes = island area.
DFS postorder: leaves return 1, each parent adds its own 1 to children's sums.

```
Island:   (0,0)─(0,1)
               │
             (1,1)─(2,1)─(2,2)

DFS tree:
  dfs(0,0) = 1 + dfs(0,1)
  dfs(0,1) = 1 + dfs(1,1)
  dfs(1,1) = 1 + dfs(2,1)
  dfs(2,1) = 1 + dfs(2,2)
  dfs(2,2) = 1 + 0+0+0+0 = 1
  → chain sums: 1,2,3,4,5 ✓
```

---

## DFS vs BFS

| | DFS | BFS |
|--|-----|-----|
| Code | Cleaner (recursive return) | More explicit |
| Stack overflow | Risk for large islands | None |
| Space | O(island size) call stack | O(island size) queue |

For large grids (n = 10⁴), a single island can be 10⁸ cells — DFS would overflow.
BFS is always safe.

---

## Full Trace — `[[1,1,0],[0,1,0],[0,1,1]]`

```
DFS from (0,0):
  sink (0,0) → 1 + dfs(0,1)
  sink (0,1) → 1 + dfs(1,1)
  sink (1,1) → 1 + dfs(2,1)
  sink (2,1) → 1 + dfs(2,2)
  sink (2,2) → 1

Unwind: 1←2←3←4←5

maxArea = 5 ✓
```

---

## Edge Cases

| Grid | Output | Reason |
|------|--------|--------|
| All zeros | 0 | No land |
| All ones | mn | One giant island |
| Single `1` | 1 | Smallest island |
| Diagonal `1`s only | 1 | Diagonals not connected (4-dir) |

---

## Comparison: #200 vs #695

| Feature | #200 Number of Islands | #695 Max Area of Island |
|---------|----------------------|------------------------|
| Return | Count of islands | Max island area |
| DFS return type | void | int |
| Accumulator | `islands++` outside DFS | `return 1 + ...` inside DFS |
| BFS tracks | nothing extra | `area++` per dequeued cell |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #463 Island Perimeter | Count boundary edges | For each '1': add 4 minus adjacent '1' count |
| #827 Making a Large Island | Flip one '0', find max | Union-Find: try each '0', merge adjacent islands |
| Count cells per island | Return all sizes | Collect DFS return values |
| Min area island | Smallest non-zero island | Track minArea alongside maxArea |
| 8-directional | Diagonals count too | Add 4 diagonal dirs to BFS/DFS |
