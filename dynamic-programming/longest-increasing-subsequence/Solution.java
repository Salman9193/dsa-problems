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

    // Reconstruction: return the actual LIS (not just its length) in O(n log n).
    //
    // The common misconception is that the patience/tails method can't reconstruct. It can:
    // keep tails as INDICES, and record a parent pointer for each element.
    //
    //   tailIdx[len-1] = index of the smallest tail among increasing subsequences of length len
    //   parent[i]      = index of i's predecessor in the LIS ending at i
    //
    // When nums[i] lands at position lo, it extends a subsequence of length lo whose best tail
    // is tailIdx[lo-1] — an entry finalized before i was processed, so the pointer stays valid
    // even though tailIdx[lo] is later overwritten.
    //
    // Time: O(n log n).  Space: O(n).
    public List<Integer> lisSequence(int[] nums) {
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        int[] tailIdx = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int size = 0;

        for (int i = 0; i < n; i++) {
            int lo = 0, hi = size;                       // lower bound on tail VALUES
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (nums[tailIdx[mid]] < nums[i]) lo = mid + 1;
                else hi = mid;
            }
            if (lo > 0) parent[i] = tailIdx[lo - 1];     // the key line
            tailIdx[lo] = i;
            if (lo == size) size++;
        }

        LinkedList<Integer> lis = new LinkedList<>();
        for (int cur = tailIdx[size - 1]; cur != -1; cur = parent[cur]) {
            lis.addFirst(nums[cur]);                     // build front-to-back
        }
        return lis;
    }

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
 * IMPORTANT: tails[] is NOT the actual LIS — it's the greedy structure, and is often not
 * even a subsequence of the input in order (for [3,4,5,1], tails ends as [1,4,5]). It gives
 * the correct LENGTH only. To recover the sequence itself, use lisSequence() above: keep the
 * tails as INDICES plus a parent[] array, which reconstructs in O(n log n).
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
