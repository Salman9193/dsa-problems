# Graph Cycle Detection — Complete Guide

Floyd's tortoise & hare works only on **functional graphs** (one outgoing
edge per node, like a linked list). General graphs require different
approaches depending on whether the graph is directed or undirected.

---

## Why Floyd's Doesn't Generalise to Graphs

Floyd's relies on two pointers following the **same deterministic sequence**
at different speeds. In a general graph, each node has multiple neighbors —
slow and fast can diverge onto different paths and never meet, even if a
cycle exists.

Floyd's works on any structure where each node has **exactly one** outgoing
edge (functional graph). A linked list is a functional graph. A general
graph is not.

---

## 1. Undirected Graph — DFS with Parent Check

### The Key Insight

In an undirected graph, every edge appears twice (A→B and B→A).
When traversing A→B, seeing B→A is not a cycle — it's the same edge reversed.
A real cycle is any back edge that leads to a visited node that is NOT the
direct parent of the current node.

```java
boolean hasCycle(int n, List<List<Integer>> adj) {
    boolean[] visited = new boolean[n];
    for (int i = 0; i < n; i++) {
        if (!visited[i] && dfs(adj, visited, i, -1))
            return true;
    }
    return false;
}

boolean dfs(List<List<Integer>> adj, boolean[] visited, int node, int parent) {
    visited[node] = true;
    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            if (dfs(adj, visited, neighbor, node)) return true;
        } else if (neighbor != parent) {
            return true;   // back edge that isn't our own parent → cycle
        }
    }
    return false;
}
```

### Example

```
Graph: 1 - 2 - 3 - 1  (triangle — has cycle)

DFS from 1:
  visit 1 (parent=-1)
    visit 2 (parent=1)
      visit 3 (parent=2)
        neighbor 1: visited AND 1 != parent(2) → cycle ✓
```

### Complexity
- Time: O(V + E)
- Space: O(V) — visited array + recursion stack

---

## 2. Directed Graph — DFS with Recursion Stack

### Why Visited Alone Isn't Enough

Consider:
```
A → B → C
↓
D → B
```

B is visited from two different paths (A→B and D→B), but there's no cycle.
If we return `true` just because B is already visited, we get a false positive.

A cycle in a directed graph means you reached a node that's **currently on
your active DFS path** (i.e. in the recursion stack). This is called a
**back edge**.

```java
boolean hasCycle(int n, List<List<Integer>> adj) {
    boolean[] visited = new boolean[n];
    boolean[] inStack = new boolean[n];  // currently in active DFS path

    for (int i = 0; i < n; i++) {
        if (!visited[i] && dfs(adj, visited, inStack, i))
            return true;
    }
    return false;
}

boolean dfs(List<List<Integer>> adj, boolean[] visited,
            boolean[] inStack, int node) {
    visited[node] = true;
    inStack[node] = true;       // entering this node's subtree

    for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
            if (dfs(adj, visited, inStack, neighbor)) return true;
        } else if (inStack[neighbor]) {
            return true;        // back edge → cycle
        }
    }

    inStack[node] = false;      // leaving this node's subtree
    return false;
}
```

### The Two Arrays Explained

| Array | Meaning | When true |
|-------|---------|-----------|
| `visited` | Ever seen this node | Permanently true after first visit |
| `inStack` | Currently on active DFS path | True while DFS is inside this node's subtree, false after backtracking |

### Example

```
Cycle:    A → B → C → B
No cycle: A → B → C
          ↓
          D → C

Cycle case:
  DFS(A): inStack=[A]
    DFS(B): inStack=[A,B]
      DFS(C): inStack=[A,B,C]
        neighbor B: visited=true AND inStack[B]=true → cycle ✓

No-cycle case:
  DFS(A): inStack=[A]
    DFS(B): inStack=[A,B]
      DFS(C): inStack=[A,B,C]
        no unvisited neighbors
      inStack[C]=false
    inStack[B]=false
    DFS(D): inStack=[A,D]
      neighbor C: visited=true but inStack[C]=false → no cycle ✓
```

### Complexity
- Time: O(V + E)
- Space: O(V) — two boolean arrays + recursion stack

---

## 3. Directed Graph — Kahn's Algorithm (BFS Topological Sort)

An alternative that avoids recursion entirely. The insight: in a DAG (no
cycles), a topological ordering exists. If we can process all nodes via
topological sort, there's no cycle. If some nodes are never processed
(their in-degree never reaches 0), they're stuck in a cycle.

```java
boolean hasCycle(int n, int[][] edges) {
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

    int processed = 0;
    while (!queue.isEmpty()) {
        int node = queue.poll();
        processed++;
        for (int neighbor : adj.get(node)) {
            if (--inDegree[neighbor] == 0) queue.offer(neighbor);
        }
    }

    return processed != n;  // unprocessed nodes are in a cycle
}
```

### Why It Works

Nodes in a cycle always have in-degree ≥ 1 — no other node in the cycle
has finished processing to reduce their count. They never enter the queue,
so `processed < n`.

### Complexity
- Time: O(V + E)
- Space: O(V)

### When to Prefer Kahn's Over DFS

- When you need topological order as a by-product
- When you want to avoid recursion stack overflow on deep graphs
- LeetCode #207 (Course Schedule), #210 (Course Schedule II)

---

## Algorithm Comparison

| Graph type | Algorithm | Time | Space | Notes |
|------------|-----------|------|-------|-------|
| Linked list / functional graph | Floyd's (tortoise & hare) | O(n) | O(1) | Only works with single outgoing edge |
| Undirected graph | DFS + parent check | O(V+E) | O(V) | Skip same-edge reversal |
| Directed graph | DFS + inStack | O(V+E) | O(V) | Two arrays: visited + inStack |
| Directed graph | Kahn's BFS (topo sort) | O(V+E) | O(V) | Iterative, no recursion |

---

## Common Mistakes

### 1. Using visited alone for directed graphs
```java
// WRONG for directed graphs
if (visited[neighbor]) return true;  // false positive on converging paths
```

### 2. Forgetting to reset inStack on backtrack
```java
// WRONG — inStack never gets cleared
inStack[node] = true;
for (int neighbor : adj.get(node)) { ... }
// missing: inStack[node] = false;
```

### 3. Using Floyd's on a general graph
Floyd's requires exactly one outgoing edge per node. On a graph where nodes
have multiple neighbors, slow and fast diverge and the algorithm is incorrect.

---

## Real-World Applications

| Domain | Graph type | Algorithm |
|--------|------------|-----------|
| OS deadlock detection | Directed (resource allocation graph) | DFS + inStack |
| Build systems (Maven, Gradle) | Directed (dependency graph) | Kahn's topo sort |
| Package managers (npm, pip) | Directed (dependency graph) | Kahn's topo sort |
| Compiler CFG analysis | Directed (control flow graph) | DFS + inStack |
| Social network mutual follows | Undirected | DFS + parent check |
| Cryptography (Pollard's Rho) | Functional (sequence graph) | Floyd's |

---

## Further Reading

- Floyd's cycle detection: https://en.wikipedia.org/wiki/Floyd%27s_cycle-finding_algorithm
- Kahn's algorithm: https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm
- Coffman et al. on OS deadlocks: https://dl.acm.org/doi/10.1145/356586.356588
- LLVM cycle info: https://github.com/llvm/llvm-project/blob/main/llvm/include/llvm/ADT/CycleInfo.h
