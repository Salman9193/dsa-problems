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

Space-optimise: process row by row, keeping a single 1D array where
`dp[c]` holds the value from the row above (before update) and `dp[c-1]`
holds the value from the left (just updated).

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
| #63 Unique Paths II | With obstacles |
| #64 Minimum Path Sum | Min cost to reach bottom-right |
| #120 Triangle | Variable-width, bottom to top |
| #931 Minimum Falling Path Sum | Diagonal moves allowed |

---

## Complexity

Time O(m×n) · Space O(n) (one row)
Math: Time O(min(m,n)) · Space O(1)
