# Top K Frequent Elements — Research & Foundations

Returning the k most frequent elements — a frequency tally plus either bucket sort by count (O(n)) or Quickselect over the distinct values.

- **D. E. Knuth, *The Art of Computer Programming*, Vol. 3 (Sorting and Searching), §5.2** — distribution/counting sort, the basis of bucketing elements by frequency in O(n).
- **C. A. R. Hoare (1961), “Algorithm 65: Find,”** *Communications of the ACM* 4(7):321–322. **Quickselect** — find the k-th smallest by partitioning around a pivot and recursing into one side, O(n) average.

**Why it matters here:** Frequencies are bounded by n, so bucketing by count (Knuth Vol. 3) gives O(n); alternatively Quickselect (Hoare 1961) selects the top k without a full sort.

*Selection / 3SUM citations verified against JCSS / Computational Geometry / CACM records this session — not from memory.*
