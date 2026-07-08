class Solution {

    // Maximum of Absolute Value Expression — LeetCode #1131
    //
    // Maximise over all i, j:
    //   |arr1[i]-arr1[j]| + |arr2[i]-arr2[j]| + |i-j|
    //
    // Reframe: each index k is a 3D point (arr1[k], arr2[k], k); the expression is the
    // Manhattan (L1) distance between two such points, so we want the L1 diameter.
    //
    // Key identity:  |a|+|b|+|c| = max over s1,s2,s3 in {+1,-1} of (s1*a + s2*b + s3*c).
    // For a fixed sign combo, define f(k) = s1*arr1[k] + s2*arr2[k] + s3*k; the best
    // pair for that combo is  max(f) - min(f)  (a single linear pass).
    //
    // Negating all three signs gives the same max-min, so only 4 combos matter:
    // fix s1 = +1 and vary s2, s3 in {+1, -1}.
    //
    // Time: O(4n) = O(n).  Space: O(1).
    public int maxAbsValExpr(int[] arr1, int[] arr2) {
        int n = arr1.length;
        int ans = 0;

        for (int s2 = 1; s2 >= -1; s2 -= 2) {          // s2 = +1 then -1
            for (int s3 = 1; s3 >= -1; s3 -= 2) {      // s3 = +1 then -1  (s1 fixed = +1)
                int mx = Integer.MIN_VALUE;
                int mn = Integer.MAX_VALUE;
                for (int k = 0; k < n; k++) {
                    int val = arr1[k] + s2 * arr2[k] + s3 * k;
                    mx = Math.max(mx, val);
                    mn = Math.min(mn, val);
                }
                ans = Math.max(ans, mx - mn);
            }
        }
        return ans;
    }
}

/*
 * Trace — arr1 = [1,2,3,4], arr2 = [-1,4,5,6]
 * -------------------------------------------
 * Combo (s1,s2,s3) = (+,+,+):  f(k) = arr1[k] + arr2[k] + k
 *   k=0: 1 + (-1) + 0 = 0
 *   k=1: 2 +  4   + 1 = 7
 *   k=2: 3 +  5   + 2 = 10
 *   k=3: 4 +  6   + 3 = 13
 *   max(f) - min(f) = 13 - 0 = 13     (pair i=3, j=0)
 *
 * The other three combos give smaller spreads, so answer = 13.  Correct.
 *
 * Why the sign trick is valid
 * ---------------------------
 * |x| = max(x, -x), and the three absolute values are independent, so choosing each
 * sign to make its term positive recovers the full sum of magnitudes. Taking the max
 * over all sign patterns therefore equals the sum of absolute values. Because the outer
 * objective is also a max over (i, j), the two maxes commute, and each sign pattern can
 * be evaluated separately as a simple max - min sweep over f(k).
 */
