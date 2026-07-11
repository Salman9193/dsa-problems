import java.util.ArrayList;
import java.util.List;

class Solution {

    // Inverse Coin Change — LeetCode #3592
    //
    // numWays[i] (1-indexed) = number of order-independent ways to make amount i from some
    // lost set of coin denominations. Recover the sorted denomination set, or [] if none.
    //
    // Inverse of Coin Change II (#518). Key fact: introducing a new coin c (larger than all
    // coins found so far) raises numWays[c] by exactly 1 — the singleton [c]. So sweep
    // amounts upward, rebuilding the count array `cur` from discovered coins:
    //   numWays[i] == cur[i]      -> i is not a coin
    //   numWays[i] == cur[i] + 1  -> i is a coin: record it, apply its Coin Change II update
    //   otherwise                 -> impossible, return []
    // cur[i] is finalized by the time we reach i (coins affect only amounts >= themselves),
    // so each decision is forced.
    //
    // Time: O(n^2).  Space: O(n).
    public List<Integer> findCoins(int[] numWays) {
        int n = numWays.length;
        long[] cur = new long[n + 1];
        cur[0] = 1;                                  // base: one way to make 0
        List<Integer> coins = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            long want = numWays[i - 1];              // input is 1-indexed
            if (want == cur[i]) {
                // i is not a denomination
            } else if (want == cur[i] + 1) {
                coins.add(i);                        // i is a denomination
                for (int a = i; a <= n; a++) {
                    cur[a] += cur[a - i];            // forward Coin Change II update for coin i
                }
            } else {
                return new ArrayList<>();            // no coin set produces this array
            }
        }
        return coins;                                // found in increasing order -> already sorted
    }
}

/*
 * Trace — numWays = [0,1,0,2,0,3,0,4,0,5]  (n = 10)
 * -------------------------------------------------
 *   i=2: cur[2]=0, want=1 -> coin 2, update cur -> 1 0 1 0 1 0 1 0 1 0 1
 *   i=4: cur[4]=1, want=2 -> coin 4, update cur -> 1 0 1 0 2 0 2 0 3 0 3
 *   i=6: cur[6]=2, want=3 -> coin 6, update cur -> 1 0 1 0 2 0 3 0 4 0 5
 *   i=8: cur[8]=4, want=4 -> not a coin
 *   i=10: cur[10]=5, want=5 -> not a coin
 *   => [2, 4, 6]
 *
 * Impossibility: a count below cur[i], or a jump of more than +1, cannot be produced by any
 * denomination set (adding coins never decreases a count), so return [].
 *
 * This inverts the generating function product 1/(1 - x^c) to recover the coins c.
 */
