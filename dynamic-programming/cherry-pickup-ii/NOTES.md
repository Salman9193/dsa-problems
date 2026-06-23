# Cherry Pickup II — Notes & Intuition

**LeetCode #1463** | Dynamic Programming | Hard

---

## Problem

Two robots start at top-left (col 0) and top-right (col c-1). Both move
simultaneously downward, each shifting left/same/right per row. Cherries
shared by both robots are collected once. Return maximum total cherries.

```
grid = [[3,1,1],
        [2,5,1],
        [1,5,5],
        [2,1,1]]  →  24
```

---

## Why 3D DP (Not 4D)

Since both robots always move to the **next row simultaneously**, they are
always on the **same row**. Their combined state is `(row, col1, col2)` —
just 3 dimensions, not 4.

```
Naive: track (row1, col1, row2, col2) → O(r^2 * c^2)
Smart: row1 == row2 always → track (row, col1, col2) → O(r * c^2)
```

---

## DP Definition

```
dp[col1][col2] = max cherries collectible from current row to bottom,
                 given robot1 at col1 and robot2 at col2
```

**Transition:** each robot moves to one of 3 adjacent columns → 9 combinations:
```
dp[c1][c2] = collect(row, c1, c2) + max over all (nc1, nc2) of dp[nc1][nc2]
```

**Overlap:** if `c1 == c2`, count the cell once:
```java
int cherries = (c1 == c2) ? grid[row][c1] : grid[row][c1] + grid[row][c2];
```

---

## Bottom-Up vs Top-Down

Both are O(r × c²) time. Bottom-up:
- Processes row by row, only needs the next row's values
- Space reduces to O(c²) — just two c×c layers
- More cache-friendly

Top-down with memoisation:
- More natural to write
- Uses O(r × c²) space for the memo table

---

## Algorithm Skeleton

```java
// Base case: last row
for (c1, c2): dp[c1][c2] = grid[r-1][c1] + grid[r-1][c2] (or once if same)

// Fill upward
for row from r-2 to 0:
    for each (c1, c2):
        cherries = collect(row, c1, c2)
        best = max(dp[nc1][nc2]) for dc1,dc2 in {-1,0,1}
        ndp[c1][c2] = cherries + best
    dp = ndp

return dp[0][c-1]   // starting positions
```

---

## Full Trace — `[[3,1,1],[2,5,1],[1,5,5],[2,1,1]]`

**Row 3 (base):**
```
dp[0][2] = grid[3][0] + grid[3][2] = 2+1 = 3
dp[0][0] = grid[3][0] = 2 (same cell)
dp[1][1] = grid[3][1] = 1
...
```

**Row 2: `[1,5,5]`**
```
ndp[1][1]: cherries=5, best=max(dp[nc1][nc2])=3 → ndp[1][1]=8
ndp[0][2]: cherries=1+5=6, best=... → builds toward 24
```

**Rows 1,0:** continue accumulating until `dp[0][2]=24` ✓

---

## Complexity

| | |
|--|--|
| Time | O(r × c²) — r rows, c² states, 9 transitions |
| Space | O(c²) — rolling two c×c layers |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| 1 row | `grid[0][0] + grid[0][c-1]` (or once if c=1) | Base case only |
| c=1 | All on same column — always overlap | Collect once per row |
| All zeros | 0 | Nothing to collect |
| Both paths never overlap | Sum of both best paths | No overlap penalty |

---

## Related Problems

| Problem | Similarity |
|---------|-----------|
| #741 Cherry Pickup (I) | One robot, two passes; similar 3D DP trick |
| #64 Minimum Path Sum | Single robot, grid DP |
| Multi-agent MAPF (research) | n robots, cooperative path planning |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Cherry Pickup I (#741) | One robot, two passes (down then up) | Simulate both passes simultaneously with same 3D DP trick |
| k robots | More than 2 robots | k-dimensional DP: O(r × c^k); intractable for large k |
| Robots must not share path | No cell visited twice | Add constraint: if col1==col2, one robot must skip |
| Collect minimum (not maximum) | Minimise cherries (costs) | Change max to min in transition |
| Different movement rules | Jump instead of step | Modify the 9-combination transition |
| 3D grid | Three dimensions | Extend DP to O(r × c² × d) |

**Why k>2 robots is intractable:** The joint state is (row, col1, col2, ..., colk) — a k+1 dimensional DP with c^k states per row. For k=10, c=100, this is 100¹⁰ states per row — exponential in k. Real warehouse systems use CBS (Conflict-Based Search) or MARL approximations. See USE_CASES.md.

**The key insight about synchrony:** Both robots move to the SAME next row simultaneously. This is what reduces the state from (row1, col1, row2, col2) — 4D — to (row, col1, col2) — 3D. If robots moved asynchronously, 4D DP would be required.
