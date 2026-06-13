# Online Election — Real-World Use Cases

The precompute-leaders + binary-search-rightmost-timestamp pattern is the
core algorithm behind two of the most widely deployed database systems:
Prometheus time-series monitoring and PostgreSQL MVCC snapshot isolation.

---

## 1. Prometheus — Point-in-Time Instant Vector Queries

Prometheus stores time series as sorted arrays of (timestamp, value) pairs.
When you query a metric at time `t` (an "instant vector" query), Prometheus
must return the latest sample at or before `t` — exactly the rightmost-≤-t
binary search in Online Election.

```
Prometheus TSDB sample array:
  timestamps = [1000, 1015, 1030, 1045, 1060]
  values     = [42.1, 43.0, 41.5, 44.2, 43.8]

Query at t=1038:
  Binary search → rightmost timestamp <= 1038 → index 2 (t=1030)
  Return value[2] = 41.5
```

This maps exactly to:
- `times[]` in Online Election = sample timestamps in Prometheus TSDB
- `leader[]` = sampled metric values
- `q(t)` = PromQL instant vector selector at evaluation timestamp `t`

### How Prometheus implements it

Prometheus stores chunks of samples in its TSDB (Time Series Database).
Each chunk's head block maintains a sorted index of timestamps. Instant
vector selectors binary search this index for the latest sample at or
before the evaluation timestamp. The 5-minute lookback window is the
maximum gap allowed before a series is considered stale.

### References

- **Prometheus querying basics:**
  https://prometheus.io/docs/prometheus/latest/querying/basics/
  "The value returned will be that of the most recent sample at or before
  the query's evaluation timestamp."

- **PromLabs — Selecting Data in PromQL:**
  https://promlabs.com/blog/2020/07/02/selecting-data-in-promql/
  "An instant vector selector selects the latest sample value at every
  evaluation resolution step. Samples in the Prometheus TSDB are not
  aligned on a time grid and can have arbitrary timestamps — the selector
  looks back a maximum of 5 minutes to find the most recent sample before
  the evaluation timestamp."

- **Grafana — Inside PromQL:**
  https://grafana.com/blog/2024/10/08/inside-promql-a-closer-look-at-the-mechanics-of-a-prometheus-query/

---

## 2. Database MVCC — Snapshot Isolation (PostgreSQL)

Multi-Version Concurrency Control (MVCC) stores multiple versions of each
database row, each tagged with the transaction ID (xmin) that created it.
When a transaction reads a row "as of" its start time, PostgreSQL finds
the latest row version committed before that transaction's start — the
rightmost-≤-t binary search on a sorted version list.

```
Row versions for "price" column:
  xmin (commit txn) = [100, 150, 200, 250, 300]
  values            = [50,  55,  60,  58,  62 ]

Transaction T starts at txn_id = 220:
  Binary search → rightmost xmin <= 220 → index 2 (xmin=200)
  T sees value[2] = 60   (the version committed before T started)
```

This maps directly to Online Election:
- `times[]` = row version commit timestamps (xmin)
- `leader[]` = row values at each version
- `q(t)` = "what value does transaction T see for this row?"

### How PostgreSQL implements it

Each row tuple stores `xmin` (transaction that created it) and `xmax`
(transaction that deleted/updated it). A row is visible to transaction T
if `xmin <= T.start_txn` and `xmax > T.start_txn` — a range check on
sorted transaction IDs, implemented as a binary search on the version chain.

### References

- **PostgreSQL MVCC documentation:**
  https://www.postgresql.org/docs/current/mvcc-intro.html
  "Each SQL statement sees a snapshot of data as it was some time ago,
  regardless of the current state of the underlying data. PostgreSQL
  maintains this guarantee using a multiversion model — readers never
  block writers, writers never block readers."

- **CMU / Andy Pavlo — The Part of PostgreSQL We Hate the Most:**
  https://www.cs.cmu.edu/~pavlo/blog/2023/04/the-part-of-postgresql-we-hate-the-most.html
  Deep dive into PostgreSQL's MVCC implementation and its tradeoffs vs
  Oracle/MySQL approaches.

- **MVCC deep dive:**
  https://celerdata.com/glossary/multiversion-concurrency-control
  "When a transaction updates a row, the database does not overwrite the
  existing row — it creates a new version. The old version is retained.
  Each version stores xmin/xmax (created_by_txn / deleted_by_txn) to
  determine visibility for any given transaction start timestamp."

---

## The Unified Algorithm

All three systems — Online Election, Prometheus, and PostgreSQL MVCC —
solve the same problem:

```
Given: sorted timestamps T[], values V[]
Query: what is V[i] for the largest i where T[i] <= t?

Precompute: leaders/values array in O(n)
Answer:     binary search for rightmost T[i] <= t in O(log n)
```

| System | T[] | V[] | Query |
|--------|-----|-----|-------|
| Online Election | vote timestamps | current leader | who's leading at t? |
| Prometheus TSDB | sample timestamps | metric values | latest value at t? |
| PostgreSQL MVCC | row version xmin | row data | which version visible at t? |

---

## Further Reading

- Prometheus querying basics: https://prometheus.io/docs/prometheus/latest/querying/basics/
- PromLabs instant vector selector: https://promlabs.com/blog/2020/07/02/selecting-data-in-promql/
- PostgreSQL MVCC: https://www.postgresql.org/docs/current/mvcc-intro.html
- CMU Pavlo MVCC analysis: https://www.cs.cmu.edu/~pavlo/blog/2023/04/the-part-of-postgresql-we-hate-the-most.html
