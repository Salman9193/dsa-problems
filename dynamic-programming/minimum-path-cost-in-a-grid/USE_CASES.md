# Minimum Path Cost in a Grid — Real-World Use Cases

A **layered/staged optimisation**: move through a sequence of stages, at each stage pick one option,
pay a cost *for the option* plus a **transition cost that depends on where you came from**. That
"transition cost depends on the previous choice" is the defining feature — and it's everywhere in
staged systems.

---

## 1. Multi-Stage Pipelines & Assembly Lines

Each row is a **stage**; each column is a **machine/option** at that stage. A part has a processing
cost at each machine, plus a **changeover/transfer cost** that depends on which machine it came
from. Minimising total cost across the line is exactly this DP. The value-indexed `moveCost`
mirrors real setups where transfer cost depends on the *source* station's configuration.

---

## 2. Network Routing Through Tiers

A layered network (edge → aggregation → core). Each hop lands on some node (a per-node cost), and
the link cost depends on the **originating** node. Cheapest layered route = this recurrence — and
the "any node in the next layer" move set is precisely the fully-connected-between-layers case.

---

## 3. Job / Task Scheduling Across Time Slots

Rows are time steps, columns are resources. Running a task on a resource has a cost, and
**switching resources** between steps has a migration cost depending on the previous resource.
Minimum-cost schedules over a horizon are layered DP of this shape.

---

## 4. Speech / Sequence Decoding (the Viterbi cousin)

This is structurally a **Viterbi/HMM decode**: rows = time, columns = states, cell value =
emission cost, `moveCost` = transition cost between states. Minimum path cost = most-likely state
sequence (in negative-log-cost form).

> This is the same lattice DP as the HMM step in the
> [Chinese Word Segmenter LLD](https://salman9193.github.io/system-design/#lld-chinese-word-segmenter) —
> emission + transition, relaxed layer by layer.

---

## The Unifying Idea

```
stages in sequence; one choice per stage
cost = per-choice cost + transition cost that depends on the PREVIOUS choice
dp[stage][choice] = cost(choice) + min over prev of ( dp[stage-1][prev] + trans[prev][choice] )
```

| Domain | Stage | Choice | Transition cost |
|--------|-------|--------|-----------------|
| Assembly line | production step | machine | changeover between machines |
| Layered network | tier | node | link cost from source node |
| Scheduling | time slot | resource | migration between resources |
| Viterbi decode | time step | hidden state | state-to-state transition |

---

## Further Reading

- Related: [Unique Paths #62](#dynamic-programming/unique-paths) (the O(1)-transition grid DP),
  [DP Taxonomy](#guides/DP_TAXONOMY) (Grid × Non-Constant-Transition), and the Viterbi write-up in
  [Word Break #139](#dynamic-programming/word-break).
