# Minimum Time Visiting All Points — Real-World Use Cases

The Chebyshev distance — minimum steps with 8-directional movement —
is a fundamental metric in robotics, game development, and warehouse
automation.

---

## 1. Robotics — Chebyshev Distance as A* Heuristic for 8-Connected Grids

The A* pathfinding algorithm requires an admissible, consistent heuristic
to guarantee finding the optimal path. For robots moving on 8-connected
grids (where diagonal movement is allowed), the Chebyshev distance is
the natural admissible heuristic — it never overestimates the true cost
and can be computed in O(1).

```
A* on 8-connected grid:
  h(node) = Chebyshev_distance(node, goal)
           = max(|node.x - goal.x|, |node.y - goal.y|)

This heuristic:
  - Is admissible: never overestimates true cost
  - Is consistent: satisfies triangle inequality
  - Is O(1): no search needed to compute
```

### Why Chebyshev beats Manhattan for 8-connected grids

Manhattan distance assumes 4-directional movement and overestimates
the true cost when diagonals are available — making it an inadmissible
heuristic for 8-connected grids. Chebyshev distance is tight: it equals
the true optimal cost when the grid is obstacle-free.

### References

- **Paper:** *Self-adaptive search algorithm for path planning based on
  the A* algorithm (ODDEA\*)*, Scientific Reports, 2025.
  https://www.nature.com/articles/s41598-025-28847-0
  "The Chebyshev distance supports eight-directional movement on
  two-dimensional grid maps and efficiently estimates the movement cost
  from a node to the target, thus reducing the possibility of redundant
  search."

- **Paper:** *Evaluating A* Heuristics: Manhattan, Euclidean, Chebyshev,
  and Hybrid*, TechRxiv, 2025.
  https://www.techrxiv.org/doi/pdf/10.36227/techrxiv.175416287.78856475/v1
  "Manhattan offers the fastest computation but suboptimal paths when
  diagonals are available. Euclidean and Chebyshev improve path quality.
  Chebyshev is the standard heuristic for 8-connected grids."

- **Paper:** *TransPath: Learning Heuristics for Grid-Based Pathfinding
  via Transformers*, arXiv:2212.11730.
  https://arxiv.org/pdf/2212.11730
  "A range of consistent and admissible heuristics are known for
  8-connected grids — the Chebyshev distance, Euclidean distance, and
  Octile distance. A* with an admissible heuristic is guaranteed to find
  the optimal solution."

---

## 2. Automated Warehouse Robotics

In automated storage and retrieval systems (AS/RS), robotic cranes and
automated guided vehicles (AGVs) can move both horizontally and vertically
simultaneously — exactly 8-directional movement on a discrete grid.

The time to retrieve an item at grid position (x, y) from the current
position (cx, cy) is:

```
retrieval_time = max(|x - cx|, |y - cy|)   ← Chebyshev distance
```

Routing optimisation for warehouse robots minimises the total Chebyshev
distance across all retrieval tasks — structurally identical to this problem.

### Reference

- **DataCamp — Chebyshev Distance: A Comprehensive Guide:**
  https://www.datacamp.com/tutorial/chebyshev-distance
  "In automated warehouses, robots often move along grid-like paths.
  Chebyshev distance helps optimise their movements, especially when
  they can move diagonally — significantly improving efficiency in item
  retrieval and storage."

---

## 3. Chess King Movement & Endgame Tablebases

The chess king moves in all 8 directions — one square at a time
horizontally, vertically, or diagonally. The minimum number of moves for
a king to travel from square A to square B is exactly the Chebyshev distance.

```
King from c5 (col=2, row=4) to e7 (col=4, row=6):
  dx = |4-2| = 2
  dy = |6-4| = 2
  Chebyshev = max(2,2) = 2 moves
  Path: c5 → d6 (diagonal) → e7 (diagonal)
```

Chess engines (Stockfish, Komodo) use Chebyshev distance in endgame
tablebases to compute the exact number of moves for kings to reach
key squares — the same formula as this problem.

### Reference

- **DataCamp — Chess King example:**
  https://www.datacamp.com/tutorial/chebyshev-distance
  "The distance from c5 to e7 is 2 because the king can reach e7 in
  two moves: one diagonal move to d6, followed by another diagonal move
  to e7. This grid-based approach highlights Chebyshev distance's
  practical application in chess and pathfinding algorithms."

---

## The Three Distance Metrics in Practice

| Metric | Formula | When to use |
|--------|---------|-------------|
| Manhattan | `|dx| + |dy|` | 4-connected grid (no diagonals) — city blocks |
| Euclidean | `√(dx²+dy²)` | Continuous space — real-world navigation |
| **Chebyshev** | **`max(|dx|,|dy|)`** | **8-connected grid — diagonals allowed** |

---

## Further Reading

- ODDEA* (Chebyshev A*): https://www.nature.com/articles/s41598-025-28847-0
- A* heuristic comparison: https://www.techrxiv.org/doi/pdf/10.36227/techrxiv.175416287.78856475/v1
- TransPath (learned heuristics): https://arxiv.org/pdf/2212.11730
- Chebyshev distance guide: https://www.datacamp.com/tutorial/chebyshev-distance
- Chebyshev distance (Wikipedia): https://en.wikipedia.org/wiki/Chebyshev_distance
