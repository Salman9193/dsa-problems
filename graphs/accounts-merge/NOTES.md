# Accounts Merge — Notes & Intuition

**LeetCode #721** | Graphs / Union-Find | Medium

---

## Problem

Merge accounts belonging to the same person — two accounts belong to the
same person if they share at least one email. Return merged accounts with
sorted emails, name first.

```
accounts = [
  ["John","johnsmith@mail.com","john_newyork@mail.com"],
  ["John","johnsmith@mail.com","john00@mail.com"],
  ["Mary","mary@mail.com"],
  ["John","johnsmith@mail.com"]
]
→ [
  ["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],
  ["Mary","mary@mail.com"]
]
```

---

## Core Insight — Emails Are the Nodes, Not Names

Names are unreliable: multiple different people can share a name. The
same person's accounts are identified ONLY through shared emails.

**The DSU/graph nodes must be emails — not account indices, not names.**

---

## Approach 1 — Union-Find (Preferred)

```
1. Assign each unique email an integer id
2. For each account, union account[1] (first email) with every other
   email in that account
3. Group all emails by their DSU root
4. Sort each group, prepend the person's name
```

**Why union WITHIN each account works for cross-account merging too:**

```
Account A: {x, y}  → union(x, y)
Account B: {y, z}  → union(y, z)

DSU transitivity: x and z end up in the SAME component automatically,
even though no single account explicitly lists both x and z.
This is exactly how connected components capture indirect relationships.
```

---

## Approach 2 — BFS/DFS on Explicit Email Graph

Build an adjacency list (star graph per account, centred on the first
email), then find connected components via BFS/DFS.

```java
// Build: connect all emails in an account to that account's first email
// Traverse: BFS/DFS each unvisited email to find its full component
```

---

## DSU vs BFS/DFS — Which to Use?

| | DSU | BFS/DFS |
|--|-----|---------|
| Time | O(NK α(NK)) | O(NK) — no α factor |
| Space | O(NK) — flat arrays | O(NK) — denser adjacency list |
| Code complexity | Lower — no graph construction | Higher — build graph, then traverse |
| Natural fit | Yes — "group by shared attribute" is the textbook DSU use case | Works, but requires an extra structural step |

**DSU is preferred** because:
1. No explicit graph needs to be built — `union()` calls do the grouping directly
2. The problem statement IS "merge accounts that share an attribute" — this maps 1:1 to Union-Find's purpose
3. Slightly less code, no adjacency list overhead

**BFS/DFS becomes preferable when:**
- You need the actual graph structure for something else (e.g. shortest "introduction path" between two emails)
- DSU isn't idiomatic in your environment

---

## Full Trace — Standard Example

```
ids: johnsmith=0, john_newyork=1, john00=2, mary=3

Account 1 (John): union(0,1) → {0,1}
Account 2 (John): union(0,2) → {0,1,2}
Account 3 (Mary): no union   → {3}
Account 4 (John): single email (id 0, already grouped)

Groups: {johnsmith,john_newyork,john00} and {mary}

Sorted + named:
  ["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"]
  ["Mary","mary@mail.com"]
```

---

## Complexity

| | |
|--|--|
| Time | O(NK log(NK)) — N accounts, K emails each; dominated by sorting |
| Space | O(NK) — id maps + DSU/graph structures |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Single account, single email | Unchanged | No merging needed |
| Two same-name accounts, no shared email | Two separate entries | Names alone don't merge |
| Long chain of shared emails (A-B, B-C, C-D) | All merged into one | DSU transitivity |
| All accounts share one email | Single merged group | Star-shaped component |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Merge by phone OR email | Multiple identifier types | Union on either match type |
| Weighted confidence merging | Probabilistic match scores | Threshold-based edge inclusion before DSU |
| Streaming accounts (online) | New accounts arrive over time | DSU handles incremental unions naturally |
| Detect conflicting merges | Same email, different declared identity | Flag during grouping, don't auto-merge |
| Very large scale (billions of records) | Single-machine DSU won't fit | Distributed connected components (Spark GraphX) |
