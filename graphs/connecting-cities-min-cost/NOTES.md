# Connecting Cities With Minimum Cost — Notes & Intuition

**LeetCode #1135** | Graphs / MST / Kruskal's | Medium

---

## Problem

Given n cities and explicit connections `[city1, city2, cost]`, find the
minimum cost to connect ALL cities. Return -1 if impossible.

```
n=3, connections=[[1,2,5],[1,3,6],[2,3,1]]  →  6
n=4, connections=[[1,2,3],[3,4,4]]          →  -1  (two disconnected components)
```

---

## This vs #1584 Min Cost Connect All Points

| Feature | #1584 Min Cost Connect Points | #1135 Connecting Cities |
|---------|------------------------------|------------------------|
| Graph | Complete (all pairs) | Sparse (explicit edges) |
| Edge weights | Computed (Manhattan) | Given |
| Impossible? | Never | Yes → return -1 |
| Best algorithm | Prim's array O(n²) | **Kruskal's O(E log E)** |

**Key insight:** Kruskal's is better for sparse graphs because it only
processes the GIVEN edges (E edges), not all n² possible pairs.

---

## Algorithm — Kruskal's with Union-Find

```
1. Sort all edges by cost ascending
2. For each edge (u, v, cost):
   - If find(u) != find(v): union them, add cost, edgesUsed++
   - If find(u) == find(v): skip (would create a cycle)
3. If edgesUsed == n-1: return totalCost (MST complete)
4. If all edges processed and edgesUsed < n-1: return -1
```

---

## Why the -1 Case Happens

A spanning tree on n nodes needs EXACTLY n-1 edges. If we exhaust all
given connections but have added fewer than n-1, some cities are in
disconnected components — no spanning tree exists.

```
n=4, connections=[[1,2,3],[3,4,4]]:
  After processing: {1,2} and {3,4} — two separate components
  edgesUsed=2 < n-1=3 → impossible → -1
```

Kruskal's builds a **minimum spanning forest** in this case — optimal
within each connected component, but no global spanning tree.

---

## Prim's vs Kruskal's — When Each Wins

| Graph type | E (edges) | Preferred | Why |
|------------|-----------|-----------|-----|
| Complete (#1584) | n(n-1)/2 ≈ n² | **Prim's array** O(n²) | Avoids storing/sorting n² edges |
| Sparse (#1135) | E << n² | **Kruskal's** O(E log E) | Only touches given edges |
| Sparse, adjacency list | E << n² | Prim's + PQ O(E log V) | Also good for sparse |

**This problem:** E = connections.length, potentially much smaller than n².
Kruskal's directly sorts the given list — no edge enumeration needed.

---

## Full Trace

**`n=3, connections=[[1,2,5],[1,3,6],[2,3,1]]`**

```
Sorted: [[2,3,1],[1,2,5],[1,3,6]]

[2,3,1]: find(2)=2, find(3)=3, diff → union, cost=1, edgesUsed=1
[1,2,5]: find(1)=1, find(2)→root, diff → union, cost=6, edgesUsed=2
         edgesUsed=2 == n-1=2 → return 6 ✓
```

**`n=4, connections=[[1,2,3],[3,4,4]]`**

```
[1,2,3]: union(1,2), edgesUsed=1
[3,4,4]: union(3,4), edgesUsed=2
All edges exhausted, edgesUsed=2 < n-1=3 → return -1 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(E log E) — sort edges; union/find is O(α(n)) ≈ O(1) |
| Space | O(n) — parent + rank arrays |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| n=1 | 0 | Single city, no connections needed |
| n=2, one edge | edge cost | One edge spans both cities |
| Disconnected input | -1 | No spanning tree exists |
| Multiple edges between same cities | Higher-cost edges skipped | DSU ignores same-component edges |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return actual MST edges | Track which edges were added | Store edge in list when union succeeds |
| Find if graph is connected | Check edgesUsed == n-1 | Same algorithm |
| #1584 All-pairs (dense) | All pairs, no impossible case | Prim's array O(n²) |
| Maximum spanning tree | Connect with max total cost | Sort edges descending |
| k-connected subgraph | More robust than tree | More complex: k-edge-connected MST |

---

## Connection to This Repo's MST Problems

| Problem | MST Type | Algorithm |
|---------|----------|-----------|
| #1584 Min Cost Connect Points | Dense (complete graph) | Prim's array |
| #1135 Connecting Cities (this) | Sparse (explicit edges) | Kruskal's |
| #684 Redundant Connection | Cycle detection (1 extra edge) | Kruskal's stops at cycle |
| #778 Swim in Rising Water | Partial MST (stop early) | Kruskal's stops when connected |
| #1631 Path Min Effort | Partial MST (stop early) | Kruskal's stops when connected |
