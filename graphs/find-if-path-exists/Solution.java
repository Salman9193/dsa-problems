import java.util.*;

class Solution {

    // Three approaches — all O(V+E) time. Choose based on context:
    //   BFS:         best when shortest path also needed; level-by-level exploration
    //   DFS:         best for deep graphs; simplest recursive form
    //   Union-Find:  best when multiple connectivity queries on the same graph

    // ── Approach 1: BFS ────────────────────────────────────────────────────
    public boolean validPathBFS(int n, int[][] edges, int source, int destination) {
        if (source == destination) return true;

        List<List<Integer>> adj = buildAdj(n, edges);
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbour : adj.get(node)) {
                if (neighbour == destination) return true;
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.offer(neighbour);
                }
            }
        }
        return false;
    }

    // ── Approach 2: DFS ────────────────────────────────────────────────────
    public boolean validPathDFS(int n, int[][] edges, int source, int destination) {
        if (source == destination) return true;
        List<List<Integer>> adj = buildAdj(n, edges);
        boolean[] visited = new boolean[n];
        return dfs(adj, visited, source, destination);
    }

    private boolean dfs(List<List<Integer>> adj, boolean[] visited, int node, int dest) {
        if (node == dest) return true;
        visited[node] = true;
        for (int neighbour : adj.get(node))
            if (!visited[neighbour] && dfs(adj, visited, neighbour, dest))
                return true;
        return false;
    }

    // ── Approach 3: Union-Find (canonical solution) ────────────────────────
    //
    // Key insight: connectivity is a symmetric, transitive relation — exactly
    // what Union-Find tracks. Union all edges, then check if source and
    // destination share the same root (same connected component).
    //
    // Advantage over BFS/DFS: after O(E α(E)) build, each connectivity query
    // is O(α(n)) ≈ O(1). For multiple queries on the same graph, this wins.
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        int[] parent = new int[n];
        int[] rank   = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        for (int[] e : edges) union(parent, rank, e[0], e[1]);

        // Same root = same connected component = path exists
        return find(parent, source) == find(parent, destination);
    }

    // find with path compression: every node on the path to root points directly
    // to the root after the call → amortised O(α(n)) per operation
    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    // union by rank: attach shorter tree under taller tree to keep height O(log n)
    private void union(int[] parent, int[] rank, int x, int y) {
        int px = find(parent, x), py = find(parent, y);
        if (px == py) return;
        if      (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }
    }

    private List<List<Integer>> buildAdj(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }
        return adj;
    }
}

/*
 * Complexity
 * ----------
 * BFS/DFS:     Time O(V+E), Space O(V+E) for adjacency list + visited array
 * Union-Find:  Build O(E α(E)), Query O(α(n)) ≈ O(1), Space O(V)
 *
 * When to choose which:
 *   BFS:        Also need shortest path; graph is wide (many branches per node)
 *   DFS:        Deep graph; simple recursive reasoning is clearest
 *   Union-Find: Multiple connectivity queries on same graph; incremental edges
 *
 * Correctness of Union-Find approach:
 *   Graph connectivity is an equivalence relation (reflexive, symmetric, transitive).
 *   Union-Find maintains a partition into equivalence classes.
 *   After union-ing all edges, find(source) == find(destination) iff source and
 *   destination are in the same equivalence class = same connected component = path exists.
 *
 * Trace — n=6, edges=[[0,1],[0,2],[3,5],[5,4],[4,3]], source=0, dest=5
 * -----------------------------------------------------------------------
 * Union-Find after processing edges:
 *   union(0,1): component {0,1}
 *   union(0,2): component {0,1,2}
 *   union(3,5): component {3,5}
 *   union(5,4): component {3,4,5}
 *   union(4,3): already same root → no-op
 *
 * find(0) = root of {0,1,2}
 * find(5) = root of {3,4,5}
 * Different roots → return false ✓
 *
 * Trace — n=3, edges=[[0,1],[1,2],[2,0]], source=0, dest=2
 * ----------------------------------------------------------
 * All edges union into one component {0,1,2}.
 * find(0) == find(2) → return true ✓
 */
