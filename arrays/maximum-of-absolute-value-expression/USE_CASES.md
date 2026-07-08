# Maximum of Absolute Value Expression — Real-World Use Cases

The expression is the **maximum Manhattan (L1) distance** between points — the L1
*diameter* of a point set. Finding it in O(n·2^d) instead of O(n²), via the
sign-expansion trick, is the general-dimension form of the classic **Manhattan ↔
Chebyshev (L1 ↔ L∞) transform** — and that shows up wherever movement or cost is
measured in axis-aligned (rectilinear) steps.

---

## 1. VLSI Physical Design — Wirelength Estimation

On a chip, wires route horizontally and vertically (rectilinearly), so the distance
between two connected cells is **Manhattan distance**, not Euclidean. Physical-design
tools estimate and optimise **wirelength** constantly: during placement, a net's cost
is a rectilinear (half-perimeter / bounding-box) length, and the farthest-apart
terminals of a net drive its critical wire length and timing.

Finding the extreme (farthest) rectilinear pair across many cells is exactly the
coordinate-spread-after-transform idea used here — relabel coordinates so a max over
sign patterns becomes independent max−min sweeps, avoiding O(n²) pairwise distance
checks across millions of cells.

- **Taxicab (Manhattan) geometry:** https://en.wikipedia.org/wiki/Taxicab_geometry
- Reference: Sherwani, *Algorithms for VLSI Physical Design Automation* — placement and
  wirelength models (half-perimeter / rectilinear metrics).

---

## 2. Grid Logistics & Rectilinear Facility Location

"Manhattan distance" is named after city blocks: on a street grid you move in
axis-aligned steps, so travel cost between two addresses is L1. Two classic problems
lean on the L1 diameter and its transform:

- **Service-area diameter** — the two demand points farthest apart in travel distance,
  a worst-case delivery or response-time bound.
- **Rectilinear minimax facility location** — placing a depot/warehouse to minimise the
  *maximum* L1 distance to any demand point. The L1 → L∞ (Chebyshev) rotation turns this
  into independent per-axis min/max problems — the same "handle each sign/axis
  separately" move as the coding trick.

- **Chebyshev distance & the 45° rotation:** https://en.wikipedia.org/wiki/Chebyshev_distance
  (rotating and scaling the L1 plane by 45° yields L∞ — the transform that makes
  max-Manhattan-distance queries cheap.)

---

## 3. L1 Diameter for Clustering & Spread Analysis

The **diameter** of a dataset — the farthest pair — under the L1 metric is a measure of
spread, used in outlier and spread analysis and as a subroutine in clustering and
k-center approximation. Naively it's O(n²); the sign-expansion transform computes it in
O(n·2^d). This is why the L1 (and L∞) diameter has an efficient closed-form sweep while
the Euclidean diameter does not decompose so cleanly.

---

## The Computational Trick (why it generalises)

```
max over pairs of   Σ_d |x_d[i] - x_d[j]|
  = max over sign patterns s ∈ {±1}^d  of  max over pairs ( Σ_d s_d·x_d[i]  −  Σ_d s_d·x_d[j] )
  = max over sign patterns  of  ( max_k f_s(k)  −  min_k f_s(k) )
    where  f_s(k) = Σ_d s_d · x_d[k]
```

Each sign pattern is one O(n) max−min sweep; there are 2^d of them (halved by
symmetry). This is the general form of the Manhattan ↔ Chebyshev transform — turning an
O(n²) pairwise search into a handful of linear passes.

| Domain | Points | Metric | What the diameter means |
|--------|--------|--------|--------------------------|
| VLSI placement | Cell / terminal locations | Manhattan (rectilinear) | Critical wire length |
| Grid logistics | Addresses on a street grid | Manhattan (city-block) | Worst-case travel distance |
| Clustering | Feature vectors | L1 | Spread / diameter of the set |

---

## Further Reading

- Taxicab (Manhattan) geometry: https://en.wikipedia.org/wiki/Taxicab_geometry
- Chebyshev distance & L1 ↔ L∞ rotation: https://en.wikipedia.org/wiki/Chebyshev_distance
- VLSI wirelength models: Sherwani, *Algorithms for VLSI Physical Design Automation*
- Rectilinear facility location: search "rectilinear minimax facility location L1 Chebyshev transform"
