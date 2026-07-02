import java.util.*;

class Solution {

    // Swim in Rising Water = minimax path problem on a grid.
    // Find the minimum time t such that a path exists from (0,0) to (n-1,n-1)
    // where every cell on the path has elevation <= t.
    //
    // This is IDENTICAL in structure to Path with Minimum Effort (#1631):
    //   #1631: minimize max |heights[a]-heights[b]| on adjacent cells (edge weight)
    //   #778:  minimize max grid[r][c] along the path (node weight)
    // Both are minimax path problems. All three algorithms apply to both.
    //
    // Key difference from Cheapest Flights (#787):
    //   #787 needs K-limited Bellman-Ford (hop constraint, additive cost)
    //   #778 needs Dijkstra/Binary Search/DSU (no hop constraint, max cost)

    // ── Approach 1: Modified Dijkstra (PREFERRED) ─────────────────────────
    //
    // dist[r][c] = minimum possible maximum elevation to reach (r,c).
    // Relaxation: newT = max(current_max, grid[nr][nc])
    //   — inheriting the current path's worst elevation, or the neighbour's,
    //   whichever is higher.
    //
    // Why max() is a valid Dijkstra relaxation (same proof as #1631):
    //   max(dist[curr], grid[nr][nc]) >= dist[curr] always (non-decreasing).
    //   So the priority queue greedy invariant holds — smallest popped = optimal.
    //
    // Early termination: first time we pop (n-1,n-1) = optimal answer.
    public int swimInWater(int[][] grid) {
        int n = grid.length;
        int[][] dist = new int[n][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = grid[0][0];

        // {maxElevation, row, col}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{grid[0][0], 0, 0});

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int t = curr[0], r = curr[1], c = curr[2];

            if (r == n-1 && c == n-1) return t; // first pop of destination = optimal
            if (t > dist[r][c]) continue;        // stale entry

            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                if (nr < 0 || nr >= n || nc < 0 || nc >= n) continue;

                // max: inherit current worst OR take neighbour's elevation
                int newT = Math.max(t, grid[nr][nc]);
                if (newT < dist[nr][nc]) {
                    dist[nr][nc] = newT;
                    pq.offer(new int[]{newT, nr, nc});
                }
            }
        }
        return dist[n-1][n-1];
    }

    // ── Approach 2: Binary Search + BFS ───────────────────────────────────
    //
    // Binary search on t. For each candidate, check if a path exists using
    // only cells with elevation <= t (simple BFS/DFS connectivity check).
    //
    // Search space: [grid[0][0], n*n-1]
    //   Lower bound: must include start cell (grid[0][0] is always needed)
    //   Upper bound: n*n-1 (maximum possible elevation in the grid)
    //
    // Why this works: the answer is monotone — if we can swim at time t,
    // we can also swim at any time t' > t. Binary search on monotone functions
    // in O(log(range)) feasibility checks.
    public int swimInWaterBS(int[][] grid) {
        int n = grid.length;
        int lo = grid[0][0], hi = n*n-1;

        while (lo < hi) {
            int mid = lo + (hi-lo)/2;
            if (canSwim(grid, mid, n)) hi = mid;
            else lo = mid+1;
        }
        return lo;
    }

    private boolean canSwim(int[][] grid, int t, int n) {
        if (grid[0][0] > t) return false;
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0,0});
        visited[0][0] = true;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            if (cell[0]==n-1 && cell[1]==n-1) return true;
            for (int[] d : dirs) {
                int nr = cell[0]+d[0], nc = cell[1]+d[1];
                if (nr>=0 && nr<n && nc>=0 && nc<n
                    && !visited[nr][nc] && grid[nr][nc]<=t) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr,nc});
                }
            }
        }
        return false;
    }

    // ── Approach 3: Kruskal's DSU ─────────────────────────────────────────
    //
    // Sort all cells by elevation. Add them one by one in elevation order,
    // unioning with already-added neighbours each time. Return the elevation
    // of the first cell that connects (0,0) to (n-1,n-1).
    //
    // Why this is correct (MST-minimax theorem):
    //   The minimax path lies on the MST. Kruskal's processes edges (here:
    //   cells acting as nodes) in increasing weight order. The first moment
    //   source and destination become connected is when the bottleneck cell
    //   (the minimax cell on the optimal path) is added.
    //
    // Note: here CELLS are the "edges" being added (node-weighted graph),
    // not actual graph edges. Each cell addition potentially merges up to
    // 4 adjacent already-added cells.
    public int swimInWaterDSU(int[][] grid) {
        int n = grid.length;
        int[][] cells = new int[n*n][3]; // {elevation, row, col}
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                cells[r*n+c] = new int[]{grid[r][c], r, c};
        Arrays.sort(cells, (a,b) -> a[0]-b[0]);

        int[] parent = new int[n*n], rank = new int[n*n];
        for (int i = 0; i < n*n; i++) parent[i] = i;
        boolean[] added = new boolean[n*n];

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        for (int[] cell : cells) {
            int t = cell[0], r = cell[1], c = cell[2];
            int idx = r*n+c;
            added[idx] = true;

            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                int nidx = nr*n+nc;
                if (nr>=0 && nr<n && nc>=0 && nc<n && added[nidx])
                    union(parent, rank, idx, nidx);
            }

            // As soon as top-left and bottom-right are in the same component
            if (find(parent,0) == find(parent,n*n-1)) return t;
        }
        return n*n-1; // unreachable for valid inputs
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }
    private void union(int[] parent, int[] rank, int x, int y) {
        int px = find(parent,x), py = find(parent,y);
        if (px==py) return;
        if      (rank[px]<rank[py]) parent[px]=py;
        else if (rank[px]>rank[py]) parent[py]=px;
        else { parent[py]=px; rank[px]++; }
    }
}

/*
 * Complexity
 * ----------
 * Modified Dijkstra: Time O(n² log n), Space O(n²)
 * Binary Search+BFS: Time O(n² log n), Space O(n²)
 * Kruskal's DSU:     Time O(n² log n), Space O(n²)
 *
 * All three are equivalent in complexity.
 * Dijkstra preferred: single pass, early exit, most direct.
 *
 * Swim vs Path with Minimum Effort (#1631):
 *   #1631 edge weight = |heights[a] - heights[b]|  (difference between cells)
 *   #778  node weight = grid[nr][nc]                (raw elevation of destination)
 *   Both are minimax path problems — same three algorithms, same complexity.
 *
 * Swim vs Network Delay Time (#743):
 *   #743: standard Dijkstra, sum of edge weights, find max over all nodes
 *   #778: modified Dijkstra, max() relaxation, early exit at destination
 *
 * Swim vs Cheapest Flights (#787):
 *   #787: K-limited Bellman-Ford, hop constraint, additive cost
 *   #778: Modified Dijkstra, no hop constraint, max cost
 *
 * Trace — grid=[[0,2],[1,3]]
 * ---------------------------
 * Initial: dist=[[0,INF],[INF,INF]]
 * pq=[(0,0,0)]
 *
 * Pop (0,0,0): neighbours:
 *   (0,1): newT=max(0,2)=2 → dist[0][1]=2, enqueue (2,0,1)
 *   (1,0): newT=max(0,1)=1 → dist[1][0]=1, enqueue (1,1,0)
 *
 * Pop (1,1,0): neighbours:
 *   (1,1): newT=max(1,3)=3 → dist[1][1]=3, enqueue (3,1,1)
 *   (0,0): already 0
 *
 * Pop (2,0,1): neighbours:
 *   (1,1): newT=max(2,3)=3, not < dist[1][1]=3 → skip
 *   (0,0): already visited
 *
 * Pop (3,1,1): r==n-1, c==n-1 → return 3 ✓
 */
