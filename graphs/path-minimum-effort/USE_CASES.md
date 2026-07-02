# Path with Minimum Effort — Real-World Use Cases

Path with Minimum Effort is the **minimax path problem** (also called the
bottleneck shortest path problem) — a classical graph theory problem with
direct applications in transportation planning, robotics, and supply chain
optimisation, and a deep theoretical connection to minimum spanning trees.

---

## 1. Minimax Path Problem — The Formal Theory

The formal name for this problem is the **minimax path problem** or
**bottleneck shortest path problem**: find the path between two nodes
that minimises the maximum weight of any edge along the path.

A closely related problem, the minimax path problem or bottleneck shortest
path problem, asks for the path that minimises the maximum weight of any
of its edges. It has applications that include transportation planning.

The minimax path between any two nodes in a graph can be found on the
minimum spanning tree of the graph. This is the key theoretical result:
the MST contains ALL minimax paths simultaneously — which is why the
Kruskal's DSU approach correctly finds the answer by simply stopping when
source and destination first become connected.

### References

- **Wikipedia — Widest path problem / Minimax path problem:**
  https://en.wikipedia.org/wiki/Widest_path_problem
  "The minimax path problem asks for the path that minimises the maximum
  weight of any of its edges. It has applications that include
  transportation planning. In an undirected graph, a widest (or minimax)
  path may be found as the path between the two vertices in the maximum
  (or minimum) spanning tree."

- **CMU 15-451 Lecture 14 — Graph Algorithms II (Bottleneck Paths and MST):**
  https://www.cs.cmu.edu/~avrim/451f11/lectures/lect1013.pdf
  "Finding maximum bottleneck paths and the minimum spanning tree (MST).
  Kruskal's algorithm requires an efficient data structure for union-find.
  A minimum bottleneck spanning tree is a spanning tree of minimum
  bottleneck capacity — an MST is necessarily a MBST." This lecture
  directly proves the MST-minimax path theorem used in the DSU approach.

- **Princeton Algorithms — Minimum Bottleneck Spanning Tree:**
  https://techlarry.github.io/Algorithm/Princeton/Topic%209%20-%20Minimum%20Spanning%20Tree%20and%20Shortest%20Path/
  "A minimum bottleneck spanning tree is a spanning tree of minimum
  bottleneck capacity (weights of its largest edge). A MST is necessarily
  a MBST, but a MBST is not necessarily a MST. Is an edge in a MST?
  Given an edge-weighted graph G and an edge e, design a linear-time
  algorithm to determine whether e appears in some MST."

---

## 2. Terrain Analysis — Hiking Route Planning

The "minimum effort" path through a height grid is literally the
computational model of hiking route planning: find the route that
minimises the steepest climb (maximum height change per step), not
necessarily the shortest distance.

```
Path with Minimum Effort:           Hiking route planner:
  heights[r][c] = cell elevation  ↔  terrain elevation at grid point
  |Δh| between adjacent cells     ↔  steepness of each segment
  minimax effort                  ↔  maximum gradient along the route
  optimal path                    ↔  least strenuous route to summit
```

Applications:
- Topographic route planning tools (AllTrails, Komoot) compute routes that
  minimise maximum gradient — exactly this problem on a DEM (Digital Elevation
  Model) raster grid
- Wheelchair/accessibility route planning: find the path with the smallest
  maximum incline between two points

---

## 3. Robotics — Energy-Efficient Terrain Traversal

For wheeled or legged robots traversing uneven terrain, the critical
metric is often the maximum slope or height change per step (not the total
distance), because steep steps require disproportionately more torque
and energy. The robot minimises the maximum elevation change per step —
the minimum effort path.

This is directly implemented in robot navigation cost maps:
- Elevation-based cost maps assign higher traversal cost to steep transitions
- The global planner finds the path minimising the worst-case cost per step
- This is the modified Dijkstra approach with `max()` relaxation

---

## 4. Supply Chain — Bottleneck Minimisation

In a supply chain or logistics network, edges represent transportation
links with capacity constraints. The "effort" is the capacity required —
find the route that minimises the maximum single-link capacity requirement
(the bottleneck).

```
Supply chain:
  nodes = warehouses/distribution centres
  edges = transportation routes with capacity constraints
  edge weight = required capacity on that link
  minimax path = route with the lowest worst-case capacity requirement
```

This is called the **max-flow min-cost bottleneck routing** problem in
operations research, a direct generalisation of this LeetCode problem.

---

## 5. Image Processing — Seam Carving

Seam carving is a content-aware image resizing technique that removes
the "least noticeable" seam (column or row of pixels) — the seam with
the minimum total or maximum perceptual change. The minimum-effort seam
is the path through the image from top to bottom that minimises the
maximum gradient (colour/intensity change) between adjacent pixels.

```
Seam carving:
  pixel value → height
  |pixel difference| → |Δh|
  minimum effort seam → path of minimum maximum perceptual change
```

---

## The MST Connection — Why All Three Approaches Work

All three approaches to this problem exploit the same underlying theorem:

```
The minimax path between any two nodes in a graph can be read off
the minimum spanning tree — it is the path in the MST between those
two nodes, and its "effort" is the maximum edge on that path.
```

| Approach | How it exploits the theorem |
|----------|-----------------------------|
| Modified Dijkstra | Directly propagates max() — implicitly builds MST-like tree |
| Binary Search + BFS | Checks if spanning tree forms at effort threshold k |
| Kruskal's DSU | Explicitly builds the MST edge by edge — stops when done |

---

## Further Reading

- Minimax path problem (Wikipedia): https://en.wikipedia.org/wiki/Widest_path_problem
- CMU 15-451 bottleneck paths + MST: https://www.cs.cmu.edu/~avrim/451f11/lectures/lect1013.pdf
- Princeton MST bottleneck: https://techlarry.github.io/Algorithm/Princeton/Topic%209%20-%20Minimum%20Spanning%20Tree%20and%20Shortest%20Path/
- Network Delay Time (Dijkstra's): see `graphs/network-delay-time/`
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
