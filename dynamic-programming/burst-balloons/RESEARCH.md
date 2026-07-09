# Burst Balloons — Research & Foundations

Interval DP: the optimum over a range depends on choosing the *last* balloon to burst, splitting the interval — the same structure as optimal matrix-chain multiplication and optimal BSTs.

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The origin of dynamic programming — optimal substructure and overlapping subproblems, the paradigm this problem is built on.

**Why it matters here:** The O(n³) solution fills a table over intervals, combining subintervals at each split point — Bellman’s dynamic programming in its classic interval-DP (matrix-chain-ordering) form.

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
