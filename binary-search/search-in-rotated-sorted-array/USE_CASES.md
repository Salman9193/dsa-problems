# Search in Rotated Sorted Array — Real-World Use Cases

Searching a sorted but wrapped/rotated structure appears in monitoring
systems, distributed databases, and anywhere data is stored in circular
buffers.

---

## 1. RRDtool — Round Robin Databases

RRDtool (Round Robin Database Tool) is the storage engine behind Prometheus,
Grafana, Cacti, Nagios, and many other monitoring systems. It stores
time-series data (CPU load, network bandwidth, temperatures) in a
**circular buffer** of fixed size.

The buffer is pre-allocated and the write pointer wraps around when full —
overwriting the oldest entry. The stored timestamps are sorted, but the
array is physically rotated: the oldest entry sits somewhere in the middle,
not at index 0.

Searching for a specific timestamp in an RRD is exactly the rotated sorted
array problem. Binary search on the wrapped timestamp array gives O(log n)
lookup — the same algorithm as LeetCode #33.

- **Paper:** Singh et al. — *RRDTool: A Round Robin Database for Network
  Monitoring*, Journal of Computer Science, 18(8):770–776, 2022
  https://thescipub.com/abstract/jcssp.2022.770.776

- **Source code & docs:** https://github.com/oetiker/rrdtool-1.x
  https://oss.oetiker.ch/rrdtool/

- **Wikipedia:** https://en.wikipedia.org/wiki/RRDtool
  "After the archive fills, the next insertion overwrites the oldest entry.
  This behaviour is referred to as 'round-robin'."

---

## 2. Consistent Hashing Ring (Amazon Dynamo, Cassandra, Redis Cluster)

In consistent hashing, both servers and keys are mapped onto a fixed-size
circular ring (e.g. 0 to 2^32 - 1). The ring is a sorted array of node
positions — but it wraps around (the position after the maximum loops back
to 0), making it a rotated sorted structure.

Finding which node owns a key = **binary search on the ring for the nearest
clockwise node** — identical in structure to finding a target in a rotated
sorted array.

### How it works
1. Hash the key to a position on the ring.
2. Binary search the sorted array of node positions for the smallest node
   position ≥ key hash (wrapping around if needed).
3. That node owns the key.

### References

- **Amazon Dynamo paper (canonical reference):**
  DeCandia et al. — *Dynamo: Amazon's Highly Available Key-value Store*,
  ACM SOSP 2007
  https://www.cs.cornell.edu/courses/cs5414/2017fa/papers/dynamo.pdf
  Introduced consistent hashing with virtual nodes as the partitioning
  mechanism for a high-availability key-value store — now the foundation
  of DynamoDB, Cassandra, and ScyllaDB.

- **Production implementations:**
  - DynamoDB — token-based ring partitioning (Dynamo paper, 2007)
  - Apache Cassandra — consistent hashing with virtual nodes
  - Redis Cluster — 16,384 fixed hash slots on a ring, binary search for
    slot-to-node mapping
  - Memcached libketama — 160 virtual nodes per server, MD5-derived ring
    positions, binary search for lookup

- **Technical overview:**
  https://blog.algomaster.io/p/consistent-hashing-explained
  "A sorted list of all hash values in the ring enables efficient lookups
  using binary search to find the nearest clockwise server."

---

## Summary

| Domain | Rotated structure | Search operation |
|--------|------------------|-----------------|
| RRDtool / monitoring DBs | Circular buffer of timestamped metrics | Binary search on wrapped timestamp array |
| Consistent hashing ring | Sorted array of node hash positions (wraps at 2^32) | Binary search for nearest clockwise node |

---

## Further Reading

- RRDtool paper: https://thescipub.com/abstract/jcssp.2022.770.776
- RRDtool source: https://github.com/oetiker/rrdtool-1.x
- Amazon Dynamo paper: https://www.cs.cornell.edu/courses/cs5414/2017fa/papers/dynamo.pdf
- Consistent hashing deep dive: https://blog.algomaster.io/p/consistent-hashing-explained
- Redis Cluster hash slots: https://redis.io/docs/management/scaling/
