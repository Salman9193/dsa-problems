# Delete Nodes and Return Forest — Notes & Intuition

**LeetCode #1110** | Trees / DFS | Medium

---

## Problem

Given a binary tree and a set of values to delete, remove those nodes
and return the roots of the resulting trees (the forest).

```
Input:
        1
       / \
      2   3
     / \ / \
    4  5 6  7
delete = [3, 5]

Output: [[1,2,null,4], [6], [7]]
```

---

## Core Insight — Postorder DFS with isRoot Flag

When we delete a node:
1. Its children become **new roots** of independent trees
2. It returns **null** to its parent (severs the connection)

We use postorder DFS so children are processed before parents —
by the time we handle a node, its children's fates are already decided.

---

## The isRoot Flag

Tracks whether a node should be added to the forest as a root:

```
isRoot = true when:
  1. It's the original root (initial call)
  2. Its parent was deleted (parent's isRoot propagates as: deleted → isRoot for children)

isRoot = false when:
  Its parent survives (still has a parent → not a forest root)
```

When recursing into children: `isRoot = deleted` (of current node).

---

## Algorithm

```java
TreeNode dfs(TreeNode node, Set<Integer> deleteSet,
             List<TreeNode> forest, boolean isRoot) {
    if (node == null) return null;

    boolean deleted = deleteSet.contains(node.val);

    if (isRoot && !deleted) forest.add(node);   // surviving root → add to forest

    node.left  = dfs(node.left,  deleteSet, forest, deleted);
    node.right = dfs(node.right, deleteSet, forest, deleted);

    return deleted ? null : node;   // null severs parent's pointer
}
```

---

## Why Postorder?

Postorder processes children before parents.

```
node.left = dfs(node.left, ...)
```

The return value of `dfs` is either:
- The child (if it survived) → parent keeps the connection
- `null` (if deleted) → parent's pointer is automatically severed

With preorder, we'd need a second pass to clean up parent pointers
after deleting nodes.

---

## Full Trace

Tree: `1→{2,3}`, `2→{4,5}`, `3→{6,7}`, delete=`{3,5}`

| Node | isRoot | Deleted | Action |
|------|--------|---------|--------|
| 4 | false | false | return 4 |
| 5 | false | true | return null (no children) |
| 2 | false | false | 2.right=null, return 2 |
| 6 | **true** | false | **add 6 to forest**, return 6 |
| 7 | **true** | false | **add 7 to forest**, return 7 |
| 3 | false | true | return null |
| 1 | **true** | false | **add 1 to forest**, 1.right=null, return 1 |

forest = [1, 6, 7] ✓

---

## Why HashSet for deleteSet?

O(1) lookup per node vs O(k) for array scan. With n=1000 nodes and k=100
deletions, HashSet saves 100,000 comparisons.

---

## Complexity

| | |
|--|--|
| Time | O(n) — every node visited once |
| Space | O(n) — HashSet + recursion stack (depth) + result list |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Delete root only | root's children as separate roots | children promoted |
| Delete leaf node | same tree minus leaf | no children to promote |
| Delete all nodes | empty list | nothing survives |
| Delete nothing | [original root] | no changes |
| Delete chain: parent then child | both removed cleanly | postorder handles it |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Delete by depth | Remove all nodes at depth k | BFS level-order; sever at level k |
| Delete by value range | Remove all values in [lo, hi] | Same postorder; deleteSet becomes a range check |
| Delete leaves only (#1325) | Remove leaves with target value | Postorder; re-check after children deleted |
| Delete and merge subtrees | Children merge instead of becoming roots | Custom merge logic in postorder DFS |
| Delete in n-ary tree | k children per node | Same algorithm; iterate children list |
| Count resulting trees | How many trees in forest? | Count nodes added to forest list |
| Minimum cuts to create k trees | Inverse: how many deletions for k trees? | Each deletion creates at most 2 new roots |

**Postorder is mandatory here:** Preorder (top-down) would require knowing if a node will be deleted before processing its children. We'd need a second pass to clean up dangling pointers. Postorder's "children first" guarantee means the parent's pointer is set correctly by the child's return value — no cleanup needed.

**React Fiber parallel:** As detailed in USE_CASES.md, React's reconciler uses the same postorder delete-and-promote pattern when component types change. The `isRoot` flag corresponds to React's "fiber needs remounting" condition.
