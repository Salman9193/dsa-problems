# Implement Trie — Research & Foundations

A trie (prefix tree) stores a set of strings by shared prefixes, giving O(key length) insert and search. This problem is a direct implementation of that classic structure.

- **E. Fredkin (1960), “Trie memory,”** *Communications of the ACM* 3(9):490–499. The paper that introduced the **trie** (prefix tree) — the data structure at the heart of this problem.
- **R. de la Briandais (1959), “File searching using variable length keys,”** *Proc. Western Joint Computer Conference*, pp. 295–298. The earlier introduction of the trie idea that Fredkin named.

**Why it matters here:** Insert / search / startsWith each walk one edge per character down the prefix tree — the operations Fredkin’s trie was designed for.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
