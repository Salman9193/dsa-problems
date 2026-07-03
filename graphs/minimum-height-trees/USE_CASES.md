# Minimum Height Trees — Real-World Use Cases

Finding the centre of a tree — the node(s) that minimise the maximum
distance to all other nodes — is a classical problem in location theory,
known as the **1-centre problem on tree networks**. It has direct applications
in facility location, network hub selection, and distributed systems.

---

## 1. Facility Location — 1-Centre Problem on Tree Networks

The Minimum Height Trees problem is the unweighted version of the classical
**1-centre problem on a tree network**: place one facility at a tree node to
minimise the maximum distance from any node to the facility.

The 1-centre problem on tree networks has applications in the emergency field
since it gives satisfactory results for the customers sited at the furthest
node of the tree. Minimising the weighted distance to the furthest node from
the facility is the optimality criterion for the deterministic weighted 1-centre
of a deterministic tree network.

```
Minimum Height Trees:               1-centre on tree network:
  tree nodes = graph nodes       ↔  demand points (customers)
  root selection                 ↔  facility placement
  tree height when rooted at r   ↔  max distance from r to any customer
  minimise tree height           ↔  minimise max distance (centre objective)
  return MHT roots               ↔  return optimal facility location(s)
```

The 1-centre (min-max distance) and 1-median (min-sum distance) are the two
canonical objectives. Both the center problem (longest distance) and the median
problem (sum of distances) are well-studied on tree networks.

### References

- **ScienceDirect — Optimal Algorithms for the Weighted 1-Centre Problem on
  Tree Networks:**
  https://www.sciencedirect.com/science/article/pii/S1110016820302441
  "The optimality criterion for the DW 1-centre is minimizing the weighted
  distance to the furthest node from the facility. This problem has many
  applications in the emergency field since it gives satisfactory results for
  the customers sited at the furthest node of the deterministic tree network."

- **ResearchGate — Algorithms for Finding p-Centres on a Weighted Tree:**
  https://www.researchgate.net/publication/229970018_Algorithms_for_finding_P-centers_on_a_weighted_tree_for_relatively_small_P
  "The center problem concerns the longest distance from each customer to its
  closest facility. Both problems (center and median) are studied in tree
  networks. An O(n)-time algorithm for the backup 2-centre problem where two
  servers can fail — each finding the tree centre independently."

- **Springer Algorithmica — Optimal Algorithms for Path/Tree-Shaped Facility
  Location in Trees:**
  https://link.springer.com/article/10.1007/s00453-007-9157-8
  "Introduces a parametric-pruning method to solve conditional extensive
  weighted 1-centre location problems in trees in linear time. Parametric
  pruning (peeling nodes from the outside inward) is the same tree-trimming
  approach as Minimum Height Trees' leaf-peeling algorithm."

---

## 2. Emergency Services — Ambulance/Fire Station Placement

Placing an emergency facility (ambulance depot, fire station) in a tree-road
network to minimise the maximum response time maps exactly to the 1-centre
problem on a tree. The centre of the tree guarantees the shortest worst-case
travel time to any node in the network.

```
Road network as a tree:
  nodes = intersections / communities
  edges = roads with travel times (weights)
  facility = ambulance depot
  objective = minimise max(travel time to any node)
  solution = 1-centre of the weighted tree
           = centre of the unweighted tree (MHT) when weights are equal
```

---

## 3. Network Hub Selection — P2P and CDN Placement

In peer-to-peer networks and content delivery networks (CDNs), selecting the
hub or primary server at the centre of the network topology minimises the
maximum hop count to any leaf node — minimising worst-case latency.

The centre of the tree minimises the maximum distance to all other nodes.
Placing a hub at the tree centre guarantees that no node is more than height/2
hops away — exactly the MHT property.

AlgoMonster confirms: "The key insight is that the centre of the tree will
minimise the maximum distance to all other nodes. The root nodes that create
minimum height trees are those at the centre of the graph structure."

```
CDN placement:
  nodes = geographic PoPs (Points of Presence)
  tree = spanning tree of the network
  centre = optimal hub placement for minimum worst-case latency
```

### Reference

- **AlgoMonster — LeetCode 310 Minimum Height Trees:**
  https://algo.monster/liteproblems/310
  "The centre of the tree will minimise the maximum distance to all other nodes.
  By processing all leaves of each layer simultaneously, we ensure moving inward
  symmetrically from all directions. The algorithm terminates at the centre,
  leaving 1 or 2 nodes. Time complexity O(n) — we visit each node and edge once."

---

## 4. Centroid Decomposition — Divide-and-Conquer on Trees

The Minimum Height Trees algorithm finds the centroid(s) of a tree — the same
nodes used as roots in **centroid decomposition**, a powerful technique for
solving divide-and-conquer problems on trees.

In centroid decomposition:
1. Find the centroid (same as MHT centre — leaf peeling)
2. Root the tree at the centroid
3. Remove the centroid → recurse on each subtree
4. Each subtree's centroid is found by the same leaf-peeling algorithm

This guarantees each subtree has at most n/2 nodes → O(n log n) total
decomposition depth.

```
Applications of centroid decomposition:
  - Counting paths of given length in a tree
  - Tree DP with distance constraints
  - Nearest common ancestor (LCA) queries
  - Competitive programming standard tool
```

---

## 5. Jordan Centre — The Formal Graph Theory Term

The formal name for this problem's answer is the **Jordan centre** of a tree —
named after mathematician Camille Jordan who proved in 1869 that every tree
has 1 or 2 centres.

Jordan's theorem: every tree has a centre consisting of either one vertex or
two adjacent vertices. The centres are exactly the vertices that minimise
eccentricity (maximum distance to any other vertex) — the MHT roots.

Linear algorithms for finding the Jordan centre and path centre of a tree are
referenced in the facility location literature as foundational results.

---

## Summary

| Domain | Tree node = | Tree height = | MHT root = |
|--------|------------|---------------|-----------|
| Minimum Height Trees (#310) | Abstract node | Rooted tree depth | Centre node(s) |
| Facility location (1-centre) | Demand point | Max distance to facility | Optimal facility location |
| Emergency services | Community | Max response time | Optimal depot location |
| CDN/P2P hub | Network PoP | Max hop count | Optimal hub placement |
| Centroid decomposition | Tree node | Subtree depth | Centroid = MHT root |

---

## Further Reading

- 1-centre on tree networks (ScienceDirect): https://www.sciencedirect.com/science/article/pii/S1110016820302441
- p-centres on weighted trees (ResearchGate): https://www.researchgate.net/publication/229970018_Algorithms_for_finding_P-centers_on_a_weighted_tree_for_relatively_small_P
- Parametric pruning (Springer): https://link.springer.com/article/10.1007/s00453-007-9157-8
- AlgoMonster explanation: https://algo.monster/liteproblems/310
- Course Schedule II (Kahn's — same BFS structure): see `graphs/course-schedule-ii/`
