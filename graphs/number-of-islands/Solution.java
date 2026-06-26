import java.util.*;

class Solution {

    // Three approaches — all equivalent for a single count query.
    // Choose based on constraints:
    //   DFS:         simplest code; risk of stack overflow for large grids (n=10^5×10^5)
    //   BFS:         same complexity; no stack overflow risk; preferred in production
    //   Union-Find:  O(mn α(mn)) ≈ O(mn); best when grid is queried multiple times

    // ── Approach 1: DFS (in-place flood fill) ─────────────────────────────
    //
    // For each unvisited land cell, increment count and DFS-sink the entire
    // island (overwrite '1' → '0'). Each DFS call consumes one island.
    //
    // Why in-place? Avoids a separate visited[][] array — O(1) extra space.
    // Trade-off: mutates the input grid. If immutability is required, use
    // a boolean[][] visited or BFS with explicit queue.
    public int numIslandsDFS(char[][] grid) {
        int islands = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') { islands++; dfs(grid, r, c); }
        return islands;
    }

    // Sink all land cells reachable from (r,c) by marking them '0'
    private void dfs(char[][] grid, int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length
            || grid[r][c] != '1') return;
        grid[r][c] = '0';
        dfs(grid, r+1, c); dfs(grid, r-1, c);
        dfs(grid, r, c+1); dfs(grid, r, c-1);
    }

    // ── Approach 2: BFS (canonical production solution) ───────────────────
    //
    // Same flood-fill logic as DFS but iterative — no call-stack depth risk.
    // Mark cells '0' on enqueue (not dequeue) to prevent duplicate entries.
    //
    // Why mark on enqueue? If we marked on dequeue, the same cell could be
    // added to the queue multiple times before it's processed — wasted work.
    public int numIslands(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int islands = 0;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '1') continue;

                islands++;
                Queue<int[]> queue = new LinkedList<>();
                queue.offer(new int[]{r, c});
                grid[r][c] = '0';  // mark on enqueue

                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    for (int[] d : dirs) {
                        int nr = cell[0]+d[0], nc = cell[1]+d[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && grid[nr][nc] == '1') {
                            grid[nr][nc] = '0'; // mark on enqueue
                            queue.offer(new int[]{nr, nc});
                        }
                    }
                }
            }
        }
        return islands;
    }

    // ── Approach 3: Union-Find ─────────────────────────────────────────────
    //
    // Each land cell = a node. Union adjacent land cells.
    // Count distinct roots = number of islands.
    //
    // Complexity: O(mn × α(mn)) where α = inverse Ackermann function.
    //   α(mn) ≤ 5 for mn ≤ 10^19 (effectively constant).
    //   Full explanation: each union/find costs O(α(n)) amortised due to
    //   path compression + union by rank. Total: O(mn) ops × O(α(mn)) each.
    //
    // Best when: grid is queried multiple times (build once, query O(α)).
    public int numIslandsUF(char[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int[] parent = new int[rows * cols];
        int[] rank   = new int[rows * cols];
        int components = 0;

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == '1') {
                    parent[r*cols+c] = r*cols+c;
                    components++;
                }

        // Only check right and down to avoid processing each edge twice
        int[][] dirs = {{1,0},{0,1}};
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != '1') continue;
                for (int[] d : dirs) {
                    int nr = r+d[0], nc = c+d[1];
                    if (nr < rows && nc < cols && grid[nr][nc] == '1') {
                        int ra = find(parent, r*cols+c);
                        int rb = find(parent, nr*cols+nc);
                        if (ra != rb) { union(parent, rank, ra, rb); components--; }
                    }
                }
            }
        }
        return components;
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        if      (rank[x] < rank[y]) parent[x] = y;
        else if (rank[x] > rank[y]) parent[y] = x;
        else { parent[y] = x; rank[x]++; }
    }
}

/*
 * Complexity
 * ----------
 * DFS/BFS:     Time O(mn),         Space O(mn) recursion stack / queue
 * Union-Find:  Time O(mn α(mn)),   Space O(mn) parent + rank arrays
 *
 * Why O(mn × α(mn)) and not just O(mn)?
 *   Each union/find operation costs O(α(n)) amortised — not O(1).
 *   α is the inverse Ackermann function: α(10^6) = 4, α(10^19) = 5.
 *   Practically indistinguishable from O(1), but theoretically distinct.
 *   Tarjan (1975) proved this is tight — no algorithm achieves O(1) per op.
 *   In interviews: "O(mn) amortised, technically O(mn α(mn))" is correct.
 *
 * Why mark '0' on enqueue (not dequeue) in BFS?
 *   Marking on dequeue allows the same cell to be enqueued multiple times
 *   by different neighbours before it is processed. This wastes queue space
 *   and processing time proportional to the cell's degree (up to 4×).
 *   Marking on enqueue ensures each cell enters the queue exactly once.
 *
 * Trace — grid 2 (3 islands):
 *   ["1","1","0","0","0"]
 *   ["1","1","0","0","0"]
 *   ["0","0","1","0","0"]
 *   ["0","0","0","1","1"]
 *
 *   Scan (0,0)='1': islands=1, BFS sinks {(0,0),(0,1),(1,0),(1,1)}
 *   Scan (2,2)='1': islands=2, BFS sinks {(2,2)}
 *   Scan (3,3)='1': islands=3, BFS sinks {(3,3),(3,4)}
 *   return 3 ✓
 *
 * Real-world connection:
 *   This IS Connected Component Labelling (CCL) — the fundamental operation
 *   in computer vision for identifying distinct objects in binary images.
 *   OpenCV's floodFill() implements exactly the BFS approach above.
 */
