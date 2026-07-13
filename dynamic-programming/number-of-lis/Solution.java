import java.util.Arrays;

class Solution {

    // Number of Longest Increasing Subsequences — LeetCode #673
    //
    // len[i] = length of the LIS ending at i
    // cnt[i] = how many LIS of that length end at i
    //
    // The core rule, for each j < i with nums[j] < nums[i]:
    //   len[j] + 1 >  len[i]  -> RESET:      a strictly longer chain invalidates the old count
    //   len[j] + 1 == len[i]  -> ACCUMULATE: another distinct way to reach the same length
    //
    // The answer is the SUM of cnt[i] over every i achieving the maximum length — not cnt of
    // the last index, and not max(cnt), since several positions can each end a maximum LIS.
    //
    // Time: O(n^2).  Space: O(n).
    public int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] len = new int[n];
        int[] cnt = new int[n];
        Arrays.fill(len, 1);
        Arrays.fill(cnt, 1);

        int best = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    if (len[j] + 1 > len[i]) {
                        len[i] = len[j] + 1;
                        cnt[i] = cnt[j];              // RESET
                    } else if (len[j] + 1 == len[i]) {
                        cnt[i] += cnt[j];             // ACCUMULATE
                    }
                }
            }
            best = Math.max(best, len[i]);
        }

        int total = 0;
        for (int i = 0; i < n; i++) {
            if (len[i] == best) total += cnt[i];      // SUM over all maximum-length endings
        }
        return total;
    }
}

/*
 * Trace — nums = [1,3,5,4,7]
 * --------------------------
 *   i=0 (1): len=1 cnt=1
 *   i=1 (3): len=2 cnt=1        (extends 1)
 *   i=2 (5): len=3 cnt=1        (extends 1->3)
 *   i=3 (4): len=3 cnt=1        (extends 1->3; 5 is too big)
 *   i=4 (7): extends the len-3 chain ending at 5   -> len=4, cnt=1  (RESET)
 *            extends the len-3 chain ending at 4   -> tie, cnt += 1 (ACCUMULATE) -> cnt=2
 *   best = 4; only i=4 has len 4  ->  answer 2      ([1,3,5,7] and [1,3,4,7])
 *
 * The same reset/accumulate rule counts maximum chains in ANY poset — replace the test
 * nums[j] < nums[i] with the relevant comparability test (e.g. nums[i] % nums[j] == 0 for
 * Largest Divisible Subset, #368).
 */
