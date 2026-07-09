# Max Points on a Line — Research & Foundations

The reason this is a *Hard* problem with an O(n²) solution — and no known subquadratic one
— is a genuine complexity result: detecting three collinear points is **3SUM-hard**.
*Citation verified against Computational Geometry: Theory and Applications / DOI records this
session — not from memory.*

- **A. Gajentaan & M. H. Overmars (1995), "On a class of O(n²) problems in computational
  geometry,"** *Computational Geometry: Theory and Applications* 5(3):165–185. DOI:
  10.1016/0925-7721(95)00022-2. Introduces **3SUM-hardness** and proves a large class of
  geometry problems at least as hard as **3SUM** (given `n` numbers, do any three sum to
  zero?). The paper explicitly lists *"given a set of lines in the plane, are there three
  that meet in a point?"* — the projective dual of *"are there three collinear points?"* —
  as 3SUM-hard. https://doi.org/10.1016/0925-7721(95)00022-2

**Why it matters here:** finding the maximum points on a line contains the decision problem
"are there ≥ 3 collinear points?", which is 3SUM-hard. Under the long-standing **3SUM
conjecture** (no truly subquadratic 3SUM algorithm), the O(n²) anchor-and-slope method is
essentially the best possible — so the quadratic running time is a feature of the problem's
inherent difficulty, not a weak solution.

**Related in this repo:** the same 3SUM base problem underlies the `3sum` array problem;
this is its computational-geometry incarnation. (Best known 3SUM bounds have since improved
slightly — Grønlund & Pettie, "Threesomes, degenerates, and love triangles," FOCS 2014 /
J. ACM 2018 — but remain quadratic up to subpolynomial factors.)
