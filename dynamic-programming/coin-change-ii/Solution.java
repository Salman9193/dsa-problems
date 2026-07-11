class Solution {

    // Coin Change II — LeetCode #518
    //
    // Count the number of order-independent combinations of coins (unlimited supply) that
    // sum to amount.
    //
    // dp[a] = number of combinations making amount a;  dp[0] = 1 (empty combination).
    // dp[a] += dp[a - coin].
    //
    // The COIN loop is OUTER and the AMOUNT loop INNER — that ordering makes it count
    // combinations (each coin introduced once, in a fixed order, so each multiset once).
    // Swapping the loops would count ordered sequences instead (Combination Sum IV, #377).
    //
    // Time: O(amount * n).  Space: O(amount).
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;                                   // one way to make 0: pick nothing
        for (int coin : coins) {                     // coin OUTER
            for (int a = coin; a <= amount; a++) {   // amount INNER
                dp[a] += dp[a - coin];
            }
        }
        return dp[amount];
    }
}

/*
 * Trace — amount = 5, coins = [1,2,5]
 * -----------------------------------
 *   init:   1 0 0 0 0 0
 *   coin 1: 1 1 1 1 1 1
 *   coin 2: 1 1 2 2 3 3
 *   coin 5: 1 1 2 2 3 4     -> dp[5] = 4
 *   ([5], [2,2,1], [2,1,1,1], [1,1,1,1,1])
 *
 * Loop order
 * ----------
 * coin-outer (here)     -> counts combinations (partitions)      #518
 * amount-outer (swap)   -> counts ordered sequences (compositions) Combination Sum IV #377
 *
 * The value dp[amount] is the denumerant: the coefficient of x^amount in the generating
 * function product 1/(1 - x^coin) over the coins (see RESEARCH.md).
 */
