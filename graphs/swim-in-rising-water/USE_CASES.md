# Swim in Rising Water — Real-World Use Cases

Swim in Rising Water is a direct computational model of terrain flooding
and the "safe passage" problem in GIS — finding the minimum water level
at which a traversable path exists across a terrain.

---

## 1. Terrain Flooding — The Direct Real-World Problem

This problem has a formal name in computational geometry and GIS:
the **flooding problem** on a DEM (Digital Elevation Model) raster.

The flooding problem takes as input a file of elevation values for each
cell on a grid. A path in the grid is a sequence of cells where each pair
of consecutive cells are neighbours. If we define the height of a path as
the elevation of the highest cell on the path, the flooding problem is
equivalent to determining, for each cell, the height of the lowest path
from that cell to the boundary — the minimum elevation threshold at which
water (or a swimmer) can reach the edge.

This is EXACTLY LeetCode #778: find the minimum t such that a path exists
from (0,0) to (n-1,n-1) where every cell has elevation ≤ t — i.e., the
elevation of the lowest minimax path from the source to the destination.

### The real-world scenario

When flooding occurs, emergency planners ask: "What is the minimum water
level at which a continuous flood path exists from the river to a critical
location (hospital, power station)?" This is the minimax path problem on
the terrain DEM — exactly Swim in Rising Water.

### References

- **arXiv:1211.1857 — Simple I/O-Efficient Flow Accumulation on Grid Terrains:**
  https://arxiv.org/pdf/1211.1857
  "The flooding problem: if we define the height of a path as the elevation
  of the highest cell on the path, the problem is equivalent to determining,
  for each cell c, the height of the lowest path from c to the boundary.
  Time-forward processing and I/O-naïve flooding algorithms address this
  minimax path problem on terrain grids."

- **Towards Data Science — Simulating Flood Inundation with Python and
  Elevation Data:**
  https://towardsdatascience.com/simulating-flood-inundation-with-python-and-elevation-data-a-beginners-guide/
  "With flood_threshold = 40 meters, flood_mask = (dem <= flood_threshold),
  we simulate the progressive flood scenario by increasing the water level
  incrementally — the same threshold-based approach as Swim in Rising Water,
  where the answer is the minimum threshold at which the path exists."

- **ScienceDirect — MatFlood: An efficient algorithm for mapping flood extent
  and depth:**
  https://www.sciencedirect.com/science/article/pii/S1364815223002153
  "A flood water level of interest — the flood water level refers to the
  vertical water level producing the inundation. MatFlood takes a DEM in
  raster format and a single location point, then determines whether cells
  are connected to the water source at each elevation threshold."

- **Nature Scientific Reports — Graph Neural Networks for Flood Forecasting
  with Digital Twin Visualization:**
  https://www.nature.com/articles/s41598-024-68857-y
  "The flood simulation data is a time series of scalar water elevation
  values discretized over a grid. We incrementally increased the water level
  to span scenarios from regular river flow to extreme flooding — exactly
  the time-rising-water model of LeetCode #778."

- **ScienceDirect — Innovative Flood Prediction Modelling:**
  https://www.sciencedirect.com/science/article/abs/pii/S0022169425004937
  "The optimization of threshold division makes a best trade-off between
  computational efficiency and simulation accuracy through sensitivity
  analysis of the threshold — the same binary search on threshold that
  Approach 2 (Binary Search + BFS) implements."

---

## 2. Emergency Route Planning Under Flooding

The inverse of flood extent mapping: given a rising water level, find
whether emergency vehicles can still travel from a command centre to a
critical facility along roads that haven't flooded (elevation > current
water level). This is exactly:

```
Swim in Rising Water (inverse):
  nodes = road intersections
  grid[r][c] = road elevation
  answer = minimum water level at which the road network is cut
         = minimum t such that NO path exists with all elevations > t

Or equivalently: maximum t at which a path still exists
= dual of the Swim in Rising Water minimum.
```

Emergency management systems run this analysis continuously as flood
forecasts update — routing ambulances, fire trucks, and supply convoys
along roads guaranteed to stay above the predicted flood level.

---

## 3. Game AI — Safe Passage at Rising Difficulty

In strategy and survival games, the "swim in rising water" mechanic models
a world where danger level rises over time. The algorithm finds the critical
time at which the player can no longer safely cross the map:

```
grid[r][c] = danger level at cell (r,c)
t = game difficulty / time elapsed
Path exists at time t = player can safely cross at current difficulty
Minimum t for path = minimum difficulty to complete the crossing
```

Pathfinding under rising threat levels appears in games like Floodgate,
survival games with tide mechanics, and RTS games with escalating hazard maps.

---

## Summary

| Domain | grid[r][c] = | Water level t = | Answer = |
|--------|-------------|-----------------|---------|
| Swim in Rising Water (#778) | Cell elevation | Time elapsed | Min time for safe path |
| Terrain flooding (GIS) | DEM cell elevation | Flood water level | Min water level for flood path |
| Emergency routing | Road elevation | Flood level | Max flood level before route cut |
| Game AI pathfinding | Hazard level | Difficulty/time | Min difficulty to cross |

---

## Further Reading

- Terrain flooding problem (arXiv): https://arxiv.org/pdf/1211.1857
- Flood threshold simulation (TDS): https://towardsdatascience.com/simulating-flood-inundation-with-python-and-elevation-data-a-beginners-guide/
- MatFlood flood extent (ScienceDirect): https://www.sciencedirect.com/science/article/pii/S1364815223002153
- GNN flood forecasting (Nature): https://www.nature.com/articles/s41598-024-68857-y
- Path with Minimum Effort (related): see `graphs/path-minimum-effort/`
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
