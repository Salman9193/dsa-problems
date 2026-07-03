import java.util.*;

class Solution {

    // Min Cost to Connect All Points = Minimum Spanning Tree (MST) problem.
    // Connect all n points with n-1 edges, minimising total Manhattan distance.
    // No cycles allowed (tree constraint). This is textbook MST.
    //
    // Two classical MST algorithms:
    //   Prim's:    grow from one node, always pick cheapest edge to unvisited node
    //   Kruskal's: sort all edges, greedily add cheapest that doesn't form cycle (DSU)
    //
    // For DENSE graphs (this is a complete graph with n(n-1)/2 edges):
    //   Prim's with array: O(n²) time, O(n) space — PREFERRED
    //   Kruskal's:         O(n² log n) time, O(n²) space — must store all edges
    //
    // Rule of thumb:
    //   Dense graph (E ≈ V²): Prim's with array
    //   Sparse graph (E ≈ V):  Kruskal's

    // ── Approach 1: Prim's Algorithm with Array (PREFERRED for dense graphs) ─
    //
    // minDist[v] = cheapest known distance from MST to node v.
    // Each iteration: pick the unvisited node with minimum minDist, add to MST,
    // then update minDist for all remaining unvisited nodes.
    //
    // Why O(n²) and not O(n² log n):
    //   We never store all edges — we compute distances on the fly.
    //   The inner loop (update minDist) is O(n) per iteration × n iterations = O(n²).
    //   No sorting, no priority queue overhead for this complete graph.
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        int[] minDist = new int[n]; // cheapest edge from current MST to each unvisited node
        boolean[] inMST = new boolean[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[0] = 0; // start from node 0 (any starting node gives same MST cost)

        int totalCost = 0;

        for (int i = 0; i < n; i++) {
            // Pick the unvisited node closest to the current MST
            int u = -1;
            for (int j = 0; j < n; j++)
                if (!inMST[j] && (u == -1 || minDist[j] < minDist[u]))
                    u = j;

            inMST[u] = true;
            totalCost += minDist[u]; // add the cheapest edge to this node

            // Update minDist for all remaining unvisited nodes
            for (int v = 0; v < n; v++) {
                if (!inMST[v]) {
                    int dist = Math.abs(points[u][0] - points[v][0])
                             + Math.abs(points[u][1] - points[v][1]);
                    // Can node u offer a cheaper connection to node v?
                    minDist[v] = Math.min(minDist[v], dist);
                }
            }
        }

        return totalCost;
    }

    // ── Approach 2: Kruskal's Algorithm with DSU ──────────────────────────────
    //
    // Build ALL edges (n(n-1)/2 of them), sort by weight, add cheapest
    // non-cycle-forming edges until n-1 edges are chosen.
    //
    // Uses DSU (Union-Find) with path compression + union by rank to detect cycles.
    // Stop early once n-1 edges are added (MST is complete).
    //
    // When to prefer Kruskal's over Prim's:
    //   - Sparse graphs where most edges don't exist (E << V²)
    //   - When edge list is already given explicitly (no need to enumerate all pairs)
    //   - When you need to process edges by weight order anyway (e.g. #1631 minimax path)
    public int minCostConnectPointsKruskal(int[][] points) {
        int n = points.length;
        List<int[]> edges = new ArrayList<>(); // {cost, i, j}

        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                edges.add(new int[]{
                    Math.abs(points[i][0]-points[j][0])
                  + Math.abs(points[i][1]-points[j][1]),
                    i, j
                });

        edges.sort((a, b) -> a[0] - b[0]);

        int[] parent = new int[n], rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int totalCost = 0, edgesUsed = 0;
        for (int[] edge : edges) {
            int cost = edge[0], u = edge[1], v = edge[2];
            int pu = find(parent, u), pv = find(parent, v);
            if (pu != pv) {
                union(parent, rank, pu, pv);
                totalCost += cost;
                if (++edgesUsed == n-1) break; // MST complete — stop early
            }
        }
        return totalCost;
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
 * Prim's (array):  Time O(n²),       Space O(n)
 * Kruskal's (DSU): Time O(n² log n), Space O(n²) for edge list
 *
 * For this problem (complete graph, n up to 1000):
 *   n² = 10^6 edges → Prim's array is O(10^6) vs Kruskal's O(10^6 log 10^6) ≈ O(2×10^7)
 *   Prim's stores only O(n) minDist array; Kruskal's stores O(n²) edges
 *   → Prim's is strictly better here on both time and space
 *
 * Why any starting node gives the same MST cost:
 *   MST is unique if all edge weights are distinct (Manhattan distances can repeat,
 *   but the total MST cost is invariant across all starting nodes regardless).
 *
 * Why n-1 edges suffice:
 *   A spanning tree on n nodes has exactly n-1 edges. Adding fewer leaves nodes
 *   disconnected; adding more creates a cycle (which we'd then remove).
 *
 * Trace — points=[[0,0],[2,2],[3,10],[5,2],[7,0]]
 * ------------------------------------------------
 * minDist=[0,INF,INF,INF,INF], all unvisited
 *
 * Iter 0: pick node 0 (dist=0). totalCost=0
 *   Update: dist[1]=|0-2|+|0-2|=4, dist[2]=|0-3|+|0-10|=13,
 *           dist[3]=|0-5|+|0-2|=7, dist[4]=|0-7|+|0-0|=7
 *
 * Iter 1: pick node 1 (dist=4). totalCost=4
 *   Update: dist[2]=min(13,|2-3|+|2-10|)=min(13,9)=9
 *           dist[3]=min(7,|2-5|+|2-2|)=min(7,3)=3
 *           dist[4]=min(7,|2-7|+|2-0|)=min(7,7)=7
 *
 * Iter 2: pick node 3 (dist=3). totalCost=7
 *   Update: dist[2]=min(9,|5-3|+|2-10|)=min(9,10)=9 (no improvement)
 *           dist[4]=min(7,|5-7|+|2-0|)=min(7,4)=4
 *
 * Iter 3: pick node 4 (dist=4). totalCost=11
 *   Update: dist[2]=min(9,|7-3|+|0-10|)=min(9,14)=9 (no improvement)
 *
 * Iter 4: pick node 2 (dist=9). totalCost=20
 *
 * return 20 ✓
 */
