# Find Eventual Safe States — Notes & Intuition

**LeetCode #802** | Graphs / DFS / Cycle Detection | Medium

---

## Problem

A node is **safe** if every path from it leads to a terminal node (no outgoing edges).
A node is **unsafe** if it is on a cycle OR has any path leading to a cycle.
Return all safe nodes sorted.

```
graph = [[1,2],[2,3],[5],[0],[5],[],[]]
Output: [2,4,5,6]
```

---

## Core Insight

```
Safe   = no cycle reachable from this node
Unsafe = lies on a cycle OR has a path to a cycle

Terminal node → always safe (no outgoing edges → no cycle possible)
Node on cycle → always unsafe
Node leading to cycle → unsafe even if not IN the cycle
```

---

## Approach 1 — DFS with 3-Colour Marking

```
White (0): unvisited
Grey  (1): currently on DFS stack — "being explored right now"
Black (2): fully explored AND confirmed safe
```

```java
isSafe(node):
  if grey:  return false  // currently on path → cycle → unsafe
  if black: return true   // already confirmed safe

  mark GREY
  for each next in graph[node]:
      if !isSafe(next): return false  // DON'T mark black — leave grey
  mark BLACK  // all paths safe
  return true
```

**Why NOT mark black on unsafe return?**

If node A is unsafe (leads to cycle) and we incorrectly mark it black, then
another path C→A would see `black(A)` and conclude A is safe — WRONG.
By leaving A grey, any future visit to A immediately returns false.

---

## DFS vs Reverse Kahn's — Full Comparison

| | Recursive DFS | Iterative DFS | Reverse Kahn's |
|--|--------------|--------------|----------------|
| Space | O(V) | O(V) | O(V+E) |
| Stack overflow risk | YES | No | No |
| Builds reverse graph | No | No | YES |
| Early termination | YES | YES | No |
| Code complexity | Low | Medium | Medium |
| Best for | Shallow/sparse | Deep graphs | Deep + no extra DFS |

---

## Stack Overflow Risk in DFS — When and How to Fix

**When it occurs:** Java default stack is ~512 frames. A chain graph
`0→1→2→...→99999` causes DFS to recurse 100,000 levels deep → **StackOverflowError**.

**Fix: Iterative DFS with explicit stack**

```java
Deque<int[]> stack; // {node, childIndex}
// On entry (childIndex==0): check colour, mark grey
// On exit (childIndex==children.count): mark black, pop
// Advance childIndex each iteration to simulate recursive loop
```

This simulates the call stack exactly — same logic, zero recursion.

---

## Why NOT Union-Find?

DSU is symmetric — it cannot model directed edges:

```
Directed path: A → B → C  (valid, no cycle)
DSU: union(A,B), union(B,C) → find(A)==find(C) → "same component"
This is WRONG — not a cycle, just transitivity.

Also: node D→cycle is UNSAFE even though D is not IN the cycle.
DSU tracks membership, not "can reach a cycle" — it can't express this.
```

**Rule:** For directed graphs, use DFS (3-colour) or Kahn's BFS.

---

## Approach 2 — Reverse Graph + Kahn's BFS

Reverse all edges. Terminal nodes become source nodes (out-degree 0 → in-degree 0 in reverse). BFS from these sources propagates safety backward.

```java
// When a node's out-degree in ORIGINAL drops to 0 → all successors safe → it's safe
for each terminal: queue.offer(terminal)
while queue not empty:
    node = dequeue; safe[node]=true
    for each predecessor in reverse graph:
        if --outDegree[prev]==0: queue.offer(prev)
```

**Trade-off:** Requires O(V+E) extra space for reverse adjacency list, but no recursion risk.

---

## Full Trace — `[[1,2],[2,3],[5],[0],[5],[],[]]`

```
Nodes: 0→{1,2}, 1→{2,3}, 2→{5}, 3→{0}, 4→{5}, 5→{}, 6→{}

DFS path for node 0:
  grey(0)→grey(1)→grey(2)→grey(5): 5 has no children → black(5)=safe
  ← back to 2: all children safe → black(2)=safe
  ← back to 1: grey(3)→grey(0): 0 is GREY → CYCLE! return false
    3 stays grey, 1 stays grey, 0 stays grey

Node 4: grey(4)→5 is black(safe) → black(4)=safe
Node 6: no children → black(6)=safe

Safe set: {2,4,5,6} → [2,4,5,6] ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| All terminals | all nodes | No edges → all safe |
| Single cycle | [] | All nodes unsafe |
| Self-loop `[[0]]` | [] | 0→0 is a cycle |
| Linear chain ending at terminal | all nodes | No cycle |
| Node leading to cycle but not in it | unsafe | Reachability, not membership |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #207 Course Schedule | Cycle exists at all? | Same 3-colour DFS |
| #210 Course Schedule II | Return topological order | Kahn's BFS on same structure |
| Count safe nodes | Return count not list | Same algorithm, count instead |
| Weighted safety | Cost to reach terminal | Dijkstra-like priority search |
| Online: new edge added | Dynamic graph | Re-run from affected nodes |
