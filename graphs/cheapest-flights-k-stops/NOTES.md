# Cheapest Flights Within K Stops — Notes & Intuition

**LeetCode #787** | Graphs / Bellman-Ford | Medium

---

## Problem

Find the cheapest price from src to dst using at most k stops (k+1 edges).
Return -1 if impossible.

```
flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]]
src=0, dst=3, k=1  →  700  (0→1→3: 100+600=700, 1 stop)
```

---

## Why NOT Standard Dijkstra

Dijkstra finds the globally cheapest path regardless of hop count. A
path with more hops but lower cost would be selected over a path with
fewer hops but higher cost — the hop constraint is completely ignored.

```
Example: src=0, dst=3, k=1
  Path 0→1→3: 2 edges, 1 stop, cost 700
  Path 0→1→2→3: 3 edges, 2 stops, cost 400 (cheaper but 2 stops!)
  Standard Dijkstra returns 400 — WRONG for k=1
  K-limited Bellman-Ford returns 700 — CORRECT
```

---

## Why NOT Plain BFS

BFS counts hops, not costs. It cannot compare weighted paths.

```
At BFS level 1 (1 stop):
  Direct flight 0→3: not available
  BFS explores all 1-stop neighbours by hop count only

BFS has no way to prefer cost 200 over cost 500 between two paths
that both use the same number of hops — it treats edges as weight 1.

BFS is only correct for UNWEIGHTED graphs where hop count = cost.
For weighted graphs with a hop constraint: use Bellman-Ford.
```

---

## K-Limited Bellman-Ford — The Right Tool

Each Bellman-Ford pass relaxes ALL edges, extending all paths by one edge:

```
Pass 1: cheapest path using ≤ 1 edge (0 stops)
Pass 2: cheapest path using ≤ 2 edges (1 stop)
Pass k+1: cheapest path using ≤ k+1 edges (k stops)
```

**The temp snapshot — why it is CRITICAL:**

```java
int[] temp = Arrays.copyOf(dist, n); // snapshot BEFORE pass

for each flight (from, to, price):
    if dist[from] + price < temp[to]:  // READ from dist (prev pass)
        temp[to] = dist[from] + price  // WRITE to temp (this pass)

dist = temp
```

Without the snapshot, a relaxation in pass i could immediately feed
into another relaxation in the SAME pass — consuming 2 edges in one
"pass." The snapshot strictly enforces: each pass = exactly one
additional edge used.

---

## Algorithm Comparison

| | BFS | Bellman-Ford | Modified Dijkstra |
|--|-----|-------------|------------------|
| Weighted edges | ✗ No | ✓ Yes | ✓ Yes |
| Hop constraint | ✓ Natural | ✓ Via k passes | ✓ Via (node,stops) state |
| Space | O(V) | O(V) | O(k×V) |
| Time | Wrong for weighted | O(k×E) | O(k×E log kE) |
| Optimal? | No | ✓ Proven (ESA 2023) | No |

**Bellman-Ford is the clear winner** — less space than Dijkstra, correct
unlike BFS, and provably optimal for this problem class.

---

## Full Trace — `k=1`

```
Initial: dist=[0, INF, INF, INF]

Pass 1: temp=[0, INF, INF, INF]
  [0→1, 100]: temp[1]=100
  (all others skip — dist[from]=INF)
  dist=[0, 100, INF, INF]

Pass 2: temp=[0, 100, INF, INF]
  [1→2, 100]: dist[1]=100 → temp[2]=200
  [1→3, 600]: dist[1]=100 → temp[3]=700
  dist=[0, 100, 200, 700]

return dist[3]=700 ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| src==dst | 0 | Already there |
| k=0 | direct flight only, or -1 | Only 1 edge allowed |
| No path within k stops | -1 | dist[dst] stays INF |
| k ≥ n-1 | standard cheapest path | Unconstrained (n-1 edges sufficient) |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return actual path | Track predecessors | Store parent per (node, pass) |
| Negative edge weights | Prices can be negative | Full Bellman-Ford (k=n-1 passes) |
| Exact k stops (not at most) | Must use exactly k+1 edges | Remove early exit; use separate `exact_k` pass |
| Multiple sources | Broadcast from many origins | Add all sources to dist at pass 0 |
| #743 Network Delay Time | No hop constraint | Standard Dijkstra |
| #1631 Path Min Effort | Max not sum | Modified Dijkstra with max() |
