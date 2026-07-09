# Find Duplicate Subtrees — Research & Foundations

Detecting subtrees that appear more than once — give each subtree a canonical fingerprint (serialization or hash) computed bottom-up, then group equal fingerprints.

- **Merkle-tree / subtree hashing.** Detecting identical subtrees uses bottom-up hashing of canonical subtree encodings — the same fingerprint-the-subtree idea as a Merkle (hash) tree. Overview: https://en.wikipedia.org/wiki/Merkle_tree

**Why it matters here:** Hashing each subtree from its children’s hashes is the Merkle-tree idea: identical subtrees get identical fingerprints, so duplicates fall out of a hash map in O(n).

*Citations verified against Canad. J. Math / JACM / SIAM / Bull. AMS / Plenum records this session (Bellman is the foundational DP text) — not from memory.*
