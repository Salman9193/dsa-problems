class Solution {

    // Approach: k-State DP — O(n*k) time, O(k) space
    //
    // Generalises #123 from k=2 to arbitrary k.
    // buy[i]  = best portfolio value after the (i+1)-th buy
    // sell[i] = best portfolio value after the (i+1)-th sell
    //
    // Special case: if k >= n/2, we can make unlimited transactions
    //   → use greedy (sum all positive diffs) which is O(n).
    //   This handles the case where k is very large (e.g. 1000000).
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;

        // If k >= n/2, unlimited transactions → greedy
        if (k >= n / 2) {
            int profit = 0;
            for (int i = 1; i < n; i++)
                if (prices[i] > prices[i-1])
                    profit += prices[i] - prices[i-1];
            return profit;
        }

        int[] buy  = new int[k];
        int[] sell = new int[k];
        java.util.Arrays.fill(buy, Integer.MIN_VALUE);

        for (int price : prices) {
            // First transaction
            buy[0]  = Math.max(buy[0],  -price);
            sell[0] = Math.max(sell[0], buy[0] + price);
            // i-th transaction builds on (i-1)-th sell
            for (int i = 1; i < k; i++) {
                buy[i]  = Math.max(buy[i],  sell[i-1] - price);
                sell[i] = Math.max(sell[i], buy[i]    + price);
            }
        }

        return sell[k - 1];
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n * k) — n prices, k transactions
 * Space: O(k)     — two arrays of size k
 *
 * Special case k >= n/2:
 *   At most n/2 non-overlapping transactions fit in n days.
 *   If k >= n/2, we're effectively unlimited → greedy gives optimal answer.
 *   Without this check, O(n*k) with k=10^9 would TLE.
 *
 * Why the greedy fallback is correct:
 *   With n prices, the maximum number of non-overlapping buy/sell pairs
 *   is floor(n/2). If k >= floor(n/2), we can always execute any
 *   profitable trade → unlimited-transaction greedy applies.
 *
 * How this generalises #121, #122, #123:
 *   k=1: sell[0] after one pass = prefix min scan result (#121)
 *   k=∞: greedy fallback triggered = sum positive diffs (#122)
 *   k=2: buy[0],sell[0],buy[1],sell[1] = 4-state DP (#123)
 *   k=any: this solution (#188)
 *
 * Trace — prices=[2,4,1], k=2
 * ----------------------------
 * price=2: buy[0]=max(MIN,-2)=-2, sell[0]=max(0,-2+2)=0
 *          buy[1]=max(MIN,0-2)=-2, sell[1]=max(0,-2+2)=0
 * price=4: buy[0]=-2, sell[0]=max(0,-2+4)=2
 *          buy[1]=max(-2,2-4)=-2, sell[1]=max(0,-2+4)=2
 * price=1: buy[0]=max(-2,-1)=-1, sell[0]=max(2,-1+1)=2
 *          buy[1]=max(-2,2-1)=1, sell[1]=max(2,1+1)=2
 * return sell[1] = 2 ✓
 */
