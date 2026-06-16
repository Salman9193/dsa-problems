import java.util.ArrayDeque;
import java.util.Deque;

class Solution {

    // Approach: Monotonic Decreasing Deque — O(n) time, O(k) space
    //
    // Maintain a deque of INDICES where the VALUES are in decreasing order.
    // The front of the deque is always the index of the current window's maximum.
    //
    // Three invariants maintained at each step:
    //   1. Deque front is always within the current window [i-k+1, i]
    //   2. Values from front to back are strictly decreasing
    //   3. result[i-k+1] = nums[deque.front()] once i >= k-1
    //
    // Why remove smaller elements from the back?
    //   If nums[j] <= nums[i] and j < i (j is to the left of i):
    //     - nums[i] is at least as large as nums[j]
    //     - nums[i] stays in the window longer than nums[j]
    //     - So nums[j] can NEVER be the maximum of any future window
    //     - Safe to discard nums[j] immediately
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();  // stores indices

        for (int i = 0; i < n; i++) {
            // Step 1: Remove expired indices from front
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1)
                dq.pollFirst();

            // Step 2: Remove indices with values <= nums[i] from back
            //         (they can never be max while nums[i] is in window)
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i])
                dq.pollLast();

            // Step 3: Add current index
            dq.offerLast(i);

            // Step 4: Record maximum once first window is complete
            if (i >= k - 1)
                result[i - k + 1] = nums[dq.peekFirst()];
        }

        return result;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — each index added to deque once, removed at most once
 * Space: O(k) — deque holds at most k indices at any time
 *
 * Why <= not < when removing from back?
 *   Using <= removes equal values too — keeps only the RIGHTMOST occurrence.
 *   When two equal values exist, the rightmost stays in the window longer
 *   and is the better candidate for future windows.
 *
 * Why store indices, not values?
 *   Indices let us check expiry (dq.front() < i - k + 1).
 *   Values alone can't tell us how far left an element is.
 *
 * Monotonic Deque Family:
 *   Sliding Window Maximum (#239) → decreasing deque, front = max
 *   Sliding Window Minimum        → increasing deque, front = min
 *   Largest Rectangle Histogram (#84) → increasing stack of bar heights
 *   Next Greater Element (#496)   → decreasing stack, pop when greater found
 *   Daily Temperatures (#739)     → decreasing stack, pop when warmer found
 *
 * Trace — nums=[1,3,-1,-3,5,3,6,7], k=3
 * ----------------------------------------
 * i=0 (1):  dq=[0]           (values:[1])
 * i=1 (3):  pop 0(1≤3) → dq=[1]    (values:[3])
 * i=2 (-1): dq=[1,2]         (values:[3,-1])   result[0]=nums[1]=3 ✓
 * i=3 (-3): dq=[1,2,3]       (values:[3,-1,-3]) result[1]=nums[1]=3 ✓
 * i=4 (5):  pop 3,2,1 → dq=[4]     (values:[5])  result[2]=nums[4]=5 ✓
 * i=5 (3):  dq=[4,5]         (values:[5,3])    result[3]=nums[4]=5 ✓
 * i=6 (6):  pop 5,4 → dq=[6]        (values:[6])  result[4]=nums[6]=6 ✓
 * i=7 (7):  pop 6 → dq=[7]          (values:[7])  result[5]=nums[7]=7 ✓
 *
 * Output: [3,3,5,5,6,7] ✓
 */
