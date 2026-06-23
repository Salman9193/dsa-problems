# Linked List Cycle — Notes & Intuition

**LeetCode #141** | Linked List | Easy

---

## Problem

Given the head of a linked list, return `true` if it contains a cycle
(some node is reachable again by following `next` pointers), `false` otherwise.

---

## Approach 1: HashSet

Store every visited node. If a node is seen twice → cycle.

```java
Set<ListNode> seen = new HashSet<>();
while (curr != null) {
    if (seen.contains(curr)) return true;
    seen.add(curr);
    curr = curr.next;
}
return false;
```

Simple, but O(n) space.

---

## Approach 2: Floyd's Cycle Detection (Tortoise & Hare)

```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true;
}
return false;
```

**O(n) time, O(1) space** — the optimal solution.

---

## Why Do They Always Meet?

Once both pointers enter the cycle, consider the gap between them.
Each iteration, fast moves 2 steps and slow moves 1 — fast gains
exactly 1 position on slow per iteration.

The gap closes by 1 every step, so it reaches 0 in at most
`cycle_length` steps. They never "skip over" each other because
the gap decreases by exactly 1, not 2.

```
Inside a cycle of length L:
  gap starts at some value k
  after 1 step: gap = k+1 (mod L)
  ...
  after L-k steps: gap = 0  → they meet
```

---

## Why Both Null Checks?

```java
while (fast != null && fast.next != null)
```

- `fast != null` — fast itself might land on null (odd-length acyclic list)
- `fast.next != null` — we call `fast.next.next`, so fast.next must be non-null first

Removing either check causes NullPointerException on acyclic lists.

---

## Extensions of Floyd's Algorithm

### 1. Find Cycle Start Node (LeetCode #142)

After `slow == fast`:
1. Reset `slow = head` (keep `fast` at meeting point).
2. Advance both by 1 step at a time.
3. They meet at the **cycle start**.

**Why it works:**
Let F = distance from head to cycle start,
    C = cycle length,
    k = distance from cycle start to meeting point.

When they meet: slow travelled F+k steps, fast travelled F+k+n*C steps.
Since fast = 2*slow: F+k = n*C → F = n*C - k.
Resetting slow to head: slow needs F more steps to reach cycle start.
Fast also needs F more steps (n*C - k from meeting point wraps to cycle start).
They meet at cycle start. ✓

### 2. Find Cycle Length

After `slow == fast`:
Keep slow fixed, advance fast by 1 until they meet again.
Count the steps → that's the cycle length.

### 3. Find Middle of Linked List (LeetCode #876)

Run slow (1 step) and fast (2 steps) from head.
When fast reaches the end, slow is at the middle.

---

## Floyd's vs Graph Cycle Detection

Floyd's works **only on functional graphs** — structures where each node
has exactly one outgoing edge (like a linked list). General graphs require
DFS-based approaches.

See `GRAPH_CYCLE_DETECTION.md` for the full comparison.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `null` | `false` | Empty list |
| Single node, no self-loop | `false` | fast immediately hits null |
| Single node, self-loop | `true` | fast.next = head = slow |
| Two nodes, cycle | `true` | fast laps slow in 1 step |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Find cycle start (#142) | Return entry node, not just bool | After collision, reset slow to head; advance both at speed 1; they meet at entry |
| Find cycle length | Length of the loop | After finding collision, count steps for fast to return |
| Find duplicate number (#287) | Array as linked list with cycle | Floyd's on implicit list: `next(i) = nums[i]` |
| Happy number (#202) | Digit sum cycle detection | Floyd's on the sequence of digit sums |
| Detect cycle without modifying list | Can't use visited set | Floyd's inherently O(1) space, non-destructive |
| k-th node from end | No cycle, different problem | Two pointers: advance one by k, then advance both |

**Why Floyd's finds the cycle start:** Let distance from head to cycle entry = F, cycle length = C, collision point = h steps into cycle. Floyd's guarantees `h = F (mod C)`. Resetting slow to head and advancing both at speed 1 makes them meet exactly at the entry after F more steps.

**Extension — distributed cycle detection (Brent's algorithm):** Better constant factor than Floyd's: teleport the hare to the tortoise's position every power-of-2 steps. Finds cycle in the same O(n) time but with fewer pointer comparisons in practice.
