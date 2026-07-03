# House Robber — Real-World Use Cases

The "maximum non-adjacent selection" pattern models any resource allocation
problem where taking one resource blocks adjacent alternatives.

---

## 1. Job/Task Scheduling — Non-Conflicting Job Selection

In CPU scheduling, selecting non-overlapping tasks on a timeline (where
adjacent time slots conflict) is structurally identical to House Robber.
Each "house" is a time slot, each value is the task profit.

The decision-making approach of House Robber models maximum profit job
scheduling: at each job, either include it (and skip the next conflicting
job) or skip it. This is the foundation of weighted job scheduling with
interval conflicts.

---

## 2. Investment Portfolio — Non-Correlated Asset Selection

In portfolio optimisation under correlation constraints, selecting assets
that are not directly correlated (modelled as "adjacent" in a sorted list)
maps to House Robber. Maximise portfolio return subject to the constraint
that no two "adjacent" (highly correlated) assets are selected.

---

## 3. Network Security — Non-Adjacent Node Patching

In network security, patching a vulnerability on a node disables adjacent
nodes for a maintenance window. Selecting which nodes to patch to maximise
security coverage without creating a blackout forms a House Robber instance.

---

## Further Reading

- AlgoMonster House Robber: https://algo.monster/liteproblems/198
- Take-or-skip DP pattern: https://takeuforward.org/data-structure/dynamic-programming-house-robber-dp-6
