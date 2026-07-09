# Flatten a Multilevel Doubly Linked List — Research & Foundations

A `child`/`next` structure *is* a general tree (left-child / right-sibling), and
flattening it in O(1) space is traversal by pointer rewiring — the subject of two
landmark papers. *Citations verified against dblp / ACM / Springer — not from memory.*

- **J. M. Morris (1979), "Traversing binary trees simply and cheaply,"** *Information
  Processing Letters* 9(5):197–200. DOI: 10.1016/0020-0190(79)90068-1. Traverses a tree
  in **O(1) space by temporarily rewiring pointers** (threading), restoring structure as
  it goes — the rigorous form of exactly what this problem does. Answers a
  stackless-traversal question posed by Knuth in 1968.
  https://dblp.org/rec/journals/ipl/Morris79a.html

- **A. J. Perlis & C. Thornton (1960), "Symbol manipulation by threaded lists,"**
  *Communications of the ACM* 3(4):195–204. DOI: 10.1145/367177.367202. Introduces
  **threaded lists** — adding pointers to a linked structure so it can be traversed
  without a stack. The intellectual ancestor of "traverse/flatten by pointer surgery."
  https://doi.org/10.1145/367177.367202

- **D. E. Knuth, *The Art of Computer Programming*, Vol. 1 (Fundamental Algorithms),
  §2.3.2 — the natural correspondence between trees and binary trees.** The
  left-child/right-sibling representation that this problem's `child`/`next` node
  embodies; flattening is the preorder DFS linearisation of that tree.

**Why they matter here:** the O(1)-space flatten is the applied version of Morris's
stackless, pointer-rewiring traversal (1979) over a threaded structure (Perlis &
Thornton, 1960), on a tree stored in Knuth's left-child/right-sibling form.
