# Path with Minimum Effort — Notes & Intuition

**LeetCode #1631** | Graphs / Dijkstra's / Binary Search / DSU | Medium

---

## Problem

Find the path from top-left to bottom-right of a height grid that minimises
the MAXIMUM absolute height difference between consecutive steps.

```
heights = [[1,2,2],[3,8,2],[5,3,5]]  →  2
heights = [[1,2,3],[3,8,4],[5,3,5]]  →  1
```

---

## This is the Minimax Path Problem

Standard Dijkstra minimises the **sum** of edge weights.
This problem minimises the **maximum** edge weight on any path.

```
Standard:   dist[next] = dist[curr] + weight        (sum)
This:       dist[next] = max(dist[curr], |Δheight|) (max)
```

This objective — minimise the worst edge along a path — is called the
**minimax path problem** (or bottleneck shortest path problem).

---

## Why max() Is a Valid Dijkstra Relaxation

Dijkstra's correctness relies on ONE property: cost is non-decreasing
as the path extends. Both sum and max satisfy this when weights ≥ 0:

```
max(dist[curr], newEdge) ≥ dist[curr]  (max never decreases — ✓)
dist[curr] + newEdge ≥ dist[curr]      (sum never decreases — ✓)
```

So the greedy invariant holds: the SMALLEST value popped from the
priority queue is finalized and optimal. Early termination on the
first pop of the destination is therefore correct.

---

## Three Approaches

### 1. Modified Dijkstra (Preferred)

```java
pq = min-heap by effort
dist[0][0] = 0; all others = INF

while pq not empty:
    (effort, r, c) = pq.poll()
    if (r,c) == destination: return effort  // early exit
    if effort > dist[r][c]: continue        // stale
    for each neighbour (nr, nc):
        newEffort = max(effort, |heights[nr][nc] - heights[r][c]|)
        if newEffort < dist[nr][nc]:
            dist[nr][nc] = newEffort
            pq.offer({newEffort, nr, nc})
```

**Why preferred:** single pass, early termination, direct Dijkstra adaptation.

### 2. Binary Search + BFS

Binary search on the answer k: does a path exist using only edges with
`|Δh| ≤ k`? If yes → try smaller; if no → try larger. BFS/DFS checks
feasibility.

**When to prefer:** when the feasibility check is much simpler to code
than the optimisation (here they're similar complexity).

### 3. Kruskal's MST + Union-Find

Sort all edges by `|Δh|`. Add them one by one via Union-Find. Return the
weight of the first edge that connects source and destination.

**Why this works:** the minimax path between two nodes lies entirely
within the MST. So the first Kruskal edge that connects source and
destination IS the minimax bottleneck.

---

## The MST-Minimax Path Theorem (Key Insight)

A minimum bottleneck spanning tree (MBST) contains all minimax paths.
Any MST is also an MBST. Therefore:

```
minimax path weight = weight of the maximum edge on the
                      unique path between the two nodes in the MST
```

Kruskal's processes edges in increasing weight order — the first edge
that connects source and destination in Kruskal's construction is exactly
this maximum edge.

---

## Comparison

| Approach | Time | Space | Highlights |
|----------|------|-------|-----------|
| Modified Dijkstra | O(mn log mn) | O(mn) | Single pass, early exit — **preferred** |
| Binary Search + BFS | O(mn log H) | O(mn) | Separates feasibility from optimisation |
| Kruskal's DSU | O(mn log mn) | O(mn) | Elegant MST insight, slightly more setup |

Where H = max height value (up to 10^6).

---

## Full Trace — `[[1,2,2],[3,8,2],[5,3,5]]`

```
Modified Dijkstra path found:
(0,0)→(0,1)→(0,2)→(1,2)→(2,2)
efforts: max(0,1)=1, max(1,0)=1, max(1,0)=1, max(1,3)=3 → total worst=3? 

Actually better path:
(0,0)→(1,0)→(2,0)→(2,1)→(2,2)
efforts: |3-1|=2, |5-3|=2, |3-5|=2, |5-3|=2 → max=2 ✓

Dijkstra correctly discovers this path first → return 2 ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| 1×1 grid | 0 | Already at destination |
| All same height | 0 | Every step has 0 effort |
| Strictly increasing | last-first | Only one path available |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Maximise minimum edge (widest path) | Maximise bottleneck | Same Dijkstra, flip to max-heap, relax with min() |
| 3D terrain | Add depth dimension | Same with 6 directions |
| Weighted turns | Different cost for direction changes | Add direction to state |
| K-path minimum effort | Best of k paths | Top-k Dijkstra variant |
