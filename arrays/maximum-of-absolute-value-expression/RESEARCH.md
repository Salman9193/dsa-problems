# Maximum of Absolute Value Expression — Research & Foundations

This problem rests on a **mathematical transform** rather than a single seminal paper:
the equivalence between the **L1 (Manhattan/taxicab)** and **L∞ (Chebyshev)** metrics,
and the computational-geometry problem of a point set's **diameter**. The honest
foundations below.

- **The L1 ↔ L∞ (Manhattan ↔ Chebyshev) transform.** In the plane, rotating and scaling
  by 45° maps the L1 metric to the L∞ metric via `(x, y) → (x+y, x−y)`. Under L∞, the
  maximum pairwise distance decomposes into independent per-coordinate max−min spans —
  which is exactly the sign-expansion this solution performs, generalised to `2^d`
  coordinate combinations. This is classical/folklore, documented at:
  https://en.wikipedia.org/wiki/Chebyshev_distance and
  https://en.wikipedia.org/wiki/Taxicab_geometry

- **Minkowski (Lᵖ) distances.** The Manhattan (p=1) and Chebyshev (p=∞) metrics are the
  endpoints of the Minkowski family, the general foundation for these distance
  transforms. https://en.wikipedia.org/wiki/Minkowski_distance

- **Diameter of a point set (computational geometry).** Finding the farthest pair under
  a metric is the *diameter* problem. Under L1/L∞ it has an efficient closed form (the
  sign-expansion / coordinate-span method); the standard textbook reference is **M. de
  Berg, O. Cheong, M. van Kreveld & M. Overmars, *Computational Geometry: Algorithms and
  Applications*, 3rd ed., Springer, 2008.**

**Why this is honest:** unlike the other problems in this set, there isn't a single
landmark paper — the technique is a classical metric transform. The value is recognising
that "maximise a sum of absolute values" is the L1-diameter problem, solvable via the
L1→L∞ decomposition, in `2^d` linear sweeps.
