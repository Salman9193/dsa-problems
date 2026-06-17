# Binary Search Tree Iterator — Real-World Use Cases

The controlled-stack BST iterator is the in-memory model of two of the
most widely used software components: database cursors and standard library
ordered map iterators.

---

## 1. Database Cursors — Lazy Sorted Index Traversal

A database cursor is the production implementation of this exact algorithm.
When PostgreSQL or MySQL executes `SELECT * FROM orders ORDER BY date`,
it opens a cursor on the B-tree index for `date`. The cursor:

1. Descends to the leftmost leaf (equivalent to `pushLeft(root)`)
2. Returns one row at a time (`next()`)
3. Advances by going right in the leaf level or up to the parent
   (equivalent to `pushLeft(node.right)`)

The cursor never loads the entire result set into memory. It maintains
only the path from root to current position — O(h) space, one row at a time.

### Why this matters in production

Without lazy cursor traversal:
- `SELECT * FROM table ORDER BY x LIMIT 10` would sort all n rows to get 10
- Network streaming of large result sets would require buffering the full result
- Nested loop joins would require materialising the inner table

With lazy cursor (BST iterator pattern):
- O(h) memory regardless of result set size
- First row returned after O(h) steps (not O(n))
- Supports early termination (LIMIT) efficiently

### References

- **CS186 Berkeley — B+ Trees and Index Traversal:**
  https://cs186berkeley.net/notes/note4/
  "Because of the sorted order, we can traverse the tree in order without
  having to sort the entire result set. The B-Tree structure maintains
  sorted order, making it perfect for range queries and cursor traversal."

- **Medium — Database Indexing Strategies:**
  https://medium.com/@artemkhrenov/database-indexing-strategies-b-tree-hash-and-specialized-indexes-explained-95a3e5e3b632
  "The B-Tree structure maintains sorted order. When you ask for results
  sorted by a column, the database can traverse the tree in order without
  sorting the entire result set. Adding a B-Tree index dropped a query
  from 8 seconds to 45 milliseconds."

- **BST Iterator pattern — academic framing:**
  https://www.cs.odu.edu/~zeil/cs361/latest/Public/treetraversal/index.html
  "We find the beginning position by starting from the root and working our
  way down, always taking left children, until we come to a node with no
  left child — the same pushLeft() initialisation in the BST iterator."

- **FB+-tree concurrent iterator** (arXiv:2503.23397):
  https://arxiv.org/html/2503.23397v1
  "FB+-tree's concurrent iterator is almost identical to an STL iterator —
  in concurrent environments, an iterator coordinates with insert and remove
  operations, maintaining the current position in the sorted leaf traversal."

---

## 2. Java TreeMap / C++ std::map — Red-Black Tree Iterators

Java's `TreeMap` and C++'s `std::map` are red-black trees — self-balancing
BSTs. Their iterators implement the same controlled-stack inorder traversal
as this problem, with O(h) space and O(1) amortised advancement.

### Java TreeMap iterator

`TreeMap.KeyIterator.next()` calls `successor(entry)`:

```java
// From JDK source: java.util.TreeMap
static <K,V> TreeMap.Entry<K,V> successor(Entry<K,V> t) {
    if (t == null)
        return null;
    else if (t.right != null) {
        // Go right, then all the way left
        Entry<K,V> p = t.right;
        while (p.left != null) p = p.left;   // pushLeft equivalent
        return p;
    } else {
        // Go up until we came from a left child
        Entry<K,V> p = t.parent;
        Entry<K,V> ch = t;
        while (p != null && ch == p.right) {
            ch = p;
            p = p.parent;
        }
        return p;
    }
}
```

**Step 1** (go right then all the way left) is equivalent to
`stack.pop()` + `pushLeft(node.right)` — the same algorithm as `next()`.

The difference: `TreeMap` uses parent pointers instead of an explicit stack,
saving one pointer per node but requiring the parent pointer in the node struct.
The BST iterator (this problem) uses an explicit stack because nodes have no
parent pointers.

### C++ std::map iterator

`std::map::iterator::operator++()` calls `_Rb_tree_increment()` in libstdc++:
```cpp
// From GCC libstdc++ bits/stl_tree.h
_Rb_tree_node_base* _Rb_tree_increment(_Rb_tree_node_base* __x) {
  if (__x->_M_right != 0) {           // go right
    __x = __x->_M_right;
    while (__x->_M_left != 0)          // then all the way left
      __x = __x->_M_left;
  } else {                             // or go up
    ...
  }
  return __x;
}
```

Again identical to `pushLeft(node.right)` — the controlled-stack pattern.

---

## The Unified Pattern

All three implementations share the same core operation:

```
After visiting node N:
  1. Go to N's right child
  2. Descend all the way left from there
  3. That node is the inorder successor (next smallest)
```

| Implementation | How step 1-2 is done | Space |
|----------------|---------------------|-------|
| BST Iterator (this problem) | Explicit stack (`pushLeft`) | O(h) stack |
| Java TreeMap | Parent pointers + loop | O(1) per call, O(h) parent chain |
| C++ std::map | Parent pointers + `_Rb_tree_increment` | O(1) per call, O(h) parent chain |
| Database cursor | Stack of B-tree page pointers | O(h) page stack |

---

## Further Reading

- CS186 B-tree traversal: https://cs186berkeley.net/notes/note4/
- FB+-tree iterator (arXiv): https://arxiv.org/html/2503.23397v1
- JDK TreeMap source: https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/util/TreeMap.java
- GCC libstdc++ stl_tree.h: https://github.com/gcc-mirror/gcc/blob/master/libstdc%2B%2B-v3/include/bits/stl_tree.h
- BST iterator academic: https://www.cs.odu.edu/~zeil/cs361/latest/Public/treetraversal/index.html
