# Task Scheduler — Real-World Use Cases

The problem is a miniature of what every job scheduler, OS scheduler, and rate limiter does:
pick the next task greedily under a **cooldown/rate constraint**, and pack the rest into the
gaps.

---

## 1. Job Schedulers & Rate-Limited Task Execution

The cooldown `n` is exactly a **per-task-type rate limit**: "don't run the same job kind more
often than every n intervals." Real schedulers enforce this to avoid hammering a downstream
resource (the same API, the same shard, the same disk). The greedy rule — always run the most
backlogged eligible task — is what a **priority ready-queue** does.

> Implemented in this repo as an LLD:
> [Job Scheduler](https://salman9193.github.io/system-design/#lld-job-scheduler) — its
> ready-queue is this same max-heap greedy, and its cooldown/backoff rule is this problem.

---

## 2. OS / CPU Scheduling

Choosing which process runs next, with idle time when nothing is eligible, is the OS
scheduler's core loop. Real policies (priority queues, multilevel feedback queues) generalise
the "run the highest-priority eligible task, otherwise idle" rule this problem uses — and
"idle" here is literally the CPU idling.

---

## 3. Throttling, Backoff & Cooldowns

Anywhere an action must wait a cooldown before repeating: API rate limits, retry backoff,
notification throttling ("don't alert on the same thing within N minutes"), and cache refresh
intervals. The scheduling question — *what's the minimum time to complete a batch of such
actions?* — is precisely this problem.

---

## The Unifying Idea

```
most frequent task = the bottleneck  →  it defines the skeleton of frames
cooldown n         →  each frame has width n + 1
all other tasks    →  fill the idle gaps inside those frames
answer = max(total tasks, (maxFreq − 1)(n + 1) + countOfMaxFreq)
```

| Domain | The "task" | The cooldown |
|--------|-----------|--------------|
| Job scheduler | A job of some type | Per-type rate limit |
| OS scheduler | A process/thread | Time-slice / eligibility |
| API client | A request to an endpoint | Rate limit / backoff |

---

## Further Reading

- Scheduling (computing): https://en.wikipedia.org/wiki/Scheduling_(computing)
- Related in this repo: [Course Schedule II](#graphs/course-schedule-ii) (dependency ordering
  via topological sort) and [Find Median from Data Stream](#design/find-median-data-stream)
  (heap mechanics).
