# Network Delay Time — Notes & Intuition

**LeetCode #743** | Graphs / Dijkstra's Algorithm | Medium

---

## Problem

Given a weighted directed graph (network) and a source node k, find the
time for a signal starting at k to reach ALL nodes. Return -1 if any node
is unreachable.

```
times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2  →  2
times=[[1,2,1]], n=2, k=1                   →  1
times=[[1,2,1]], n=2, k=2                   →  -1
```

---

## Core Insight — Single-Source Shortest Path

This is the textbook Dijkstra's algorithm problem. Find the shortest path
from k to EVERY other node. The answer is the MAXIMUM of these shortest
paths — the time for the LAST node to receive the signal.

```
If ANY node has shortest distance = infinity (unreachable) → return -1
Otherwise → return max(dist[1], dist[2], ..., dist[n])
```

---

## Dijkstra's Algorithm — Why a Priority Queue

```java
pq.offer({k, 0})
while pq not empty:
    (node, d) = pq.poll()  // always the SMALLEST current distance
    if d > dist[node]: continue  // stale entry, skip
    for each (next, weight) in adj[node]:
        if dist[node]+weight < dist[next]:
            dist[next] = dist[node]+weight
            pq.offer({next, dist[next]})
```

**Why NOT plain BFS?** BFS processes nodes in insertion (FIFO) order — this
is only correct for UNWEIGHTED graphs. With weighted edges, a node reached
via a long path first could "finalize" wrongly before a shorter path is
found. The priority queue always pops the CURRENTLY smallest distance,
which is what makes Dijkstra's greedy choice provably correct (for
non-negative weights).

**Why "if (d > dist[node]) continue"?**
The same node can be pushed to the queue multiple times (once per
relaxation). This check skips stale, outdated entries — "lazy deletion" —
avoiding the need for an indexed priority queue with decrease-key support.

---

## Bellman-Ford Alternative

```java
for i in 0..n-2:           // relax all edges n-1 times
    for each edge (u,v,w):
        if dist[u]+w < dist[v]: dist[v] = dist[u]+w
```

**When to use instead of Dijkstra's:** if edge weights can be negative.
Dijkstra's greedy "process closest node next" assumption BREAKS with
negative weights — a negative edge discovered later could retroactively
shorten an already-finalized path. Bellman-Ford handles this correctly
(and can also detect negative cycles, which Dijkstra's cannot).

**Trade-off:** O(V·E) — much slower than Dijkstra's O(E log V).

---

## Comparison

| | Dijkstra's | Bellman-Ford |
|--|-----------|--------------|
| Time | O(E log V) | O(V·E) |
| Negative weights | Breaks | Handles correctly |
| Negative cycle detection | Cannot | Can (extra pass) |
| Network delay time fits? | Yes (delays are always ≥ 0) | Also correct, just slower |

---

## Full Trace — `times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2`

```
adj: 2->[(1,1),(3,1)], 3->[(4,1)]
dist = [_, INF, 0, INF, INF]

Pop (2,0): relax 1->dist[1]=1; relax 3->dist[3]=1
Pop (1,1): no outgoing edges
Pop (3,1): relax 4->dist[4]=2
Pop (4,2): no outgoing edges

dist = [_, 1, 0, 1, 2]
max = 2 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(E log V) — Dijkstra's with binary heap |
| Space | O(V + E) — adjacency list + dist array + priority queue |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| k has no outgoing edges, n>1 | -1 | Other nodes unreachable |
| n=1 | 0 | Single node, signal "arrives" instantly |
| Self-loop edge | Ignored in shortest path | Doesn't help reach other nodes |
| Disconnected graph | -1 | Some node has dist=INF |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #1334 Find the City With the Smallest Number of Neighbors | All-pairs shortest path | Floyd-Warshall (run Dijkstra's from each node, or Floyd-Warshall directly) |
| #787 Cheapest Flights Within K Stops | Limited number of edges | Modified Bellman-Ford (k+1 relaxation passes) |
| Negative weights possible | Edge weights can be negative | Bellman-Ford |
| A* search | Have a heuristic toward target | Dijkstra's + heuristic priority |
| All-pairs shortest path | Need distance between every pair | Floyd-Warshall O(V³), or Dijkstra's from each node O(V·E log V) |
