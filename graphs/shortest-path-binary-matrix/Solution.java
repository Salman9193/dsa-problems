import java.util.*;

class Solution {

    // Approach: Single-Source BFS with 8-directional movement — O(n²) time, O(n²) space
    //
    // Key insight: BFS explores cells in order of increasing distance from source.
    // The first time BFS reaches (n-1, n-1) is guaranteed to be the shortest path —
    // BFS never revisits cells and always processes closer cells before farther ones.
    //
    // This is the Lee algorithm (C.Y. Lee, 1961) — originally developed for PCB
    // wire routing: find the shortest electrical path between two pins on a grid
    // while avoiding obstacles (components and existing wires).
    //
    // 8-directional movement: horizontal, vertical, AND diagonal moves each cost 1.
    // This allows the shortest path through a clear grid to be n cells long
    // (one diagonal sweep) rather than 2n-1 (only 4-directional).
    //
    // Why NOT multi-source BFS (unlike 01 Matrix)?
    //   This problem has a SINGLE fixed source (0,0) and single target (n-1,n-1).
    //   Multi-source BFS would give wrong distances — we want distance from (0,0),
    //   not distance from any passable cell.
    //
    // Why mark visited on enqueue (not dequeue)?
    //   Marking on dequeue allows duplicate entries in the queue —
    //   the same cell can be enqueued by multiple neighbours before being processed.
    //   Marking on enqueue ensures each cell enters the queue exactly once. ✓
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;

        // Start or end is blocked — no clear path possible
        if (grid[0][0] == 1 || grid[n-1][n-1] == 1) return -1;

        // Single cell: path length = 1 (just the cell itself)
        if (n == 1) return 1;

        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

        // Queue stores {row, col, pathLength}
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0, 1});
        grid[0][0] = 1; // mark visited on enqueue

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1], dist = cell[2];

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= n || nc < 0 || nc >= n || grid[nr][nc] == 1)
                    continue;

                // Early termination: return as soon as destination is reached
                // First time BFS reaches destination = shortest path (BFS guarantee)
                if (nr == n-1 && nc == n-1) return dist + 1;

                grid[nr][nc] = 1; // mark visited on enqueue
                queue.offer(new int[]{nr, nc, dist + 1});
            }
        }

        return -1; // destination unreachable
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n²) — each cell visited at most once
 * Space: O(n²) — queue holds at most n² cells + grid marking in-place
 *
 * Why early termination works:
 *   BFS processes cells in non-decreasing order of distance from source.
 *   When (n-1, n-1) is first dequeued (or detected as a neighbour), the
 *   current distance is the minimum possible — no shorter path can exist
 *   because any other path to (n-1, n-1) would be processed later (longer).
 *
 * Why check destination among neighbours (not after dequeue)?
 *   Checking when enqueuing saves one BFS level. Since we mark on enqueue,
 *   checking the neighbour immediately gives the correct dist+1 answer
 *   without needing to dequeue the destination cell.
 *
 * 4-directional vs 8-directional:
 *   4-directional: shortest path in n×n clear grid = 2n-1 (right then down)
 *   8-directional: shortest path in n×n clear grid = n (diagonal sweep)
 *   8-directional is strictly better — it models realistic grid movement.
 *
 * Trace — [[0,0,0],[1,1,0],[1,1,0]], n=3
 * -----------------------------------------
 * Init: queue=[(0,0,1)], grid[0][0]=1
 *
 * Process (0,0,1):
 *   8 neighbours: (0,1)=0 → enqueue (0,1,2), mark; (1,1)=1 skip; others OOB/blocked
 *
 * Process (0,1,2):
 *   (0,2)=0 → enqueue (0,2,3), mark
 *   (1,2)=0 → enqueue (1,2,3), mark
 *
 * Process (0,2,3):
 *   (1,2) already marked
 *
 * Process (1,2,3):
 *   (2,2)=0 and is destination → return 3+1 = 4 ✓
 *
 * Trace — [[0,1],[1,0]], n=2
 * ---------------------------
 * Init: queue=[(0,0,1)], grid[0][0]=1
 * Process (0,0,1):
 *   (0,1)=1 blocked, (1,0)=1 blocked
 *   (1,1)=0 → is destination → return 1+1 = 2 ✓
 *
 * Trace — [[1,0,0],[1,1,0],[1,1,0]]
 * ------------------------------------
 * grid[0][0]=1 → return -1 immediately ✓
 */
