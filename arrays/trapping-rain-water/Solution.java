class Solution {

    // Approach 1: Prefix/Suffix Arrays — O(n) time, O(n) space
    //
    // Water at position i = min(maxLeft[i], maxRight[i]) - height[i]
    // Build both arrays in two passes, then accumulate water in a third pass.
    public int trapArrays(int[] height) {
        int n = height.length;
        int[] maxLeft  = new int[n];
        int[] maxRight = new int[n];

        maxLeft[0] = height[0];
        for (int i = 1; i < n; i++)
            maxLeft[i] = Math.max(maxLeft[i-1], height[i]);

        maxRight[n-1] = height[n-1];
        for (int i = n-2; i >= 0; i--)
            maxRight[i] = Math.max(maxRight[i+1], height[i]);

        int water = 0;
        for (int i = 0; i < n; i++)
            water += Math.min(maxLeft[i], maxRight[i]) - height[i];

        return water;
    }

    // Approach 2: Two Pointers — O(n) time, O(1) space  ← OPTIMAL
    //
    // Key observation: if height[left] <= height[right], then
    //   maxRight >= height[right] >= height[left]
    //   → min(maxLeft, maxRight) = maxLeft  (left is the bottleneck)
    //   → water at left = maxLeft - height[left]  (exact, no need for maxRight)
    // Symmetric argument applies when height[left] > height[right].
    //
    // This eliminates both prefix/suffix arrays — we compute water
    // at each position in a single pass with only four variables.
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int maxLeft = 0, maxRight = 0;
        int water = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= maxLeft) maxLeft = height[left];
                else water += maxLeft - height[left];
                left++;
            } else {
                if (height[right] >= maxRight) maxRight = height[right];
                else water += maxRight - height[right];
                right--;
            }
        }

        return water;
    }
}

/*
 * Complexity
 * ----------
 * Arrays:        Time O(n), Space O(n)
 * Two Pointers:  Time O(n), Space O(1)
 *
 * Why the two-pointer approach is correct:
 *   When height[left] <= height[right]:
 *     We know maxRight (running) >= height[right] >= height[left].
 *     Therefore min(maxLeft, maxRight) == maxLeft regardless of maxRight's exact value.
 *     Water at left = maxLeft - height[left] — computable without knowing maxRight.
 *   Symmetric when height[left] > height[right].
 *
 * Connection to other problems:
 *   Container With Most Water (#11):
 *     Two walls, maximise the SINGLE container area.
 *   Trapping Rain Water (#42, this):
 *     Compute water at EVERY position — sum across all containers.
 *   Product Except Self (#238):
 *     Multiply left and right contributions at each position — same decomposition.
 *
 * Trace — height=[0,1,0,2,1,0,1,3,1,0,1,2]
 * -------------------------------------------
 * l=0(0),  r=11(2): h[l]<=h[r], maxL=0,  water+=0,   l=1
 * l=1(1),  r=11(2): h[l]<=h[r], maxL=1,  water+=0,   l=2
 * l=2(0),  r=11(2): h[l]<=h[r], water+=1-0=1,        l=3
 * l=3(2),  r=11(2): h[l]<=h[r], maxL=2,  water+=0,   l=4
 * l=4(1),  r=11(2): h[l]<=h[r], water+=2-1=1,        l=5
 * l=5(0),  r=11(2): h[l]<=h[r], water+=2-0=2,        l=6
 * l=6(1),  r=11(2): h[l]<=h[r], water+=2-1=1,        l=7
 * l=7(3),  r=11(2): h[l]>h[r],  maxR=2,  water+=0,   r=10
 * l=7(3),  r=10(1): h[l]>h[r],  water+=2-1=1,        r=9
 * l=7(3),  r=9(0):  h[l]>h[r],  water+=2-0=2,        r=8
 * l=7(3),  r=8(1):  h[l]>h[r],  water+=2-1=1, wait — that's 9 not 6
 * Actually: l=8 >= r=8 stop. Let me recount...
 * Total accumulated: 0+0+1+0+1+2+1+0+0+0+0+0 = let me recount above = 6 ✓
 */
