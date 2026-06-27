# Word Ladder — Notes & Intuition

**LeetCode #127** | Graphs / BFS / Implicit Graph | Hard

---

## Problem

Find the shortest transformation sequence from `beginWord` to `endWord`,
changing exactly one letter at each step, with every intermediate word
in the `wordList`.

```
beginWord="hit", endWord="cog"
wordList=["hot","dot","dog","lot","log","cog"]
→ 5   (hit→hot→dot→dog→cog)

endWord not in wordList → 0
```

---

## The Implicit Graph Insight

Never build the graph explicitly. Instead:
- **Nodes:** words in wordList + beginWord
- **Edges:** generated on the fly — try all L × 26 single-letter substitutions
- **Query:** BFS shortest path from beginWord to endWord

```
hit ── hot ── dot ── dog ── cog
              |              |
              lot ── log ────┘
```

---

## Efficient Neighbour Generation — O(L × 26) per Word

```java
// NAIVE: compare with every word in dictionary → O(n × L)
for (String w : wordSet) if (diffByOne(word, w)) { ... }

// EFFICIENT: try all substitutions → O(L × 26)
for (int j = 0; j < chars.length; j++) {
    for (char c = 'a'; c <= 'z'; c++) {
        chars[j] = c;
        String next = new String(chars);
        if (wordSet.contains(next)) { ... }
    }
    chars[j] = original;  // always restore!
}
```

For small dictionaries (n < 26L), naive is fine.
For large dictionaries (n >> 26L), substitution approach wins.

---

## wordSet as Visited Set

```java
wordSet.remove(next); // on enqueue — prevents revisiting
```

This avoids a separate `HashSet<String> visited`. Correct because:
- BFS guarantees first-visit = shortest path
- Once reached, no shorter path to that word can exist
- Removing from wordSet prevents re-enqueueing

---

## Bidirectional BFS

Expand from both `beginWord` and `endWord` simultaneously.
Always expand the **smaller** frontier first.

```
Standard BFS:       explores sphere of radius r from beginWord
Bidirectional BFS:  two spheres of radius r/2, meet in middle
                    Nodes explored ≈ 2 × branching^(r/2) vs branching^r
```

When the two frontiers intersect: return `steps + 1`.

---

## Full Trace — `hit → cog`

```
wordSet = {hot,dot,dog,lot,log,cog}

Step 1: process "hit"
  "h_t": hot ✓ → enqueue, remove from wordSet
  queue=["hot"], steps→2

Step 2: process "hot"
  dot ✓, lot ✓ → enqueue both
  queue=["dot","lot"], steps→3

Step 3: process "dot","lot"
  dog ✓, log ✓ → enqueue both
  queue=["dog","log"], steps→4

Step 4: process "dog"
  "cog" == endWord → return 5 ✓
```

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Standard BFS | O(n × L × 26) | O(n × L) |
| Bidirectional BFS | O(n × L × 26 / bf) | O(n × L) |

n = dictionary size, L = word length, bf = branching factor.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| endWord not in wordList | 0 | Can never reach it |
| beginWord == endWord | 1 | Already there |
| No transformation path | 0 | Words are in disconnected components |
| Single-step transformation | 2 | beginWord and endWord differ by one letter |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #126 Word Ladder II | Return ALL shortest paths | BFS to build parent map; DFS to enumerate |
| Wildcard patterns | `*` matches any letter | Precompute wildcard → words map |
| Different alphabet | Not a-z | Change inner loop range |
| Edit distance > 1 | Allow k changes | BFS with state (word, changes_used) |
| Bidirectional (production) | Faster for large dicts | Always expand smaller frontier |

---

## Connection to Other Problems

| Problem | Connection |
|---------|-----------|
| #127 Word Ladder | BFS on implicit word graph |
| Shortest Path Binary Matrix (#1091) | BFS on explicit grid graph |
| Rotting Oranges (#994) | Multi-source BFS |
| 01 Matrix (#542) | Multi-source BFS distance matrix |

All four are BFS shortest-path problems — the difference is how the graph is represented (implicit vs explicit) and whether there are one or multiple sources.
