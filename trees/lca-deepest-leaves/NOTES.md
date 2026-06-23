# Lowest Common Ancestor of Deepest Leaves — Notes & Intuition

**LeetCode #1123** | Trees / DFS | Medium

---

## Problem

Return the lowest common ancestor (LCA) of all deepest leaves in a binary tree.

```
Input:    3               Deepest leaves: 7, 4 (depth 3)
         / \              Output: node 2
        5   1
       / \ / \
      6  2 0  8
        / \
       7   4
```

---

## Core Insight — Compare Subtree Heights

For any node, the LCA of its deepest leaves depends on the heights of its
left and right subtrees:

```
left_height == right_height → deepest leaves on BOTH sides → THIS node is LCA
left_height >  right_height → deepest leaves only on left  → propagate left's LCA
left_height <  right_height → deepest leaves only on right → propagate right's LCA
```

This gives a clean single-pass postorder DFS returning both the LCA and height.

---

## The "Return Two Values" DFS Pattern

Many tree problems require propagating two pieces of information up the tree.
The cleanest approaches:

1. **Instance variables** — store LCA in a field, return height as int
2. **Array/pair return** — `return new TreeNode[]{lca, heightSentinel}`
3. **Record/class** — `return new Result(lca, height)`

This problem uses approach 2 — returning `[lca_node, height_carrier]`.

---

## Algorithm

```java
TreeNode[] dfs(TreeNode node) {
    if (node == null) return new TreeNode[]{null, new TreeNode(0)};

    TreeNode[] l = dfs(node.left);
    TreeNode[] r = dfs(node.right);
    int lh = l[1].val, rh = r[1].val;

    TreeNode lca;
    if      (lh == rh) lca = node;   // this node is LCA
    else if (lh >  rh) lca = l[0];   // propagate left's LCA
    else               lca = r[0];   // propagate right's LCA

    return new TreeNode[]{lca, new TreeNode(Math.max(lh, rh) + 1)};
}
return dfs(root)[0];
```

---

## Why lh == rh → This Node Is LCA

If `lh == rh`, the globally-deepest leaves exist on both sides.
Any ancestor of ALL deepest leaves must be an ancestor of both the
deepest-left and deepest-right leaves. This node is the deepest such
ancestor → it IS the LCA by definition.

If `lh ≠ rh`, the deepest leaves are entirely in one subtree — this node
is not the LCA (it's a common ancestor but not the lowest one).

---

## Full Trace — `3→{5,1}`, `5→{6,2}`, `2→{7,4}`, `1→{0,8}`

| Node | lh | rh | lca | height |
|------|----|----|-----|--------|
| null | — | — | null | 0 |
| 7 | 0 | 0 | **7** | 1 |
| 4 | 0 | 0 | **4** | 1 |
| 2 | 1 | 1 | **2** | 2 |
| 6 | 0 | 0 | 6 | 1 |
| 5 | 1 | 2 | **2** (from right) | 3 |
| 0 | 0 | 0 | 0 | 1 |
| 8 | 0 | 0 | 8 | 1 |
| 1 | 1 | 1 | **1** | 2 |
| 3 | 3 | 2 | **2** (from left) | 4 |

Result: node 2 ✓

---

## Complexity

| | |
|--|--|
| Time | O(n) — one postorder traversal |
| Space | O(h) — recursion stack |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Single root | root | Only one node — both leaf and LCA |
| Right-skewed `1→2→3→4` | node 4 | Only one deepest leaf |
| Perfect binary tree (depth 3) | root | All leaves at same depth — root is LCA |
| Two deepest leaves, different subtrees | lowest common parent | Standard LCA case |

---

## Related Problems

| Problem | Connection |
|---------|-----------|
| #236 LCA of Binary Tree | LCA of two specific nodes |
| #865 Smallest Subtree With All Deepest Nodes | Same problem, different name |
| **#1123 LCA of Deepest Leaves** | LCA of all deepest leaves |
| #104 Maximum Depth of Binary Tree | Core subroutine used here |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| LCA of two specific nodes (#236) | Given two specific nodes | Classic LCA: DFS tracking both nodes |
| LCA in binary lifting | Preprocess for O(log n) queries | Binary lifting table: ancestor[node][k] = 2^k-th ancestor |
| LCA in n-ary tree | k children per node | Same postorder DFS; iterate children |
| Deepest node (single) | Return the single deepest leaf | Modified DFS returning max depth node |
| LCA with node weights | Deepest by weighted path | Use weighted depth instead of hop count |
| Dynamic LCA | Tree changes with insertions | Link-cut trees for O(log n) amortised |
| LCA of k deepest leaves | Not just the deepest, but k-th deepest | Two-pass: find k-th depth, then LCA of all at that depth |

**Binary lifting for repeated LCA queries:** Preprocess `ancestor[v][k]` = 2^k-th ancestor of v in O(n log n). Answer any LCA query in O(log n). Useful when the same tree is queried thousands of times.

**"Return two values" as a general pattern:** This problem uses DFS returning (node, height). This "two-value return" pattern appears throughout tree problems:
- Diameter of tree: return (diameter, height)
- Maximum path sum: return (max_path, max_single_path)  
- House Robber III: return (rob_root, skip_root)
Recognising this pattern immediately unlocks a large class of tree DPs.
