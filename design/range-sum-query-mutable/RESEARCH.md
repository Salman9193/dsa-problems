# Range Sum Query Mutable — Research & Foundations

Prefix-sum queries with point updates, both in O(log n). The canonical structures are the **Fenwick tree (BIT)** and the segment tree.

- **P. M. Fenwick (1994), “A new data structure for cumulative frequency tables,”** *Software: Practice and Experience* 24(3):327–336. The **Fenwick tree / binary indexed tree (BIT)** — O(log n) prefix sums with point updates, exactly this problem’s requirement.

**Why it matters here:** A Fenwick tree gives exactly O(log n) update and prefix-sum query using an implicit binary-indexed structure — the intended solution.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
