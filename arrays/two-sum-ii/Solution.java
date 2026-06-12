class Solution {

    // Approach: Two Pointers — O(n) time, O(1) space
    //
    // The sorted order gives directional information:
    //   sum < target → need a larger value → move left pointer right
    //   sum > target → need a smaller value → move right pointer left
    //   sum == target → found the answer
    //
    // Why this is correct — the pruning argument:
    //   When sum < target and we move left++, we eliminate ALL pairs
    //   (left, j) for every j <= right, because:
    //     numbers[left] + numbers[j] <= numbers[left] + numbers[right] < target
    //   None of those pairs can work. We discard all of them in O(1).
    //   Symmetric argument for right--.
    //   Each step eliminates at least one row/column of the n×n pair matrix
    //   → O(n) total eliminations → O(n) time.
    public int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if      (sum == target) return new int[]{left + 1, right + 1};  // 1-indexed
            else if (sum < target)  left++;
            else                    right--;
        }

        return new int[]{-1, -1};  // guaranteed not to reach here
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — left and right each move at most n steps total
 * Space: O(1) — no auxiliary data structures
 *
 * Why not HashMap (like Two Sum I)?
 *   HashMap gives O(n) time but O(n) space.
 *   The sorted constraint allows O(1) space via two pointers.
 *   The problem explicitly requires O(1) extra space.
 *
 * Two Sum Family:
 *   #1   Two Sum       — unsorted → HashMap, O(n) space
 *   #167 Two Sum II    — sorted   → two pointers, O(1) space  ← this
 *   #15  3Sum          — unsorted → sort + two pointers
 *   #18  4Sum          — unsorted → sort + two pointers × 2
 *
 * Trace — numbers=[2,7,11,15], target=9
 * ---------------------------------------
 * left=0(2), right=3(15): sum=17 > 9  → right--
 * left=0(2), right=2(11): sum=13 > 9  → right--
 * left=0(2), right=1(7):  sum=9 == 9  → return [1,2] ✓
 *
 * Trace — numbers=[2,3,4], target=6
 * ------------------------------------
 * left=0(2), right=2(4): sum=6 == 6 → return [1,3] ✓
 *
 * Edge cases:
 *   [-1, 0], target=-1 → left=0(-1), right=1(0): sum=-1 == -1 → [1,2] ✓
 *   [1, 2], target=3   → sum=3 == 3 → [1,2] ✓
 */
