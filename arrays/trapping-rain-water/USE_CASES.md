# Trapping Rain Water — Real-World Use Cases

The water-trapping computation — finding how much water accumulates in
depressions between terrain features — appears directly in hydrology,
geospatial analysis, and financial risk modelling.

---

## 1. GIS & Hydrology — DEM Depression Filling & Watershed Analysis

Digital Elevation Models (DEMs) represent terrain as a grid of elevation
values. The trapping rain water algorithm computes the 1D cross-section
of the 2D depression-filling problem — identifying pits and sinks in
terrain where water accumulates before flowing out.

The 2D generalisation, the **Priority-Flood algorithm**, is the
production-grade algorithm used in GIS tools (ArcGIS, QGIS, GRASS,
WhiteboxTools) for watershed delineation and flood hazard mapping.

### How it connects

```
1D trapping rain water:
  height = [4, 1, 0, 2, 3]
  water  = [0, 2, 3, 1, 0]  → depressions filled to the bounding wall level

2D Priority-Flood:
  elevation_grid[i][j] = terrain height
  fills all depressions to their spill-point elevation
  → produces a hydrologically conditioned DEM for drainage direction analysis
```

Both use the same principle: water fills each depression to the minimum
height of its bounding walls (min of maxLeft, maxRight → min of surrounding
boundary in 2D).

### References

- **Paper:** Barnes, Lehman & Mulla — *Priority-Flood: An Optimal
  Depression-Filling and Watershed-Labeling Algorithm for Digital
  Elevation Models*, Computers & Geosciences, 2014.
  https://arxiv.org/pdf/1511.04463
  "The Priority-Flood algorithm is simple to understand and implement —
  only 20 lines of pseudocode. It works on irregular meshes as well as
  4-, 6-, 8-, and n-connected grids and can be adapted to label watersheds
  and determine flow directions."

- **Paper:** *The Use of Digital Elevation Models for Watershed and Flood
  Hazard Mapping*, ResearchGate, 2013.
  https://www.researchgate.net/publication/269222368
  "DEMs have proven to be a valuable tool for the topographic
  parameterisation of hydrological models — the basis for any flood
  modelling process. DTA algorithms include removing closed depressions,
  drainage direction assignment, and watershed delineation."

- **GIS tools implementing this:**
  ArcGIS ArcHydro, QGIS SAGA, WhiteboxTools (`fill_depressions`)

---

## 2. Finance — Maximum Drawdown (MDD)

Maximum Drawdown is one of the most widely used risk metrics in portfolio
management and hedge fund evaluation. It measures the largest peak-to-trough
decline in portfolio value over a time period.

Computing MDD requires the same prefix maximum scan as trapping rain water:

```
Portfolio value series:  [100, 120, 90, 110, 80, 130]

Running peak (maxLeft):  [100, 120, 120, 120, 120, 130]
Drawdown at each point:  [  0,   0,  30,  10,  40,   0]
MDD = max drawdown = 40  (peak 120 → trough 80)
```

The "water trapped" at each position in the rain water problem corresponds
directly to the drawdown at each time step — both measure how far below
the running maximum the current value sits.

### The mathematical connection

```
Trapping Rain Water:
  water[i] = maxLeft[i] - height[i]   (when left is the bottleneck)

Maximum Drawdown:
  drawdown[t] = maxPrice[0..t] - price[t]
  MDD = max over all t of drawdown[t]
```

Both formulas are identical in structure — a running maximum minus the
current value.

### References

- **Paper:** Chekhlov, Uryasev & Zabarankin — *Portfolio Optimization
  with Drawdown Constraints*, University of Pennsylvania.
  https://www.cis.upenn.edu/~mkearns/finread/drawdown.pdf
  Introduces Conditional Drawdown-at-Risk (CDaR), a family of risk
  functions based on the drawdown curve — the running-maximum-minus-current
  series computed by the same prefix scan as trapping rain water.

- **Paper:** *Diversified Reward-Risk Parity in Portfolio Construction*,
  arXiv:2106.09055.
  https://arxiv.org/pdf/2106.09055
  "Maximum Drawdown (MDD) is the worst consecutive loss in a specified
  time period — given by the minimum return r(t,τ) across all t and τ.
  Assets with smaller maximum drawdowns are favoured by investors."

- **Paper:** *Effectiveness of Measures of Performance During Speculative
  Bubbles*, arXiv:0709.2423.
  https://arxiv.org/pdf/0709.2423
  "The maximum drawdown of a random process is defined as
  MDD(T) = sup over t of (sup over s≤t of X(s) − X(t)) —
  the supremum of the running maximum minus current value."

---

## The Shared Algorithm

Both domains use the same prefix maximum scan:

```
runningMax = 0
accumulated = 0

for each position i (left to right):
    runningMax = max(runningMax, value[i])
    accumulated += runningMax - value[i]   // water depth / drawdown depth
```

| Domain | value[i] | runningMax | accumulated |
|--------|----------|-----------|-------------|
| Trapping Rain Water | terrain height | left wall height | trapped water volume |
| Maximum Drawdown | portfolio value | portfolio peak | total drawdown area |

---

## Further Reading

- Priority-Flood algorithm: https://arxiv.org/pdf/1511.04463
- DEM watershed analysis: https://www.researchgate.net/publication/269222368
- Portfolio drawdown constraints: https://www.cis.upenn.edu/~mkearns/finread/drawdown.pdf
- CDaR risk measure: https://arxiv.org/pdf/2106.09055
- MDD Wikipedia: https://en.wikipedia.org/wiki/Drawdown_(economics)
