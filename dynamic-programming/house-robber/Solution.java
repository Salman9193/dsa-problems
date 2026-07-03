class Solution {

    // House Robber = the foundational 1D "take-or-skip" DP pattern.
    // Cannot take two adjacent elements. Maximise total.
    //
    // Recurrence: dp[i] = max(dp[i-1], dp[i-2] + nums[i])
    //   - Skip house i: best up to i-1
    //   - Rob house i:  nums[i] + best up to i-2
    //
    // Space-optimised: only need the previous two values, not the full array.

    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int prev2 = 0;         // dp[i-2]
        int prev1 = 0;         // dp[i-1]

        for (int num : nums) {
            int curr = Math.max(prev1, prev2 + num);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}

/*
 * Complexity: Time O(n), Space O(1)
 *
 * The "take-or-skip" pattern generalises to:
 *   #213 House Robber II  — circular array (split into two linear subproblems)
 *   #337 House Robber III — tree (DFS returning (rob_root, skip_root) pair)
 *   #740 Delete and Earn  — convert to house robber on value buckets
 *   #198 is the base case — master this, then extend to II and III
 *
 * Trace — nums=[2,7,9,3,1]:
 *   num=2: curr=max(0,0+2)=2,  prev2=0, prev1=2
 *   num=7: curr=max(2,0+7)=7,  prev2=2, prev1=7
 *   num=9: curr=max(7,2+9)=11, prev2=7, prev1=11
 *   num=3: curr=max(11,7+3)=11,prev2=11,prev1=11
 *   num=1: curr=max(11,11+1)=12,prev2=11,prev1=12
 *   return 12 ✓ (rob houses 0,2,4: 2+9+1=12)
 */
