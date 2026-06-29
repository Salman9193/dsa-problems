# Course Schedule II — Notes & Intuition

**LeetCode #210** | Graphs / Topological Sort | Medium

---

## Problem

Same as Course Schedule I (#207), but return the actual topological ordering.
Return empty array `[]` if impossible (cycle exists).

```
prerequisites=[[1,0]]                      →  [0,1]
prerequisites=[[1,0],[2,0],[3,1],[3,2]]    →  [0,1,2,3] or [0,2,1,3]
prerequisites=[[1,0],[0,1]]                →  []  (cycle)
```

---

## Relation to Course Schedule I (#207)

| | #207 Course Schedule | #210 Course Schedule II |
|--|---------------------|------------------------|
| Return | `boolean` | `int[]` order |
| Kahn's change | `return processed==n` | record `order[idx++]=course` |
| DFS change | `return !hasCycle` | `result.add(node)` postorder + reverse |

The algorithms are **identical** — only what we accumulate differs.

---

## Approach 1 — Kahn's BFS (Preferred)

```java
// Same as #207, but record each dequeued course:
order[idx++] = course;
// Return order if no cycle, empty array if cycle
return idx == numCourses ? order : new int[0];
```

**Why Kahn's is preferred here:**
- Order produced **directly** during BFS — no reversal needed
- Cycle detection is natural: `idx < numCourses` → cycle
- Iterative — no call-stack depth risk
- BFS **levels** give minimum semesters (parallel rounds)

---

## Approach 2 — DFS Postorder

```java
// Add node AFTER all its dependents are fully explored:
colour[node] = 2;
result.add(node);  // postorder

// Reverse at end to get topological order
Collections.reverse(result);
```

**Why postorder gives REVERSE topological order:**
```
Graph: 0→1→3, 0→2→3

DFS explores deepest first:
  3 has no dependents → added first
  1, 2, 0 added progressively
  Postorder: [3, 1, 2, 0]
  Reversed:  [0, 2, 1, 3]  ✓ correct order
```

---

## Kahn's BFS Levels = Minimum Semesters

Each BFS level = one semester where all courses can be taken simultaneously.

```
Level 0 (Semester 1): all courses with inDegree=0  → no prerequisites
Level 1 (Semester 2): courses unlocked after Level 0
Level k (Semester k+1): courses needing exactly k levels of prerequisites
```

Number of BFS levels = minimum semesters = answer to **#1136 Parallel Courses**.

---

## Full Trace — `[[1,0],[2,0],[3,1],[3,2]]`, numCourses=4

**Kahn's:**
```
Graph: 0→1, 0→2, 1→3, 2→3
inDegree: [0,1,1,2]

Level 0: Queue=[0]
  dequeue 0 → order=[0]; reduce 1→0, 2→0

Level 1: Queue=[1,2]
  dequeue 1 → order=[0,1]; reduce 3→1
  dequeue 2 → order=[0,1,2]; reduce 3→0

Level 2: Queue=[3]
  dequeue 3 → order=[0,1,2,3]

return [0,1,2,3] ✓
3 levels → minimum 3 semesters
```

**DFS:**
```
dfs(0)→grey: dfs(1)→grey: dfs(3)→grey: 3 done → [3], black
  1 done → [3,1], black
  dfs(2)→grey: 3 black (skip) → [3,1,2], black
0 done → [3,1,2,0]
Reversed: [0,2,1,3] ✓  (also valid)
```

---

## Comparison

| Feature | Kahn's BFS | DFS Postorder |
|---------|-----------|--------------|
| Order built | Directly during BFS | Built reversed, then reversed again |
| Cycle detection | `idx < numCourses` | Grey node encountered |
| Stack overflow risk | No (iterative) | Yes (deep chains) |
| BFS levels available | Yes (min semesters) | No |
| Code complexity | Lower | Higher (reverse + conversion) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| No prerequisites | Any permutation of [0..n-1] | All independent |
| Single course | [0] | No dependencies |
| Cycle exists | [] | No valid ordering |
| Linear chain 0→1→2→...→n | [0,1,2,...,n] | Only one valid order |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #1136 Parallel Courses | Min semesters (parallel rounds) | Count BFS levels in Kahn's |
| #269 Alien Dictionary | Build ordering from words | Topological sort on characters |
| All valid orderings | Return every valid sequence | Backtracking with in-degree tracking |
| Lexicographically smallest | Among all valid orders | Priority queue instead of regular queue in Kahn's |
| With weights (task durations) | Critical path | Topo sort + DP on longest path |
