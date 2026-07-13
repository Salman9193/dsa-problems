import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {

    // Russian Doll Envelopes — LeetCode #354
    //
    // Nesting requires BOTH width and height strictly greater — a 2-D condition.
    //
    // Trick: sort by width ascending, and for EQUAL widths sort height DESCENDING.
    // Then the width condition is handled by the ordering, and the answer is just the
    // longest strictly-increasing subsequence of the heights.
    //
    // Why height DESCENDING on ties: two envelopes of equal width can never nest. If equal
    // widths were sorted by height ascending, their heights would form an increasing run and
    // LIS would chain them — an illegal nesting. Sorting heights descending makes that run
    // decreasing, so an increasing subsequence structurally cannot pick two of them.
    //
    // Time: O(n log n).  Space: O(n).
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes == null || envelopes.length == 0) return 0;

        Arrays.sort(envelopes, (a, b) ->
                a[0] != b[0] ? Integer.compare(a[0], b[0])   // width ascending
                             : Integer.compare(b[1], a[1])); // height DESCENDING on ties

        // strict LIS on heights via patience sorting
        List<Integer> tails = new ArrayList<>();
        for (int[] e : envelopes) {
            int h = e[1];
            int pos = lowerBound(tails, h);      // first tail >= h  => strictly increasing
            if (pos == tails.size()) tails.add(h);
            else tails.set(pos, h);
        }
        return tails.size();
    }

    // first index with list[idx] >= target
    private int lowerBound(List<Integer> list, int target) {
        int lo = 0, hi = list.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (list.get(mid) < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
}

/*
 * Trace — [[5,4],[6,4],[6,7],[2,3]]
 * ---------------------------------
 *   sorted (width asc, height desc): [2,3], [5,4], [6,7], [6,4]
 *   heights: 3, 4, 7, 4
 *     3 -> tails [3]
 *     4 -> tails [3,4]
 *     7 -> tails [3,4,7]
 *     4 -> lowerBound(4) = 1 -> tails [3,4,7]  (unchanged)
 *   answer = 3     ([2,3] in [5,4] in [6,7])
 *
 *   Note [6,4] could NOT extend [6,7] — the descending tiebreak placed [6,7] first, so the
 *   two width-6 envelopes appear as a DECREASING height run and cannot both be chosen.
 *
 * The tiebreak is not cosmetic — with height ASCENDING on ties:
 *   [[4,5],[4,6],[6,7],[2,3],[1,1]]  -> returns 5, but the correct answer is 4.
 *
 * Poset view: nesting is a 2-DIMENSIONAL partial order (width x height). Sorting collapses
 * one dimension, leaving a total-order LIS on the other — which is why the O(n log n) patience
 * greedy applies here but NOT to Largest Divisible Subset (#368), whose divisibility order is
 * not 2-dimensional.
 */
