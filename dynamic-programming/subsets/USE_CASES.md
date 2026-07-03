# Subsets — Real-World Use Cases

## 1. Feature Selection in Machine Learning

Enumerating all subsets of features to find the optimal feature set for a
model is exponential (2^n subsets for n features). Backtracking with pruning
(branch and bound) avoids evaluating all 2^n — it prunes subtrees where
adding more features can't improve the current best.

## 2. Combinatorial Testing (t-way testing)

Software testing requires testing all combinations of parameter values. The
power set of test parameters gives all possible test cases. Backtracking
generates these systematically and can prune based on coverage criteria.

## 3. Power Set in Cryptography

Backtracking subset enumeration is used in subset-sum cryptosystems (Merkle-
Hellman knapsack) and in birthday attack analyses where all subsets of a
message space must be enumerated to find collisions.
