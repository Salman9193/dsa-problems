import java.util.*;

class Solution {

    // Minimum Height Trees = find the CENTRE(S) of a tree.
    //
    // Key insight: the roots that produce minimum-height trees are the centre
    // nodes — the nodes closest to all other nodes. A tree has AT MOST 2 centres.
    //
    // Algorithm: repeatedly prune leaf nodes (degree 1) layer by layer, from the
    // outside inward, until 1 or 2 nodes remain. Those are the centres.
    //
    // This is REVERSE Kahn's topological sort:
    //   Kahn's (DAG):  process nodes with in-degree 0 (sources)
    //   This (tree):   process nodes with degree 1 (leaves in undirected tree)
    //
    // Why at most 2 centres?
    //   Any tree reduces to a path at its core. A path has at most 2 midpoints.
    //   Peeling simultaneously from all sides converges to the middle:
    //     1 node if the "diameter path" has odd number of nodes (even length)
    //     2 nodes if the "diameter path" has even number of nodes (odd length)
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        // Base case: single node — it is its own centre
        if (n == 1) return Collections.singletonList(0);

        // Build adjacency using HashSet for O(1) removal of leaf edges
        List<Set<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new HashSet<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // Seed: all initial leaves (nodes with exactly 1 neighbour)
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (adj.get(i).size() == 1) leaves.add(i);

        int remaining = n;

        // Peel leaves layer by layer until ≤ 2 nodes remain
        while (remaining > 2) {
            remaining -= leaves.size(); // shrink count before processing
            List<Integer> newLeaves = new ArrayList<>();

            for (int leaf : leaves) {
                // Each leaf has exactly one remaining neighbour
                int neighbour = adj.get(leaf).iterator().next();
                adj.get(neighbour).remove(leaf); // remove leaf from neighbour's adj

                // If neighbour now has degree 1, it becomes the next leaf
                if (adj.get(neighbour).size() == 1)
                    newLeaves.add(neighbour);
            }

            leaves = newLeaves;
        }

        // remaining ≤ 2: leaves contains the centre(s) = MHT roots
        return leaves;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — each node and edge processed exactly once across all iterations
 * Space: O(n) — adjacency sets + leaves list
 *
 * Why HashSet for adjacency (not ArrayList)?
 *   We need to REMOVE edges as leaves are peeled. HashSet gives O(1) removal.
 *   ArrayList gives O(degree) removal — acceptable here since each edge is
 *   removed exactly once (total O(n) removals), but Set makes intent clearer.
 *
 * Why "remaining > 2" (not > 1)?
 *   When exactly 2 nodes remain, BOTH are valid centres (the two midpoints
 *   of the diameter path). Continuing would remove one incorrectly.
 *   n=2: initial leaves=[0,1], remaining=2, loop never executes → return [0,1] ✓
 *
 * Why subtract leaves.size() BEFORE processing?
 *   We count how many nodes remain AFTER removing this leaf layer.
 *   remaining -= leaves.size() gives the count BEFORE we determine newLeaves.
 *   This correctly gates the while loop.
 *
 * Connection to Kahn's Algorithm:
 *   Kahn's (directed DAG): start from in-degree 0 nodes, decrement neighbours,
 *   enqueue when in-degree reaches 0 → produces topological order.
 *   This (undirected tree): start from degree-1 nodes, decrement neighbours,
 *   enqueue when degree reaches 1 → peels to tree centre.
 *   Both are BFS-based layer-by-layer processing. Kahn's works inward from
 *   sources; this works inward from leaves. Same pattern, dual direction.
 *
 * Why leaf nodes can never be optimal roots (except n≤2):
 *   A leaf node has all other nodes on one side. Rooting at a leaf pushes the
 *   height to at least the diameter of the tree. The centre minimises the
 *   maximum distance to any node — by definition, it cannot be a leaf.
 *
 * Trace — n=6, edges=[[3,0],[3,1],[3,2],[3,4],[4,5]]
 * ----------------------------------------------------
 * adj: 0:{3}, 1:{3}, 2:{3}, 3:{0,1,2,4}, 4:{3,5}, 5:{4}
 * Initial leaves: [0,1,2,5], remaining=6
 *
 * Round 1: remaining = 6-4 = 2
 *   leaf 0: neighbour=3, adj[3]={1,2,4} (removed 0), size=3≠1
 *   leaf 1: neighbour=3, adj[3]={2,4},   size=2≠1
 *   leaf 2: neighbour=3, adj[3]={4},     size=1 → newLeaves.add(3)
 *   leaf 5: neighbour=4, adj[4]={3},     size=1 → newLeaves.add(4)
 *   leaves = [3,4]
 *
 * remaining=2 ≤ 2 → exit loop
 * return [3,4] ✓
 *
 * Trace — n=4, edges=[[1,0],[1,2],[1,3]]
 * ----------------------------------------
 * adj: 0:{1}, 1:{0,2,3}, 2:{1}, 3:{1}
 * Initial leaves: [0,2,3], remaining=4
 *
 * Round 1: remaining = 4-3 = 1
 *   leaf 0: neighbour=1, adj[1]={2,3}, size=2≠1
 *   leaf 2: neighbour=1, adj[1]={3},   size=1 → newLeaves.add(1)
 *   leaf 3: neighbour=1, adj[1]={},    size=0≠1 (already added 1)
 *   leaves = [1]
 *
 * remaining=1 ≤ 2 → exit loop
 * return [1] ✓
 */
