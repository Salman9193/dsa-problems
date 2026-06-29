# Is Graph Bipartite? — Notes & Intuition

**LeetCode #785** | Graphs / BFS / DFS | Medium

---

## Problem

Determine if an undirected graph is bipartite — can its nodes be split
into two groups such that every edge connects a node from group A to
a node from group B?

```
[[1,3],[0,2],[1,3],[0,2]]      →  true   (A={0,2}, B={1,3})
[[1,2,3],[0,2],[0,1,3],[0,2]]  →  false  (triangle 0-1-2)
```

---

## Key Equivalences

All of these conditions are equivalent:

```
Bipartite
  ↔ 2-colourable (no two adjacent nodes share the same colour)
  ↔ no odd-length cycles
  ↔ graph spectrum symmetric about 0 (Hückel molecular orbital theorem)
```

---

## The 2-Colouring Algorithm

Assign colour 1 to the start node. Assign the opposite colour to each
neighbour. If we ever try to assign a colour that conflicts with an
existing one → not bipartite.

```
colour = 0: unvisited
colour = 1: red
colour = -1: blue
```

Negating flips between 1 and -1 cleanly: `colour[neighbour] = -colour[node]`

---

## BFS Approach

```java
queue.offer(start);
colour[start] = 1;

while (!queue.isEmpty()) {
    int node = queue.poll();
    for (int neighbour : graph[node]) {
        if (colour[neighbour] == 0) {
            colour[neighbour] = -colour[node];
            queue.offer(neighbour);
        } else if (colour[neighbour] == colour[node]) {
            return false;  // conflict!
        }
    }
}
```

---

## Why the Outer `for` Loop?

The graph may be **disconnected**. Without the outer loop, isolated
components are never checked — they could contain odd cycles and we'd
incorrectly return `true`.

```java
for (int start = 0; start < n; start++) {
    if (colour[start] != 0) continue;
    // BFS from this unvisited component
}
```

---

## Full Trace — `[[1,3],[0,2],[1,3],[0,2]]`

```
colours after BFS:
  node 0: red   (1)
  node 1: blue  (-1)
  node 2: red   (1)
  node 3: blue  (-1)

Edges: 0-1 (red-blue ✓), 0-3 (red-blue ✓),
       1-2 (blue-red ✓), 2-3 (red-blue ✓)
→ bipartite: A={0,2}, B={1,3} ✓
```

**Counter-example — triangle `[[1,2],[0,2],[0,1]]`:**
```
colour[0]=1, colour[1]=-1
Process edge 1→2: colour[2]=0 → colour[2]=1
Process edge 2→0: colour[0]=1 == colour[2]=1 → CONFLICT → false ✓
```

---

## Why Odd Cycles Break Bipartiteness

In an odd-length cycle, alternating colouring forces the start and end
node (which are adjacent) to have the **same colour**:

```
Cycle: 0 → 1 → 2 → 0   (length 3, odd)
colour: 1 → -1 → 1 → conflict at 0 (need -1, but it's 1)
```

Even-length cycles always work:
```
Cycle: 0 → 1 → 2 → 3 → 0   (length 4, even)
colour: 1 → -1 → 1 → -1 → needs 1 at 0 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(V + E) — visit each node and edge once |
| Space | O(V) — colour array + queue |

---

## Related Problems

| Problem | Connection |
|---------|-----------|
| #785 Is Graph Bipartite? (this) | 2-colouring check |
| #886 Possible Bipartition | Same algorithm on a derived graph |
| #207 Course Schedule | Cycle detection in directed graph |
| Bipartite matching | Hungarian algorithm, Hopcroft-Karp |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Possible bipartition (#886) | Can people be split into two groups given dislikes? | Build conflict graph; check bipartiteness |
| k-colouring | Generalise to k colours | Backtracking for k≥3; NP-complete — see K_COLOURING.md |
| Bipartite matching | Find maximum matching | Hungarian algorithm or Hopcroft-Karp |
| Find odd cycle | Return the cycle if not bipartite | Track parent in BFS; reconstruct cycle on conflict |
| Directed bipartite | Edges are directed | Check if underlying undirected graph is bipartite |
| Weighted bipartite matching | Edges have weights | Hungarian algorithm with weights |
| Online bipartiteness | Edges added one at a time | Link-cut trees for dynamic connectivity |

**Bipartite matching applications:**
- Assigning tasks to workers (each worker can do a subset of tasks) → maximum bipartite matching
- Stable matching (Gale-Shapley) — a special case where preferences are ranked
- Network flow reduction: max bipartite matching = max flow in a unit-capacity flow network

**Why bipartite check is O(V+E) but k-colouring is NP-complete:** With 2 colours, every assignment is forced — there's only one valid colour for each node. With k≥3, multiple choices exist at each step. This breaks the greedy correctness — see COMPLEXITY_THEORY.md and K_COLOURING.md.

---

## Union-Find Alternative

DSU can solve bipartite checking using a **virtual node trick per node**:
For each node u, union all its neighbours together. If u and any of its
neighbours end up in the same component as u itself, a cycle of odd length
exists → not bipartite.

More precisely: for each node u with neighbours v1, v2, ...:
- union(v1, v2), union(v2, v3), ... (all neighbours in one group)
- If find(u) == find(any_neighbour) → odd cycle detected → not bipartite

```java
public boolean isBipartite(int[][] graph) {
    int n = graph.length;
    int[] parent = new int[n]; int[] rank = new int[n];
    for (int i = 0; i < n; i++) parent[i] = i;

    for (int u = 0; u < n; u++) {
        if (graph[u].length == 0) continue;
        int firstNeighbour = graph[u][0];
        for (int v : graph[u]) {
            // u and v are adjacent → they must be in different sets
            // If they're already in the same set → odd cycle → not bipartite
            if (find(parent, u) == find(parent, v)) return false;
            // Union all neighbours of u together (they share the same colour)
            union(parent, rank, firstNeighbour, v);
        }
    }
    return true;
}

// find with path compression — O(α(n)) amortised
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
    return parent[x];
}

// union by rank — keeps tree height O(log n) → ensures near-O(1) find()
private void union(int[] parent, int[] rank, int x, int y) {
    int px = find(parent, x), py = find(parent, y);
    if (px == py) return;
    if      (rank[px] < rank[py]) parent[px] = py;
    else if (rank[px] > rank[py]) parent[py] = px;
    else { parent[py] = px; rank[px]++; }
}
```

**Why this works:** In a bipartite graph, all neighbours of u must be in
the same colour class (opposite to u). So unioning all neighbours of u is
valid. If u itself ends up in the same component as its neighbours, a
contradition arises → not bipartite.

**BFS vs DSU:** BFS is cleaner and more intuitive for bipartite checking.
DSU is useful when the graph evolves dynamically (edges added online) —
BFS would need to re-run from scratch; DSU handles new edges incrementally.
