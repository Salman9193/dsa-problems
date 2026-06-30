# Redundant Connection — Notes & Intuition

**LeetCode #684** | Graphs / Union-Find | Medium

---

## Problem

Given a graph that started as a tree (n nodes, n-1 edges) with one extra
edge added, find the edge whose removal restores a valid tree. If multiple
answers exist, return the one occurring **last** in the input.

```
edges=[[1,2],[1,3],[2,3]]                  →  [2,3]
edges=[[1,2],[2,3],[3,4],[1,4],[1,5]]     →  [1,4]
```

---

## Core Insight — Tree Property

A tree with n nodes has EXACTLY n-1 edges and no cycles. The input has
exactly n edges — one too many — guaranteeing exactly one cycle exists.

**The redundant edge = the first edge (in input order) connecting two
nodes already in the same connected component.**

---

## Why DSU Is THE Natural Fit (Not an Alternative)

This problem incrementally builds a graph edge by edge, repeatedly asking
"are these two nodes already connected?" — this is EXACTLY what Union-Find
was designed for.

```java
for each edge (u, v):
    if find(u) == find(v): return edge  // already connected → redundant
    union(u, v)                          // merge components
```

**Why NOT BFS/DFS?**
BFS/DFS would need to search the ENTIRE current graph for a path between
u and v before adding each edge — O(n) per edge, O(n²) total.
DSU's find() answers "same component?" in O(α(n)) — no traversal needed.

```
BFS/DFS: O(n²) — rebuild search per edge
DSU:     O(n α(n)) — O(1)-ish per edge
```

For n=1000: BFS/DFS ≈ 10^6 operations, DSU ≈ 5000 operations.

---

## Why Left-to-Right Processing Gives the LAST Valid Answer

Every edge processed BEFORE the redundant one successfully unions two
DIFFERENT components (no cycle yet). The moment an edge connects an
ALREADY-unified pair, that edge is provably:
1. The cause of the (only) cycle
2. The LAST such cycle-causing edge among all edges processed so far
   (since all prior edges were cycle-free)

No need to track "last" explicitly — processing order + early return
naturally gives the correct answer.

---

## Full Trace — `[[1,2],[2,3],[3,4],[1,4],[1,5]]`

```
parent = [0,1,2,3,4,5]  (1-indexed)

[1,2]: find(1)=1, find(2)=2, diff → union → {1,2}
[2,3]: find(2)=1, find(3)=3, diff → union → {1,2,3}
[3,4]: find(3)=1, find(4)=4, diff → union → {1,2,3,4}
[1,4]: find(1)=1, find(4)=1, SAME → return [1,4] ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(n α(n)) — n edges, O(α(n)) per find/union |
| Space | O(n) — parent + rank arrays |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Triangle `[[1,2],[2,3],[1,3]]` | `[1,3]` (or last cycle edge) | First edge re-connecting same component |
| Star + extra edge | The extra spoke edge | Same logic |
| Self-loop `[[1,1]]` | `[1,1]` | Immediate cycle (u==v, same component trivially) |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #685 Redundant Connection II | Directed graph, in-degree 2 possible | More complex: 3 cases (cycle, two-parent, both) |
| Find ALL redundant edges | Multiple extra edges | Same DSU loop, collect all cycle-causing edges |
| Weighted edges, minimize removed weight | Choose cheapest edge to remove | Among cycle edges, track minimum weight |
| Return edge index, not edge itself | Need position in input | Track index alongside edge during iteration |
| Build MST instead | Want max spanning tree, not just remove redundant | Kruskal's algorithm (same DSU core) |

---

## Connection to Kruskal's MST

This problem is the inverse framing of Kruskal's algorithm:

```
Kruskal's MST:        Process edges sorted by weight; skip edges that
                       would create a cycle (same component) → builds MST

Redundant Connection: Process edges in input order; the edge that WOULD
                       create a cycle is exactly what Kruskal's would skip
                       — but here we want to IDENTIFY it, not skip silently
```

Both use the identical DSU mechanism: `find()` to check connectivity,
`union()` to merge components, skip (or report) edges that connect
already-unified nodes.
