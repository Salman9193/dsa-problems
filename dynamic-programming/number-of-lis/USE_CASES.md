# Number of LIS — Real-World Use Cases

Counting **how many optimal solutions exist**, not just finding one, is a recurring need — it
measures how *robust* or *unique* an optimum is.

---

## 1. Counting Optimal Paths / Solutions

Whenever you've computed a best answer, "how many ways achieve it?" tells you whether the
optimum is unique or one of many. Applications: counting shortest paths in a graph, counting
optimal alignments in bioinformatics (equally-scoring DNA/protein alignments), and counting
maximum-scoring schedules.

The **reset/accumulate** rule here is the same one used to count shortest paths in Dijkstra/BFS
(reset the count on a strictly shorter path, accumulate on an equal-length one).

---

## 2. Robustness & Degeneracy Analysis

A single optimum means the answer is forced; a *large* number of optima means the objective is
degenerate and other criteria (cost, risk, preference) can be used to break the tie without
losing optimality. This drives tie-breaking policy in scheduling, routing, and resource
allocation.

---

## 3. Combinatorial Enumeration

Counting maximum chains in a poset — the number of longest increasing runs in a time series,
the number of maximal nesting arrangements, or the number of longest divisibility chains — all
use this identical count-alongside-DP technique.

---

## The Unifying Idea

```
len[i] = best value ending at i
cnt[i] = how many ways achieve that best
  strictly better  -> RESET      (the old ways are no longer optimal)
  equally good     -> ACCUMULATE (a genuinely different optimal way)
answer = SUM of cnt[i] over every i achieving the global best
```

| Domain | The "optimum" | The count tells you |
|--------|---------------|---------------------|
| Shortest paths | Path length | How many shortest routes |
| Sequence alignment | Alignment score | How many equally-good alignments |
| Scheduling | Makespan | How much freedom to tie-break |

---

## Further Reading

- Counting shortest paths (same reset/accumulate rule): https://en.wikipedia.org/wiki/Shortest_path_problem
- Related: [LIS #300](#dynamic-programming/longest-increasing-subsequence),
  [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) — the same
  counting technique applies with a different comparability test.
