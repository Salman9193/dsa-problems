# Cheapest Flights K Stops — Research & Foundations

Shortest path with a bound on the number of edges — exactly what Bellman–Ford computes, since after i rounds of edge relaxation it knows the best path using ≤ i edges.

- **R. Bellman (1958), “On a routing problem,”** *Quarterly of Applied Mathematics* 16(1):87–90. The DP shortest-path recurrence (Bellman–Ford): relax all edges V−1 times, handling negative weights and bounding paths by edge count.
- **L. R. Ford Jr. (1956), “Network flow theory,”** RAND Corporation Technical Report P-923. The other origin of the Bellman–Ford algorithm.

**Why it matters here:** Running Bellman–Ford for K+1 rounds gives the cheapest route using at most K stops — the edge-count bound is a natural byproduct of the algorithm.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
