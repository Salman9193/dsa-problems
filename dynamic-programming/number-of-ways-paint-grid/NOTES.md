# Number of Ways to Paint N×3 Grid — Notes & Intuition

**LeetCode #1411** | Dynamic Programming / Combinatorics | Hard

---

## Problem

Count ways to paint an n×3 grid with 3 colours such that no two adjacent
cells (horizontally or vertically) share the same colour. Return answer mod 10^9+7.

```
n=1 → 12
n=2 → 54
n=3 → 246
```

---

## Step 1 — Observe Row Symmetry

With 3 colours and 3 cells per row, valid rows (no adjacent same colour)
fall into exactly two structural types:

```
ABA (first == third):  010, 020, 101, 121, 202, 212  → 6 patterns
ABC (all different):   012, 021, 102, 120, 201, 210  → 6 patterns
Total: 12 valid rows ✓  (matches n=1 answer)
```

**Key symmetry:** by the symmetry of the colour set, ALL 6 ABA patterns
are structurally equivalent. ALL 6 ABC patterns are structurally equivalent.
So we only track aggregate counts, not individual patterns.

---

## Step 2 — Count Compatible Successors

When stacking two rows, each column must differ between rows.
For a given row above, how many valid rows can appear below it?

**ABA above → ABA below:** 3 patterns
**ABA above → ABC below:** 2 patterns
**ABC above → ABA below:** 2 patterns
**ABC above → ABC below:** 2 patterns

### Derivation (ABA = 010 above):

```
col0_below ≠ 0 → {1,2}
col1_below ≠ 1 → {0,2}
col2_below ≠ 0 → {1,2}
Also: col0≠col1, col1≠col2

Enumerate:
  (1,0,1) ABA ✓
  (1,0,2) ABC ✓
  (1,2,1) ABA ✓
  (2,0,1) ABC ✓
  (2,0,2) ABA ✓

3 ABA + 2 ABC ✓
```

By symmetry, every ABA pattern has exactly 3+2 compatible successors,
and every ABC pattern has exactly 2+2.

---

## Step 3 — The DP Recurrence

```java
long aba = 6, abc = 6;  // base case: n=1

for each additional row:
    newAba = aba * 3 + abc * 2;  // ABA rows below
    newAbc = aba * 2 + abc * 2;  // ABC rows below
    aba = newAba; abc = newAbc;

return (aba + abc) % MOD;
```

---

## Verification

| n | aba | abc | total |
|---|-----|-----|-------|
| 1 | 6 | 6 | **12** ✓ |
| 2 | 30 | 24 | **54** ✓ |
| 3 | 138 | 108 | **246** ✓ |

---

## The Transfer Matrix View

The recurrence is a linear map on the vector [aba, abc]:

```
[aba_{n}]   [3 2]   [aba_{n-1}]
[abc_{n}] = [2 2] × [abc_{n-1}]
```

The matrix T = [[3,2],[2,2]] is the **transfer matrix** of the P₃ □ Pₙ
grid graph under 3-colouring. After n-1 multiplications:

```
[aba_n, abc_n] = T^(n-1) × [6, 6]
```

For n ≤ 1000 (problem constraint), O(n) iteration suffices.
For very large n (n = 10^18), use **matrix exponentiation** → O(log n) time.

---

## Why This Works — Formal Justification

The ABA/ABC classification works because:
1. **Valid row set has exactly 12 elements** — proved by enumeration
2. **Colour symmetry** — any permutation of {0,1,2} maps ABA→ABA and ABC→ABC
3. **Transition counts are invariant** — every ABA row has exactly 3 ABA + 2 ABC
   successors regardless of which specific ABA row it is (proved by symmetry)

These three facts together mean the 12-dimensional state space collapses to
a 2-dimensional one — this is why two variables suffice.

---

## Complexity

| | |
|--|--|
| Time | O(n) — n iterations of the recurrence |
| Space | O(1) — two variables |
| With matrix exponentiation | O(log n) — for very large n |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| k colours instead of 3 | More colours | Recount ABA/ABC equivalents for k colours |
| n×4 grid | Wider rows | More row types; larger transfer matrix |
| n×m general grid | Arbitrary width | Transfer matrix of size O(k^m); exponential in m |
| Toroidal grid | Wrap-around horizontally | Add periodic boundary condition |
| Very large n (10^18) | Can't iterate n times | Matrix exponentiation O(log n) |
| Count only ABA-ending configs | Specific ending type | Return just `aba` |

---

## Edge Cases

| n | Output | Notes |
|---|--------|-------|
| 1 | 12 | Direct count of valid 1-row patterns |
| 2 | 54 | Verified by recurrence |
| Large n | Grows exponentially, taken mod 10^9+7 | long arithmetic prevents overflow |
