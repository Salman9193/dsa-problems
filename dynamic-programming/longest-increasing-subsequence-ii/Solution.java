import java.util.Arrays;

class Solution {

    // ---------------------------------------------------------------------
    // Segment tree: point update, range max. (Bentley, 1977)
    // ---------------------------------------------------------------------
    static class SegTree {
        private final int n;
        private final int[] t;

        SegTree(int n) {
            this.n = n;
            this.t = new int[4 * n + 4];
        }

        void update(int pos, int val) { update(1, 1, n, pos, val); }

        private void update(int u, int l, int r, int pos, int val) {
            if (l == r) {
                t[u] = Math.max(t[u], val);
                return;
            }
            int mid = (l + r) >>> 1;
            if (pos <= mid) update(2 * u, l, mid, pos, val);
            else update(2 * u + 1, mid + 1, r, pos, val);
            t[u] = Math.max(t[2 * u], t[2 * u + 1]);
        }

        int query(int ql, int qr) {
            if (ql > qr) return 0;
            return query(1, 1, n, ql, qr);
        }

        private int query(int u, int l, int r, int ql, int qr) {
            if (qr < l || r < ql) return 0;
            if (ql <= l && r <= qr) return t[u];
            int mid = (l + r) >>> 1;
            return Math.max(query(2 * u, l, mid, ql, qr),
                            query(2 * u + 1, mid + 1, r, ql, qr));
        }
    }

    // ---------------------------------------------------------------------
    // Longest Increasing Subsequence II — LeetCode #2407
    //
    // Strictly increasing subsequence, adjacent elements differing by at most k.
    //
    // Key the DP by VALUE (not position):
    //     dp[x] = 1 + max( dp[x-k .. x-1] )     <- range max over a contiguous value window
    //
    // Iterate nums in ARRAY order (that enforces "subsequence"); the segment tree indexes the
    // VALUE axis and answers the window max in O(log M).
    //
    // Patience sorting does NOT work here: the k-window breaks the "a smaller tail is always at
    // least as extendable" property that the greedy relies on (a smaller tail can fall below
    // x-k and be unusable). We reach O(n log n) by a different lever: INTERVAL STRUCTURE.
    //
    // Time: O(n log M).  Space: O(M).
    // ---------------------------------------------------------------------
    public int lengthOfLIS(int[] nums, int k) {
        int max = 0;
        for (int x : nums) max = Math.max(max, x);

        SegTree seg = new SegTree(max);
        int best = 0;
        for (int x : nums) {
            int lo = Math.max(1, x - k);
            int prev = seg.query(lo, x - 1);      // best chain endable at a value in [x-k, x-1]
            int cur = prev + 1;
            seg.update(x, cur);                   // point update at value x
            best = Math.max(best, cur);
        }
        return best;
    }

    // ---------------------------------------------------------------------
    // Variant — "Longest Upgrade Path"
    //
    // Release i has version v[i] (distinct) and m[i] = the oldest version it can upgrade
    // directly from. Upgrade j -> i is legal iff  m[i] <= v[j] < v[i].
    // Find the longest chain of successive upgrades.
    //
    // This is #2407 with a PER-ELEMENT window bound instead of a fixed width k:
    //     dp[v[i]] = 1 + max( dp over the value range [ m[i], v[i]-1 ] )
    // (#2407 is the special case m[i] = v[i] - k.)
    //
    // Sort by version — that is a topological order, since every edge goes low -> high — then
    // run the identical segment-tree range-max, coordinate-compressed.
    //
    // NOTE: this relation is NOT transitive. With v=[1,5,10], m=[0,0,5]: 1->5 and 5->10 are both
    // legal, but 1->10 is NOT (release 10 dropped support for v1). So this is a longest PATH in
    // a DAG, not a longest chain in a poset — you must hop through the intermediate release.
    // The DP is unchanged, because longest-path-in-a-DAG never required transitivity.
    //
    // Time: O(n log n).  Space: O(n).
    // ---------------------------------------------------------------------
    public int longestUpgradePath(int[] v, int[] m) {
        int n = v.length;
        if (n == 0) return 0;

        int[] vals = v.clone();                   // coordinate compression on the value axis
        Arrays.sort(vals);

        Integer[] order = new Integer[n];         // process in ascending version = topological order
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (a, b) -> Integer.compare(v[a], v[b]));

        SegTree seg = new SegTree(n);
        int best = 0;
        for (int i : order) {
            int lo = lowerBound(vals, m[i]) + 1;       // first version >= m[i]      (1-indexed)
            int hi = lowerBound(vals, v[i]);           // last version  <  v[i]      (1-indexed)
            int prev = seg.query(lo, hi);
            int cur = prev + 1;
            seg.update(lowerBound(vals, v[i]) + 1, cur);
            best = Math.max(best, cur);
        }
        return best;
    }

    // first index with a[idx] >= target
    private int lowerBound(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

/*
 * Trace (#2407) — nums = [4,2,1,4,3,4,5,8,15], k = 3
 * --------------------------------------------------
 *    x=4  window [1,3]  max 0 -> dp[4]=1
 *    x=2  window [1,1]  max 0 -> dp[2]=1
 *    x=1  window empty         -> dp[1]=1
 *    x=4  window [1,3]  max 1 -> dp[4]=2
 *    x=3  window [1,2]  max 1 -> dp[3]=2
 *    x=4  window [1,3]  max 2 -> dp[4]=3
 *    x=5  window [2,4]  max 3 -> dp[5]=4
 *    x=8  window [5,7]  max 4 -> dp[8]=5
 *    x=15 window [12,14] max 0 -> dp[15]=1     (stranded: nothing within 3 below it)
 *  answer 5   ([1,3,4,5,8])
 *
 * Why not patience sorting: the k-window breaks "a smaller tail is always at least as
 * extendable" — a smaller tail may fall below x-k and be USELESS. The greedy's core assumption
 * is not merely unreliable here, it is backwards. Hence the segment tree.
 */
