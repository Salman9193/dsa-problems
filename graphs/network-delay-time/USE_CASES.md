# Network Delay Time — Real-World Use Cases

Network Delay Time is the textbook Dijkstra's algorithm problem — and
Dijkstra's algorithm itself is one of the most widely deployed algorithms
in computing, running inside internet routing protocols and every major
GPS navigation system.

---

## 1. OSPF — Open Shortest Path First Routing Protocol

OSPF is the most direct production application of this exact problem.
OSPF is a link-state routing protocol that calculates the shortest path
to every destination in a network — literally running Dijkstra's
algorithm on each router.

OSPF is a link-state routing protocol designed for routing IP packets
within a single Autonomous System. Each router using OSPF maintains a
synchronized Link-State Database (LSDB) that reflects the current network
topology. Using Dijkstra's algorithm, each router calculates the shortest
and most cost-effective paths to every known destination in the network,
then updates its routing table with the best next-hop decisions.

### The structural parallel

```
Network Delay Time:                  OSPF routing:
  nodes = network nodes          ↔   nodes = routers
  k = signal source               ↔   k = the router computing its own table
  times[i]=[u,v,w] edge weight    ↔   link cost (based on bandwidth, latency)
  dist[i] = shortest delay to i   ↔   shortest cost to reach router i
  max(dist) = full propagation    ↔   the LSDB synchronisation guarantees
              time                    every router gets a consistent view
```

OSPF is based on a link-state routing algorithm in which each router
contains information of every domain and, based on this information,
defines the shortest path using the Dijkstra algorithm. Each router
independently runs Dijkstra's algorithm on the SAME topology data
(received via flooded Link-State Advertisements), guaranteeing all
routers reach consistent, loop-free routing decisions.

### References

- **ClouDNS — OSPF (Open Shortest Path First): What It Is and How It Works:**
  https://www.cloudns.net/blog/ospf-open-shortest-path-first-what-it-is-and-how-it-works/
  "Using Dijkstra's algorithm, each router calculates the shortest and
  most cost-effective paths to every known destination in the network.
  OSPF evaluates link costs based on bandwidth, creating a more accurate
  and efficient routing strategy than hop-count-based protocols."

- **USPTO Patent 7860106 — System and method for routing table computation
  and analysis:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7860106
  "OSPF is a link state routing protocol that stores information about
  every known link as a link state advertisement (LSA) within a link
  state database (LSDB). Using the well-known Dijkstra's algorithm to
  calculate the shortest path first (SPF), a routing table is computed
  that contains the shortest routes to every destination."

- **EECS 489 (University of Michigan) — Link State Routing / Dijkstra's
  SPF Algorithm course notes:**
  https://www.eecs.umich.edu/courses/eecs489/w10/winter10/lectures/lecture6_2.pdf
  "Algorithm complexity: each iteration extracts from minHeap O(log|N|),
  total O(|N|log|N|). Each neighbour of each node could also potentially
  go through the minHeap once: O(|E|log|N|). Total: O(|E| log |N|)" —
  the exact complexity bound used in this problem's Dijkstra's solution.

---

## 2. GPS Navigation — Google Maps, Waze, and Turn-by-Turn Routing

Every major GPS navigation system uses Dijkstra's algorithm (or its more
optimised variants) as the foundation for shortest/fastest route
computation — the same single-source shortest path problem as Network
Delay Time, applied to a road network instead of an abstract graph.

In real-world GPS navigation systems, cities and roads are represented
as graphs: nodes represent intersections or locations, edges represent
roads, and weights represent distance, time, or traffic congestion.
Dijkstra's algorithm explores all possible paths from a source point,
keeping track of minimum distances or costs to find the shortest or
fastest route.

### The exact structural mapping

```
Network Delay Time:                  GPS Navigation:
  network node                  ↔    road intersection
  times[i]=[u,v,w]               ↔    road segment with travel time w
  source k                       ↔    starting location
  dist[i]                        ↔    shortest travel time to location i
  max(dist) over all nodes       ↔    (not directly used in GPS — GPS wants
                                       dist[destination], a single target,
                                       which Dijkstra's also computes)
```

### Scaling challenges at production size

Estimating the shortest travel time between locations in a large
transportation network is computationally expensive when applying pure
Dijkstra's algorithm at scale. This motivates approximation techniques
(landmark-based methods, graph neural networks) for very large road
networks where exact per-query Dijkstra's would be too slow — though the
underlying shortest-path problem is identical to Network Delay Time.

### References

- **Medium (Manoj Badola) — Dijkstra's Algorithm in GPS Navigation
  (Google Maps):**
  https://medium.com/@23bt04006/dijkstras-algorithm-in-gps-navigation-google-maps-finding-optimal-road-routes-0b5ca6df06e8
  "In real-world GPS navigation systems, cities and roads are represented
  as graphs: nodes represent intersections or locations, edges represent
  roads, weights represent distance, time, or traffic congestion. Dijkstra
  designed the algorithm in just 20 minutes — it became one of the
  cornerstones of computer science."

- **arXiv:2501.09803 — Graph Neural Networks for Travel Distance
  Estimation and Route Recommendation Under Probabilistic Hazards:**
  https://arxiv.org/pdf/2501.09803
  "The most widely used approach for shortest distance estimation is
  Dijkstra's Algorithm, mainly used to calculate the shortest path.
  However, this option is computationally expensive when applied to
  large-scale networks. This paper proposes a fast framework based on
  graph neural networks which approximate the single-source shortest
  distance, motivated by the scalability limits of exact Dijkstra's
  at production road-network scale."

- **arXiv:2205.15190 — Vehicle Route Planning using Dynamically Weighted
  Dijkstra's Algorithm with Traffic Prediction:**
  https://arxiv.org/pdf/2205.15190
  "The weights of the edges are dynamic and can change with time,
  representative of a real traffic network. The weight of an edge
  signifies travel time conforming to traffic flow theory — the same
  single-source shortest path computation as Network Delay Time, with
  time-varying edge weights instead of fixed ones."

- **Codecademy — A Complete Guide to Dijkstra's Shortest Path Algorithm:**
  https://www.codecademy.com/article/dijkstras-shortest-path-algorithm
  "With a min-heap implementation, Dijkstra's algorithm achieves
  O((V+E) log V) time complexity. The algorithm powers technologies we
  use daily — from GPS navigation to network routing protocols like OSPF,
  and telecommunications signal routing — minimising latency in
  communication networks."

---

## 3. Telecommunications — Signal Propagation Delay (Direct Match)

This is literally the problem as stated: how long until a broadcast
signal reaches every node in a network. Telecommunications networks use
Dijkstra's algorithm to optimise signal routing and minimise latency —
exactly computing the network delay time for every node from a broadcast source.

---

## Summary

| Domain | Node = | Edge weight = | "Network delay" = |
|--------|--------|---------------|-------------------|
| Network Delay Time (#743) | Abstract network node | Signal transmission time | Max time for all nodes to receive signal |
| OSPF routing | Router | Link cost (bandwidth-based) | Time for routing table convergence |
| GPS navigation | Road intersection | Travel time/distance | Time to reach a specific destination |
| Telecom signal routing | Network switch/node | Propagation delay | Broadcast completion time |

---

## Further Reading

- OSPF + Dijkstra's (ClouDNS): https://www.cloudns.net/blog/ospf-open-shortest-path-first-what-it-is-and-how-it-works/
- Routing table computation patent: https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7860106
- EECS 489 Dijkstra's SPF complexity: https://www.eecs.umich.edu/courses/eecs489/w10/winter10/lectures/lecture6_2.pdf
- GPS navigation Dijkstra's (Medium): https://medium.com/@23bt04006/dijkstras-algorithm-in-gps-navigation-google-maps-finding-optimal-road-routes-0b5ca6df06e8
- GNN approximation at scale (arXiv): https://arxiv.org/pdf/2501.09803
- Dynamic traffic-weighted Dijkstra's (arXiv): https://arxiv.org/pdf/2205.15190
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
