class Solution {

    // Approach: Greedy — O(n) time, O(1) space
    //
    // With unlimited transactions, the optimal strategy is:
    // Sum every positive price difference (every "uphill" between consecutive days).
    //
    // Proof: any profitable multi-day transaction can be decomposed into
    // consecutive 1-day transactions without loss of profit.
    //
    //   buy day 0, sell day 3 → profit = p[3]-p[0]
    //   = (p[1]-p[0]) + (p[2]-p[1]) + (p[3]-p[2])  ← sum of daily diffs
    //
    // So we capture ALL upward movements — equivalent to perfect hindsight.
    //
    // Why greedy works here (but not for k-limited):
    //   No constraint on transaction count → every positive diff is independent
    //   → greedy locally optimal choice (take every gain) = globally optimal.
    //   See GREEDY_VS_DP.md for the interchange argument.
    public int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i-1])
                profit += prices[i] - prices[i-1];
        }
        return profit;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — single pass
 * Space: O(1)
 *
 * Example:
 *   prices = [7,1,5,3,6,4]
 *   diffs  = [-6,+4,-2,+3,-2]
 *   sum of positives = 4+3 = 7
 *   (buy at 1 sell at 5, buy at 3 sell at 6)
 *
 * Greedy decomposition proof:
 *   buy at price[i], sell at price[j] (j > i)
 *   profit = price[j] - price[i]
 *          = sum_{t=i}^{j-1} (price[t+1] - price[t])
 *   = sum of daily diffs, each captured independently
 *   So collecting all positive diffs = collecting all profitable sub-trades.
 *
 * Stock series connection:
 *   #121 k=1:       prefix min scan
 *   #122 k=∞:       greedy sum of positive diffs  ← this
 *   #123 k=2:       4-state DP
 *   #188 k=any:     k-state DP
 */
