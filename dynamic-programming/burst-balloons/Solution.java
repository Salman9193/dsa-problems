class Solution {

    // Burst Balloons = the canonical interval DP problem.
    //
    // Naive: try all n! orderings — TLE.
    //
    // Key insight: instead of choosing which balloon to burst FIRST,
    // choose which balloon to burst LAST in each interval [left, right].
    // The last balloon to burst in [left,right] has nums[left-1] and nums[right+1]
    // as its neighbours (since everything else in the interval is already gone).
    //
    // dp[left][right] = max coins from bursting all balloons in (left, right) exclusive
    //
    // Add sentinel balloons: nums[-1] = nums[n] = 1
    // dp[i][j] = max coins obtainable by bursting all balloons between i and j (exclusive)
    //
    // Recurrence: for each k in (i, j):
    //   dp[i][j] = max(dp[i][k] + nums[i]*nums[k]*nums[j] + dp[k][j])
    //   k is the LAST balloon burst in (i,j)
    //   coins from k = nums[i] * nums[k] * nums[j] (neighbours are i and j since all
    //   other balloons in (i,j) have already been burst)

    public int maxCoins(int[] nums) {
        int n = nums.length;
        // Pad with sentinel 1s on both ends
        int[] padded = new int[n + 2];
        padded[0] = padded[n + 1] = 1;
        for (int i = 0; i < n; i++) padded[i + 1] = nums[i];
        n = n + 2;

        int[][] dp = new int[n][n];

        // Fill by increasing interval length
        for (int len = 2; len < n; len++) {             // interval length
            for (int left = 0; left < n - len; left++) { // left boundary
                int right = left + len;                   // right boundary
                for (int k = left + 1; k < right; k++) { // last balloon in (left,right)
                    dp[left][right] = Math.max(dp[left][right],
                        dp[left][k] + padded[left] * padded[k] * padded[right] + dp[k][right]);
                }
            }
        }

        return dp[0][n - 1];
    }
}

/*
 * Complexity: Time O(n³), Space O(n²)
 *
 * Why "last balloon to burst" (not first)?
 *   If we choose the FIRST to burst, its neighbours change as other balloons
 *   are removed — we can't determine its coins without knowing the full sequence.
 *   If we choose the LAST to burst in interval (i,j), its neighbours are FIXED:
 *   they are the sentinels i and j (everything else in (i,j) is already gone).
 *   This gives us a clean, independent subproblem.
 *
 * The interval DP template:
 *   for length in 2..n:         // grow intervals from small to large
 *     for left in 0..n-length:  // slide left boundary
 *       right = left + length
 *       for k in left+1..right: // split point
 *         dp[left][right] = max over k of (dp[left][k] + cost(k) + dp[k][right])
 *
 * This template applies to:
 *   #312 Burst Balloons   — this problem
 *   #1000 Minimum Cost to Merge Stones
 *   Matrix Chain Multiplication
 *   Optimal BST construction
 *
 * Trace — nums=[3,1,5,8]:
 *   padded=[1,3,1,5,8,1], n=6
 *   dp[0][2]: k=1: dp[0][1]+1*3*1+dp[1][2] = 0+3+0 = 3
 *   dp[1][3]: k=2: 0+3*1*5+0 = 15
 *   dp[2][4]: k=3: 0+1*5*8+0 = 40
 *   ... (build up to dp[0][5] = 167) ✓
 */
