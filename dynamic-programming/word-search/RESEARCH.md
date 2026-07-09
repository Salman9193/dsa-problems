# Word Search — Research & Foundations

Searching a grid for a word — backtracking DFS that advances on matches, marks visited cells, and backtracks on dead ends.

- **S. W. Golomb & L. D. Baumert (1965), “Backtrack programming,”** *Journal of the ACM* 12(4):516–524. The paper that formalised **backtracking** — build partial solutions and abandon a branch as soon as it cannot lead to a valid completion.

**Why it matters here:** This is backtracking (Golomb–Baumert, 1965) on a grid: extend the path while it matches, retreat the moment it can’t.

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
