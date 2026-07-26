class Solution {

    // Unique Paths II — LeetCode #63
    //
    // Unique Paths (#62) with obstacles. Same grid recurrence:
    //     dp[r][c] = dp[r-1][c] + dp[r][c-1]
    // except an OBSTACLE has zero paths through it:
    //     dp[r][c] = 0   when grid[r][c] == 1
    //
    // 1D rolling array: dp[c] needs only the row above (old dp[c]) and the cell to the left
    // (new dp[c-1]), so a single row of width n suffices, swept left to right.
    //
    // The wrinkle vs #62: an obstacle must ACTIVELY write dp[c] = 0. In plain #62 you never
    // write a zero, so it's the easy thing to forget when shrinking to 1D.
    //
    // Time: O(m*n).  Space: O(n).
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[] dp = new int[n];
        dp[0] = (obstacleGrid[0][0] == 0) ? 1 : 0;      // trapped at the start => 0

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (obstacleGrid[r][c] == 1) {
                    dp[c] = 0;                          // obstacle: no paths through this cell
                } else if (c > 0) {
                    dp[c] += dp[c - 1];                 // above (old dp[c]) + left (new dp[c-1])
                }
                // c == 0 and open: dp[0] stays as the value carried from directly above
            }
        }
        return dp[n - 1];
    }

    // 2D reference version — clearer, O(m*n) space.
    public int uniquePathsWithObstacles2D(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1) return 0;

        int[][] dp = new int[m][n];
        dp[0][0] = 1;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1) { dp[r][c] = 0; continue; }
                if (r > 0) dp[r][c] += dp[r - 1][c];
                if (c > 0) dp[r][c] += dp[r][c - 1];
            }
        }
        return dp[m - 1][n - 1];
    }
}

/*
 * Trace (1D) — [[0,0,0],[0,1,0],[0,0,0]]
 * --------------------------------------
 *   init:            dp = [1, 0, 0]
 *   row 0:           dp = [1, 1, 1]
 *   row 1 [0,1,0]:   c1 obstacle -> dp[1]=0; c2 -> 1+0=1   dp = [1, 0, 1]
 *   row 2 [0,0,0]:   c1 -> 0+1=1; c2 -> 1+1=2              dp = [1, 1, 2]
 *   answer = 2
 *
 * The obstacle's 0 at row1/c1 removes one contributor to the cells below and right of it — that
 * propagation is the whole mechanism.
 *
 * With no obstacles this reduces to Unique Paths (#62) = C(m+n-2, m-1).
 */
