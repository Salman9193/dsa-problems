# Reverse Linked List — Notes & Intuition

**LeetCode #206** | Linked List | Easy

---

## Problem

Given the head of a singly linked list, reverse it in-place and return
the new head.

```
1 → 2 → 3 → 4 → 5 → null   →   5 → 4 → 3 → 2 → 1 → null
```

---

## Approach 1 — Iterative (O(1) space) ← Preferred

Three pointers work in coordination: `prev`, `curr`, `next`.

```java
ListNode prev = null, curr = head;
while (curr != null) {
    ListNode next = curr.next;  // save next (critical: done FIRST)
    curr.next = prev;           // reverse the pointer
    prev = curr;                // advance prev
    curr = next;                // advance curr
}
return prev;
```

### Why the ORDER matters

```
WRONG order: curr.next = prev; next = curr.next;
  After curr.next = prev, curr.next no longer points to the original next.
  next = curr.next now gives prev, not the original next → list is lost.

CORRECT: save next FIRST, then overwrite curr.next.
```

### Step-by-step on `1 → 2 → 3`

```
Start: prev=null, curr=1

Step 1: next=2
        1.next = null     → null ← 1
        prev=1, curr=2

Step 2: next=3
        2.next = 1        → null ← 1 ← 2
        prev=2, curr=3

Step 3: next=null
        3.next = 2        → null ← 1 ← 2 ← 3
        prev=3, curr=null

Return prev=3 ✓
```

---

## Approach 2 — Recursive (O(n) stack space)

```java
ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) return head;
    ListNode newHead = reverseList(head.next);
    head.next.next = head;  // next node points back to current
    head.next = null;        // break forward link (prevent cycle)
    return newHead;
}
```

The recursion unwinds from the tail, wiring reverse pointers on the way back.
`newHead` is always the original tail — it bubbles up unchanged through all calls.

### Why `head.next = null`?

Without it, after `head.next.next = head`, node 1 and node 2 point to each other
creating a cycle: `1 ↔ 2`. Setting `head.next = null` breaks the forward link.

---

## Comparison

| | Iterative | Recursive |
|--|-----------|-----------|
| Time | O(n) | O(n) |
| Space | **O(1)** | O(n) stack |
| Risk | None | Stack overflow for large n |
| Preferred | ✓ Always | Interview demonstration only |

---

## Problems That Use This as a Subroutine

| Problem | How reversal is used |
|---------|---------------------|
| #92 Reverse Linked List II | Reverse sublist between positions left and right |
| #25 Reverse Nodes in k-Group | Reverse every k consecutive nodes |
| #234 Palindrome Linked List | Reverse second half, compare with first half |
| #143 Reorder List | Reverse second half, interleave with first |

Mastering the iterative three-pointer pattern enables all of these.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `null` | `null` | Empty list |
| `[1]` | `[1]` | Single node, already reversed |
| `[1,2]` | `[2,1]` | Two nodes |
| `[1,2,3,4,5]` | `[5,4,3,2,1]` | Full reversal |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Reverse sublist (#92) | Only reverse positions [left, right] | Find left-1 node; reverse [left..right]; reattach |
| Reverse in k-groups (#25) | Reverse every k consecutive nodes | Detect next k nodes, reverse, advance |
| Palindrome linked list (#234) | Check if palindrome | Reverse second half; compare with first half; restore |
| Reorder list (#143) | Interleave first and second half | Find midpoint; reverse second half; merge |
| Rotate list (#61) | Rotate by k positions | Find new tail (length-k from end); rewire |
| Reverse even-length groups (#2074) | Reverse groups of even sizes | Iterate through groups; reverse if even length |

**The subroutine view:** Reversing a linked list is rarely asked in isolation at Staff level. It always appears as a subroutine for harder problems. The pattern to recognise:
```
Find midpoint (fast/slow pointers)
Reverse second half
Process (compare, merge, interleave)
Optionally restore
```
This template solves #234, #143, #876, #2095 with only minor variations.

**In-place restoration:** After palindrome check, always restore the list: reverse the second half back. In production code, mutating input data structures is a defect.
