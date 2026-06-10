import java.util.*;

class Solution {

    // Approach: Sort + Two Pointers — O(n^2) time, O(1) extra space
    //
    // Fix one element nums[i], then use two pointers on the rest
    // to find pairs summing to -nums[i].
    //
    // Sorting enables:
    //   1. Two-pointer convergence: moving left right increases sum,
    //      moving right left decreases it — O(n) per fixed element.
    //   2. Easy duplicate skipping by comparing adjacent elements.
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            // skip duplicate values for the fixed element
            if (i > 0 && nums[i] == nums[i-1]) continue;

            // early termination: smallest possible sum already > 0
            if (nums[i] > 0) break;

            int left = i + 1, right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    // skip duplicates for left pointer
                    while (left < right && nums[left]  == nums[left+1])  left++;
                    // skip duplicates for right pointer
                    while (left < right && nums[right] == nums[right-1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;    // need larger sum → move left right
                } else {
                    right--;   // need smaller sum → move right left
                }
            }
        }

        return result;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n^2) — O(n log n) sort + O(n) two-pointer scan × O(n) fixed elements
 * Space: O(1) extra — output list doesn't count; sort is in-place
 *
 * Duplicate skipping — three places:
 *   1. Outer loop: if nums[i] == nums[i-1], skip (same fixed element)
 *   2. After finding valid triplet: skip duplicate left values
 *   3. After finding valid triplet: skip duplicate right values
 *   Note: left/right duplicate skipping only happens AFTER recording a triplet —
 *   doing it before would skip valid combinations.
 *
 * Why sorting enables two pointers:
 *   After sorting, the array is directional:
 *   - Moving left rightward  → increases the two-pointer sum
 *   - Moving right leftward  → decreases the two-pointer sum
 *   This lets us converge to the target in O(n) instead of O(n^2) pairs.
 *
 * Trace — nums=[-1,0,1,2,-1,-4], sorted=[-4,-1,-1,0,1,2]
 * ---------------------------------------------------------
 * i=0 (-4): left=1(-1), right=5(2) sum=-3<0 → left++
 *           left=2(-1), right=5(2) sum=-3<0 → left++
 *           left=3(0),  right=5(2) sum=-2<0 → left++
 *           left=4(1),  right=5(2) sum=-1<0 → left++ → done
 *
 * i=1 (-1): left=2(-1), right=5(2) sum=0 ✓ → add[-1,-1,2], skip→ left=3,right=4
 *           left=3(0),  right=4(1) sum=0 ✓ → add[-1,0,1]
 *
 * i=2 (-1): nums[2]==nums[1] → skip
 *
 * i=3 (0):  left=4(1), right=5(2) sum=3>0 → right-- → done
 *
 * Result: [[-1,-1,2], [-1,0,1]] ✓
 */
