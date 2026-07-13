# Parallel Courses III — Notes & Intuition

**LeetCode #2050** | Graphs / Topological Sort + DP | Hard
Courses have durations and prerequisites; any number can run in parallel. Minimum total months?

**This problem is literally the Critical Path Method (CPM) forward pass.**

---

## Problem

`n` courses, `relations[j] = [prev, next]` (prev must finish before next starts), and
`time[i]` = months to complete course `i+1`. Courses may start as soon as **all** their
prerequisites are done, and **any number can run simultaneously**. Return the minimum months
to complete everything. (The graph is guaranteed to be a **DAG**.)

```
n = 3, relations = [[1,3],[2,3]], time = [3,2,5]   →  8
  courses 1 (3mo) and 2 (2mo) start at month 0, in parallel
  course 3 waits for BOTH → starts at max(3,2) = 3, takes 5 → 8
```

---

## The Key Insight — It's the Longest Path in a DAG

Because unlimited courses run in parallel, the total time is **not** the sum of durations. A
course starts when its **slowest** prerequisite finishes. So the answer is dictated by the
**longest chain of dependencies weighted by duration** — the **critical path**.

```
finish[v] = time[v] + max( finish[u] : u is a prerequisite of v )
answer    = max over all v of finish[v]
```

That is the **CPM forward pass** (earliest finish times), and it's just a DP relaxation done in
**topological order**.

### Why topological order is the whole trick

The recurrence needs every prerequisite's `finish[]` to be **final** before computing `v`.
Topological order guarantees exactly that. And critically:

> **Longest path is NP-hard in a general graph** (it would solve Hamiltonian path), **but in a
> DAG it's O(V + E)** — because acyclicity means a topological order exists, so no value ever
> needs revisiting.

**Acyclicity is what buys you the DP.** That single sentence is the point of this problem.

---

## The Algorithm (Kahn's topological sort + relaxation)

```java
public int minimumTime(int n, int[][] relations, int[] time) {
    List<List<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    int[] indeg = new int[n];
    for (int[] r : relations) {
        adj.get(r[0] - 1).add(r[1] - 1);      // 1-indexed input
        indeg[r[1] - 1]++;
    }

    int[] finish = new int[n];
    Deque<Integer> q = new ArrayDeque<>();
    for (int i = 0; i < n; i++)
        if (indeg[i] == 0) { finish[i] = time[i]; q.offer(i); }   // no prereqs → start at 0

    int ans = 0;
    while (!q.isEmpty()) {
        int u = q.poll();
        ans = Math.max(ans, finish[u]);
        for (int v : adj.get(u)) {
            finish[v] = Math.max(finish[v], finish[u] + time[v]);   // ← the relaxation (max!)
            if (--indeg[v] == 0) q.offer(v);
        }
    }
    return ans;
}
```

**O(V + E)** time, **O(V + E)** space.

---

## The Pitfall — `max`, not "first prerequisite"

A course must wait for **ALL** prerequisites, so the relaxation takes a **max** over incoming
edges. Writing `finish[v] = finish[u] + time[v]` (overwriting rather than maxing) lets a course
start as soon as *any* one prerequisite finishes — the single most common bug here.

Equally: only enqueue `v` when its in-degree hits **0**, i.e. every prerequisite has been
relaxed into it. That's Kahn's algorithm doing double duty — it sequences the DP *and* enforces
"wait for all."

---

## Full Trace — `n=5, relations=[[1,5],[2,5],[3,5],[3,4],[4,5]], time=[1,2,3,4,5]`

| course | prereqs | finish |
|--------|---------|--------|
| 1 | — | 1 |
| 2 | — | 2 |
| 3 | — | 3 |
| 4 | 3 | 3 + 4 = **7** |
| 5 | 1,2,3,4 | max(1,2,3,7) + 5 = **12** |

Answer **12**. The **critical path is 3 → 4 → 5** (3+4+5 = 12); courses 1 and 2 have *slack*
and don't matter. ✓

---

## This Is CPM — the Vocabulary Maps Exactly

| CPM / project management | This problem |
|--------------------------|--------------|
| Activity | Course |
| Activity duration | `time[i]` |
| Precedence network (AON) | The DAG |
| **Forward pass** (earliest start/finish) | The `finish[]` relaxation |
| **Critical path** | The longest weighted path |
| **Project makespan** | `max(finish)` — the answer |
| **Slack / float** | Courses off the critical path |

### The backward pass (not needed here, but the other half of CPM)

Run the same relaxation in **reverse** topological order to get the **latest** each activity can
finish without delaying the project:

```
latestFinish[u] = min( latestFinish[v] - time[v] : v is a successor of u )
slack[u] = latestFinish[u] - finish[u]
```

**Activities with zero slack are exactly the critical path.** Everything with positive slack can
slip by that much, for free. This is what makes CPM *actionable* — see the
[Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler), which
computes both passes over its dependency DAG.

---

## Complexity

| | Time | Space |
|--|------|-------|
| Kahn topological sort + relaxation | **O(V + E)** | O(V + E) |
| Memoised DFS (equivalent) | O(V + E) | O(V + E) |

---

## Edge Cases

| Case | Result |
|------|--------|
| no relations | `max(time)` — everything runs in parallel |
| a single chain | the sum of all durations |
| multiple disconnected components | the max over components |
| cycle | impossible (the problem guarantees a DAG — a cycle would make it unsolvable) |

## The Family

| Problem | Adds |
|---------|------|
| [Course Schedule](#graphs/course-schedule) | can it be ordered? (cycle detection) |
| [Course Schedule II](#graphs/course-schedule-ii) | **an** order (topological sort) |
| **Parallel Courses III #2050** | **durations + parallelism → the critical path** |
| [LIS #300](#dynamic-programming/longest-increasing-subsequence) / [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) | longest chain in a poset (the unweighted cousin) |

**The through-line:** *linearize, then relax.* Every one of these is "topologically order the
DAG, then run a DP over it." Only the relaxation changes.
