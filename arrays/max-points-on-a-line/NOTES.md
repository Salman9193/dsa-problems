# Max Points on a Line — Notes & Intuition

**LeetCode #149** | Arrays / Geometry | Hard
Points are unique. Coordinates in [-10⁴, 10⁴]. Return the most points on one straight line.

---

## Problem

Given `n` unique 2D points, return the maximum number that lie on the same straight line.

```
Input:  [[1,1],[2,2],[3,3]]           Output: 3
Input:  [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]   Output: 4
```

---

## The Idea — Anchor + Group by Slope

Three or more points are collinear iff they share the same slope **through a common
point**. So:

1. **Fix each point `i` as an anchor.**
2. Compute the slope from `i` to every other point `j`.
3. Count how many `j` share each slope — the biggest group **+ the anchor** is the best
   line through `i`.
4. Take the max over all anchors.

A line with `k` points is found when its lowest-index point is the anchor (the other
`k−1` appear as one slope group), so scanning `j > i` suffices.

---

## The Real Trap — Don't Use Floating-Point Slope

`dy/dx` as a `double` loses precision and merges near-parallel lines incorrectly.
Represent slope as a **reduced integer pair `(dx, dy)`** divided by their GCD, with a
**canonical sign** so `(dx, dy)` and `(−dx, −dy)` map to one key:

- reduce: `g = gcd(dx, dy)`, then `dx /= g`, `dy /= g`
- normalize sign: if `dx < 0` (or `dx == 0 && dy < 0`), negate both

Vertical (`dx = 0`) and horizontal (`dy = 0`) lines then fall out for free.

```java
int dx = xj - xi, dy = yj - yi;
int g = gcd(dx, dy);               // g >= 1 since points are unique
dx /= g; dy /= g;
if (dx < 0 || (dx == 0 && dy < 0)) { dx = -dx; dy = -dy; }
long key = (long) dx * 200003L + dy;   // collision-free: |dy| < 200003
```

---

## Full Approach

```
best = 2                                 // any two points form a line
for each anchor i:
    slopeCount = {}                      // reduced (dx,dy) -> count
    for j > i:
        key = normalizedSlope(i, j)
        slopeCount[key]++
        best = max(best, slopeCount[key] + 1)   // +1 for the anchor
return best
```

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Check every triple | O(n³) | O(1) |
| **Anchor + slope map** | **O(n²)** | O(n) |

The O(n²) is essentially optimal here — detecting three collinear points is **3SUM-hard**
(see Research & Foundations), so a truly subquadratic algorithm would break a long-standing
barrier.

---

## Edge Cases

| Case | Handling |
|------|----------|
| n ≤ 2 | every set of ≤ 2 points is trivially collinear → return n |
| vertical line | `dx = 0` → normalized to `(0, 1)`, grouped like any slope |
| horizontal line | `dy = 0` → normalized to `(1, 0)` |
| negative slopes | canonical sign makes `(dx,dy)` and `(−dx,−dy)` one key |
| duplicate points | not possible — the constraints guarantee unique points |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Duplicate points allowed | `(dx,dy)=(0,0)` cases | track duplicates per anchor, add to every line's count |
| Max points on a line in 3D | 3 coordinates | reduce direction vectors by GCD across all 3 components |
| Count all lines with ≥ 3 points | enumerate lines | group globally by (reduced direction, canonical intercept) |
| Fewest lines to cover all points | NP-hard | the point-line-cover problem |

**The through-line:** anchor each point, bucket the rest by GCD-reduced integer slope
(never floating-point), and the biggest bucket + 1 is the best line — an O(n²) method that
matches the 3SUM-hard lower bound for collinearity.
