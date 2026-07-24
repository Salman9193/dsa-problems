# Klee's Algorithm — Measure of a Union

**The problem:** given `n` intervals, how much of the line do they cover *in total*, counting
overlaps once?

```
[1,4]  [2,6]  [8,10]
        ├──────────┤        ├────┤
   1    2    4     6   7    8    10
covered: [1,6] ∪ [8,10]  =  5 + 2  =  7
```

It looks trivial. It is famous because **Victor Klee asked in 1977 whether it could be done in
*less* than O(n log n)** — and the answer turned out to be **no**. This is one of the rare
everyday algorithms with a **matching lower bound**, which makes it worth knowing for what it
teaches, not just for what it computes.

---

## The 1-D Algorithm

Two equivalent formulations. Both are O(n log n), both dominated by the sort.

### Form A — Merge and sum (simplest)

```java
public long unionMeasure(int[][] intervals) {
    if (intervals.length == 0) return 0;
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

    long total = 0;
    int curStart = intervals[0][0], curEnd = intervals[0][1];
    for (int[] iv : intervals) {
        if (iv[0] <= curEnd) {                 // overlaps: extend
            curEnd = Math.max(curEnd, iv[1]);
        } else {                               // disjoint: bank it, start fresh
            total += curEnd - curStart;
            curStart = iv[0];
            curEnd = iv[1];
        }
    }
    return total + (curEnd - curStart);
}
```

This is [Merge Intervals #56](#arrays/merge-intervals) with a running sum instead of a list.

### Form B — Event sweep (generalises)

Turn each interval into two **events**, sort them, and sweep left to right holding a **coverage
counter**:

```java
public long unionMeasure(int[][] intervals) {
    int[][] events = new int[intervals.length * 2][2];
    int k = 0;
    for (int[] iv : intervals) {
        events[k++] = new int[]{iv[0], +1};    // an interval opens
        events[k++] = new int[]{iv[1], -1};    // an interval closes
    }
    Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));

    long total = 0;
    int cover = 0, prev = 0;
    for (int[] e : events) {
        if (cover > 0) total += e[0] - prev;    // this stretch was covered
        cover += e[1];
        prev = e[0];
    }
    return total;
}
```

**Prefer Form B.** It costs the same and extends to everything else: counting *maximum* overlap
([Meeting Rooms II #253](#arrays/meeting-rooms-ii)), finding regions covered **at least k** times,
finding gaps, and — crucially — **the 2-D version below**. Merge-and-sum is a dead end; the sweep
is a pattern.

> **The mental model:** *stop caring about intervals, start caring about the transitions.* An
> interval is not an object; it's a `+1` and a `-1`. Almost every interval problem gets simpler
> under that reframe.

### Sorting subtlety — ties

When a close and an open land on the same coordinate, order matters and **which order depends on
the question**:

| Question | Tie order | Why |
|----------|-----------|-----|
| **Union measure** (`[1,3] ∪ [3,5]` = 4) | **open before close** | touching intervals form one covered run |
| **Max concurrent** (rooms for `[1,3]`,`[3,5]` = 1) | **close before open** | one ends exactly as the other starts |

**Same events, opposite tie-break, different answer.** Getting this backwards is the classic bug —
and interviewers ask about it precisely because it reveals whether you've actually implemented one.

---

## Why O(n log n) Is Optimal

The reason this problem has a name.

**Klee (1977)** posed it as a question. **Fredman & Weide (1978)** answered it: **Ω(n log n)** in
the algebraic decision-tree model. The argument is a reduction — **element distinctness** reduces
to union measure:

```
given x₁ … xₙ, build the intervals [xᵢ, xᵢ + 1]
   all xᵢ distinct   ⇒  no two intervals coincide  ⇒  measure is "large"
   any duplicate     ⇒  two intervals coincide     ⇒  measure is strictly smaller
```

So a faster union-measure algorithm would give a faster element-distinctness algorithm — and
element distinctness is **known** to require Ω(n log n) in that model. **The sort isn't laziness;
it's the price of the problem.**

> **Why this matters beyond the problem:** it's a clean, small example of proving you *can't* do
> better. Most "can I beat O(n log n)?" questions have no such answer — here there is one, and the
> technique (reduce a known-hard problem to yours) is the standard move.

---

## 2-D: Klee's Measure Problem

Now the rectangles. **Given `n` axis-aligned rectangles, what is the area of their union?**
([Rectangle Area II #850](https://leetcode.com/problems/rectangle-area-ii/))

**The insight: sweep a vertical line, and at every moment the answer is a 1-D Klee measure.**

```
      │ sweep →
  ┌───┼────┐
  │   │    │      At each x-slab between consecutive x-coordinates,
  ├───┼─┐  │      the set of active rectangles is FIXED.
  │   │ │  │      Covered height = 1-D union measure of their y-intervals.
  └───┼─┘  │      Area += covered_height × slab_width
      │
```

```java
// events: (x, y1, y2, +1 open / -1 close), sorted by x
long area = 0;
int prevX = events[0].x;
for (Event e : events) {
    area += coveredY() * (e.x - prevX);   // the 1-D measure, right now
    apply(e);                             // activate / deactivate its y-interval
    prevX = e.x;
}
```

The only question is how to maintain `coveredY()` under insertions and deletions.

| Approach | `coveredY()` | Total |
|----------|--------------|-------|
| Recompute the 1-D union each slab | O(n log n) | **O(n² log n)** |
| **Segment tree over y, storing (count, covered length)** | **O(log n)** | **O(n log n)** |

### The segment tree that makes it O(n log n)

Compress the y-coordinates, then build a tree over the y-*intervals* where each node keeps:
- `count` — how many active rectangles cover this node's whole range
- `covered` — how much of this node's range is covered

```java
void pull(int node, int lo, int hi) {
    if (count[node] > 0)      covered[node] = ys[hi] - ys[lo];       // fully covered
    else if (hi - lo == 1)    covered[node] = 0;                     // a leaf, uncovered
    else                      covered[node] = covered[left] + covered[right];
}
```

**The trick worth remembering:** this tree **never needs lazy propagation**, because a `-1` always
exactly cancels an earlier `+1` on the *same* range. Counts are only ever decremented by the
interval that incremented them, so a node's `count` is always meaningful without pushing anything
down. That's why the "count + covered" segment tree is simpler than a general lazy tree, and it's a
neat special case to have seen.

> **The historical punchline:** the segment tree was invented **for this problem**. Bentley's 1977
> manuscript is literally titled *"Solutions to Klee's rectangle problems."* The structure in
> [Segment Tree](#guides/SEGMENT_TREE) — and the one used in
> [LIS II #2407](#dynamic-programming/longest-increasing-subsequence-ii) — traces back to Klee's
> question.

---

## Higher Dimensions

The problem keeps its name and gets much harder:

| Dimensions | Best known | Source |
|------------|-----------|--------|
| 1 | **Θ(n log n)** — tight | Klee 1977 / Fredman–Weide 1978 |
| 2 | **O(n log n)** — tight | Bentley 1977 |
| d | O(n^(d−1) log n) | van Leeuwen & Wood 1981 |
| d | **O(n^(d/2) log n)** | Overmars & Yap 1991 |

Whether `n^(d/2)` is optimal is **still open**, and it's an active area — recent work gives
conditional lower bounds under fine-grained complexity assumptions. A problem that looks like a
warm-up in 1-D is a research topic in 4-D.

---

## The Problem Family

| Problem | The twist | Tool |
|---------|-----------|------|
| [Merge Intervals #56](#arrays/merge-intervals) | output the union, not its measure | sort + merge |
| [Meeting Rooms II #253](#arrays/meeting-rooms-ii) | **max** coverage, not total | sweep, track peak `cover` |
| Non-Overlapping Intervals #435 | fewest removals to disjoin | greedy by **end** time |
| Employee Free Time #759 | the **gaps** (complement of the union) | sweep, emit `cover == 0` runs |
| The Skyline Problem #218 | the *shape* of the max, not the area | sweep + max-heap / multiset |
| **Rectangle Area II #850** | **2-D union area** | **sweep + count/covered segment tree** |

**They are all one algorithm with a different accumulator** — exactly the pattern that recurs across
this repo: *sort into a canonical order, sweep, and change only what you accumulate.*

| Accumulate | Get |
|------------|-----|
| length while `cover > 0` | **union measure** (Klee) |
| `max(cover)` | maximum concurrency (rooms) |
| length while `cover == 0` | free time / gaps |
| length while `cover >= k` | regions covered ≥ k times |
| the value of `cover` at each x | the skyline |

---

## Real-World Use Cases

The unifying situation: **you have overlapping records of something, and the sum double-counts.**
Whenever *total ≠ sum of parts*, this is the algorithm.

### 1. Incident Duration & SLA Downtime

Three alerts fire over overlapping windows — `10:00–10:30`, `10:15–10:45`, `10:40–11:00`. Summing
gives **85 minutes**; the actual outage is **60**.

Every uptime figure and SLA credit calculation is a union measure. Getting it wrong means either
over-refunding customers or quietly breaching a contract you believed you'd met. The **gaps**
variant (`cover == 0`) gives the complementary *uptime* directly.

### 2. Genomic Coverage — the heavy industrial user

Millions of sequencing reads align to a reference genome as intervals. Two standard questions map
exactly onto two accumulators:

| Genomics term | Question | Sweep variant |
|---------------|----------|---------------|
| **Breadth** of coverage | what fraction of the genome is covered *at all*? | length while `cover > 0` — **the union measure** |
| **Depth** of coverage | which regions are covered **at least k** times? | length while `cover >= k` |

This is what `bedtools merge`, `genomecov`, and `coverage` compute — and BEDTools explicitly uses a
**sweep over genome-sorted intervals** as its fast path, for the same reason this guide prefers the
event form: sorted input makes overlap detection streaming and memory-light.
*(Quinlan & Hall, 2010, "BEDTools: a flexible suite of utilities for comparing genomic features,"*
*Bioinformatics* 26(6):841–842.)

### 3. VLSI Design-Rule Checking — the original motivation

Bentley's rectangle problems came from **chip layout**: total area covered by metal on a layer,
overlap between layers, spacing violations. That's *why* the 2-D case was solved early and well —
and why the segment tree exists at all.

### 4. Media Watch Time

Watch `0–50%`, then rewatch `30–60%` → the viewer saw **60%**, not 80%. Any "percent of content
consumed" metric that sums segments is inflated, and inflated engagement metrics drive bad
decisions. (The HTML5 `TimeRanges` API for buffered video is itself a union-of-intervals structure.)

### 5. Metered Billing & Capacity

- **Bill the union, not the sum** — overlapping sessions, connections, or instance-hours.
- The *inverse* question — **peak concurrency** for license seats, connection pools, or room
  counts — is the same sweep tracking `max(cover)`.

Both numbers come from one pass over the same events.

### 6. Code Coverage

Which lines are executed by *at least one* test is a union over per-test line ranges; the k-variant
tells you which lines are covered redundantly (and therefore where tests may be duplicative).

### 7. CIDR / Firewall Rule Analysis

IP ranges are intervals over a 32-bit integer space. *"How many addresses do these rules actually
cover?"* is a union measure; *"is this rule fully shadowed by others?"* is a containment check over
the same sorted events — a standard audit for dead firewall rules.

### 8. Calendar Free/Busy

The **complement** of the union: sweep and emit stretches where `cover == 0`. Scheduling across
several people intersects those free sets.

---

### The Point of the Sweep Form

All of the above are one algorithm with the accumulator swapped:

| Question | Accumulate | Used for |
|----------|-----------|----------|
| Total covered | length while `cover > 0` | downtime, watch time, billing, coverage breadth |
| Peak concurrency | `max(cover)` | license seats, rooms, connection pools |
| Gaps | length while `cover == 0` | free/busy, uptime, uncovered regions |
| Redundancy | length while `cover >= 2` | duplicate monitors, over-provisioning |
| Depth ≥ k | length while `cover >= k` | sequencing depth, quorum coverage |

**Same events, same sort, one line different.** That's the whole argument for learning the event
sweep rather than merge-and-sum.

### A Caution About Higher Dimensions

1-D and 2-D are `O(n log n)`. Beyond that it gets genuinely expensive — `O(n^(d/2) log n)` — and
computing exact union **volume** in high dimensions is **#P-hard** even for axis-parallel boxes. The
usual case where people hit this is the **hypervolume indicator** in multi-objective optimisation.

**The practical answer there is Monte Carlo estimation, not exact computation.** Recognising when to
stop reaching for the exact algorithm is part of knowing it.

---

## Streaming & Online Variants

The classic sweep is a **blocking operator**: it sorts, so it can't emit anything until the input
ends. That's fine for a batch job and useless for a live feed — and most of the use cases above
(downtime, coverage, billing, watch time) arrive as *streams*.

Whether it streams depends on exactly two questions: **is the input sorted**, and **do you need
exact?**

### Case 1 — Sorted by start: O(1) memory, exact, incremental ✅

This is the case that matters in practice, because **event logs, metrics, and CDC streams are
usually already time-ordered.** Once intervals arrive in start order, a run can only ever be
*extended* — so the moment one starts beyond `curEnd`, the previous run is **final** and can be
emitted immediately:

```java
class StreamingUnion {
    private long total = 0;
    private Integer curStart = null, curEnd = null;

    void push(int a, int b) {
        if (curStart == null) { curStart = a; curEnd = b; return; }
        if (a <= curEnd) {
            curEnd = Math.max(curEnd, b);        // extend the open run
        } else {
            total += curEnd - curStart;          // gap ⇒ previous run is FINAL — emit it
            curStart = a; curEnd = b;
        }
    }
    long value() {                                // running answer at any moment
        return total + (curStart == null ? 0 : curEnd - curStart);
    }
}
```

**Constant state, one pass, incremental output**, and a valid running total at every point:

```
after (1,4):  union = 3
after (2,6):  union = 5
after (8,10): union = 7      ← run [1,6] now finalized and emitted
after (9,12): union = 9
```

### Case 2 — Out-of-order arrivals: the failure mode is losing a closed run ⚠️

The practical trap. A late event can **bridge a gap you already closed**:

```
stream: (1,2), (10,11)      → union = 2, and run [1,2] has already been emitted
then LATE (3,9) arrives     → streaming says 2, truth is 8
```

This is precisely why stream processors use **watermarks**: buffer within a bounded lateness
window, only finalize a run once the watermark passes its end, and send stragglers to a side
output. Bounded out-of-orderness (Flink/Beam style) converts "sorted stream" from an *assumption*
into an *engineered guarantee*, and the buffer is **O(events within the lateness window)** — not
O(n).

### Case 3 — Unsorted and exact: Ω(n) is unavoidable ❌

The obstruction isn't the algorithm, it's **the answer**. Feed it `n` pairwise-disjoint intervals
and the union *is* `n` disjoint pieces, so anything answering exactly must carry Ω(n) state.

**You cannot sketch your way around an answer whose own description is linear.** Worth internalising
as a general principle for streaming problems: check the output size before hunting for a sublinear
algorithm.

### Case 4 — Unsorted and approximate: polylog space ✅

Here the connection is elegant: **streaming union measure is the continuous generalization of
counting distinct elements.** F₀ (distinct elements) is the special case where every "set" is a
single point; Klee's measure is the case where every set is a **box**. Same problem, richer sets —
so the whole HyperLogLog/F₀ sketching toolkit applies in spirit.

It's an active research line with strong bounds — **(ε,δ)-estimation in polylog(|Ω|) space**:

| Work | Space |
|------|-------|
| Meel, Chakraborty & Vinodchandran (PODS '21, '22) | `O(log³|Ω| / ε² · log 1/δ)` |
| Nandi, Vinodchandran, Ghosh, Meel, Pal & Chakraborty (APPROX '24) | `Õ(log²|Ω| / ε² · log 1/δ)` |

The framework is **Delphic families** — sets supporting efficient membership, cardinality, and
sampling queries. The applications those authors call out include **test coverage and hypervolume
estimation**, two of the use cases listed above.

### The Decision Table

| Situation | Result |
|-----------|--------|
| Sorted by start | **O(1) memory, exact, incremental** |
| Bounded lateness | **O(lateness window), exact** — watermarks |
| Unsorted, exact | **Ω(n) — unavoidable** |
| Unsorted, approximate | **polylog space**, F₀-style sketching |

> **The reusable lesson:** "can this be streamed?" is rarely a yes/no about the algorithm. It's a
> question about **input order** and **required exactness** — and relaxing either one changes the
> answer completely.

---

## Interview Notes

- **Say "sweep line," then say what you accumulate.** That framing answers half the follow-ups
  before they're asked.
- **Volunteer the tie-break rule.** Stating *"opens before closes for union measure, closes before
  opens for max-concurrency"* is a strong signal — it's the detail only implementers know.
- **Coordinate-compress** whenever values are large or sparse; the tree should be sized by the
  number of distinct coordinates, not by the coordinate range (see
  [LIS II](#dynamic-programming/longest-increasing-subsequence-ii) for the same rule).
- **Mention the lower bound if asked "can we do better?"** — "No: Fredman–Weide proved Ω(n log n)
  by reduction from element distinctness." That's a genuinely rare thing to be able to say.
- **If asked "what if the data streams?"** — the strong answer is a question back: *is it sorted,
  and do you need exact?* Sorted by start ⇒ O(1) memory exactly; unsorted + exact ⇒ Ω(n) because the
  union itself can be n disjoint pieces; unsorted + approximate ⇒ polylog space, since this is the
  continuous generalization of distinct-elements counting.
- **Watch for overflow:** union measures over large coordinate ranges need `long`, and #850 wants
  the answer modulo 10⁹+7 (compute exactly, then reduce).

---

## Research & Foundations

*Citations verified against American Mathematical Monthly / Communications of the ACM / SIAM /
J. Algorithms records — not from memory.*

- **V. Klee (1977), "Can the measure of ⋃₁ⁿ[aᵢ,bᵢ] be computed in less than O(n log n) steps?"**
  *The American Mathematical Monthly* 84(4):284–285.
  DOI: [10.1080/00029890.1977.11994336](https://doi.org/10.1080/00029890.1977.11994336). The paper
  that posed the question and gave the problem its name.

- **M. L. Fredman & B. W. Weide (1978), "On the complexity of computing the measure of ⋃[aᵢ,bᵢ],"**
  *Communications of the ACM* 21(7):540–544.
  DOI: [10.1145/359545.359553](https://doi.org/10.1145/359545.359553). **Answers Klee's question:
  Ω(n log n)**, via reduction from element distinctness — so the sort-based algorithm is optimal.

- **J. L. Bentley (1977), "Solutions to Klee's rectangle problems."** Unpublished manuscript /
  technical report, Department of Computer Science, Carnegie-Mellon University. The **2-D O(n log n)
  algorithm**, and the origin of the **segment tree**.

- **J. van Leeuwen & D. Wood (1981), "The measure problem for rectangular ranges in d-space,"**
  *Journal of Algorithms* 2(3):282–300. Generalises to d dimensions in O(n^(d−1) log n).

- **M. H. Overmars & C.-K. Yap (1991), "New upper bounds in Klee's measure problem,"** *SIAM Journal
  on Computing* 20(6):1034–1045. DOI: [10.1137/0220065](https://doi.org/10.1137/0220065). The
  long-standing **O(n^(d/2) log n)** bound.

### Streaming

- **A. Pavan & S. Tirthapura (2007), "Range-Efficient Counting of Distinct Elements in a Massive
  Data Stream,"** *SIAM Journal on Computing* 37(2):359–379.
  DOI: [10.1137/050643672](https://doi.org/10.1137/050643672). The 1-D streaming case — counting
  distinct elements covered by a stream of *ranges* rather than points.

- **K. S. Meel, S. Chakraborty & N. V. Vinodchandran (2021), "Estimating the Size of Union of Sets
  in Streaming Models,"** *PODS '21*, pp. 126–137.
  DOI: [10.1145/3452021.3458333](https://doi.org/10.1145/3452021.3458333), and **PODS '22**,
  "Estimation of the Size of Union of Delphic Sets." (ε,δ)-estimation over **Delphic families** in
  `O(log³|Ω| / ε² · log 1/δ)` space.

- **M. Nandi, N. V. Vinodchandran, A. Ghosh, K. S. Meel, S. Pal & S. Chakraborty (2024), "Improved
  Streaming Algorithm for the Klee's Measure Problem and Generalizations,"** *APPROX/RANDOM 2024*,
  LIPIcs vol. 317, 26:1–26:18.
  DOI: [10.4230/LIPIcs.APPROX/RANDOM.2024.26](https://doi.org/10.4230/LIPIcs.APPROX/RANDOM.2024.26).
  Improves the space bound to `Õ(log²|Ω| / ε² · log 1/δ)`, and states the framing used above: the
  streaming union-measure problem **naturally generalizes F₀ (distinct elements) estimation**, where
  each set is a single point.

**Related in this repo:** [Intervals](#guides/INTERVALS) (the merge/insert/overlap patterns),
[Segment Tree](#guides/SEGMENT_TREE) (the structure Bentley invented here),
[Merge Intervals #56](#arrays/merge-intervals),
[Meeting Rooms II #253](#arrays/meeting-rooms-ii).
