# Linked List Random Node — Notes & Intuition

**LeetCode #382** | Linked List / Randomisation | Medium

---

## Problem

Given a singly linked list, return a random node's value where each node
has equal probability of being chosen. The list length is not known in advance.

```
head = [1, 2, 3]
getRandom() → each of 1, 2, 3 with probability 1/3
```

---

## Naive Approach — Two Pass

Count length n, generate `rand.nextInt(n)`, walk to that index.

```java
int n = 0;
ListNode curr = head;
while (curr != null) { n++; curr = curr.next; }

int target = rand.nextInt(n);
curr = head;
for (int i = 0; i < target; i++) curr = curr.next;
return curr.val;
```

Correct but requires **two passes** — fails if the list is a stream that
can only be read once.

---

## Reservoir Sampling — One Pass, O(1) Space

**Vitter's Algorithm R (k=1):**
When visiting the `i`-th node (1-indexed), replace the current selection
with this node's value with probability `1/i`.

```java
int result = 0, i = 1;
ListNode curr = head;
while (curr != null) {
    if (rand.nextInt(i) == 0) result = curr.val;  // replace with prob 1/i
    i++;
    curr = curr.next;
}
return result;
```

---

## Proof of Correctness (Telescoping Product)

We need to show that every node has exactly `1/n` probability of being returned.

**For node at position k (1-indexed):**

```
P(selected at step k)          = 1/k
P(not replaced at step k+1)    = k/(k+1)
P(not replaced at step k+2)    = (k+1)/(k+2)
...
P(not replaced at step n)      = (n-1)/n

P(k is final result) = (1/k) × (k/(k+1)) × ((k+1)/(k+2)) × ... × ((n-1)/n)
                     = (1/k) × (k/n)       [telescoping: all middle terms cancel]
                     = 1/n  ✓
```

---

## Worked Example — `[1, 2, 3]`

| step | node | prob replace | result if replace | P(final) |
|------|------|-------------|-------------------|---------|
| i=1 | 1 | 1/1 = 1 (always) | 1 | P(1)=1×½×⅔=1/3 |
| i=2 | 2 | 1/2 | 2 | P(2)=½×⅔=1/3 |
| i=3 | 3 | 1/3 | 3 | P(3)=1/3 |

All equal → uniform distribution ✓

---

## Generalisation — Sample k of n

To sample k items from a stream of unknown size:

```java
// Keep first k items in reservoir
// For each subsequent item i (i > k):
//   Replace reservoir[rand.nextInt(i)] with new item with prob k/i
int[] reservoir = new int[k];
// fill with first k items...
for (int i = k; curr != null; i++) {
    int j = rand.nextInt(i + 1);   // random index [0, i]
    if (j < k) reservoir[j] = curr.val;
    curr = curr.next;
}
```

This problem is the k=1 special case.

---

## Comparison

| | Naive | Reservoir Sampling |
|--|-------|-------------------|
| Time | O(n) | O(n) |
| Space | O(1) | O(1) |
| Passes | 2 | 1 |
| Works on stream | ✗ | ✓ |
| Known n required | ✓ | ✗ |

---

## Edge Cases

| Input | Behaviour |
|-------|-----------|
| Single node `[1]` | Always returns 1 (rand.nextInt(1)=0 always) |
| Two nodes | Returns each with prob 1/2 |
| Very long list | O(n), O(1) space — no memory issues |
