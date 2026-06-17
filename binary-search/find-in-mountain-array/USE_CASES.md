# Find in Mountain Array — Real-World Use Cases

The peak-finding binary search and the three-step unimodal search strategy
underpin one of the most important algorithms in numerical optimisation —
the Golden Section Search — and its discrete variant used across competitive
programming, robotics, and signal processing.

---

## 1. Unimodal Function Optimisation — Golden Section Search (Kiefer 1953)

The peak-finding step in this problem is the discrete version of the
**Golden Section Search** — the classical derivative-free algorithm for
finding the maximum of a unimodal function on a continuous interval.

### The connection

```
Mountain array (discrete):
  find peak of arr[0..n-1] in O(log n) calls
  → binary search comparing arr[mid] vs arr[mid+1]

Golden Section Search (continuous):
  find peak of f(x) on [a,b] without derivatives
  → evaluate at two interior points, eliminate the non-peak third
  → converges in O(log_{1/φ}(ε)) evaluations where φ = 1.618...
```

Both algorithms:
- Operate on unimodal functions (single peak)
- Find the peak without evaluating all points
- Reduce the search space by a constant factor per iteration
- Require only comparisons between function values (no derivatives)

### Why Golden Section, not bisection?

The golden-section search maintains four points whose three interval widths are in the ratio φ:1:φ where φ is the golden ratio. These ratios are maintained for each iteration and are maximally efficient. Fibonacci search and golden-section search were discovered by Kiefer (1953).

The convergence rate of golden section search is a constant 1/φ = 0.61803 — the search space is reduced by 38.196% each iteration. Binary search converges with rate 1/2. The mountain array problem is a special case where both input and output are discrete integer values — the peak-finding is a discrete unimodal function search.

Golden section search reduces the interval by ~38.2% per step vs ~50% for
binary search — but it requires only ONE new function evaluation per step
(reuses one evaluation from the previous step), making it more call-efficient.

### Applications of unimodal optimisation

- **Hyperparameter tuning:** finding the optimal learning rate, regularisation
  parameter, or bandwidth that maximises model performance (unimodal in many cases)
- **Signal processing:** finding the peak frequency in a spectrum
- **Economics:** finding the price that maximises revenue (unimodal demand curve)
- **Robotics:** optimising trajectory parameters with a unimodal cost function

### References

- **Original paper:** Kiefer, J. — *Sequential minimax search for a maximum*,
  Proceedings of the American Mathematical Society, 4(3):502–506, 1953.
  https://doi.org/10.2307/2032161

- **Wikipedia — Golden-section search:**
  https://en.wikipedia.org/wiki/Golden-section_search

- **Paper:** Kodnyanko — *Optimal Section Method for Minimizing Unimodal
  Functions*, Computational Mathematics and Modeling, 36:235–252, 2025.
  https://link.springer.com/article/10.1007/s10598-025-09641-z
  "Comparison of the new method with bisection search and golden section search
  on twenty unimodal functions of various types — the passive algorithm is 2.26×
  faster than bisection and 1.72× faster than golden section."

---

## 2. Ternary Search / Binary Search on Unimodal Functions

The peak-finding binary search (comparing `arr[mid]` vs `arr[mid+1]`) is
the standard technique taught in competitive programming for searching
unimodal discrete functions — used in robotics path planning, game AI,
and geometric optimisation.

If a function is unimodal — strictly increases first then reaches a maximum then strictly decreases — we can use either ternary search or binary search to find the peak efficiently. Binary search on the derivative (comparing adjacent values) is equivalent to and often easier to implement than ternary search for discrete unimodal functions.

### Key insight: binary search on the derivative

```
arr[mid] < arr[mid+1]  →  derivative is positive  →  still ascending  →  peak is right
arr[mid] > arr[mid+1]  →  derivative is negative  →  past peak        →  peak is here or left
```

This is exactly the peak-finding step in this problem — binary search on
the sign of the discrete derivative.

### Applications

- **Robot arm optimisation:** finding the joint angle that maximises reach
  (unimodal in many configurations) — uses binary search on the derivative
- **Game AI (minimax):** finding the move that maximises score in a
  unimodal game tree layer — ternary/binary search on the evaluation function
- **GPS routing:** finding the mountain pass (peak elevation between two
  valleys) on an elevation profile — peak-finding binary search

### References

- **USACO Guide — Optimizing Unimodal Functions:**
  https://usaco.guide/gold/ternary-search
  "If our function is unimodal, we can use binary search to find the peak
  efficiently. Binary search on the derivative (comparing adjacent values)
  is equivalent to and often easier to implement than ternary search."

- **UIC Math — Golden Section Search derivation:**
  https://homepages.math.uic.edu/~jan/mcs471f05/Lec9/gss.pdf

---

## The Unified Pattern — Unimodal Search

All three search strategies in this problem generalise from discrete arrays
to continuous functions:

| Step | Discrete (this problem) | Continuous (Golden Section Search) |
|------|------------------------|-----------------------------------|
| Find peak | Binary search on `arr[mid] vs arr[mid+1]` | Evaluate at 2 interior points, compare |
| Search left | Standard binary search (ascending) | Not needed — peak IS the answer |
| Search right | Flipped binary search (descending) | Not needed — peak IS the answer |

The "find peak + search each half" pattern from this problem is a strict
generalisation — when you also need to find a target (not just the peak),
you need all three steps.

---

## Further Reading

- Kiefer 1953 (golden section search): https://doi.org/10.2307/2032161
- Golden-section search (Wikipedia): https://en.wikipedia.org/wiki/Golden-section_search
- Optimal section method (Kodnyanko 2025): https://link.springer.com/article/10.1007/s10598-025-09641-z
- USACO ternary/binary search on unimodal: https://usaco.guide/gold/ternary-search
- UIC golden section derivation: https://homepages.math.uic.edu/~jan/mcs471f05/Lec9/gss.pdf
