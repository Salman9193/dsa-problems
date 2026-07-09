# Word Search II — Research & Foundations

Searching a grid for many dictionary words at once. The efficient approach builds a **trie** of the word list and runs one DFS, pruning by trie prefixes — a grid analogue of multi-pattern matching.

- **E. Fredkin (1960), “Trie memory,”** *Communications of the ACM* 3(9):490–499. The paper that introduced the **trie** (prefix tree) — the data structure at the heart of this problem.
- **A. V. Aho & M. J. Corasick (1975), “Efficient string matching: an aid to bibliographic search,”** *Communications of the ACM* 18(6):333–340. **Multi-pattern matching** via a trie of patterns plus failure links — the automaton behind searching many words at once.

**Why it matters here:** A trie (Fredkin 1960) lets one traversal test all words simultaneously and prune dead prefixes; the many-patterns-at-once idea is the same motivation as the Aho–Corasick automaton.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
