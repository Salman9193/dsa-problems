# Populating Next Right Pointers II — Real-World Use Cases

Establishing horizontal `next` pointers across tree nodes at the same
level is the core operation behind B+ tree leaf linking — the structural
feature that makes database range scans fast.

---

## 1. B+ Tree Leaf Node Linking — Database Range Scans

The `next` pointer populated in this problem is **exactly** the leaf-level
`next` pointer in a B+ tree. This single pointer is the defining structural
feature that separates B+ trees from B-trees, and it is the reason why
PostgreSQL, MySQL InnoDB, and SQL Server can perform range scans in O(1)
per step rather than O(log n) per step.

### How it works

After building a B+ tree, all leaf nodes are linked in sorted key order —
exactly as this algorithm links same-level tree nodes:

```
B+ tree leaf level after linking (same as this problem's output):

[10 → 20 → 30 → 40 → 50 → null]
  ↑                           ↑
first leaf                  last leaf
```

A range query `WHERE age BETWEEN 25 AND 45`:
1. Traverse root → internal nodes → find leaf containing 25  (O(log n))
2. Follow `next` pointers: 30 → 40 → stop at 50 > 45         (O(k) where k = result size)

Without leaf linking, step 2 would require re-traversing the tree from
the root for each successive value — O(k log n) instead of O(k).

### The structural parallel

```
This problem:
  Tree node.next → next node at same level (populated by this algorithm)

B+ tree:
  Leaf node.next → next leaf node in sorted order (populated during tree construction)
```

Both establish a linked list across a single level of a tree structure,
enabling O(1) lateral traversal without re-entering the tree.

### Which databases use linked leaves

- **PostgreSQL** — default B+tree index (`nbtree`); leaf pages linked via
  `BTPageOpaqueData.btpo_next`
- **MySQL InnoDB** — B+tree primary and secondary indexes; leaf pages linked
- **SQL Server** — clustered and non-clustered B+tree indexes
- **SQLite** — B+tree leaf pages linked for table scans
- **MongoDB WiredTiger** — deliberately OMITS leaf linking (re-traverses tree
  for each range step — trades range scan speed for write simplicity)

### References

- **NashTech Blog — B+Tree: The Beating Heart of Indexing in PostgreSQL:**
  https://blog.nashtechglobal.com/btree-the-beating-heart-of-indexing-in-postgresql/
  "The leaf nodes are linked together sequentially (often as a doubly-linked
  list). Once the starting point of a range is found, the database can traverse
  horizontally through the leaves without needing to go back up the tree."

- **DEV.to — B+Tree vs LSM Tree:**
  https://dev.to/priteshsurana/btree-vs-lsm-tree-why-your-databases-data-structure-is-everything-94c
  "In a B+Tree, all leaf nodes are linked together in a doubly linked list in
  sorted key order. A BETWEEN query traverses the tree once to find the starting
  leaf, then reads forward along the linked list. In WiredTiger's B-Tree, leaf
  nodes are not linked — to advance from one leaf to the next during a range scan,
  the engine must re-enter the tree from a higher level."

- **Medium — B Trees and B+ Trees:**
  https://medium.com/@akashsdas_dev/b-trees-and-b-trees-682d363df1f7
  "A defining feature of B+ Trees is that leaf nodes are linked — each one holds
  a pointer to its right neighbour. This makes range queries lightning-fast since
  you can walk through records sequentially without jumping back to internal nodes.
  MySQL InnoDB, PostgreSQL, and SQL Server all implement this linked-leaf strategy."

- **Research paper:** *FB+-tree: A Memory-Optimized B+-tree with Latch-Free Update*,
  arXiv:2503.23397 (2025).
  https://arxiv.org/html/2503.23397v1
  "FB+-tree exhibits superior performance on range scan workloads thanks to
  its balanced structure and sequential pointer arrangement — the linked leaf
  structure is the key differentiator for range scan performance."

- **CMU PDL — Prefetching B+-trees (pB+-trees):**
  https://pdl.cmu.edu/PDL-FTP/Database/pf_final_abs.shtml
  "pB+-Trees provide arrays of pointers to leaf nodes to prefetch arbitrarily
  far ahead, even for nonclustered indices — yielding over a sixfold speedup
  on range scans of 1000+ keys."

---

## 2. Browser Render Tree — CSS Sibling Selectors

The browser's render tree is a tree where nodes at the same depth represent
elements at the same DOM nesting level. CSS sibling selectors (`+` adjacent,
`~` general sibling) require traversing nodes at the same level — the same
horizontal traversal enabled by `next` pointers.

Establishing `next` pointers across same-level render tree nodes (as this
algorithm does) enables O(1)-per-step sibling traversal for CSS selector
matching and layout reflow calculations.

---

## The Core Pattern

Populating `next` pointers is the tree-to-linked-list transformation at
a single level:

```
Before:         After:
    A               A → null
   / \             / \
  B   C           B → C → null
 /     \         /     \
D       E       D  →  →  E → null
```

| System | Tree type | Level = | next pointer enables |
|--------|-----------|---------|---------------------|
| This problem | Binary tree | Same depth | O(1) lateral traversal |
| B+ tree (PostgreSQL/MySQL) | B+ tree | Leaf level only | O(k) range scan (no re-traversal) |
| Browser render tree | DOM tree | Same nesting depth | O(1) CSS sibling matching |

---

## Further Reading

- NashTech B+tree PostgreSQL: https://blog.nashtechglobal.com/btree-the-beating-heart-of-indexing-in-postgresql/
- B+Tree vs LSM Tree: https://dev.to/priteshsurana/btree-vs-lsm-tree-why-your-databases-data-structure-is-everything-94c
- FB+-tree paper: https://arxiv.org/html/2503.23397v1
- CMU pB+-tree prefetching: https://pdl.cmu.edu/PDL-FTP/Database/pf_final_abs.shtml
- PostgreSQL nbtree source: https://github.com/postgres/postgres/tree/master/src/backend/access/nbtree
