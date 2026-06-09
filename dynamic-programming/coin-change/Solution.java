import java.util.Arrays;

class Solution {

    // Approach: Bottom-Up Dynamic Programming — O(amount * coins) time, O(amount) space
    //
    // dp[i] = minimum number of coins needed to make amount i.
    //
    // Recurrence:
    //   dp[i] = min over all coins c where c <= i of { dp[i - c] + 1 }
    //
    // Intuition: if we use coin c as the last coin to make amount i,
    // we need dp[i-c] coins for the remainder, plus 1 for coin c itself.
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);  // amount+1 acts as "infinity" (safely unreachable)
        dp[0] = 0;                    // base case: 0 coins needed to make amount 0

        for (int i = 1; i <= amount; i++) {
            for (int c : coins) {
                if (c <= i) {
                    dp[i] = Math.min(dp[i], dp[i - c] + 1);
                }
            }
        }

        // If dp[amount] was never updated from infinity, no valid combination exists
        return dp[amount] > amount ? -1 : dp[amount];
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(amount * |coins|) — outer loop over amounts, inner loop over coins
 * Space: O(amount)           — the dp array
 *
 * Why not greedy?
 * ---------------
 * Greedy (always pick the largest coin that fits) fails for arbitrary denominations:
 *   coins = [1, 3, 4],  amount = 6
 *   Greedy: 4 + 1 + 1 = 3 coins  ✗
 *   DP:     3 + 3     = 2 coins  ✓
 *
 * Greedy works ONLY when the coin system is "canonical" (e.g. US coins: 1,5,10,25).
 * DP works for ALL denomination systems.
 *
 * Why amount+1 as infinity?
 * -------------------------
 * The maximum coins ever needed = amount (all 1-coins).
 * So amount+1 is safely unreachable — a clean sentinel for "impossible so far."
 *
 * Trace — coins=[1,5,10,25], amount=11
 * -------------------------------------
 *  dp[0]  = 0
 *  dp[1]  = dp[0]  + 1 = 1   (coin 1)
 *  dp[5]  = dp[0]  + 1 = 1   (coin 5)
 *  dp[6]  = dp[5]  + 1 = 2   (coin 5 + coin 1)
 *  dp[10] = dp[0]  + 1 = 1   (coin 10)
 *  dp[11] = dp[10] + 1 = 2   (coin 10 + coin 1)  ✓
 */
