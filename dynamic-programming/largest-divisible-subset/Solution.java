import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Solution {

    // Largest Divisible Subset — LeetCode #368
    //
    // After sorting, a valid subset must form a chain a1 | a2 | ... | ak, because
    // divisibility is transitive: if each element divides the next, every pair divides.
    // So the problem is Longest Increasing Subsequence with "% == 0" instead of "<",
    // plus reconstruction of the actual subset via parent pointers.
    //
    // dp[i]   = size of the largest divisible subset ending at nums[i] (sorted).
    // prev[i] = previous index in that chain.
    //
    // Time: O(n^2).  Space: O(n).
    public List<Integer> largestDivisibleSubset(int[] nums) {
        int n = nums.length;
        if (n == 0) return new ArrayList<>();

        Arrays.sort(nums);
        int[] dp = new int[n];
        int[] prev = new int[n];
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int best = 0;                             // index where the longest chain ends
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] % nums[j] == 0 && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > dp[best]) best = i;
        }

        List<Integer> res = new ArrayList<>();
        for (int i = best; i >= 0; i = prev[i]) res.add(nums[i]);   // walk parents back
        Collections.reverse(res);
        return res;
    }
}

/*
 * Why sorting makes it a chain
 * ----------------------------
 * In a valid subset sorted ascending, the smaller of any pair must divide the larger
 * (that is the only way the pair can be divisible). If each element divides its immediate
 * successor, transitivity (a|b and b|c => a|c) makes every pair divisible. So a valid
 * subset is exactly a chain under divisibility, and the largest one is the longest chain.
 *
 * Trace — [1, 2, 4, 8]
 *   dp   = [1, 2, 3, 4]
 *   prev = [-1, 0, 1, 2]
 *   best = 3 (value 8); walk prev: 8 -> 4 -> 2 -> 1; reverse -> [1, 2, 4, 8].
 *
 * This is the longest chain in the divisibility partial order (see RESEARCH.md).
 */
