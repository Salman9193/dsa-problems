# Clone Graph — Real-World Use Cases

Deep-copying a graph with cycles and shared references is a fundamental
systems operation. It appears in compilers, operating systems, version
control, and distributed databases.

---

## 1. LLVM Compiler — IR Function Graph Cloning

When LLVM inlines a function into its caller, it must deep-clone the
function's intermediate representation (IR) graph — a graph of basic
blocks connected by control-flow edges (branches, loops). The
`ValueToValueMapTy` in LLVM is the exact equivalent of the visited HashMap:
it maps original IR values to their clones, preventing double-cloning of
shared values and breaking cycles in loops.

- **LLVM Source — `CloneFunction.cpp`:**
  The dedicated file for cloning IR graphs during inlining.
  https://github.com/llvm/llvm-project/blob/main/llvm/lib/Transforms/Utils/CloneFunction.cpp

- **LLVM API — `Cloning.h`:**
  Defines `CloneFunction`, `CloneBasicBlock`, and `InlineFunction`.
  https://llvm.org/doxygen/Cloning_8h.html

- **Paper:** Cummins et al. — *MLGO: a Machine Learning Guided Compiler
  Optimizations Framework* (2021), which describes LLVM's inlining pass
  operating on the call graph's strongly-connected components.
  https://arxiv.org/pdf/2101.04808

---

## 2. Unix fork() — Process Memory Graph Copy

When a process forks in Unix/Linux, the OS conceptually deep-copies the
entire process address space: a graph of virtual memory pages and their
page-table mappings. The "visited set" is the page table itself — pages
already copied are not duplicated again.

Modern Linux optimises this with **Copy-on-Write (COW)**: pages are
initially shared between parent and child (marked read-only), and a
physical copy is made only when one process writes to a page. This is
the lazy version of the clone-graph algorithm.

- **Linux kernel explanation:**
  When `fork()` is called, the parent's virtual-to-physical address
  translations are marked read-only and a copy of those translations is
  made for the child, allowing both to share the same physical pages until
  a write occurs.

- **xv6 COW Fork Lab (Duke/MIT OS courses):**
  https://courses.cs.duke.edu/fall25/compsci510/cow.html
  Hands-on implementation of COW fork — directly implements the
  reference-counted graph copy the clone graph algorithm describes.

- **Wikipedia — Copy-on-write:**
  https://en.wikipedia.org/wiki/Copy-on-write

---

## 3. Git — Commit DAG Traversal & Cloning

Git stores history as a Directed Acyclic Graph (DAG) of commit objects.
Every `git clone` and `git fetch` traverses this graph and copies nodes
(commits) that the receiver doesn't have yet. Git's object store — a
HashMap of SHA-1 → object — is the visited map: if a commit's SHA is
already present, it is not re-fetched.

- **Official Git Glossary:**
  "Commit objects form a directed acyclic graph because they have parents
  (directed) and the graph is acyclic."
  https://git-scm.com/docs/gitglossary

- **"Git for Computer Scientists"** (eagain.net) — widely cited technical
  reference explaining Git's DAG object model:
  https://eagain.net/articles/git-for-computer-scientists/
  "The DAG only gets added to and existing nodes cannot be mutated."

---

## 4. Apache Cassandra — Data Replication Graph

When Cassandra replicates data or migrates a shard to a new node, it
traverses the data dependency graph and copies rows to target nodes
according to a replication strategy. A visited/seen mechanism prevents
double-replication of rows shared across partition ranges.

- **DataStax / Cassandra Docs — Data Replication:**
  "Cassandra stores replicas on multiple nodes to ensure reliability and
  fault tolerance. All replicas are equally important; there is no primary
  or master replica."
  https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/architecture/archDataDistributeReplication.html

- **Apache Cassandra Wikipedia:**
  Cassandra incorporates Amazon Dynamo's distributed storage and
  replication techniques, using consistent hashing to distribute data
  across nodes.
  https://en.wikipedia.org/wiki/Apache_Cassandra

---

## Summary

| Domain | Graph being cloned | Visited map equivalent |
|--------|--------------------|----------------------|
| LLVM compiler | IR basic-block control-flow graph | `ValueToValueMapTy` |
| Unix `fork()` | Process virtual memory page graph | Page table + COW ref counts |
| Git | Commit DAG | SHA-1 → object store (HashMap) |
| Apache Cassandra | Data partition replica graph | Replication strategy seen-set |

---

## Further Reading

- LLVM CloneFunction source: https://github.com/llvm/llvm-project/blob/main/llvm/lib/Transforms/Utils/CloneFunction.cpp
- LLVM Cloning API: https://llvm.org/doxygen/Cloning_8h.html
- COW fork (xv6 lab): https://courses.cs.duke.edu/fall25/compsci510/cow.html
- Git internals (DAG): https://eagain.net/articles/git-for-computer-scientists/
- Cassandra replication: https://docs.datastax.com/en/cassandra-oss/3.x/cassandra/architecture/archDataDistributeReplication.html
