# Find Duplicate Subtrees — Real-World Use Cases

Fingerprinting subtrees to detect identical structure is the foundation of
content-addressed storage, compiler common-subexpression elimination, and
structural deduplication. The postorder-signature idea — hash a node from its
children's hashes — is exactly how Merkle trees and build systems work.

---

## 1. Content-Addressed Storage — Merkle Trees (Git, IPFS)

Git and IPFS give every object a hash derived from its content, and every tree
object's hash is computed from its children's hashes — a postorder fingerprint,
identical in spirit to this problem's signature. Two subtrees with the same
hash are the same object and are stored **once**. This is how `git` deduplicates
identical directories across commits and how IPFS deduplicates identical file
chunks across the network.

- **Git internals — tree objects & content addressing:**
  https://git-scm.com/book/en/v2/Git-Internals-Git-Objects
  "Git names objects by the SHA-1 hash of their content; identical content
  yields the same object, stored only once."

- **Merkle tree — Wikipedia:**
  https://en.wikipedia.org/wiki/Merkle_tree
  "Every leaf is labelled with the hash of a data block, and every non-leaf
  with the hash of its child nodes' labels."

- **IPFS Merkle DAG:**
  https://docs.ipfs.tech/concepts/merkle-dag/
  Deduplicates identical subtrees of the file DAG by content hash.

---

## 2. Compilers — Common Subexpression Elimination (CSE)

A compiler's intermediate representation is a tree/DAG of expressions. When two
subexpressions are structurally identical (`a*b` computed twice), CSE detects
the duplicate — via **value numbering**, which assigns each unique subexpression
an ID exactly like the subtree-ID optimization here — and computes it once,
reusing the result. This turns the expression tree into a DAG.

- **Value numbering — Wikipedia:**
  https://en.wikipedia.org/wiki/Value_numbering
  "Value numbering assigns each computation a number such that two computations
  get the same number iff they are provably equivalent" — the subtree-ID idea.

- **LLVM GVN (Global Value Numbering) pass:**
  https://llvm.org/docs/Passes.html#gvn-global-value-numbering
  Eliminates redundant subexpressions across the IR.

---

## 3. Build Systems — Action & Artifact Deduplication

Modern build systems (Bazel, Buck) hash each build action from its inputs
(which are themselves hashed outputs of prior actions) — a Merkle-style
postorder fingerprint of the build graph. Identical subtrees of the build graph
produce identical hashes, so their outputs are fetched from cache instead of
rebuilt, and shared across machines.

- **Bazel remote caching (action hashing):**
  https://bazel.build/remote/caching
  "Bazel computes a hash of each action's command line and inputs; matching
  hashes reuse the cached output."

- **Bazel Merkle tree input format:**
  https://github.com/bazelbuild/remote-apis
  The remote execution API sends inputs as a Merkle tree of directory digests.

---

## 4. Code Clone & Plagiarism Detection

Tools that detect duplicated code parse source into abstract syntax trees and
look for identical (or near-identical) subtrees — precisely this problem applied
to ASTs. Serializing each AST subtree and grouping by signature surfaces
copy-pasted functions and structural clones.

- **Baxter et al., "Clone Detection Using Abstract Syntax Trees" (ICSM 1998):**
  https://www.researchgate.net/publication/3814634_Clone_detection_using_abstract_syntax_trees
  The seminal paper: hash AST subtrees into buckets, then compare within
  buckets to find duplicated code.

- **PMD Copy-Paste Detector (CPD):**
  https://pmd.github.io/latest/pmd_userdocs_cpd.html
  Detects duplicated code across a codebase.

---

## Summary

| Domain | Subtree being fingerprinted | Signature mechanism |
|--------|----------------------------|---------------------|
| Git / IPFS | Directory / file tree | SHA hash of children's hashes |
| Compilers (CSE) | Expression subtree | Value numbering (subtree IDs) |
| Build systems | Build-action graph | Merkle hash of inputs |
| Clone detection | AST subtree | Serialized subtree signature |

---

## Further Reading

- Git objects: https://git-scm.com/book/en/v2/Git-Internals-Git-Objects
- Merkle tree: https://en.wikipedia.org/wiki/Merkle_tree
- Value numbering (CSE): https://en.wikipedia.org/wiki/Value_numbering
- Bazel remote caching: https://bazel.build/remote/caching
- AST clone detection (Baxter 1998): https://www.researchgate.net/publication/3814634_Clone_detection_using_abstract_syntax_trees
