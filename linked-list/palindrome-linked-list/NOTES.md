# Palindrome Linked List — Notes & Intuition

**LeetCode #234** | Linked List | Easy
Constraints: 1 ≤ nodes ≤ 1e5, values 0–9. Follow-up: **O(n) time, O(1) space**.

---

## Problem

Given the head of a singly linked list, return `true` if it reads the same forwards
and backwards.

```
Input:  1 → 2 → 2 → 1        Output: true
Input:  1 → 2                Output: false
```

---

## The Catch — You Can't Walk Backwards

A singly linked list only goes forward, so the naive "compare ends moving inward"
needs random access you don't have. Two ways around it:

- **Baseline:** copy values to an array, two-pointer check. O(n) time, **O(n) space**
  — simple, but ignores the follow-up.
- **Optimal:** find the middle, reverse the second half in place, compare. O(n) time,
  **O(1) space** — the interview answer.

---

## The O(1)-Space Idea — Middle, Reverse, Compare

Three linear passes, no extra structure:

1. **Find the middle** with slow/fast pointers (tortoise & hare). Fast moves twice as
   fast, so when it reaches the end, slow sits at the midpoint (start of the second
   half).
2. **Reverse the second half** in place, starting at slow.
3. **Compare** the first half (from `head`) with the reversed second half, walking both
   inward until the shorter (second) side runs out.

```
1 → 2 → 3 → 2 → 1
        ↑ slow (midpoint)
reverse back half:  1 → 2 → [3] ← 2 ← 1   compare front vs back
```

---

## Two Techniques in One

| Technique | Role here | Also used for |
|-----------|-----------|---------------|
| **Fast/slow pointer** | Find the midpoint in one pass | Floyd's cycle detection, nth-from-end |
| **In-place reversal** | Make the back half readable forward | Memory-tight list processing |

---

## Why the Comparison Terminates Correctly

The second half is always **≤** the first, so loop while the *back* pointer is non-null:

- **Even** `1→2→2→1`: slow stops at the 3rd node; halves `[1,2]` vs reversed `[1,2]`;
  back runs out after 2 compares.
- **Odd** `1→2→3→2→1`: slow stops at the middle `3`; the reversed back half is
  `[1,2,3]`; the shared middle compares against itself (harmless) before back hits
  null.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Array + two pointers | O(n) | O(n) |
| Middle + reverse half | O(n) | **O(1)** |

---

## Edge Cases & The Mutation Caveat

| Case | Handling |
|------|----------|
| empty / single node | palindrome by definition → `true` |
| two nodes | compares the only pair |
| all equal values | passes; midpoint logic still holds |
| **mutates the input** | the back half ends up reversed — if the caller needs the original, reverse it back after comparing (still O(1) space) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Doubly linked list | has `prev` | trivial two-pointer from both ends, no reversal |
| Must not mutate | restore required | reverse the back half again after comparing |
| Array / string palindrome | random access | direct two-pointer from both ends |
| Palindrome by structure, k-chunks | grouped values | compare reversed chunk sequences |

**The through-line:** when a structure only reads one way, the fast/slow pointer finds
its middle and in-place reversal makes the other half traversable — turning a
"read-it-backwards" problem into O(1) extra space.
