# Parallel Courses III — Real-World Use Cases

This is the **Critical Path Method (CPM)** — the algorithm behind essentially every project
schedule, build system, and dependency-driven pipeline. The question "what's the earliest
everything can be done?" is always *longest weighted path in a DAG*.

---

## 1. Project Scheduling (CPM / PERT)

The literal origin. Activities have durations and precedence constraints; unlimited activities
run in parallel given enough resources. The **critical path** — the longest weighted chain —
determines the project's **makespan**.

The actionable consequence: **only critical-path activities matter for the deadline.** Delaying
a task with *slack* costs nothing; delaying a critical task delays the entire project by the
same amount. That is why project managers obsess over the critical path.

Developed for the DuPont chemical plants (CPM, Kelley & Walker 1959) and the Navy's Polaris
missile programme (PERT, Malcolm et al. 1959).

---

## 2. Build Systems & CI/CD Pipelines

`make`, Bazel, Gradle, and every CI DAG solve exactly this: tasks with durations and
dependencies, executed in parallel. The **critical path is the build's lower bound** — no
amount of extra parallelism can beat it. Optimising a *non-critical* build step speeds up
nothing; this is how you know **which** step to optimise.

---

## 3. Task / Job Schedulers

Given a job DAG, the critical path tells you the minimum wall-clock time and which jobs are
*urgent*. See the
[Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler), which runs
Kahn's algorithm over exactly such a DAG.

---

## 4. Course Planning, Manufacturing, Circuit Timing

- **Course planning:** the literal reading — how many semesters to graduate.
- **Assembly lines / supply chains:** the slowest dependent chain sets the throughput.
- **Digital circuit timing analysis:** the **critical path** through combinational logic
  determines the maximum clock frequency. Same algorithm, different units.

---

## The Unifying Idea

```
tasks with durations + precedence + unlimited parallelism
      ⇒ makespan = LONGEST weighted path through the DAG (the critical path)
      ⇒ slack = how much a task can slip without delaying the project
finish[v] = time[v] + max(finish[u] : u → v),  in topological order
```

| Domain | Node | Weight | Critical path = |
|--------|------|--------|-----------------|
| Project mgmt | Activity | Duration | Project makespan |
| Build system | Build step | Compile time | Fastest possible build |
| Circuit design | Logic gate | Propagation delay | Max clock speed |
| Course planning | Course | Semester count | Time to graduate |

---

## Further Reading

- Critical path method: https://en.wikipedia.org/wiki/Critical_path_method
- Longest path problem (NP-hard in general, linear in a DAG):
  https://en.wikipedia.org/wiki/Longest_path_problem
- Related: [Course Schedule II](#graphs/course-schedule-ii) (the unweighted topological sort).
