# Number of Islands — Notes & Intuition

**LeetCode #200** | Graphs / BFS / DFS / Union-Find | Medium

---

## Problem

Count the number of islands in a 2D binary grid where `'1'` is land and
`'0'` is water. Islands are connected land cells (4-directional adjacency).

```
["1","1","0","0","0"]
["1","1","0","0","0"]   →  3 islands
["0","0","1","0","0"]
["0","0","0","1","1"]
```

---

## Core Insight — Flood Fill = Connected Component Count

Scan the grid. When an unvisited `'1'` is found:
1. Increment island count
2. Flood-fill the entire connected land mass (mark all reachable `'1'`→`'0'`)

Each flood-fill eliminates one complete island. After scanning all cells,
the counter equals the number of islands.

---

## Approach 1 — DFS (In-Place Flood Fill)

```java
if (grid[r][c] == '1') { islands++; dfs(grid, r, c); }

void dfs(grid, r, c):
    if out-of-bounds or '0': return
    grid[r][c] = '0'    // sink
    dfs all 4 directions
```

**Pro:** Simple, no extra space (mutates grid).
**Con:** Stack overflow risk for huge grids (O(mn) call depth for a single island).

---

## Approach 2 — BFS (Preferred in Production)

```java
queue.offer(start); grid[r][c] = '0';  // mark on ENQUEUE

while queue not empty:
    cell = queue.poll()
    for each valid '1' neighbour:
        grid[nr][nc] = '0'             // mark on ENQUEUE
        queue.offer(neighbour)
```

**Why mark on enqueue (not dequeue)?**
If marked on dequeue, the same cell can be enqueued by multiple neighbours
before being processed — wasted queue entries. Marking on enqueue ensures
each cell is enqueued exactly once.

**Pro:** No stack overflow risk. Same O(mn) time. Preferred for large grids.

---

## Approach 3 — Union-Find

Each land cell = a node. Union adjacent land cells. Count distinct roots.

```java
components = count of '1' cells
for each adjacent pair (land, land): union them; components--
return components
```

**Pro:** No grid mutation. O(α(mn)) per query after O(mn) build.
**Best when:** Multiple connectivity queries on the same grid.

---

## The α(mn) Complexity Explained

Union-Find with path compression + union by rank costs O(α(n)) amortised per operation, where α is the **inverse Ackermann function**:

```
α(n) = how many times you apply log* to n before reaching ≤ 1

n = 10^6:  log(10^6)≈20 → log(20)≈4.3 → log(4.3)≈2.1 → log(2.1)≈1 → done
α(10^6) = 4

n = 10^19: α(10^19) = 5
```

**For all practical grid sizes, α ≤ 5 — effectively constant.**

The total complexity O(mn × α(mn)) ≈ O(mn × 5) = O(mn).

The distinction matters theoretically (Tarjan 1975 proved O(mn α(mn)) is tight)
but not practically. In interviews: **"O(mn) amortised, technically O(mn α(mn))"**.

---

## Comparison

| Approach | Time | Space | Mutates grid? | Stack overflow? |
|----------|------|-------|--------------|----------------|
| DFS | O(mn) | O(mn) call stack | Yes | Yes (large grids) |
| BFS | O(mn) | O(mn) queue | Yes | No |
| Union-Find | O(mn α(mn)) | O(mn) | No | No |

---

## Trace — 3-Island Grid

```
["1","1","0","0","0"]
["1","1","0","0","0"]
["0","0","1","0","0"]
["0","0","0","1","1"]

Scan (0,0)='1': islands=1, BFS sinks {(0,0),(0,1),(1,0),(1,1)}
Scan (2,2)='1': islands=2, BFS sinks {(2,2)}
Scan (3,3)='1': islands=3, BFS sinks {(3,3),(3,4)}
return 3 ✓
```

---

## Edge Cases

| Grid | Islands | Reason |
|------|---------|--------|
| All `'0'` | 0 | No land |
| All `'1'` | 1 | One giant island |
| 1×1 `['1']` | 1 | Single land cell |
| Diagonal land only | n | Diagonal is NOT connected (4-dir only) |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #695 Max Area of Island | Return largest island size | DFS returning size |
| #463 Island Perimeter | Count boundary edges | For each '1', add 4 minus adjacent '1' count |
| #827 Making a Large Island | Flip one '0', find max island | Union-Find + try each '0' |
| 8-connectivity | Diagonals also count | Add 4 diagonal dirs to BFS/DFS |
| Count perimeter only | Don't count interior cells | DFS tracking boundary cells |
| Stream of grid updates | Cells flip '0'→'1' online | Union-Find handles online insertions |
