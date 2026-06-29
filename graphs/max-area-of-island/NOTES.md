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

---

## Union-Find Alternative

DSU can compute max area by tracking the **size of each component**:

```java
public int maxAreaOfIsland(int[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    int[] parent = new int[rows * cols];
    int[] size   = new int[rows * cols]; // size[root] = component size
    Arrays.fill(size, 1);
    for (int i = 0; i < rows * cols; i++) parent[i] = i;

    int[][] dirs = {{1,0},{0,1}}; // right + down only (process each edge once)
    int maxArea = 0;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] != 1) continue;
            maxArea = Math.max(maxArea, size[find(parent, r*cols+c)]);
            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                if (nr < rows && nc < cols && grid[nr][nc] == 1) {
                    int ra = find(parent, r*cols+c);
                    int rb = find(parent, nr*cols+nc);
                    if (ra != rb) {
                        // Merge smaller into larger; update size
                        parent[ra] = rb;
                        size[rb] += size[ra];
                        maxArea = Math.max(maxArea, size[rb]);
                    }
                }
            }
        }
    }
    return maxArea;
}
```

**Key addition over standard DSU:** `size[]` array tracks component size.
When two components merge, their sizes add. `maxArea` is updated at each merge.

**BFS vs DSU:** BFS/DFS is simpler and preferred here (no extra size array needed).
DSU shines when the grid is queried multiple times after dynamic cell additions —
`union(new_cell, neighbour)` updates max area in O(α) without re-running DFS.

**Related:** LeetCode #827 (Making a Large Island) — flip one 0→1, find max area.
DSU is the natural approach: precompute component sizes, then for each 0 cell
check the sum of sizes of its distinct neighbouring components + 1.

**Complete DSU helper methods (path compression + union by rank):**

```java
// find with path compression — O(α(n)) amortised
// After find(x), x points directly to root — future finds are O(1)
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
    return parent[x];
}

// union by rank — attach shorter tree under taller tree
// Keeps tree height O(log n) so find() stays near-O(1)
// Note: when tracking size[], we update size on the ROOT after merge
private void union(int[] parent, int[] rank, int[] size, int x, int y) {
    int px = find(parent, x), py = find(parent, y);
    if (px == py) return;
    if (rank[px] < rank[py]) {
        parent[px] = py; size[py] += size[px];
    } else if (rank[px] > rank[py]) {
        parent[py] = px; size[px] += size[py];
    } else {
        parent[py] = px; size[px] += size[py]; rank[px]++;
    }
}
```

*Why both matter:*
- Without path compression: find() is O(log n) per call (tree height after union by rank)
- Without union by rank: tree can degenerate to O(n) height → find() is O(n)
- Together (Tarjan 1975): amortised O(α(n)) per operation — effectively O(1)
