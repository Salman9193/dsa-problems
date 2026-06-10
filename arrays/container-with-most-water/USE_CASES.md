# Container With Most Water — Real-World Use Cases

The container problem — maximise area bounded by two walls where height
is the bottleneck — appears in civil engineering and network flow theory.

---

## 1. Dam / Reservoir Design — Storage-Area-Elevation Optimisation

Designing a dam involves selecting two terrain elevation points (the
valley walls) that maximise the water storage cross-section. The stored
water volume is bounded by the shorter of the two walls (spillway height)
and the width of the valley between them — exactly the container formula:

```
storage ∝ valley_width × min(left_wall_height, right_wall_height)
```

Engineers evaluate candidate dam sites along a river cross-section to
find the pair of terrain points that maximises storage capacity —
the same optimisation as Container With Most Water.

- **Paper:** Ferdowsi et al. — *Optimisation of Gravity Dam Cross-Section*,
  Journal of Soft Computing in Civil Engineering, 4(3):65–78, 2020.
  https://www.jsoftcivil.com/article_107850_33353ae971388e14e7c3d66868c4ac1f.pdf
  "The cross-sectional area of Tilari Dam is 709.493 m². As dams are
  normally constructed in wide valleys, small changes in cross-sectional
  area lead to high-cost savings — optimisation reduced the area by 20%."

- **Reservoir siting reference:**
  Springer — *Planning for Dams and Reservoirs: Hydrologic Design Elements*
  https://link.springer.com/content/pdf/10.1007%2F978-94-015-9894-1_3.pdf
  "Each alternative site receives a detailed assessment to determine the
  size of dam that can be constructed and the corresponding
  storage-area-elevation relationships."

- **US Army Corps of Engineers EM 1110-2-2300** (Earth Dams & Reservoirs):
  https://www.publications.usace.army.mil/portals/76/publications/engineermanuals/em_1110-2-2300.pdf

---

## 2. Network Flow — Max-Flow Min-Cut (Bottleneck = Shorter Wall)

The max-flow min-cut theorem states that the maximum flow through a
network equals the capacity of the minimum cut — the bottleneck that
limits all flow paths. This is the exact structural analogue of the
container problem: water (flow) is bounded by the shorter wall (min-cut
bottleneck), and the two-pointer algorithm greedily eliminates the
current bottleneck at each step.

```
Container problem:  area   = width  × min(h[left], h[right])
Network max-flow:   flow   = paths  × bottleneck_capacity
```

The two-pointer greedy strategy — always move the shorter wall —
mirrors how augmenting path algorithms advance: each iteration pushes
flow along the current bottleneck path, then moves past it.

- **Foundational paper:** Ford, L.R. & Fulkerson, D.R. — *Maximal Flow
  Through a Network*, Canadian Journal of Mathematics, 8:399–404, 1956.
  The original max-flow paper. Proves that max flow = min cut and
  introduces the augmenting path algorithm that processes one bottleneck
  per iteration.

- **Wikipedia — Max-flow min-cut theorem:**
  https://en.wikipedia.org/wiki/Max-flow_min-cut_theorem
  "The max-flow min-cut theorem states that the maximum amount of flow
  from source to sink equals the total weight of edges in a minimum cut.
  If there's a bottleneck in the network — a small min-cut — that
  bottleneck determines the overall maximum flow."

- **Princeton lecture notes (Kleinberg & Tardos):**
  https://www.cs.princeton.edu/~wayne/kleinberg-tardos/pdf/07NetworkFlowI.pdf
  "The bottleneck capacity of an augmenting path is the minimum residual
  capacity edge — it limits all potential flow along the path."

---

## The Shared Structure

| Concept | Container problem | Network max-flow |
|---------|-----------------|-----------------|
| Two boundaries | Left wall, right wall | Source, sink |
| Width | Index distance | Number of flow paths |
| Bottleneck | min(h[left], h[right]) | Min-cut capacity |
| Greedy step | Move shorter wall inward | Augment along bottleneck path |
| Goal | Maximise area | Maximise flow |

---

## Further Reading

- Ferdowsi et al. dam optimisation: https://www.jsoftcivil.com/article_107850_33353ae971388e14e7c3d66868c4ac1f.pdf
- Reservoir siting (Springer): https://link.springer.com/content/pdf/10.1007%2F978-94-015-9894-1_3.pdf
- Max-flow min-cut (Wikipedia): https://en.wikipedia.org/wiki/Max-flow_min-cut_theorem
- Princeton network flow notes: https://www.cs.princeton.edu/~wayne/kleinberg-tardos/pdf/07NetworkFlowI.pdf
