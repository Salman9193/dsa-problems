# Russian Doll Envelopes — Notes & Intuition

**LeetCode #354** | Dynamic Programming (2-D LIS) | Hard
An envelope fits inside another only if **both** width and height are strictly greater.

---

## Problem

Given envelopes `[w, h]`, find the maximum number that can be nested (Russian-doll style).
Nesting requires **strictly** greater width **and** height.

```
Input:  [[5,4],[6,4],[6,7],[2,3]]
Output: 3        →  [2,3] ⊂ [5,4] ⊂ [6,7]
```

---

## The Key Insight — Collapse One Dimension, Then It's Just LIS

Nesting is a **2-D** condition, which looks harder than LIS. But if you **sort by width**, the
width condition is handled by the ordering itself — and what remains is: *find the longest
strictly increasing subsequence of heights*. That's plain LIS.

```
sort by width ascending  →  run LIS on the heights
```

---

## The Trap — Equal Widths (the whole problem)

Two envelopes with the **same width can never nest** (width must be *strictly* greater). But
LIS on heights doesn't know that — it would happily chain two equal-width envelopes if their
heights increase.

**The fix:** for equal widths, sort heights **descending**.

Then equal-width heights form a *decreasing* run, and an increasing subsequence **structurally
cannot pick two of them**. The illegal nesting becomes impossible by construction.

```java
Arrays.sort(envelopes, (a, b) ->
    a[0] != b[0] ? a[0] - b[0]     // width ascending
                 : b[1] - a[1]);   // height DESCENDING on ties  ← the trick
```

### Proof that the descending tiebreak is mandatory

| Input | Correct | With height **ascending** tiebreak |
|-------|---------|-----------------------------------|
| `[[4,5],[4,6],[6,7],[2,3],[1,1]]` | **4** | **5** ✗ (chains both width-4 envelopes) |
| `[[2,100],[3,200],[4,300],[5,250],[5,400],[5,500],[6,360],[6,370],[7,380]]` | **5** | **6** ✗ |

Get the tiebreak wrong and you silently overcount. This is *the* reason the problem is Hard.

---

## The Algorithm

```java
public int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) ->
        a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);   // width ↑, height ↓ on ties

    // LIS (strict) on heights — patience sorting, O(n log n)
    List<Integer> tails = new ArrayList<>();
    for (int[] e : envelopes) {
        int h = e[1];
        int pos = lowerBound(tails, h);              // first tail >= h  (strict LIS)
        if (pos == tails.size()) tails.add(h);
        else tails.set(pos, h);
    }
    return tails.size();
}
```

**O(n log n)** time, **O(n)** space. (An O(n²) DP works too but TLEs at `n = 10⁵`.)

---

## Full Trace — `[[5,4],[6,4],[6,7],[2,3]]`

Sorted (width ↑, height ↓): `[2,3], [5,4], [6,7], [6,4]`
*(note the two width-6 envelopes: `[6,7]` comes before `[6,4]`)*

Heights: `3, 4, 7, 4` → LIS:

| h | tails |
|---|-------|
| 3 | `[3]` |
| 4 | `[3,4]` |
| 7 | `[3,4,7]` |
| 4 | `[3,4,7]` → 4 replaces 7? No — `lowerBound(4)` = index 1 → `[3,4,7]` unchanged |

Length **3**. ✓ And crucially `[6,4]` could not extend `[6,7]` — the descending tiebreak
prevented it.

---

## Why Patience Sorting *Works* Here (but not for Largest Divisible Subset)

The nesting relation is a **2-dimensional poset** (width × height, each a total order). Sorting
collapses one dimension, leaving a plain total-order LIS on the other — so the O(n log n)
greedy + binary search applies.

Contrast [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset):
divisibility is **not** a 2-D poset, the "smallest tail is always most extendable" property
fails, and the greedy collapses — it's stuck at O(n²).

| Problem | Poset | Patience sort? |
|---------|-------|----------------|
| [LIS #300](#dynamic-programming/longest-increasing-subsequence) | position × value (2-D) | ✅ O(n log n) |
| **Russian Doll #354** | width × height (2-D) | ✅ O(n log n) *after the sort trick* |
| [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) | divisibility (higher dim.) | ❌ stuck at O(n²) |

**Whether the greedy works is a property of the poset's dimension, not of the problem's
wording.** (See Research & Foundations.)

---

## Edge Cases

| Case | Handling |
|------|----------|
| all identical envelopes | 1 (none nest) |
| equal widths, varying heights | at most one is usable — the tiebreak enforces it |
| single envelope | 1 |
| strictly increasing in both dims | all of them |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| 3-D nesting (boxes) | 3 dimensions | sorting no longer collapses it to LIS — needs O(n²) DP (the poset is 3-D) |
| Reconstruct the nesting | need the envelopes | keep tail *indices* + parent[] (as in LIS) |
| Non-strict nesting (≥) | equal allowed | sort height *ascending* on ties; use upper_bound |
| Maximum total area nested | weighted chain | longest *weighted* path in the DAG |

**The through-line:** sort by width ascending with height **descending** on ties, then run LIS
on heights — the descending tiebreak is what makes equal-width envelopes structurally
unchainable.
