# Combination Sum — Real-World Use Cases

## 1. Change-Making Problem — Coin Combinations

Finding all ways to make change for an amount using coin denominations is
exactly Combination Sum. Used in vending machine design, payment systems,
and economics (optimal denominations). The DP version counts combinations;
the backtracking version enumerates them.

## 2. Resource Allocation — Exact Budget Fulfillment

Given a set of project costs (reusable across time periods), find all
combinations of projects whose total cost exactly meets a budget constraint.
Used in capital budgeting and portfolio construction.

## 3. Cryptography — Subset Sum Problem Reduction

Combination Sum generalises to the subset sum problem (NP-complete in general).
Backtracking with pruning is the practical approach for cryptographic applications
where exact-sum subsets must be found (Merkle-Hellman knapsack attacks).
