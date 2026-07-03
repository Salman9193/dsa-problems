# Critical Connections in a Network — Notes & Intuition

**LeetCode #1192** | Graphs / Tarjan's Bridge Algorithm | Hard

---

## Problem

Given a network of n servers and connections between them, find all
critical connections — connections whose removal leaves some server
unable to reach others.

```
n=4, connections=[[0,1],[1,2],[2,0],[1,3]]  →  [[1,3]]
```

---

## Core Insight — Find All Bridges

A critical connection = a **bridge** — an edge whose removal disconnects
the graph. Non-bridge edges are part of a cycle (alternative path exists).

```
connections=[[0,1],[1,2],[2,0],[1,3]]:
  Triangle 0-1-2 forms a cycle → no edge in it is a bridge
  Edge 1-3: node 3 has no alternative path back → BRIDGE = critical connection
```

---

## Tarjan's Bridge Algorithm — Key Concepts

### disc[u] and low[u]

```
disc[u] = discovery timestamp — when DFS first visited u
low[u]  = earliest disc reachable from u's subtree via back edges
```

`low[u]` captures how far back u's subtree can "reach" in the DFS tree.
A back edge from v to ancestor a updates: `low[v] = min(low[v], disc[a])`.

### The Bridge Condition

```
Edge (u, v) is a bridge iff low[v] > disc[u]

Meaning: v's entire subtree has NO back edge reaching u or any ancestor.
         The edge (u,v) is the ONLY connection from v's subtree to the rest.
         Remove it → v's subtree is disconnected.
```

### The Parent Edge Skip

```java
else if (v != parent) {
    low[u] = Math.min(low[u], disc[v]); // back edge
}
```

Skip the edge we used to ARRIVE at u (v == parent). In an undirected graph,
this edge appears twice. Using it as a "back edge" would make every edge
appear to have an alternative path.

---

## Critical Bug: disc[v] vs low[v] for Back Edges

When processing a back edge to already-visited v:

```java
low[u] = Math.min(low[u], disc[v]);  // CORRECT
low[u] = Math.min(low[u], low[v]);   // WRONG — causes missed bridges
```

- `disc[v]`: "I can reach directly to v's discovery time" — correct.
- `low[v]`: "I can reach as far as v's subtree discovered" — overstates;
  v's subtree discoveries don't help u if v is the only path.

**This is the #1 source of bugs in Tarjan's bridge/AP implementations.**

---

## Bridge vs Articulation Point Conditions

| | Bridge | Articulation Point |
|--|--------|-------------------|
| Condition | `low[v] > disc[u]` (**strict >**) | `low[v] >= disc[u]` (**>=**) |
| Why different | low[v]==disc[u]: edge still not bridge (back-edge bypass exists) but vertex u IS an AP (back-edge from v goes TO u — removing u kills it) | See above |

---

## Algorithm Steps

```
1. Build adjacency list from connections
2. DFS from any node (graph is connected — one call suffices)
3. On each tree edge (u→v):
   a. Recurse DFS(v, parent=u)
   b. low[u] = min(low[u], low[v])
   c. if low[v] > disc[u]: add [u,v] to bridges
4. On each back edge (u→v, v already visited, v≠parent):
   a. low[u] = min(low[u], disc[v])
```

---

## Full Trace — `n=4, connections=[[0,1],[1,2],[2,0],[1,3]]`

```
DFS tree: 0 → 1 → 2 (back to 0), 1 → 3

disc: [0, 1, 2, 3]
low after DFS:
  low[3]: no back edges → stays 3
    low[3]=3 > disc[1]=1 → BRIDGE [1,3] ✓
  low[2]: back edge to 0 → low[2]=0
    low[2]=0 NOT > disc[1]=1 → not bridge ✓
  low[1]: min(low[1], low[2])=0, min(0, low[3]) stays 0
    low[1]=0 NOT > disc[0]=0 → not bridge ✓
  low[0]: back edge from 2 to 0 already propagated

Result: [[1,3]] ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(V+E) — single DFS pass |
| Space | O(V+E) — adj list + disc/low arrays + call stack |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Find articulation points | Vertices not edges | Same framework, >= condition, root special case |
| Count bridges | Return count not list | Same algorithm, count++ instead of add |
| 2-edge-connected components | All edges NOT bridges | DSU on non-bridge edges |
| Multi-edges between servers | `v != parent` insufficient | Track edge index, not parent node |
| Dynamic bridge maintenance | Edges added/removed online | arXiv:1202.0319 real-time algorithm |

---

## Connection to Guides

- Full SCC, Bridges, AP guide: see `guides/SCC_BRIDGES_AP.md`
- DFS 3-colour (directed cycle detection): see `graphs/course-schedule/`
- Union-Find (undirected cycle detection): see `graphs/redundant-connection/`
