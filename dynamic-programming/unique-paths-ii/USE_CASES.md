# Unique Paths II — Real-World Use Cases

Counting routes across a grid **where some cells are blocked** — the obstacle version of grid
path-counting. It models any "how many ways from A to B, avoiding forbidden regions, with limited
move directions" question.

---

## 1. Robotics & Warehouse Routing

The literal case: a robot (or AGV) crossing a grid floor with **shelving, walls, or occupied
cells** as obstacles. Counting valid monotone routes feeds path diversity and congestion analysis —
how many distinct ways exist to get a bot from dock to bin without backtracking.

---

## 2. Network & Circuit Routing

Counting monotone routes across a routing grid where some tracks or nodes are **reserved or
faulty**. On a chip or a PCB, "how many obstacle-free monotone paths connect these pads?" measures
routing flexibility.

---

## 3. Game Level & Board Analysis

Grid games with impassable tiles: counting the number of shortest (monotone) paths a piece can take
across a board with blocked squares — used for level-difficulty tuning and move-space analysis.

---

## 4. Constrained Combinatorial Counting

Any lattice-path count with **forbidden points** — a classic combinatorics setup. Unlike the
obstacle-free case (a clean binomial `C(m+n-2, m-1)`), obstacles break the closed form and force
the DP, which is exactly why it exists: the DP handles arbitrary blocked-cell patterns that no
single formula covers.

---

## The Unifying Idea

```
count monotone (right/down) routes across a grid
with blocked cells = hard zeros in the recurrence
dp[r][c] = (blocked) ? 0 : dp[r-1][c] + dp[r][c-1]
```

| Domain | Grid | Obstacle |
|--------|------|----------|
| Warehouse robotics | floor tiles | shelves / occupied cells |
| Chip / PCB routing | routing tracks | reserved / faulty tracks |
| Board games | board squares | impassable tiles |
| Combinatorics | integer lattice | forbidden points |

---

## Further Reading

- Lattice path counting: https://en.wikipedia.org/wiki/Lattice_path
- Related: [Unique Paths #62](#dynamic-programming/unique-paths) (the obstacle-free case with the
  clean binomial formula), and [DP Taxonomy → Grid DP](#guides/DP_TAXONOMY).
