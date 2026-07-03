# Possible Bipartition — Notes & Intuition

**LeetCode #886** | Graphs / BFS / DFS / Union-Find | Medium

---

## Problem

Given n people and dislike pairs, can all people be split into 2 groups
where no two people who dislike each other are in the same group?

```
n=4, dislikes=[[1,2],[1,3],[2,4]]      →  true   ({1,4} and {2,3})
n=3, dislikes=[[1,2],[1,3],[2,3]]      →  false  (triangle — odd cycle)
n=5, dislikes=[[1,2],[2,3],[3,4],[4,5],[1,5]]  →  false  (odd cycle)
```

---

## Core Insight — This IS Bipartite Checking

"Split into 2 groups with no conflict within a group" = bipartite graph.

```
Build graph: edge between every pair that dislikes each other
Answer = true iff the dislike graph is bipartite
```

This problem reduces EXACTLY to Is Graph Bipartite (#785) with one
extra step: build the adjacency list from the dislike edge list.

---

## #886 vs #785 — The Only Difference

| | #785 Is Graph Bipartite? | #886 Possible Bipartition |
|--|--------------------------|--------------------------|
| Input | Adjacency list (ready) | Edge list (dislikes) |
| Graph build | Not needed | Build adj from dislikes |
| Bipartite check | Same | Same |
| Extra step | None | Build adj list first |

The bipartite check code is IDENTICAL in both problems.

---

## Three Approaches

### 1. BFS 2-Colouring (Preferred)

```java
colour[start] = 1
BFS: for each neighbour next of node:
    if colour[next] == 0: colour[next] = -colour[node], enqueue
    if colour[next] == colour[node]: return false  // conflict
```

### 2. DFS 2-Colouring

```java
dfs(node, colour c):
    colour[node] = c
    for each neighbour next:
        if colour[next] == c: return false   // conflict
        if colour[next] == 0: dfs(next, -c)
```

### 3. Union-Find

All neighbours of node u must be in the OPPOSITE group → they're all in
the SAME group as each other → union them all.

```java
for each node u:
    first = neighbours[0]
    for each v in neighbours:
        if find(u) == find(v): return false  // u in same group as neighbour
        union(first, v)                       // all neighbours same group
```

---

## Why Union-Find Is Useful Here

BFS/DFS requires the full graph structure. Union-Find shines when:

1. **Dynamic dislike additions:** new dislikes arrive in a stream — just
   call `union()` and check `find()` per new edge in O(α(n)), no rebuild.

2. **Only connectivity queries:** if you want to later ask "are persons
   A and B compatible?" Union-Find answers in O(α(n)).

3. **Space:** O(V) parent array vs O(V+E) adjacency list.

---

## Full Traces

**`n=4, dislikes=[[1,2],[1,3],[2,4]]` (BFS)**
```
BFS from 1 (colour=1):
  2 → colour=-1; 3 → colour=-1
  BFS from 2 (colour=-1):
    4 → colour=1; 1 already done
  All consistent → true ✓
Groups: {1,4} and {2,3}
```

**`n=3, dislikes=[[1,2],[1,3],[2,3]]` (odd triangle)**
```
BFS from 1 (colour=1):
  2 → colour=-1; 3 → colour=-1
  BFS from 2 (colour=-1):
    neighbour 3 has colour=-1 == colour[2]=-1 → CONFLICT → false ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(V+E) — BFS/DFS; O(E α(V)) — Union-Find |
| Space | O(V+E) — adjacency list + colour array |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| No dislikes | true | All in one group is valid |
| Self-dislike (rare) | false | Can't be in opposite group of yourself |
| Disconnected components | Check each component independently | BFS/DFS outer loop handles this |
| n=1 | true | Single person, trivially partitioned |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| k groups (k-colourable) | More than 2 groups | NP-hard for k≥3 (k-coloring) |
| Maximum split size | Constrain group sizes | Bipartite + constraint check |
| Dynamic dislikes | Edges added online | Union-Find handles incrementally |
| Return the actual groups | Not just boolean | Collect colour=1 and colour=-1 sets |
