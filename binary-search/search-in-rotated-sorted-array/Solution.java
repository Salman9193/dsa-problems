class Solution {

    // Approach: Modified Binary Search — O(log n) time, O(1) space
    //
    // Key insight: in a rotated sorted array, at least one half is always sorted.
    // At every step, determine which half is sorted, then check if the target
    // lies within that half to decide where to continue searching.
    //
    // No need to find the pivot explicitly — one extra comparison per iteration suffices.
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) return mid;

            // Case 1: Left half [left..mid] is sorted
            if (nums[left] <= nums[mid]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;   // target falls within sorted left half
                } else {
                    left = mid + 1;    // target must be in right half
                }
            }
            // Case 2: Right half [mid..right] is sorted
            else {
                if (target > nums[mid] && target <= nums[right]) {
                    left = mid + 1;    // target falls within sorted right half
                } else {
                    right = mid - 1;  // target must be in left half
                }
            }
        }

        return -1;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(log n) — binary search with one extra comparison per step
 * Space: O(1)     — no auxiliary data structures
 *
 * Why it works
 * ------------
 * After computing mid, one of two cases always holds:
 *
 *   Case 1  nums[left] <= nums[mid]  →  left half is sorted
 *     If target ∈ [nums[left], nums[mid]) → search left (right = mid-1)
 *     Else                               → search right (left = mid+1)
 *
 *   Case 2  nums[left] >  nums[mid]  →  right half is sorted
 *     If target ∈ (nums[mid], nums[right]] → search right (left = mid+1)
 *     Else                                 → search left  (right = mid-1)
 *
 * Trace — nums=[4,5,6,7,0,1,2], target=0
 * ----------------------------------------
 * left=0, right=6, mid=3 → nums[3]=7
 *   Left half [4,5,6,7] sorted (nums[0]=4 ≤ nums[3]=7)
 *   target=0 NOT in [4,7) → search right: left=4
 *
 * left=4, right=6, mid=5 → nums[5]=1
 *   Left half [0,1] sorted (nums[4]=0 ≤ nums[5]=1)
 *   target=0 in [0,1) → search left: right=4
 *
 * left=4, right=4, mid=4 → nums[4]=0 == target ✓  → return 4
 */
