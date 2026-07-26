# Unique Paths — Notes & Intuition

**LeetCode #62** | Dynamic Programming | Medium

---

## Problem

Count paths from top-left (0,0) to bottom-right (m-1,n-1) in an m×n grid,
moving only right or down.

```
m=3, n=7 → 28
m=3, n=2 → 3
```

---

## Core Insight — 2D Grid DP

Each cell can only be reached from the cell above or the cell to the left:
```
dp[r][c] = dp[r-1][c] + dp[r][c-1]
Base: entire first row = 1 (only way: go all right)
      entire first col = 1 (only way: go all down)
```

Space-optimise: process row by row, keeping a single 1D array.

---

## The 2D → 1D Shrinkage (why it works)

The full DP is a grid of `m×n` values, but look at what the recurrence actually reads:

```
dp[r][c] = dp[r-1][c] + dp[r][c-1]
             ▲ above      ▲ left
```

Every cell needs **only the row directly above it and the cell just to its left** — never
anything two rows back. So you never need the whole grid in memory; **one row is enough**, reused
top to bottom. That collapses `O(mn)` space to `O(n)`.

### The trick that makes one array serve as two

Keep a single array `dp[]` of width `n` and iterate `c` **left to right**. At the moment you're
about to update `dp[c]`:

```
dp[c]      still holds the OLD value → that's dp[r-1][c]  (the row ABOVE)   ✓
dp[c-1]    was just updated this pass → that's dp[r][c-1] (the cell LEFT)   ✓

so:   dp[c] = dp[c] + dp[c-1]
              └old┘   └new┘
```

The single line `dp[c] += dp[c-1]` **is** `dp[r][c] = dp[r-1][c] + dp[r][c-1]` — the old value
plays "above," the freshly-written neighbour plays "left." The left-to-right sweep is what keeps
both roles valid in one array.

```java
public int uniquePaths(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);                 // first row: one way to reach each cell
    for (int r = 1; r < m; r++)
        for (int c = 1; c < n; c++)
            dp[c] += dp[c - 1];         // above (old dp[c]) + left (new dp[c-1])
    return dp[n - 1];
}
```

**O(mn)** time, **O(n)** space.

> **The rolling-array principle, stated generally:** if `dp[r][…]` depends only on row `r-1` (and
> already-computed cells of row `r`), you need just **one or two rows**, not the whole grid. **The
> iteration direction is load-bearing** — sweep in the direction that keeps the
> already-updated cells representing the *current* row and the not-yet-updated cells representing
> the *previous* row. Get the direction wrong and you read a value from the wrong generation.
> (This is the same reason 0/1 knapsack sweeps capacity **descending** while unbounded sweeps
> **ascending** — see [Knapsack Variants](#guides/KNAPSACK_VARIANTS).)

Pick the array's length as the **smaller** of `m`, `n` to minimise space.

---

## Math Solution

Total steps = (m-1) down + (n-1) right = m+n-2 steps total.
Choose which m-1 of those steps go down:
```
Answer = C(m+n-2, m-1)
```

---

## The 2D Grid DP Family

| Problem | Variant |
|---------|---------|
| #62 Unique Paths | Count paths |
| [#63 Unique Paths II](#dynamic-programming/unique-paths-ii) | With obstacles |
| #64 Minimum Path Sum | Min cost to reach bottom-right |
| #120 Triangle | Variable-width, bottom to top |
| #931 Minimum Falling Path Sum | Diagonal moves allowed |

---

## Complexity

Time O(m×n) · Space O(n) (one row)
Math: Time O(min(m,n)) · Space O(1)
