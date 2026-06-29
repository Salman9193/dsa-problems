# Course Schedule — Notes & Intuition

**LeetCode #207** | Graphs / Topological Sort / Cycle Detection | Medium

---

## Problem

Given `numCourses` and prerequisite pairs `[a, b]` (must take b before a),
can all courses be completed?

```
prerequisites=[[1,0]]        →  true   (0→1: take 0 then 1)
prerequisites=[[1,0],[0,1]]  →  false  (cycle: 0↔1)
```

---

## Core Insight — DAG Check

Scheduling is possible iff the prerequisite graph is a **DAG** (no cycles).
A cycle means A requires B requires ... requires A — impossible to start.

---

## Approach 1 — Kahn's Algorithm (BFS Topological Sort)

**Intuition:** Start with courses that have no prerequisites. Taking them
reduces the prerequisite count of their dependents. Repeat until either:
- All courses are processed → no cycle → `true`
- Some courses are stuck (always have prerequisites remaining) → cycle → `false`

```java
// Build in-degree array + adjacency list
for each [a, b]: adj[b].add(a); inDegree[a]++;

// Seed: all courses with no prerequisites
Queue: all i where inDegree[i] == 0

// Process
while queue not empty:
    course = dequeue; processed++
    for each next in adj[course]:
        if --inDegree[next] == 0: enqueue(next)

return processed == numCourses
```

**Cycle detection:** Nodes in a cycle NEVER reach in-degree 0 — they always
wait for each other. If `processed < numCourses`, a cycle exists.

---

## Approach 2 — DFS with 3-Colour Marking

```
White (0): unvisited
Grey  (1): currently on the DFS call stack
Black (2): fully explored (no cycle through here)
```

**Back edge = cycle:** DFS reaches a GREY node → we found a path back to
an ancestor currently being explored → directed cycle.

```java
for each unvisited node: if hasCycle(node): return false

hasCycle(node):
    colour[node] = GREY
    for each next in adj[node]:
        if next is GREY: return true   // back edge → cycle
        if next is WHITE and hasCycle(next): return true
    colour[node] = BLACK
    return false
```

**Why grey ≠ black matters:**
- Black node: fully explored, all paths from it terminate — safe to revisit
- Grey node: currently on DFS path — revisiting = we looped back = cycle

---

## Why NOT Union-Find?

DSU merges components **symmetrically** — it cannot model directed edges.

```
Directed path: A → B → C  (valid, no cycle)
DSU after union(A,B), union(B,C): find(A) == find(C) → reports "same component"

This is WRONG — A and C are not cyclic, just transitively connected.
DSU would incorrectly detect a cycle in any transitive directed path.
```

**Rule:** DSU detects cycles in **undirected** graphs only. For directed graphs,
use DFS (3-colour back-edge detection) or Kahn's BFS.

---

## Comparison

| Approach | Time | Space | Also gives order? | Cycle detection |
|----------|------|-------|------------------|-----------------|
| Kahn's BFS | O(V+E) | O(V+E) | Yes (processing order) | processed < V |
| DFS 3-colour | O(V+E) | O(V+E) | Yes (reverse postorder) | grey node reached |
| Union-Find | ✗ wrong | — | — | — |

---

## Full Traces

**`prerequisites=[[1,0],[2,0],[3,1],[3,2]]`, numCourses=4**

```
Graph: 0→1, 0→2, 1→3, 2→3
inDegree: [0,1,1,2]

Kahn's:
  Queue=[0] → process 0: inDegree[1]=0, inDegree[2]=0
  Queue=[1,2] → process 1: inDegree[3]=1
              → process 2: inDegree[3]=0
  Queue=[3] → process 3
  processed=4 == numCourses=4 → true ✓
```

**Cycle: `[[0,1],[1,0]]`, numCourses=2**

```
inDegree: [1,1]  — no node starts at 0
Queue=[] → processed=0 ≠ 2 → false ✓

DFS from 0: grey(0) → grey(1) → back to grey(0) → cycle ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| No prerequisites | true | All courses independent |
| Self-loop `[[0,0]]` | false | Course requires itself |
| Single course | true | No dependencies |
| Long chain, no cycle | true | Valid linear order |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #210 Course Schedule II | Return the actual order | Kahn's: return processing queue; DFS: return reverse postorder |
| #269 Alien Dictionary | Build order from character constraints | Same topological sort on characters |
| #1136 Parallel Courses | Min semesters (BFS levels) | Kahn's: count BFS levels |
| k prerequisites each | More edges | Same O(V+E) algorithm |
| Online: new prerequisite added | Dynamic graph | Rebuild adj + inDegree from scratch |
