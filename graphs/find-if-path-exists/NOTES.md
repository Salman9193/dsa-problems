# Find if Path Exists in Graph — Notes & Intuition

**LeetCode #1971** | Graphs / BFS / DFS / Union-Find | Easy

---

## Problem

Given n nodes (0 to n-1), bidirectional edges, a source and destination,
determine if a path exists from source to destination.

```
n=3, edges=[[0,1],[1,2],[2,0]], source=0, dest=2   →  true
n=6, edges=[[0,1],[0,2],[3,5],[5,4],[4,3]], source=0, dest=5  →  false
```

---

## Three Approaches

This is a fundamental **graph connectivity** problem. Three clean solutions exist.

---

## Approach 1 — BFS

Explore level by level from source. If destination is reached → true.

```java
Queue<Integer> queue = new LinkedList<>();
boolean[] visited = new boolean[n];
queue.offer(source); visited[source] = true;

while (!queue.isEmpty()) {
    int node = queue.poll();
    for (int neighbour : adj.get(node)) {
        if (neighbour == destination) return true;
        if (!visited[neighbour]) { visited[neighbour] = true; queue.offer(neighbour); }
    }
}
return false;
```

Best when: shortest path is also needed (BFS finds it naturally).

---

## Approach 2 — DFS

Recurse deeply from source. If destination is reached → true.

```java
boolean dfs(node, dest):
    if node == dest: return true
    visited[node] = true
    for neighbour in adj[node]:
        if not visited[neighbour] and dfs(neighbour, dest): return true
    return false
```

Best when: graph is deep, recursive reasoning is clearest.
Risk: stack overflow for n = 10⁵ with a degenerate chain graph.

---

## Approach 3 — Union-Find (Canonical for Connectivity)

Union all edges. Check if source and destination share the same root.

```java
for each edge (u, v): union(u, v)
return find(source) == find(destination)
```

**Why this works:** Graph connectivity is an equivalence relation
(reflexive, symmetric, transitive). Union-Find maintains exactly this —
a partition into equivalence classes. Two nodes are connected iff they
share a root.

Best when: multiple connectivity queries on the same graph, or edges
arrive incrementally (online union-find).

---

## Comparison

| Approach | Build | Query | Space | Best for |
|----------|-------|-------|-------|---------|
| BFS | O(V+E) | O(V+E) | O(V+E) | Shortest path too |
| DFS | O(V+E) | O(V+E) | O(V+E) | Simple graphs |
| Union-Find | O(E α(E)) | O(α(n)) ≈ O(1) | O(V) | Multiple queries |

---

## Full Trace — Union-Find

`n=6, edges=[[0,1],[0,2],[3,5],[5,4],[4,3]], source=0, dest=5`

```
Initial: parent=[0,1,2,3,4,5]

union(0,1): parent[1]=0 → {0,1}
union(0,2): parent[2]=0 → {0,1,2}
union(3,5): parent[5]=3 → {3,5}
union(5,4): find(5)=3, parent[4]=3 → {3,4,5}
union(4,3): find(4)=3, find(3)=3 → already same, no-op

find(0) = 0   (component {0,1,2})
find(5) = 3   (component {3,4,5})
0 ≠ 3 → false ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| source == destination | true | No traversal needed |
| No edges, source ≠ dest | false | Isolated nodes |
| Fully connected graph | true | All in one component |
| source is isolated | false | No edges from source |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #547 Number of Provinces | Count connected components | BFS/DFS/Union-Find per unvisited node |
| #200 Number of Islands | 2D grid connectivity | BFS/DFS with 4-directional adjacency |
| #323 Number of Connected Components | Count components | Union-Find: count roots |
| Shortest path | Count hops to destination | BFS (tracks levels) |
| Directed graph | Edges have direction | BFS/DFS only (Union-Find is for undirected) |
| Dynamic edges | Edges added/removed | Dynamic connectivity (link-cut trees) |

---

## Why Union-Find Doesn't Work for Directed Graphs

Union-Find tracks undirected connectivity — if A is connected to B,
B is connected to A. In a directed graph, A→B doesn't mean B→A.
For directed reachability, use BFS/DFS from the source following
edge directions only.
