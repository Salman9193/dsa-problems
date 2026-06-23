# Graph Algorithms — Topological Sort & Shortest Paths

The two most commonly tested advanced graph algorithms at Google Staff level:
topological sort (for dependency graphs) and Dijkstra's algorithm (for
weighted shortest paths).

---

## Part 1: Topological Sort

A topological ordering of a directed graph is a linear ordering of nodes
such that for every directed edge u→v, u comes before v.
Only possible for **Directed Acyclic Graphs (DAGs)**.

### When to use
- Dependency resolution (build systems, package managers)
- Course prerequisite scheduling
- Task ordering with dependencies

---

### Algorithm 1: Kahn's Algorithm (BFS-based)

Repeatedly remove nodes with in-degree 0 (no prerequisites).

```java
public int[] topoSort(int n, int[][] edges) {
    int[] inDegree = new int[n];
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

    for (int[] e : edges) {
        adj.get(e[0]).add(e[1]);
        inDegree[e[1]]++;
    }

    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < n; i++)
        if (inDegree[i] == 0) queue.offer(i);

    int[] order = new int[n];
    int idx = 0;
    while (!queue.isEmpty()) {
        int node = queue.poll();
        order[idx++] = node;
        for (int next : adj.get(node))
            if (--inDegree[next] == 0) queue.offer(next);
    }

    return idx == n ? order : new int[]{};  // empty if cycle detected
}
```

**Cycle detection:** If `idx < n` after processing, there's a cycle — some
nodes never reached in-degree 0.

---

### Algorithm 2: DFS-based Topological Sort

Post-order DFS: add node to stack AFTER visiting all its descendants.
Reverse the stack for topological order.

```java
public int[] topoSortDFS(int n, int[][] edges) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    for (int[] e : edges) adj.get(e[0]).add(e[1]);

    int[] colour = new int[n]; // 0=white, 1=grey(in progress), 2=black(done)
    Deque<Integer> stack = new ArrayDeque<>();
    boolean[] hasCycle = {false};

    for (int i = 0; i < n; i++)
        if (colour[i] == 0) dfs(i, adj, colour, stack, hasCycle);

    if (hasCycle[0]) return new int[]{};
    int[] order = new int[n];
    for (int i = 0; i < n; i++) order[i] = stack.pop();
    return order;
}

private void dfs(int node, List<List<Integer>> adj, int[] colour,
                 Deque<Integer> stack, boolean[] hasCycle) {
    colour[node] = 1;  // grey: currently visiting
    for (int next : adj.get(node)) {
        if (colour[next] == 1) { hasCycle[0] = true; return; }  // back edge = cycle
        if (colour[next] == 0) dfs(next, adj, colour, stack, hasCycle);
    }
    colour[node] = 2;  // black: done
    stack.push(node);
}
```

---

### Core LeetCode Problems

#### #207 Course Schedule (Cycle detection only)
```java
public boolean canFinish(int numCourses, int[][] prerequisites) {
    // Kahn's: if topo order has all n nodes → no cycle
    int[] result = topoSort(numCourses, prerequisites);
    return result.length == numCourses;
}
```

#### #210 Course Schedule II (Return the order)
```java
public int[] findOrder(int numCourses, int[][] prerequisites) {
    return topoSort(numCourses, prerequisites);
}
```

#### #269 Alien Dictionary
Build a character ordering graph from the given words, then topological sort.

```java
public String alienOrder(String[] words) {
    Map<Character, Set<Character>> adj = new HashMap<>();
    Map<Character, Integer> inDegree = new HashMap<>();

    // Initialise all characters
    for (String w : words)
        for (char c : w.toCharArray()) {
            adj.putIfAbsent(c, new HashSet<>());
            inDegree.putIfAbsent(c, 0);
        }

    // Build edges from adjacent words
    for (int i = 0; i < words.length - 1; i++) {
        String w1 = words[i], w2 = words[i+1];
        int minLen = Math.min(w1.length(), w2.length());
        boolean found = false;
        for (int j = 0; j < minLen; j++) {
            if (w1.charAt(j) != w2.charAt(j)) {
                if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    inDegree.merge(w2.charAt(j), 1, Integer::sum);
                }
                found = true; break;
            }
        }
        if (!found && w1.length() > w2.length()) return "";  // invalid
    }

    // Kahn's algorithm
    Queue<Character> queue = new LinkedList<>();
    for (char c : inDegree.keySet())
        if (inDegree.get(c) == 0) queue.offer(c);

    StringBuilder sb = new StringBuilder();
    while (!queue.isEmpty()) {
        char c = queue.poll();
        sb.append(c);
        for (char next : adj.get(c)) {
            inDegree.merge(next, -1, Integer::sum);
            if (inDegree.get(next) == 0) queue.offer(next);
        }
    }
    return sb.length() == inDegree.size() ? sb.toString() : "";
}
```

---

## Part 2: Dijkstra's Algorithm

Finds the shortest path from a source to all other nodes in a weighted
graph with **non-negative edge weights**.

### Key Idea
Greedily process nodes in order of current shortest distance.
Use a min-heap (priority queue) to always get the nearest unprocessed node.

```java
public int[] dijkstra(int n, List<List<int[]>> adj, int src) {
    // adj[u] = list of [v, weight]
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    // Min-heap: [distance, node]
    PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[0]-b[0]);
    pq.offer(new int[]{0, src});

    while (!pq.isEmpty()) {
        int[] curr = pq.poll();
        int d = curr[0], u = curr[1];

        if (d > dist[u]) continue;  // stale entry — skip

        for (int[] edge : adj.get(u)) {
            int v = edge[0], w = edge[1];
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                pq.offer(new int[]{dist[v], v});
            }
        }
    }
    return dist;
}
```

**Complexity:** O((V + E) log V) with a binary heap.

---

### Core LeetCode Problems

#### #743 Network Delay Time
Shortest path from node k; return max distance (time for signal to reach all nodes).

```java
public int networkDelayTime(int[][] times, int n, int k) {
    List<List<int[]>> adj = new ArrayList<>();
    for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
    for (int[] t : times) adj.get(t[0]).add(new int[]{t[1], t[2]});

    int[] dist = dijkstra(n + 1, adj, k);
    int max = 0;
    for (int i = 1; i <= n; i++) {
        if (dist[i] == Integer.MAX_VALUE) return -1;
        max = Math.max(max, dist[i]);
    }
    return max;
}
```

#### #787 Cheapest Flights Within K Stops
Dijkstra with state `(cost, node, stops_remaining)`.
Or: Bellman-Ford for k iterations.

```java
public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    // Bellman-Ford: relax all edges k+1 times
    int[] prices = new int[n];
    Arrays.fill(prices, Integer.MAX_VALUE);
    prices[src] = 0;

    for (int i = 0; i <= k; i++) {
        int[] temp = Arrays.copyOf(prices, n);
        for (int[] f : flights) {
            if (prices[f[0]] != Integer.MAX_VALUE)
                temp[f[1]] = Math.min(temp[f[1]], prices[f[0]] + f[2]);
        }
        prices = temp;
    }
    return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
}
```

#### #1091 Shortest Path in Binary Matrix
BFS (unweighted) — Dijkstra is overkill here.

#### #778 Swim in Rising Water
Dijkstra where edge weight = max elevation along path.

---

## Bellman-Ford — When Dijkstra Fails

Use when the graph has **negative edge weights** (but no negative cycles).

```java
public int[] bellmanFord(int n, int[][] edges, int src) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;

    // Relax all edges n-1 times
    for (int i = 0; i < n - 1; i++)
        for (int[] e : edges)
            if (dist[e[0]] != Integer.MAX_VALUE && dist[e[0]] + e[2] < dist[e[1]])
                dist[e[1]] = dist[e[0]] + e[2];

    // Check for negative cycles
    for (int[] e : edges)
        if (dist[e[0]] != Integer.MAX_VALUE && dist[e[0]] + e[2] < dist[e[1]])
            return null;  // negative cycle exists

    return dist;
}
```

**Complexity:** O(V × E) — slower than Dijkstra but handles negatives.

---

## Algorithm Selection Guide

| Graph type | Weights | Algorithm | Complexity |
|-----------|---------|-----------|------------|
| Unweighted | — | BFS | O(V+E) |
| Weighted, non-negative | ≥ 0 | Dijkstra | O((V+E) log V) |
| Weighted, with negatives | Any | Bellman-Ford | O(V × E) |
| All pairs shortest path | Any | Floyd-Warshall | O(V³) |
| DAG shortest path | Any | Topo sort + DP | O(V+E) |

---

## Extensions

| Variant | Approach |
|---------|---------|
| Shortest path with constraints (#787) | Add constraint to state: (dist, node, k_remaining) |
| Bidirectional Dijkstra | Run from both ends; meet in middle — 2× speedup |
| A* search | Add heuristic to Dijkstra; faster when heuristic is good |
| Floyd-Warshall | DP: dist[i][j] = min(dist[i][k] + dist[k][j]) for all k |
| Johnson's algorithm | Re-weight for negative edges, then Dijkstra from each node |
