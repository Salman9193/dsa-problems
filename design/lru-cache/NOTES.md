# LRU Cache — Notes & Intuition

**LeetCode #146** | Design / Linked List + HashMap | Medium

---

## Problem

Design a data structure that follows the Least Recently Used (LRU)
cache eviction policy:

- `get(key)` — return the value if it exists; mark as recently used
- `put(key, value)` — insert or update; evict the LEAST RECENTLY USED
  key if over capacity

Both operations must be O(1).

---

## Why This Requires Two Data Structures

HashMap alone: O(1) lookup, but can't track access ORDER.
Linked list alone: O(1) move-to-front, but O(n) lookup by key.

Together:
```
HashMap:          key → Node pointer        O(1) lookup
Doubly Linked List: MRU ← head ↔ ... ↔ tail → LRU
                  O(1) move-to-front (with prev pointer)
                  O(1) evict tail (LRU)
```

---

## The Data Structure

```
Dummy head ↔ [MRU node] ↔ [node] ↔ ... ↔ [LRU node] ↔ Dummy tail
```

- Head sentinel: always `head.next` = most recently used
- Tail sentinel: always `tail.prev` = least recently used
- Dummy nodes eliminate boundary checks

---

## Four Pointer Operations on Insert

```
BEFORE:   head ↔ A ↔ B ↔ tail
INSERT X at front:
  head ↔ X ↔ A ↔ B ↔ tail

Steps (ORDER MATTERS):
  1. X.next = head.next   (X → A)
  2. X.prev = head        (X ← head)
  3. head.next.prev = X   (A ← X)   ← must use head.next BEFORE step 4!
  4. head.next = X        (head → X)
```

If steps 3 and 4 are swapped, `head.next` in step 3 would already point
to X (from step 4), creating a cycle.

---

## Full Operations

```java
get(key):
  node = map.get(key)
  remove(node); insertAtFront(node)   // move to front = recent
  return node.val

put(key, value):
  if exists:
    update node.val; move to front
  else:
    create new node; insert at front; add to map
    if size > capacity:
      evict = tail.prev       // LRU is just before dummy tail
      remove(evict); map.remove(evict.key)
```

---

## Trace — capacity=2

```
put(1,1): list=[1],       map={1:node1}
put(2,2): list=[2,1],     map={1,2}
get(1):   list=[1,2],     map={1,2}   ← 1 moved to front
put(3,3): list=[3,1],     map={1,3}   ← 2 evicted (LRU)
get(2):   return -1                    ← 2 was evicted
put(4,4): list=[4,3],     map={3,4}   ← 1 evicted (LRU)
get(1):   return -1
get(3):   return 3         list=[3,4]
get(4):   return 4         list=[4,3]
```

---

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| get() | O(1) | — |
| put() | O(1) | O(capacity) |

---

## Why Not Use Java's LinkedHashMap?

Java's `LinkedHashMap` with `accessOrder=true` implements LRU in one class.
But in interviews, you're expected to implement from scratch — it demonstrates
understanding of the underlying data structures.

---

## Extensions

| Variant | Approach |
|---------|---------|
| LFU Cache (#460) | Two HashMaps + doubly linked list; track frequency counts |
| Thread-safe LRU | Lock on get/put; or ConcurrentHashMap + ConcurrentLinkedQueue |
| LRU with TTL expiry | Add timestamp to node; lazy expiry on access |
| Distributed LRU | Consistent hashing + per-shard LRU |
| LRU with O(1) getMin | Add min-tracking alongside LRU |

---

## Real-World Usage

- CPU L1/L2 cache eviction (hardware)
- Browser cache (recent pages)
- CDN edge nodes (recently served assets)
- Redis maxmemory-policy allkeys-lru
- PostgreSQL buffer pool page eviction
- OS page replacement algorithms
