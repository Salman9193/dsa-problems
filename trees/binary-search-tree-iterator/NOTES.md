# Binary Search Tree Iterator — Notes & Intuition

**LeetCode #173** | Trees / Stack / Design | Medium
Constraints: O(1) average time for next()/hasNext(), O(h) space.

---

## Problem

Implement a BST iterator: `next()` returns the next smallest element,
`hasNext()` returns whether more elements exist.

```
BST:    7
       / \
      3   15
         /  \
        9   20

next()    → 3
next()    → 7
hasNext() → true
next()    → 9
next()    → 15
next()    → 20
hasNext() → false
```

---

## Naive Approach — Flatten to List (O(n) Space)

Run full inorder traversal, store all values, iterate with an index.
Simple but uses O(n) space — violates the O(h) constraint.

---

## Controlled Stack — O(h) Space

**Key insight:** inorder traversal visits nodes in sorted order.
Simulate the call stack with an explicit stack, pushing only the
**left spine** from the current position down.

### The left spine invariant

At any point, the stack holds the path from some ancestor down to the
leftmost unvisited node. This is always at most h nodes → O(h) space.

```java
private void pushLeft(TreeNode node) {
    while (node != null) {
        stack.push(node);
        node = node.left;
    }
}
```

### next()

```java
public int next() {
    TreeNode node = stack.pop();    // next smallest = leftmost unvisited
    pushLeft(node.right);            // extend frontier into right subtree
    return node.val;
}
```

Pop the top node (the current minimum), then push the left spine of
its right subtree — making the right subtree's leftmost node the next
minimum.

---

## Why O(1) Amortised for next()

`pushLeft()` may push up to h nodes in one call — not O(1) worst case.
But amortised across all calls:
- Each node is pushed **exactly once** (when its left spine is established)
- Each node is popped **exactly once** (when it becomes the minimum)
- Total work across n calls = O(2n) = O(n) → **O(1) per call amortised**

---

## Full Trace — `7→{3,15}`, `15→{9,20}`

| Operation | Action | Stack | Return |
|-----------|--------|-------|--------|
| Init | pushLeft(7): push 7, push 3 | [7,3] | — |
| next() | pop 3, pushLeft(null) | [7] | **3** |
| next() | pop 7, pushLeft(15): push 15, push 9 | [15,9] | **7** |
| next() | pop 9, pushLeft(null) | [15] | **9** |
| next() | pop 15, pushLeft(20): push 20 | [20] | **15** |
| next() | pop 20, pushLeft(null) | [] | **20** |
| hasNext() | stack empty | [] | **false** |

---

## Comparison

| Approach | next() | hasNext() | Space |
|----------|--------|-----------|-------|
| Flatten to list | O(1) | O(1) | O(n) |
| **Controlled stack** | **O(1) amortised** | **O(1)** | **O(h)** |

---

## Connection to Java TreeMap

`TreeMap.KeyIterator.next()` calls `successor(entry)`:
1. If node has a right child → go right, then all the way left
2. Else → go up until we come from a left child

Step 1 is identical to `stack.pop()` + `pushLeft(node.right)`.
The controlled-stack approach IS the Java TreeMap iterator algorithm,
made explicit with an external stack.

---

## Edge Cases

| Input | Behaviour |
|-------|-----------|
| Single node | Init pushes 1 node; one next() call; hasNext()=false |
| Left-skewed BST `[5,4,3,2,1]` | Stack depth = h = n; still O(h) space |
| Right-skewed BST `[1,2,3,4,5]` | pushLeft pushes only root on init; each next() pushes one node |
| Empty tree | hasNext() immediately false; next() never called |
