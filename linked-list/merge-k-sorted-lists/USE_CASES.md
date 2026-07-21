# Merge K Sorted Lists — Real-World Use Cases

K-way merge of sorted sequences is one of the most heavily used algorithms in
real systems. It is the merge step of external sorting, the read path of
LSM-tree databases, and the core of stream/log aggregation.

---

## 1. External Merge Sort — Sorting Data Too Big for RAM

When a dataset exceeds memory, databases sort it in two phases: split into
sorted "runs" that fit in RAM, then **k-way merge** those runs using a
min-heap that holds one front element per run. This is exactly the heap
approach in this problem, applied to file streams instead of linked lists.

- **PostgreSQL external sort (`tuplesort`):**
  https://www.postgresql.org/docs/current/runtime-config-resource.html#GUC-WORK-MEM
  When a sort exceeds `work_mem`, PostgreSQL spills sorted runs to disk and
  merges them — a k-way merge over on-disk runs.

- **Database System Concepts (Silberschatz) — External Sort-Merge:**
  https://www.db-book.com/
  The standard textbook treatment: create sorted runs, then merge with a
  priority queue of run heads.

---

## 2. LSM-Tree Databases — Merging SSTables on Read & Compaction

Log-structured merge-tree databases (Cassandra, RocksDB, LevelDB, HBase)
store data in multiple immutable sorted files (SSTables). A read merges the
sorted streams from several SSTables, and background **compaction** merges
many SSTables into fewer — both are k-way merges over sorted key streams.

- **RocksDB compaction:**
  https://github.com/facebook/rocksdb/wiki/Compaction
  Compaction merges multiple sorted SSTables into new sorted output files.

- **Google Bigtable paper** (Chang et al., 2006) — the origin of the SSTable
  design that LSM databases inherit:
  https://research.google/pubs/pub27898/

---

## 3. Log Aggregation & Distributed Query Merge

Distributed search and log systems keep per-shard results sorted (by time,
score, or key) and merge the k shard streams into one global order. The
coordinator uses a heap over the current head of each shard's stream — pop the
global min, emit it, pull the next from that shard.

- **Elasticsearch / Lucene segment merge:**
  https://www.elastic.co/blog/lucenes-handling-of-deleted-documents
  Lucene merges sorted postings across segments; distributed search merges
  sorted per-shard hits into a single ranked list.

- **Apache Kafka Streams / log-compacted topics** rely on merging ordered
  partitions:
  https://kafka.apache.org/documentation/#compaction

---

## 4. Version Control & Timeline Merges

Merging several already-sorted commit or event streams into a single ordered
timeline (by timestamp) is a k-way merge. Git's `git log` across multiple refs
and social-media timeline assembly both merge sorted per-source streams.

- **Git revision walking (priority queue by commit date):**
  https://git-scm.com/docs/git-log
  Git walks multiple parents/refs and emits commits in date order using a
  priority queue — a k-way merge of commit streams.

---

## Summary

| Domain | k sorted streams being merged | Merge mechanism |
|--------|------------------------------|-----------------|
| External sort | On-disk sorted runs | Min-heap of run heads |
| LSM databases | SSTable key streams | Heap merge (read + compaction) |
| Distributed search | Per-shard sorted results | Coordinator heap merge |
| Version control | Commit / event streams | Priority queue by timestamp |

---

## Further Reading

- RocksDB compaction: https://github.com/facebook/rocksdb/wiki/Compaction
- Bigtable paper (SSTables): https://research.google/pubs/pub27898/
- PostgreSQL external sort: https://www.postgresql.org/docs/current/runtime-config-resource.html#GUC-WORK-MEM
- Lucene segment merge: https://www.elastic.co/blog/lucenes-handling-of-deleted-documents
- Kafka log compaction: https://kafka.apache.org/documentation/#compaction

---

## Where This Sits in a Real Database

The LSM-tree read and compaction paths above are the **defining characteristic** of Bigtable, HBase,
Cassandra, and RocksDB — and the reason they're chosen for write-heavy workloads:

```
write → commit log (sequential append) + in-memory memtable     ← no random disk I/O
memtable full → flush to a new immutable SSTable
compaction → K-WAY MERGE of many SSTables into fewer            ← this problem
read → K-WAY MERGE across memtable + relevant SSTables          ← this problem again
```

**The trade this buys:** writes become sequential appends (fast), and you pay for it later in
**compaction** — which is exactly the k-way merge implemented here. That's the LSM bargain versus a
B-tree's in-place updates, and it's why "which index structure?" is really "do I optimise writes or
reads?"

> See [Database Scaling → Native Scale-Out](https://salman9193.github.io/system-design/#fu-database-scaling)
> for how this fits Bigtable's tablet architecture, and
> [Sharded Database Platform](https://salman9193.github.io/system-design/#hld-sharded-database-platform)
> for the operational side.
