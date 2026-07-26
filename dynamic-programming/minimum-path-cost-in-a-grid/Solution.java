import java.util.Arrays;

class Solution {

    // Minimum Path Cost in a Grid — LeetCode #2304
    //
    // Grid DP, but with a NON-CONSTANT transition: you may move from a cell to ANY column of the
    // next row, and the move cost depends on the SOURCE cell's value. So each cell scans the whole
    // previous row:
    //
    //   dp[r][c] = grid[r][c] + min over k of ( dp[r-1][k] + moveCost[grid[r-1][k]][c] )
    //
    // That inner min-over-k is why this is O(m*n^2), not O(m*n) like Unique Paths.
    //
    // Rolling array: only the previous row is needed. BUT because every target column reads the
    // ENTIRE previous row, we must write a fresh next[] row rather than updating dp in place.
    //
    // Time: O(m * n^2).  Space: O(n).
    public int minPathCost(int[][] grid, int[][] moveCost) {
        int m = grid.length, n = grid[0].length;
        int[] dp = grid[0].clone();                      // first row: cost to reach = cell value

        for (int r = 1; r < m; r++) {
            int[] next = new int[n];
            Arrays.fill(next, Integer.MAX_VALUE);        // minimising => start at +infinity
            for (int c = 0; c < n; c++) {                // target column in the current row
                int cell = grid[r][c];
                for (int k = 0; k < n; k++) {            // source column in the previous row
                    int cost = dp[k] + moveCost[grid[r - 1][k]][c] + cell;
                    next[c] = Math.min(next[c], cost);
                }
            }
            dp = next;                                   // keep rows separate (not in place)
        }

        int ans = Integer.MAX_VALUE;
        for (int v : dp) ans = Math.min(ans, v);
        return ans;
    }
}

/*
 * Trace — grid = [[5,3],[4,0],[2,1]], moveCost row-indexed by CELL VALUE
 * ---------------------------------------------------------------------
 *   dp(row0) = [5, 3]
 *   row1 [4,0], prev values [5,3]:
 *     c0: min(5+moveCost[5][0]=5+9, 3+moveCost[3][0]=3+18)+4 = 14+4 = 18
 *     c1: min(5+moveCost[5][1]=5+8, 3+moveCost[3][1]=3+6)+0  = 9
 *     dp = [18, 9]
 *   row2 [2,1], prev values [4,0]:
 *     c0: min(18+moveCost[4][0]=18+2, 9+moveCost[0][0]=9+9)+2 = 20
 *     c1: min(18+moveCost[4][1]=18+4, 9+moveCost[0][1]=9+8)+1 = 18
 *     dp = [20, 18]
 *   answer = min(20,18) = 17   (path 5 -> 0 -> 1)
 *
 * Common bugs: (1) forgetting to init next[] to +inf; (2) indexing moveCost by k or by the current
 * cell instead of by grid[r-1][k] (the SOURCE cell's value); (3) updating dp in place, which
 * corrupts previous-row values that later target columns still need.
 */
