# Unique Paths II — Notes & Intuition

**LeetCode #63** | Grid DP | Medium
[Unique Paths #62](#dynamic-programming/unique-paths) with obstacles. Same recurrence, one extra
rule — and a subtle wrinkle when you shrink to 1D.

---

## Problem

A robot walks from top-left to bottom-right of an `m×n` grid, moving only **right** or **down**.
Some cells are **obstacles** (`1`) and cannot be entered. Count the paths.

```
[[0,0,0],
 [0,1,0],     →  2 paths   (around the single obstacle, two ways)
 [0,0,0]]
```

---

## The Only Change From #62

The recurrence is identical — a cell is reached from above plus from the left — **except an
obstacle has zero paths through it**:

```
dp[r][c] = (grid[r][c] == 1) ? 0
                             : dp[r-1][c] + dp[r][c-1]
```

That's the whole idea. The obstacle acts as a hard `0` that "poisons" every path trying to pass
through it, and that zero propagates naturally to the cells downstream.

**The base cases also change.** In #62 the entire first row and column are `1`. Here, the first
row is `1` **only up to the first obstacle** — once a wall appears, every cell after it in that row
is unreachable (`0`), because the only way along the top edge is straight right.

---

## 2D Version

```java
public int uniquePathsWithObstacles(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    if (grid[0][0] == 1) return 0;                 // trapped at the start

    int[][] dp = new int[m][n];
    dp[0][0] = 1;
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++) {
            if (grid[r][c] == 1) { dp[r][c] = 0; continue; }   // obstacle
            if (r > 0) dp[r][c] += dp[r-1][c];                 // from above
            if (c > 0) dp[r][c] += dp[r][c-1];                 // from left
        }
    return dp[m-1][n-1];
}
```

**O(mn)** time, **O(mn)** space.

---

## The 1D Shrinkage — and Its Wrinkle

Just like #62, `dp[r][c]` needs only the row above and the cell to the left, so **one row
suffices** — sweep left to right, and `dp[c]` (old) is "above" while `dp[c-1]` (new) is "left."

**The wrinkle: an obstacle must set `dp[c] = 0`.** In plain #62 you never write a zero, so it's
easy to forget — here it's the crux. When you hit an obstacle you *overwrite* the carried-down
value with `0`, which correctly says "no paths reach this cell," and that zero then flows into the
cells below and to the right on subsequent updates.

```java
public int uniquePathsWithObstacles(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    dp[0] = (grid[0][0] == 0) ? 1 : 0;

    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++) {
            if (grid[r][c] == 1) {
                dp[c] = 0;                 // ← the wrinkle: obstacle zeroes this cell
            } else if (c > 0) {
                dp[c] += dp[c-1];          // above (old dp[c]) + left (new dp[c-1])
            }
            // c == 0, no obstacle: dp[0] stays = paths from directly above (correct)
        }
    return dp[n-1];
}
```

**O(mn)** time, **O(n)** space.

**Why `dp[0]` is left alone when `c==0`:** the first column can only be reached from directly
above, which is exactly the *old* `dp[0]` — so doing nothing is the correct "carry down." Writing
`dp[0] += dp[-1]` would be both wrong and out of bounds; the `else if (c > 0)` guard is what
protects it.

> **The general rule this teaches:** rolling-array shrinkage isn't just "delete a dimension." You
> must account for **every place the 2D version wrote a value the 1D version now carries over** — a
> boundary reset, an obstacle zero, a base case. The obstacle here is the value that must be
> *actively written*, and forgetting it is the classic 1D bug.

---

## Trace — `[[0,0,0],[0,1,0],[0,0,0]]` (1D array)

```
init:              dp = [1, 0, 0]
row 0 (no obst):   dp = [1, 1, 1]        all reachable along the top
row 1 [0,1,0]:
   c0: dp[0]=1     (carry from above)
   c1: obstacle → dp[1]=0
   c2: dp[2]+=dp[1] → 1+0 = 1            dp = [1, 0, 1]
row 2 [0,0,0]:
   c0: dp[0]=1
   c1: dp[1]+=dp[0] → 0+1 = 1
   c2: dp[2]+=dp[1] → 1+1 = 2            dp = [1, 1, 2]
answer = dp[2] = 2 ✓
```

Watch how the obstacle's `0` at `row1/c1` cut off one contributor to the cells below it — that's
the mechanism.

---

## Edge Cases

| Case | Result |
|------|--------|
| start cell is an obstacle | `0` (trapped immediately) |
| end cell is an obstacle | `0` (unreachable) |
| a full obstacle row/column blocking the grid | `0` |
| no obstacles | reduces to [#62](#dynamic-programming/unique-paths) = `C(m+n-2, m-1)` |
| 1×1 open grid | `1` |

---

## The Grid DP Family

| # | Problem | Twist |
|---|---------|-------|
| [#62](#dynamic-programming/unique-paths) | Unique Paths | count paths, no obstacles |
| **#63** | **Unique Paths II** | **obstacles → zero out cells** |
| #64 | Minimum Path Sum | `min` instead of `+`, carry cost |
| #120 | Triangle | variable-width rows |
| #931 | Minimum Falling Path Sum | three predecessors (diagonals) |

**All of them are grid DP with an O(1) transition** — see the
[DP Taxonomy → Grid DP](#guides/DP_TAXONOMY). The unifying skill is the **rolling array**: because
each row depends only on the previous one, `O(mn)` space collapses to `O(n)` — you just have to
carry over *everything* the 2D version wrote, obstacles included.

**The through-line:** obstacles are just a hard `0` in the same recurrence, and the 1D version's
one subtlety is that you must *write* that zero, not skip the cell.
