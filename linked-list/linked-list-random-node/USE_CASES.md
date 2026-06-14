# Linked List Random Node — Real-World Use Cases

Reservoir sampling — selecting a uniform random sample from a stream of
unknown size in a single pass — is one of the most important algorithms
in data engineering, used wherever data is too large to store entirely.

---

## 1. Vitter's Algorithm R — The Foundational Paper (1985)

Reservoir sampling was formalised by Jeffrey Vitter in 1985. Algorithm R
is the simple version (equivalent to this LeetCode problem for k=1), and
Algorithm Z is the optimised version with O(n(1 + log(N/n))) time.

> "We introduce fast algorithms for selecting a random sample of n records
> without replacement from a pool of N records, where the value of N is
> unknown beforehand. Algorithm Z does the sampling in one pass using
> constant space." — Vitter, 1985

### Why it matters

Before reservoir sampling, random sampling required knowing N in advance
or storing all records in memory — neither feasible for large streams.
Reservoir sampling made random sampling possible on:
- Tape drives (1985 — the original use case)
- Database table scans without full materialization
- Network packet streams at line rate
- Internet-scale log files

### References

- **Original paper:** Vitter, J.S. — *Random sampling with a reservoir*,
  ACM Transactions on Mathematical Software, 11(1):37–57, 1985.
  https://dl.acm.org/doi/10.1145/3147.3165
  **PDF:** https://www.cs.umd.edu/~samir/498/vitter.pdf

- **Wikipedia — Reservoir Sampling:**
  https://en.wikipedia.org/wiki/Reservoir_sampling
  "Reservoir sampling is a family of randomized algorithms for choosing
  a simple random sample of k items from a population of unknown size n
  in a single pass. The population is revealed to the algorithm over time
  and the algorithm cannot look back at previous items."

---

## 2. Streaming Anomaly Detection — Reference Window Sampling

Anomaly detection systems (intrusion detection, fraud detection,
observability platforms) compare a stream of incoming events against a
reference sample of "normal" past events. Since the stream is infinite,
you can't store everything — reservoir sampling maintains a random
reference window of fixed size w.

```
Event stream:  e1, e2, e3, ... (unbounded)
Reference reservoir: size=1000

For each new event ei:
  j = rand.nextInt(i)
  if j < 1000: reservoir[j] = ei  ← replace with prob 1000/i
```

At any time, the reservoir is a uniform random sample of all events
seen so far — the reference distribution for anomaly scoring.

### Reference

- **Paper:** *No Free Lunch But A Cheaper Supper: A General Framework
  for Streaming Anomaly Detection*, arXiv:1909.06927.
  https://arxiv.org/pdf/1909.06927
  "The reservoir sampling algorithm (Vitter, 1985) is used in streaming
  anomaly detection as the 'uniform reservoir' windowing technique —
  maintaining a random reference sample of past observations of
  indeterminate or unbounded length in a single pass."

---

## 3. Distributed Parallel Reservoir Sampling

In distributed systems (Apache Spark, Flink, Kafka Streams), data arrives
at multiple nodes simultaneously. Each node runs reservoir sampling on
its local stream, then local reservoirs are merged to produce a global
uniform sample — without any node knowing the global stream size.

```
Node 1: [e1, e4, e7, ...] → local reservoir R1
Node 2: [e2, e5, e8, ...] → local reservoir R2
Node 3: [e3, e6, e9, ...] → local reservoir R3

Merge: combine R1, R2, R3 using weighted reservoir sampling
       → global uniform sample from the full stream
```

### Reference

- **Paper:** *Efficient Random Sampling — Parallel, Vectorized,
  Cache-Efficient, and Online*, arXiv:1610.05141.
  https://arxiv.org/pdf/1610.05141
  "Reservoir sampling is useful for maintaining a sample of size n over
  a data stream. In distributed settings, elements arrive at each
  processing element independently — each PE runs classical reservoir
  sampling locally, and a global sample is drawn by combining local
  reservoirs."

---

## 4. Computer Graphics — Weighted Reservoir Sampling for Rendering

Modern real-time rendering (games, VR, film) uses reservoir sampling to
select light sources for ray tracing — each candidate light is weighted
by its contribution, and reservoir sampling picks among millions of
lights in a single pass without storing all of them.

This is the k=1 case with non-uniform probabilities (weighted reservoir
sampling, WRS) — the same algorithm as this problem, generalised.

### Reference

- **Paper:** *Enhancing Foveated Rendering with Weighted Reservoir Sampling*,
  arXiv:2510.03964.
  https://arxiv.org/pdf/2510.03964
  "Reservoir sampling is a technique to efficiently sample n items from a
  population of potentially unknown size N. For the remainder of this paper,
  we focus on cases where the reservoir operates under tight memory constraints,
  maintaining a reservoir containing only a single sample (n=1) and the total
  length of the data sequence N is unknown — the exact setting of LeetCode #382."

---

## Summary

| Domain | Stream | k= | Algorithm | Reference |
|--------|--------|-----|-----------|-----------|
| Foundational (Vitter 1985) | Any sequential stream | any k | Algorithm R / Z | ACM TOMS 1985 |
| Streaming anomaly detection | Infinite event stream | w (window) | Uniform reservoir | arXiv:1909.06927 |
| Distributed sampling (Spark/Flink) | Parallel streams | n (global) | Parallel Algorithm R | arXiv:1610.05141 |
| Real-time rendering (WRS) | Light source candidates | 1 | Weighted reservoir | arXiv:2510.03964 |

---

## Further Reading

- Vitter 1985 (original): https://dl.acm.org/doi/10.1145/3147.3165
- PDF of Vitter 1985: https://www.cs.umd.edu/~samir/498/vitter.pdf
- Reservoir sampling (Wikipedia): https://en.wikipedia.org/wiki/Reservoir_sampling
- Streaming anomaly detection: https://arxiv.org/pdf/1909.06927
- Parallel reservoir sampling: https://arxiv.org/pdf/1610.05141
- Weighted reservoir for rendering: https://arxiv.org/pdf/2510.03964
