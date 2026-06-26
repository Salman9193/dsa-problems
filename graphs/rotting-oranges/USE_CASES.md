# Rotting Oranges — Real-World Use Cases

Multi-source BFS — starting propagation from multiple points simultaneously
and finding the minimum time for the spread to complete — is the core
algorithm behind wildfire simulation and epidemic spreading models.

---

## 1. Wildfire Simulation — Minimum Travel Time Algorithm

The Minimum Travel Time (MTT) algorithm (Finney 2002) is the standard
algorithm used in wildfire simulation tools (FARSITE, FlamMap) to predict
fire spread across a landscape. It is the continuous-domain equivalent
of the multi-source BFS in this problem.

### The structural parallel

```
Rotting Oranges (discrete):
  Grid cells = fuel cells (oranges)
  Rotten oranges = ignition points (fire sources)
  Minute = one time step
  Multi-source BFS: spread from all ignition points simultaneously

MTT Algorithm (continuous):
  Raster grid = landscape fuel cells
  Multiple ignition points = initial fire perimeters
  Minimum travel time: BFS-like wavefront expansion from all fire fronts
  Result: arrival time map = the time each cell burns
```

Fire spread simulators predict the growth of individual wildfire perimeters
based on ignition locations, fuels, topography and weather conditions.
The MTT algorithm computes, for each cell, the minimum time for the fire to
reach it from any ignition point — exactly what multi-source BFS computes for
rotting oranges.

### Multi-source observation in wildfire prediction

Recent advances use multi-source data assimilation: integrating observations
from UAVs, watchtowers, and satellites to update fire perimeters in real time.
This multi-source update corresponds to the multi-source BFS seed step —
all current fire perimeter cells are added to the queue simultaneously before
continuing the spread simulation.

### References

- **ScienceDirect — Driving wildfire spread prediction by multi-source
  real-time observations:**
  https://www.sciencedirect.com/science/article/abs/pii/S0304380025003011
  "This paper proposes a multi-source observation data-driven DA model for
  high-precision wildfire spread prediction. Compared with traditional methods
  that rely on a single data source, this framework achieves a substantial
  breakthrough in prediction performance by integrating data from multiple
  platforms simultaneously."

- **Minimum Travel Time algorithm (Finney 2002):**
  Fire growth using minimum travel time methods, Canadian Journal of Forest
  Research, 32:420-1424.
  "The MTT algorithm predicts fire growth based on ignition locations, fuels,
  topography and weather conditions — the continuous-domain BFS equivalent
  of multi-source BFS on a grid."

- **USPTO Patent 11966670 — Method and system for predicting wildfire hazard
  and spread:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/11966670
  "Process-driven simulation generates hypotheses of the spatial-temporal
  evolution of wildfire spread. Simulation models generate burn probability
  maps using Monte Carlo approaches with multiple ignition locations —
  each simulation run is a multi-source BFS from the ignition points."

---

## 2. Epidemic Spreading — FastSIR Algorithm (BFS on Networks)

The SIR (Susceptible-Infected-Recovered) model is the standard mathematical
model for epidemic spreading. The FastSIR algorithm is a BFS-based
implementation that mirrors the Rotting Oranges structure exactly:

```
Rotting Oranges:          SIR Epidemic Model:
  grid cells         ↔    individuals (nodes in network)
  rotten oranges     ↔    initially infected individuals
  fresh oranges      ↔    susceptible individuals
  '0' empty cells    ↔    recovered/immune individuals
  multi-source BFS   ↔    infection spreading from all infected simultaneously
  minute = 1 level   ↔    generation = 1 BFS level
  return minutes     ↔    return epidemic duration
  return -1          ↔    some individuals never infected (isolated component)
```

The FastSIR algorithm uses a queue and array simultaneously for storing
node states — a BFS-based approach for epidemic spreading simulations on
arbitrary network structures. The naive SIR algorithm running time is reduced
by approximately constant factor 1/q. The algorithm models full epidemic
dynamics and can be upgraded to a parallel version with interventions
(antiviral drugs and containments).

### Reference

- **Paper:** *FastSIR Algorithm: A Fast Algorithm for simulation of epidemic
  spread in large networks by using SIR compartment model*, arXiv:1202.1639.
  https://arxiv.org/pdf/1202.1639
  "We propose the FastSIR algorithm to reduce the average case running time
  of the naive SIR algorithm. Running time is reduced by using two data
  structures (queue and array) simultaneously for storing node states.
  This models full epidemic dynamics."

- **Forest fire as epidemic spread paper**, arXiv:2309.00660.
  https://arxiv.org/pdf/2309.00660
  Treats fire spread as a continuous-space SIR model — directly connecting
  wildfire simulation (use case 1) and epidemic spreading (use case 2)
  through the shared BFS propagation structure.

---

## 3. Network Broadcast — Multi-Source Message Propagation

In distributed systems, a broadcast from multiple source servers simultaneously
reaches all nodes in minimum time via multi-source BFS on the network graph.
This is the core of:

- **Gossip protocols:** each infected node broadcasts to neighbours; the
  "infection time" of each node = minimum hops from any source
- **DNS propagation:** when a DNS record changes, updated values propagate
  from multiple authoritative servers simultaneously
- **CDN cache invalidation:** when content changes, all CDN edge nodes
  invalidate simultaneously and propagate to downstream nodes

The minimum time for all nodes to receive the broadcast is exactly what
multi-source BFS computes — the answer to Rotting Oranges.

---

## The Multi-Source BFS Pattern in Production

All three use cases share the same algorithmic skeleton:

```
// Seed ALL sources simultaneously
for each source: queue.offer(source); mark(source);

// BFS level by level (each level = one time step)
int time = 0;
while (!queue.isEmpty() && unseen_targets_exist()) {
    time++;
    int size = queue.size();
    for (int i = 0; i < size; i++) {
        node = queue.poll();
        for each unvisited neighbour:
            mark(neighbour); queue.offer(neighbour); update_count();
    }
}
return all_targets_reached() ? time : -1;
```

| Domain | Sources | Spread to | Time step | -1 means |
|--------|---------|-----------|-----------|---------|
| Rotting Oranges | Rotten cells | Adjacent fresh | 1 minute | Isolated fresh cells |
| Wildfire MTT | Ignition points | Adjacent fuel cells | 1 time unit | Unreachable fuel |
| FastSIR epidemic | Initial infected | Network neighbours | 1 generation | Isolated subgraph |
| Network broadcast | Source servers | Network nodes | 1 hop | Disconnected nodes |

---

## Further Reading

- FastSIR algorithm: https://arxiv.org/pdf/1202.1639
- Forest fire as SIR model: https://arxiv.org/pdf/2309.00660
- Multi-source wildfire prediction: https://www.sciencedirect.com/science/article/abs/pii/S0304380025003011
- Wildfire hazard patent: https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/11966670
- MTT algorithm: Finney 2002, Canadian Journal of Forest Research 32:420-1424
