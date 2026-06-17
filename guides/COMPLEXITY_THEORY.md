# Complexity Theory — P, NP, and NP-Complete

A practical guide to understanding why some problems are easy and others
are fundamentally hard — and what to do about it.

---

## Decision Problems

Complexity theory classifies **decision problems** — problems with a yes/no answer.

```
"Is this graph 2-colourable?"            → yes/no
"Does this array have a pair summing to k?" → yes/no
"Can this schedule fit in k time slots?" → yes/no
```

---

## The Two Fundamental Classes

### P — Polynomial Time

Problems solvable in O(n^k) time for some constant k.

```
Examples in P:
  Sorting              → O(n log n)
  Binary search        → O(log n)
  Shortest path        → O((V+E) log V)
  Bipartite check      → O(V+E)
  Dynamic programming  → O(n^2), O(n^3)
  Most LeetCode problems
```

**P means:** a computer can find the answer efficiently.

### NP — Nondeterministic Polynomial Time

Problems where a proposed solution can be **verified** in polynomial time,
even if finding one is hard.

```
Example: 3-Graph-Colouring
  Finding a valid 3-colouring? → unknown, may need exponential search
  Verifying a given colouring? → O(V + E), just check every edge
```

**NP means:** a computer can check an answer efficiently.

**Key relationship:** P ⊆ NP
- If you can find an answer in polynomial time, you can certainly check one.
- The open question: is P = NP? (Biggest unsolved problem in mathematics.)

---

## NP-Complete — The Hardest Problems in NP

A problem X is **NP-complete** if:
1. X is in NP (solutions verifiable in polynomial time)
2. Every other NP problem **reduces** to X in polynomial time

"Reduces" means: a polynomial-time solver for X would solve every NP problem.

```
NP-complete = universally hard
If you find a polynomial algorithm for ANY NP-complete problem:
  → P = NP
  → You win the $1 million Clay Millennium Prize
  → RSA encryption breaks immediately
```

---

## The Complexity Landscape

```
         ┌──────────────────────────────────────┐
         │                 NP                   │
         │   ┌────────────────────────┐         │
         │   │      NP-Complete       │         │
         │   │  3-SAT, 3-Colouring,   │         │
         │   │  Hamiltonian Path,     │         │
         │   │  Subset Sum, TSP,      │         │
         │   │  Clique, Vertex Cover  │         │
         │   └────────────────────────┘         │
         │   ┌──────────────┐                   │
         │   │      P       │                   │
         │   │  Sorting     │                   │
         │   │  BFS / DFS   │                   │
         │   │  Bipartite   │                   │
         │   │  2-SAT       │                   │
         │   └──────────────┘                   │
         └──────────────────────────────────────┘

(Diagram assumes P ≠ NP, which is widely believed but unproven)
```

---

## How Reduction Works

To prove problem B is NP-complete, show:
"If I could solve B efficiently, I could solve A efficiently"
where A is already known to be NP-complete.

Classic chain (Karp 1972):
```
Circuit SAT
    ↓ reduces to
3-SAT
    ↓ reduces to
Independent Set
    ↓ reduces to
Vertex Cover
    ↓ reduces to
3-Colouring
    ↓ reduces to
Hamiltonian Path
    ↓ ...
```

Karp's 1972 paper established 21 NP-complete problems, founding the field.
Reference: Karp, R.M. — *Reducibility Among Combinatorial Problems*, 1972.

---

## The Phase Transition: k=2 vs k=3

Many problems are easy for small k and NP-complete for k ≥ 3:

| Problem | k=2 | k≥3 |
|---------|-----|-----|
| Graph colouring | O(V+E) — bipartite check | NP-complete |
| Satisfiability | O(n) — 2-SAT (Aspvall 1979) | NP-complete (3-SAT) |
| Dimensional matching | O(n²) | NP-complete |

**Why the jump?**
With k=2, every assignment is **forced** — when you assign colour 1 to a node,
all its neighbours must be colour 2. No choices → no backtracking needed.
With k≥3, multiple valid colours exist at each step, and a wrong early
choice may only be detected much later — backtracking becomes necessary.

---

## What To Do With NP-Complete Problems in Practice

### 1. Exploit Special Structure
Some graph families admit polynomial k-colouring:
- **Chordal graphs** → polynomial colouring (perfect graphs)
- **Planar graphs** → always 4-colourable (Four Colour Theorem, 1976)
- **Trees** → always 2-colourable (bipartite)
- **Bounded treewidth** → linear-time colouring via tree decomposition

### 2. Approximate
- **Graph colouring:** greedy always uses ≤ Δ+1 colours (Δ = max degree)
- **Brooks' theorem:** can always achieve Δ colours unless graph is complete or odd cycle
- **DSATUR heuristic:** near-optimal in practice for sparse graphs

### 3. Backtrack on Small Inputs
For n ≤ 30-50, exponential backtracking with good pruning is fast enough.
Used in: Sudoku solvers, exam schedulers, register allocators (small register count).

### 4. Reduce to SAT
Modern SAT solvers (MiniSAT, Z3, CaDiCaL) handle millions of variables.
Encoding k-colouring as SAT is standard in production constraint solvers.

### 5. Integer Linear Programming (ILP)
For mission-critical exact solutions: frequency assignment, exam scheduling.
Solvers: Gurobi, CPLEX, OR-Tools.

---

## The LeetCode Connection

Most LeetCode problems are in P — solvable in O(n^k).
Backtracking problems are NP-complete in the general case, but test inputs
are small enough that exponential algorithms pass.

| LeetCode Pattern | Complexity Class | Why |
|-----------------|-----------------|-----|
| Binary search | P | O(log n) |
| Dynamic programming | P | O(n²) or O(n³) |
| Greedy algorithms | P | O(n log n) |
| BFS / DFS | P | O(V+E) |
| Bipartite check | P | Special case: k=2 colouring |
| Backtracking (general) | NP-complete | k-colouring, Hamiltonian, Subset Sum |
| Sudoku solver | NP-complete (general n×n) | 9-colouring of specific graph |
| N-Queens | NP-complete (general) | Constraint satisfaction |

---

## If P = NP (What Would Break)

- **RSA encryption** → private keys derivable from public keys in polynomial time
- **AES, SHA** → broken by polynomial-time brute force
- **Drug design** → protein folding (NP-hard) becomes tractable
- **Logistics** → Travelling Salesman solved optimally in seconds
- **Mathematics** → theorem proving becomes automated

This is why cryptography assumes P ≠ NP.

---

## Key Papers and References

- **Karp 1972** — *Reducibility Among Combinatorial Problems*: established 21 NP-complete problems. The founding paper of NP-completeness.
- **Cook 1971** — *The Complexity of Theorem Proving Procedures*: proved 3-SAT is NP-complete (Cook-Levin theorem).
- **Aspvall, Plass & Tarjan 1979** — 2-SAT solvable in linear time (P).
- **Appel & Haken 1976** — Four Colour Theorem: every planar graph is 4-colourable.
- **Brooks 1941** — Brooks' Theorem: every connected graph is Δ-colourable unless it's a complete graph or odd cycle.

---

## Summary

| Concept | Definition | Example |
|---------|-----------|---------|
| P | Find solution in poly time | Sorting, BFS, bipartite check |
| NP | Verify solution in poly time | 3-colouring, Subset Sum |
| NP-complete | Hardest problems in NP | 3-SAT, 3-Colouring, TSP |
| Reduction | Problem A → Problem B | 3-SAT reduces to 3-Colouring |
| P vs NP | Does finding = verifying? | Greatest open problem |

The key practical takeaway: when you encounter an NP-complete problem,
don't look for an efficient exact algorithm — look for structure to exploit,
approximations to use, or small-input backtracking to apply.
