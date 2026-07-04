# Merge K Sorted Lists — Notes & Intuition

**LeetCode #23** | Linked List / Heap | Hard

---

## Problem

You are given an array of `k` linked lists, each sorted in ascending order.
Merge them into one sorted linked list and return its head.

A recurring Google problem — it tests heaps, linked-list pointer surgery, and
the ability to reach the optimal `O(N log k)` rather than the naive
`O(N log N)`.

---

## The Core Insight

At any moment, the next node of the answer is the **smallest current head**
among the `k` lists. Everything else follows:

- A **min-heap of size k** gives that smallest head in `O(log k)`.
- Pop it, append it, then push its successor.
- Each of the `N` total nodes is pushed and popped exactly once.

Result: `O(N log k)`. Because each list is *already sorted*, we only ever
compare `k` front candidates — the log factor is `log k`, not `log N`.

---

## Approach 1 — Min-Heap

```java
PriorityQueue<ListNode> heap =
    new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

for (ListNode node : lists)
    if (node != null) heap.offer(node);   // seed with all heads

ListNode dummy = new ListNode(0), tail = dummy;
while (!heap.isEmpty()) {
    ListNode smallest = heap.poll();       // global min head
    tail.next = smallest;
    tail = tail.next;
    if (smallest.next != null) heap.offer(smallest.next);  // push successor
}
return dummy.next;
```

- **Dummy head** removes the "is this the first node?" special case, and
  `dummy.next` is naturally `null` when every list is empty.
- Heap never exceeds `k` entries → `O(k)` space.

---

## Approach 2 — Divide & Conquer

Merge lists in pairs, halving the count each round:

```
k lists -> merge pairs -> k/2 -> k/4 -> ... -> 1
```

```java
ListNode merge(ListNode[] lists, int lo, int hi) {
    if (lo == hi) return lists[lo];
    int mid = lo + (hi - lo) / 2;
    return mergeTwo(merge(lists, lo, mid), merge(lists, mid + 1, hi));
}
```

Each of the `log k` rounds touches all `N` nodes once → `O(N log k)`, with
`O(log k)` recursion-depth space and no heap overhead.

---

## Why Not Simpler Options

| Approach | Time | Note |
|----------|------|------|
| Concatenate all, then sort | O(N log N) | Ignores that lists are already sorted |
| Merge one-by-one (list1+list2, then +list3...) | O(N·k) | Re-walks the growing merged list k times |
| **Min-heap** | **O(N log k)** | Optimal; simplest to reason about |
| **Divide & conquer** | **O(N log k)** | Optimal; less constant overhead, O(log k) space |

---

## Full Trace — Min-Heap

```
lists = [ [1,4,5], [1,3,4], [2,6] ]

seed heap: {1(A), 1(B), 2(C)}
poll 1(A) -> out 1 ; push 4(A)   heap {1(B), 2(C), 4(A)}
poll 1(B) -> out 1 ; push 3(B)   heap {2(C), 3(B), 4(A)}
poll 2(C) -> out 2 ; push 6(C)   heap {3(B), 4(A), 6(C)}
poll 3(B) -> out 3 ; push 4(B)   heap {4(A), 4(B), 6(C)}
poll 4(A) -> out 4 ; push 5(A)   heap {4(B), 5(A), 6(C)}
poll 4(B) -> out 4 ; (end B)     heap {5(A), 6(C)}
poll 5(A) -> out 5 ; (end A)     heap {6(C)}
poll 6(C) -> out 6 ; (end C)     heap {}

merged: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6 ✓
```

---

## Edge Cases

| Input | Output |
|-------|--------|
| `lists = []` (no lists) | `null` |
| `lists = [null]` | `null` |
| `lists = [null, null]` | `null` — heap never seeded |
| One non-empty list | That list, unchanged |
| Duplicate values across lists | Preserved; `<=` keeps it stable |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Merge k sorted **arrays** | Arrays not lists | Heap of `(value, arrayIdx, elemIdx)` triples |
| Merge k sorted **streams / files** | Data too big for memory | External merge sort — the heap merge step is identical |
| **Smallest Range Covering k Lists** (#632) | Range over all lists | Min-heap of heads + track current max; slide as you pop |
| **Kth Smallest in Sorted Matrix** (#378) | k sorted rows | Heap merge, stop after k pops |
| **Ugly Number II / Super Ugly** | Merge k generated sequences | Same k-way merge idea over multiplied streams |
| Merge with a stability guarantee | Equal keys keep order | Include a sequence tiebreaker in the comparator |

**Trade-off:** the heap solution generalizes directly to external / streaming
merges (you never hold all N in memory — only k heads), which is why external
merge sort in databases uses exactly this k-way merge. Divide & conquer is
purely in-memory but avoids the heap's per-node comparator cost.
