import java.util.*;

class Solution {

    // Redundant Connection = THE canonical Union-Find problem.
    //
    // Key insight: a tree with n nodes has exactly n-1 edges and no cycles.
    // The input has exactly n edges (one extra) — guaranteeing exactly one cycle.
    // The redundant edge is the FIRST edge (in input order) that connects two
    // nodes ALREADY in the same component.
    //
    // Why DSU is the natural fit (not BFS/DFS):
    //   We need to repeatedly ask "are u and v already connected?" while
    //   incrementally building the graph edge by edge.
    //   BFS/DFS would require re-searching the whole graph for a path
    //   before adding each edge → O(n) per edge → O(n²) total.
    //   DSU answers "same component?" in O(α(n)) via find() — O(n α(n)) total.
    //
    // Why processing left-to-right gives the LAST valid answer:
    //   Every edge before the redundant one successfully unions two different
    //   components (no cycle). The moment we hit an edge connecting an
    //   already-unified pair, THAT edge is provably the one whose removal
    //   restores a valid tree — and it's the one that occurs latest among
    //   all cycle-causing edges, because all earlier edges were cycle-free.
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1]; // 1-indexed nodes
        int[] rank   = new int[n + 1];
        for (int i = 0; i <= n; i++) parent[i] = i;

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            int pu = find(parent, u), pv = find(parent, v);

            if (pu == pv) return edge; // already connected → this edge creates the cycle

            union(parent, rank, pu, pv);
        }

        return new int[0]; // unreachable per problem constraints (exactly one redundant edge exists)
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
 * Time:  O(n α(n)) — n edges, each find+union is O(α(n)) amortised
 * Space: O(n) — parent + rank arrays
 *
 * Why DSU beats BFS/DFS for this specific problem:
 *
 *   BFS/DFS approach: for each edge (u,v), before adding it, search the
 *   current graph for an existing path from u to v. If found, this edge
 *   is redundant. Each search is O(V+E) = O(n). Total: O(n²).
 *
 *   DSU approach: find(u) and find(v) directly answer "same component?"
 *   in O(α(n)) — no graph traversal needed. Total: O(n α(n)).
 *
 *   For n up to 1000 (typical constraint), O(n²)=10^6 vs O(n α(n))≈5000 —
 *   DSU is ~200x faster in practice.
 *
 * Why this is "the canonical Union-Find problem":
 *   Unlike other DSU applications (Number of Provinces, Number of Islands)
 *   which COULD also use BFS/DFS with similar complexity, this problem's
 *   incremental nature (add edges one at a time, query connectivity each
 *   time) is EXACTLY what DSU was designed for. There is no simpler
 *   alternative — DSU is not just an option here, it's the natural model.
 *
 * Trace — edges=[[1,2],[1,3],[2,3]]
 * ------------------------------------
 * parent = [0,1,2,3]  (index 0 unused, 1-indexed)
 *
 * Edge [1,2]: find(1)=1, find(2)=2, different → union(1,2)
 *   parent = [0,1,1,3]
 * Edge [1,3]: find(1)=1, find(3)=3, different → union(1,3)
 *   parent = [0,1,1,1]
 * Edge [2,3]: find(2)=1, find(3)=1, SAME → return [2,3] ✓
 *
 * Trace — edges=[[1,2],[2,3],[3,4],[1,4],[1,5]]
 * -------------------------------------------------
 * Edge [1,2]: union(1,2) → {1,2}
 * Edge [2,3]: union(2,3) → {1,2,3}
 * Edge [3,4]: union(3,4) → {1,2,3,4}
 * Edge [1,4]: find(1)==find(4) → SAME → return [1,4] ✓
 * (Edge [1,5] never processed since we return early)
 */
