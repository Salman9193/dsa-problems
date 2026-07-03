# Min Cost to Connect All Points — Notes & Intuition

**LeetCode #1584** | Graphs / Minimum Spanning Tree | Medium

---

## Problem

Given n points on a 2D plane, connect them all with minimum total Manhattan
distance cost (each connection costs |x1-x2| + |y1-y2|).

```
points = [[0,0],[2,2],[3,10],[5,2],[7,0]]  →  20
points = [[3,12],[-2,5],[-4,1]]            →  18
```

---

## Core Insight — Minimum Spanning Tree

Connect all n nodes with n-1 edges, minimising total edge weight, no cycles.
This is the textbook **Minimum Spanning Tree (MST)** problem.

```
MST properties:
  - Exactly n-1 edges (tree on n nodes)
  - No cycles
  - Minimum total edge weight among all spanning trees
```

---

## Approach 1 — Prim's Algorithm with Array (Preferred for Dense Graphs)

```
minDist[v] = cheapest edge from current MST to node v

Repeat n times:
  1. Pick unvisited node u with minimum minDist[u]
  2. Add u to MST: totalCost += minDist[u], inMST[u] = true
  3. For all unvisited v: minDist[v] = min(minDist[v], dist(u,v))
```

**Why O(n²) not O(n² log n):**
We never store all edges — distances computed on the fly. Inner update loop
is O(n) per iteration × n iterations = O(n²). No sorting required.

---

## Approach 2 — Kruskal's Algorithm with DSU

```
1. Build ALL n(n-1)/2 edges with weights
2. Sort by weight
3. Add cheapest edge via Union-Find if it doesn't create a cycle
4. Stop when n-1 edges added
```

Uses the **reference UnionFind template** from `guides/UNION_FIND.md`.

---

## Prim's vs Kruskal's — Which to Use?

| | Prim's (array) | Kruskal's (DSU) |
|--|---------------|-----------------|
| Time | O(n²) | O(n² log n) |
| Space | O(n) | O(n²) — must store all edges |
| Best for | **Dense graphs** (E ≈ V²) | Sparse graphs (E ≈ V) |
| This problem | ✓ **Preferred** | Works but slower |

**This is a complete graph** — every pair of points has an edge.
E = n(n-1)/2 ≈ n² → Prim's array wins on both time and space.

**General rule:**
- Dense (E close to V²): Prim's with array
- Sparse (E close to V): Kruskal's with DSU

---

## Full Trace — `[[0,0],[2,2],[3,10],[5,2],[7,0]]`

```
Init: minDist=[0,INF,INF,INF,INF]

Step 0: pick node 0 (dist=0). cost=0
  Update dist: [1]=4, [2]=13, [3]=7, [4]=7

Step 1: pick node 1 (dist=4). cost=4
  Update: [2]=min(13,9)=9, [3]=min(7,3)=3, [4]=min(7,7)=7

Step 2: pick node 3 (dist=3). cost=7
  Update: [2]=min(9,10)=9, [4]=min(7,4)=4

Step 3: pick node 4 (dist=4). cost=11
  Update: [2]=min(9,14)=9

Step 4: pick node 2 (dist=9). cost=20

return 20 ✓
MST edges: 0-1(4), 1-3(3), 3-4(4), 1-2(9) or 2-?(9)
```

---

## Connection to Other Problems in This Repo

| Problem | MST Connection |
|---------|---------------|
| #1584 Min Cost Connect Points (this) | Full MST — minimize total cost |
| #778 Swim in Rising Water | Kruskal's stopping when src-dst connected |
| #1631 Path with Minimum Effort | Kruskal's stopping when src-dst connected |
| #684 Redundant Connection | Kruskal's detecting the cycle-creating edge |

The minimax path problems (#778, #1631) use a KEY theorem from MST theory:
**the minimax path between any two nodes lies entirely on the MST.**
Kruskal's finds this path implicitly by stopping when the two nodes connect.

---

## Complexity

| | |
|--|--|
| Time | O(n²) — Prim's with array |
| Space | O(n) — minDist + inMST arrays |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| n=1 | 0 | Single point, no connections needed |
| n=2 | Manhattan distance between them | One edge connects both |
| All same point | 0 | All distances are 0 |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Euclidean distance instead | √((x1-x2)²+(y1-y2)²) | Same algorithm, different dist formula |
| Max edge in MST | Bottleneck spanning tree | Same MST — max edge is the answer |
| k clusters (don't connect all) | Remove k-1 heaviest MST edges | Build MST, delete k-1 most expensive |
| Steiner tree | Must include fixed terminals | NP-hard in general |
