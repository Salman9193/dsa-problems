# Task Scheduler — Notes & Intuition

**LeetCode #621** | Greedy / Heap | Medium
Identical tasks need a cooldown of `n` intervals between them. Find the minimum total time.

---

## Problem

Given task labels and an integer `n` (the cooldown between two *identical* tasks), each task
takes 1 unit. The CPU can idle. Return the **minimum** number of intervals to finish all.

```
Input:  tasks = ["A","A","A","B","B","B"], n = 2
Output: 8      →  A B idle A B idle A B
```

---

## Approach 1 — Greedy Max-Heap Simulation (the intuitive one)

At every tick, run the **most frequent available** task — that's the greedy choice, because
the highest-count task is the bottleneck and must be started as early and often as possible.

```
max-heap of remaining counts
repeat until heap empty:
    pop up to (n + 1) tasks       // one full cooldown cycle
    decrement each, remember them
    push back those with count > 0
    time += (heap empty ? tasks popped : n + 1)   // last cycle needs no trailing idle
```

**O(T log 26)** ≈ O(T) time (heap holds ≤ 26 letters), O(26) space. This mirrors what a real
scheduler does — see the [Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler).

---

## Approach 2 — The Greedy Math Formula (the "aha")

You don't need to simulate. Think of the **most frequent task as a skeleton of frames**:

```
tasks = A×3, B×3, n = 2      maxFreq = 3

A _ _ | A _ _ | A          ← (maxFreq - 1) frames of width (n + 1), then a final A
```

- There are `maxFreq − 1` complete frames, each of width `n + 1`.
- The tail is the final run of every task tied for the max → `countOfMaxFreq`.
- Everything else **fills the idle gaps**.

```
answer = max( tasks.length,  (maxFreq - 1) * (n + 1) + countOfMaxFreq )
```

**Why the `max(...)`:** if there are so many *distinct* tasks that they overflow the gaps,
there are **no idles at all** and the answer is simply `tasks.length`. The formula only
governs the idle-bound case.

```java
int[] freq = new int[26];
for (char c : tasks) freq[c - 'A']++;
int maxFreq = 0, countMax = 0;
for (int f : freq) {
    if (f > maxFreq) { maxFreq = f; countMax = 1; }
    else if (f == maxFreq) countMax++;
}
return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + countMax);
```

**O(T)** time, **O(1)** space.

---

## Full Trace — `["A","A","A","B","B","B"]`, `n = 2`

`freq: A=3, B=3` → `maxFreq = 3`, `countMax = 2` (A and B tie).

```
(maxFreq - 1) * (n + 1) + countMax
= (3 - 1) * 3 + 2
= 6 + 2 = 8
max(6 tasks, 8) = 8            →  A B idle | A B idle | A B   ✓
```

Contrast `["A","A","A","B","B","B"], n = 0`: formula gives `(2)(1) + 2 = 4`, but
`tasks.length = 6` → answer **6** (no cooldown, no idles). That's the `max` earning its keep.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Max-heap simulation | O(T log 26) ≈ O(T) | O(26) |
| **Greedy formula** | **O(T)** | **O(1)** |

---

## Edge Cases

| Case | Result |
|------|--------|
| `n = 0` | `tasks.length` (no cooldown → no idles) |
| all tasks distinct | `tasks.length` (they fill each other's gaps) |
| one task type, count `k` | `(k−1)·(n+1) + 1` |
| many ties at max freq | `countMax` extends the tail |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| **Return the actual schedule** | not just the count | heap simulation, recording each tick |
| Tasks with different durations | non-unit cost | becomes a harder scheduling problem |
| Multiple CPUs | parallel machines | list scheduling (Graham) — see Research |
| Task **dependencies** | DAG ordering | topological sort ([Course Schedule II](#graphs/course-schedule-ii)) |
| Priorities / deadlines | ordering policy | priority queue; EDF |

**The through-line:** greedily run the most frequent task (heap), or skip the simulation
entirely — the max-frequency task lays out `(maxFreq−1)` frames of width `(n+1)` plus a tail,
and everything else fills the idles: `max(len, (maxFreq−1)(n+1) + countMax)`.

> **Real-world implementation:** this cooldown logic is the rate-limit/backoff rule inside a
> real scheduler — see the
> [Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler), whose
> ready-queue is the same max-heap greedy.
