import java.util.*;

class Solution {

    // Approach 1: DFS (in-place, cleanest) — O(mn) time, O(mn) space
    //
    // Key insight: same flood-fill as Number of Islands (#200), but instead
    // of counting islands, we MEASURE their size. DFS returns the area of the
    // island it sinks — each cell contributes 1 as the recursion unwinds.
    //
    // return 1 + dfs(up) + dfs(down) + dfs(left) + dfs(right)
    // Each recursive call either returns 0 (boundary/water/visited) or 1+subtree.
    // The sum accumulates the total cell count for this island.
    //
    // In-place sinking (grid[r][c] = 0) marks visited cells without a separate
    // boolean[][] array. Trade-off: mutates input. Use visited[][] if immutability needed.
    public int maxAreaOfIslandDFS(int[][] grid) {
        int maxArea = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == 1)
                    maxArea = Math.max(maxArea, dfs(grid, r, c));
        return maxArea;
    }

    private int dfs(int[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length
            || grid[r][c] != 1) return 0;
        grid[r][c] = 0; // sink — mark visited
        return 1
            + dfs(grid, r+1, c)
            + dfs(grid, r-1, c)
            + dfs(grid, r, c+1)
            + dfs(grid, r, c-1);
    }

    // Approach 2: BFS (no stack overflow risk) — O(mn) time, O(mn) space
    //
    // Preferred for large grids: DFS recursion depth = island size, which can
    // be O(mn) for a single giant island — risks StackOverflowError in Java.
    // BFS uses an explicit queue → safe for any grid size.
    //
    // Mark cells on ENQUEUE (not dequeue) to prevent the same cell being added
    // multiple times by different neighbours before it is processed.
    public int maxAreaOfIsland(int[][] grid) {
        int rows = grid.length, cols = grid[0].length, maxArea = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 1) continue;

                int area = 0;
                Queue<int[]> queue = new LinkedList<>();
                queue.offer(new int[]{r, c});
                grid[r][c] = 0; // mark on enqueue

                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    area++;
                    for (int[] d : dirs) {
                        int nr = cell[0]+d[0], nc = cell[1]+d[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && grid[nr][nc] == 1) {
                            grid[nr][nc] = 0; // mark on enqueue
                            queue.offer(new int[]{nr, nc});
                        }
                    }
                }
                maxArea = Math.max(maxArea, area);
            }
        }
        return maxArea;
    }
}

/*
 * Complexity
 * ----------
 * DFS: Time O(mn), Space O(mn) recursion stack (risky for large grids)
 * BFS: Time O(mn), Space O(mn) queue (safe for any grid size)
 *
 * DFS return value accumulation:
 *   dfs(r,c) returns the total area of the island containing (r,c).
 *   Base case: out-of-bounds or water → 0 (no cells)
 *   Recursive case: 1 (this cell) + areas of all 4 sub-islands
 *   Each cell contributes exactly 1 to its island's area as the stack unwinds.
 *
 * Comparison with Number of Islands (#200):
 *   #200: islands++; dfs(grid,r,c); // dfs returns void, just sinks
 *   #695: maxArea = max(maxArea, dfs(grid,r,c)); // dfs returns int area
 *   Same flood-fill — different accumulation.
 *
 * Trace — [[1,1,0],[0,1,0],[0,1,1]]
 * ------------------------------------
 * DFS from (0,0):
 *   dfs(0,0)=1: sink (0,0)
 *     dfs(1,0)=0: water
 *     dfs(-1,0)=0: OOB
 *     dfs(0,1)=1: sink (0,1)
 *       dfs(1,1)=1: sink (1,1)
 *         dfs(2,1)=1: sink (2,1)
 *           dfs(2,2)=1: sink (2,2) → return 1
 *         return 1+1=2
 *       return 1+2=3
 *     return 1+3=4
 *   return 1+4=5
 * maxArea = 5 ✓
 */
