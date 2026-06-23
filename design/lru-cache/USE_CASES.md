# LRU Cache — Real-World Use Cases

---

## 1. CPU Cache Hierarchy

Modern CPUs (Intel, AMD, Apple Silicon) use LRU (or pseudo-LRU) eviction
in L1/L2/L3 caches. The cache controller tracks access recency for cache
lines (64 bytes each) and evicts the least recently used line on a cache miss.

The hardware implementation uses a priority update circuit equivalent to
the doubly linked list's move-to-front operation — updating a small tree
of "last used" bits per set. This is the direct hardware analogue of
`moveToFront(node)`.

---

## 2. Database Buffer Pools — PostgreSQL, MySQL InnoDB

Both PostgreSQL and MySQL InnoDB maintain a buffer pool — a fixed-size
memory region caching disk pages. When the pool is full and a new page
must be loaded, the LRU page is evicted to disk.

PostgreSQL uses a "clock sweep" algorithm (a approximation of LRU) for
its 8kB page buffer. InnoDB uses a modified LRU with a "young" and "old"
sublist — preventing large sequential scans from evicting working-set pages.

---

## 3. CDN Edge Caches — Cloudflare, Akamai, Fastly

CDN edge nodes serve requests from geographically distributed caches.
Each edge node maintains an LRU cache of recently requested assets.
When a new asset is requested and the cache is full, the LRU asset is
evicted to make room.

The HashMap + doubly linked list implementation allows O(1) serve (get)
and O(1) cache update (put) — critical at CDN scale where millions of
requests are served per second per edge node.

---

## Further Reading

- LeetCode #460 LFU Cache — harder variant
- Redis eviction policies: https://redis.io/docs/manual/eviction/
- PostgreSQL buffer management: https://www.postgresql.org/docs/current/storage-page-layout.html
