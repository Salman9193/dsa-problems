import java.util.*;

class Solution {

    // Approach: Multi-Source BFS — O(mn) time, O(mn) space
    //
    // Key insight: multiple rotten oranges spread simultaneously, so we must
    // start BFS from ALL rotten sources at once (not one at a time).
    // This gives each fresh orange the shortest distance to ANY rotten source,
    // which equals the time it takes to rot.
    //
    // Why multi-source BFS and not DFS?
    //   DFS doesn't give shortest paths — it may reach a fresh orange via a
    //   long path before finding the short path from a closer rotten source,
    //   producing an incorrect (too high) time.
    //
    // Why mark on enqueue (not dequeue)?
    //   If marked on dequeue, the same cell could be enqueued multiple times
    //   by different neighbours before it's processed — wasted work.
    //   Marking on enqueue ensures each cell enters the queue exactly once.
    public int orangesRotting(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int fresh = 0;

        // Seed queue with ALL rotten oranges simultaneously (multi-source BFS)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c});
                if (grid[r][c] == 1) fresh++;
            }
        }

        // Edge case: no fresh oranges — already done regardless of rotten count
        if (fresh == 0) return 0;

        int minutes = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        // Process one BFS level per minute
        // Each level = all oranges that rot at the same time step
        while (!queue.isEmpty() && fresh > 0) {
            minutes++;
            int levelSize = queue.size(); // snapshot: only process this level's nodes

            for (int i = 0; i < levelSize; i++) {
                int[] cell = queue.poll();
                for (int[] d : dirs) {
                    int nr = cell[0]+d[0], nc = cell[1]+d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2; // rot it (mark on enqueue)
                        fresh--;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        // If fresh > 0, some oranges were isolated and could never rot
        return fresh == 0 ? minutes : -1;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(mn) — each cell is enqueued and dequeued at most once
 * Space: O(mn) — queue holds at most mn cells in the worst case
 *
 * The multi-source BFS computes, for each fresh orange, the shortest
 * distance from any rotten orange to that cell. The answer is the maximum
 * of these shortest distances — the last orange to rot determines the time.
 *
 * Why level-by-level (levelSize snapshot)?
 *   Without the snapshot, we'd mix cells from different time steps.
 *   The snapshot freezes how many cells belong to "this minute" before
 *   we start adding next-minute cells. Same as the level-order traversal
 *   pattern used in BFS on trees.
 *
 * Edge cases:
 *   fresh==0 initially → return 0 (no fresh oranges to rot)
 *   queue empty initially (no rotten oranges) but fresh>0 → loop never runs → -1 ✓
 *   isolated fresh orange (no path from any rotten) → fresh>0 after BFS → -1 ✓
 *
 * Trace — [[2,1,1],[1,1,0],[0,1,1]]
 * ------------------------------------
 * Initial: queue=[(0,0)], fresh=6
 *
 * Minute 1: level={(0,0)} → rot (0,1),(1,0). fresh=4, queue=[(0,1),(1,0)]
 * Minute 2: level={(0,1),(1,0)} → rot (0,2),(1,1). fresh=2, queue=[(0,2),(1,1)]
 * Minute 3: level={(0,2),(1,1)} → rot (2,1). fresh=1, queue=[(2,1)]
 * Minute 4: level={(2,1)} → rot (2,2). fresh=0, queue=[]
 *
 * fresh==0 → return 4 ✓
 *
 * Trace — [[2,1,1],[0,1,1],[1,0,1]]
 * ------------------------------------
 * (1,0),(2,0),(2,2) are isolated: (1,0) blocked by (1,0)='0',
 * (2,0) has no rotten neighbour path, (2,2) disconnected.
 * BFS terminates with fresh>0 → return -1 ✓
 */
