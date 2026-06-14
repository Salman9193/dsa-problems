class Solution {

    // Approach: Chebyshev Distance — O(n) time, O(1) space
    //
    // With 8-directional movement (including diagonals), the minimum steps
    // to travel from point A to point B is the Chebyshev distance:
    //
    //   max(|x2 - x1|, |y2 - y1|)
    //
    // Why: each diagonal step reduces BOTH dx and dy by 1 simultaneously.
    //   Optimal strategy:
    //     - Move diagonally min(dx, dy) times → covers both axes
    //     - Move straight  |dx - dy| times    → covers remaining axis
    //     - Total = min(dx,dy) + |dx-dy| = max(dx,dy)
    //
    // Simply sum the Chebyshev distances between consecutive points.
    public int minTimeToVisitAllPoints(int[][] points) {
        int time = 0;
        for (int i = 1; i < points.length; i++) {
            int dx = Math.abs(points[i][0] - points[i-1][0]);
            int dy = Math.abs(points[i][1] - points[i-1][1]);
            time += Math.max(dx, dy);
        }
        return time;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — single pass over n-1 consecutive point pairs
 * Space: O(1) — two variables per iteration
 *
 * The Three Distance Metrics:
 *   Manhattan:  |dx| + |dy|       → 4-directional movement (no diagonals)
 *   Euclidean:  sqrt(dx² + dy²)   → continuous straight-line movement
 *   Chebyshev:  max(|dx|, |dy|)   → 8-directional movement (diagonals allowed)
 *
 * Chebyshev = L∞ norm. It equals Manhattan distance after a 45° rotation
 * of the coordinate system (substitution: u = x+y, v = x-y).
 *
 * Proof that max(dx,dy) is optimal:
 *   Each step reduces max(remaining_dx, remaining_dy) by at most 1.
 *   Diagonal: reduces both dx and dy by 1 → max decreases by 1.
 *   Straight: reduces one of dx or dy by 1 → max decreases by 1 (if it's the larger).
 *   So exactly max(dx,dy) steps are necessary AND sufficient.
 *
 * Trace — [[1,1],[3,4],[-1,0]]
 * ------------------------------
 * Segment 1: (1,1)→(3,4):  dx=2, dy=3 → max(2,3) = 3
 * Segment 2: (3,4)→(-1,0): dx=4, dy=4 → max(4,4) = 4
 * Total = 7 ✓
 *
 * Trace — [[3,2],[-2,2]]
 * -----------------------
 * Segment 1: (3,2)→(-2,2): dx=5, dy=0 → max(5,0) = 5
 * Total = 5 ✓
 */
