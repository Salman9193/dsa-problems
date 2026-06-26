# 01 Matrix — Real-World Use Cases

The 01 Matrix problem IS the binary image distance transform — one of the
most fundamental operations in computer vision, medical imaging, and
robot motion planning.

---

## 1. Distance Transform — Computer Vision

The distance transform assigns each foreground pixel in a binary image
its distance to the nearest background pixel. This is exactly the 01 Matrix
problem: foreground pixels = `1`s, background pixels = `0`s.

### The fire metaphor

One way to think about the distance transform is to imagine that foreground
regions are made of some uniform slow-burning flammable material. Then
consider simultaneously starting a fire at all points on the boundary of
a foreground region and letting the fire burn its way into the interior.
The distance transform records how long each interior point takes to burn.

This fire-spreading metaphor IS multi-source BFS: background pixels = fire
sources (distance 0), foreground pixels = fuel cells that burn at rate 1
per step. The time each cell takes to burn = its BFS distance from any source.

### Applications of the distance transform

- **OCR:** measuring stroke widths of characters (skeleton extraction)
- **Medical imaging:** measuring thickness of anatomical structures (bone cortex, vessel walls)
- **Object detection:** generating distance maps for template matching (Chamfer matching)
- **Image segmentation:** watershed transform seeded by distance transform maxima
- **Lane detection:** orienting toward lane boundaries using distance-to-edge maps

### The two-pass DP IS the Borgefors algorithm

The two-pass DP in this problem (forward scan top-left, backward scan
bottom-right) is the exact implementation of the Borgefors (1986) city-block
distance transform — the foundational paper in distance transform computation.

The distance transform is computed sequentially by a two-pass method.
The first (forward) pass scans in a left-right top-bottom raster scan order.
The second (backward) pass scans in a reverse right-left bottom-top order
— exactly the two-pass DP above.

### References

- **Borgefors 1986 — Distance transformations in digital images:**
  Computer Vision, Graphics, and Image Processing, 34:344–371.
  https://people.cmm.minesparis.psl.eu/users/marcoteg/cv/publi_pdf/MM_refs/1986_Borgefors_distance.pdf
  "In the first part, optimal DTs are computed, optimal in the sense that
  the maximum difference from the Euclidean distance is minimized. An
  excellent new DT is presented, easily computed, with a maximal difference
  from the Euclidean distance of about 2%."

- **arXiv:2106.03503 — The Distance Transform and its Computation:**
  https://arxiv.org/pdf/2106.03503
  "Starting from the positions of the object pixels, whose distances are
  zero by definition, the distances of background pixels are determined
  step by step while incrementing the distance value — exactly the
  multi-source BFS approach of this problem."

- **Edinburgh HIPR2 — Distance Transform:**
  https://homepages.inf.ed.ac.uk/rbf/HIPR2/distance.htm
  "The distance transform is an operator normally only applied to binary
  images. The result is a graylevel image where the intensity of points
  inside foreground regions shows the distance to the closest boundary.
  Discuss the differences between city block, chessboard, and Euclidean
  distance metrics — the 4-directional BFS gives city-block (Manhattan) distance."

- **Cornell CS664 — Distance Transforms:**
  https://www.cs.cornell.edu/courses/cs664/2008sp/handouts/cs664-7-dtrans.pdf
  "Distance transforms are a natural way to 'blur' feature locations
  geometrically — used for comparing binary feature maps where small
  variations cause misalignment."

- **USPTO Patent 7813580 — Adaptive image region partition and morphologic processing:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7813580
  "The foreground distance transform assigns each foreground pixel the
  shortest distance to any background pixel. The distance transform is
  computed sequentially by a two-pass method: forward pass (left-right,
  top-bottom raster scan) and backward pass (right-left, bottom-top)."

- **ScienceDirect — Efficient Euclidean Distance Transform (PBEDT):**
  https://www.sciencedirect.com/science/article/abs/pii/S0031320312003482
  Generalises the two-pass approach to arbitrary dimensions and exact
  Euclidean distance (not just Manhattan) — using perpendicular bisector
  geometry. The Manhattan distance version is exactly the two-pass DP.

---

## 2. Robot Motion Planning — Clearance Maps

Distance-to-obstacle maps give each free cell its **clearance** — how far
it is from the nearest obstacle. Robots use clearance maps to plan paths
that stay away from obstacles by at least a safety margin.

```
01 Matrix problem:
  1s = obstacles (walls, barriers)
  0s = free space markers (reference points)
  dist[r][c] = clearance at cell (r,c)

Robot planner:
  "Plan a path where dist[r][c] ≥ safety_margin at every step"
  = filter out cells where dist[r][c] < safety_margin
  = plan in the remaining free space
```

This is called the **configuration space obstacle (C-obstacle)** expansion:
growing obstacles by the robot's radius gives the effective obstacle map.
Multi-source BFS from all obstacle cells computes this expansion exactly.

### Connection to potential field methods

In potential field path planning, obstacles generate repulsive forces
inversely proportional to distance. The distance map from multi-source BFS
provides the distance values used to compute the repulsive potential:

```
repulsive_potential(r,c) = k / dist[r][c]²
```

The robot follows the gradient of total potential (attractive to goal +
repulsive from obstacles) — using the exact distance values from this problem.

---

## 3. Voronoi Diagrams on Grids

The 01 Matrix problem computes a **discrete Voronoi diagram** when zeros
are labeled with their source ID:

- Each zero is a Voronoi "site"
- Each one is assigned to its nearest zero (Voronoi region)
- The distance stored = the Voronoi distance

Applications:
- Nearest facility maps (which hospital, fire station, or bus stop is closest?)
- Terrain analysis (which watershed does each point drain to?)
- Network coverage maps (which cell tower covers each area?)

---

## The Manhattan vs Euclidean Distance Trade-off

| Metric | Computation | Grid moves | Used in |
|--------|------------|-----------|---------|
| Manhattan (city-block) | O(mn) two-pass DP or BFS | 4-directional | This problem, Borgefors DT |
| Chebyshev (chessboard) | O(mn) DP | 8-directional | Chebyshev DT |
| Euclidean | O(mn) exact algorithms | Continuous | Medical imaging, PBEDT |

The 4-directional BFS/DP gives Manhattan distance. For true Euclidean
distance, use the PBEDT algorithm (ScienceDirect 2012) or Meijster et al. (2000).

---

## Further Reading

- Borgefors 1986 (foundational DT paper): https://people.cmm.minesparis.psl.eu/users/marcoteg/cv/publi_pdf/MM_refs/1986_Borgefors_distance.pdf
- Distance transform computation (arXiv): https://arxiv.org/pdf/2106.03503
- Edinburgh HIPR2 tutorial: https://homepages.inf.ed.ac.uk/rbf/HIPR2/distance.htm
- Cornell lecture notes: https://www.cs.cornell.edu/courses/cs664/2008sp/handouts/cs664-7-dtrans.pdf
- USPTO 7813580 (two-pass patent): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7813580
- Euclidean DT (ScienceDirect): https://www.sciencedirect.com/science/article/abs/pii/S0031320312003482
