import java.util.*;

class Solution {

    // Daily Temperatures — canonical monotonic stack problem.
    // For each day, find how many days until a warmer temperature.
    //
    // Monotonic decreasing stack (by temperature):
    //   Stack stores INDICES of days with unresolved "next warmer" queries.
    //   When we find a day warmer than the stack's top: resolve that day.
    //
    // Key: stack is always in decreasing order of temperatures.
    // When a hotter day arrives, it "pops" all cooler pending days.

    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n]; // default 0 (no warmer day found)
        Deque<Integer> stack = new ArrayDeque<>(); // stores indices

        for (int i = 0; i < n; i++) {
            // Pop all indices whose temperature is less than today's
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int idx = stack.pop();
                result[idx] = i - idx; // days until warmer = i - idx
            }
            stack.push(i); // push current index (unresolved)
        }

        return result;
    }
}

/*
 * Complexity: Time O(n) — each index pushed and popped at most once
 * Space: O(n) — stack + result array
 *
 * The Monotonic Stack Pattern:
 *   "Next Greater Element" family uses a monotonic stack:
 *   - Decreasing stack → resolve when finding something GREATER
 *   - Increasing stack → resolve when finding something SMALLER
 *
 * Same pattern solves:
 *   #496 Next Greater Element I    — same, with hashmap for lookup
 *   #503 Next Greater Element II   — circular array (iterate 2×)
 *   #84  Largest Rectangle in Histogram — monotonic increasing stack
 *   #42  Trapping Rain Water       — monotonic stack alternative
 *   #85  Maximal Rectangle         — apply #84 row by row
 *
 * Trace — temperatures=[73,74,75,71,69,72,76,73]:
 *   i=0: stack=[0] (73, unresolved)
 *   i=1: 74>73 → result[0]=1. stack=[1]
 *   i=2: 75>74 → result[1]=1. stack=[2]
 *   i=3: 71<75 → stack=[2,3]
 *   i=4: 69<71 → stack=[2,3,4]
 *   i=5: 72>69 → result[4]=1; 72>71 → result[3]=2; 72<75 → stack=[2,5]
 *   i=6: 76>72 → result[5]=1; 76>75 → result[2]=4; stack=[6]
 *   i=7: 73<76 → stack=[6,7]
 *   result=[1,1,4,2,1,1,0,0] ✓
 */
