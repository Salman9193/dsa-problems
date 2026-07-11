# Combination Sum II — Research & Foundations

This is a **backtracking** enumeration with a duplicate-pruning refinement. Its foundation
is the classic formalization of backtracking; the "skip equal values at the same level"
rule is a symmetry-breaking pruning on top of it. *Citation verified against JACM records
this session — not from memory.*

- **S. W. Golomb & L. D. Baumert (1965), "Backtrack programming,"** *Journal of the ACM*
  12(4):516–524. The paper that formalized **backtracking** — build partial solutions and
  abandon a branch as soon as it cannot lead to a valid completion. The `c[i] > remaining`
  prune (on sorted input) and the exhaustive branch-and-retreat search here are exactly
  this method.

**Why it matters here:** the search explores the tree of sub-multisets, pruning branches
that overshoot the target (Golomb–Baumert). The added `i > start && c[i] == c[i-1]` skip is
a **symmetry-breaking** step: among equal-valued choices at one level it keeps only the
first, so equivalent selections are enumerated once — the standard technique for
backtracking over a multiset.

**Related in this repo:** the reusable-candidates sibling
[Combination Sum #39](#dynamic-programming/combination-sum) (and its DP-tabulation note on
when counting/min variants become polynomial), plus the same duplicate-skip pattern in
Subsets II (#90) and Permutations II (#47). For the *counting* cousin, see
[Coin Change #322](#dynamic-programming/coin-change).
