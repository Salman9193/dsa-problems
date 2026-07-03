# Kth Largest Element — Real-World Use Cases

## 1. Streaming Analytics — Top-K Leaderboards

In streaming systems (Kafka, Flink), maintaining the top-k items from a
continuous data stream uses a min-heap of size k. Each incoming element
is compared against the heap root; if larger, it replaces the root. Used
in real-time leaderboards (gaming, trading) and anomaly detection
(top-k slowest API calls).

## 2. Database ORDER BY LIMIT k

SQL `ORDER BY value DESC LIMIT k` is implemented internally using a min-heap
of size k — never sort the full result set. PostgreSQL and MySQL use exactly
this heap-based approach for large result sets with small LIMIT values.

## 3. Recommendation Systems — Top-K Item Selection

Finding the k highest-scoring items for a recommendation (out of millions
of candidates) uses quickselect or a min-heap. Netflix's recommendation
pipeline selects top-k candidates per user before ranking — O(n log k) heap
is faster than full sort when k << n.
