# Minimum Path Cost in a Grid — Notes & Intuition

**LeetCode #2304** | Grid DP with a non-constant transition | Medium
Looks like [Unique Paths #62](#dynamic-programming/unique-paths), but the transition is a **loop
over the whole previous row**, not two fixed neighbours — which bumps it from O(mn) to O(mn²).

---

## Problem

An `m×n` grid of **distinct** values `0 … m·n−1`. From a cell you may move to **any** column of
the next row. Each move has a cost: `moveCost[v][j]` is the cost of moving from a cell **whose
value is `v`** into **column `j`** of the next row.

The cost of a path = **sum of cell values visited** + **sum of move costs**. Start from any cell in
the first row, end at any cell in the last row. Minimise it.

```
grid = [[5,3],[4,0],[2,1]]     →  17   (path 5 → 0 → 1)
  values: 5 + 0 + 1 = 6
  moves:  cost(5→col0)=3 + cost(0→col1)=8 = 11
  total:  6 + 11 = 17
```

---

## Why This Isn't Just Unique Paths

In [Unique Paths](#dynamic-programming/unique-paths) / [Min Path Sum #64], a cell is reached from
**two fixed neighbours** (above, left) — an **O(1)** transition. Here you can come from **any of the
`n` columns** in the row above, and the move cost depends on **which** cell you left:

```
Unique Paths:   dp[r][c] = dp[r-1][c] + dp[r][c-1]              ← 2 sources, O(1)
Min Path Cost:  dp[r][c] = grid[r][c] +
                    min over k of ( dp[r-1][k] + moveCost[grid[r-1][k]][c] )   ← n sources, O(n)
```

That inner `min over k` is a **non-constant transition** — the defining feature of the
[non-constant-transition DP type](#guides/DP_TAXONOMY). So this problem is a **hybrid**: grid-shaped
states, but a per-cell transition that loops. That's why it's `O(m·n²)`, not `O(m·n)`.

> The taxonomy framing pays off here: *total = states × transition-cost = O(mn) × O(n) = O(mn²).*
> The moment you see "from any cell in the previous row," you know the transition isn't O(1).

---

## The Recurrence

`dp[r][c]` = minimum cost of a path ending at cell `(r, c)`:

```
dp[0][c] = grid[0][c]                                  // first row: just the cell's value
dp[r][c] = grid[r][c] + min over k in [0,n) of
              ( dp[r-1][k] + moveCost[ grid[r-1][k] ][ c ] )
answer   = min over c of dp[m-1][c]
```

**The subtlety that trips people:** the move-cost index is `grid[r-1][k]` — the **value of the
source cell**, not its column and not the current cell. Read it as *"I was standing on a cell whose
value was `v`; what does it cost to step into column `c`?"*

---

## Solution — 1D Rolling Array

Each row needs only the previous row's dp, so the same **rolling-array shrinkage** as Unique Paths
applies (see [#62's explanation](#dynamic-programming/unique-paths)) — one caveat below.

```java
public int minPathCost(int[][] grid, int[][] moveCost) {
    int m = grid.length, n = grid[0].length;
    int[] dp = grid[0].clone();                        // first row = cell values

    for (int r = 1; r < m; r++) {
        int[] next = new int[n];
        Arrays.fill(next, Integer.MAX_VALUE);          // ← MUST init to +inf, we're minimising
        for (int c = 0; c < n; c++) {                  // target column, current row
            for (int k = 0; k < n; k++) {              // source column, previous row
                int cost = dp[k] + moveCost[grid[r-1][k]][c] + grid[r][c];
                next[c] = Math.min(next[c], cost);
            }
        }
        dp = next;                                     // fresh array — NOT in place
    }

    int ans = Integer.MAX_VALUE;
    for (int v : dp) ans = Math.min(ans, v);
    return ans;
}
```

**O(m·n²)** time, **O(n)** space.

### The rolling-array caveat here

Unlike Unique Paths, you **cannot update `dp` in place** — you write a **fresh `next[]` row**. Why:
every target column `c` reads **all** of the previous row's values (`dp[k]` for all `k`), so if you
overwrote `dp[c]` mid-row you'd corrupt a value that a later `c` still needs to read.

> **The general rule:** in-place rolling works only when each cell reads a *bounded, already-final*
> slice of the previous row (Unique Paths reads just `dp[c]`). When the transition reads the
> **whole** previous row, keep the two rows separate. This is the same reason a general
> non-constant transition needs its previous layer intact.

Two other easy mistakes (both from the problem's structure):
- **Initialise `next[]` to +∞** before taking `min`, or every cell stays 0.
- **Index `moveCost` by the source cell's value** `grid[r-1][k]`, not by `k` and not by the current
  cell — the single most common bug on this problem.

---

## Trace — `grid = [[5,3],[4,0],[2,1]]`

```
dp (row0) = [5, 3]                          // the two starting values

row1 = [4,0], values grid[0]=[5,3]:
  c0 (val 4): min( dp[0]+moveCost[5][0], dp[1]+moveCost[3][0] ) + 4
            = min( 5+9, 3+18 ) + 4 = 14+4 = 18
  c1 (val 0): min( dp[0]+moveCost[5][1], dp[1]+moveCost[3][1] ) + 0
            = min( 5+8, 3+6 ) + 0 = 9
  dp = [18, 9]

row2 = [2,1], values grid[1]=[4,0]:
  c0 (val 2): min( 18+moveCost[4][0], 9+moveCost[0][0] ) + 2
            = min( 18+2, 9+9 ) + 2 = 18+2 = 20
  c1 (val 1): min( 18+moveCost[4][1], 9+moveCost[0][1] ) + 1
            = min( 18+4, 9+8 ) + 1 = 17+1 = 18
  dp = [20, 18]

answer = min(20, 18) = 17 ✓   (the 5 → 0 → 1 path)
```

---

## Edge Cases

| Case | Handling |
|------|----------|
| single row (`m == 1`) | answer = `min(grid[0])`, no moves |
| single column (`n == 1`) | forced path; still sum values + moves |
| large values | costs can exceed `int`; use `long` if `m·n·maxCost` overflows |
| `next[]` not initialised to ∞ | classic bug → wrong (too-small) answers |

---

## The Grid DP Family — and Where This Sits

| # | Problem | Transition |
|---|---------|-----------|
| [#62](#dynamic-programming/unique-paths) | Unique Paths | **O(1)** — 2 fixed neighbours |
| [#63](#dynamic-programming/unique-paths-ii) | Unique Paths II | O(1) + obstacles |
| #64 | Minimum Path Sum | O(1) — min of 2 |
| **#2304** | **Minimum Path Cost** | **O(n) — min over the whole previous row** |
| #931 | Minimum Falling Path Sum | O(1) — 3 diagonal predecessors |
| #1289 | Min Falling Path Sum II | O(n), same shape as #2304 |

**#2304 and #1289 are the "non-constant transition" members of the grid family** — the ones where
the answer for a cell scans an entire previous row. Everything else is O(1) per cell.

> See [DP Taxonomy](#guides/DP_TAXONOMY): this problem lives at the seam between **Grid DP**
> (its state) and **Non-Constant-Transition DP** (its transition). Recognising that seam is what
> tells you the complexity is O(mn²) before you write a line.

**The through-line:** same grid-DP skeleton and same rolling-array shrinkage as Unique Paths, but
the transition scans the whole previous row — so you keep the rows separate and the cost gains a
factor of `n`.
