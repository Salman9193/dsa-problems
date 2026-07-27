# Design Skiplist — Real-World Use Cases

Skip lists are the go-to when you need **sorted data with fast, concurrency-friendly
insert/delete** — and, unlike a balanced tree, you get it without any rebalancing code. That single
property (easy concurrency) is why they show up in some of the most-used systems in the world.

---

## 1. Redis Sorted Sets (ZSET) — the canonical example

Redis backs its **sorted set** with a skip list (plus a hash map for O(1) score lookup). It powers:

- **Leaderboards** — members ranked by score, with fast rank queries and range scans.
- **Priority queues / delay queues** — score = timestamp, pop the front.
- **Range-by-score / range-by-rank** — trivial: follow the level-0 list from where the search lands.

The skip list gives Redis `O(log n)` inserts and updates plus **natural in-order range iteration**,
in far less code than a balanced tree — which matters for a single-threaded server where simplicity
and predictable behavior are prized.

---

## 2. LSM-Tree Memtables — the in-memory write buffer

LevelDB, RocksDB, Cassandra, and HBase buffer incoming writes in an in-memory **memtable**, and
that memtable is very often a skip list. Why a skip list specifically:

- Writes arrive constantly and must stay **sorted** (so the flush produces an ordered SSTable).
- The memtable takes **highly concurrent inserts**, and a skip list splices with local pointer
  writes — no whole-subtree rotations to synchronize.

```
write → skip-list memtable (sorted, concurrent)
memtable full → flush as an immutable, sorted SSTable
compaction → k-way MERGE of SSTables   (see Merge K Sorted Lists)
```

> This is the *in-memory half* of the LSM story documented in
> [Database Scaling → Native Scale-Out](https://salman9193.github.io/system-design/#fu-database-scaling).
> The [k-way merge](#linked-list/merge-k-sorted-lists) that compacts the flushed SSTables is the
> other half.

---

## 3. Concurrent / Lock-Free Sorted Maps

`java.util.concurrent.ConcurrentSkipListMap` and `ConcurrentSkipListSet` are skip lists — the JDK's
answer to "a sorted map that many threads hammer at once." A concurrent red-black tree is notoriously
hard because a rotation mutates many nodes atomically; a skip list insert touches only its
`O(log n)` predecessors, so lock-free and fine-grained-locking variants are practical.

---

## 4. Range Indexes & Ordered In-Memory Stores

Any in-memory index needing **ordered iteration + point lookup + range scans** — time-series
buffers, event queues ordered by timestamp, in-memory secondary indexes — is a natural skip-list
fit, for the same reasons as the memtable.

---

## The Unifying Idea

```
need: sorted order + O(log n) mutation + easy concurrency
skip list: randomized levels → no rebalancing → local, splice-only writes
```

| System | What's sorted | Why skip list |
|--------|---------------|---------------|
| Redis ZSET | members by score | O(log n) + range scans, minimal code |
| LSM memtable | keys | concurrent sorted inserts, cheap flush |
| ConcurrentSkipListMap | keys | lock-free-friendly ordered map |
| Time-series buffer | events by time | ordered iteration + fast append |

---

## Further Reading

- **Pugh (1990)**, the original paper — see this problem's discussion of the O(log n) argument.
- Redis internals: sorted sets and skip lists — https://redis.io/docs/data-types/sorted-sets/
- Related: [Merge K Sorted Lists](#linked-list/merge-k-sorted-lists) (LSM compaction),
  [LRU Cache](#design/lru-cache) (the other production-grade design structure),
  [Implement Trie #208](#strings/implement-trie).
