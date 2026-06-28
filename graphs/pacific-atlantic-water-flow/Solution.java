import java.util.*;

class Solution {

    // Approach: Reverse BFS from both ocean borders — O(mn) time, O(mn) space
    //
    // Key insight: instead of BFS from each cell to check if it reaches both
    // oceans (O(mn × (m+n)) total), REVERSE the problem:
    //   Ask: "from which cells can the ocean reach uphill?"
    //   Forward:  water flows A→B if height[B] ≤ height[A]  (downhill)
    //   Reverse:  water flows B→A if height[A] ≥ height[B]  (uphill)
    //
    // Two separate BFS, both using the reversed (uphill) condition:
    //   1. Pacific BFS: seed top row + left column (Pacific border cells)
    //   2. Atlantic BFS: seed bottom row + right column (Atlantic border cells)
    //
    // A cell can flow to BOTH oceans iff it's reachable in BOTH BFS runs.
    //
    // Why reverse BFS instead of forward BFS?
    //   Forward BFS from each cell: O(mn) per cell → O(m²n²) total — too slow.
    //   Reverse BFS from borders: each cell visited at most twice → O(mn) total.
    //   The reversal is correct because "A can reach ocean" ↔ "ocean can reach A uphill".
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] pacific  = new boolean[rows][cols];
        boolean[][] atlantic = new boolean[rows][cols];

        Queue<int[]> pacQueue = new LinkedList<>();
        Queue<int[]> atlQueue = new LinkedList<>();

        // Seed Pacific border: top row + left column
        for (int r = 0; r < rows; r++) {
            pacQueue.offer(new int[]{r, 0});
            pacific[r][0] = true;
        }
        for (int c = 0; c < cols; c++) {
            if (!pacific[0][c]) { pacQueue.offer(new int[]{0, c}); pacific[0][c] = true; }
        }

        // Seed Atlantic border: bottom row + right column
        for (int r = 0; r < rows; r++) {
            atlQueue.offer(new int[]{r, cols-1});
            atlantic[r][cols-1] = true;
        }
        for (int c = 0; c < cols; c++) {
            if (!atlantic[rows-1][c]) { atlQueue.offer(new int[]{rows-1, c}); atlantic[rows-1][c] = true; }
        }

        bfs(heights, pacQueue,  pacific);
        bfs(heights, atlQueue, atlantic);

        // Collect cells reachable from both oceans
        List<List<Integer>> result = new ArrayList<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (pacific[r][c] && atlantic[r][c])
                    result.add(List.of(r, c));

        return result;
    }

    // Uphill BFS: from ocean border, expand to neighbours with height >= current
    // This is the reverse of water flowing downhill from land to ocean
    private void bfs(int[][] heights, Queue<int[]> queue, boolean[][] visited) {
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        int rows = heights.length, cols = heights[0].length;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0]+d[0], nc = cell[1]+d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && !visited[nr][nc]
                    && heights[nr][nc] >= heights[cell[0]][cell[1]]) { // uphill ≥
                    visited[nr][nc] = true; // mark on enqueue
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(mn) — each cell visited at most twice (once per BFS)
 * Space: O(mn) — two visited arrays + two queues
 *
 * Why uphill condition (≥) not downhill (≤)?
 *   We are expanding FROM the ocean inward.
 *   The ocean can "reach" a cell if the cell's height ≥ ocean cell's height
 *   (water could flow downhill from that cell to the ocean cell).
 *   So from ocean border cell (height h), we can expand to neighbour with height ≥ h.
 *
 * Correctness argument:
 *   Cell X can flow to Pacific iff Pacific BFS (uphill from Pacific border) reaches X.
 *   Proof: X can flow to Pacific border cell P means there exists a downhill path X→...→P.
 *   Reversing: P can reach X going uphill, so Pacific BFS starting from P will visit X. ✓
 *
 * Real-world connection:
 *   This is D4 watershed delineation on a Digital Elevation Model (DEM):
 *   heights[] = DEM raster, Pacific border = one ocean outlet,
 *   Atlantic border = another ocean outlet.
 *   Production GIS tools (ArcGIS, QGIS, GRASS) run the same reverse-flow BFS
 *   to delineate which land areas drain into which watershed.
 *
 * Trace — [[1,2,3],[8,9,4],[7,6,5]]
 * ------------------------------------
 * Pacific seeds: (0,0),(1,0),(2,0),(0,1),(0,2) — top + left
 * Pacific BFS uphill: (1,0)=8 → (1,1)=9≥8 ✓ → reaches all cells
 *
 * Atlantic seeds: (0,2),(1,2),(2,2),(2,1),(2,0) — bottom + right
 * Atlantic BFS uphill:
 *   (2,2)=5 → (2,1)=6≥5 ✓
 *   (2,1)=6 → (2,0)=7≥6 ✓, (1,1)=9≥6 ✓
 *   (1,1)=9 → (0,1)=2<9 ✗, (1,0)=8<9 ✗
 *   Atlantic = {(0,2),(1,2),(2,2),(2,1),(2,0),(1,1)}
 *
 * Intersection = all cells (since Pacific reaches all) ∩ Atlantic
 *             = {(0,2),(1,1),(2,0),(2,1),(2,2)}
 */
