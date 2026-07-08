# Maximum of Absolute Value Expression — Notes & Intuition

**LeetCode #1131** | Arrays | Medium
Constraints: 2 ≤ n ≤ 40000, |values| up to 1e6 — O(n²) is too slow, need O(n).

---

## Problem

Given two integer arrays `arr1`, `arr2` of equal length `n`, return the maximum,
over all pairs `i, j`, of:

```
|arr1[i] - arr1[j]| + |arr2[i] - arr2[j]| + |i - j|
```

```
Input:  arr1 = [1, 2, 3, 4], arr2 = [-1, 4, 5, 6]
Output: 13
```

---

## The Reframe — Manhattan Distance in 3D

Treat each index `k` as a 3D point `P_k = (arr1[k], arr2[k], k)`. The expression is
exactly the **Manhattan (L1) distance** between `P_i` and `P_j`. So the question is:
*what is the maximum L1 distance between any two of these points* — the L1 **diameter**
of the point set?

Brute force checks all O(n²) pairs — too slow for n = 40000. The trick below is O(n).

---

## The Key Insight — Expand |·| into Signs

One identity does all the work. Since `|x| = max(x, -x)`, a **sum** of absolute values
equals the max over independent sign choices of the signed sum:

```
|a| + |b| + |c| = max over s1,s2,s3 ∈ {+1,-1} of ( s1·a + s2·b + s3·c )
```

You can always choose each sign to make its own term positive, so the best sign choice
recovers the full sum of magnitudes.

Apply it with `a = arr1[i]-arr1[j]`, `b = arr2[i]-arr2[j]`, `c = i-j`. The overall
answer is a max over `i, j`, and max-of-max commutes, so pull the sign choice outside:

```
answer = max over signs [ max over i,j  ( s1·arr1[i] + s2·arr2[i] + s3·i )
                                       − ( s1·arr1[j] + s2·arr2[j] + s3·j ) ]
```

For a fixed sign combo, define `f(k) = s1·arr1[k] + s2·arr2[k] + s3·k`. The inner max
is just `f(i) − f(j)`, which is biggest when `f(i)` is the **max** and `f(j)` the
**min**:

```
for a fixed sign combo:  max(f) − min(f)   — one O(n) pass over the array
```

---

## Why Only 4 Sign Combinations

There are 2³ = 8 combinations, but negating all three signs flips `f → −f`, and
`max(−f) − min(−f) = max(f) − min(f)` — an identical value. So the 8 collapse to **4**.
Fix `s1 = +1` and vary `s2, s3 ∈ {±1}`.

---

## The Algorithm

```java
int ans = 0;
for (int s2 = 1; s2 >= -1; s2 -= 2)          // s2 = +1 then -1
  for (int s3 = 1; s3 >= -1; s3 -= 2) {       // s3 = +1 then -1  (s1 fixed = +1)
    int mx = Integer.MIN_VALUE, mn = Integer.MAX_VALUE;
    for (int k = 0; k < n; k++) {
      int val = arr1[k] + s2 * arr2[k] + s3 * k;
      mx = Math.max(mx, val);
      mn = Math.min(mn, val);
    }
    ans = Math.max(ans, mx - mn);
  }
```

---

## Full Trace — arr1 = [1,2,3,4], arr2 = [-1,4,5,6]

Combo `(s1,s2,s3) = (+,+,+)`: `f(k) = arr1[k] + arr2[k] + k`

| k | arr1[k] | arr2[k] | k | f = arr1+arr2+k |
|---|---------|---------|---|-----------------|
| 0 | 1 | -1 | 0 | 0 |
| 1 | 2 | 4 | 1 | 7 |
| 2 | 3 | 5 | 2 | 10 |
| 3 | 4 | 6 | 3 | 13 |

`max(f) − min(f) = 13 − 0 = 13`, achieved by the pair `i=3, j=0`. The other three
combos yield smaller spreads, so the answer is **13**. ✓

(On example 2 — `arr1=[1,-2,-5,0,10]`, `arr2=[0,-2,-1,-7,-4]` — the winning combo is
`(+,−,+)`, giving 20.)

---

## Edge Cases

| Case | Result | Reason |
|------|--------|--------|
| `i = j` allowed | expression = 0 | included automatically; the answer is always ≥ 0 |
| `n = 2` | \|Δarr1\| + \|Δarr2\| + 1 | only one pair to consider |
| identical arrays | 2·(n-1) | only the \|i−j\| term varies; maximised at the two ends |
| negative inputs | handled | signs are chosen per combo, so the sign of the inputs is irrelevant |

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Brute force (all pairs) | O(n²) | O(1) |
| Sign expansion | O(4n) = O(n) | O(1) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| d-dimensional Manhattan | more terms | max over 2^d sign combos, each O(n) → O(n·2^d) |
| Max Manhattan distance of 2D points | drop the \|i−j\| term | same trick with 2 signs (the L1→L∞ / Chebyshev rotation) |
| Minimum Manhattan distance | closest, not farthest | different problem — the sign trick gives MAX only; min needs divide-and-conquer / geometry |
| Weighted terms `w·\|·\|` | scaled coordinates | fold each weight into `f(k)` |
| Return the pair (i, j) | need indices | track argmax/argmin per combo, not just the values |
| Chebyshev (L∞) distance | max instead of sum | rotate coordinates: L∞ in the original space = L1 after mapping `(x, y) → (x+y, x−y)` |

**The deep idea:** "max of a sum of absolute values" is a max over sign patterns. In d
dimensions that's `2^d` linear sweeps instead of O(n²) pairwise work — the general form
of the L1 ↔ L∞ (Manhattan ↔ Chebyshev) transform.
