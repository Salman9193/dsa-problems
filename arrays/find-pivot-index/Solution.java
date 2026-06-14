class Solution {

    // Approach: Total Sum — O(n) time, O(1) space
    //
    // For index i to be the pivot:
    //   leftSum == rightSum
    //   leftSum == totalSum - leftSum - nums[i]
    //   2 * leftSum == totalSum - nums[i]
    //
    // This eliminates the need for a suffix sum array entirely.
    // Scan once to get totalSum, scan again maintaining leftSum.
    // Check the pivot condition BEFORE adding nums[i] to leftSum.
    public int pivotIndex(int[] nums) {
        int total = 0;
        for (int n : nums) total += n;

        int leftSum = 0;
        for (int i = 0; i < nums.length; i++) {
            // rightSum = total - leftSum - nums[i]
            if (leftSum == total - leftSum - nums[i]) return i;
            leftSum += nums[i];
        }

        return -1;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — two linear passes
 * Space: O(1) — only `total` and `leftSum` variables
 *
 * Why check BEFORE adding nums[i] to leftSum?
 *   At the start of iteration i, leftSum = sum of nums[0..i-1]
 *   This is exactly the left sum for pivot index i.
 *   If we added nums[i] first, leftSum would include nums[i] itself.
 *
 * Prefix sum family:
 *   #238 Product Except Self    → prefix product × suffix product
 *   #42  Trapping Rain Water    → prefix max + suffix max
 *   #724 Find Pivot Index       → prefix sum = totalSum - prefix sum - nums[i]
 *   #560 Subarray Sum Equals K  → prefix sum + HashMap
 *   #303 Range Sum Query        → precomputed prefix sum array
 *
 * Trace — nums=[1,7,3,6,5,6], total=28
 * ---------------------------------------
 * i=0: leftSum=0,  right=28-0-1=27,  0≠27, leftSum=1
 * i=1: leftSum=1,  right=28-1-7=20,  1≠20, leftSum=8
 * i=2: leftSum=8,  right=28-8-3=17,  8≠17, leftSum=11
 * i=3: leftSum=11, right=28-11-6=11, 11==11 → return 3 ✓
 *
 * Trace — nums=[2,1,-1], total=2
 * --------------------------------
 * i=0: leftSum=0, right=2-0-2=0, 0==0 → return 0 ✓
 *   (empty left side has sum 0 — valid pivot at index 0)
 *
 * Trace — nums=[1,2,3], total=6
 * --------------------------------
 * i=0: leftSum=0, right=6-0-1=5,  0≠5, leftSum=1
 * i=1: leftSum=1, right=6-1-2=3,  1≠3, leftSum=3
 * i=2: leftSum=3, right=6-3-3=0,  3≠0
 * → return -1 ✓
 */
