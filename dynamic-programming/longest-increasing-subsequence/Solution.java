import java.util.*;

class Solution {

    // Longest Increasing Subsequence (LIS)
    //
    // Two approaches with very different complexity:
    //   O(n²) DP:        dp[i] = max(dp[j]+1) for all j<i where nums[j]<nums[i]
    //   O(n log n) Patience Sorting: maintain a "tails" array with binary search
    //
    // Patience Sorting insight:
    //   tails[k] = smallest tail element of all increasing subsequences of length k+1
    //   For each num: binary search for first tail >= num, replace it (or extend)
    //   Length of tails = length of LIS

    // ── Approach 1: O(n²) DP ─────────────────────────────────────────────────
    public int lengthOfLIS_DP(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // each element is an LIS of length 1 alone
        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i])
                    dp[i] = Math.max(dp[i], dp[j] + 1);
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }

    // ── Approach 2: O(n log n) Patience Sorting ───────────────────────────────
    //
    // tails[i] = smallest possible tail for an IS of length i+1
    // tails is always strictly increasing (by construction)
    //
    // For each num:
    //   Find leftmost tails[pos] >= num via binary search
    //   Replace tails[pos] = num (or append if num > all tails)
    //
    // Why replacing is correct: a smaller tail allows more future extensions.
    // tails.size() = LIS length. NOTE: tails itself is NOT necessarily the LIS.
    public int lengthOfLIS(int[] nums) {
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int pos = lowerBound(tails, num); // first index where tails[pos] >= num
            if (pos == tails.size())
                tails.add(num);   // num is larger than all tails → extend LIS
            else
                tails.set(pos, num); // replace with smaller tail → more room to grow
        }

        return tails.size();
    }

    // Binary search: first index in list where list[i] >= target
    private int lowerBound(List<Integer> list, int target) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (list.get(mid) < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

/*
 * Complexity
 * ----------
 * O(n²) DP:          Time O(n²), Space O(n)
 * O(n log n) Patience: Time O(n log n), Space O(n)
 *
 * When to use which:
 *   n ≤ 1000: either works
 *   n ≤ 100000: must use O(n log n)
 *
 * The patience sorting connection:
 *   Deal cards one by one. Place each card on the leftmost pile whose top
 *   is ≥ the card (greedy). Number of piles = LIS length.
 *   This is equivalent to the tails array binary search approach.
 *
 * IMPORTANT: tails[] is NOT the actual LIS — it's the greedy structure
 * that only tracks lengths. To reconstruct the LIS, track parent pointers
 * in the O(n²) DP approach.
 *
 * Trace — nums=[10,9,2,5,3,7,101,18]:
 * Patience (tails evolution):
 *   10: tails=[10]
 *    9: replace 10 → tails=[9]
 *    2: replace 9  → tails=[2]
 *    5: append     → tails=[2,5]
 *    3: replace 5  → tails=[2,3]
 *    7: append     → tails=[2,3,7]
 *  101: append     → tails=[2,3,7,101]
 *   18: replace 101→ tails=[2,3,7,18]
 * return 4 ✓  (LIS could be [2,3,7,101] or [2,3,7,18])
 */
