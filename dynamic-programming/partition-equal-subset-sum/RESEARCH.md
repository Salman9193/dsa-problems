# Partition Equal Subset Sum — Research & Foundations

Splitting a set into two equal-sum halves — the subset-sum / partition problem: NP-complete in general, pseudo-polynomial by DP.

- **R. M. Karp (1972), “Reducibility among combinatorial problems,”** in *Complexity of Computer Computations*, pp. 85–103, Plenum Press. DOI: 10.1007/978-1-4684-2001-2_9. Places **subset-sum / partition** among the original NP-complete problems — why this DP is pseudo-polynomial, not polynomial. https://doi.org/10.1007/978-1-4684-2001-2_9
- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The origin of dynamic programming — optimal substructure and overlapping subproblems, the paradigm this problem is built on.

**Why it matters here:** Karp (1972) placed partition among the first NP-complete problems; Bellman’s DP still solves it in O(n·sum) pseudo-polynomial time via the boolean subset-sum table.

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
