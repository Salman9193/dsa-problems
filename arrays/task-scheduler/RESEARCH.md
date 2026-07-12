# Task Scheduler — Research & Foundations

This is a **greedy scheduling** problem: always advance the bottleneck (most frequent) task.
Greedy/list scheduling has a precise foundational literature. *Citations verified against Bell
System Technical Journal / SIAM records this session — not from memory.*

- **R. L. Graham (1966), "Bounds for certain multiprocessing anomalies,"** *Bell System
  Technical Journal* 45(9):1563–1581. The origin of **list scheduling** — the greedy rule of
  assigning the next available task to the next free slot — and of the famous
  *scheduling anomalies* (adding processors can make a schedule *longer*). The foundational
  paper of greedy scheduling analysis.
  https://onlinelibrary.wiley.com/doi/abs/10.1002/j.1538-7305.1966.tb01709.x

- **R. L. Graham (1969), "Bounds on multiprocessing timing anomalies,"** *SIAM Journal on
  Applied Mathematics* 17(2):416–429. Sharpens the worst-case bounds for list scheduling —
  the classic (2 − 1/m) approximation guarantee. https://doi.org/10.1137/0117039

- **R. L. Graham, E. L. Lawler, J. K. Lenstra & A. H. G. Rinnooy Kan (1979), "Optimization
  and approximation in deterministic sequencing and scheduling: a survey,"** *Annals of
  Discrete Mathematics* 5:287–326. The survey that established the **α | β | γ** notation
  and mapped the complexity landscape of scheduling problems.

- **J. W. J. Williams (1964), "Algorithm 232: Heapsort,"** *Communications of the ACM*
  7(6):347–348. The **binary heap** / priority queue underlying the max-heap simulation — a
  scheduler's ready-queue.

**Why it matters here:** the heap solution is textbook **list scheduling** (Graham 1966) —
greedily dispatch the highest-priority *eligible* task, idling when none is eligible. The
closed-form `max(len, (maxFreq−1)(n+1) + countMax)` is a proof that, for this special case
(unit tasks, one machine, a uniform cooldown), the greedy schedule is not just good but
**optimal** — the bottleneck task's frames give a matching lower bound.

**Related in this repo:** [Find Median from Data Stream](#design/find-median-data-stream) and
[Merge K Sorted Lists](#linked-list/merge-k-sorted-lists) (heap mechanics),
[Course Schedule II](#graphs/course-schedule-ii) (dependency-ordered scheduling via topological
sort — Kahn 1962), and the applied version, the
[Job Scheduler LLD](https://salman9193.github.io/system-design/#lld-job-scheduler).
