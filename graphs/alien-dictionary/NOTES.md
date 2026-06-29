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

## Phase 2 (Alternative) — DFS Postorder

DFS adds a character to the result AFTER all its successors are fully explored
(postorder). Reversing gives topological order.

**3-colour marking (same as Course Schedule):**
```
White (0): unvisited
Grey  (1): currently on DFS stack — being explored
Black (2): fully explored, confirmed no cycle
```

```java
// For each unvisited character, DFS
for char c in all chars:
    if colour[c] == WHITE:
        if !dfs(c): return ""  // cycle detected

// Reverse postorder = topological order
Collections.reverse(result)
return result

dfs(node):
    colour[node] = GREY        // currently exploring
    for each next in adj[node]:
        if colour[next] == GREY: return false  // back edge → cycle
        if colour[next] == WHITE:
            if !dfs(next): return false
    colour[node] = BLACK       // fully explored
    result.add(node)           // add AFTER all successors (postorder)
    return true
```

**Why postorder = reverse topological order?**
A character is added AFTER all characters that must come after it in the
alien alphabet. So characters with no successors (end of chains) are added
first, and characters that precede everything are added last.
Reversing gives the correct alien alphabet order.

```
Graph: w→e→r→t→f

DFS from w:
  grey(w) → grey(e) → grey(r) → grey(t) → grey(f)
  f has no successors → result=[f], black(f)
  back to t → result=[f,t], black(t)
  back to r → result=[f,t,r], black(r)
  back to e → result=[f,t,r,e], black(e)
  back to w → result=[f,t,r,e,w], black(w)

Reversed: [w,e,r,t,f] → "wertf" ✓
```

**Cycle detection in DFS:**
If DFS reaches a GREY node → back edge → directed cycle → return `""`.

```
Cycle example: ["z","x","z"]
Graph: z→x, x→z

DFS from z:
  grey(z) → grey(x) → x's neighbour is z → z is GREY → CYCLE → return "" ✓
```

---

## Why NOT Union-Find?

This is a directed ordering problem — character A comes BEFORE B.
DSU is symmetric: union(A,B) cannot model direction.
Use Kahn's BFS or DFS 3-colour for directed topological ordering.

---

## Comparison: Kahn's BFS vs DFS Postorder — Which Is More Apt?

**Kahn's BFS is the better choice for Alien Dictionary.** Here's why:

| Dimension | Kahn's BFS ✓ | DFS Postorder |
|-----------|-------------|---------------|
| Order built | Directly — no extra step | Reversed → must call `Collections.reverse()` |
| Cycle detection | `result.length() < inDegree.size()` — single check | Must track grey/white/black — 3-state colour array |
| Code clarity | Higher — linear flow | Lower — recursive + reverse + conversion overhead |
| Stack overflow risk | None (iterative queue) | Risk on deep chains (26 chars max here — safe) |
| Extra data structures | `inDegree` map + queue | `colour` map + `result` list + reversal |
| Handles isolated chars | Naturally (inDegree=0 → queue) | Naturally (white → DFS from it) |

**Why Kahn's wins specifically for this problem:**

1. **Cycle detection is simpler.** With Kahn's, one check at the end suffices:
   `result.length() != inDegree.size()`. With DFS, you need the 3-colour
   system and must propagate the `false` return through the call stack.

2. **No reversal needed.** Kahn's produces the answer in correct order as it runs.
   DFS postorder builds the REVERSE — you must reverse the result at the end.
   This is an extra O(V) step and a common source of bugs.

3. **Alphabet is small (≤ 26 characters).** The main advantage of DFS over Kahn's
   in other problems is handling very deep dependency chains without a queue.
   Here the graph has at most 26 nodes — stack overflow is impossible either way.
   So DFS's iterative-safety advantage doesn't apply.

4. **Interview clarity.** Kahn's maps directly to the mental model: "process
   characters with no prerequisites first, unlock others as you go" — same
   intuition as the problem statement's sorted dictionary structure.

**When DFS would be preferred:**
- You're already using DFS for graph construction and want a single traversal
- You need to detect cycles during exploration (not just at the end)
- The problem also asks for strongly connected components (Kosaraju/Tarjan)

**Bottom line for interviews:** Implement Kahn's BFS. Mention DFS as an
alternative and explain the reversal requirement — that shows depth of understanding.

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
