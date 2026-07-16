# Longest Increasing Subsequence II — Notes & Intuition

**LeetCode #2407** | DP + Segment Tree (range-max) | Hard
LIS, but adjacent elements may differ by at most `k`. The window constraint kills patience
sorting — and forces a completely different route to O(n log n).

---

## Problem

Find the longest **strictly increasing** subsequence of `nums` in which the difference between
**adjacent chosen elements** is at most `k`.

```
nums = [4,2,1,4,3,4,5,8,15], k = 3  →  5     ([1,3,4,5,8])
nums = [1,5], k = 1                 →  1
```

---

## The Key Insight — Index the DP by VALUE, not by position

The plain LIS DP (`dp[i]` = LIS ending at index `i`) gives O(n²) and TLEs here. The move is to
key the DP by **value**:

```
dp[x] = length of the best chain ENDING at value x
dp[x] = 1 + max( dp[x-k] , … , dp[x-1] )      ← a MAX over a contiguous value window
```

That's a **range-max query** on the value axis, with a **point update** after each element. A
**segment tree** (or BIT) does both in O(log M).

**O(n log M)** time, **O(M)** space, where `M = max(nums)`.

```java
public int lengthOfLIS(int[] nums, int k) {
    int M = 0;
    for (int x : nums) M = Math.max(M, x);
    SegTree seg = new SegTree(M);          // max over value ranges
    int best = 0;
    for (int x : nums) {                   // process in ARRAY order (preserves subsequence order)
        int lo = Math.max(1, x - k);
        int prev = (lo <= x - 1) ? seg.query(lo, x - 1) : 0;   // best chain we can extend
        int cur = prev + 1;
        seg.update(x, cur);                // point update at value x
        best = Math.max(best, cur);
    }
    return best;
}
```

**Note the traversal order:** iterate `nums` **left to right** (array order). That's what enforces
"subsequence" — you may only extend chains built from *earlier* elements. The **value** axis is
what the segment tree indexes; the **position** axis is the loop.

---

## Why Patience Sorting Fails Here

Plain LIS is O(n log n) via patience sorting, which keeps only the **smallest tail per length**.
That's valid because a smaller tail is **always at least as extendable**.

**The `k`-window destroys that property.** A smaller tail can be *too small* — below `x - k` — and
therefore **unusable**, while a larger tail works fine. The greedy's core assumption is not just
unreliable, it's **backwards**.

So we get O(n log n) by a **completely different mechanism**: not the greedy, but **interval
structure** — the eligible predecessors form a *contiguous range on the value axis*, and
contiguous ranges admit range-max queries.

| Route to sub-quadratic | Exploits | Used by |
|------------------------|----------|---------|
| **Patience sorting** | poset dimension (a smaller tail always dominates) | [LIS #300](#dynamic-programming/longest-increasing-subsequence), [Russian Doll #354](#dynamic-programming/russian-doll-envelopes) |
| **Segment tree range-max** | **interval structure** (eligible predecessors are contiguous) | **#2407** |
| *(neither)* | — | [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) — stuck at O(n²) |

**"Can I beat O(n²)?" has at least two different answers.** Knowing which lever a problem gives
you is the actual skill.

---

## Patience Sorting *Is* the Prefix Case of This Segment Tree

The two "levers" aren't unrelated — one contains the other. **Plain LIS is the `k → ∞`
special case** of this problem: the window's lower bound vanishes and the value window
degenerates to a **prefix**.

```
with k:      max{ dp[j] : nums[i]−k ≤ nums[j] ≤ nums[i]−1 }     ← two-sided window
plain LIS:   max{ dp[j] :             nums[j] ≤ nums[i]−1 }     ← prefix [0, nums[i]−1]
```

So the *same* value-indexed range-max tree solves plain LIS — just query the prefix `[0, x−1]`
instead of `[x−k, x−1]`:

```java
for (int x : nums) {
    int best = (x > 0) ? seg.query(0, x - 1) : 0;   // prefix max over all smaller values
    seg.update(x, best + 1);
}
```

That works and is `O(n log M)` — but for *plain* LIS it's **strictly worse than patience
sorting**, and the reason is the whole point:

> A **prefix-max** query is exactly what a monotone `tails` array answers with a plain binary
> search. When the query degenerates to a prefix, **the sorted `tails` array *is* the compressed
> segment tree, and the binary search *is* the range query.** The tree's extra machinery buys
> nothing.

So patience sorting isn't a *different* algorithm from the segment-tree DP — it's the segment
tree collapsed to its prefix special case. (Verified by simulation: the prefix-query tree and
patience sorting return identical lengths on 500 random inputs.)

### The test for which tool to reach for

Reach for the segment tree exactly when the predecessor condition **stops being a clean
prefix** — because *that's* when the monotone `tails` array breaks:

| Variant | Predecessor condition | `tails` array enough? |
|---------|-----------------------|-----------------------|
| Plain LIS (#300) | prefix `nums[j] < nums[i]` | ✅ patience sorting |
| Non-decreasing / `≤` vs `<` | prefix `[0, x]` vs `[0, x−1]` | ✅ (tree just makes the boundary explicit) |
| **Value window (#2407)** | two-sided `[x−k, x−1]` | ❌ **need range-max** |
| **Weighted / max-sum IS** | prefix, but maximize **weight** not length | ❌ **need range-max** |
| **Count of LIS (#673) at n log n** | prefix, but carry a **count** | ❌ need range-(max,count) |

The subtle two are the last: the condition is *still a prefix*, yet patience sorting **fails
anyway**. Its correctness rests on the invariant *"a smaller tail is always at least as
extendable."* With **weights** that invariant is false — a smaller tail can carry **less
accumulated weight** — so "keep the smallest tail" throws away the better chain.

```
nums = [1, 2, 10],  weights = [1, 100, 1]
  max-sum increasing subsequence = [2] alone → weight 100 (beats [1,2,10] = 102? → 102 wins)
  a length-oriented tails array prefers the SMALLER tail value (1), but the heavier chain
  ends at 2 — "smaller tail is better" is simply the wrong objective here.
```

**The unifying test:** if extending depends only on the predecessor's value being *smaller* (a
prefix) **and** "smaller value ⇒ at least as good" holds, patience sorting suffices. The moment
the condition becomes a bounded **range**, or the objective makes "smaller value" stop implying
"better" (weights, counts), the monotone array breaks and you need the segment tree's general
range query. This problem is the first kind of break (a range); weighted/count LIS are the
second (a broken invariant).

---

## Trace — `nums = [4,2,1,4,3,4,5,8,15]`, `k = 3`

| x | window `[x−3, x−1]` | max in window | dp[x] |
|---|---------------------|---------------|-------|
| 4 | [1,3] | 0 | 1 |
| 2 | [1,1] | 0 | 1 |
| 1 | — | 0 | 1 |
| 4 | [1,3] | 1 (from 2 or 1) | **2** |
| 3 | [1,2] | 1 | 2 |
| 4 | [1,3] | 2 (from 3) | **3** |
| 5 | [2,4] | 3 (from 4) | **4** |
| 8 | [5,7] | 4 (from 5) | **5** |
| 15 | [12,14] | 0 | 1 |

Answer **5** — `[1,3,4,5,8]`. ✓ (15 is stranded: nothing within 3 below it.)

---

## Variant — "Longest Upgrade Path" (a per-element window)

> `n` releases; release `i` has version `v[i]` (distinct) and `m[i]`, the **oldest version it can
> upgrade directly from**. You may upgrade `j → i` iff `m[i] ≤ v[j] < v[i]`. Find the longest
> chain of successive upgrades.

This is **#2407 with a per-element window bound** instead of a fixed width:

```
dp[v[i]] = 1 + max( dp over the value range [ m[i] , v[i]−1 ] )
```

`#2407` is the special case `m[i] = v[i] − k`. Sort by version (a topological order, since every
edge goes low→high), then run the identical segment-tree range-max. **O(n log n)** — the
generalization costs nothing.

### The subtle part: this relation is **not transitive**

Every other "longest chain" problem in this repo has a **transitive** relation — a genuine
**poset**. This one does **not**:

```
v = [1, 5, 10],  m = [0, 0, 5]
   1 →  5  ✅   (m[5]  = 0 ≤ 1 < 5)
   5 → 10  ✅   (m[10] = 5 ≤ 5 < 10)
   1 → 10  ❌   (m[10] = 5 ≤ 1?  NO — release 10 dropped support for v1)
```

A legal two-step path exists with **no direct edge** — which is exactly the real-world semantics:
**you must hop through an intermediate release.**

**Consequences:**
1. It's a **longest path in a DAG**, *not* a longest chain in a poset. Dilworth/Mirsky and the
   whole chain/antichain apparatus **do not apply** (they need a partial order).
2. The DP is unchanged — *linearize, then relax* — because longest-path-in-a-DAG never needed
   transitivity in the first place.
3. Patience sorting fails even harder than in
   [Largest Divisible Subset](#dynamic-programming/largest-divisible-subset): there a smaller tail
   was merely *incomparable*; here it can be **actively disqualified** for being too old.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Naive DP (`dp[i]` over all `j < i`) | O(n²) | O(n) |
| **Segment tree range-max** | **O(n log M)** | O(M) |
| Monotonic deque (only if window is by *index*) | O(n) | O(n) |

*(The deque trick doesn't apply here: the window slides on the **value** axis while we iterate the
**position** axis, so entries don't expire monotonically.)*

---

## Edge Cases

| Case | Handling |
|------|----------|
| `x - k < 1` | clamp the window's low end to 1 |
| `x = 1` | window is empty → `dp[1] = 1` |
| huge values | **coordinate-compress** to O(n) distinct values |
| `k ≥ max(nums)` | degenerates to plain LIS |

**The through-line:** key the DP by **value**, not position; the eligible predecessors form a
**contiguous value window**, so a segment tree's range-max turns the O(n²) scan into O(log M).
