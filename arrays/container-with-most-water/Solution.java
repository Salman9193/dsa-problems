class Solution {

    // Approach: Two Pointers — O(n) time, O(1) space
    //
    // Start with the widest possible container (left=0, right=n-1).
    // At each step, the area is bounded by the SHORTER of the two walls.
    // Moving the taller wall inward can never improve the result:
    //   - width decreases
    //   - height stays bounded by the same shorter wall
    // So we always move the SHORTER wall inward — greedy, provably correct.
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxWater = 0;

        while (left < right) {
            int water = (right - left) * Math.min(height[left], height[right]);
            maxWater = Math.max(maxWater, water);

            if (height[left] <= height[right]) {
                left++;    // shorter wall is on the left — move it inward
            } else {
                right--;   // shorter wall is on the right — move it inward
            }
        }

        return maxWater;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — each pointer moves at most n times, single pass
 * Space: O(1) — two pointers, one variable
 *
 * Correctness proof (why moving the shorter wall is safe):
 *   Suppose height[left] <= height[right].
 *   Consider ALL containers with left as the left wall:
 *     - The widest one is left..right (already computed).
 *     - Any narrower one (left..right') has:
 *         width' < width  AND  height' <= height[left]  (still bounded by left)
 *         → area' <= area
 *   So no container with left as the left wall can beat what we've already seen.
 *   We can safely discard left and move inward.
 *   (Symmetric argument applies when height[right] < height[left].)
 *
 * Why this is Greedy (not DP):
 *   Moving the shorter wall is a locally optimal choice that never blocks
 *   a globally better solution (proven above). The interchange argument holds.
 *   See GREEDY_VS_DP.md for the general framework.
 *
 * Trace — height = [1,8,6,2,5,4,8,3,7]
 * ---------------------------------------
 * l=0(1), r=8(7): water=8*1=8,   move l (1<7)
 * l=1(8), r=8(7): water=7*7=49,  move r (8>7) ← MAX
 * l=1(8), r=7(3): water=6*3=18,  move r (8>3)
 * l=1(8), r=6(8): water=5*8=40,  move l (8==8)
 * l=2(6), r=6(8): water=4*6=24,  move l (6<8)
 * l=3(2), r=6(8): water=3*2=6,   move l (2<8)
 * l=4(5), r=6(8): water=2*5=10,  move l (5<8)
 * l=5(4), r=6(8): water=1*4=4,   move l (4<8)
 * l=6 >= r=6 → stop
 * return 49 ✓
 */
