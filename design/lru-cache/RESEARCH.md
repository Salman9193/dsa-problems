# LRU Cache — Research & Foundations

A fixed-capacity cache that evicts the least-recently-used entry, with O(1) get/put via a hash map plus a doubly linked list. The eviction policy itself is a classic systems idea.

- **L. A. Bélády (1966), “A study of replacement algorithms for a virtual-storage computer,”** *IBM Systems Journal* 5(2):78–101. DOI: 10.1147/sj.52.0078. The foundational study of cache/page **replacement policies** (including the optimal MIN algorithm), the context in which LRU is the practical approximation. https://doi.org/10.1147/sj.52.0078

**Why it matters here:** LRU is the practical stand-in for Bélády’s optimal (but future-knowing) MIN replacement policy — the paper that framed cache replacement.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
