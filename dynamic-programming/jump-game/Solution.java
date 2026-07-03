class Solution {

    // Jump Game: can you reach the last index?
    // nums[i] = max jump length from position i.
    //
    // Approach 1: Greedy — track the farthest reachable position.
    // If current index exceeds farthest, it's unreachable → false.
    //
    // Approach 2: DP O(n²) — dp[i] = true if index i is reachable.
    // Greedy is always preferred here (O(n) vs O(n²)).

    // ── Greedy O(n) ───────────────────────────────────────────────────────────
    public boolean canJump(int[] nums) {
        int maxReach = 0;

        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // current position unreachable
            maxReach = Math.max(maxReach, i + nums[i]);
        }

        return true;
    }
}

/*
 * Complexity: Time O(n), Space O(1)
 *
 * Why greedy is correct:
 *   If position i is reachable (i <= maxReach), we can stand there and update
 *   maxReach. We never need to know HOW we reached i — only that we can.
 *   The maximum reachable position is monotonically non-decreasing as we scan.
 *
 * Extension: Jump Game II (#45)
 *   Minimum number of jumps to reach the end.
 *   Same greedy: for each "window" [prev_end+1, curr_end], find the farthest
 *   reachable next_end = max(i + nums[i]). Each window = one jump.
 *
 * Trace — nums=[2,3,1,1,4]:
 *   i=0: maxReach=max(0,0+2)=2
 *   i=1: maxReach=max(2,1+3)=4
 *   i=2: maxReach=max(4,2+1)=4
 *   i=3: maxReach=max(4,3+1)=4
 *   i=4: maxReach=max(4,4+4)=8
 *   return true ✓
 *
 * Trace — nums=[3,2,1,0,4]:
 *   i=0: maxReach=3
 *   i=1: maxReach=3
 *   i=2: maxReach=3
 *   i=3: maxReach=3
 *   i=4: i=4 > maxReach=3 → return false ✓
 */
