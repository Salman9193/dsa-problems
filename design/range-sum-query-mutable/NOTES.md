# Range Sum Query - Mutable — Notes & Intuition

**LeetCode #307** | Design / Segment Tree | Medium

---

## Problem

Design a structure over an integer array supporting two operations, both fast:

- `update(index, val)` — set `nums[index] = val`
- `sumRange(left, right)` — return the sum of `nums[left..right]` inclusive

Both operations are called many times, interleaved.

> This is the canonical "segment tree" interview problem. Google has asked it
> in phone screens, and candidates report the interviewer was specifically
> looking for a segment tree — not a prefix-sum or brute-force answer.

---

## Why Not the Obvious Approaches

| Approach | update | sumRange | Verdict |
|----------|--------|----------|---------|
| Plain array | O(1) | O(n) | Query too slow with many range reads |
| Prefix sums | O(n) | O(1) | Update too slow — must rebuild the suffix |
| **Segment tree** | **O(log n)** | **O(log n)** | Balanced — both operations fast |
| Fenwick / BIT | O(log n) | O(log n) | Also valid, less flexible than segment tree |

When **both** updates and range queries are frequent, you need a structure
that's logarithmic on both — a segment tree (or Fenwick tree).

---

## What a Segment Tree Is

A binary tree where:
- each **leaf** holds one array element
- each **internal node** holds the aggregate (here, the sum) of its two
  children's ranges
- the **root** holds the aggregate over the whole array

```
nums = [1, 3, 5]

              9            <- root: sum[0..2]
             / \
            8   1          <- sum[1..2]=8, sum[0..0]=1
           / \
          3   5            <- leaves
```

Because the tree has height O(log n), any single-element update or any range
query touches only O(log n) nodes.

---

## Array-Backed (Iterative) Layout

Store the tree in an array `tree[2n]`:
- leaves at `tree[n .. 2n-1]`
- internal nodes at `tree[1 .. n-1]`, root at `tree[1]`
- node `i` has children `2i` and `2i+1`, parent `i/2` (heap indexing)

### Build — O(n)

```java
for (int i = 0; i < n; i++) tree[n + i] = nums[i];      // place leaves
for (int i = n - 1; i >= 1; i--)                        // build parents up
    tree[i] = tree[2*i] + tree[2*i + 1];
```

### Update — O(log n)

```java
int pos = index + n;
tree[pos] = val;
while (pos > 1) {                    // refresh ancestors up to the root
    int parent = pos / 2;
    tree[parent] = tree[2*parent] + tree[2*parent + 1];
    pos = parent;
}
```

### Range Query — O(log n)

```java
int l = left + n, r = right + n + 1;   // half-open [l, r)
int sum = 0;
while (l < r) {
    if ((l & 1) == 1) sum += tree[l++];  // l is a right child -> include it
    if ((r & 1) == 1) sum += tree[--r];  // r-1 is inside -> include it
    l /= 2; r /= 2;
}
```

The query walks up the tree, at each level grabbing at most one node on each
side that is a "canonical segment" fully inside `[left, right]`.

---

## Trace — nums = [1, 3, 5]

```
Build:  tree[3]=1 tree[4]=3 tree[5]=5
        tree[2] = 3+5 = 8
        tree[1] = 8+1 = 9   (root)

sumRange(0,2): l=3, r=6
  l=3 odd -> sum += tree[3]=1 (sum=1), l=4;  l=4/2=2, r=6/2=3
  l=2<r=3: r=3 odd -> sum += tree[--r=2]=8 (sum=9), r=2;  l=1, r=1 stop
  => 9  (1+3+5) ✓

update(1,2): tree[4]=2
  parent2: tree[2]=2+5=7
  parent1: tree[1]=7+1=8
  sumRange(0,2) now returns 8  (1+2+5) ✓
```

---

## Recursive Form (When to Prefer It)

The iterative version above is compact for pure sums. Switch to the
**recursive** form (4n array, build/query/update over `[lo, hi]` with a mid
split) when you need:

- a different merge (min, max, gcd) — just change the combine step
- **lazy propagation** for O(log n) *range updates* (add v to all of `[l, r]`)
- coordinate compression or persistence

```java
// recursive query sketch
int query(int node, int lo, int hi, int l, int r) {
    if (r < lo || hi < l) return 0;             // no overlap
    if (l <= lo && hi <= r) return tree[node];  // full overlap
    int mid = (lo + hi) / 2;                     // partial: split
    return query(2*node,   lo,    mid, l, r)
         + query(2*node+1, mid+1, hi,  l, r);
}
```

---

## Segment Tree vs Fenwick (BIT)

| | Segment Tree | Fenwick / BIT |
|--|-------------|---------------|
| Range sum + point update | ✓ | ✓ |
| Range min / max / gcd | ✓ | ✗ (sum-like only) |
| Range update (lazy) | ✓ | Harder (needs 2 BITs) |
| Code size | Larger | Very small |
| Memory | 2n–4n | n+1 |

Use Fenwick when you only need prefix/range **sums**; use a segment tree when
you need other aggregates or range updates.

---

## Edge Cases

| Case | Handling |
|------|----------|
| Single element array | Root is the leaf; query/update trivially O(1) |
| Query full range `[0, n-1]` | Returns `tree[1]` after at most log n steps |
| Repeated updates same index | Each is independent O(log n) |
| Negative values | Sums handle them; no special case |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Range **min/max** query | Different aggregate | Swap `+` for `Math.min` / `Math.max` in build/update/query |
| Range **update** + point query (#370) | Add v to `[l, r]` | Lazy propagation, or difference array + BIT |
| Range update + range query | Both ranged | Segment tree with lazy propagation |
| **Count of Smaller After Self** (#315) | Rank queries | Segment tree / BIT over value space with coordinate compression |
| **Count of Range Sum** (#327) | Range-sum counting | Merge sort or segment tree over prefix sums |
| **The Skyline Problem** (#218) | Interval max | Segment tree / sweep line with a max-heap |
| 2D range sum, mutable (#308) | 2D grid | 2D BIT or segment tree of segment trees |

**Trade-off:** the iterative sum tree is the shortest correct answer under
interview time pressure. If the follow-up asks for range updates or a
non-sum aggregate, reach for the recursive form with lazy propagation — say
so proactively; it signals you know the structure's full range.
