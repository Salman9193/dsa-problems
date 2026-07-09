# Merge K Sorted Lists — Research & Foundations

Merging k sorted lists into one — a **k-way merge** driven by a min-heap over the current heads.

- **J. W. J. Williams (1964), “Algorithm 232: Heapsort,”** *Communications of the ACM* 7(6):347–348. Introduced the **binary heap** / priority queue — the structure that makes k-way merge efficient.
- **D. E. Knuth, *TAOCP* Vol. 3 (Sorting and Searching), §5.4.1 — multiway merging.** The classic treatment of merging k sorted runs with a priority queue / selection tree.

**Why it matters here:** A binary heap (Williams 1964) of the k list heads yields each next-smallest element in O(log k), for O(N log k) total — the classic multiway-merge method (Knuth Vol. 3).

*Citations verified against ACM TOMS / CACM / BIT records this session — not from memory.*
