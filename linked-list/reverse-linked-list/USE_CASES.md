# Reverse Linked List — Real-World Use Cases

In-place pointer reversal is the foundation of two of the most important
data structures in systems programming: LRU caches and the Linux kernel's
generic linked list.

---

## 1. LRU Cache — Move-to-Front via Pointer Rewiring

The Least Recently Used (LRU) cache is implemented with a doubly linked
list + HashMap. Every cache access moves the accessed node to the front
of the list (most recently used position) and evicts the tail node (least
recently used) when capacity is exceeded.

"Move to front" = remove node from current position + insert at head.
Both operations are pointer rewiring — the same skill as reversing a
linked list, applied to a doubly linked list.

```
LRU Cache state (MRU → LRU):  A ↔ B ↔ C ↔ D
Access C:
  Remove C:  A ↔ B ↔ D      (rewire B.next=D, D.prev=B)
  Insert at head: C ↔ A ↔ B ↔ D  (rewire C.next=A, A.prev=C)
```

This is the same three-pointer rewiring as reversing a linked list —
save pointers, redirect next/prev, advance. The pointer manipulation
skills transfer directly.

### Where LRU caches are used in production

- **CPU L1/L2/L3 cache eviction policy** — hardware implementations
- **Browser cache** (Chrome, Firefox) — recently visited pages
- **CDN edge nodes** (Cloudflare, Akamai) — recently served assets
- **Database buffer pool** (PostgreSQL, MySQL InnoDB) — recently read pages
- **Redis** — `maxmemory-policy allkeys-lru`

### References

- **LeetCode #146 LRU Cache** — canonical implementation combining
  doubly linked list + HashMap.
  "Every time we interact with a node item in the cache, we remove it
  from its current position and move it to the front. This requires the
  same pointer manipulation as reversing a linked list."

- **GeeksforGeeks — LRU Cache using Doubly Linked List:**
  https://www.geeksforgeeks.org/dsa/lru-cache-implementation-using-double-linked-lists/
  "The doubly linked list allows constant-time access to both ends —
  retrieving MRU or LRU item. Eviction removes the tail node."

- **LRU Cache pointer pitfalls:**
  https://neetcode.io/solutions/lru-cache
  "When removing a node, you must update both prev.next and next.prev.
  When inserting, you must update four pointers: the new node's prev and
  next, plus the adjacent nodes' pointers. Missing any update corrupts
  the list structure."

---

## 2. Linux Kernel — Generic Circular Doubly Linked List (`linux/list.h`)

The Linux kernel uses a single generic circular doubly linked list
implementation (`include/linux/list.h`) for virtually all internal
data structures — process lists, device lists, file system inodes,
network socket queues, and more.

The kernel's approach is unique: instead of making data structures
into linked lists, it embeds a `list_head` node inside the struct:

```c
struct list_head {
    struct list_head *next;
    struct list_head *prev;
};

struct fox {
    unsigned long tail_length;
    unsigned long weight;
    bool is_fantastic;
    struct list_head list;  // embedded list node
};
```

The kernel provides `list_add()`, `list_del()`, `list_move()`,
`list_reverse_poison()` — all implemented as the same next/prev
pointer rewiring taught by the reverse linked list problem.

`list_reverse_poison()` directly implements the three-pointer reversal
algorithm for reversing a kernel list — identical to the iterative
approach in this problem.

### References

- **Linux kernel source — `include/linux/list.h`:**
  https://github.com/torvalds/linux/blob/master/include/linux/list.h
  Contains `list_add()`, `list_del()`, `list_move()`,
  `list_reverse_poison()` — all built on next/prev pointer manipulation.

- **Linux Kernel Development (Robert Love), Chapter 6:**
  "The Linux kernel's linked list implementation is fundamentally a
  circular doubly linked list. The kernel provides a family of routines
  to manipulate linked lists — list_add(), list_del() — all based on
  the same next/prev pointer rewiring as linked list reversal."

- **Kernel list_head usage:**
  "The utility is in how the list_head structure is used — embedding it
  inside data structures rather than wrapping them. This allows a single
  generic implementation to serve all kernel data structures."

---

## The Shared Pointer Pattern

Both LRU cache and Linux kernel lists use the same three-step pointer
operation that reverse linked list teaches:

```
1. Save affected pointers before overwriting
2. Redirect next/prev pointers to new positions
3. Update all adjacent nodes' pointers to maintain list integrity
```

| System | Operation | Pointers updated |
|--------|-----------|-----------------|
| Reverse Linked List | Full reversal | curr.next = prev (n times) |
| LRU Cache move-to-front | Remove + insert | 4 pointers per operation |
| Linux kernel list_del | Node removal | prev.next and next.prev |
| Linux kernel list_add | Node insertion | 4 pointers |

---

## Further Reading

- Linux kernel list.h: https://github.com/torvalds/linux/blob/master/include/linux/list.h
- GeeksforGeeks LRU with DLL: https://www.geeksforgeeks.org/dsa/lru-cache-implementation-using-double-linked-lists/
- LRU pointer pitfalls: https://neetcode.io/solutions/lru-cache
- In-place reversal pattern: https://emre.me/coding-patterns/in-place-reversal-of-a-linked-list/
