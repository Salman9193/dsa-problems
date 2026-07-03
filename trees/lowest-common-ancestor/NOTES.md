# Lowest Common Ancestor of a Binary Tree — Notes & Intuition

**LeetCode #236** | Trees / DFS | Medium

---

## Problem

Given a binary tree and two nodes p and q, find the deepest node that has
both p and q as descendants. A node is a descendant of itself.

```
Tree: [3,5,1,6,2,0,8,null,null,7,4]
LCA(5, 1) = 3   (p and q in different subtrees of 3)
LCA(5, 4) = 5   (5 is ancestor of 4 — LCA is 5 itself)
```

---

## Core Insight — Three Cases

A node X is the LCA of p and q iff one of:

```
Case 1: p in X's LEFT subtree, q in X's RIGHT subtree (or vice versa)
Case 2: X == p AND q is somewhere in X's subtree
Case 3: X == q AND p is somewhere in X's subtree
```

Cases 2 and 3 are handled by the `if (root == p || root == q) return root`
early return — we don't need to search deeper since both p and q are
guaranteed to exist in the tree.

---

## Approach 1 — Recursive DFS (Preferred)

```java
if root == null:  return null
if root == p or root == q:  return root  // early return

left  = LCA(root.left,  p, q)
right = LCA(root.right, p, q)

if left != null AND right != null: return root  // case 1: split
return left != null ? left : right              // case 2/3: same subtree
```

**Return value semantics:**
- `null` = neither p nor q found in this subtree
- non-null = either (a) p or q found (for parent to combine), OR
             (b) THIS is the LCA (both found in different subtrees)

These two sub-cases are indistinguishable — and that's fine! Once the
LCA is found, it bubbles up unchanged all the way to the root.

---

## Approach 2 — Iterative with Parent Map

Build a parent pointer map via BFS/DFS. Walk p's ancestors into a set.
Walk q's ancestors until one is in p's set.

```
parent map: each node → its parent
ancestors(p) set: p, parent(p), parent(parent(p)), ..., root
Walk q upward: first node in ancestors(p) = LCA
```

**When to prefer:**
- Very deep trees (recursive O(h) stack → overflow risk)
- Multiple LCA queries on same tree (build map once, O(h) per query)

---

## Recursive vs Iterative

| | Recursive DFS | Iterative (parent map) |
|--|--------------|----------------------|
| Time | O(n) | O(n) |
| Space | O(h) — call stack | O(n) — parent map |
| Stack overflow | Yes (deep trees) | No |
| Multiple queries | Re-run O(n) each | Build O(n) once, O(h) per query |
| Code simplicity | Higher | Lower |

---

## LCA in BST vs LCA in Binary Tree

| | #235 LCA of BST | #236 LCA of Binary Tree |
|--|----------------|------------------------|
| Property used | BST ordering | None — must check both subtrees |
| Time | O(h) | O(n) |
| Direction | One path down tree | Full DFS |
| Code | 3 cases based on values | Postorder DFS |

---

## Full Traces

**`LCA(5, 1)` on `[3,5,1,...]`:**
```
dfs(3): left = dfs(5) → returns 5 (root==p)
        right = dfs(1) → returns 1 (root==q)
        both non-null → return root=3 ✓
```

**`LCA(5, 4)` on `[3,5,1,...,7,4]`:**
```
dfs(3): left = dfs(5) → returns 5 (root==p, early return)
        right = dfs(1) → returns null (4 not in right subtree)
        left=5, right=null → return left=5 ✓
```

---

## Node Distance Formula Using LCA

```
distance(u, v) = depth(u) + depth(v) - 2 × depth(LCA(u, v))
```

This gives the number of edges between any two nodes in a tree — useful
for distance queries, range sum on paths, and diameter computation.

---

## Advanced LCA Algorithms (Staff Engineer Level)

| Algorithm | Preprocessing | Query | Space | Best for |
|-----------|--------------|-------|-------|---------|
| Naive DFS (#236) | O(n) | O(n) | O(h) | Single query |
| Binary Lifting | O(n log n) | O(log n) | O(n log n) | Many queries, static tree |
| Sparse Table (RMQ) | O(n) | O(1) | O(n) | Many queries, read-only |
| Tarjan's offline | O(n α(n)) | O(α(n)) | O(n) | All queries given upfront |
| ekmett/lca (Haskell) | O(log h) | O(log h) | O(log h) | Dynamic/distributed trees |

---

## Complexity

| | |
|--|--|
| Time | O(n) — visits every node at most once |
| Space | O(h) — recursion stack depth = tree height |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| p is ancestor of q | p | p itself is the LCA |
| p == q | p | A node is LCA of itself |
| Root is p or q | root | root is ancestor of everything |
| Linear tree (skewed) | Correct, but O(n) stack | Use iterative to avoid overflow |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #235 LCA of BST | Can use BST property | O(h) without full search |
| LCA of multiple nodes | Find LCA of k nodes | Find LCA pairwise, or use Euler tour |
| LCA on DAG | Node can have multiple parents | More complex — see arXiv:2204.10932 |
| Many queries offline | Q pairs given upfront | Tarjan's offline LCA, O((V+Q) α(V)) |
| Many queries online | Queries arrive dynamically | Binary lifting O(n log n) prep, O(log n) query |
