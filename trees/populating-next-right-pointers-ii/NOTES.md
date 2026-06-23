# Populating Next Right Pointers in Each Node II — Notes & Intuition

**LeetCode #117** | Trees / BFS | Medium

---

## Problem

Given any binary tree, populate each node's `next` pointer to its next
right neighbour at the same level. If no right neighbour, `next = null`.

```
Input:          Output:
    1               1 → null
   / \             / \
  2   3           2 → 3 → null
 / \   \         / \   \
4   5   7       4 → 5 → 7 → null
```

---

## Approach 1 — BFS (O(n) space)

Process each level from a queue, link nodes left to right.

```java
while (!queue.isEmpty()) {
    int size = queue.size();
    Node prev = null;
    for (int i = 0; i < size; i++) {
        Node curr = queue.poll();
        if (prev != null) prev.next = curr;
        prev = curr;
        if (curr.left  != null) queue.offer(curr.left);
        if (curr.right != null) queue.offer(curr.right);
    }
}
```

Correct, but O(n) space for the queue.

---

## Approach 2 — O(1) Space (Optimal)

**Key insight:** once level L's `next` pointers are established, we can
traverse level L using those pointers to build level L+1's `next` links —
no queue needed.

```java
Node curr = root;
while (curr != null) {
    Node dummy = new Node(0);   // sentinel head for next level
    Node tail = dummy;

    while (curr != null) {
        if (curr.left  != null) { tail.next = curr.left;  tail = tail.next; }
        if (curr.right != null) { tail.next = curr.right; tail = tail.next; }
        curr = curr.next;        // horizontal traversal using established next
    }

    curr = dummy.next;           // drop to next level
}
```

---

## Why the Dummy Node?

Without dummy:
```java
// Need null check every time:
if (head == null) { head = child; tail = child; }
else { tail.next = child; tail = child; }
```

With dummy:
```java
// Always safe — dummy is never null:
tail.next = child;
tail = tail.next;
```

`dummy.next` is the head of the next level (or null if no children exist).
The dummy node pattern eliminates the "first element" special case.

Same trick used in: merge sort linked list, LRU cache, reverse linked list k-group.

---

## Why This Is Correct

The algorithm relies on one invariant:

> When processing level L, all of level L's `next` pointers are already set.

This holds because:
1. Level 0 (root) has no `next` to set.
2. When we process level L, we set all of level L+1's `next` pointers.
3. By induction, every level is fully linked before we traverse it.

We never traverse a level before its `next` pointers are set — the outer
loop drops to `dummy.next` (level L+1) only after finishing level L.

---

## Full Trace

Tree: `1 → {2,3}`, `2 → {4,5}`, `3 → {null,7}`

**Level 0 (curr=1):**
```
1.left=2  → tail→2,  list=[2]
1.right=3 → tail→3,  list=[2→3]
curr=1.next=null → drop to 2
```

**Level 1 (curr=2→3):**
```
2.left=4  → list=[4]
2.right=5 → list=[4→5]
curr=2.next=3
3.left=null
3.right=7 → list=[4→5→7]
curr=3.next=null → drop to 4
```

**Level 2 (curr=4→5→7):**
```
No children → list=[]
curr=null → outer loop ends
```

Result: `1→null | 2→3→null | 4→5→7→null` ✓

---

## Comparison

| | BFS | O(1) Space |
|--|-----|-----------|
| Time | O(n) | O(n) |
| Space | O(n) | O(1) |
| Key structure | Queue | Already-set next pointers + dummy |

---

## Related Problems

| Problem | Difference |
|---------|------------|
| #116 Populate Next Right I | Perfect binary tree — every node has both children |
| **#117 Populate Next Right II** | **Arbitrary tree — any node may have 0/1/2 children** |
| #102 Level Order Traversal | Returns values; uses queue |
| #199 Right Side View | Last node per level |

---

## Edge Cases

| Input | Output |
|-------|--------|
| `null` | `null` |
| Single node | `next = null` |
| Left-skewed tree | Each node links to null (single per level) |
| Perfect binary tree | Same as #116 |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Perfect binary tree (#116) | Every node has 0 or 2 children | Simpler O(1) space: use already-set next pointers directly |
| N-ary tree | Each node has up to k children | Same dummy node trick; iterate children list |
| Return level order as list (#102) | Collect values instead of linking | BFS with queue; size snapshot per level |
| Right side view (#199) | Last node per level | BFS; record last node seen each level |
| Level averages (#637) | Mean value per level | BFS; accumulate sum and count per level |
| Connect nodes at same distance from root | Weighted tree | BFS with distance tracking |

**Why O(1) space is achievable only after level L is linked:** The key invariant is that we use the CURRENT level's next pointers (already set) to traverse it while building the NEXT level's next pointers. This chicken-and-egg works because each level is fully linked before we start using those links.

**B+ tree connection:** As detailed in USE_CASES.md, this algorithm's output (a linked list across one tree level) IS the B+ tree leaf-level structure used by PostgreSQL and MySQL for O(1)-per-step range scans. The `next` pointer is `btpo_next` in PostgreSQL's nbtree implementation.
