# Parallel Courses III — Research & Foundations

This is the **Critical Path Method** forward pass: the longest duration-weighted path through a
DAG. CPM and PERT were both developed in 1959, and the surrounding theory is unusually clean —
including a well-documented account of **where this DP stops being the right tool**. *Citations
verified against Operations Research / Management Science / Proc. Eastern Joint Computer
Conference records this session — not from memory.*

- **J. E. Kelley, Jr. & M. R. Walker (1959), "Critical-Path Planning and Scheduling,"**
  *Proceedings of the Eastern Joint Computer Conference*, Boston, pp. 160–173. The original
  **CPM** paper, developed for DuPont's plant-maintenance scheduling.

- **J. E. Kelley, Jr. (1961), "Critical-Path Planning and Scheduling: Mathematical Basis,"**
  *Operations Research* 9(3):296–320. DOI: 10.1287/opre.9.3.296. The mathematical foundation:
  the forward/backward passes, float, and the **linear-programming** basis of the time–cost
  tradeoff. https://doi.org/10.1287/opre.9.3.296

- **D. G. Malcolm, J. H. Roseboom, C. E. Clark & W. Fazar (1959), "Application of a Technique
  for Research and Development Program Evaluation,"** *Operations Research* 7:646–669. The
  original **PERT** paper (the Navy's Polaris programme) — three-point estimates
  (optimistic / most likely / pessimistic) and the beta-distribution model.

- **A. B. Kahn (1962), "Topological sorting of large networks,"** *Communications of the ACM*
  5(11):558–562. DOI: 10.1145/368996.369025. The topological sort that sequences the DP — and,
  historically, motivated *precisely* by PERT-style project networks.
  https://doi.org/10.1145/368996.369025

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The relaxation
  `finish[v] = time[v] + max(finish[u])` is Bellman's principle of optimality applied along a
  topological order.

**Why it matters here:** the makespan is the **longest weighted path** in the DAG. That problem
is **NP-hard in a general graph** (it would solve Hamiltonian path), but **O(V + E) in a DAG**,
because a topological order guarantees each predecessor is finalized before it is needed.
**Acyclicity is exactly what makes the DP possible.**

---

## Where the DP Stops Working — Two Honest Caveats

**1. PERT's probabilistic extension is systematically biased.** The classical approach (sum the
means and variances *along the critical path*, then apply a normal approximation) **understates**
the expected project duration, for two reasons: the critical path is *itself random* (a
different path may become critical in a given scenario), and project completion is a **max** over
converging paths, where Jensen's inequality gives `E[max(X,Y)] ≥ max(E[X], E[Y])` — the
so-called **merge bias**. Established in:

- **K. R. MacCrimmon & C. A. Ryavec (1964), "An Analytical Study of the PERT Assumptions,"**
  *Operations Research* 12(1):16–37. The standard critique of PERT's statistical assumptions.

  The DP handles a *deterministic* max cleanly; it does **not** handle the max of *dependent
  random variables*. The honest modern answer is **Monte Carlo simulation**.

**2. Crashing (time–cost tradeoff) is a network-flow / LP problem, not a DP.** Shortening the
project by paying to speed up activities cannot be done by greedily crashing the cheapest
critical activity: crashing one path makes *another* path critical, and with parallel critical
paths you must crash an entire **cut**, not a single activity. The exact methods are
linear-programming and minimum-cut / min-cost-flow:

- **D. R. Fulkerson (1961), "A Network Flow Computation for Project Cost Curves,"**
  *Management Science* 7(2):167–178.
- **C. Phillips, Jr. & M. I. Dessouky (1977), "Solving the Project Time/Cost Tradeoff Problem
  Using the Minimal Cut Concept,"** *Management Science* 24:393–400.

**Related in this repo:** [Course Schedule](#graphs/course-schedule) /
[Course Schedule II](#graphs/course-schedule-ii) (the unweighted topological sort),
[Task Scheduler #621](#arrays/task-scheduler) (greedy scheduling bounds, Graham 1966), and the
[Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler), which
computes both CPM passes over its dependency DAG.
