# Validate Binary Search Tree — Notes & Intuition

**LeetCode #98** | Trees / DFS | Medium

---

## Problem

Determine if a binary tree is a valid BST:
- Left subtree contains only nodes with values LESS than the node's value
- Right subtree contains only nodes with values GREATER than the node's value
- Both subtrees must also be valid BSTs

```
[2,1,3] → true
[5,1,4,null,null,3,6] → false  (4 is in right subtree of 5 but 4 < 5)
```

---

## Common Mistake — Only Checking Direct Children

```
❌ Wrong:
   if (node.left.val < node.val && node.right.val > node.val) → OK
   This misses: [10,5,15,null,null,6,20]
   6 is in right subtree of 10 but 6 < 10 → INVALID
   Yet 6 > 15's left child rule passes locally.
```

---

## Correct — Pass Down Valid Range

```
validate(node, min, max):
    if node == null: return true
    if node.val <= min OR node.val >= max: return false
    return validate(node.left, min, node.val)
        && validate(node.right, node.val, max)
```

Each recursive call narrows the valid range. Root: (-∞, +∞).

---

## Alternative — Inorder Traversal

Valid BST's inorder traversal produces a STRICTLY increasing sequence.
Track previous value; if current <= previous → invalid.

---

## Complexity

Time O(n) · Space O(h)
