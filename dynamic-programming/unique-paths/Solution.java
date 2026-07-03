class Solution {

    // Unique Paths = the foundational 2D grid DP problem.
    // Count paths from top-left to bottom-right moving only right or down.
    //
    // Recurrence: dp[r][c] = dp[r-1][c] + dp[r][c-1]
    //   (paths arriving from above OR from the left)
    //
    // Space optimised to O(n) by keeping only one row.

    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // first row: only one way (all right)

        for (int r = 1; r < m; r++)
            for (int c = 1; c < n; c++)
                dp[c] += dp[c-1]; // dp[c] = from above; dp[c-1] = from left

        return dp[n-1];
    }

    // Math solution: C(m+n-2, m-1) — choose which steps go down
    public int uniquePathsMath(int m, int n) {
        long result = 1;
        for (int i = 0; i < Math.min(m-1, n-1); i++) {
            result = result * (m + n - 2 - i) / (i + 1);
        }
        return (int) result;
    }
}

/*
 * Complexity (DP): Time O(m*n), Space O(n)
 * Complexity (Math): Time O(min(m,n)), Space O(1)
 *
 * The 2D grid DP pattern generalises to:
 *   #63 Unique Paths II   — with obstacles (dp[r][c]=0 if obstacle)
 *   #64 Minimum Path Sum  — min cost instead of count
 *   #120 Triangle         — variable-width grid
 *   #931 Minimum Falling Path Sum — diagonal moves allowed
 *
 * Trace — m=3, n=3:
 *   Init dp=[1,1,1]
 *   r=1: c=1: dp[1]+=dp[0]=1 → [1,2,1]; c=2: dp[2]+=dp[1]=2 → [1,2,3]
 *   r=2: c=1: dp[1]+=dp[0]=1 → [1,3,3]; c=2: dp[2]+=dp[1]=3 → [1,3,6]
 *   return dp[2]=6 ✓
 */
