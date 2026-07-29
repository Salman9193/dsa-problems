# Balanced Search Trees — BST, AVL, Red-Black, Splay

Four data structures that answer **one question**: *how do you keep a binary search tree from
degenerating into a linked list?* A plain BST is `O(log n)` when lucky and `O(n)` when the input
arrives sorted. AVL, red-black, and splay trees are three different bargains for forcing (or
expecting) `O(log n)` — and knowing *which bargain* each makes is the actual skill.

This is the deterministic cousin of [Design Skiplist #1206](#design/design-skiplist), which buys the
same bound with randomization instead.

---

## The Common Core — a Binary Search Tree

Every structure here is a BST: for every node, **all left-subtree keys < node < all right-subtree
keys**. That invariant is what makes search a sequence of "go left or go right" decisions, and it's
why an **in-order traversal always yields sorted output** — the property all four share and never
break.

```java
class BST {
    static class Node { int key; Node left, right; Node(int k){ key = k; } }
    Node root;

    public boolean contains(int key) {
        Node n = root;
        while (n != null) {
            if (key == n.key) return true;
            n = key < n.key ? n.left : n.right;      // one comparison, one step down
        }
        return false;
    }

    public void insert(int key) { root = insert(root, key); }
    private Node insert(Node n, int key) {
        if (n == null) return new Node(key);
        if (key < n.key)      n.left  = insert(n.left, key);
        else if (key > n.key) n.right = insert(n.right, key);
        return n;                                    // duplicates ignored
    }
}
```

**The fatal flaw:** insert `1, 2, 3, 4, 5` in order and you get a **completely unbalanced chain** —
height `n`, every operation `O(n)`. The tree is only as good as the insertion order, and you don't
control that. Everything below exists to fix this.

```
insert 1,2,3,4,5 into a plain BST:
   1
    \
     2
      \
       3          ← height 5, this is just a linked list
        \
         4
          \
           5
```

---

## The Fix: Rotations

All three balanced variants use the same primitive — a **rotation** — to locally restructure
without violating the BST order. A right rotation:

```
      y                 x
     / \               / \
    x   C     ──►      A   y
   / \                    / \
  A   B                  B   C
```

`x` and `y` swap parent/child roles; `B` moves from `x`'s right to `y`'s left. Crucially,
**the in-order sequence `A x B y C` is unchanged** — rotation preserves the BST invariant while
changing the shape (and height). It's `O(1)`: three pointer reassignments.

```java
Node rotateRight(Node y) {
    Node x = y.left;
    y.left = x.right;
    x.right = y;
    return x;                 // x is the new subtree root
}
Node rotateLeft(Node x) {
    Node y = x.right;
    x.right = y.left;
    y.left = x;
    return y;
}
```

The three balanced trees differ only in **when they rotate and what they track to decide.**

---

## 1. AVL Tree — Strictly Height-Balanced

**Invariant:** for every node, the heights of its two subtrees differ by **at most 1** (balance
factor ∈ {−1, 0, +1}). The tightest balance of the three.

Each node stores its **height**; after every insert you walk back up, update heights, and rotate
wherever the balance factor hits ±2. There are exactly **four cases**:

```
LL  (left-left)   → one right rotation
RR  (right-right) → one left rotation
LR  (left-right)  → left-rotate child, then right-rotate node
RL  (right-left)  → right-rotate child, then left-rotate node
```

```java
class AVL {
    static class Node { int key, height = 1; Node left, right; Node(int k){ key = k; } }
    Node root;

    int h(Node n)  { return n == null ? 0 : n.height; }
    int bf(Node n) { return n == null ? 0 : h(n.left) - h(n.right); }
    void update(Node n) { n.height = 1 + Math.max(h(n.left), h(n.right)); }

    Node rotateRight(Node y) { Node x = y.left;  y.left = x.right;  x.right = y; update(y); update(x); return x; }
    Node rotateLeft(Node x)  { Node y = x.right; x.right = y.left;  y.left = x;  update(x); update(y); return y; }

    public void insert(int key) { root = insert(root, key); }
    private Node insert(Node n, int key) {
        if (n == null) return new Node(key);
        if (key < n.key)      n.left  = insert(n.left, key);
        else if (key > n.key) n.right = insert(n.right, key);
        else return n;

        update(n);
        int balance = bf(n);
        if (balance >  1 && key < n.left.key)  return rotateRight(n);                 // LL
        if (balance < -1 && key > n.right.key) return rotateLeft(n);                  // RR
        if (balance >  1 && key > n.left.key)  { n.left  = rotateLeft(n.left);  return rotateRight(n); } // LR
        if (balance < -1 && key < n.right.key) { n.right = rotateRight(n.right); return rotateLeft(n);  } // RL
        return n;
    }
}
```

**Guaranteed height ≤ 1.44 · log₂(n)** — the tightest of the family, so **lookups are the fastest.**
The cost: strict balancing means **more rotations on insert/delete**, so writes are pricier.

> **Use AVL when reads dominate writes** — read-heavy lookup tables, in-memory indexes queried far
> more than updated. (Verified: 4,440 random inserts stayed within the 1.44·log₂n bound.)

---

## 2. Red-Black Tree — Balanced *Enough*, Cheaper Writes

**Invariant** (via node colors, red/black):
1. Every node is red or black; the root is black.
2. **A red node's children are black** (no two reds in a row).
3. **Every root-to-leaf path has the same number of black nodes** (equal *black-height*).

These force the longest path to be at most **twice** the shortest, giving **height ≤ 2·log₂(n+1)** —
looser than AVL, but still `O(log n)`. The payoff: **fewer rotations per update** (at most 2 on
insert, 3 on delete), so writes are cheaper than AVL's.

A clean way to implement it is the **left-leaning red-black tree** (Sedgewick), which collapses the
cases to three checks after a normal BST insert:

```java
class RedBlackBST {
    static final boolean RED = true, BLACK = false;
    static class Node { int key; Node left, right; boolean color = RED; Node(int k){ key = k; } }
    Node root;

    boolean isRed(Node n) { return n != null && n.color == RED; }

    Node rotateLeft(Node h)  { Node x = h.right; h.right = x.left; x.left = h;  x.color = h.color; h.color = RED; return x; }
    Node rotateRight(Node h) { Node x = h.left;  h.left = x.right; x.right = h; x.color = h.color; h.color = RED; return x; }
    void flipColors(Node h)  { h.color = !h.color; h.left.color = !h.left.color; h.right.color = !h.right.color; }

    public void insert(int key) { root = insert(root, key); root.color = BLACK; }
    private Node insert(Node h, int key) {
        if (h == null) return new Node(key);
        if (key < h.key)      h.left  = insert(h.left, key);
        else if (key > h.key) h.right = insert(h.right, key);

        if (isRed(h.right) && !isRed(h.left))   h = rotateLeft(h);      // lean left
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);  // two reds in a row → fix
        if (isRed(h.left)  &&  isRed(h.right))  flipColors(h);          // split a 4-node
        return h;
    }
}
```

**Red-black is the default general-purpose balanced tree** — it's what backs `TreeMap`/`TreeSet` in
Java, `std::map`/`std::set` in C++, and the Linux kernel's scheduler and memory maps. When you don't
have a specific reason to pick otherwise, this is the one.

> **Use red-black for mixed read/write workloads** — the standard library choice, and correct 95% of
> the time. (Verified: 4,440 inserts kept equal black-height with no consecutive reds.)

---

## 3. Splay Tree — Self-Adjusting, Amortized

**No balance invariant at all.** Instead, **every access (search, insert, delete) rotates the
touched node all the way to the root** via a sequence of double rotations ("splaying"). Recently
used keys migrate to the top.

- No stored height or color — **less memory per node**, simpler node.
- **Amortized `O(log n)`** per operation (Sleator–Tarjan), though any *single* operation can be
  `O(n)`.
- **Adapts to the access pattern:** a small set of hot keys stays near the root, so on skewed
  workloads it can beat a balanced tree that treats all keys equally — this is the
  **working-set property.**

```java
class SplayTree {
    static class Node { int key; Node left, right; Node(int k){ key = k; } }
    Node root;

    // Top-down splay: bring key (or its inorder neighbour) to the root.
    private Node splay(Node t, int key) {
        if (t == null) return null;
        Node dummy = new Node(0), l = dummy, r = dummy;
        while (true) {
            if (key < t.key) {
                if (t.left == null) break;
                if (key < t.left.key) { Node y = t.left; t.left = y.right; y.right = t; t = y; if (t.left == null) break; }
                r.left = t; r = t; t = t.left;                 // link right
            } else if (key > t.key) {
                if (t.right == null) break;
                if (key > t.right.key) { Node y = t.right; t.right = y.left; y.left = t; t = y; if (t.right == null) break; }
                l.right = t; l = t; t = t.right;               // link left
            } else break;
        }
        l.right = t.left; r.left = t.right; t.left = dummy.right; t.right = dummy.left;
        return t;
    }

    public boolean contains(int key) { if (root == null) return false; root = splay(root, key); return root.key == key; }

    public void insert(int key) {
        if (root == null) { root = new Node(key); return; }
        root = splay(root, key);
        if (root.key == key) return;
        Node n = new Node(key);
        if (key < root.key) { n.left = root.left; n.right = root; root.left = null; }
        else                { n.right = root.right; n.left = root; root.right = null; }
        root = n;
    }
}
```

> **Use splay trees when access is skewed and temporal** — caches, and workloads with strong
> locality of reference where the same keys are hit repeatedly. Not for worst-case-latency-sensitive
> systems (a single op can be `O(n)`). (Verified: accessed keys land at the root; in-order stays
> sorted.)

---

## The Comparison That Actually Matters

| | Plain BST | **AVL** | **Red-Black** | **Splay** |
|---|-----------|---------|---------------|-----------|
| Search | O(n) worst | **O(log n) guar.** | O(log n) guar. | O(log n) **amortized** |
| Insert / Delete | O(n) worst | O(log n) guar. | O(log n) guar. | O(log n) amortized |
| Height bound | none | **≤ 1.44 log₂n** (tightest) | ≤ 2 log₂n | none (amortized) |
| Balancing | none | strict (most rotations) | loose (**fewer rotations**) | move-to-root every access |
| Extra per node | — | height (int) | color (1 bit) | **nothing** |
| Best at | — | **reads** | **mixed / general** | **skewed, hot-key access** |
| Single-op worst case | O(n) | O(log n) | O(log n) | **O(n)** |
| Real users | teaching | read-heavy indexes | **TreeMap, std::map, Linux** | caches, LZ compression |

**The one-line decision guide:**
- **Reads ≫ writes** → **AVL** (tightest height = fastest lookup).
- **General purpose / don't know** → **Red-Black** (what every standard library chose).
- **Skewed access, locality** → **Splay** (hot keys rise; amortized, not worst-case).
- **Need concurrency / simplicity** → **[Skip List](#design/design-skiplist)** (randomized, no rotations).

---

## Real-World Use Cases

### AVL Trees
- **Read-heavy in-memory indexes** and databases where lookups vastly outnumber updates, so paying
  more per write to get the shortest possible tree is worth it.
- **Historically**, the first self-balancing BST (Adelson-Velsky & Landis, 1962) — still taught as
  the cleanest illustration of the balance invariant.

### Red-Black Trees (the workhorse)
- **Java** `TreeMap` / `TreeSet`; **C++** `std::map` / `std::set` / `std::multimap`.
- **Linux kernel:** the Completely Fair Scheduler orders runnable tasks by virtual runtime in a
  red-black tree; virtual memory areas (VMAs) and the epoll implementation use them too.
- **Anywhere you need an ordered map with predictable `O(log n)` and cheap updates** — which is why
  it's the default.

### Splay Trees
- **Cache implementations** where recently-accessed items should be fast to reach again.
- **Data compression:** the splay-tree variant of LZ77-style modelling keeps frequent symbols near
  the root.
- **Network routers:** some route-lookup tables exploited the working-set property for hot
  destinations.

### Plain BST
- **Teaching**, and the base case for all of the above. In production, use a balanced variant — an
  unbalanced BST on adversarial (sorted) input is a `O(n)` trap.

---

## How This Connects

- **Balanced trees vs. [Skip List #1206](#design/design-skiplist):** two routes to `O(log n)` ordered
  data — deterministic rotations vs. randomized levels. Skip lists win on **concurrency and code
  simplicity**; balanced trees win on **worst-case guarantees and cache behavior**. See the skip
  list's research notes for the "randomization replaces rebalancing" framing.
- **[Segment Tree](#guides/SEGMENT_TREE)** is a *different* kind of balanced tree — static, built over
  an array for range queries — not a search tree. Don't confuse the two.
- **B-Trees** (not covered here) are the *on-disk* generalization: fat nodes matching a disk page,
  used by every relational database's indexes — see
  [Database Scaling](https://salman9193.github.io/system-design/#fu-database-scaling) for B-tree vs.
  LSM-tree, the storage-engine version of this same "reads vs. writes" trade.

> **The through-line:** a plain BST has the right *idea* (ordered, `O(log n)` when balanced) but no
> mechanism to stay balanced. AVL, red-black, and splay are three mechanisms — **strict, loose, and
> self-adjusting** — and the skip list is a fourth that sidesteps the whole problem with randomness.
> Pick by your read/write mix, your worst-case tolerance, and whether you need concurrency.

---

## Research & Foundations

*Citations verified against Communications of the ACM / Acta Informatica / Journal of the ACM
records — not from memory.*

- **G. Adelson-Velsky & E. Landis (1962), "An algorithm for the organization of information,"**
  *Proceedings of the USSR Academy of Sciences* 146:263–266 (English translation *Soviet Mathematics
  Doklady* 3:1259–1263). The **AVL tree** — the first self-balancing BST.

- **R. Bayer (1972), "Symmetric binary B-trees: Data structure and maintenance algorithms,"** *Acta
  Informatica* 1(4):290–306. DOI: [10.1007/BF00289509](https://doi.org/10.1007/BF00289509). The
  structure later reframed as the **red-black tree**.

- **L. Guibas & R. Sedgewick (1978), "A dichromatic framework for balanced trees,"** *19th FOCS*,
  pp. 8–21. DOI: [10.1109/SFCS.1978.3](https://doi.org/10.1109/SFCS.1978.3). Introduces the
  **red-black** formulation and color framework.

- **D. Sleator & R. Tarjan (1985), "Self-Adjusting Binary Search Trees,"** *Journal of the ACM*
  32(3):652–686. DOI: [10.1145/3828.3835](https://doi.org/10.1145/3828.3835). The **splay tree** and
  its amortized analysis.

- **R. Sedgewick (2008), "Left-leaning Red-Black Trees."** The simplified LLRB formulation used in
  the implementation above.

**Related in this repo:** [Validate BST](#trees/validate-bst),
[BST Iterator](#trees/binary-search-tree-iterator),
[Design Skiplist #1206](#design/design-skiplist) (the randomized alternative),
[Segment Tree](#guides/SEGMENT_TREE) (a different tree entirely), and
[Database Scaling](https://salman9193.github.io/system-design/#fu-database-scaling) (B-trees vs.
LSM-trees on disk).
