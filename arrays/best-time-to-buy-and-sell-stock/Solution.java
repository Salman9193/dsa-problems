class Solution {

    // Approach: One-Pass Prefix Minimum — O(n) time, O(1) space
    //
    // At each day, the maximum profit if we sell today =
    //   price[today] - min(price[0..today-1])
    //
    // So we track:
    //   minPrice  — the cheapest buying opportunity seen so far (prefix minimum)
    //   maxProfit — the best profit seen so far
    //
    // We never need to look back further because:
    //   - We can only sell AFTER buying → need the minimum to the LEFT of today
    //   - A lower minimum always leads to a better or equal profit
    //   → always keep the global left-minimum
    public int maxProfit(int[] prices) {
        int minPrice  = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;                              // better buy day
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice);  // sell today
            }
        }

        return maxProfit;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — single pass
 * Space: O(1) — two variables
 *
 * Is this Greedy or DP?
 * ---------------------
 * Both views converge to the same algorithm:
 *
 * Greedy view:
 *   At each step, keep the best possible buy price seen so far.
 *   This is locally optimal and never forecloses a better solution —
 *   if a lower price appears later, we update immediately.
 *   The interchange argument holds → greedy is correct.
 *
 * DP view:
 *   dp[i] = max(dp[i-1], price[i] - minPrice[0..i-1])
 *   Since only the previous state is needed, it collapses to O(1) space
 *   → identical to the greedy scan above.
 *
 * Trace — prices = [7, 1, 5, 3, 6, 4]
 * --------------------------------------
 * price=7: minPrice=7,  maxProfit=0
 * price=1: minPrice=1,  maxProfit=0   (new minimum)
 * price=5: minPrice=1,  maxProfit=4   (5-1=4)
 * price=3: minPrice=1,  maxProfit=4   (3-1=2, no improvement)
 * price=6: minPrice=1,  maxProfit=5   (6-1=5) ← answer
 * price=4: minPrice=1,  maxProfit=5   (4-1=3, no improvement)
 * return 5 ✓
 *
 * Decreasing prices — [7, 6, 4, 3, 1]:
 * price=7: minPrice=7,  maxProfit=0
 * price=6: minPrice=6,  maxProfit=0
 * price=4: minPrice=4,  maxProfit=0
 * price=3: minPrice=3,  maxProfit=0
 * price=1: minPrice=1,  maxProfit=0
 * return 0 ✓  (no profitable trade exists)
 */
