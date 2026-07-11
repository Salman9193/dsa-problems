class Solution {

    // Combination Sum IV — LeetCode #377
    //
    // Count the ordered sequences of nums that sum to target (order matters, so this counts
    // integer *compositions* with parts restricted to nums, not combinations).
    //
    // dp[t] = number of ordered sequences summing to t.
    // Build t by choosing every possible LAST element num and adding dp[t - num]:
    //     dp[t] = sum over num<=t of dp[t - num],  dp[0] = 1.
    //
    // The amount loop is OUTER and the candidate loop INNER — that ordering is what makes
    // order matter (every num is tried as the last element for every t). Swapping the loops
    // would instead count order-independent combinations (that is Coin Change II, #518).
    //
    // Time: O(target * n).  Space: O(target).
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;                                  // empty sequence for sum 0
        for (int t = 1; t <= target; t++) {         // amount OUTER
            for (int num : nums) {                  // candidate INNER
                if (num <= t) {
                    dp[t] += dp[t - num];
                }
            }
        }
        return dp[target];
    }
}

/*
 * Trace — nums = [1,2,3], target = 4
 * ----------------------------------
 *   dp[0] = 1
 *   dp[1] = dp[0]                 = 1
 *   dp[2] = dp[1] + dp[0]         = 2
 *   dp[3] = dp[2] + dp[1] + dp[0] = 4
 *   dp[4] = dp[3] + dp[2] + dp[1] = 7
 *   => 7 ordered sequences:
 *      (1,1,1,1) (1,1,2) (1,2,1) (2,1,1) (1,3) (3,1) (2,2)
 *
 * Loop order matters
 * ------------------
 * amount-outer (here)      -> counts ordered sequences / permutations  (#377)
 * candidate-outer (swap)   -> counts order-independent combinations     (Coin Change II #518)
 *
 * Note: intermediate dp values can overflow int even when the final answer fits; use long
 * accumulation if the platform doesn't allow wrap-around.
 */
