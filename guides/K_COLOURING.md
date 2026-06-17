# Graph k-Colouring — From Bipartite to NP-Complete

How the bipartite check generalises to k-colouring, why the complexity
jumps at k=3, and what algorithms are used in practice.

---

## The Generalisation

**Bipartite check (k=2):** Can we colour nodes with 2 colours such that
no two adjacent nodes share a colour?

**k-colouring:** Can we colour nodes with k colours such that no two
adjacent nodes share a colour?

The answer for k=2 is findable in O(V+E). For k≥3, it's NP-complete.

---

## Why k=2 Is Special

With 2 colours, every assignment is **forced**:
```
Assign colour 1 to node A
→ ALL neighbours of A must be colour 2  (only one option)
→ ALL their neighbours must be colour 1 (only one option)
→ ...
```

No choices → no backtracking → greedy works → O(V+E).

With k=3:
```
Assign colour 1 to node A
→ Neighbours can be colour 2 OR colour 3  (two options)
→ A wrong early choice may cause a conflict detected much later
→ Backtracking required → exponential worst case
```

---

## Complexity

| k | Complexity | Algorithm |
|---|-----------|-----------|
| 1 | O(E) — check if no edges | Edge count |
| **2** | **O(V+E)** | **BFS/DFS 2-colouring (bipartite check)** |
| 3 | NP-complete | Backtracking, SAT reduction |
| 4 | NP-complete | Backtracking, SAT reduction |
| k ≥ 3 | NP-complete | Backtracking, approximation |

Special case: **planar graphs** are always 4-colourable (Four Colour Theorem).

---

## Algorithm: Backtracking for k-Colouring

```java
public boolean graphColouring(int[][] graph, int k) {
    int[] colour = new int[graph.length];
    return backtrack(graph, colour, k, 0);
}

private boolean backtrack(int[][] graph, int[] colour, int k, int node) {
    if (node == graph.length) return true;  // all nodes coloured ✓

    for (int c = 1; c <= k; c++) {
        if (isSafe(graph, colour, node, c)) {
            colour[node] = c;
            if (backtrack(graph, colour, k, node + 1)) return true;
            colour[node] = 0;  // undo — try next colour
        }
    }
    return false;  // no valid colour found
}

private boolean isSafe(int[][] graph, int[] colour, int node, int c) {
    for (int neighbour : graph[node]) {
        if (colour[neighbour] == c) return false;  // conflict
    }
    return true;
}
```

**Complexity:** O(k^V) worst case — exponential.

---

## Connection to Bipartite (k=2)

Setting k=2 in the backtracking solution gives something equivalent to
the BFS bipartite check — but much slower because backtracking doesn't
exploit the "forced assignment" structure.

The BFS bipartite check is the O(V+E) shortcut that works exclusively
when k=2:

```java
// BFS bipartite check — exploits forced assignment (k=2 only)
colour[neighbour] = -colour[node];   // only ONE valid choice

// vs backtracking k-colouring — must try all k choices
for (int c = 1; c <= k; c++) {
    if (isSafe(...)) { colour[node] = c; recurse; colour[node] = 0; }
}
```

---

## Practical Algorithms for k-Colouring

### 1. Greedy Colouring (Fast, Approximate)

Assign each node the smallest colour not used by its neighbours.

```java
public int[] greedyColour(int[][] graph) {
    int n = graph.length;
    int[] colour = new int[n];  // 0 = uncoloured
    Arrays.fill(colour, -1);
    colour[0] = 0;

    for (int node = 1; node < n; node++) {
        Set<Integer> usedByNeighbours = new HashSet<>();
        for (int nb : graph[node])
            if (colour[nb] != -1) usedByNeighbours.add(colour[nb]);

        int c = 0;
        while (usedByNeighbours.contains(c)) c++;
        colour[node] = c;
    }
    return colour;
}
```

**Result:** Uses at most Δ+1 colours (Δ = max degree).
**Brooks' Theorem:** Always possible to achieve Δ colours unless graph is
a complete graph (Kn) or odd cycle.

### 2. DSATUR (Near-Optimal Heuristic)

At each step, colour the node with the most distinct colours among its
neighbours (highest "saturation"). Better than greedy in practice.

### 3. SAT Reduction (Exact, Modern)

Encode k-colouring as a SAT formula, use a modern SAT solver:
```
For each node v: at least one of {colour_v_1, ..., colour_v_k} is true
For each node v: at most one colour is true (mutual exclusion)
For each edge (u,v), for each colour c: NOT (colour_u_c AND colour_v_c)
```

Modern SAT solvers (Z3, CaDiCaL, MiniSAT) handle millions of variables.

---

## Key Bounds and Theorems

| Theorem | Statement | Implication |
|---------|-----------|-------------|
| **2-colour = bipartite** | G is 2-colourable ↔ G is bipartite ↔ no odd cycles | O(V+E) algorithm |
| **Brooks' Theorem (1941)** | χ(G) ≤ Δ(G), unless G is Kn or odd cycle | Greedy achieves near-optimal |
| **Four Colour Theorem (1976)** | Every planar graph is 4-colourable | Geography maps need ≤ 4 colours |
| **3-colouring is NP-complete** | Proved by Stockmeyer 1973 via reduction from 3-SAT | No poly algorithm known |
| **Hückel's theorem** | G bipartite ↔ adjacency spectrum symmetric about 0 | Chemistry connection |

---

## The Chromatic Number χ(G)

The **chromatic number** χ(G) is the minimum k for which G is k-colourable.

```
χ(G) = 1  ↔  G has no edges
χ(G) = 2  ↔  G is bipartite (detectable in O(V+E))
χ(G) = 3  ↔  finding this is NP-complete
χ(G) ≤ Δ+1  always (greedy upper bound)
χ(G) ≤ 4   for all planar graphs (Four Colour Theorem)
```

Computing χ(G) exactly is NP-hard. Approximating it within a constant
factor is also NP-hard. This is an extremely hard problem.

---

## Real-World Applications

| Application | Graph | k = |
|-------------|-------|-----|
| **Register allocation** (LLVM/GCC) | Variable interference graph | Number of CPU registers |
| **Exam scheduling** | Student-conflict graph | Number of time slots |
| **Frequency assignment** (4G/5G) | Cell tower adjacency graph | Number of frequencies |
| **Sudoku** | Cell constraint graph | 9 |
| **Map colouring** | Country adjacency graph | ≤ 4 (Four Colour Theorem) |
| **Bipartite check** | Any graph | 2 |

---

## Bipartite → k-Colour: The Full Picture

```
k=1:   Trivial — only if graph has no edges
k=2:   Bipartite check — O(V+E), greedy, polynomial ← LeetCode #785
k=3:   NP-complete (Stockmeyer 1973)
k=4:   NP-complete BUT planar graphs always 4-colourable (Appel & Haken 1976)
k≥3:   NP-complete in general, approximable within Δ+1 (greedy)
k=χ(G): The chromatic number — NP-hard to compute exactly
```

The bipartite check is the unique special case where the colouring problem
is solvable in polynomial time. Every other value of k (k ≥ 3) is NP-complete.

---

## Further Reading

- See `COMPLEXITY_THEORY.md` for P vs NP background
- Karp 1972 — 21 NP-complete problems: https://dl.acm.org/doi/10.1007/978-1-4684-2001-2_9
- Four Colour Theorem: https://en.wikipedia.org/wiki/Four_color_theorem
- Brooks' Theorem: https://en.wikipedia.org/wiki/Brooks%27_theorem
- DSATUR algorithm: https://en.wikipedia.org/wiki/DSATUR
- Graph colouring (Wikipedia): https://en.wikipedia.org/wiki/Graph_coloring
