class Solution {

    // Approach: 4-State DP — O(n) time, O(1) space
    //
    // Track 4 states in one pass:
    //   buy1  = max portfolio value after 1st buy   (most negative → cheapest buy)
    //   sell1 = max portfolio value after 1st sell
    //   buy2  = max portfolio value after 2nd buy   (sell1 profit reduces effective cost)
    //   sell2 = max portfolio value after 2nd sell  ← answer
    //
    // Key insight for buy2: sell1 - price
    //   The profit from the first transaction "funds" the second buy.
    //   sell2 thus captures the combined profit of BOTH transactions.
    //
    // Order of updates matters — each state builds on the previous in the same iteration.
    // This is safe because:
    //   buy1 only depends on price (independent)
    //   sell1 depends on buy1 (just updated — reflects cheapest buy up to today)
    //   buy2 depends on sell1 (reflects best 1-transaction profit up to today)
    //   sell2 depends on buy2 (reflects best 2nd buy up to today)
    public int maxProfit(int[] prices) {
        int buy1  = Integer.MIN_VALUE;
        int sell1 = 0;
        int buy2  = Integer.MIN_VALUE;
        int sell2 = 0;

        for (int price : prices) {
            buy1  = Math.max(buy1,  -price);           // best: buy at lowest price
            sell1 = Math.max(sell1, buy1  + price);    // best: sell at highest gain
            buy2  = Math.max(buy2,  sell1 - price);    // best: 2nd buy using 1st profit
            sell2 = Math.max(sell2, buy2  + price);    // best: 2nd sell
        }

        return sell2;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — single pass, 4 constant-time updates per price
 * Space: O(1) — 4 integer variables
 *
 * Why buy1 = Integer.MIN_VALUE?
 *   buy1 represents the portfolio value AFTER spending money to buy.
 *   Before any buy, portfolio value = 0. After buying at price p: -p.
 *   Initialising to MIN_VALUE means "haven't bought yet" — any buy is better.
 *
 * Generalisation to k transactions (see #188):
 *   int[] buy  = new int[k]; Arrays.fill(buy, Integer.MIN_VALUE);
 *   int[] sell = new int[k]; // zeros
 *   for (int price : prices) {
 *       buy[0]  = Math.max(buy[0],  -price);
 *       sell[0] = Math.max(sell[0], buy[0] + price);
 *       for (int i = 1; i < k; i++) {
 *           buy[i]  = Math.max(buy[i],  sell[i-1] - price);
 *           sell[i] = Math.max(sell[i], buy[i]    + price);
 *       }
 *   }
 *   return sell[k-1];
 *
 * Trace — prices=[3,3,5,0,0,3,1,4]
 * ----------------------------------
 * price=3: buy1=-3, sell1=0,  buy2=-3, sell2=0
 * price=3: buy1=-3, sell1=0,  buy2=-3, sell2=0
 * price=5: buy1=-3, sell1=2,  buy2=-3, sell2=2
 * price=0: buy1=0,  sell1=2,  buy2=2,  sell2=2
 * price=0: buy1=0,  sell1=2,  buy2=2,  sell2=2
 * price=3: buy1=0,  sell1=3,  buy2=2,  sell2=5
 * price=1: buy1=0,  sell1=3,  buy2=2,  sell2=5
 * price=4: buy1=0,  sell1=4,  buy2=2,  sell2=6 ✓
 * return sell2 = 6
 */
