# Linked List Random Node — Research & Foundations

Returning a uniformly random node from a list of unknown length in one pass — exactly **reservoir sampling**.

- **J. S. Vitter (1985), “Random sampling with a reservoir,”** *ACM Transactions on Mathematical Software* 11(1):37–57. DOI: 10.1145/3147.3165. **Reservoir sampling** — pick a uniform random item from a stream of unknown length in one pass, O(1) space. https://doi.org/10.1145/3147.3165

**Why it matters here:** Vitter’s reservoir sampling (1985) keeps the i-th element with probability 1/i as it is seen, giving a uniform pick in one O(n)-time, O(1)-space pass — no need to know the length first.

*Citations verified against ACM TOMS / CACM / BIT records this session — not from memory.*
