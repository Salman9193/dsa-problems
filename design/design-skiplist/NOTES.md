# Design Skiplist — Notes & Intuition

**LeetCode #1206** | Probabilistic data structure | Hard
Build a sorted structure with `O(log n)` expected search/insert/delete — **without** the rotations
and rebalancing a balanced BST needs. The trick: **flip coins instead of enforcing balance.**

---

## Problem

Implement `Skiplist`:
- `search(target)` → is `target` present?
- `add(num)` → insert (duplicates allowed)
- `erase(num)` → remove one occurrence; return whether it existed

All in `O(log n)` **expected** time.

---

## The Idea — Express Lanes Over a Sorted List

Start with a sorted linked list: search is `O(n)` because you step one node at a time. A skip list
adds **express lanes** on top — higher levels that skip over many nodes:

```
L3: HEAD ─────────────────────────► 50 ──────────────► NIL
L2: HEAD ─────────► 20 ───────────► 50 ──────► 70 ───► NIL
L1: HEAD ─► 10 ──► 20 ─► 30 ─► 40 ► 50 ─► 60 ► 70 ───► NIL
            (every node is on L1; ~half reach L2; ~quarter reach L3…)
```

**Search** walks the top lane until the next node would overshoot, then drops a level and repeats.
Each level roughly **halves** the remaining candidates — the same logarithmic collapse as binary
search, but over a linked structure that supports `O(1)` splice-in insertion.

---

## Why It's `O(log n)` — the Coin-Flip Argument

When you insert a node, you give it a **random level**: always on level 1; with probability `p`
(=½) also on level 2; with probability `p²` on level 3; and so on. So:

```
~n     nodes on level 1
~n/2   nodes on level 2
~n/4   nodes on level 3
…
~1     node  on the top level   ⇒  about log₂(n) levels total
```

- **Height** is `O(log n)` expected (geometric distribution of levels).
- **At each level you traverse `O(1/p)` = O(2) nodes** on average before dropping down.
- **Total: O(log n) expected**, for search, insert, and delete alike.

> **The reason it works without balancing:** a balanced BST *deterministically* keeps height
> `O(log n)` by rotating on every mutation. A skip list keeps height `O(log n)` **in expectation**
> by randomizing, so it never rotates, never rebalances, and never rewrites the invariant-repair
> code. **Randomization replaces rebalancing.**

**Worst case is `O(n)`** (every coin flip loses, everything lands on level 1) — but, exactly like
randomized quicksort, **no input sequence forces it**, because the levels come from a random source,
not from the data. Pugh's paper quantifies it: at n≈4096 with p=½, the chance a search takes more
than **3× the expected length is under 1 in 200 million.**

---

## Implementation

Each node holds a `next[]` array — one forward pointer per level it participates in. The engine of
every operation is the same **"walk down the levels collecting predecessors"** loop.

```java
class Skiplist {
    private static final int MAX_LEVEL = 16;
    private static final double P = 0.5;

    private final Node head = new Node(-1, MAX_LEVEL);
    private int level = 1;                       // current highest occupied level
    private final Random rng = new Random();

    private static class Node {
        int val;
        Node[] next;
        Node(int val, int level) { this.val = val; this.next = new Node[level]; }
    }

    // random level from a geometric distribution: keep flipping while heads
    private int randomLevel() {
        int lvl = 1;
        while (rng.nextDouble() < P && lvl < MAX_LEVEL) lvl++;
        return lvl;
    }

    public boolean search(int target) {
        Node cur = head;
        for (int i = level - 1; i >= 0; i--)               // top lane down to bottom
            while (cur.next[i] != null && cur.next[i].val < target)
                cur = cur.next[i];                          // skip forward on this lane
        cur = cur.next[0];
        return cur != null && cur.val == target;
    }

    public void add(int num) {
        Node[] update = new Node[MAX_LEVEL];               // predecessor on each level
        Node cur = head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) cur = cur.next[i];
            update[i] = cur;                                // remember where we dropped down
        }
        int lvl = randomLevel();
        if (lvl > level) {                                 // new node is taller than the list
            for (int i = level; i < lvl; i++) update[i] = head;
            level = lvl;
        }
        Node node = new Node(num, lvl);
        for (int i = 0; i < lvl; i++) {                    // splice in at each of its levels
            node.next[i] = update[i].next[i];
            update[i].next[i] = node;
        }
    }

    public boolean erase(int num) {
        Node[] update = new Node[MAX_LEVEL];
        Node cur = head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) cur = cur.next[i];
            update[i] = cur;
        }
        cur = cur.next[0];
        if (cur == null || cur.val != num) return false;   // not present
        for (int i = 0; i < level; i++)                    // unlink at every level it's on
            if (update[i].next[i] == cur) update[i].next[i] = cur.next[i];
        while (level > 1 && head.next[level - 1] == null) level--;   // shrink empty top lanes
        return true;
    }
}
```

**The `update[]` array is the whole pattern.** Every operation first walks the lanes top-to-bottom
recording, at each level, the last node before where `num` belongs. Search ignores it; insert and
delete splice/unlink using it. Learn that loop and you've learned the structure.

---

## Complexity

| Operation | Expected | Worst case |
|-----------|----------|-----------|
| search | **O(log n)** | O(n) |
| add | **O(log n)** | O(n) |
| erase | **O(log n)** | O(n) |
| space | **O(n)** expected (Σ n·pⁱ = n/(1−p) = 2n at p=½) | O(n log n) |

Choosing `p`: **½** minimizes expected *search cost*; **¼** uses less memory and fewer pointers per
node at a small speed cost — a real production tuning knob (LevelDB uses ¼-ish branching).

---

## Skip List vs. the Alternatives

| | Skip List | Balanced BST (AVL/RB) | B-Tree |
|---|-----------|----------------------|--------|
| Search/insert/delete | O(log n) **expected** | O(log n) **guaranteed** | O(log n) guaranteed |
| Balancing | **none — randomized** | rotations / recoloring | node splits/merges |
| Code complexity | **low** | high | high |
| **Concurrency** | **easy, lock-free-friendly** | hard (rotations touch many nodes) | moderate |
| Range scans | **natural — follow level-0** | in-order traversal | natural |
| Cache locality | poor (pointer chasing) | poor | **good (fat nodes)** |
| Worst case | O(n) (improbable) | O(log n) | O(log n) |

**The two rows that decide real-world use:** *concurrency* and *code complexity*. A skip list insert
touches only the `O(log n)` predecessors and splices with local pointer writes, so it parallelizes
far more easily than a BST whose rotations reshuffle whole subtrees. That's why concurrent sorted
structures so often pick skip lists — see the use cases.

---

## Edge Cases

| Case | Handling |
|------|----------|
| duplicates | allowed; `erase` removes **one**; search finds any |
| erase missing element | return `false`, structure unchanged |
| empty list | search returns false; level collapses back to 1 |
| everything on level 1 (unlucky) | still correct, just `O(n)` that run |
| level cap | `MAX_LEVEL ≈ log₂(maxN)` (16 handles ~65k, 32 handles billions) |

---

## Real-World Connection

Skip lists aren't a toy — they're the sorted structure of choice when you need **ordered data with
easy concurrency**:

- **Redis** implements sorted sets (`ZSET`) with a skip list — leaderboards, ranked queries, range
  scans.
- **LSM-tree memtables** (LevelDB, RocksDB, Cassandra, HBase) frequently use a skip list as the
  in-memory write buffer, precisely because it takes concurrent sorted inserts without locking the
  whole structure.

> The write path in an LSM engine — see
> [Database Scaling → Native Scale-Out](https://salman9193.github.io/system-design/#fu-database-scaling) —
> lands new keys in a skip-list memtable, then flushes it as an immutable SSTable; compaction later
> [k-way-merges](#linked-list/merge-k-sorted-lists) those SSTables. The skip list is the *in-memory
> half* of that story.

**The through-line:** a probabilistic structure that buys **simplicity and concurrency** by
accepting an *expected* rather than *guaranteed* bound — the same "randomization instead of a hard
invariant" trade behind hash tables and behind the sketching in [Klee's Algorithm](#guides/KLEES_ALGORITHM).
