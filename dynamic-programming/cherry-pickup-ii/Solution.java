class Solution {

    // Approach: Bottom-Up 3D DP — O(r * c^2) time, O(c^2) space
    //
    // Both robots are always on the SAME row — state is (row, col1, col2).
    // dp[col1][col2] = max cherries from current row to bottom, given
    //   robot1 at col1 and robot2 at col2 on this row.
    //
    // Transition: each robot moves to one of 3 adjacent columns → 9 combinations.
    // Overlap: if col1 == col2, collect grid[row][col1] once, not twice.
    //
    // Bottom-up from last row → only two c×c layers needed at a time → O(c^2) space.
    public int cherryPickup(int[][] grid) {
        int r = grid.length, c = grid[0].length;
        int[][] dp = new int[c][c];

        // Base case: bottom row
        for (int c1 = 0; c1 < c; c1++)
            for (int c2 = 0; c2 < c; c2++)
                dp[c1][c2] = (c1 == c2) ? grid[r-1][c1]
                                         : grid[r-1][c1] + grid[r-1][c2];

        // Fill from second-to-last row upward
        for (int row = r - 2; row >= 0; row--) {
            int[][] ndp = new int[c][c];

            for (int c1 = 0; c1 < c; c1++) {
                for (int c2 = 0; c2 < c; c2++) {
                    // Cherries collected at this row (count once if same cell)
                    int cherries = (c1 == c2) ? grid[row][c1]
                                              : grid[row][c1] + grid[row][c2];

                    // Try all 9 combinations of next moves (dc1, dc2 ∈ {-1,0,1})
                    int best = 0;
                    for (int dc1 = -1; dc1 <= 1; dc1++) {
                        int nc1 = c1 + dc1;
                        if (nc1 < 0 || nc1 >= c) continue;
                        for (int dc2 = -1; dc2 <= 1; dc2++) {
                            int nc2 = c2 + dc2;
                            if (nc2 < 0 || nc2 >= c) continue;
                            best = Math.max(best, dp[nc1][nc2]);
                        }
                    }

                    ndp[c1][c2] = cherries + best;
                }
            }

            dp = ndp;
        }

        // Robot1 starts at col 0, Robot2 starts at col c-1
        return dp[0][c - 1];
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(r * c^2) — r rows, c^2 (col1,col2) states, 9 transitions each
 * Space: O(c^2)     — only two c×c layers kept at a time
 *
 * Key insight: both robots are always on the same row.
 *   Full state = (row, col1, col2) → 3D DP, not 4D.
 *   This reduces from O(r^2 * c^2) naive to O(r * c^2).
 *
 * Overlap handling:
 *   if col1 == col2: collect grid[row][col1] ONCE (not twice)
 *   else:            collect grid[row][col1] + grid[row][col2]
 *
 * Why bottom-up?
 *   We only depend on the NEXT row's values (row+1), not all future rows.
 *   Swapping dp ← ndp after each row keeps space O(c^2).
 *   Top-down with memo also works but uses O(r * c^2) space.
 *
 * Symmetry optimisation (optional):
 *   Robot1 always starts left of Robot2 → col1 ≤ col2 always.
 *   Only fill lower triangle of c×c table → halves computation.
 *   Not applied here to keep the code clean.
 *
 * Trace — grid=[[3,1,1],[2,5,1],[1,5,5],[2,1,1]], c=3
 * -----------------------------------------------------
 * Bottom row (row=3): dp[0][2]=2+1=3, dp[1][1]=1, dp[0][0]=2, ...
 *
 * Row 2 [1,5,5]:
 *   ndp[1][1] = 5 + max(dp[nc1][nc2] for all 9 moves) = 5 + 3 = 8
 *   ... (builds up through rows 1 and 0)
 *
 * Row 0 [3,1,1]:
 *   dp[0][2] = 3 + 1 + best_from_row1 = 24 ✓
 */
