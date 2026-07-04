import java.util.*;

// Approach: Segment Tree (array-backed) — O(log n) update, O(log n) range query
//
// The problem: support two operations on an array, both fast:
//   update(i, val)      — set nums[i] = val
//   sumRange(i, j)      — sum of nums[i..j]
//
// Naive array: update O(1) but query O(n).
// Prefix sums:  query O(1) but update O(n) (must rebuild the suffix).
// Segment tree: BOTH O(log n) — the right trade-off when updates AND queries
//               are both frequent.
//
// A segment tree stores aggregate values (here, sums) over ranges. Each leaf
// holds one element; each internal node holds the sum of its two children's
// ranges. Updating one leaf touches only the O(log n) nodes on the path to the
// root; a range query combines O(log n) canonical segments that tile [i..j].

class NumArray {

    private final int[] tree;   // segment tree: tree[1] is root over [0, n-1]
    private final int n;

    public NumArray(int[] nums) {
        n = nums.length;
        tree = new int[2 * n];  // iterative segment tree needs exactly 2n slots

        // Leaves live in tree[n .. 2n-1]; copy the input there.
        for (int i = 0; i < n; i++) tree[n + i] = nums[i];

        // Build internal nodes bottom-up: each parent = sum of its two children.
        for (int i = n - 1; i >= 1; i--) {
            tree[i] = tree[2 * i] + tree[2 * i + 1];
        }
    }

    public void update(int index, int val) {
        int pos = index + n;        // map array index to its leaf position
        tree[pos] = val;            // set the leaf

        // Walk up to the root, refreshing each ancestor's sum.
        while (pos > 1) {
            int parent = pos / 2;
            tree[parent] = tree[2 * parent] + tree[2 * parent + 1];
            pos = parent;
        }
    }

    public int sumRange(int left, int right) {
        int sum = 0;
        int l = left + n;           // leaf position of left boundary
        int r = right + n + 1;      // one PAST the leaf of right (half-open [l, r))

        // Classic iterative range query. At each level:
        //   - if l is a RIGHT child, it isn't fully covered by its parent's
        //     range within [l, r), so add it and step inward (l++).
        //   - if r is a RIGHT child, its left sibling r-1 is inside [l, r),
        //     so step r-- and add tree[r].
        // Then move both up a level (l /= 2, r /= 2).
        while (l < r) {
            if ((l & 1) == 1) sum += tree[l++];   // l is a right child
            if ((r & 1) == 1) sum += tree[--r];   // r is a right child
            l /= 2;
            r /= 2;
        }
        return sum;
    }
}

/*
 * Complexity
 * ----------
 * Constructor:  O(n) time, O(n) space (2n array)
 * update:       O(log n) — one leaf-to-root path
 * sumRange:     O(log n) — at most 2 nodes per level, log n levels
 *
 * Why 2n slots (iterative form)
 * -----------------------------
 * Leaves occupy tree[n .. 2n-1]. Internal nodes occupy tree[1 .. n-1], with
 * tree[1] the root. For any node i, its children are 2i and 2i+1, and its
 * parent is i/2 — the same index arithmetic as a binary heap.
 *
 * Trace — nums = [1, 3, 5], n = 3, tree has 6 slots
 * -------------------------------------------------
 * Place leaves:   tree[3]=1  tree[4]=3  tree[5]=5
 * Build up:
 *   i=2: tree[2] = tree[4] + tree[5] = 3 + 5 = 8
 *   i=1: tree[1] = tree[2] + tree[3] = 8 + 1 = 9   (root = total sum)
 *
 * sumRange(0, 2):  l = 0+3 = 3,  r = 2+3+1 = 6
 *   l=3 (right child): sum += tree[3]=1 -> sum=1, l=4; r=6, l=4/2=2, r=6/2=3
 *   l=2 < r=3:  l even (skip), r odd? r=3 -> sum += tree[--r=2]=8 -> sum=9, r=2
 *   l=2/2=1, r=2/2=1 -> l==r, stop.  Answer = 9 ✓  (1+3+5)
 *
 * update(1, 2):  pos = 1+3 = 4, tree[4]=2
 *   parent 2: tree[2] = tree[4]+tree[5] = 2+5 = 7
 *   parent 1: tree[1] = tree[2]+tree[3] = 7+1 = 8
 *   Now sumRange(0,2) would return 8 ✓  (1+2+5)
 *
 * Recursive form (easier to extend to min/max/gcd or lazy propagation) uses a
 * 4n array and a build/query/update recursion over [lo, hi] with a mid split.
 * The iterative form above is the most compact for pure sum queries.
 */
