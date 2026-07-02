import java.util.*;

class Solution {

    // Cheapest Flights Within K Stops = constrained shortest path:
    // find cheapest path from src to dst using at most k+1 edges (k stops).
    //
    // WHY NOT STANDARD DIJKSTRA:
    //   Dijkstra finds the globally cheapest path regardless of hop count.
    //   A path with fewer hops but higher cost could be the correct answer
    //   when the globally cheapest path exceeds k stops. Dijkstra has no
    //   mechanism to track or enforce a hop-count constraint.
    //
    // WHY NOT PLAIN BFS:
    //   BFS finds the path with fewest hops (unweighted). It cannot compare
    //   COSTS between paths — it treats all edges as weight 1. For weighted
    //   graphs with a hop limit, BFS gives wrong answers (see NOTES.md).
    //
    // THE RIGHT TOOL: K-Limited Bellman-Ford.
    //   Each of k+1 passes relaxes all edges, adding exactly one more hop.
    //   After k+1 passes: dist[dst] = cheapest path using ≤ k+1 edges.
    //   This is optimal — Kociumaka & Polak (ESA 2023) proved Bellman-Ford
    //   is asymptotically optimal for hop-bounded shortest path problems.

    // ── Approach 1: K-Limited Bellman-Ford (PREFERRED) ────────────────────
    //
    // Key: use a SNAPSHOT of dist[] before each pass (temp array).
    // Without snapshot: a relaxation within the same pass can chain —
    //   dist[a] updated → immediately used to update dist[b] in same pass
    //   = 2 edges consumed in one "pass" → wrong hop count tracking.
    // With snapshot: each pass uses ONLY distances from the PREVIOUS pass,
    //   ensuring exactly one additional edge is added per pass.
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 0; i <= k; i++) {            // k+1 passes = k stops + 1 flight
            int[] temp = Arrays.copyOf(dist, n);  // snapshot BEFORE this pass

            for (int[] flight : flights) {
                int from = flight[0], to = flight[1], price = flight[2];
                // Only relax from dist[] (previous pass), write to temp[] (this pass)
                if (dist[from] != Integer.MAX_VALUE
                    && dist[from] + price < temp[to]) {
                    temp[to] = dist[from] + price;
                }
            }
            dist = temp;
        }

        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
    }

    // ── Approach 2: Modified Dijkstra with State (node, stops) ────────────
    //
    // Dijkstra CAN handle hop-constrained paths if we extend the state to
    // include the number of stops used. Two paths to the same node with
    // different stop counts are DIFFERENT states — Dijkstra processes each
    // independently, enforcing the hop limit naturally.
    //
    // Why less preferred than Bellman-Ford:
    //   - Requires 2D dist array: dist[node][stops] → O(n×k) space
    //   - More complex: need to track (price, node, stops) in queue
    //   - Bellman-Ford is O(k×E) vs Dijkstra's O(k×E×log(kE)) here
    //   - Bellman-Ford is proven OPTIMAL for this problem (ESA 2023)
    public int findCheapestPriceDijkstra(int n, int[][] flights, int src, int dst, int k) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int[] f : flights)
            adj.computeIfAbsent(f[0], x -> new ArrayList<>()).add(new int[]{f[1], f[2]});

        // dist[node][stops] = cheapest price to reach node using exactly 'stops' edges
        int[][] dist = new int[n][k + 2];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[src][0] = 0;

        // {price, node, stops_used}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, src, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int price = curr[0], node = curr[1], stops = curr[2];

            if (node == dst) return price;   // first pop of dst = optimal for this stop count
            if (stops > k) continue;         // exceeded stop limit

            for (int[] next : adj.getOrDefault(node, Collections.emptyList())) {
                int newPrice = price + next[1];
                if (newPrice < dist[next[0]][stops + 1]) {
                    dist[next[0]][stops + 1] = newPrice;
                    pq.offer(new int[]{newPrice, next[0], stops + 1});
                }
            }
        }

        return -1;
    }
}

/*
 * Complexity
 * ----------
 * K-Limited Bellman-Ford:   Time O(k×E),         Space O(V)
 * Modified Dijkstra:         Time O(k×E log(kE)), Space O(k×V)
 *
 * Why Bellman-Ford is preferred:
 *   1. Less space: O(V) vs O(k×V)
 *   2. Less complexity: no priority queue, simpler code
 *   3. Optimal: Kociumaka & Polak (ESA 2023) proved Bellman-Ford is
 *      asymptotically optimal for hop-bounded path problems (additive weights)
 *   4. The temp snapshot elegantly enforces edge count — no extra state needed
 *
 * Why BFS is WRONG for this problem:
 *   BFS tracks hop count, not cost. At BFS level 1 (1 stop), it might reach
 *   the destination via a direct expensive flight (cost 500) while missing
 *   a connecting path (cost 200) that also has 1 stop. BFS has no way to
 *   compare costs — it treats all edges as weight 1.
 *   For weighted graphs with a hop limit: Bellman-Ford > BFS.
 *
 * What happens WITHOUT the temp snapshot (why it's critical):
 *   Pass 1, processing flight [0→1, price=100]:
 *     dist[1] = 100 (updated)
 *   Pass 1, processing flight [1→2, price=100]:
 *     Without temp: dist[1] = 100 (just updated!) → dist[2] = 200
 *     This used 2 edges in 1 pass — violates k=0 stops constraint!
 *   With temp: dist[1] was INF at start of pass → flight [1→2] not relaxed
 *   Correct: node 2 only reachable in pass 2 (k=1 stop)
 *
 * Trace — flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]]
 *         src=0, dst=3, k=1
 * ------------------------------------------------------------------
 * Initial: dist=[0, INF, INF, INF]
 *
 * Pass 1 (i=0): temp=[0, INF, INF, INF]
 *   [0,1,100]: dist[0]=0 → temp[1]=100
 *   [1,2,100]: dist[1]=INF → skip (uses dist not temp!)
 *   [2,0,100]: dist[2]=INF → skip
 *   [1,3,600]: dist[1]=INF → skip
 *   [2,3,200]: dist[2]=INF → skip
 *   dist = [0, 100, INF, INF]
 *
 * Pass 2 (i=1): temp=[0, 100, INF, INF]
 *   [0,1,100]: 0+100=100 not < temp[1]=100 → skip
 *   [1,2,100]: dist[1]=100 → temp[2]=200
 *   [2,0,100]: dist[2]=INF → skip
 *   [1,3,600]: dist[1]=100 → temp[3]=700
 *   [2,3,200]: dist[2]=INF → skip
 *   dist = [0, 100, 200, 700]
 *
 * dist[3] = 700 ✓  (path: 0→1→3, cost 100+600=700, 1 stop)
 */
