# Find if Path Exists in Graph — Real-World Use Cases

Graph reachability — the core of this problem — underpins network routing,
social network analysis, distributed systems, and dynamic connectivity.

---

## 1. Social Networks — Degrees of Separation

LinkedIn "Are you connected to X?" and Facebook friend suggestions both
start with a graph reachability check: can user A reach user B through
the social graph?

BFS explores all first-degree connections (friends) before moving to
second-degree connections (friends of friends). This property makes BFS
ideal for finding the shortest path in unweighted graphs such as social
networks and for web crawling where search engines explore pages level by level.

For a simple reachability check (does a path exist?), Union-Find is more
efficient: after building the friend graph once, each "are they connected?"
query is O(α(n)) ≈ O(1).

### Reference

- **Paper:** *HDBMS: A Context-Aware Hybrid Graph Traversal Algorithm for
  Efficient Information Discovery in Social Networks*, arXiv:2508.14092.
  https://arxiv.org/pdf/2508.14092
  "Graph traversal algorithms are central to applications in network
  analysis, pathfinding in AI, and bioinformatics. DFS explores each
  branch exhaustively — ideal for complete path finding — while BFS
  operates level by level, providing shorter paths and benefiting
  shortest-path problems. HDBMS was evaluated against DFS, BFS, and
  hybrid techniques over large-scale social networks, biological networks,
  and transportation graphs."

---

## 2. Network Routing — Is a Host Reachable?

Every routing protocol (OSPF, BGP, IS-IS) and every network diagnostic
tool (`ping`, `traceroute`) starts with a reachability check — can packets
get from source to destination given the current network topology?

The BFS approach is the basis for flooding protocols: broadcast a packet
from source, mark each reachable router as visited, determine the reachable
set. Unreachable destinations are dropped early.

The Union-Find approach is used by network management systems to pre-partition
the network into connected components — any source/destination pair in
different components is immediately known to be unreachable without traversal.

---

## 3. Dynamic Connectivity — Union-Find in Production Systems

In production systems (distributed databases, microservice meshes, version
control), the graph of connections changes over time (edges added and removed).

The union-find (disjoint-set union) data structure with rollback support
is the standard exact method for offline fully dynamic connectivity. In 1972,
Tarjan introduced linear-time algorithms leveraging DFS trees to compute
connected components, articulation points, and bridges, demonstrating how
depth-first exploration efficiently uncovers graph structure.

For the online case (only insertions, no deletions), Union-Find handles
each edge insertion in O(α(n)) — making it the preferred structure for
incrementally built graphs (stream processing, live network topology updates).

For the fully dynamic case (insertions AND deletions), link-cut trees
provide O(log n) per operation.

### Reference

- **Grokipedia — Dynamic Connectivity:**
  https://grokipedia.com/page/Dynamic_connectivity
  "A standard exact method for offline fully dynamic connectivity uses the
  union-find (disjoint-set union) data structure with rollback support.
  With path compression and union by rank, both operations run in amortised
  time O(α(n)), where α is the inverse Ackermann function — effectively
  constant time for all reasonable input sizes."

---

## 4. Git — Commit Reachability

Git uses graph reachability to answer questions like:
- Is commit A an ancestor of branch B? (`git merge-base`)
- Which commits are reachable from a tag? (`git log TAG`)
- Should a branch be fast-forwarded or does it need a merge?

All of these reduce to: does a path exist from node A to node B in the
commit DAG? Git uses DFS internally for most reachability queries.

---

## 5. Garbage Collection — Memory Reachability

Garbage collectors (JVM, V8, CPython) mark all memory blocks reachable
from root references (stack, global variables) using BFS or DFS traversal
of the reference graph. Memory blocks NOT reachable from any root are
unreachable — they can be collected. This is graph reachability applied
to memory management.

BFS can help with garbage collection algorithms to discover and free memory
that is no longer reachable: represent the memory space as a graph where
nodes are allocated memory blocks and edges are references or pointers
between them. BFS from root nodes marks all reachable blocks; unmarked
blocks are collected.

---

## Algorithm Selection Guide

| Use case | Edges | Queries | Best algorithm |
|----------|-------|---------|---------------|
| Single reachability check | Static | 1 | BFS or DFS — simpler |
| Multiple reachability queries | Static | Many | Union-Find — O(1) per query |
| Shortest path + reachability | Static | Any | BFS — gives both |
| Incremental edges, many queries | Growing | Many | Union-Find — O(α) per edge+query |
| Edge deletions too | Dynamic | Many | Link-cut trees — O(log n) |
| Directed graph reachability | Directed | Any | BFS/DFS only — Union-Find is undirected |

---

## Further Reading

- HDBMS hybrid graph traversal (social networks): https://arxiv.org/pdf/2508.14092
- Dynamic connectivity (Grokipedia): https://grokipedia.com/page/Dynamic_connectivity
- Union-Find guide in this repo: see `guides/UNION_FIND.md`
- Tarjan's DFS algorithms (1972): foundational paper on DFS-based connectivity
