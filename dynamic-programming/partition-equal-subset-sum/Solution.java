import java.util.Arrays;

class Solution {

    // Approach: 0/1 Knapsack DP — O(n × target) time, O(target) space
    //
    // Key reduction:
    //   Partition into 2 equal subsets
    //   ↔ Does any subset sum to total/2?
    //   ↔ 0/1 Knapsack: can we fill a knapsack of capacity target=total/2?
    //
    // dp[s] = true if some subset of nums seen so far sums to exactly s
    //
    // Transition: for each num, update RIGHT TO LEFT to avoid reusing same element
    //   dp[s] = dp[s] || dp[s - num]
    //
    // Why right-to-left?
    //   Left-to-right would let dp[s-num] reflect the current iteration's updates
    //   → same element could be added multiple times (unbounded knapsack bug).
    //   Right-to-left reads dp[s-num] from the PREVIOUS iteration → 0/1 property ✓
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int n : nums) sum += n;
        if (sum % 2 != 0) return false;   // odd total → impossible

        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;  // empty subset always sums to 0

        for (int num : nums) {
            for (int s = target; s >= num; s--) {
                dp[s] = dp[s] || dp[s - num];
                if (dp[target]) return true;  // early exit
            }
        }

        return dp[target];
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n × target) = O(n × sum/2) — pseudo-polynomial
 * Space: O(target) = O(sum/2) — 1D boolean array
 *
 * Why "pseudo-polynomial"?
 *   The runtime depends on the numerical VALUE of the input (sum/2), not just n.
 *   If values are large (e.g. sum = 10^9), this is intractable.
 *   True polynomial in n would be O(n^k) for some fixed k.
 *   This is why subset sum is NP-complete in general (large values case).
 *
 * The thought process (how to arrive at DP):
 *   Step 1: Reduce → "does any subset sum to total/2?"
 *   Step 2: Try greedy → fails (wrong local choices block global solutions)
 *   Step 3: Recursive tree → overlapping subproblems → DP
 *   Step 4: dp[s] = "can we reach sum s?"
 *   Step 5: Right-to-left to enforce 0/1 (not unbounded)
 *
 * Trace — nums=[1,5,11,5], target=11
 * ------------------------------------
 * Initial: dp = [T,F,F,F,F,F,F,F,F,F,F,F]
 *
 * num=1:  s=1:  dp[1] |= dp[0]=T  → dp[1]=T
 *   dp = [T,T,F,F,F,F,F,F,F,F,F,F]
 *
 * num=5:  s=11..5:
 *   dp[6] |= dp[1]=T → T
 *   dp[5] |= dp[0]=T → T
 *   dp = [T,T,F,F,F,T,T,F,F,F,F,F]
 *
 * num=11: s=11:
 *   dp[11] |= dp[0]=T → T  ← FOUND, return true ✓
 *
 * Counter-example: nums=[1,2,3,5], target=5+3+2+1=11 odd... wait
 *   sum=11 → odd → return false immediately ✓
 *
 * nums=[1,2,3,5]: sum=11 odd → false ✓
 */
