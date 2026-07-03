# Strongly Connected Components, Bridges & Articulation Points

**Differentiating concepts for Staff/Principal Engineer interview rounds.**
All algorithms run in O(V+E) via DFS.

---

## Part 1 — Strongly Connected Components (SCC)

### Definition

A **Strongly Connected Component** is a maximal subset of vertices in a
**directed** graph where EVERY vertex is reachable from EVERY other vertex.

```
Directed graph: 0→1→2→0, 3→4
SCCs: {0,1,2}  {3}  {4}

Condensed DAG (SCC graph): {0,1,2} → {3} → {4}
The condensed DAG is ALWAYS a DAG (no cycles between SCCs).
```

**Why SCCs matter:** they reveal the maximal "mutually reachable" groups —
circular dependency clusters in compilers, tightly-knit communities in social
networks, deadlock rings in distributed systems.

---

### Algorithm 1: Kosaraju's (Two DFS Passes)

```
Pass 1: DFS on original graph, push each node to stack on FINISH
Pass 2: DFS on REVERSED graph, in reverse-finish order (pop from stack)
        Each DFS tree in pass 2 = one SCC
```

**Why it works:** In the transposed graph, SCCs are the same sets but with
reversed edges between them. The finish-order from pass 1 guarantees that
when we pop and DFS in the transpose, we stay within one SCC (can't escape
to another because inter-SCC edges are reversed).

```java
public int[] kosaraju(int n, List<List<Integer>> adj) {
    // Pass 1: finish order
    boolean[] visited = new boolean[n];
    Deque<Integer> stack = new ArrayDeque<>();
    for (int i = 0; i < n; i++)
        if (!visited[i]) dfs1(adj, visited, stack, i);

    // Build reverse graph
    List<List<Integer>> radj = new ArrayList<>();
    for (int i = 0; i < n; i++) radj.add(new ArrayList<>());
    for (int u = 0; u < n; u++)
        for (int v : adj.get(u)) radj.get(v).add(u);

    // Pass 2: DFS on reverse in reverse-finish order
    Arrays.fill(visited, false);
    int[] comp = new int[n];
    int sccId = 0;
    while (!stack.isEmpty()) {
        int node = stack.pop();
        if (!visited[node]) {
            dfs2(radj, visited, comp, sccId++, node);
        }
    }
    return comp; // comp[i] = SCC id of node i
}

private void dfs1(List<List<Integer>> adj, boolean[] visited,
                  Deque<Integer> stack, int u) {
    visited[u] = true;
    for (int v : adj.get(u))
        if (!visited[v]) dfs1(adj, visited, stack, v);
    stack.push(u); // push AFTER all descendants
}

private void dfs2(List<List<Integer>> radj, boolean[] visited,
                  int[] comp, int sccId, int u) {
    visited[u] = true;
    comp[u] = sccId;
    for (int v : radj.get(u))
        if (!visited[v]) dfs2(radj, visited, comp, sccId, v);
}
```

**Complexity:** O(V+E) — two DFS passes + one graph transpose.
**Space:** O(V+E) — reverse graph + stack + visited.

---

### Algorithm 2: Tarjan's SCC (One Pass — Lowlink Values)

Uses **discovery time** `disc[u]` and **lowlink** `low[u]`:
- `disc[u]` = when u was first visited
- `low[u]` = earliest disc reachable from u's subtree via back edges
- u is an SCC root iff `low[u] == disc[u]`

Uses an explicit stack to collect SCC members. `onStack[]` tracks which
nodes are currently in the SCC-collection stack (not the DFS call stack).

```java
int timer = 0, sccCount = 0;
int[] disc, low, comp;
boolean[] onStack;
Deque<Integer> stack;

public void tarjanSCC(int n, List<List<Integer>> adj) {
    disc = new int[n]; low = new int[n]; comp = new int[n];
    onStack = new boolean[n];
    Arrays.fill(disc, -1);
    stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++)
        if (disc[i] == -1) dfs(adj, i);
}

private void dfs(List<List<Integer>> adj, int u) {
    disc[u] = low[u] = timer++;
    stack.push(u); onStack[u] = true;

    for (int v : adj.get(u)) {
        if (disc[v] == -1) {
            dfs(adj, v);
            low[u] = Math.min(low[u], low[v]);
        } else if (onStack[v]) {
            // Back edge to ancestor still on stack
            low[u] = Math.min(low[u], disc[v]);
        }
        // If v was visited but NOT onStack: cross edge to finished SCC — ignore
    }

    // u is SCC root if its lowlink equals its own discovery time
    if (low[u] == disc[u]) {
        while (true) {
            int v = stack.pop();
            onStack[v] = false;
            comp[v] = sccCount;
            if (v == u) break;
        }
        sccCount++;
    }
}
```

**Complexity:** O(V+E) — single DFS pass.
**Space:** O(V) — no need for reverse graph.

---

### Kosaraju vs Tarjan — When to Use Which

| | Kosaraju | Tarjan |
|--|----------|--------|
| Passes | 2 DFS + graph transpose | 1 DFS |
| Space | O(V+E) — needs reverse graph | O(V) — no reverse graph |
| Code clarity | Higher (two simple DFS) | Lower (onStack + lowlink) |
| Memory efficiency | Lower | Higher |
| Interview preference | Easier to explain | More impressive, one pass |
| Production preference | Static graphs | Dynamic / memory-constrained |

**Interview tip:** Explain Kosaraju first (cleaner intuition), then mention
Tarjan as the one-pass O(V) space alternative. Shows depth.

---

## Part 2 — Bridges (Cut Edges)

### Definition

A **bridge** is an edge whose removal increases the number of connected
components. Applies to **undirected** graphs.

```
Graph: 0-1-2-0, 1-3
Bridges: edge (1,3) — removing it disconnects node 3
Not bridges: 0-1, 1-2, 2-0 (part of a cycle — alternative paths exist)
```

### Tarjan's Bridge Algorithm

Track `disc[u]` and `low[u]`. After DFS into child v:
```
low[u] = min(low[u], low[v])
if low[v] > disc[u]: edge (u,v) is a BRIDGE
```

`low[v] > disc[u]` means v's entire subtree cannot reach u or any ancestor
of u — the edge (u,v) is the ONLY connection.

```java
int timer = 0;

public void findBridges(int u, int parent, int[] disc, int[] low,
                        boolean[] visited, List<List<Integer>> adj,
                        List<List<Integer>> bridges) {
    visited[u] = true;
    disc[u] = low[u] = timer++;

    for (int v : adj.get(u)) {
        if (!visited[v]) {
            findBridges(v, u, disc, low, visited, adj, bridges);
            low[u] = Math.min(low[u], low[v]);
            if (low[v] > disc[u])           // STRICT >
                bridges.add(Arrays.asList(u, v));
        } else if (v != parent) {           // back edge (not tree edge we came from)
            low[u] = Math.min(low[u], disc[v]);
        }
    }
}
```

**Multi-edge caveat:** if two edges exist between u and v, `v != parent` is
incorrect — both edges would be treated as back edges, preventing either from
being a bridge. Fix: use **edge indices** instead of parent node to identify
which specific edge to skip.

---

## Part 3 — Articulation Points (Cut Vertices)

### Definition

An **articulation point** is a vertex whose removal increases the number of
connected components. Applies to **undirected** graphs.

```
Graph: 0-1-2-3, 1-4
Articulation points: node 1 (removing it disconnects {0} from {2,3,4})
                     node 2 (removing it disconnects {3} from rest)
```

### Tarjan's Articulation Point Algorithm

Node u is an articulation point iff:
1. u is the DFS root AND has ≥ 2 independent subtrees, OR
2. u is NOT the root AND has a child v with `low[v] >= disc[u]`

Note: condition uses `>=` (not strict `>`), unlike bridges.

```java
int timer = 0;

public void findAPs(int u, int parent, int[] disc, int[] low,
                    boolean[] visited, boolean[] isAP,
                    List<List<Integer>> adj) {
    visited[u] = true;
    disc[u] = low[u] = timer++;
    int children = 0;

    for (int v : adj.get(u)) {
        if (!visited[v]) {
            children++;
            findAPs(v, u, disc, low, visited, isAP, adj);
            low[u] = Math.min(low[u], low[v]);

            if (parent == -1 && children > 1)   // root with ≥ 2 subtrees
                isAP[u] = true;
            if (parent != -1 && low[v] >= disc[u]) // non-root: v can't bypass u
                isAP[u] = true;
        } else if (v != parent) {
            low[u] = Math.min(low[u], disc[v]);
        }
    }
}
```

---

## The Critical Difference: Bridges vs Articulation Points

| | Bridges | Articulation Points |
|--|---------|---------------------|
| What is removed | Edge | Vertex |
| Condition | `low[v] > disc[u]` (**strict**) | `low[v] >= disc[u]` (**≥**) |
| Root special case | No | Yes (≥ 2 children in DFS tree) |

**Why >= for AP but > for bridge?**

```
low[v] == disc[u]: v can reach exactly u via a back edge.
  → Removing edge (u,v): u still reachable from v via back edge → NOT a bridge.
  → Removing vertex u: the back edge from v to u is GONE → v disconnected → AP.
```

---

## Lowlink Intuition Summary

```
disc[u] = discovery timestamp of u (unique, monotonically increasing)
low[u]  = minimum disc[] reachable from subtree of u via at most one back edge

Back edges update: low[u] = min(low[u], disc[v])   ← use disc (direct reach)
Tree edges update: low[u] = min(low[u], low[v])    ← use low (propagate from child)

#1 Bug source: confusing disc[v] vs low[v] for back edges.
  disc[v]: "where can I reach DIRECTLY from here?"
  low[v]:  "what did my child's subtree discover?" (propagated, not direct)
  For back edges, always use disc[v] — you reach v directly, not through v's children.
```

---

## Real-World Applications

### SCC Applications

| Domain | SCC models | Algorithm |
|--------|-----------|-----------|
| Compiler optimization | Mutually recursive function groups | Tarjan's (used in GCC, LLVM) |
| Package managers (npm, apt) | Circular dependency groups | Kosaraju's or Tarjan's |
| Social networks | Tightly-knit mutual-follower communities | Kosaraju's |
| Web PageRank | Strongly connected web page clusters | Tarjan's |
| Deadlock detection | Circular wait-for graphs in distributed systems | Tarjan's |
| Microservices | Cyclic service call dependency clusters | Kosaraju's |

### Bridge Applications

| Domain | Bridge models | Why it matters |
|--------|--------------|----------------|
| Network reliability | Single cable/link whose failure splits network | Must add redundancy |
| Internet routing | Critical router-to-router links | Identify bottlenecks |
| Power grids | Single transmission line splitting grid | Resilience planning |
| LeetCode #1192 Critical Connections | Server connection bridges | Exact match |

### Articulation Point Applications

| Domain | AP models | Why it matters |
|--------|----------|----------------|
| Network design | Single router whose failure splits network | Must add redundancy |
| Social networks | "Broker" node connecting otherwise disconnected communities | High influence |
| Infrastructure | Single road/bridge connecting regions | Resilience planning |
| Circuit design | Single component whose failure breaks circuit | Fault analysis |

---

## References

- **emre.me — Tarjan's Algorithm: Bridges and Articulation Points:**
  https://emre.me/algorithms/tarjans-algorithm/
  "Bridges and Articulation Points hint at weak points, bottlenecks or
  vulnerabilities in the graph. During DFS, bridges are found where the id
  of the node your edge is coming from is less than the low-link value of
  the node your edge is going to."

- **arXiv:1202.0319 — Real-Time Monitoring of Undirected Networks:**
  https://arxiv.org/pdf/1202.0319
  "Articulation Points in a network represent single points of failure.
  Bridge-block forest (BBF): each round node connected to only two square
  nodes is a bridge; each square node connected to two round nodes is an
  articulation point."

- **arXiv:2103.15217 — Euler Meets GPU: Bridge-Finding and Biconnectivity:**
  https://arxiv.org/pdf/2103.15217
  "The algorithmic study of biconnectivity dates back to Hopcroft and Tarjan
  who presented linear-time algorithms for finding articulation points, bridges,
  and 2-connected components. These are central in planar graph recognition
  and network analysis."

- **Scaler — Articulation Points and Bridges:**
  https://www.scaler.com/topics/data-structures/articulation-points-and-bridges/
  "Articulation Points represent single points of failure. If we remove a
  vertex that is an articulation point, the number of connected components
  in that network increases."

- **Codeforces — Articulation Points and Bridges (Tarjan's Algorithm):**
  https://codeforces.com/blog/entry/71146
  "Conditions for articulation point: (1) all paths from ancestors of V to
  descendants require V; (2) V is the DFS root with ≥ 2 disconnected subtrees."

- **Educative — Kosaraju's SCC Algorithm:**
  https://www.educative.io/answers/what-is-kosarajus-strongly-connected-component-algorithm
  "Identifying SCCs in directed graphs is pivotal with wide-ranging applications,
  from social network analysis to web page rankings and compiler optimizations."

- **Cornell SCC Blog — Kosaraju and Advertising:**
  https://blogs.cornell.edu/info2040/2021/11/08/kosarajus-algorithm-strongly-connected-components-and-advertising/
  "Twitter's directed graph can be analyzed with Kosaraju's algorithm to find
  communities and understand airline traffic patterns."
