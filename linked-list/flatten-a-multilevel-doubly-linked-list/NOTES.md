# Flatten a Multilevel Doubly Linked List — Notes & Intuition

**LeetCode #430** | Linked List | Medium
Each node has `prev`, `next`, and a `child` pointer to a separate list. Flatten to one
level; set every `child` to null.

---

## Problem

A doubly linked list where nodes may also have a `child` pointer to another
doubly linked list (which may have its own children, forming many levels). Flatten it
into a single-level doubly linked list — depth-first — and null out all `child`
pointers.

```
1 - 2 - 3 - 4 - 5 - 6
        |
        7 - 8 - 9 - 10
            |
            11 - 12

→ 1-2-3-7-8-11-12-9-10-4-5-6
```

---

## The Insight — It's Preorder DFS

Look at the output order: when a node has a child, the **entire child list comes right
after it**, and only then does the node's original `next` resume. That's exactly
**preorder depth-first traversal**: visit node → descend into child → then continue
with the sibling. So flattening = "splice each child sublist in between a node and its
`next`."

This structure is a **general tree in disguise**: `child` = first child, `next` =
next sibling. That's the classic *left-child / right-sibling* tree representation, and
flattening is its DFS linearization (see USE_CASES).

---

## The O(1)-Space Approach — Splice As You Go

Walk the list. Each time a node has a child, rewire three links and keep walking — no
recursion, no stack:

1. Save `next` (the node's original successor).
2. Connect `curr → child`, and null `curr.child`.
3. Find the **tail** of the child list, and connect `tail → next`.

Then keep advancing. Because `curr` walks *into* the freshly-spliced child list, any
**nested** children are found and spliced the same way as you reach them — nesting
handles itself.

```java
while (curr != null) {
    if (curr.child != null) {
        Node next  = curr.next;
        Node child = curr.child;
        curr.next = child;  child.prev = curr;  curr.child = null;   // splice head
        Node tail = child;
        while (tail.next != null) tail = tail.next;                  // find child tail
        tail.next = next;   if (next != null) next.prev = tail;      // reconnect
    }
    curr = curr.next;
}
```

---

## Full Trace

At `3` (child `7`): save `next=4`, wire `3→7`, walk to child tail `10`, wire `10→4`.
Spine becomes `1-2-3-7-8-9-10-4-5-6`. `curr` continues into `7`, then `8` (child `11`):
save `next=9`, wire `8→11`, tail `12`, wire `12→9`. Final:
`1-2-3-7-8-11-12-9-10-4-5-6`. ✓

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Stack DFS (push `next`, descend `child`) | O(n) | O(depth) |
| **Splice as you go** | O(n) | **O(1)** |

Each node is visited once by the main walk; the tail-scans collectively cross each
sublist once more — still linear. The splice-in-place version needs no stack.

---

## Edge Cases

| Case | Handling |
|------|----------|
| empty list | return null |
| no children anywhere | already flat; the `if` never fires |
| child at the last node | `next` is null; guard the `next.prev` write |
| deeply nested (child of child of …) | handled — walking into a child reaches its children |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Recursive flatten | call stack | DFS returning each sublist's tail |
| Re-inflate (unflatten) | reverse op | needs stored level metadata |
| N-ary tree preorder | tree, not list | same DFS (LeetCode 589) |
| Flatten binary tree to linked list | tree → list | same splice-the-tail idea (LeetCode 114) |

**The through-line:** a multilevel list with `child`/`next` pointers is a general tree
(left-child/right-sibling); flattening is preorder DFS, done in O(1) space by splicing
each child sublist — via its tail — between a node and its next.
