# Pacific Atlantic Water Flow — Real-World Use Cases

The Pacific Atlantic Water Flow problem IS watershed delineation on a
Digital Elevation Model (DEM) — one of the most fundamental operations
in hydrology, GIS, and environmental science.

---

## 1. Watershed Delineation — The Direct Real-World Equivalent

A watershed (or drainage basin) is the area of land that drains all water
to a common outlet (river, lake, or ocean). Delineating watersheds from
a digital elevation model is the hydrological equivalent of this problem:

```
LeetCode #417:                     Watershed Delineation (GIS):
  heights[][]   ↔  DEM raster (elevation per grid cell)
  Pacific border ↔  Ocean/basin outlet A (e.g. pour point A)
  Atlantic border ↔  Ocean/basin outlet B (e.g. pour point B)
  "can flow to both" ↔  drainage divide (cells draining to both basins)
  Reverse BFS uphill  ↔  flow direction + flow accumulation (reverse tracing)
```

Computerized methods for watershed delineation use digital elevation models
(DEMs), datasets that represent the height of the Earth's land surface.
Tools for watershed delineation are implemented in GIS software packages
including ArcGIS, QGIS, and GRASS GIS, as well as specialised hydrologic
modelling packages like TAUDEM and WMS.

The standard DEM flow algorithm assigns each cell a flow direction to its
steepest downhill neighbour. The reverse BFS in this problem is the upstream
tracing step: starting from an outlet, identify all cells that drain into it
by traversing uphill — exactly `heights[nr][nc] >= heights[curr]`.

### References

- **Wikipedia — Watershed delineation:**
  https://en.wikipedia.org/wiki/Watershed_delineation
  "Computerized methods for watershed delineation use digital elevation models,
  datasets that represent the height of the Earth's land surface. Tools are
  implemented in ArcGIS, QGIS, GRASS GIS, and programming languages like Python or R."

- **ScienceDirect — A fast algorithm to delineate watershed boundaries:**
  https://www.sciencedirect.com/science/article/abs/pii/S1364815220308999
  "Watershed delineation is one of the fundamental tasks in hydrological studies.
  A number of methods have been developed to model how water flows from higher
  elevation to lower over adjacent DEM grid cells. This paper proposes an
  algorithm for rapid watershed delineation directly from flow direction rasters."

- **arXiv:1708.00354 — A Watershed Delineation Algorithm for 2D Flow Direction Grids:**
  https://arxiv.org/pdf/1708.00354
  "Watersheds are the basic modelling element for hydrological problems.
  The nested set model applies a modified depth-first graph traversal algorithm —
  the same reverse traversal as the uphill BFS in Pacific Atlantic Water Flow."

- **USPTO Patent 11954410 — Watershed marching-delineation algorithm:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/11954410
  "Watershed boundaries may be created from digital flow direction grids.
  A modified depth first graph traversal algorithm is available for use —
  tracing upstream from outlet cells to identify all contributing land area."

- **Topostreets — Hydrology With DEMs:**
  https://topostreets.com/hydrology-with-dems-flow-direction-accumulation-and-watershed-delineation/
  "Flow direction calculates the direction of flow for every cell given the
  elevation of the neighbouring cells. Flow accumulation quantifies how many
  cells drain into any given cell. Together they answer: where a drop of rain
  will travel and which communities are at risk of flooding."

---

## 2. The D8 Algorithm — Production Flow Direction

The standard flow direction algorithm used in ArcGIS, QGIS, and TauDEM is D8:
water flows to the one neighbouring cell that has the steepest downhill slope
(the lowest elevation among 8 neighbours). This is the 8-directional version
of the flow condition in this problem.

LeetCode #417 uses D4 (4-directional, no diagonals) for simplicity. Production
watershed delineation tools use D8 (8-directional) for more realistic flow paths.

```
D4 (this problem):  flow to 4 neighbours if lower or equal
D8 (production):    flow to steepest of 8 neighbours (diagonal included)
```

The extension from D4 to D8 requires only adding 4 diagonal directions to the
BFS neighbours list — the reverse-uphill logic is identical.

---

## 3. Continental Divide — The Geographic Version

The Continental Divide of the Americas is the precise real-world instance of
this problem at a continental scale:

```
Pacific watershed:   all land draining to the Pacific Ocean
Atlantic watershed:  all land draining to the Atlantic/Gulf
Continental Divide:  the boundary line — cells draining to both
```

The LeetCode problem with Pacific + Atlantic borders is literally modelling the
Continental Divide of North America at grid resolution.

USGS, Environment Canada, and the HydroSHEDS project have computed the
continental divide using exactly this type of two-source BFS/DFS on DEMs:
- HydroSHEDS: https://www.hydrosheds.org — global hydrographic dataset using
  SRTM elevation data and watershed delineation algorithms
- USGS National Hydrography Dataset (NHDPlus): uses ArcGIS watershed tools
  implementing the reverse-flow tracing algorithm

---

## 4. Contamination Source Tracing — Reverse BFS in Practice

In environmental engineering, when contamination is found at a river outlet,
identifying all upstream land areas that could have contributed (reverse tracing)
is exactly the reverse BFS of this problem:

```
Pacific Atlantic:          Contamination tracing:
  Ocean border = source      River outlet = contamination point
  BFS uphill from border     BFS upstream (reverse flow) from outlet
  Result: contributing cells Result: all potential contamination sources
```

This is called upstream catchment delineation and is used in:
- Water quality management
- Flood risk assessment
- Agricultural runoff tracking

---

## Summary

| Domain | heights[] | Border 1 | Border 2 | Result cells |
|--------|-----------|---------|---------|-------------|
| LeetCode #417 | Grid heights | Pacific (top+left) | Atlantic (right+bottom) | Flow to both |
| Watershed GIS | DEM elevation | Basin outlet A | Basin outlet B | Drainage divide |
| Continental Divide | Terrain elevation | Pacific Ocean | Atlantic/Gulf | Continental divide line |
| Contamination trace | Terrain elevation | Contamination site A | Site B | Upstream sources |

---

## Further Reading

- Watershed delineation (Wikipedia): https://en.wikipedia.org/wiki/Watershed_delineation
- ScienceDirect fast watershed algorithm: https://www.sciencedirect.com/science/article/abs/pii/S1364815220308999
- DEM watershed algorithm (arXiv): https://arxiv.org/pdf/1708.00354
- Watershed delineation patent: https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/11954410
- DEM hydrology guide: https://topostreets.com/hydrology-with-dems-flow-direction-accumulation-and-watershed-delineation/
- HydroSHEDS global dataset: https://www.hydrosheds.org
