# Range Sum Query - Mutable — Real-World Use Cases

Segment trees answer aggregate queries over ranges while supporting updates,
both in O(log n). This shows up wherever data changes and you still need fast
range analytics — databases, graphics, computational geometry, and finance.

---

## 1. Databases — Aggregate Indexes & Range Analytics

OLAP and time-series databases must answer "sum / min / max over this range"
on data that is constantly updated. Segment trees and their cousins (Fenwick
trees, aggregate B-tree indexes) power these range-aggregate queries without
rescanning the underlying rows.

- **PostgreSQL BRIN (Block Range Index):**
  https://www.postgresql.org/docs/current/brin-intro.html
  Stores summary aggregates (min/max) per block range so range queries skip
  irrelevant blocks — a coarse-grained, disk-oriented version of the same idea.

- **Apache Druid segment aggregation:**
  https://druid.apache.org/docs/latest/design/segments/
  Time-series segments store pre-aggregated metrics for fast range rollups.

---

## 2. Computational Geometry — Sweep-Line Algorithms

The classic **Skyline Problem** and rectangle-union-area problems are solved
with a sweep line backed by a segment tree over the coordinate axis. As the
line sweeps, the tree maintains the currently active intervals and answers
"max height" or "covered length" over a range in O(log n).

- **Segment tree (geometry) — Wikipedia:**
  https://en.wikipedia.org/wiki/Segment_tree
  "Segment trees... were designed to solve computational-geometry problems
  such as the measure problem (total length covered by a set of intervals)."

- **CP-Algorithms — Segment Tree:**
  https://cp-algorithms.com/data_structures/segment_tree.html
  Comprehensive reference covering range queries, lazy propagation, and the
  geometry applications.

---

## 3. Graphics & Games — Level-of-Detail and Collision Ranges

Interval and segment trees index spatial ranges so a renderer or physics
engine can query "which objects overlap this range" as objects move (update).
Range-max structures also drive terrain height queries and view-frustum
culling over 1D coordinate spans.

- **Interval tree — Wikipedia:**
  https://en.wikipedia.org/wiki/Interval_tree
  Describes windowing and range-overlap queries used in graphics and GIS.

---

## 4. Finance — Real-Time Range Statistics on Streaming Prices

Trading systems track running aggregates (sum, min, max, moving windows) over
price and volume series that update tick-by-tick. A segment tree lets a system
update a single time bucket and immediately re-query aggregates over any range
of buckets in logarithmic time, instead of recomputing from scratch.

- **Fenwick tree (binary indexed tree) — Wikipedia:**
  https://en.wikipedia.org/wiki/Fenwick_tree
  "A Fenwick tree... can efficiently update elements and calculate prefix sums
  in a table of numbers" — the structure used for streaming prefix aggregates.

- **CP-Algorithms — Fenwick Tree:**
  https://cp-algorithms.com/data_structures/fenwick.html

---

## Summary

| Domain | What's aggregated over a range | Structure |
|--------|-------------------------------|-----------|
| Databases | Metrics over row/time ranges | Aggregate index / BRIN / Druid segments |
| Geometry | Covered length, max height (sweep line) | Segment tree over coordinate axis |
| Graphics / games | Overlapping objects, terrain height | Interval / segment tree |
| Finance | Running sum/min/max over time buckets | Segment tree / Fenwick tree |

---

## Further Reading

- CP-Algorithms segment tree: https://cp-algorithms.com/data_structures/segment_tree.html
- CP-Algorithms Fenwick tree: https://cp-algorithms.com/data_structures/fenwick.html
- Segment tree (geometry): https://en.wikipedia.org/wiki/Segment_tree
- PostgreSQL BRIN: https://www.postgresql.org/docs/current/brin-intro.html
- Apache Druid segments: https://druid.apache.org/docs/latest/design/segments/
