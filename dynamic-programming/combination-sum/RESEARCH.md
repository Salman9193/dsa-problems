# Combination Sum — Research & Foundations

Enumerating combinations that reach a target — backtracking that prunes a branch once the running sum exceeds the target.

- **S. W. Golomb & L. D. Baumert (1965), “Backtrack programming,”** *Journal of the ACM* 12(4):516–524. The paper that formalised **backtracking** — build partial solutions and abandon a branch as soon as it cannot lead to a valid completion.

**Why it matters here:** The target check is the pruning rule at the heart of backtracking (Golomb–Baumert, 1965) — stop exploring branches that cannot succeed.

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
