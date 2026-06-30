import java.util.*;

class Solution {

    // The canonical Dijkstra's algorithm problem: single-source shortest
    // path on a weighted DIRECTED graph with non-negative edge weights.
    //
    // Answer = max over all nodes of (shortest path from k to that node).
    // This is the time for the LAST node to receive the signal.
    // If any node is unreachable, return -1.

    // ── Approach 1: Dijkstra's with Priority Queue (preferred) ───────────────
    //
    // Why a priority queue and NOT plain BFS?
    //   BFS processes nodes in insertion order — correct ONLY for unweighted
    //   graphs (or all-equal-weight edges). Here edges have different weights,
    //   so a node reached via a longer path first could incorrectly "finalize"
    //   before a shorter path to it is discovered. The priority queue always
    //   processes the CURRENTLY closest unvisited node next, which is what
    //   guarantees Dijkstra's greedy correctness (provided no negative weights).
    public int networkDelayTime(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] t : times)
            adj.computeIfAbsent(t[0], x -> new ArrayList<>()).add(new int[]{t[1], t[2]});

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // {node, dist}
        pq.offer(new int[]{k, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int node = curr[0], d = curr[1];

            // Lazy deletion: this entry is stale if a shorter path to 'node'
            // was already found and processed. Skip without erroring —
            // avoids implementing a decrease-key operation on the heap.
            if (d > dist[node]) continue;

            for (int[] edge : adj.getOrDefault(node, Collections.emptyList())) {
                int next = edge[0], weight = edge[1];
                if (dist[node] + weight < dist[next]) {
                    dist[next] = dist[node] + weight;
                    pq.offer(new int[]{next, dist[next]});
                }
            }
        }

        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1; // node unreachable from k
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }

    // ── Approach 2: Bellman-Ford (handles negative weights) ───────────────────
    //
    // Relax ALL edges n-1 times. Guaranteed correct even with negative
    // edge weights (unlike Dijkstra's, whose greedy choice assumes weights
    // are non-negative — a negative edge could invalidate an already-finalised
    // shortest distance).
    //
    // Trade-off: O(V·E) — much slower than Dijkstra's O(E log V) for large
    // graphs, but necessary if negative weights are possible.
    public int networkDelayTimeBellmanFord(int[][] times, int n, int k) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        // Relax all edges n-1 times — guarantees shortest paths are found
        // since the longest possible simple path has at most n-1 edges
        for (int i = 0; i < n - 1; i++)
            for (int[] t : times)
                if (dist[t[0]] != Integer.MAX_VALUE && dist[t[0]] + t[2] < dist[t[1]])
                    dist[t[1]] = dist[t[0]] + t[2];

        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            maxDist = Math.max(maxDist, dist[i]);
        }
        return maxDist;
    }
}

/*
 * Complexity
 * ----------
 * Dijkstra's (priority queue): Time O(E log V), Space O(V + E)
 *   Each edge relaxation potentially adds a heap entry → O(log V) per op.
 *   Total heap operations bounded by O(E), giving O(E log V) overall.
 *
 * Bellman-Ford: Time O(V·E), Space O(V)
 *   n-1 full passes over all edges — much slower, but handles negative weights.
 *
 * When to choose which:
 *   Dijkstra's: all weights non-negative (network delay times are always ≥ 0
 *               in this problem — Dijkstra's is the right choice)
 *   Bellman-Ford: weights can be negative, or you need to DETECT negative
 *               cycles (Dijkstra's cannot detect these at all)
 *
 * Why "if (d > dist[node]) continue" matters:
 *   A node can be pushed to the priority queue multiple times — once per
 *   relaxation that improves its distance. By the time an OLDER (larger)
 *   queue entry for that node is popped, a newer, smaller distance may
 *   already be recorded in dist[]. This check discards the stale entry
 *   in O(1) rather than maintaining a separate "removed" set or using a
 *   more complex indexed priority queue with decrease-key support.
 *
 * Trace — times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2
 * ----------------------------------------------------
 * adj: 2 -> [(1,1),(3,1)], 3 -> [(4,1)]
 * dist = [_, INF, 0, INF, INF]  (index 0 unused)
 *
 * pq=[(2,0)]
 * Pop (2,0): relax 1 -> dist[1]=1, enqueue; relax 3 -> dist[3]=1, enqueue
 * pq=[(1,1),(3,1)]
 *
 * Pop (1,1): node 1 has no outgoing edges
 * Pop (3,1): relax 4 -> dist[4]=2, enqueue
 * pq=[(4,2)]
 *
 * Pop (4,2): node 4 has no outgoing edges
 *
 * dist = [_, 1, 0, 1, 2]
 * max(1, 0, 1, 2) = 2 ✓
 */
