# Sqrt(x) — Notes & Intuition

**LeetCode #69** | Binary Search / Math | Easy
Constraint: do not use built-in sqrt().

---

## Problem

Return the integer square root of x (floor of actual sqrt).

```
4  →  2   (√4 = 2.0 exactly)
8  →  2   (√8 = 2.828... → floor = 2)
0  →  0
1  →  1
```

---

## Why Binary Search?

We want the largest integer m where m*m <= x.
As m increases, m*m increases monotonically.
This is the "rightmost element satisfying a condition" binary search pattern.

---

## Approach 1 — Binary Search

```java
int left = 1, right = x / 2;  // sqrt(x) <= x/2 for x >= 4
while (left < right) {
    int mid = left + (right - left + 1) / 2;  // upper-bias
    if (mid <= x / mid) left = mid;            // condition true → keep mid
    else right = mid - 1;                      // too large → shrink right
}
return left;
```

### Why `x / 2` as upper bound?

For x >= 4: `(x/2)^2 = x^2/4 >= x` → `x/2 >= sqrt(x)`.
So `sqrt(x) <= x/2` — halves the initial search space.

### Why `mid <= x / mid` not `mid*mid <= x`?

`mid*mid` overflows `int` when x is close to `Integer.MAX_VALUE`.
`x / mid` is always safe and equivalent.

### Why upper-bias?

We're finding the **rightmost** m satisfying `m*m <= x`:
- Condition true → `left = mid` (mid is a candidate, keep searching right)
- Condition false → `right = mid - 1`

Lower-bias causes infinite loop when `left = right - 1`:
```
mid = left (lower) → condition true → left = left → no progress
mid = right (upper) → condition decides correctly → terminates
```

Same upper-bias pattern as #911 Online Election and #875 Koko Eating Bananas.

---

## Approach 2 — Newton's Method

Newton-Raphson for `f(r) = r^2 - x = 0`:

```
r_{n+1} = (r_n + x / r_n) / 2
```

```java
long r = x;
while (r * r > x) r = (r + x / r) / 2;
return (int) r;
```

Start from `r = x` (safe overestimate), converge to `floor(sqrt(x))`.

**Why it converges so fast:**
Each iteration squares the number of correct digits (quadratic convergence).
For 32-bit integers: converges in ~5 iterations regardless of x.

---

## The Binary Search Family — Rightmost Condition

| Problem | Condition | Find |
|---------|-----------|------|
| #33 Search Rotated Array | sorted half | exact target |
| #911 Online Election | `times[mid] <= t` | rightmost ≤ t |
| **#69 Sqrt(x)** | **`mid*mid <= x`** | **floor of sqrt** |
| #875 Koko Eating Bananas | feasibility check | minimum valid rate |
| #1011 Ship Packages | capacity feasibility | minimum capacity |

All use: upper-bias mid + `left = mid` when condition holds.

---

## Full Traces

**Binary Search — x=8:**

| left | right | mid (upper) | mid <= 8/mid | action |
|------|-------|-------------|-------------|--------|
| 1 | 4 | 3 | 3 <= 2? NO | right=2 |
| 1 | 2 | 2 | 2 <= 4? YES | left=2 |
| left==right | → **return 2** |

**Newton's Method — x=8:**

| r | r*r > 8? | next r = (r + 8/r)/2 |
|---|---------|----------------------|
| 8 | 64>8 YES | (8+1)/2 = 4 |
| 4 | 16>8 YES | (4+2)/2 = 3 |
| 3 | 9>8 YES | (3+2)/2 = 2 |
| 2 | 4>8 NO | stop → **return 2** |

---

## Edge Cases

| x | Output | Reason |
|---|--------|--------|
| 0 | 0 | base case |
| 1 | 1 | base case |
| 4 | 2 | perfect square |
| 2147395600 | 46340 | near INT_MAX — overflow check critical |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Precise floating-point sqrt | Return float with ε precision | Newton's method; stop when |r² - x| < ε |
| Cube root | x^(1/3) | Newton: r_{n+1} = (2r + x/r²) / 3 |
| k-th root | x^(1/k) | Newton: r_{n+1} = ((k-1)r + x/r^(k-1)) / k |
| Integer square root with overflow | Very large x | Use long arithmetic; already done here |
| Is perfect square? (#367) | Boolean check | `isqrt(x)^2 == x` |
| Power function (#50 Pow(x,n)) | x^n with n large | Fast exponentiation: O(log n) |
| Binary search on answer pattern | Generalise sqrt idea | Same "rightmost satisfying condition" search |

**Binary search on answer:** The sqrt binary search is the canonical example of searching on the answer space rather than an array. The generalisation:
```
Condition: f(mid) ≤ target
Find: rightmost mid satisfying condition
```
Applied to: Koko Eating Bananas (f = days to eat all), Ship Packages (f = days to ship), Split Array (f = max subarray sum). All use upper-bias mid and the same `left = mid` / `right = mid - 1` structure.
