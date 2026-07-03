import java.util.*;

class Solution {

    // Possible Bipartition = Is Graph Bipartite? (#785) with one extra step:
    // build the graph from the dislikes edge list, then check bipartiteness.
    //
    // "Can we split people into 2 groups where no two disliking people share a group?"
    // = "Is the dislike graph bipartite?"
    //
    // A graph is bipartite iff it can be 2-coloured iff it has no odd-length cycles.
    // Three approaches — all correct.

    // ── Approach 1: BFS 2-Colouring (PREFERRED) ──────────────────────────────
    //
    // Assign alternating colours (+1/-1) level by level via BFS.
    // If two adjacent nodes have the same colour → odd cycle → not bipartite.
    // Process all components (disconnected groups may exist).
    public boolean possibleBipartition(int n, int[][] dislikes) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] d : dislikes) {
            adj.get(d[0]).add(d[1]);
            adj.get(d[1]).add(d[0]); // undirected — dislike is mutual
        }

        int[] colour = new int[n + 1]; // 0=uncoloured, 1=group1, -1=group2

        for (int i = 1; i <= n; i++) {
            if (colour[i] != 0) continue; // already processed in a previous component
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(i);
            colour[i] = 1;

            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int next : adj.get(node)) {
                    if (colour[next] == 0) {
                        colour[next] = -colour[node]; // assign opposite group
                        queue.offer(next);
                    } else if (colour[next] == colour[node]) {
                        return false; // same group conflict → not bipartite
                    }
                }
            }
        }
        return true;
    }

    // ── Approach 2: DFS 2-Colouring ───────────────────────────────────────────
    //
    // Same logic as BFS but recursive. Each call assigns colour c to node, then
    // recurses with -c for all neighbours. Contradiction = same colour neighbour.
    public boolean possibleBipartitionDFS(int n, int[][] dislikes) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] d : dislikes) {
            adj.get(d[0]).add(d[1]);
            adj.get(d[1]).add(d[0]);
        }

        int[] colour = new int[n + 1];

        for (int i = 1; i <= n; i++)
            if (colour[i] == 0 && !dfs(adj, colour, i, 1))
                return false;

        return true;
    }

    private boolean dfs(List<List<Integer>> adj, int[] colour, int node, int c) {
        colour[node] = c;
        for (int next : adj.get(node)) {
            if (colour[next] == c) return false;           // same colour → conflict
            if (colour[next] == 0 && !dfs(adj, colour, next, -c)) return false;
        }
        return true;
    }

    // ── Approach 3: Union-Find ─────────────────────────────────────────────────
    //
    // All neighbours of node u must be in the OPPOSITE group from u.
    // Therefore all neighbours of u are in the SAME group as each other.
    // Union all of u's neighbours together. If u itself ends up in the same
    // component as any of its neighbours → contradiction → not bipartite.
    //
    // When to prefer over BFS/DFS:
    //   - Edges arrive dynamically (online stream of dislikes)
    //     BFS/DFS would need to re-run from scratch; Union-Find handles each
    //     new edge in O(α(n)) without rebuilding the full graph structure.
    //   - Only connectivity queries needed after building
    public boolean possibleBipartitionUF(int n, int[][] dislikes) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] d : dislikes) {
            adj.get(d[0]).add(d[1]);
            adj.get(d[1]).add(d[0]);
        }

        int[] parent = new int[n + 1];
        int[] rank   = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;

        for (int u = 1; u <= n; u++) {
            List<Integer> neighbours = adj.get(u);
            if (neighbours.isEmpty()) continue;

            int first = neighbours.get(0);
            for (int v : neighbours) {
                // u and v dislike each other → must be in opposite groups
                // If u is already in the same component as v → contradiction
                if (find(parent, u) == find(parent, v)) return false;
                // Union all neighbours of u together (they're all in the same opposite group)
                union(parent, rank, first, v);
            }
        }
        return true;
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
 * BFS/DFS:     Time O(V+E), Space O(V+E)
 * Union-Find:  Time O(E α(V)) ≈ O(E), Space O(V)
 *
 * Relation to #785 Is Graph Bipartite?
 *   #785: adjacency list given directly → check bipartiteness immediately
 *   #886: edge list (dislikes) given → build adjacency list first, then check
 *   The bipartite check is identical. #886 adds only the graph-build step.
 *
 * Why Union-Find works for bipartite checking:
 *   In a bipartite graph, all neighbours of a node u belong to the OPPOSITE
 *   partition from u. This means all of u's neighbours belong to the SAME
 *   partition as each other. Unioning them is therefore valid. If u itself
 *   ever lands in the same component as one of its neighbours, u is in the
 *   same partition as a node it should be opposite to → not bipartite.
 *
 * Trace — n=4, dislikes=[[1,2],[1,3],[2,4]]
 * -------------------------------------------
 * BFS:
 *   Start node 1, colour=1. Neighbours {2,3} → colour=-1, enqueue
 *   Process 2: neighbours {1(done),4} → colour[4]=1, enqueue
 *   Process 3: neighbours {1(done)} → no action
 *   Process 4: neighbours {2(done)} → colour[2]=-1, consistent ✓
 *   Groups: {1,4}=colour1, {2,3}=colour-1 → return true ✓
 *
 * Trace — n=3, dislikes=[[1,2],[1,3],[2,3]]
 * -------------------------------------------
 * BFS:
 *   Start node 1, colour=1. Neighbours {2,3} → both colour=-1
 *   Process 2: neighbour 3 has colour=-1 == colour[2]=-1 → CONFLICT → return false ✓
 */
