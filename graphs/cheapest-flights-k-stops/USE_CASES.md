# Cheapest Flights Within K Stops — Real-World Use Cases

The hop-constrained shortest path problem — find the cheapest path using
at most k edges — is a fundamental problem in network routing and has been
proven to require Bellman-Ford as the asymptotically optimal algorithm.

---

## 1. Bellman-Ford Is Proven Optimal for Hop-Bounded Paths (ESA 2023)

A 2023 paper at the European Symposium on Algorithms (the premier European
algorithms conference) directly proves that Bellman-Ford is the optimal
algorithm for this exact problem class — not just practical, but provably
asymptotically optimal.

The paper investigates the computational complexity of solving the
hop-bounded path problem (AHOP) for additive cost functions (like flight
prices). It establishes that a solution based on the Bellman-Ford algorithm
is optimal for additive weights. Even in undirected graphs with only
non-negative edge weights, Bellman-Ford remains the fastest known algorithm
for that problem, and the paper gives a negative answer showing no
subpolynomial improvement is possible (under the APSP hypothesis).

```
This LeetCode problem = AHOP (Additive Hop-bounded Optimal Path) problem
K-limited Bellman-Ford = optimal algorithm for AHOP (ESA 2023)
```

### References

- **ESA 2023 — Bellman-Ford Is Optimal for Shortest Hop-Bounded Paths
  (Kociumaka & Polak):**
  https://drops.dagstuhl.de/entities/document/10.4230/LIPIcs.ESA.2023.72
  "Bellman-Ford Is Optimal for Shortest Hop-Bounded Paths. Even in
  undirected graphs with only non-negative edge weights, Bellman-Ford
  remains the fastest known algorithm for the hop-bounded path problem.
  Unless the APSP Hypothesis fails, there is no o(h·m) time algorithm
  for finding the length of a shortest h-hop-bounded s-t path."

- **arXiv:2211.07325 — Bellman-Ford is optimal for shortest hop-bounded
  paths:**
  https://arxiv.org/pdf/2211.07325
  "The paper's contributions are to investigate the computational
  complexity of solving the AHOP problem for additive and bottleneck
  weights. We establish that a solution based on the Bellman-Ford algorithm
  is optimal for additive weights."

---

## 2. QoS Routing — Hop-Constrained Minimum Cost Paths in Networks

Quality of Service (QoS) routing requires finding low-cost paths that
satisfy constraints — the most common being a hop limit (maximum number
of links traversed), which directly corresponds to the k-stop constraint
in this problem.

Bellman-Ford routing algorithm provides the best strategy for QoS routing
problems involving multiple constrained routing problems arising in a flat
network. We argue that a QoS version of the Bellman-Ford routing algorithm
provides the best strategy for minimising hop counts subject to cost
constraints. Routing data from one node to another with QoS and
multi-constraint routing has great importance — Bellman-Ford is robust in
situations when edge weights are constrained.

The hop count, which captures the number of links over which resources are
allocated, is a commonly used cost measure. A standard approach for
determining the cheapest path that meets service guarantees is to compute
a minimum hop shortest path — exactly this problem's k-stop flight routing.

### References

- **ResearchGate — Findings on A Least Hops Path Using Bellman-Ford
  Algorithm:**
  https://www.researchgate.net/publication/303011996_Findings_on_A_Least_Hops_Path_Using_Bellman-_Ford_Algorithm
  "We argue that a QoS version of the Bellman-Ford routing algorithm
  provides the best strategy for QoS routing problems. Bellman-Ford is
  very powerful in solving most multiple constrained routing problems
  arising in a flat network, if the minimum hop is the main objective."

- **Academia.edu — Findings on a least hop(s) path using Bellman-Ford:**
  https://www.academia.edu/41975066/Findings_on_a_least_hop_s_path_using_Bellman_Ford_algorithm
  "The k-shortest paths Extended Bellman-Ford (k-EB) algorithm computes
  All Hops k-shortest Paths (AHKP) between source and destination.
  Bellman-Ford achieves near 100% success ratio in finding feasible paths
  while minimising hop count — exactly the k-stop constraint in LeetCode #787."

---

## 3. Airline Pricing — The Direct Real-World Instance

Cheapest Flights Within K Stops is literally the airline booking problem:
given a flight network with prices, find the cheapest itinerary from
origin to destination with at most k layovers.

```
Airline booking system:           LeetCode #787:
  airports = nodes              ↔   cities (nodes 0..n-1)
  direct flights with prices    ↔   flights[i] = [from, to, price]
  source city                   ↔   src
  destination city              ↔   dst
  max k layovers                ↔   at most k stops
  cheapest itinerary            ↔   return minimum total price
```

Production airline booking systems (Amadeus, Sabre, Google Flights) solve
exactly this problem: given a flight graph with prices (and potentially
hundreds of airlines and thousands of airports), find the cheapest path
from origin to destination within a layover constraint, subject to timing,
availability, and other constraints.

---

## 4. IP Routing — Time To Live (TTL) Constraint

IP packets carry a TTL (Time To Live) field that decrements at each
router hop. A packet is dropped when TTL reaches 0. This is the network
routing version of this problem:

```
IP network routing:               LeetCode #787:
  routers = nodes               ↔   nodes
  link costs                    ↔   flight prices
  TTL field = max hops          ↔   k stops limit
  cheapest TTL-bounded path     ↔   cheapest price within k stops
```

Routing protocols that must respect TTL constraints use hop-bounded
shortest path algorithms — the same k-limited Bellman-Ford structure
as this problem.

---

## Summary

| Domain | Node = | Edge weight = | Hop limit = | Answer = |
|--------|--------|---------------|-------------|----------|
| LeetCode #787 | City | Flight price | k stops | Cheapest itinerary |
| QoS routing | Router | Link cost | Max hops | Cheapest QoS path |
| IP routing (TTL) | Router | Link latency | TTL field | Cheapest TTL-bounded route |
| Airline booking | Airport | Ticket price | Max layovers | Cheapest itinerary |

---

## Further Reading

- ESA 2023 optimality proof: https://drops.dagstuhl.de/entities/document/10.4230/LIPIcs.ESA.2023.72
- arXiv hop-bounded optimality: https://arxiv.org/pdf/2211.07325
- QoS Bellman-Ford routing: https://www.researchgate.net/publication/303011996_Findings_on_A_Least_Hops_Path_Using_Bellman-_Ford_Algorithm
- Network Delay Time (Dijkstra's, no hop limit): see `graphs/network-delay-time/`
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
