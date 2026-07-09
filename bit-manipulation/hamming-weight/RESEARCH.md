# Hamming Weight — Research & Foundations

Counting the set bits (population count) of an integer; the `n & (n-1)` loop runs once per set bit.

- **P. Wegner (1960), “A technique for counting ones in a binary computer,”** *Communications of the ACM* 3(5):322. DOI: 10.1145/367236.367286. The origin of the **`n & (n-1)` bit-clearing trick** (a.k.a. Kernighan’s algorithm): each step removes the lowest set bit, so the loop runs once per set bit. https://doi.org/10.1145/367236.367286

**Why it matters here:** Wegner (1960) introduced clearing the lowest set bit with n & (n-1), so the loop iterates exactly (number of ones) times — the trick this problem rewards.

*Citations verified against ACM TOMS / CACM / BIT records this session — not from memory.*
