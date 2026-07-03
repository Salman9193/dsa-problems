# Permutations — Real-World Use Cases

## 1. Travelling Salesman Problem — Route Enumeration

Finding the shortest route through n cities requires evaluating all (n-1)!
permutations of city visit orders. Backtracking with pruning (branch and bound)
avoids evaluating all n! — prunes subtrees whose partial cost already exceeds
the current best.

## 2. Task Scheduling — All Orderings

Determining the optimal task execution order (minimise makespan, maximise
throughput) requires considering all n! orderings. Backtracking explores these
with early pruning based on feasibility constraints (deadlines, dependencies).

## 3. Cryptographic Key Testing

Brute-force key search over all permutations of key components is an exponential
enumeration problem. Backtracking with known constraints prunes invalid key
prefixes early, reducing search space.
