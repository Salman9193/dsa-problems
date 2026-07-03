# Binary Tree Maximum Path Sum — Notes & Intuition

**LeetCode #124** | Trees / DFS | Hard

---

## Problem

Find the maximum sum of any path in the binary tree.
A path is any sequence of nodes (no node used twice).
The path can start and end at ANY node.

```
[-10,9,20,null,null,15,7] → 42  (path: 15→20→7)
[1,2,3]                   → 6   (path: 2→1→3)
[-3]                      → -3
```

---

## Core Insight — Two Values at Each Node

```
leftGain  = max(dfs(left),  0)  // ignore negative subtrees
rightGain = max(dfs(right), 0)

// Update global max: path THROUGH this node (doesn't go up)
maxSum = max(maxSum, node.val + leftGain + rightGain)

// Return to parent: gain from this node upward (pick ONE side)
return node.val + max(leftGain, rightGain)
```

**Why two values?**
- A path through the current node uses BOTH left and right → stays here
- A path continuing to parent uses only ONE side → goes up

---

## The Two-Return-Value DFS Pattern

This pattern generalises to many tree path problems:

| Problem | "Through" update | Return |
|---------|-----------------|--------|
| #124 Max Path Sum | val + left + right | val + max(left, right) |
| #543 Diameter | left + right (edges) | 1 + max(left, right) |
| #687 Univalue Path | left_streak + right_streak | 1 + max(streaks) |

---

## Complexity

Time O(n) · Space O(h)
