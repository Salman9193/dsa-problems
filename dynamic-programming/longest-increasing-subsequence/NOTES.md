# Longest Increasing Subsequence — Notes & Intuition

**LeetCode #300** | Dynamic Programming / Binary Search | Medium

---

## Problem

Find the length of the longest strictly increasing subsequence.
(Not necessarily contiguous.)

```
[10,9,2,5,3,7,101,18] → 4   ([2,3,7,101] or [2,3,7,18])
[0,1,0,3,2,3]         → 4
[7,7,7,7,7]           → 1
```

---

## Approach 1 — O(n²) DP

```
dp[i] = length of longest IS ending at index i
dp[i] = max(dp[j]+1) for all j<i where nums[j]<nums[i]
Base: dp[i] = 1 (just the element itself)
```

Intuition: for each element, scan backwards for all smaller elements that
can precede it, take the best.

---

## Approach 2 — O(n log n) Patience Sorting

**The tails array:** `tails[k]` = smallest possible tail of any IS of length k+1.

For each `num`:
- Binary search for first `tails[pos] >= num`
- If `pos == tails.size()`: num extends the longest IS → append
- Else: replace `tails[pos] = num` → smaller tail, more room to grow

```
nums=[2,5,3,7]:
  2: tails=[2]
  5: tails=[2,5]        (5 > 2 → extend)
  3: tails=[2,3]        (3 replaces 5 at pos=1 → smaller tail for IS of length 2)
  7: tails=[2,3,7]      (7 > 3 → extend)
return 3 ✓
```

**Key caveat:** `tails` is **NOT** the actual LIS — it's a structural artefact of the greedy,
and is usually not even a subsequence of the input in order. (For `[3,4,5,1]`, `tails` ends as
`[1,4,5]`, but `1` comes *last* in the input.) It gives the right *length*, never the sequence.
To recover the sequence itself, see **Reconstructing the LIS** below — it can be done in
O(n log n), contrary to a common misconception.

---

## Reconstructing the LIS (not just its length)

### From the O(n²) DP — parent pointers

`dp[i]` = LIS length ending at `i`. Record which predecessor achieved it, then walk back.

```java
int[] dp = new int[n], parent = new int[n];
Arrays.fill(dp, 1); Arrays.fill(parent, -1);
int best = 0;
for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++)
        if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) { dp[i] = dp[j] + 1; parent[i] = j; }
    if (dp[i] > dp[best]) best = i;
}
// walk parent[] back from `best`, then reverse
```

### From the O(n log n) patience version — parent indices

The misconception is that you *can't* reconstruct here. You can. The fix: keep `tails` as
**indices**, plus a `parent[]` array.

```java
int[] tailIdx = new int[n];   // tailIdx[len-1] = index of the smallest tail of an LIS of length len
int[] parent  = new int[n];   // parent[i] = predecessor index in the LIS ending at i
Arrays.fill(parent, -1);
int size = 0;

for (int i = 0; i < n; i++) {
    int lo = 0, hi = size;                        // lower_bound on tail VALUES
    while (lo < hi) {
        int mid = (lo + hi) >>> 1;
        if (nums[tailIdx[mid]] < nums[i]) lo = mid + 1; else hi = mid;
    }
    if (lo > 0) parent[i] = tailIdx[lo - 1];      // ← the key line
    tailIdx[lo] = i;
    if (lo == size) size++;
}
// walk parent[] back from tailIdx[size - 1], then reverse
```

**Why `parent[i] = tailIdx[lo - 1]` is correct:** when `nums[i]` lands at position `lo`, it
extends an increasing subsequence of length `lo`, whose best tail is currently `tailIdx[lo-1]`.
That entry was finalized *before* `i` was processed, so the pointer is valid — even though later
writes overwrite `tailIdx[lo]` itself. Reconstruction is then a simple parent-walk from the last
element of the longest chain.

**O(n log n) time, O(n) space** — the length *and* the sequence.

---

## The Patience Sorting Connection

LIS ≡ the patience sorting card game:
- Deal cards one at a time
- Place each card on the leftmost pile whose top card ≥ current card
- If no such pile: start a new pile
- **Number of piles = LIS length**

This is identical to the `tails` array (each pile top *is* a tail).

---

## Where Dilworth's Theorem Comes In (why patience sorting is *optimal*)

The piles aren't just a trick — their optimality is a theorem. Model the sequence as a
**partially ordered set**: for indices `i < j`, say

```
(i, nums[i])  ≺  (j, nums[j])     iff    i < j  AND  nums[i] < nums[j]
```

Under this order:

| Poset concept | What it is in the sequence |
|---------------|---------------------------|
| **Chain** (pairwise comparable) | an **increasing subsequence** |
| **Antichain** (pairwise incomparable) | a **decreasing subsequence** (since `i < j` but `nums[i] ≥ nums[j]`) |

So **LIS = the longest chain** (the poset's *height*).

Now the two dual theorems — and *which* one applies depends on how you orient the poset:

- **Orientation A** — comparable iff `i < j` **and** `nums[i] < nums[j]` (as above). Then
  chains = increasing subsequences, antichains = decreasing ones, and **LIS = the longest
  chain**. The relevant theorem is **Mirsky's (1971)**: *longest chain = minimum number of
  antichains partitioning the poset* → **LIS = the minimum number of decreasing subsequences
  covering the sequence.**

- **Orientation B** — comparable iff `i < j` **and** `nums[i] > nums[j]`. Now chains =
  decreasing subsequences, antichains = increasing ones, and **LIS = the largest antichain**.
  The relevant theorem is **Dilworth's (1950)**: *largest antichain = minimum number of chains
  covering the poset* → **the same conclusion**.

Both orientations yield the identical statement — **LIS = the minimum number of decreasing
subsequences covering the sequence** — which is exactly the patience **pile count** (each pile
*is* a decreasing subsequence). So citing either theorem is legitimate; they are duals of each
other, and the choice is just a convention about which relation you call the order.

---

## What the Theorem Actually Does (it is *not* an algorithm)

Dilworth/Mirsky **compute nothing**. They supply a **min-max duality** that proves the greedy
is optimal. Patience sorting produces `k` piles, and:

1. **Upper bound (pigeonhole / duality).** Every pile is *decreasing*, so an increasing
   subsequence can take **at most one element per pile** — two from one pile would be
   decreasing. With `k` piles: **LIS ≤ k**.
2. **Lower bound (construction).** Each card on pile `j` back-points to a smaller, earlier card
   on pile `j−1`. Following those pointers yields an actual increasing subsequence of length
   `k`: **LIS ≥ k**.

Hence **LIS = k**. The piles are simultaneously *an answer* and *a certificate that no better
answer exists* — the same shape as an LP dual, or a cut certifying a max-flow. **Patience
sorting is a constructive proof of the duality**, and that is why the greedy is provably
optimal rather than merely a good heuristic. (Note the back-pointers in step 2 are exactly the
`parent[]` array used for reconstruction above.)

**Erdős–Szekeres falls straight out.** If a sequence has `n > (r−1)(s−1)` elements and its LIS
were ≤ `r−1`, then by Mirsky it could be covered by ≤ `r−1` decreasing subsequences; by
pigeonhole one of them has ≥ `s` elements. Hence **every sequence of length `(r−1)(s−1)+1` has
an increasing subsequence of length `r` or a decreasing one of length `s`** — the classic 1935
result, a two-line corollary of the chain/antichain duality.

**Repo link:** the same poset machinery underlies
[Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) — there the
partial order is *divisibility*, and the answer is again the **longest chain**.

---

## Comparison

| | O(n²) DP | O(n log n) Patience |
|--|----------|---------------------|
| Time | O(n²) | O(n log n) |
| Space | O(n) | O(n) |
| Reconstruct LIS? | Yes (parent pointers) | **Yes** — tail *indices* + parent[] (see above) |
| Use when | n ≤ 1000 | n ≤ 100,000 |

---

## Complexity

O(n²) DP: Time O(n²) · Space O(n)
Patience: Time O(n log n) · Space O(n)

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #354 Russian Doll Envelopes | 2D LIS | Sort by width↑, height↓; LIS on height |
| #673 Number of LIS | Count all LIS | O(n²) DP tracking count + length |
| Longest Non-Decreasing Subsequence | Allow equal | Change < to <= in binary search |
| Longest Decreasing Subsequence | Reverse direction | Negate all values, run LIS |
