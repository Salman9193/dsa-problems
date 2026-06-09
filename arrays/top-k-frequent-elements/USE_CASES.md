# Top K Frequent Elements — Real-World Use Cases

Finding the most frequent elements from a large dataset appears in network
security, databases, and distributed systems at scale.

---

## 1. Network Traffic Analysis — Heavy Hitters & Count-Min Sketch

In network monitoring, the "heavy hitters" problem asks: which source IPs,
destination ports, or flows account for the most traffic? These are the
top-k most frequent elements in a stream of millions of packets per second.

The exact top-k algorithm (HashMap + bucket sort) works for datasets that
fit in memory. For streaming data at line rate (10 Gbps+), the
**Count-Min Sketch** approximates top-k using sub-linear space.

### Count-Min Sketch (Cormode & Muthukrishnan, 2005)

The Count-Min Sketch uses a 2D array of counters and d independent hash
functions. For each stream element, it increments d counters. To estimate
the frequency of an element, it returns the minimum of its d counter values
— the minimum reduces overcount from hash collisions.

```
Initialize: d × w table of counters, d hash functions h1...hd
Update(x):  for each j in [d]: C[j][hj(x)]++
Query(x):   return min over j of C[j][hj(x)]
```

- **Original paper:** Cormode & Muthukrishnan — *An Improved Data Stream
  Summary: The Count-Min Sketch and its Applications*,
  Journal of Algorithms, 55(1):58–75, 2005.
  https://dimacs.rutgers.edu/~graham/pubs/papers/cm-full.pdf

- **Applications cited in the paper:** network switch traffic monitoring,
  click-stream analysis at major websites, streaming telescope/satellite data.

- **DDoS detection:** the top-k most frequent source IPs in a time window
  identify hosts sending anomalously many packets — direct heavy-hitter query.

---

## 2. PostgreSQL Query Planner — Most Common Values (MCV)

PostgreSQL's `ANALYZE` command runs top-k frequency extraction on every
indexed column and stores the results in `pg_statistic` as `most_common_vals`
(MCV list). The query planner uses these to estimate selectivity for WHERE
clauses — determining how many rows a filter will return, which drives
join ordering and index selection decisions.

```sql
-- view the top-k most frequent values for a column
SELECT most_common_vals, most_common_freqs
FROM pg_stats
WHERE tablename = 'orders' AND attname = 'status';
```

By default PostgreSQL stores up to 100 most common values per column
(`default_statistics_target = 100`), sampling 30,000 rows per table to
compute them.

- **PostgreSQL docs — Statistics Used by the Planner:**
  https://www.postgresql.org/docs/current/planner-stats.html
  "ANALYZE stores up to 100 most common values in the most_common_vals
  array — these statistics drive the planner's row count estimates for
  WHERE clause selectivity."

- **Apache MADlib MFV Sketch** — extends PostgreSQL with a Count-Min
  Sketch-based `mfvsketch_top_histogram` UDA for approximate top-k MCV
  at scale, based directly on Cormode-Muthukrishnan:
  https://madlib.apache.org/docs/v1.0/group__grp__mfvsketch.html

---

## 3. Trending Topics — Twitter / Google Trends

Computing the top-k most frequent hashtags, search terms, or keywords
over a sliding time window is the canonical streaming top-k problem.
Twitter's trending algorithm, Google Trends, and YouTube trending all
reduce to frequency counting + top-k extraction per time window.

The exact version (HashMap + heap or bucket sort) is used for offline
batch computation. The streaming version (Count-Min Sketch +
min-heap of size k) handles real-time feeds.

- **Paper:** Cormode & Muthukrishnan — *What's hot and what's not:
  tracking most frequent items dynamically*,
  ACM Transactions on Database Systems, 30(1):249–278, 2005.
  (Directly addresses trending / hot-item tracking as an application.)

---

## Summary

| Domain | Stream elements | Top-k query | Algorithm |
|--------|----------------|-------------|-----------|
| Network monitoring (DDoS) | Packet source IPs / ports | Top-k heaviest flows | Count-Min Sketch (streaming) |
| PostgreSQL query planner | Column values per table | Top-k most common values | HashMap + heap (batch) |
| Trending topics (Twitter, Google) | Hashtags / search terms | Top-k per time window | Count-Min + heap (streaming) |

---

## Further Reading

- Count-Min Sketch original paper: https://dimacs.rutgers.edu/~graham/pubs/papers/cm-full.pdf
- PostgreSQL planner statistics: https://www.postgresql.org/docs/current/planner-stats.html
- Apache MADlib MFV: https://madlib.apache.org/docs/v1.0/group__grp__mfvsketch.html
- Stanford lecture notes on heavy hitters: https://web.stanford.edu/class/cs168/l/l2.pdf
