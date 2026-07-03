import java.util.*;

class Solution {

    // Critical Connections in a Network = find all BRIDGES in an undirected graph.
    //
    // A bridge is an edge whose removal disconnects the graph.
    // A critical server connection is exactly a bridge — if it fails, some
    // server(s) can no longer reach the rest of the network.
    //
    // Algorithm: Tarjan's bridge-finding algorithm — O(V+E) single DFS pass.
    //
    // Key data structures:
    //   disc[u] = discovery timestamp of u (when DFS first visits u)
    //   low[u]  = minimum discovery time reachable from u's subtree via back edges
    //
    // Bridge condition: low[v] > disc[u]
    //   v's subtree has NO back edge that reaches u or any ancestor of u.
    //   Therefore edge (u,v) is the ONLY connection → removing it disconnects.
    //
    // NOT a bridge if low[v] <= disc[u]:
    //   v can reach back to u or above → alternative path exists → not a bridge.

    int timer = 0;

    public List<List<Integer>> criticalConnections(int n,
            List<List<Integer>> connections) {
        // Build adjacency list
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (List<Integer> conn : connections) {
            adj.get(conn.get(0)).add(conn.get(1));
            adj.get(conn.get(1)).add(conn.get(0));
        }

        int[] disc    = new int[n];
        int[] low     = new int[n];
        boolean[] visited = new boolean[n];
        List<List<Integer>> bridges = new ArrayList<>();

        // Single DFS pass — problem guarantees connected graph so one call suffices
        dfs(0, -1, disc, low, visited, adj, bridges);
        return bridges;
    }

    private void dfs(int u, int parent, int[] disc, int[] low,
                     boolean[] visited, List<List<Integer>> adj,
                     List<List<Integer>> bridges) {
        visited[u] = true;
        disc[u] = low[u] = timer++;

        for (int v : adj.get(u)) {
            if (!visited[v]) {
                // Tree edge: recurse into child
                dfs(v, u, disc, low, visited, adj, bridges);

                // After returning: propagate low value up
                low[u] = Math.min(low[u], low[v]);

                // Bridge condition: v's subtree cannot reach u or above
                if (low[v] > disc[u])
                    bridges.add(Arrays.asList(u, v));

            } else if (v != parent) {
                // Back edge to an already-visited ancestor (not the edge we came from)
                // Update low[u] with disc[v] — direct back-edge reach
                // IMPORTANT: use disc[v], NOT low[v]
                //   disc[v]: how far back v was discovered (direct reachability)
                //   Using low[v] here would over-count paths (v's subtree paths
                //   don't help u directly)
                low[u] = Math.min(low[u], disc[v]);
            }
            // If v == parent: skip (the undirected edge we traversed to get here)
        }
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(V+E) — single DFS pass; each edge visited twice (undirected)
 * Space: O(V+E) — adjacency list + disc/low/visited arrays + recursion stack
 *
 * The parent edge skip (v != parent):
 *   In an undirected graph, edge (u,v) appears as both u→v and v→u.
 *   When processing u's neighbours and encountering v=parent (the node we
 *   came from), we must NOT update low[u] using this edge — that would make
 *   every edge appear to be on a cycle. Skip it: it's the same tree edge.
 *
 * Multi-edge bug:
 *   If there are 2 edges between u and v, `v != parent` is wrong — one
 *   should be treated as a back edge (alternative path exists). Fix: track
 *   edge indices instead of parent node. For LeetCode #1192, no multi-edges
 *   exist so parent-node skip is sufficient.
 *
 * Bridge (low[v] > disc[u]) vs Articulation Point (low[v] >= disc[u]):
 *   Bridge uses STRICT > because:
 *     low[v] == disc[u] means v can reach u via back edge.
 *     Removing edge (u,v): u still reachable from v → NOT a bridge.
 *     Removing vertex u: back edge from v to u is gone → v disconnected → AP.
 *   This one-character difference (> vs >=) is the #1 source of bugs.
 *
 * Trace — n=4, connections=[[0,1],[1,2],[2,0],[1,3]]
 * ---------------------------------------------------
 * adj: 0→{1,2}, 1→{0,2,3}, 2→{1,0}, 3→{1}
 *
 * dfs(0, parent=-1):
 *   disc[0]=0, low[0]=0
 *
 *   dfs(1, parent=0):
 *     disc[1]=1, low[1]=1
 *
 *     dfs(2, parent=1):
 *       disc[2]=2, low[2]=2
 *       Neighbour 1: already visited, v==parent → skip
 *       Neighbour 0: already visited, v≠parent → low[2]=min(2,disc[0])=0
 *       low[2]=0
 *     ← back to 1: low[1]=min(1,low[2])=0
 *     low[2]=0 NOT > disc[1]=1 → [1,2] not a bridge ✓
 *
 *     dfs(3, parent=1):
 *       disc[3]=3, low[3]=3
 *       Neighbour 1: already visited, v==parent → skip
 *       low[3]=3
 *     ← back to 1: low[1]=min(0,low[3])=0
 *     low[3]=3 > disc[1]=1 → [1,3] IS A BRIDGE ✓
 *
 *   Neighbour 2: already visited, v≠parent → low[0]=min(0,disc[2])=0
 *
 * Result: [[1,3]] ✓
 */
