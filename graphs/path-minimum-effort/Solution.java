import java.util.*;

class Solution {

    // Path with Minimum Effort = minimax path problem:
    // Find the path from (0,0) to (m-1,n-1) that minimises the MAXIMUM
    // absolute height difference across any single step.
    //
    // This is NOT standard Dijkstra (which minimises SUM of edge weights).
    // The adaptation: dist[cell] = minimum possible MAXIMUM effort to reach
    // the cell, not total accumulated cost.
    //
    // Three approaches — all correct, different trade-offs.

    // ── Approach 1: Modified Dijkstra (PREFERRED) ─────────────────────────
    //
    // Adapt Dijkstra's relaxation from sum to max:
    //   Standard: newDist = dist[curr] + weight
    //   This:     newDist = max(dist[curr], |heights[nr][nc] - heights[r][c]|)
    //
    // Why this is still correct:
    //   Dijkstra's correctness relies on the cost function being monotonically
    //   non-decreasing as we move further from the source. Both sum and max
    //   satisfy this property (adding a non-negative value never decreases the
    //   result). So the greedy "process smallest first" invariant holds.
    //
    // Why NOT plain BFS:
    //   BFS gives shortest hop count — doesn't account for height differences.
    //   We need a priority queue to always process the minimum-effort frontier.
    //
    // Early termination: first time we pop the destination from the priority
    // queue, that effort value is optimal (same invariant as Dijkstra's).
    public int minimumEffortPath(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = 0;

        // {effort, row, col} — min-heap by effort
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0, 0});

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int effort = curr[0], r = curr[1], c = curr[2];

            // Early termination: first pop of destination = optimal answer
            if (r == rows-1 && c == cols-1) return effort;

            if (effort > dist[r][c]) continue; // stale entry — skip

            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;

                // Key adaptation: max instead of sum
                int newEffort = Math.max(effort,
                    Math.abs(heights[nr][nc] - heights[r][c]));

                if (newEffort < dist[nr][nc]) {
                    dist[nr][nc] = newEffort;
                    pq.offer(new int[]{newEffort, nr, nc});
                }
            }
        }

        return dist[rows-1][cols-1];
    }

    // ── Approach 2: Binary Search + BFS ───────────────────────────────────
    //
    // Binary search on the answer (effort k). For each candidate k, check
    // if a path exists using only edges with |h1-h2| <= k.
    //
    // When to prefer over Dijkstra: when the feasibility check is much
    // simpler than the optimisation (BFS is simpler than priority queue).
    // Here they're similar complexity, so Dijkstra is still preferred.
    public int minimumEffortPathBS(int[][] heights) {
        int lo = 0, hi = 1_000_000;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canReach(heights, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    private boolean canReach(int[][] heights, int maxEffort) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            if (r == rows-1 && c == cols-1) return true;
            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && !visited[nr][nc]
                    && Math.abs(heights[nr][nc]-heights[r][c]) <= maxEffort) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return false;
    }

    // ── Approach 3: Kruskal's MST + Union-Find ────────────────────────────
    //
    // Deep insight: the minimax path between any two nodes in a graph
    // lies entirely within the MST. Proof sketch: if the minimax path
    // uses an edge e not in the MST, replacing e with the path's MST
    // counterpart never increases the maximum edge weight.
    //
    // So: sort all edges by |height difference|, add them via Union-Find.
    // The FIRST edge that connects (0,0) and (m-1,n-1) IS the minimax edge
    // (its weight = the answer). This is exactly Kruskal's algorithm
    // stopping early when source and destination are in the same component.
    public int minimumEffortPathDSU(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        List<int[]> edges = new ArrayList<>(); // {weight, node1, node2}

        int[][] dirs = {{0,1},{1,0}}; // right + down only (undirected)
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                for (int[] d : dirs) {
                    int nr = r+d[0], nc = c+d[1];
                    if (nr < rows && nc < cols)
                        edges.add(new int[]{
                            Math.abs(heights[nr][nc]-heights[r][c]),
                            r*cols+c, nr*cols+nc
                        });
                }

        edges.sort((a, b) -> a[0]-b[0]); // sort edges by weight ascending

        int[] parent = new int[rows*cols], rank = new int[rows*cols];
        for (int i = 0; i < rows*cols; i++) parent[i] = i;

        for (int[] edge : edges) {
            union(parent, rank, edge[1], edge[2]);
            // Once source (0) and destination (rows*cols-1) are connected,
            // this edge's weight is the minimax bottleneck
            if (find(parent, 0) == find(parent, rows*cols-1)) return edge[0];
        }

        return 0; // single cell grid
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        int px = find(parent, x), py = find(parent, y);
        if (px == py) return;
        if      (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }
    }
}

/*
 * Complexity
 * ----------
 * Modified Dijkstra: Time O(mn log mn), Space O(mn)
 * Binary Search+BFS: Time O(mn log(max_height)), Space O(mn)
 * Kruskal's DSU:     Time O(mn log mn), Space O(mn)
 *
 * All three are O(mn log mn) — modified Dijkstra preferred for:
 *   - Single pass (no binary search outer loop)
 *   - Early termination on first pop of destination
 *   - Most direct adaptation of familiar Dijkstra's
 *
 * Why max() is a valid Dijkstra relaxation:
 *   Dijkstra's correctness requires: the cost function is non-decreasing
 *   as the path extends. Both sum and max satisfy this when edge weights
 *   are non-negative (|height difference| is always ≥ 0). Specifically:
 *     max(dist[curr], newEdge) ≥ dist[curr]  (max never decreases)
 *   So the greedy invariant holds: the smallest value popped from the
 *   priority queue is always finalized and optimal.
 *
 * The Kruskal / MST insight (deep result):
 *   The minimax path between two nodes uses only edges from the MST.
 *   Proof: if the path uses edge e ∉ MST, the MST cycle property says
 *   e is the MAXIMUM weight edge in some cycle. Replacing e with the
 *   alternative cycle path gives a path whose max edge is ≤ max(e) —
 *   so e was not needed. Therefore the MST contains all minimax paths,
 *   and Kruskal's stops at the exact right edge.
 *
 * Trace — heights=[[1,2,2],[3,8,2],[5,3,5]]
 * -------------------------------------------
 * Modified Dijkstra:
 *   dist = [[0,INF,INF],[INF,INF,INF],[INF,INF,INF]]
 *   pq=[(0,0,0)]
 *
 *   Pop (0,0,0): neighbours (0,1): effort=max(0,|2-1|)=1; (1,0): effort=max(0,|3-1|)=2
 *   pq=[(1,0,1),(2,1,0)]
 *
 *   Pop (1,0,1): (0,2): effort=max(1,|2-2|)=1; (1,1): effort=max(1,|8-2|)=6
 *   pq=[(1,0,2),(2,1,0),(6,1,1)]
 *
 *   Pop (1,0,2): (1,2): effort=max(1,|2-2|)=1
 *   pq=[(1,1,2),(2,1,0),(6,1,1)]
 *
 *   Pop (1,1,2): (2,2): effort=max(1,|5-2|)=3
 *   pq=[(2,1,0),(3,2,2),(6,1,1)]
 *
 *   Pop (2,1,0): (2,0): effort=max(2,|5-3|)=2; (1,1): max(2,|8-3|)=5
 *   pq=[(2,2,0),(3,2,2),(5,1,1),(6,1,1)]
 *
 *   Pop (2,2,0): (2,1): effort=max(2,|3-5|)=2
 *   pq=[(2,2,1),(3,2,2),(5,1,1),(6,1,1)]
 *
 *   Pop (2,2,1): (2,2): effort=max(2,|5-3|)=2 < dist[2][2]=3 → update dist[2][2]=2
 *   pq=[(2,2,2),(3,2,2),(5,1,1),(6,1,1)]
 *
 *   Pop (2,2,2): r==rows-1, c==cols-1 → return 2 ✓
 */
