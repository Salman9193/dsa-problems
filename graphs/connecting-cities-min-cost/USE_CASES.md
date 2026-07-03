# Connecting Cities With Minimum Cost — Real-World Use Cases

Connecting Cities With Minimum Cost is the sparse-graph version of the
MST problem — the direct model for telecommunications backbone design,
ISP network planning, and infrastructure connectivity projects where
not every pair of locations can be directly connected.

---

## 1. Fibre-Optic and Telecommunications Backbone Design

The most direct real-world instance: a telecom company needs to lay
fibre-optic cables between cities to provide connectivity. Not every
pair of cities can be connected (terrain, distance, rights of way), so
only some connections are available — exactly the sparse edge list in
this problem.

A telecom company needs to lay fiber-optic cables between cities to
provide high-speed internet. The goal is to connect all cities with the
lowest possible infrastructure cost. The graph representation uses cities
as vertices and possible cable routes as weighted edges (cost per km).
Kruskal's algorithm is best suited for sparse networks, where not every
city is directly connected.

Cities are sorted by cost using efficient sorting algorithms. Union-Find
for cycle detection ensures no redundant loops are created in the network —
exactly the cycle-check in this problem's DSU step.

### The impossible case in production

When the -1 case occurs, the telecom provider knows that with the current
available routes, it is IMPOSSIBLE to connect all cities with a single
network. This triggers a business decision: either negotiate new routes
(add more edges to the graph) or serve only a subset of cities.

### References

- **upGrad — Kruskal's Algorithm for Minimum Spanning Tree:**
  https://www.upgrad.com/blog/kruskal-algorithm-for-minimum-spanning-tree/
  "A telecom company needs to lay fiber-optic cables between cities to
  provide high-speed internet. Cities are represented as vertices, possible
  cable routes as weighted edges. Kruskal's algorithm is best suited for
  sparse networks, where not every city is directly connected."

- **Medium — Kruskal's Algorithm in Network Design for ISPs:**
  https://medium.com/@23bt04019/kruskals-algorithm-in-network-design-for-isps-mst-for-connections-f831b16c37e5
  "A minimum spanning tree connects all vertices with the minimum total
  edge weight. Kruskal's algorithm: sort all edges in non-decreasing order,
  pick the smallest edge that doesn't form a cycle. Complexity: O(E log E)
  due to sorting edges."

- **Medium — Kruskal's Algorithm in Wireless Network Setup:**
  https://medium.com/@23bt04075/kruskals-algorithm-in-wireless-network-setup-minimum-connections-0c82df87c992
  "Campus or Building-Wide Wireless Setup: Each block is treated as a node,
  and Kruskal's algorithm finds the best way to link them with minimal
  cables or access points. Works well for sparse graphs with fewer edges.
  Can handle disconnected graphs and finds a Minimum Spanning Forest."

---

## 2. Kruskal's on Sparse Graphs — The Algorithmic Fit

This problem's choice of Kruskal's over Prim's is justified by a 2023
academic paper specifically studying MST algorithms on sparse graphs.

The most commonly used method to find the MST of a sparse graph is the
Kruskal algorithm. The algorithm first sorts the edges from small to large
and then adds them on the premise of ensuring no cycles, until the number
of edges equals the number of vertices minus 1. Its computational complexity
is O(m log m), the same as the Kruskal algorithm but more efficient for very
sparse graphs with few edges.

For sparse graphs where E << V², Kruskal's O(E log E) directly reflects the
actual problem size — unlike Prim's array O(V²) which wastes time examining
non-existent pairs.

### Reference

- **Wiley Mathematical Problems in Engineering (2023) — Minimum Spanning
  Tree Method for Sparse Graphs:**
  https://onlinelibrary.wiley.com/doi/10.1155/2023/8591115
  "The most commonly used method to find the MST of a sparse graph is the
  Kruskal algorithm. For sparse graphs with very few edges, the proposed
  method is faster than the Kruskal algorithm itself. The algorithm sorts
  all edges from large to small and deletes heavy edges while maintaining
  connectivity until E = V-1. Complexity: O(m log m)."

---

## 3. Adaptive Prim–Kruskal (APK) — Production Hybrid

For real-world networks that are dense in some regions (city centres) and
sparse in others (rural areas), production systems use a hybrid approach
that switches between Prim's and Kruskal's based on local graph density.

The Adaptive Prim-Kruskal algorithm offers a new perspective on MST
computation by integrating global and local decision mechanisms. It exploits
Kruskal's efficiency in early-stage sparse connectivity and Prim's
effectiveness in late-stage dense expansion. In sparse graphs, the Kruskal
phase dominates, avoiding the overhead of priority queue operations.

```
APK algorithm:
  Sparse regions (rural, few connections): Kruskal's edge-sort phase
  Dense regions (urban, many connections): Prim's vertex-expansion phase
  Density-aware switch: τ(δ) threshold determines which phase runs

This is exactly the situation in Connecting Cities: some city clusters
may be densely interconnected (Prim's works well) while the inter-cluster
connections are sparse (Kruskal's is preferred).
```

### Reference

- **JETIR — Adaptive Prim-Kruskal (APK): A Hybrid Density-Adaptive MST Algorithm:**
  https://www.jetir.org/papers/JETIR2510208.pdf
  "The APK algorithm exploits Kruskal's efficiency in early-stage sparse
  connectivity and Prim's effectiveness in late-stage dense expansion.
  In sparse graphs (δ low), the Kruskal phase dominates, avoiding the
  overhead of priority queue operations."

---

## 4. Power Grid and Infrastructure Planning

Electric grid planners must connect substations, transformers, and load
centres at minimum cable cost. Not all pairs can be connected (terrain,
distance, regulations), so the input is an explicit edge list — exactly
#1135's structure. The -1 case means certain regions cannot be connected
to the main grid via available routes.

Popular application areas include network design such as roads, telephone,
electrical and cable-laying. Combining Kruskal's tree with Dijkstra's
path-finding capabilities allows for MSTs that are also path-sensitive,
optimizing for cost while respecting distance or time constraints.

### Reference

- **Pass4Sure — Understanding Kruskal's Algorithm (telecommunications
  backbone design, GIS):**
  https://www.pass4sure.com/blog/understanding-kruskals-algorithm-in-design-and-analysis-of-algorithms/
  "Telecommunication Backbone Design: Central nodes require Prim's vertex-
  based growth, while outer regions benefit from Kruskal's edge-centric
  selection. Sparse Graphs: When E << V², Kruskal's sorting step is
  relatively quick, making it ideal."

---

## Summary

| Domain | n cities = | connections = | -1 means = |
|--------|-----------|---------------|-----------|
| Connecting Cities (#1135) | Abstract nodes | Explicit available edges | Disconnected graph |
| Telecom fibre design | City offices | Available cable routes | Cannot reach all cities |
| Power grid | Substations | Viable cable corridors | Cannot energise all substations |
| Campus wireless | Building blocks | Feasible access-point links | Isolated buildings |

---

## Further Reading

- Kruskal's telecom design: https://www.upgrad.com/blog/kruskal-algorithm-for-minimum-spanning-tree/
- MST for sparse graphs (Wiley 2023): https://onlinelibrary.wiley.com/doi/10.1155/2023/8591115
- APK hybrid algorithm (JETIR): https://www.jetir.org/papers/JETIR2510208.pdf
- Kruskal's ISP network design: https://medium.com/@23bt04019/kruskals-algorithm-in-network-design-for-isps-mst-for-connections-f831b16c37e5
- Min Cost Connect Points (#1584): see `graphs/min-cost-connect-points/`
- Union-Find guide: see `guides/UNION_FIND.md`
