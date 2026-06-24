# Minimum Cost to Hire K Workers — Real-World Use Cases

The minimum cost proportional hiring problem — selecting k workers at
minimum cost where pay is proportional to quality and each has a wage floor
— is studied directly in crowdsourcing optimisation and team formation literature.

---

## 1. Crowdsourcing — Minimum Cost Worker Selection with Quality Constraints

Online crowdsourcing platforms (Amazon Mechanical Turk, Figure Eight, Toloka)
assign tasks to workers with varying skill levels and wage requirements.
The platform wants to hire exactly k workers to complete a task batch,
minimising total cost while satisfying each worker's minimum wage
and maintaining pay proportional to skill.

This is structurally identical to LeetCode #857:

```
Crowdsourcing:
  quality[i]  = worker i's skill level (task completion accuracy)
  wage[i]     = worker i's minimum acceptable payment
  k           = number of workers needed

Constraint: all k workers paid at uniform rate r per skill unit
            r must satisfy r × quality[i] >= wage[i] for all hired workers

Objective:  minimise r × sum(quality[i])  over all valid groups of k
```

The greedy approach — sort by wage/quality ratio, maintain k-smallest-quality
heap — directly minimises crowdsourcing cost under this model.

### References

- **Paper:** *Optimization in Knowledge-Intensive Crowdsourcing*,
  arXiv:1401.1302.
  https://arxiv.org/pdf/1401.1302
  "The quality constraint requires the aggregated skill of assigned workers
  to be at least as large as the minimum skill requirement of the task.
  The cost constraint limits the aggregated workers' wage. The objective
  is worker-to-task assignment that maximises quality and minimises cost."

- **Paper:** *An Incentive Compatible Multi-Armed-Bandit Crowdsourcing
  Mechanism with Quality Assurance*, arXiv:1406.7157.
  https://arxiv.org/pdf/1406.7157
  "The minimum cost crowdsourcing problem maps to the minimum knapsack
  problem. The greedy algorithm sorts workers in ascending order of their
  cost-per-quality ratio and selects workers to satisfy the quality
  constraint at minimum cost — exactly the wage/quality sort in #857."

---

## 2. Team Formation — Minimum Cost per Marginal Skill Contribution

Team formation problems in crowdsourcing markets require assembling a team
that collectively covers all required skills at minimum cost. Each worker
has a bid (wage) and a set of skills (quality contribution). The greedy
mechanism selects the worker with the minimum cost per marginal skill
contribution — analogous to the wage/quality ratio sort.

### Reference

- **Paper:** *An Efficient and Truthful Pricing Mechanism for Team
  Formation in Crowdsourcing Markets*, arXiv:1812.04865.
  https://arxiv.org/pdf/1812.04865
  "The greedy mechanism selects the worker with the minimum cost per
  marginal skill contribution until a team that can complete the task
  is formed. In each iteration, it always selects the worker with the
  lowest bid/marginal_skill ratio — structurally identical to sorting
  by wage/quality in the minimum cost hiring problem."

---

## 3. Labour Economics — Proportional Pay and Minimum Wage Floors

The problem's two constraints — proportional pay within a group and
minimum wage floors — directly model real-world labour regulations:

**Proportional pay (equal pay for equal work):**
Many jurisdictions require that workers doing comparable work are paid
proportionally to their contribution. The proportionality constraint in
this problem models pay equity requirements.

**Minimum wage floors:**
The `wage[i]` values model minimum wage laws or collective bargaining
agreements — each worker has a floor below which they cannot be paid.

The algorithm finds the optimal group composition that satisfies both
simultaneously — the kind of optimisation a staffing agency or HR system
would run when assembling project teams under budget constraints.

---

## 4. Related Problem: Maximum Performance of a Team (#1383)

LeetCode #1383 is the dual problem: given engineers with `speed` and
`efficiency`, select at most k engineers to maximise `min(efficiency) × sum(speed)`.

```
#857:  minimise captain_ratio × sum(quality)    [cost minimisation]
#1383: maximise min(efficiency) × sum(speed)    [performance maximisation]
```

Both use the same algorithmic pattern:
- Sort by the "binding" metric (ratio / efficiency)
- Max-heap on the "sum" metric (quality / speed)
- Evaluate at each step when the binding metric is set by the current worker

---

## The Algorithmic Pattern

The core pattern — sort by one metric, maintain a k-element heap on another
metric — appears across many real-world optimisation problems:

```
Sort workers by:    wage/quality ratio        (captain constraint)
Heap maintains:     k smallest qualities      (cost minimisation)

Generalises to:
  Sort assets by:   risk/return ratio         (portfolio optimisation)
  Heap maintains:   k smallest variances      (minimum risk portfolio)

  Sort instances by: cost/compute ratio       (cloud resource selection)
  Heap maintains:   k lowest-cost instances   (minimum cloud bill)
```

---

## Summary

| Domain | quality[i] | wage[i] | Proportionality | Objective |
|--------|-----------|---------|----------------|-----------|
| Crowdsourcing (#857) | Worker skill | Min acceptable pay | Pay ∝ skill | Minimise total cost |
| Team formation | Marginal skill | Worker bid | Pay ∝ contribution | Minimise team cost |
| Cloud computing | Compute power | Instance cost | Workload ∝ power | Minimise cloud bill |
| Labour law compliance | Worker output | Legal minimum wage | Pay ∝ output | Min payroll cost |

---

## Further Reading

- Crowdsourcing optimisation: https://arxiv.org/pdf/1401.1302
- Quality-assured crowdsourcing: https://arxiv.org/pdf/1406.7157
- Team formation pricing: https://arxiv.org/pdf/1812.04865
- LeetCode #1383 (dual problem): Maximum Performance of a Team
