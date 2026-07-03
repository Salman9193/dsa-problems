import java.util.*;

class Solution {

    // Connecting Cities With Minimum Cost = Minimum Spanning Tree on a SPARSE graph.
    // Given: n cities and an EXPLICIT edge list (not all pairs like #1584).
    // Goal: minimum-cost spanning tree, or -1 if not all cities can be connected.
    //
    // Why Kruskal's (not Prim's array) for THIS problem:
    //   #1584 (complete graph, E=n²): Prim's array O(n²) — never store edges
    //   #1135 (sparse graph, E=connections.length): Kruskal's O(E log E) — sort given edges
    //
    //   Prim's array here would still cost O(n²) scanning all n² potential pairs,
    //   even though most don't exist — wasteful for sparse input.
    //   Kruskal's only touches the GIVEN edges → O(E log E) where E << n².
    //
    // Why -1 is possible here but not in #1584:
    //   #1584: complete graph — always possible to connect all points.
    //   #1135: sparse — some cities may only appear in one connected component.
    //   A spanning tree needs exactly n-1 edges. If we exhaust all edges with
    //   fewer than n-1 added, some cities are unreachable → return -1.

    public int minimumCost(int n, int[][] connections) {
        // Sort edges by cost ascending — Kruskal's greedy order
        Arrays.sort(connections, (a, b) -> a[2] - b[2]);

        int[] parent = new int[n + 1]; // 1-indexed cities
        int[] rank   = new int[n + 1];
        for (int i = 1; i <= n; i++) parent[i] = i;

        int totalCost = 0, edgesUsed = 0;

        for (int[] conn : connections) {
            int u = conn[0], v = conn[1], cost = conn[2];
            int pu = find(parent, u), pv = find(parent, v);

            if (pu != pv) {                     // different components → safe to add
                union(parent, rank, pu, pv);
                totalCost += cost;
                if (++edgesUsed == n - 1)
                    return totalCost;           // MST complete — early exit
            }
            // same component → skip (would create a cycle)
        }

        // Fewer than n-1 edges added → graph is disconnected
        return -1;
    }

    // find with path compression — O(α(n)) amortised
    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    // union by rank — keeps tree height O(log n)
    private void union(int[] parent, int[] rank, int x, int y) {
        if      (rank[x] < rank[y]) parent[x] = y;
        else if (rank[x] > rank[y]) parent[y] = x;
        else { parent[y] = x; rank[x]++; }
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(E log E) — sorting E edges dominates; each union/find is O(α(n)) ≈ O(1)
 * Space: O(n) — parent + rank arrays (only n cities, not E edges)
 *
 * Why early exit at edgesUsed == n-1:
 *   A spanning tree on n nodes has EXACTLY n-1 edges. Once we've successfully
 *   added n-1 edges, ALL cities are connected (no more edges needed or useful).
 *   This avoids processing remaining (potentially many) edges unnecessarily.
 *
 * Detecting disconnected graph:
 *   If we process all edges and edgesUsed < n-1, at least two cities have no
 *   path between them — the graph has multiple connected components and no
 *   spanning tree exists. Return -1.
 *
 * This is Kruskal's Minimum Spanning FOREST when -1: if not all cities
 * connected, Kruskal's builds the MST within each connected component
 * (a minimum spanning forest), but we detect the incomplete case and return -1.
 *
 * Trace — n=3, connections=[[1,2,5],[1,3,6],[2,3,1]]
 * -----------------------------------------------------
 * Sorted: [[2,3,1],[1,2,5],[1,3,6]]
 * parent=[_,1,2,3]
 *
 * [2,3,1]: find(2)=2, find(3)=3, diff → union(2,3), cost=1, edgesUsed=1
 * [1,2,5]: find(1)=1, find(2)=2(→root of {2,3}), diff → union(1,2), cost=6, edgesUsed=2
 *   edgesUsed=2 == n-1=2 → return 6 ✓
 *
 * Trace — n=4, connections=[[1,2,3],[3,4,4]]
 * --------------------------------------------
 * Sorted: [[1,2,3],[3,4,4]]
 *
 * [1,2,3]: union(1,2), cost=3, edgesUsed=1
 * [3,4,4]: union(3,4), cost=7, edgesUsed=2
 * All edges exhausted, edgesUsed=2 < n-1=3 → return -1 ✓
 * (Cities {1,2} and {3,4} are disconnected — no spanning tree exists)
 */
