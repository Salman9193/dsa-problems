class Solution {

    // Approach 1: Prefix + Suffix Arrays — O(n) time, O(n) extra space
    //
    // For each index i:
    //   prefix[i] = product of all elements to the LEFT  of i
    //   suffix[i] = product of all elements to the RIGHT of i
    //   answer[i] = prefix[i] * suffix[i]
    public int[] productExceptSelfTwoArrays(int[] nums) {
        int n = nums.length;
        int[] prefix = new int[n];
        int[] suffix = new int[n];
        int[] answer = new int[n];

        prefix[0] = 1;
        for (int i = 1; i < n; i++)
            prefix[i] = prefix[i-1] * nums[i-1];

        suffix[n-1] = 1;
        for (int i = n-2; i >= 0; i--)
            suffix[i] = suffix[i+1] * nums[i+1];

        for (int i = 0; i < n; i++)
            answer[i] = prefix[i] * suffix[i];

        return answer;
    }

    // Approach 2: Space-Optimised — O(n) time, O(1) extra space  ← OPTIMAL
    //
    // Use the output array to store prefix products (Pass 1).
    // Then do a single right-to-left pass with a running suffix variable (Pass 2),
    // multiplying the suffix into each position in place.
    //
    // The output array itself doesn't count as extra space per the problem convention.
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];

        // Pass 1: answer[i] = product of nums[0..i-1]  (prefix)
        answer[0] = 1;
        for (int i = 1; i < n; i++)
            answer[i] = answer[i-1] * nums[i-1];

        // Pass 2: multiply running suffix into each position right-to-left
        int suffix = 1;
        for (int i = n-1; i >= 0; i--) {
            answer[i] *= suffix;       // incorporate suffix product
            suffix    *= nums[i];      // extend suffix leftward
        }

        return answer;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — two linear passes
 * Space: O(1) extra — only the output array (which doesn't count)
 *
 * Why not division?
 *   Total product / nums[i] breaks when:
 *   - nums[i] == 0        → division by zero
 *   - multiple zeros      → ambiguous which zero to "cancel"
 *   - large values        → integer overflow before division
 *
 * Trace — nums = [1, 2, 3, 4]
 * ----------------------------
 * After Pass 1 (prefix):
 *   answer = [1, 1, 2, 6]
 *             ↑  ↑  ↑  ↑
 *             1  1  1*2  1*2*3
 *
 * Pass 2 (suffix right-to-left):
 *   i=3: answer[3] = 6  * suffix(1)  = 6,   suffix = 1*4  = 4
 *   i=2: answer[2] = 2  * suffix(4)  = 8,   suffix = 4*3  = 12
 *   i=1: answer[1] = 1  * suffix(12) = 12,  suffix = 12*2 = 24
 *   i=0: answer[0] = 1  * suffix(24) = 24,  suffix = 24*1 = 24
 *
 *   answer = [24, 12, 8, 6]  ✓
 */
