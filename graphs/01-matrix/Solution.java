import java.util.*;

class Solution {

    // Approach 1: Multi-Source BFS — O(mn) time, O(mn) space
    //
    // Key insight: instead of BFS from each '1' to find its nearest '0'
    // (O(mn) per cell = O(m²n²) total), seed BFS from ALL zeros simultaneously.
    // Each '1' gets its distance the first time BFS reaches it — guaranteed
    // to be the minimum distance to any '0'.
    //
    // Why: BFS explores in order of increasing distance from sources.
    // All zeros start at distance 0. BFS assigns distance 1 to all adjacent
    // cells, then distance 2, etc. The first time a cell is reached is
    // via the shortest path from any zero.
    //
    // No levelSize snapshot needed (unlike Rotting Oranges which counts
    // levels/minutes): here we propagate dist[cell]+1 directly, so each
    // cell's distance is self-contained in the array.
    public int[][] updateMatrixBFS(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int[][] dist = new int[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        // Seed all zeros; mark all ones as unvisited
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c});
                } else {
                    dist[r][c] = Integer.MAX_VALUE; // unvisited sentinel
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0]+d[0], nc = cell[1]+d[1];
                // MAX_VALUE means unvisited — first reach = shortest distance
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && dist[nr][nc] == Integer.MAX_VALUE) {
                    dist[nr][nc] = dist[cell[0]][cell[1]] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    // Approach 2: Two-Pass DP — O(mn) time, O(1) extra space
    //
    // Key insight: the nearest zero to cell (r,c) must come from one of
    // four diagonal quadrants. Two passes cover all four directions:
    //   Pass 1 (top-left → bottom-right): propagates influence from top and left
    //   Pass 2 (bottom-right → top-left): propagates influence from bottom and right
    //
    // Together, the two passes compute min distance from any direction.
    //
    // This is the classic two-pass distance transform algorithm (Borgefors 1986) —
    // the same algorithm used in computer vision for binary image distance transforms.
    // The forward/backward scan structure is identical.
    public int[][] updateMatrix(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int INF = rows + cols; // tighter than Integer.MAX_VALUE; avoids overflow on +1
        int[][] dist = new int[rows][cols];

        // Pass 1: propagate from top-left
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) { dist[r][c] = 0; continue; }
                int fromTop  = r > 0       ? dist[r-1][c] + 1 : INF;
                int fromLeft = c > 0       ? dist[r][c-1] + 1 : INF;
                dist[r][c] = Math.min(fromTop, fromLeft);
            }
        }

        // Pass 2: propagate from bottom-right (may improve pass-1 values)
        for (int r = rows-1; r >= 0; r--) {
            for (int c = cols-1; c >= 0; c--) {
                int fromBottom = r < rows-1 ? dist[r+1][c] + 1 : INF;
                int fromRight  = c < cols-1 ? dist[r][c+1] + 1 : INF;
                dist[r][c] = Math.min(dist[r][c], Math.min(fromBottom, fromRight));
            }
        }

        return dist;
    }
}

/*
 * Complexity
 * ----------
 * BFS:         Time O(mn), Space O(mn) for queue + dist array
 * Two-pass DP: Time O(mn), Space O(1) extra (dist array is the output)
 *
 * Why INF = rows + cols (not Integer.MAX_VALUE) in DP?
 *   The maximum possible distance in an m×n grid is m+n-2 (corner to corner).
 *   Using Integer.MAX_VALUE would overflow on +1: MAX_VALUE + 1 → negative.
 *   rows+cols is always a safe upper bound with no overflow risk.
 *
 * Why two passes suffice for all four directions?
 *   Pass 1 (top-left sweep): captures nearest zero from above or left.
 *   Pass 2 (bottom-right sweep): captures nearest zero from below or right.
 *   Combined, all four compass directions are covered → global minimum. ✓
 *
 * Why not just one pass?
 *   A single top-left pass misses zeros that are to the right or below.
 *   Example: cell (0,0)=1, zeros only in (2,2) → pass 1 assigns INF to (0,0)
 *   since it only looks up and left. Pass 2 corrects this via bottom-right propagation.
 *
 * BFS vs DP — when to choose which:
 *   BFS:  intuitive, handles all cases, easy to extend (weighted, 8-dir)
 *   DP:   O(1) extra space, no queue overhead, slightly faster constant
 *   Both: O(mn) time, same correctness guarantee
 *
 * Trace — [[0,0,0],[0,1,0],[1,1,1]] (BFS)
 * -----------------------------------------
 * Initial queue: all (r,c) where mat[r][c]=0:
 *   (0,0),(0,1),(0,2),(1,0),(1,2)
 * dist = [[0,0,0],[0,MAX,0],[MAX,MAX,MAX]]
 *
 * Process (0,1) → (1,1): dist[1][1] = 0+1 = 1, enqueue (1,1)
 * Process (1,0) → (2,0): dist[2][0] = 0+1 = 1, enqueue (2,0)
 * Process (1,2) → (2,2): dist[2][2] = 0+1 = 1, enqueue (2,2)
 * Process (1,1) → (2,1): dist[2][1] = 1+1 = 2, enqueue (2,1)
 *
 * dist = [[0,0,0],[0,1,0],[1,2,1]] ✓
 */
