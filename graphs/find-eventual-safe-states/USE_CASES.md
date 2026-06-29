# Find Eventual Safe States — Real-World Use Cases

Finding nodes that are guaranteed to terminate (safe) vs those that can get
stuck in infinite loops (unsafe) is the core of deadlock analysis, model
checking, and formal verification.

---

## 1. OS Deadlock Detection — Safe States and the Wait-for Graph

The concept of "safe state" in this problem comes directly from operating
systems theory. In OS terminology:

- A **safe state** is one from which the OS can guarantee all processes
  will eventually complete (reach a terminal state)
- An **unsafe state** is one that could lead to deadlock (a cycle in the
  resource-allocation graph where every process waits for another)

The **Wait-for Graph (WFG)** is the OS equivalent of this problem's graph:
nodes = processes, edges = "process A waits for process B to release a resource".
A cycle in the WFG = deadlock. Safe nodes = processes guaranteed to terminate.

```
Find Eventual Safe States:           OS Wait-for Graph:
  node = graph node              ↔    process
  terminal node (no out-edges)   ↔    process with all resources it needs
  safe node                      ↔    process guaranteed to complete (no deadlock)
  cycle                          ↔    deadlock
  unsafe node (leads to cycle)   ↔    process that will deadlock
```

A Wait-for-Graph (WFG) algorithm maps the relationship between processes
and resources to construct a graph representing the state of system resource
allocations. By assessing this graph, the OS can identify circular cycles in
which multiple processes are requesting the same resource — creating a
deadlock condition.

The **Banker's Algorithm** (Dijkstra 1965) ensures the system is always in
a safe state — it refuses resource allocations that would move the system
into an unsafe state, exactly as defined in this problem.

### References

- **GeeksforGeeks — Deadlock Detection and Recovery:**
  https://www.geeksforgeeks.org/operating-systems/deadlock-detection-recovery/
  "Prevention: The OS ensures the system is always in a safe state where
  deadlocks cannot occur — achieved through resource allocation algorithms
  such as the Banker's Algorithm. Detection: Wait-For Graph identifies
  circular cycles in which multiple processes are requesting the same resource."

- **Groundcover — Deadlock Detection Explained:**
  https://www.groundcover.com/learn/performance/deadlock-detection
  "A Wait-for-Graph algorithm maps the relationship between processes and
  resources to construct a graph representing the state of system resource
  allocations. By assessing this graph, the OS can identify circular cycles
  creating a deadlock condition."

- **Medium — Understanding Deadlocks in OS:**
  https://medium.com/@d24dce136/understanding-deadlocks-in-operating-systems-from-modelling-to-recovery-691f9aa281a7
  "Safe states: dynamic allocation checking ensures safe states (Banker's
  Algorithm). Detection: identify cycles via Wait-for Graph. Safe node =
  process guaranteed to complete without deadlock."

---

## 2. Model Checking — Liveness Properties and Termination Detection

In formal verification and model checking, the "eventual safe states"
problem models **liveness properties**: does every execution path eventually
terminate or reach a desired state?

A **liveness property** states: "something good eventually happens."
In model checking terms: "from every reachable state, there is a path to a
good (terminal) state." Nodes that can reach a cycle violate liveness —
they may loop forever without terminating.

Model checking algorithms like CTL (Computation Tree Logic) formulas
`AG EF q` (from every state, a quiescent state q is eventually reachable)
directly correspond to finding eventual safe states.

Safety properties expressed in CTL as AGEF q (where q is a set of quiescent
states) require verifying that from every reachable state, some quiescent
(terminal) state is eventually reachable. The distributed search constructs
a path of "helpful" transitions from each reachable state to a state that is
either quiescent or known to have a path to a quiescent state — the same
reachability argument as finding eventual safe states.

A **fault-tolerant termination detection** algorithm uses model checking to
verify two core properties: (1) if the basic algorithm has terminated, then
termination will eventually be announced (liveness); (2) if termination is
announced, it is correct (safety). These are exactly the "safe state"
properties in this problem.

### References

- **Academia.edu — Deadlock and Termination Detection using IMDS Formalism
  and Model Checking:**
  https://www.academia.edu/30571754/Deadlock_and_Termination_Detection_using_IMDS_Formalism_and_Model_Checking_Version_2
  "Safety or liveness properties of the system can be asked in temporal logic.
  An agent falls into a deadlock if it waits for a server's state that cannot
  occur — exactly an unsafe node (can reach a cycle, never terminates)."

- **arXiv:2602.00272 — A Fault-Tolerant Version of Safra's Termination
  Detection Algorithm:**
  https://arxiv.org/pdf/2602.00272
  "Two main properties: (1) liveness — if execution has terminated, termination
  is eventually announced; (2) safety — if termination is announced, it is
  correct. These correspond to 'safe nodes' reaching terminal states."

- **ScienceDirect — Deadlock detection in complex software systems using
  Bayesian optimization:**
  https://www.sciencedirect.com/science/article/abs/pii/S0164121217301061
  "Deadlock detection uses graph transformations to model system state. The
  proposed approaches can analyze reachability, safety and liveness properties —
  the same triad as this problem's safe/unsafe/terminal classification."

---

## 3. Job Scheduler — Guaranteed-Terminating Tasks

In a distributed job scheduler (Apache Airflow, Luigi, Temporal), a task is
"eventually safe" if all its downstream tasks are guaranteed to terminate —
i.e., no task in its dependency chain can get stuck in an infinite retry loop
or circular dependency.

```
Safe task:   all paths through its task graph eventually complete
Unsafe task: some path leads to a retrying loop or circular dependency
Terminal task: no downstream tasks (leaf node in the task DAG)
```

Identifying safe tasks tells the scheduler which tasks can be submitted
safely (they won't hang the system indefinitely).

---

## Summary

| Domain | Node = | Edge = | Terminal = | Safe node = | Cycle = |
|--------|--------|--------|-----------|------------|---------|
| LeetCode #802 | Graph node | Directed edge | No outgoing edges | All paths terminate | Unsafe |
| OS deadlock | Process | Waits-for | Holds all needed resources | Completes without deadlock | Deadlock |
| Model checking | System state | Transition | Quiescent state | Reaches terminal | Liveness violation |
| Job scheduler | Task | Dependency | Leaf task (no deps) | All downstream tasks complete | Infinite retry loop |

---

## Further Reading

- Deadlock detection (GeeksforGeeks): https://www.geeksforgeeks.org/operating-systems/deadlock-detection-recovery/
- Wait-for Graph explained: https://www.groundcover.com/learn/performance/deadlock-detection
- IMDS model checking (Academia.edu): https://www.academia.edu/30571754/Deadlock_and_Termination_Detection_using_IMDS_Formalism_and_Model_Checking_Version_2
- Termination detection (arXiv): https://arxiv.org/pdf/2602.00272
- Bayesian deadlock detection (ScienceDirect): https://www.sciencedirect.com/science/article/abs/pii/S0164121217301061
- Course Schedule I (#207): see `graphs/course-schedule/`
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
