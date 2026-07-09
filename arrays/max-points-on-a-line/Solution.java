import java.util.HashMap;
import java.util.Map;

class Solution {

    // Max Points on a Line — LeetCode #149
    //
    // Fix each point as an anchor and group the others by the slope from the anchor.
    // Points sharing a slope through the anchor are collinear with it, so the largest
    // slope group + the anchor is the best line through that anchor.
    //
    // Slope is stored as a GCD-reduced integer pair (dx, dy) with a canonical sign, NOT
    // as a floating-point number (which loses precision). Scanning j > i suffices: a line
    // with k points is found when its lowest-index point is the anchor.
    //
    // Time: O(n^2).  Space: O(n).  (Points are guaranteed unique.)
    public int maxPoints(int[][] points) {
        int n = points.length;
        if (n <= 2) return n;

        int best = 2;                                  // any two points form a line
        for (int i = 0; i < n; i++) {
            Map<Long, Integer> slope = new HashMap<>();
            int localMax = 0;
            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                int g = gcd(dx, dy);                   // g >= 1 (unique points)
                dx /= g;
                dy /= g;
                if (dx < 0 || (dx == 0 && dy < 0)) {   // canonical sign
                    dx = -dx;
                    dy = -dy;
                }
                long key = (long) dx * 200003L + dy;   // collision-free encoding
                int c = slope.merge(key, 1, Integer::sum);
                localMax = Math.max(localMax, c);
            }
            best = Math.max(best, localMax + 1);       // + the anchor itself
        }
        return best;
    }

    private int gcd(int a, int b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }
}

/*
 * Why integer (dx, dy) instead of a double slope
 * ----------------------------------------------
 * dy/dx as a floating-point value rounds, so distinct lines with very close slopes can
 * collide (or the same line can split). Reducing (dx, dy) by their GCD and fixing a
 * canonical sign gives an exact, hashable key. Vertical lines (dx = 0 -> (0,1)) and
 * horizontal lines (dy = 0 -> (1,0)) are handled with no special cases.
 *
 * Why the encoding is collision-free
 * ----------------------------------
 * After normalization dx is in [0, 2*10^4] and |dy| <= 2*10^4. Since |dy| < 200003,
 * key = dx*200003 + dy maps distinct (dx, dy) to distinct longs.
 *
 * Trace — points [[1,1],[2,2],[3,3]]
 *   anchor (1,1): to (2,2) -> (1,1); to (3,3) -> (1,1). slope (1,1) count = 2.
 *   best = 2 + 1 = 3.  Correct.
 */
