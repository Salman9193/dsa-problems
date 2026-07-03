import java.util.*;

class Solution {

    // Largest Rectangle in Histogram — the canonical monotonic stack Hard.
    // Find the largest rectangle that fits within histogram bars.
    //
    // Key insight: for each bar, find how far LEFT and RIGHT it can extend
    // before hitting a shorter bar. The rectangle height = this bar's height.
    //
    // Monotonic increasing stack: bars stay in increasing height order.
    // When a shorter bar is found (breaking the increasing order), all taller
    // bars on the stack are resolved — their right boundary is this shorter bar.

    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int maxArea = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // indices, monotonic increasing heights

        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i]; // sentinel 0 at end flushes stack

            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];              // bar height
                int width  = stack.isEmpty() ? i : i - stack.peek() - 1; // span
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }

        return maxArea;
    }
}

/*
 * Complexity: Time O(n), Space O(n)
 *
 * Why monotonic INCREASING stack (opposite of Daily Temperatures)?
 *   We want to find "next SMALLER" for right boundary.
 *   Stack stays increasing → a bar stays until something SMALLER arrives.
 *   Daily Temperatures: "next GREATER" → stack stays decreasing.
 *
 * Width calculation when popping bar at index `top`:
 *   Right boundary: current index i (first bar shorter than `top`)
 *   Left boundary: stack.peek() after popping (last bar still shorter than `top`)
 *   Width = i - stack.peek() - 1    (or i if stack is empty = extends to index 0)
 *
 * Trace — heights=[2,1,5,6,2,3]:
 *   i=0: h=2, stack=[0]
 *   i=1: h=1<2 → pop 0: height=2, width=1-(-1)-1=1? (stack empty: width=i=1)
 *         area=2. push 1. stack=[1]
 *   i=2: h=5>1, stack=[1,2]
 *   i=3: h=6>5, stack=[1,2,3]
 *   i=4: h=2<6 → pop 3: height=6,width=4-2-1=1,area=6
 *         h=2<5 → pop 2: height=5,width=4-1-1=2,area=10
 *         h=2>=2, push 4. stack=[1,4]
 *   i=5: h=3>2, stack=[1,4,5]
 *   i=6: sentinel h=0 → pop 5: h=3,w=6-4-1=1,area=3
 *         pop 4: h=2,w=6-1-1=4,area=8
 *         pop 1: h=1,w=6(stack empty),area=6
 *   return 10 ✓
 */
