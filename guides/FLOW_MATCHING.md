# Floyd-Warshall, Max Flow / Min Cut, and Bipartite Matching

**Tier 3 — Advanced, Low Frequency**

Understand the IDEAS and KEY REDUCTIONS. Do not grind implementations early.
Flow and matching are real but rare in standard SWE interview loops — high
effort, low hit rate unless targeting a specialised team (trading, infra,
network systems, operations research).

---

## Part 1 — Floyd-Warshall: All-Pairs Shortest Path

### When to Use

| Need | Algorithm |
|------|-----------|
| Single source, non-negative weights | Dijkstra O(E log V) |
| Single source, negative weights | Bellman-Ford O(VE) |
| ALL pairs, any weights, small graph | **Floyd-Warshall O(V³)** |
| ALL pairs, non-negative, large sparse graph | Dijkstra × V: O(VE log V) |

Floyd-Warshall's triple loop is so compact that for small dense graphs
(V ≤ 200), it's often the cleanest answer even if Dijkstra × V is
asymptotically better.

### The Algorithm

```java
// Step 1: initialise distance matrix
int[][] dist = new int[n][n];
for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE / 2); // avoid overflow
for (int i = 0; i < n; i++) dist[i][i] = 0;
for (int[] edge : edges) dist[edge[0]][edge[1]] = edge[2]; // directed

// Step 2: triple loop — the entire algorithm
for (int k = 0; k < n; k++)           // intermediate node k
    for (int i = 0; i < n; i++)        // source i
        for (int j = 0; j < n; j++)    // destination j
            if (dist[i][k] + dist[k][j] < dist[i][j])
                dist[i][j] = dist[i][k] + dist[k][j];

// Step 3: check for negative cycles
for (int i = 0; i < n; i++)
    if (dist[i][i] < 0) // negative cycle detected
        throw new IllegalArgumentException("Negative cycle exists");
```

### The Invariant (Why It Works)

After the k-th outer iteration, `dist[i][j]` = length of the shortest
path from i to j that uses only nodes {0, 1, ..., k} as intermediate nodes.

```
k=0: only direct edges allowed
k=1: paths can go through node 0 or direct
k=2: paths can go through nodes 0 and 1
...
k=n-1: all nodes can be intermediates → true all-pairs shortest paths
```

Each iteration asks: "Is going through k shorter than the current best?"

### Detecting Negative Cycles

After the full algorithm, if `dist[i][i] < 0` for any i, node i is on
a negative cycle. The all-pairs distances are undefined in this case.

### Complexity

| | |
|--|--|
| Time | O(V³) — three nested loops |
| Space | O(V²) — distance matrix |

### LeetCode Problems Using Floyd-Warshall

| Problem | Why F-W | Alternative |
|---------|---------|------------|
| #1334 Find the City With Smallest Neighbours | All-pairs distances needed | Dijkstra × V |
| #743 Network Delay Time | Single source (use Dijkstra, not F-W) | Dijkstra |
| #399 Evaluate Division | All-pairs ratios on small graph | BFS per query |
| Transitive closure | Reachability between all pairs | F-W with bool matrix |

---

## Part 2 — Max Flow / Min Cut

### The Central Theorem (Most Important Idea)

**Max-Flow Min-Cut Theorem (Ford & Fulkerson, 1956):**

```
Maximum flow from s to t = Minimum cut capacity separating s from t
```

**Cut definition:** a partition of all nodes into two sets S (containing s)
and T (containing t). The cut capacity = sum of capacities of edges from S→T.

**Why this is powerful:** it converts a flow MAXIMISATION problem into a
cut MINIMISATION problem (and vice versa). Many real-world problems reduce
to finding a minimum bottleneck separating two groups of nodes.

### The Residual Graph

For each edge (u → v) with capacity c and current flow f:
- **Forward residual edge:** u → v with capacity c - f (remaining capacity)
- **Backward residual edge:** v → u with capacity f ("undo" this flow)

Back edges allow the algorithm to cancel previously committed flow —
equivalent to rerouting flow along a better path. This is what guarantees
the algorithm always finds the true maximum flow.

```
Example of why back edges matter:
  s → a → t (cap 1)
  s → b → t (cap 1)
  a → b     (cap 1)

Without back edges: might send s→a→b→t, then can't reach t again
With back edges:    can undo a→b, reroute to s→a→t and s→b→t → flow=2 ✓
```

### Algorithm Progression

#### Ford-Fulkerson (DFS augmentation)
```
while augmenting path exists (DFS on residual graph):
    bottleneck = min capacity along path
    augment flow along path
    update residual graph
```
- Time: O(E × max_flow) — can be infinite with irrational capacities
- Simple but impractical for large flows

#### Edmonds-Karp (BFS augmentation)
```
Same as Ford-Fulkerson but use BFS instead of DFS
→ always finds SHORTEST augmenting path
```
- Time: O(VE²) — polynomial guarantee
- BFS ensures at most O(VE) augmentations

#### Dinic's Algorithm (BFS + blocking flows)
```
while BFS finds path s→t in residual graph:
    build LEVEL GRAPH (only edges going to next BFS level)
    while blocking flow exists in level graph (DFS):
        find and augment blocking flow
        update residual graph
```
- Time: O(V²E)
- **Blocking flow:** saturates at least one edge in every s-t path
- Much faster in practice because it processes all paths of current length at once
- For unit-capacity graphs: O(E√V) — same as Hopcroft-Karp

**Why Dinic's is preferred:** instead of one augmenting path per BFS
(Edmonds-Karp), it finds ALL paths of the current shortest length simultaneously.

### Min Cut from Max Flow

After max flow is found, the min cut is:
1. Run BFS/DFS from s in residual graph
2. All nodes reachable from s → set S
3. All unreachable nodes → set T
4. Edges from S to T in ORIGINAL graph = min cut edges

### Applications of Max-Flow Min-Cut

| Application | Source | Sink | Edge capacity |
|-------------|--------|------|---------------|
| Network bandwidth | Server | Client | Link bandwidth |
| Pipeline flow | Pump station | Distribution | Pipe capacity |
| Bipartite matching | Virtual source | Virtual sink | 1 |
| Image segmentation | "Foreground" super-node | "Background" super-node | Pixel affinity |
| Project selection | Project | Profit/cost | Project value/dependency cost |

---

## Part 3 — Bipartite Matching

### The Most Important Fact: It Reduces to Max Flow

```
Given bipartite graph G = (L ∪ R, E):

1. Add source s, edges s → each l ∈ L (capacity 1)
2. Add sink t, edges each r ∈ R → t (capacity 1)
3. Keep all L→R edges (capacity 1)
4. Run ANY max flow algorithm on this graph
5. Maximum matching = max flow value
   Matched pairs = saturated L→R edges in final flow
```

This is why knowing bipartite matching = knowing max flow. You do NOT
need to implement Hopcroft-Karp from scratch — just reduce to flow.

```
        s ──1──> L1 ──1──> R1 ──1──> t
                  └─────1──> R2
        s ──1──> L2 ──1──> R1
                  └─────1──> R2 ──1──> t
```

### Hopcroft-Karp

Specialised for bipartite matching — conceptually equivalent to Dinic's
on the unit-capacity flow reduction, but optimised:

```
while BFS finds augmenting paths (builds alternating BFS tree):
    DFS to find maximal set of vertex-disjoint augmenting paths
    flip matched/unmatched on all found paths simultaneously
```

- Time: O(E√V) — processes O(√V) BFS phases, each O(E)
- Each BFS phase finds all shortest augmenting paths simultaneously
- **Augmenting path:** alternates between unmatched and matched edges,
  starting and ending at unmatched vertices. Flipping it increases matching by 1.

### Hungarian Algorithm

For **weighted** bipartite matching (minimum cost perfect matching):
- Each edge has a cost; find perfect matching of minimum total cost
- O(V³) — based on the assignment problem / potential functions
- Use case: assign n workers to n jobs minimising total cost
- Less common in standard SWE interviews

### König's Theorem

For bipartite graphs:
```
Maximum matching = Minimum vertex cover
```
(A vertex cover = set of vertices touching every edge.)

This is the bipartite analogue of max-flow min-cut.

---

## The Reduction Hierarchy

```
Bipartite Matching
      ↓ reduces to
   Max Flow (unit capacities, trivial 3-step construction)
      ↓ equivalent to (by duality)
   Min Cut (Max-Flow Min-Cut theorem)
      ↓ generalises to
   Linear Programming (LP duality = max-flow min-cut)

Weighted Bipartite Matching (assignment problem)
      ↓ reduces to
   Min-Cost Max-Flow
```

**The key insight for interviews:** when you see an assignment/scheduling
problem where items must be matched to resources with constraints, think
"bipartite graph → max flow → min cut." The reduction is almost always trivial.

---

## Algorithm Comparison

| Algorithm | Problem | Time | Key idea |
|-----------|---------|------|---------|
| Floyd-Warshall | All-pairs SP | O(V³) | DP on intermediate nodes |
| Ford-Fulkerson | Max flow | O(E × maxflow) | DFS augmenting paths |
| Edmonds-Karp | Max flow | O(VE²) | BFS augmenting paths |
| Dinic's | Max flow | O(V²E) | BFS levels + blocking flow |
| Hopcroft-Karp | Bipartite matching | O(E√V) | Dinic's on unit-capacity bipartite |
| Hungarian | Weighted bipartite | O(V³) | Potential functions |

---

## LeetCode Problems in This Space

### Floyd-Warshall
| # | Problem | Approach |
|---|---------|---------|
| 1334 | Find City With Fewest Reachable Neighbours | Floyd-Warshall or Dijkstra × V |
| 399 | Evaluate Division | BFS per query or Floyd-Warshall (V ≤ 26) |

### Max Flow (Conceptual / Indirect)
| # | Problem | Connection |
|---|---------|-----------|
| 1557 | Minimum Number of Vertices to Reach All Nodes | Not flow, but uses in-degree 0 concept |
| LC Hard problems | Usually disguised as "partition into groups with constraints" | Reduce to flow |

### Bipartite Matching (Conceptual)
| # | Problem | Connection |
|---|---------|-----------|
| 886 | Possible Bipartition | Is bipartite check (prerequisite to matching) |
| 785 | Is Graph Bipartite? | Same prerequisite check |
| Assignment problems | "Assign n workers to n tasks" | Direct bipartite matching → max flow |

---

## What to Know Cold for Interviews

**Floyd-Warshall:**
```java
for (k) for (i) for (j)
    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
```
Ten characters of loop structure. O(V³). Handles negatives. Detects negative cycles via dist[i][i] < 0.

**Max-Flow Min-Cut Theorem:**
"The maximum flow from s to t equals the minimum cut capacity separating s from t."
Know how to build the residual graph and why back edges are necessary.

**Bipartite Matching → Max Flow:**
Add source connected to all left nodes (cap 1), sink connected from all right nodes (cap 1), keep L→R edges (cap 1). Max flow = max matching.

---

## Real-World References

- **Dinic's algorithm in production:** used in image segmentation (graph cuts), network flow analysis, and scheduling systems. Google's Borg scheduler uses flow algorithms for task placement.

- **Wikipedia — Max-flow min-cut theorem:**
  https://en.wikipedia.org/wiki/Max-flow_min-cut_theorem
  "The max-flow min-cut theorem states that in a flow network, the maximum amount of flow passing from the source to the sink is equal to the minimum cut capacity."

- **Wikipedia — Hopcroft-Karp algorithm:**
  https://en.wikipedia.org/wiki/Hopcroft%E2%80%93Karp_algorithm
  "The Hopcroft-Karp algorithm is equivalent to Dinic's algorithm on unit-capacity bipartite graphs. It runs in O(E√V) time."

- **Wikipedia — Hungarian algorithm:**
  https://en.wikipedia.org/wiki/Hungarian_algorithm
  "The Hungarian algorithm is a combinatorial optimisation algorithm that solves the assignment problem in polynomial time, O(n³)."

- **Wikipedia — Floyd-Warshall algorithm:**
  https://en.wikipedia.org/wiki/Floyd%E2%80%93Warshall_algorithm
  "A dynamic programming algorithm for finding shortest paths in a weighted graph with positive or negative edge weights (but no negative cycles). O(V³) time and O(V²) space."
