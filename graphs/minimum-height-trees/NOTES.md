# Minimum Height Trees — Notes & Intuition

**LeetCode #310** | Graphs / BFS / Topological Sort | Medium

---

## Problem

Find all root labels that produce trees of minimum height. A tree can be
rooted at any node — different roots give different heights.

```
n=4, edges=[[1,0],[1,2],[1,3]]              →  [1]
n=6, edges=[[3,0],[3,1],[3,2],[3,4],[4,5]] →  [3,4]
```

---

## Core Insight — Find Tree Centres

The roots of minimum height trees are the **centres** of the tree — the
nodes that minimise the maximum distance to any other node.

```
A tree has AT MOST 2 centres.

Why at most 2?
  Any tree has a longest path (diameter). The centre(s) are the midpoint(s)
  of this diameter path.
  Even diameter → 1 centre (exact midpoint)
  Odd diameter  → 2 centres (two adjacent midpoints)
```

---

## Algorithm — Leaf Peeling (Reverse Kahn's)

Repeatedly remove all current leaf nodes (degree 1) simultaneously,
layer by layer, until ≤ 2 nodes remain. Those are the centres.

```
Think of it as burning a tree from all its tips inward — the last nodes
left standing are the centres.
```

```java
while (remaining > 2):
    remaining -= leaves.size()
    for each leaf:
        neighbour = leaf's only neighbour
        remove leaf from neighbour's adjacency
        if neighbour.degree == 1: add to newLeaves
    leaves = newLeaves
```

---

## Analogy with Kahn's Topological Sort

| | Kahn's (DAG) | Leaf Peeling (Tree) |
|--|-------------|---------------------|
| Start from | In-degree 0 (sources) | Degree 1 (leaves) |
| Trigger for next | In-degree reaches 0 | Degree reaches 1 |
| Direction | Sources → sinks | Outside → centre |
| Graph type | Directed acyclic | Undirected tree |
| Result | Topological order | Tree centre(s) |

Same BFS layer-by-layer pattern — different stopping condition.

---

## Why Leaf Nodes Are Never Optimal Roots

A leaf has all other nodes on one side → height = distance to the farthest
node = at least the diameter of the tree. The centre minimises the maximum
distance — it cannot be a leaf (for n > 2).

```
Tree: 0-1-2-3-4 (path of length 4)
Root at 0: height = 4 (must reach node 4)
Root at 2: height = 2 (max distance to 0 or 4 is 2) ← centre, optimal
```

---

## Why `remaining > 2` (Not `> 1`)

When exactly 2 nodes remain, BOTH are valid centres. The loop exits,
returning both. If we continued to > 1, we'd incorrectly eliminate one.

```
n=2, edges=[[0,1]]:
  initial leaves = [0, 1]
  remaining = 2 ≤ 2 → immediate exit
  return [0, 1] ✓ (both are valid roots, both height = 1)
```

---

## Full Traces

**`n=6, edges=[[3,0],[3,1],[3,2],[3,4],[4,5]]`**
```
Leaves: [0,1,2,5] → remaining=2 → newLeaves=[3,4]
Result: [3,4] ✓
```

**`n=4, edges=[[1,0],[1,2],[1,3]]` (star)**
```
Leaves: [0,2,3] → remaining=1 → newLeaves=[1]
Result: [1] ✓ (centre of a star is always its hub)
```

---

## Complexity

| | |
|--|--|
| Time | O(n) — each node/edge processed exactly once |
| Space | O(n) — adjacency sets + leaf lists |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| n=1 | [0] | Single node is trivially the centre |
| n=2 | [0,1] | Both are centres (path of length 1) |
| Path graph | [mid] or [mid1,mid2] | Centre of the path |
| Star graph | [hub] | Hub is always the single centre |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Centroid decomposition | Recursive tree centres for divide-and-conquer | Same peeling idea, recursive |
| Weighted tree 1-centre | Minimise max weighted distance | Binary search + linear scan |
| p-centre problem | Place p facilities to minimise max distance | O(n log n) — parametric search |
| HLD (Heavy-Light Decomposition) | Optimal root for HLD is centroid | Same centre-finding as starting point |

---

## Connection to Other Problems

| Problem | Connection |
|---------|-----------|
| #207 Course Schedule | Kahn's on directed graph — same in-degree countdown |
| #210 Course Schedule II | Same — Kahn's BFS, return order |
| #310 Minimum Height Trees (this) | Kahn's on undirected tree — degree countdown |
| Centroid decomposition | Same centre, recursive on each half |
