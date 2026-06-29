# Evaluate Division — Notes & Intuition

**LeetCode #399** | Graphs / BFS / Weighted | Medium

---

## Problem

Given equations `a/b = 2.0`, `b/c = 3.0`, answer queries like `a/c`, `b/a`, `x/x`.

```
equations = [["a","b"],["b","c"]], values = [2.0, 3.0]

a/c   →  6.0   (a/b × b/c = 2×3)
b/a   →  0.5   (1 / (a/b))
a/a   →  1.0
x/x   →  -1.0  (x not in system)
```

---

## Core Insight — Weighted Directed Graph

Each equation `a/b = k` becomes two directed edges:
```
a → b  weight k       (a/b = k)
b → a  weight 1/k     (b/a = 1/k)
```

A query `a/c` asks: **what is the product of edge weights along any path a→...→c?**

```
a → b → c
  ×2  ×3  = 6
```

The intermediate variable `b` algebraically cancels: `(a/b) × (b/c) = a/c`.

---

## Why Path Product = Division

This follows directly from algebra:
```
a/b = 2  means  a = 2b
b/c = 3  means  b = 3c
→ a = 2b = 2×3c = 6c
→ a/c = 6
```

The graph path product computes this chain of substitutions.

---

## Algorithm — BFS with Cumulative Product

```java
// Build: a→{b:2.0}, b→{a:0.5, c:3.0}, c→{b:0.333}
for each equation a/b = k:
    graph[a][b] = k
    graph[b][a] = 1/k

// Query src/dst:
BFS from src, tracking product[node] = src/node
On visiting neighbour: product[next] = product[curr] × weight(curr→next)
Return product[dst] when dst is reached; else -1.0
```

---

## Alternative — Weighted Union-Find

Better when there are many queries (large Q):

```
ratio[x] = x / root_of_x_component

find(x):
    if x != parent[x]:
        root = find(parent[x])
        ratio[x] *= ratio[parent[x]]   // accumulate through path
        parent[x] = root
    return parent[x]

query(x, y):
    if find(x) != find(y): return -1.0
    return ratio[x] / ratio[y]         // (x/root) / (y/root) = x/y
```

---

## Complexity Comparison

| Approach | Build | Per Query | Best for |
|----------|-------|-----------|---------|
| BFS | O(E) | O(V+E) | Few queries |
| Weighted Union-Find | O(E α(E)) | O(α(E)) | Many queries |
| Floyd-Warshall (all-pairs) | O(V³) | O(1) | All pairs precomputed |

---

## Full Trace

`equations=[["a","b"],["b","c"]], values=[2.0,3.0]`

```
Graph:
  a → {b: 2.0}
  b → {a: 0.5,  c: 3.0}
  c → {b: 0.333}

Query a/c (BFS from a):
  product = {a: 1.0}
  visit a → b:  product[b] = 1.0 × 2.0 = 2.0
  visit b → c:  product[c] = 2.0 × 3.0 = 6.0  → dst found → return 6.0 ✓
  visit b → a:  already visited

Query b/a (BFS from b):
  product = {b: 1.0}
  visit b → a:  product[a] = 1.0 × 0.5 = 0.5  → dst found → return 0.5 ✓
```

---

## Edge Cases

| Query | Result | Reason |
|-------|--------|--------|
| `x/y` where x not in system | -1.0 | Unknown variable |
| `a/a` | 1.0 | Self-division; same node |
| `a/d` (d in system, no path to a) | -1.0 | Different component |
| Direct edge `a/b` | 2.0 | O(1) — found in first BFS step |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Currency arbitrage | Find cycle with product > 1 | Bellman-Ford on log-negated weights |
| Max product path | Not just any path, but the best | Dijkstra with negative log weights |
| All-pairs precompute | Answer all queries O(1) | Floyd-Warshall O(V³) build |
| Streaming equations | New equations arrive online | Weighted Union-Find handles incrementally |
| Detect contradictions | New equation conflicts existing | Check if product is consistent before union |

---

## Connection to Other Graph Problems

| Problem | Structure | This problem's connection |
|---------|-----------|--------------------------|
| Path exists (#547 Number of Provinces) | Unweighted BFS/Union-Find | Same but with edge weights |
| Shortest path (#743) | Additive weights | This uses multiplicative weights |
| Currency arbitrage | Multiplicative cycle detection | Negate log weights → Bellman-Ford |
| Weighted Union-Find | Ratio tracking | Generalises standard DSU |

---

## Union-Find Alternative — Weighted DSU

For many queries on the same graph, weighted DSU gives O(α) per query after O(E) build.

```
parent[x] = root of x's component
ratio[x]  = x / parent[x]   (x's value relative to its root)

find(x):  path-compress AND accumulate ratios up to root
  if parent[x] != x:
      root = find(parent[x])
      ratio[x] *= ratio[parent[x]]   // chain multiplication
      parent[x] = root
  return parent[x]

union(x, y, val):  // x / y = val
  rx = find(x), ry = find(y)
  parent[rx] = ry
  ratio[rx] = ratio[y] * val / ratio[x]

query(x, y):
  if find(x) != find(y): return -1.0   // different components
  return ratio[x] / ratio[y]           // (x/root) / (y/root) = x/y
```

**When DSU beats BFS:** For Q queries on a static graph:
- BFS: O(E + Q×(V+E))
- Weighted DSU: O(E×α(E) + Q×α(V)) — much better for large Q.
