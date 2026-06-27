# Shortest Path in Binary Matrix — Real-World Use Cases

The BFS shortest path on a binary grid is the **Lee algorithm** — one of
the foundational algorithms in electronic design automation, invented
specifically for PCB wire routing in 1961.

---

## 1. Lee Algorithm — PCB Wire Routing (The Origin of This Problem)

C.Y. Lee introduced this algorithm in 1961 at Bell Labs to solve the
wire routing problem for printed circuit boards: given two pins on a PCB
grid, find the shortest electrical connection path while avoiding other
components and existing wires.

### The structural parallel

```
Shortest Path in Binary Matrix:      Lee Algorithm for PCB routing:
  0 = passable cell              ↔    open grid point (no obstacle)
  1 = blocked cell               ↔    obstacle (component or existing wire)
  source: (0,0)                  ↔    source pin
  destination: (n-1,n-1)         ↔    target pin
  BFS wavefront expansion        ↔    Lee's wavefront propagation
  return path length             ↔    return wire length (routed path)
```

The Lee algorithm employs a BFS mechanism to propagate a wavefront from
the source cell across the grid, assigning distance labels to reachable cells
until the target is encountered, followed by backtracking to reconstruct the
shortest path. This guarantees the shortest path in terms of grid steps,
avoiding obstacles marked as blocked cells.

### PCB routing with cell weights

The generalised Lee algorithm assigns weights to grid points to control
routing preferences. The user specifies weight 1 for all open grid points
to find the shortest path. For minimal crossing of existing wires, existing
wire cells are assigned very high weights and free cells very low weights.
This is Dijkstra's algorithm on the grid — the weighted generalisation of
the BFS approach in this problem.

### References

- **Grokipedia — Lee Algorithm:**
  https://grokipedia.com/page/Lee_algorithm
  "The Lee algorithm is a BFS-based method for finding the shortest path
  between two points in a maze or routing area, modelled as a regular grid.
  It always gives an optimal solution, if one exists. The implementation
  uses a FIFO queue for expansion and a parent array or backpointers for
  path recovery."

- **Techie Delight — Lee Algorithm:**
  https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/
  "The Lee algorithm is one possible solution for maze routing problems
  based on BFS. It always gives an optimal solution, if one exists.
  In BFS, all cells having shortest path 1 are visited first, followed by
  adjacent cells with shortest path 2, and so on. If we reach any node in
  BFS, its shortest path is one more than its parent."

- **arXiv:1712.05202 — Cellular Automata Applications in Shortest Path:**
  https://arxiv.org/pdf/1712.05202
  "A very well known approach to routing problems is the Lee algorithm.
  Its purpose is to find an optimal path between two points on a regular grid.
  Each point of the grid is associated with a weight. The algorithm finds
  the path with the lowest sum of weights. By specifying weight one for all
  open grid points, the algorithm finds the path with the lowest number of
  grid points — the shortest path."

---

## 2. Game AI Pathfinding — 8-Directional Grid Movement

Game engines use BFS or A* on 8-directional grids for unit pathfinding.
The 8-directional BFS is the foundation — A* adds a heuristic to guide
the search toward the destination, reducing the number of cells explored
without changing the correctness guarantee.

### Why 8-directional matters in games

In classic 4-directional pathfinding, diagonal movement requires two steps
(right + down). This produces suboptimal paths that look unnatural —
units move in a "staircase" pattern. 8-directional movement allows true
diagonal steps, producing more natural, shorter paths.

```
4-directional path (n=5, clear grid):
  → → → → ↓ ↓ ↓ ↓  (8 steps)

8-directional path (n=5, clear grid):
  ↘ ↘ ↘ ↘  (4 diagonal steps = 5 cells)
```

Games using 8-directional BFS/A*: Age of Empires, StarCraft, Warcraft III,
most RTS games, and modern grid-based RPGs.

### A* as an optimised BFS

A* is BFS with a priority queue ordered by `f(n) = g(n) + h(n)`:
- `g(n)` = distance from start (same as BFS level)
- `h(n)` = heuristic estimate of distance to goal

For 8-directional grids, the Chebyshev distance heuristic is used:
`h(r,c) = max(|r - (n-1)|, |c - (n-1)|)`

A* with a perfect heuristic explores no unnecessary cells. BFS (this problem)
is A* with `h(n) = 0` — explores in all directions equally.

---

## 3. Maze Solving — Shortest Exit Path

Maze solving is the canonical application of the Lee algorithm. A maze
is a binary grid (0=open, 1=wall) and the problem asks for the shortest
path from entrance to exit.

Physical maze solvers (robots, Micromouse competitions) implement BFS to
find the shortest path, then re-run at full speed along the found path.
The Micromouse competition (IEEE since 1977) requires robots to solve a
16×16 maze using shortest-path algorithms — exactly this problem.

---

## 4. Bidirectional BFS Optimisation

For large grids, bidirectional BFS expands from BOTH source and destination
simultaneously, meeting in the middle. This reduces the search space from
O(n²) to O(n) in the optimal case — important for very large grids or
3D routing problems.

```
Forward BFS:  expand from (0,0)
Backward BFS: expand from (n-1,n-1)
Meeting condition: forward visited ∩ backward visited is non-empty
Answer: forward_dist[meeting] + backward_dist[meeting]
```

Bidirectional BFS is used in Google Maps and Apple Maps routing — expanding
from both origin and destination simultaneously.

---

## Summary

| Use case | Grid | Source | Destination | 8-dir? |
|----------|------|--------|-------------|--------|
| PCB routing (Lee, 1961) | PCB layout grid | Source pin | Target pin | Often 4-dir |
| Game pathfinding (A*) | Terrain/tile map | Unit position | Target position | Yes |
| Maze solving | Maze grid | Entrance | Exit | Optional |
| Micromouse (IEEE) | 16×16 physical maze | Start cell | Centre cell | Yes |

---

## Further Reading

- Lee algorithm (Grokipedia): https://grokipedia.com/page/Lee_algorithm
- Lee algorithm maze (Techie Delight): https://www.techiedelight.com/lee-algorithm-shortest-path-in-a-maze/
- Cellular automata + Lee (arXiv): https://arxiv.org/pdf/1712.05202
- A* search (Wikipedia): https://en.wikipedia.org/wiki/A*_search_algorithm
- Micromouse competition: https://en.wikipedia.org/wiki/Micromouse
- Bidirectional BFS: https://en.wikipedia.org/wiki/Bidirectional_search
