# Greedy vs Dynamic Programming — When to Use Which

A practical guide to recognising when greedy suffices and when you need DP.

---

## The One-Line Mental Model

> **Greedy works when a locally optimal choice never blocks a globally better solution.
> DP is needed when it can.**

---

## The Formal Condition — Matroid Theory

A problem where greedy always yields the optimal solution has a structure called
a **matroid**. The formal reason greedy works on matroids is that their
"independence system" satisfies an exchange property — you can always swap a
greedy choice for a non-greedy one without making things worse.

In practice you rarely invoke matroids explicitly, but knowing the concept
explains *why* greedy works on spanning trees (graphic matroid) and Huffman
coding (entropy matroid) but not on arbitrary coin systems.

---

## Three Practical Tests

### Test 1 — The Interchange Argument (Rigorous)

Assume the optimal solution does NOT use the greedy choice at some step.
Show you can swap the greedy choice in without increasing cost.

- If the swap never increases cost → greedy is correct.
- If the swap can increase cost → greedy fails → use DP.

**Example — Activity Selection (greedy works):**
Suppose optimal picks activity B first, but greedy picks A (earliest finish).
Swapping B for A can only free up more time, never less → greedy is safe.

**Example — Coin Change `[1,3,4]`, amount=6 (greedy fails):**
Greedy picks 4 first (largest ≤ 6), leaving 2 → solved by 1+1 = 3 total.
Optimal is 3+3 = 2 total. The greedy choice (4) blocks the better solution
(two 3s). Interchange argument fails → need DP.

---

### Test 2 — The Canonical Check (For Coin Systems Specifically)

For coin/denomination problems, verify greedy matches DP across a test range:

```java
boolean isCanonical(int[] coins) {
    Arrays.sort(coins);
    int maxAmount = coins[coins.length - 1] * 2;

    for (int amount = 1; amount <= maxAmount; amount++) {
        if (greedyCount(coins, amount) != dpCount(coins, amount)) {
            return false;  // greedy fails here
        }
    }
    return true;
}
```

US coins `[1,5,10,25,50,100]` → canonical → greedy works.
Coins `[1,3,4]` → not canonical (fails at amount 6) → use DP.

---

### Test 3 — Overlapping Subproblems Check

Ask: *is the same sub-problem solved more than once in a naive recursion?*

Draw the recursion tree for a small input. If nodes repeat → overlapping
subproblems → DP (memoisation removes the redundancy).

```
coinChange([1,3,4], 6)
├── coinChange(5)
│   ├── coinChange(4)
│   │   ├── coinChange(3)  ← repeated
│   │   └── ...
│   └── coinChange(2)
│       └── coinChange(1)  ← repeated
└── coinChange(3)          ← repeated
    └── ...
```

Repeated sub-problems → DP.

---

## Decision Flowchart

```
Start: minimisation / maximisation problem
         │
         ▼
Is there a "take the best now" rule that never hurts later?
  (interchange argument holds)
         │
    YES  │  NO
         │   └──► Overlapping subproblems?
         │              │
         │         YES  │  NO
         │              │   └──► Brute force / backtracking
         │              ▼
         │         Optimal substructure?
         │         (optimal solution built from optimal sub-solutions)
         │              │
         │         YES  │  NO
         │              │   └──► Neither greedy nor DP — rethink model
         │              ▼
         ▼            D P
      G R E E D Y
```

---

## Known Problem Classes

| Problem | Greedy? | Reason |
|---------|---------|--------|
| US coin change | ✓ | Canonical denomination system |
| Arbitrary coin change | ✗ | Denominations can overlap adversarially |
| Activity selection (max non-overlapping intervals) | ✓ | Earliest-finish greedy provably optimal via interchange |
| 0/1 Knapsack | ✗ | Items can't be fractionally taken — local choice forecloses global opt |
| Fractional Knapsack | ✓ | Items splittable — take highest value/weight ratio greedily |
| Huffman encoding | ✓ | Greedy on frequencies builds optimal prefix tree (entropy matroid) |
| Dijkstra shortest path | ✓ | No negative edges → greedy relaxation always correct |
| Bellman-Ford shortest path | ✗ | Negative edges break greedy → DP over all edges |
| Minimum spanning tree (Kruskal / Prim) | ✓ | Graphic matroid guarantees greedy optimality |
| Longest common subsequence | ✗ | No greedy ordering exists — full DP table needed |
| Matrix chain multiplication | ✗ | Greedy parenthesisation is not optimal |
| Edit distance | ✗ | Every alignment must be considered |

---

## The Two Properties That Signal DP

Both must be present:

### 1. Optimal Substructure
The optimal solution to the full problem contains optimal solutions to
its sub-problems.

> Coin change: minimum coins for amount `i` uses minimum coins for `i-c`.
> Longest common subsequence: optimal alignment of full strings uses
> optimal alignment of prefixes.

### 2. Overlapping Subproblems
The same sub-problems recur across different branches of the recursion.

> Without memoisation, naive recursion recomputes the same amounts/indices
> exponentially. DP caches results to bring this to polynomial time.

If only (1) is present but not (2), divide-and-conquer (e.g. merge sort) suffices.
If both are present → DP.

---

## Greedy Pitfall — "It Works on Examples"

The most common interview mistake: greedy works on all the test cases you try,
so you assume it's correct. Always try to construct a counter-example by:

1. Using denominations/weights that are close together (e.g. `[1, n-1, n]`).
2. Targeting an amount where two medium coins beat one large coin.
3. Making the greedy's first choice leave a remainder that has no efficient solution.

If you can't construct a counter-example AND the interchange argument holds
informally → greedy is likely safe.

---

## Summary

| Signal | Use |
|--------|-----|
| Locally optimal = globally optimal (provable) | Greedy |
| Overlapping subproblems + optimal substructure | DP |
| Items/choices are indivisible and interact | DP |
| Problem has matroid structure | Greedy |
| Counter-example exists for greedy | DP |
| Can't find counter-example + interchange argument holds | Greedy |

---

## Further Reading

- Matroid theory and greedy algorithms: Oxley — *Matroid Theory*, Oxford University Press
- Cormen et al. — *Introduction to Algorithms (CLRS)*, Chapter 16 (Greedy) and Chapter 15 (DP)
- Kleinberg & Tardos — *Algorithm Design*, Chapters 4 (Greedy) and 6 (DP)
  — contains the clearest treatment of the interchange argument
