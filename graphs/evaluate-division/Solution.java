import java.util.*;

class Solution {

    // Approach 1: BFS per query — O(E) build + O(V+E) per query
    //
    // Model: each equation a/b = k becomes two directed edges:
    //   a → b  with weight k
    //   b → a  with weight 1/k
    //
    // Query a/c = product of edge weights along path a→...→c
    // BFS finds any path; edge weights multiply along the way.
    //
    // Alternative: Weighted Union-Find (see below) — O(α(n)) per query after build.
    public double[] calcEquation(List<List<String>> equations,
                                  double[] values,
                                  List<List<String>> queries) {
        // Build adjacency list: node → {neighbour → weight}
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String u = equations.get(i).get(0);
            String v = equations.get(i).get(1);
            double w = values[i];
            graph.computeIfAbsent(u, k -> new HashMap<>()).put(v, w);
            graph.computeIfAbsent(v, k -> new HashMap<>()).put(u, 1.0 / w);
        }

        double[] result = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String src = queries.get(i).get(0);
            String dst = queries.get(i).get(1);
            result[i] = bfs(graph, src, dst);
        }
        return result;
    }

    // BFS: track cumulative product from src to each visited node
    private double bfs(Map<String, Map<String, Double>> graph,
                       String src, String dst) {
        if (!graph.containsKey(src) || !graph.containsKey(dst)) return -1.0;
        if (src.equals(dst)) return 1.0;

        Queue<String> queue = new LinkedList<>();
        Map<String, Double> product = new HashMap<>();
        queue.offer(src);
        product.put(src, 1.0);

        while (!queue.isEmpty()) {
            String node = queue.poll();
            for (Map.Entry<String, Double> e : graph.get(node).entrySet()) {
                String next = e.getKey();
                if (product.containsKey(next)) continue;        // already visited
                product.put(next, product.get(node) * e.getValue());
                if (next.equals(dst)) return product.get(next); // found
                queue.offer(next);
            }
        }
        return -1.0;  // no path exists
    }
}

/*
 * Complexity
 * ----------
 * Build graph:   O(E)
 * BFS per query: O(V + E)
 * Total:         O(E + Q(V+E))  where Q = number of queries
 *
 * Alternative — Weighted Union-Find:
 *   parent[x] = root of x's component
 *   ratio[x]  = x / parent[x]   (x's value relative to its root)
 *
 *   find(x):
 *     if parent[x] != x:
 *       root = find(parent[x])
 *       ratio[x] *= ratio[parent[x]]  // path compression accumulates ratios
 *       parent[x] = root
 *     return parent[x]
 *
 *   union(x, y, val):  // x/y = val
 *     rx = find(x), ry = find(y)
 *     parent[rx] = ry
 *     ratio[rx] = ratio[y] * val / ratio[x]
 *
 *   query(x, y):
 *     if find(x) != find(y): return -1.0
 *     return ratio[x] / ratio[y]   // x/root ÷ y/root = x/y
 *
 *   Build: O(E α(E))   Query: O(α(E))   → much better for large Q
 *
 * Key insight — why path product = division result:
 *   a/b = 2, b/c = 3
 *   a/c = (a/b) × (b/c) = 2 × 3 = 6
 *   The intermediate variable b "cancels" — same as algebraic chain rule.
 *   Every division query is a product of a PATH in the weighted graph.
 *
 * Edge cases:
 *   - Variable not in graph       → -1.0
 *   - Query x/x (self-division)   → 1.0
 *   - No path between components  → -1.0
 *   - Direct edge exists          → O(1) lookup in neighbour map
 *
 * Trace — equations=[["a","b"],["b","c"]], values=[2.0,3.0]
 * ----------------------------------------------------------
 * Graph: a→{b:2}, b→{a:0.5,c:3}, c→{b:0.333}
 *
 * Query a/c:
 *   BFS from a: product={a:1.0}
 *   Visit a → b: product[b] = 1.0 × 2.0 = 2.0, enqueue b
 *   Visit b → c: product[c] = 2.0 × 3.0 = 6.0 = dst → return 6.0 ✓
 *
 * Query b/a:
 *   BFS from b: product={b:1.0}
 *   Visit b → a: product[a] = 1.0 × 0.5 = 0.5 = dst → return 0.5 ✓
 *
 * Query x/x (x not in graph):
 *   !graph.containsKey("x") → return -1.0 ✓
 */
