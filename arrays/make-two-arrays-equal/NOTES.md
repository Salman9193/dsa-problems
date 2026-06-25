# Make Two Arrays Equal by Reversing Subarrays — Notes & Intuition

**LeetCode #1460** | Arrays / Sorting | Easy

---

## Problem

Given arrays `target` and `arr` of the same length, can `arr` be made equal
to `target` by reversing any subarrays any number of times?

```
target=[1,2,3,4], arr=[2,4,1,3]  →  true
target=[7],       arr=[7]         →  true
target=[3,7,9],   arr=[3,7,11]   →  false
```

---

## The Crucial Insight — Unlimited Reversals = Any Permutation

The phrase "any subarrays any number of times" is the key.

**Claim:** Any permutation of `arr` is reachable using subarray reversals.

**Proof:**
Any permutation decomposes into a sequence of adjacent transpositions
(swapping two neighbouring elements). Swapping positions i and i+1 is
exactly a reversal of the subarray `[i, i+1]`. Since we can apply any
reversal any number of times, we can perform any adjacent swap, and
therefore any permutation.

**Consequence:** The problem reduces to:
```
Can arr be rearranged to equal target?
↔ Do arr and target contain the same elements with the same frequencies?
↔ Are arr and target permutations of each other?
↔ Do they have the same multiset?
```

---

## Two Approaches

### Approach 1 — Sort and Compare: O(n log n)

```java
Arrays.sort(target);
Arrays.sort(arr);
return Arrays.equals(target, arr);
```

Pros: Simple, no extra space beyond sort buffer.
Cons: Mutates input arrays; O(n log n).

### Approach 2 — Frequency Map: O(n)

```java
Map<Integer, Integer> freq = new HashMap<>();
for (int x : target) freq.merge(x,  1, Integer::sum);
for (int x : arr)    freq.merge(x, -1, Integer::sum);
for (int count : freq.values()) if (count != 0) return false;
return true;
```

Pros: O(n) time; does not mutate input.
Cons: O(n) extra space for the map.

---

## The General Pattern — "Any Number of Operation X"

When a problem allows "any number of" some rearrangement operation,
ask: **does this operation generate ALL permutations?**

| Operation | Generates all permutations? | Reduces to |
|-----------|----------------------------|------------|
| Any subarray reversal | YES | Multiset equality |
| Any adjacent swap | YES | Multiset equality |
| Swap any two elements | YES | Multiset equality |
| Rotation only | NO | String rotation check |
| Swap at fixed positions | NO | Depends on reachability graph |

If the answer is YES → the problem is multiset equality.

---

## Why This Problem LOOKS Harder Than It Is

"Reversing subarrays" sounds like a complex, constrained operation.
The unlock is recognising it as a complete permutation generator.

Compare with the hard version: find the MINIMUM number of reversals
to sort arr into target — that is NP-hard for unsigned permutations
(the genome rearrangement problem in bioinformatics). Unlimited
reversals makes it trivial.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Sort and compare | O(n log n) | O(1) extra |
| Frequency map | O(n) | O(n) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `target=[7], arr=[7]` | true | Single element, trivially equal |
| Same elements, different count | false | Multiset mismatch |
| All same elements | true | Multisets identical |
| Already equal | true | Zero reversals needed |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Minimum reversals to sort | Count ops | NP-hard (genome rearrangement) |
| k-reversal limit | k ops max | Graph reachability in permutation space |
| Circular array | Rotations allowed too | Still multiset (rotations are permutations) |
| Weighted reversals | Cost per reversal | Graph shortest path in Cayley graph |
| Strings instead of integers | Same problem on chars | Same frequency map (anagram check) |
