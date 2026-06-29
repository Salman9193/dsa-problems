# Alien Dictionary — Notes & Intuition

**LeetCode #269** | Graphs / Topological Sort | Hard

---

## Problem

Given a sorted list of words in an alien language, infer the character
ordering. Return any valid ordering, or `""` if impossible (cycle).

```
["wrt","wrf","er","ett","rftt"]  →  "wertf"
["z","x"]                         →  "zx"
["z","x","z"]                     →  ""   (cycle)
["abc","ab"]                      →  ""   (invalid: longer before prefix)
```

---

## Two-Phase Algorithm

```
Phase 1: Build ordering graph
  → Compare adjacent word pairs
  → First differing character = one ordering constraint (directed edge)

Phase 2: Topological sort (Kahn's BFS or DFS postorder)
  → Valid ordering = topological order of constraint graph
  → Cycle = impossible → return ""
```

---

## Phase 1 — Building the Graph

For each adjacent pair `(w1, w2)`:

```java
for each position j:
    if w1[j] != w2[j]:
        add edge w1[j] → w2[j]  // w1[j] comes before w2[j]
        BREAK                     // only first difference matters
```

**Why break after first difference?**
Only the first differing character position determines word order.
Characters after the first mismatch are irrelevant — they don't affect
which word sorts first.

```
"wrt" vs "wrf":  t and f at position 2 → t < f
                 Nothing beyond position 2 tells us anything.
```

**Critical edge case — longer word before its prefix:**
```
["abc","ab"] → "ab" is a prefix of "abc" but comes AFTER it → INVALID
if (w1.startsWith(w2) && w1.length() > w2.length()) return ""
```

**Why initialise ALL characters (even isolated ones)?**
A character with no ordering constraints still needs inDegree=0 in Kahn's
queue — otherwise it would be missing from the result.

---

## Phase 2 — Kahn's BFS Topological Sort

```java
Queue: all chars with inDegree == 0
while queue not empty:
    c = dequeue; result += c
    for each next in adj[c]: if --inDegree[next]==0: enqueue(next)

if result.length() < total_chars: cycle → return ""
```

---

## Full Trace — `["wrt","wrf","er","ett","rftt"]`

```
Adjacent pair comparisons:
  wrt vs wrf: t→f
  wrf vs er:  w→e
  er  vs ett: r→t
  ett vs rftt: e→r

Graph: w→e→r→t→f
inDegree: {w:0, e:1, r:1, t:1, f:1}

Kahn's: [w]→[e]→[r]→[t]→[f]
Result: "wertf" ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Single word | all chars in word | No ordering constraints possible |
| `["abc","ab"]` | `""` | Longer word before its prefix — invalid |
| `["z","x","z"]` | `""` | Cycle: z→x and x→z |
| All same chars | any order | No constraints between chars |
| No adjacent differences | any order | Words share prefixes throughout |

---

## Why NOT Union-Find?

This is a directed ordering problem — character A comes BEFORE B.
DSU is symmetric: union(A,B) cannot model direction.
Use Kahn's BFS or DFS 3-colour for directed topological ordering.

---

## Comparison: Kahn's vs DFS

| | Kahn's BFS | DFS Postorder |
|--|-----------|--------------|
| Cycle detection | `result.length() < total_chars` | Grey node hit |
| Order built | Directly (correct order) | Reversed (must reverse at end) |
| Code clarity | Higher | Lower (reverse + recursion) |
| Stack overflow risk | No | Yes for deep dependency chains |

Kahn's preferred for this problem.

---

## Complexity

| | |
|--|--|
| Time | O(C) — C = total characters across all words |
| Space | O(1) extra — at most 26 nodes and 26² edges (fixed alphabet) |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Multiple valid orderings | Return all | Backtrack from Kahn's |
| Lexicographically smallest | Among valid orderings | Priority queue in Kahn's |
| k-character alphabet | Larger alphabet | Same algorithm, adjust size |
| Verify given ordering | Is a given order valid? | Build edges, check consistency |
| With word frequencies | Weighted preference | Weighted topological sort |

---

## Connection to Other Problems

| Problem | Connection |
|---------|-----------|
| #207 Course Schedule | Same: cycle detection in directed graph |
| #210 Course Schedule II | Same: topological order output |
| #802 Find Eventual Safe States | Same: directed graph, DFS 3-colour |
| #269 Alien Dictionary (this) | Same + graph construction from data |

The extra challenge here is **building the graph correctly** — once built,
it reduces to standard Course Schedule II.
