# Segment Tree — Complete Pattern Guide

A segment tree answers **range aggregate queries** (sum, min, max, gcd, ...)
and supports **point or range updates**, both in `O(log n)`. It is the
structure to reach for when data changes *and* you still need fast range
analytics — the case where plain arrays (slow query) and prefix sums (slow
update) both fail.

---

## When to Recognise It

Look for a problem that asks for **both**:

- a query over a *range* — "sum / min / max of `[i..j]`", and
- *updates* to individual elements or ranges,

where a brute-force query would be `O(n)`. If updates and range queries are
both frequent, the intended answer is almost always a segment tree (or its
lighter cousin, the Fenwick / binary indexed tree).

| Structure | update | range query | range update |
|-----------|--------|-------------|--------------|
| Plain array | O(1) | O(n) | O(n) |
| Prefix sums | O(n) | O(1) | O(n) |
| **Segment tree** | **O(log n)** | **O(log n)** | O(log n) w/ lazy |
| Fenwick (BIT) | O(log n) | O(log n) | harder (2 BITs) |

---

## The Structure

A binary tree where each node covers a contiguous range:

```
nums = [1, 3, 5, 7]

               16              node covers [0..3]  (sum = 16)
              /  \
            4      12          [0..1]=4     [2..3]=12
           / \    /  \
          1   3  5    7        leaves
```

- **Leaf** `i` holds `nums[i]`.
- **Internal node** holds the aggregate of its children's ranges.
- **Root** holds the aggregate over the whole array.
- Height is `O(log n)`, so any single update or range query touches only
  `O(log n)` nodes.

---

## Iterative Array Form (Compact, Sum)

Store the tree in `int[2n]`: leaves at `tree[n .. 2n-1]`, internal nodes at
`tree[1 .. n-1]`, root at `tree[1]`. For node `i`: children `2i`, `2i+1`;
parent `i/2` (heap indexing).

```java
int[] tree; int n;

void build(int[] nums) {
    n = nums.length;
    tree = new int[2 * n];
    for (int i = 0; i < n; i++) tree[n + i] = nums[i];      // leaves
    for (int i = n - 1; i >= 1; i--)                        // parents up
        tree[i] = tree[2*i] + tree[2*i + 1];
}

void update(int index, int val) {
    int pos = index + n;
    tree[pos] = val;
    while (pos > 1) {                    // refresh ancestors to the root
        int parent = pos / 2;
        tree[parent] = tree[2*parent] + tree[2*parent + 1];
        pos = parent;
    }
}

int query(int left, int right) {         // inclusive [left, right]
    int l = left + n, r = right + n + 1;  // half-open [l, r)
    int sum = 0;
    while (l < r) {
        if ((l & 1) == 1) sum += tree[l++];   // l is a right child
        if ((r & 1) == 1) sum += tree[--r];   // r-1 is inside
        l /= 2; r /= 2;
    }
    return sum;
}
```

---

## Recursive Form (Flexible — min/max/gcd, Lazy)

Prefer this when you need a different aggregate or **range updates**. Uses a
`4n` array and a `[lo, hi]` range with a mid split.

```java
int[] tree = new int[4 * n];

void build(int[] a, int node, int lo, int hi) {
    if (lo == hi) { tree[node] = a[lo]; return; }
    int mid = (lo + hi) / 2;
    build(a, 2*node,   lo,    mid);
    build(a, 2*node+1, mid+1, hi);
    tree[node] = tree[2*node] + tree[2*node+1];    // combine (swap for min/max)
}

int query(int node, int lo, int hi, int l, int r) {
    if (r < lo || hi < l) return 0;                // no overlap (identity)
    if (l <= lo && hi <= r) return tree[node];     // full overlap
    int mid = (lo + hi) / 2;                         // partial: split
    return query(2*node,   lo,    mid, l, r)
         + query(2*node+1, mid+1, hi,  l, r);
}

void update(int node, int lo, int hi, int idx, int val) {
    if (lo == hi) { tree[node] = val; return; }
    int mid = (lo + hi) / 2;
    if (idx <= mid) update(2*node,   lo,    mid, idx, val);
    else            update(2*node+1, mid+1, hi,  idx, val);
    tree[node] = tree[2*node] + tree[2*node+1];
}
```

Change the **combine** (`+`) and the **no-overlap identity** (`0` for sum,
`+INF` for min, `-INF` for max) to switch aggregate.

---

## Lazy Propagation — O(log n) Range Updates

To add a value to *every* element of a range in `O(log n)`, defer the update:
store a pending delta in a `lazy[]` array and push it down only when needed.

```java
int[] tree = new int[4*n], lazy = new int[4*n];

void pushDown(int node, int lo, int hi) {
    if (lazy[node] != 0) {
        int mid = (lo + hi) / 2;
        apply(2*node,   lo,    mid, lazy[node]);
        apply(2*node+1, mid+1, hi,  lazy[node]);
        lazy[node] = 0;
    }
}
void apply(int node, int lo, int hi, int delta) {
    tree[node] += (hi - lo + 1) * delta;   // sum over the covered length
    lazy[node] += delta;
}
void rangeUpdate(int node, int lo, int hi, int l, int r, int delta) {
    if (r < lo || hi < l) return;
    if (l <= lo && hi <= r) { apply(node, lo, hi, delta); return; }
    pushDown(node, lo, hi);
    int mid = (lo + hi) / 2;
    rangeUpdate(2*node,   lo,    mid, l, r, delta);
    rangeUpdate(2*node+1, mid+1, hi,  l, r, delta);
    tree[node] = tree[2*node] + tree[2*node+1];
}
```

---

## Segment Tree vs Fenwick Tree (BIT)

| | Segment Tree | Fenwick / BIT |
|--|-------------|---------------|
| Range **sum** + point update | ✓ | ✓ (smallest code) |
| Range **min / max / gcd** | ✓ | ✗ (sum-like only) |
| Range update (lazy) | ✓ | Harder — needs two BITs |
| Memory | 2n–4n | n+1 |
| When to pick | Any aggregate, range updates | Pure prefix/range sums |

**Rule of thumb:** Fenwick if you only need sums; segment tree the moment you
need min/max/gcd or range updates.

---

## Core Problems

| # | Problem | What it drills |
|---|---------|----------------|
| #307 | Range Sum Query - Mutable | The canonical build — point update, range sum |
| #308 | Range Sum Query 2D - Mutable | 2D segment tree / 2D BIT |
| #315 | Count of Smaller Numbers After Self | BIT/segment tree over value space + coord compression |
| #327 | Count of Range Sum | Merge sort or segment tree over prefix sums |
| #218 | The Skyline Problem | Sweep line + segment tree of heights |
| #699 | Falling Squares | Segment tree with range max + lazy |
| #732 | My Calendar III | Range add + range max (booking overlaps) |

---

## Interview Tips

1. **State the trade-off first.** "Array is O(n) query, prefix sums are O(n)
   update; since both are frequent I'll use a segment tree for O(log n) both."
   That one sentence signals you know *why* the structure fits.

2. **Pick the form to the follow-up.** Iterative sum tree for pure range sums;
   recursive form the moment min/max or range updates come up. Mention lazy
   propagation proactively when the follow-up says "add v to a whole range".

3. **Know the identity element.** Sum → 0, min → +∞, max → −∞, gcd → 0. Getting
   the no-overlap return value right is the most common bug.

4. **Coordinate compression** is the bridge from "range sum" to counting
   problems (#315, #327): map values to ranks, then run the tree over ranks.
