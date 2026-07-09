# Max Points on a Line — Real-World Use Cases

Finding collinear points — and, more generally, fitting or detecting lines in point sets —
is a core operation in computer vision, geometry, and any system that reasons about spatial
alignment.

---

## 1. Computer Vision — Line & Edge Detection

Detecting straight lines in an image reduces to finding many near-collinear feature points.
The classic tools:

- **Hough transform** — each point votes for all lines through it; peaks in the accumulator
  are lines with many collinear points. Directly the "most points on a line" idea in a
  voting form.
- **RANSAC** — repeatedly hypothesizes a line from a minimal sample and counts inliers
  (points on/near it), keeping the line with the most support.

- **Hough transform:** https://en.wikipedia.org/wiki/Hough_transform
- **RANSAC:** https://en.wikipedia.org/wiki/Random_sample_consensus

---

## 2. Computational Geometry — Degeneracy Testing

Collinearity is a **degeneracy** that many geometric algorithms must detect or rule out:
convex-hull, triangulation, and arrangement algorithms behave specially when three points
are collinear. "Are any three points collinear?" is a standard degeneracy test — and, as
the Research tab notes, a 3SUM-hard one.

---

## 3. GIS, Mapping & Sensing

Detecting aligned geographic features (straight road segments, property boundaries, flight
paths), calibration-marker alignment in metrology, and finding aligned detections in radar/
lidar point clouds all rest on collinearity / line-fitting over point sets.

---

## The Unifying Idea

```
group points by the (exact, GCD-reduced) slope they make with an anchor
        →  the biggest group is the line with the most points
in a voting form (Hough) or sampling form (RANSAC)
        →  detect the dominant line in noisy data
```

| Domain | Points | What "most on a line" means |
|--------|--------|-----------------------------|
| Computer vision | Edge/feature pixels | The detected straight edge |
| Comp. geometry | Input vertices | A degeneracy to detect/handle |
| GIS / sensing | Geo features / detections | Aligned roads, boundaries, tracks |

---

## Further Reading

- Hough transform: https://en.wikipedia.org/wiki/Hough_transform
- RANSAC: https://en.wikipedia.org/wiki/Random_sample_consensus
- Collinearity & degeneracy: https://en.wikipedia.org/wiki/Collinearity
