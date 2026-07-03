# Burst Balloons — Real-World Use Cases

## 1. Matrix Chain Multiplication — O(n³) Interval DP

The canonical interval DP problem in CS curricula is matrix chain multiplication:
given a sequence of matrices, find the optimal parenthesisation that minimises
scalar multiplications. The same interval DP template applies directly.

Used in compilers (optimising tensor operations), neural network inference
(batched matrix operations), and numerical linear algebra libraries.

## 2. Optimal Binary Search Tree Construction

Given keys with access frequencies, build a BST that minimises expected
search cost. Interval DP on key ranges: dp[i][j] = min cost of optimal
BST on keys i..j. Used in database index construction and compiler symbol
table optimisation.

## 3. Genomic Sequence Assembly

In genomic assembly, merging overlapping sequence fragments in optimal
order to minimise assembly errors uses interval DP. The "burst" operation
corresponds to merging two adjacent fragments, and the cost depends on
the merged result — the same structure as burst balloons.
