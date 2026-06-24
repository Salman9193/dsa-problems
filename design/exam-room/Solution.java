import java.util.TreeSet;

class ExamRoom {

    // Approach: TreeSet of occupied seats — O(k) seat(), O(log k) leave()
    //
    // Key insight: Between any two consecutive occupied seats [a, b],
    // the optimal new seat is the midpoint: (a+b)/2, distance = (b-a)/2.
    //
    // Special boundary cases:
    //   Left boundary  [_, first]: best seat = 0,   distance = first
    //   Right boundary [last, _]:  best seat = n-1, distance = n-1-last
    //
    // For tie-breaking (equal distance → lower seat):
    //   Interior gap mid = (a+b)/2 is always ≤ right boundary seat (n-1)
    //   Among equal-distance interior gaps, the leftmost is encountered first
    //   during sorted iteration → naturally picks the lower index.
    //
    // TreeSet maintains sorted order for O(k) iteration and O(log k) add/remove.
    private final TreeSet<Integer> occupied;
    private final int n;

    public ExamRoom(int n) {
        this.n = n;
        this.occupied = new TreeSet<>();
    }

    // O(k) where k = current number of students
    public int seat() {
        if (occupied.isEmpty()) {
            occupied.add(0);
            return 0;
        }

        int bestSeat = 0;
        int bestDist = occupied.first();   // distance if we sit at seat 0 (left boundary)

        // Check all interior gaps between consecutive occupied seats
        int prev = -1;
        for (int s : occupied) {
            if (prev != -1) {
                int mid  = (s + prev) / 2;
                int dist = mid - prev;     // distance to nearest neighbour at midpoint
                if (dist > bestDist) {     // strictly greater: lower seat wins ties
                    bestDist = dist;
                    bestSeat = mid;
                }
            }
            prev = s;
        }

        // Check right boundary: sitting at seat n-1
        int last = occupied.last();
        if (n - 1 - last > bestDist) {    // strictly greater: interior wins ties (lower seat)
            bestSeat = n - 1;
        }

        occupied.add(bestSeat);
        return bestSeat;
    }

    // O(log k)
    public void leave(int p) {
        occupied.remove(p);
    }
}

/*
 * Complexity
 * ----------
 * seat():   O(k) — iterate all k occupied seats to find best gap
 * leave(p): O(log k) — TreeSet remove
 * Space:    O(k) — k = number of currently seated students
 *
 * For 10^4 calls and k ≤ 10^4: total work = O(k^2) = O(10^8) — acceptable.
 *
 * Tie-breaking proof:
 *   Two gaps with equal distance d:
 *   Case 1: both interior → leftmost gap found first in sorted iteration;
 *           its midpoint is smaller → naturally selected (strict > check).
 *   Case 2: interior gap vs right boundary (n-1):
 *           Interior mid < n-1 by definition (gap strictly before last seat).
 *           Strict > check means right boundary only wins if STRICTLY larger.
 *           Interior wins on tie → lower seat chosen. ✓
 *   Case 3: left boundary (seat 0) vs interior:
 *           Left boundary wins only if its distance is STRICTLY greater.
 *           On tie, interior mid > 0 → left boundary (seat 0) wins. ✓
 *           Wait — bestDist starts as first (left boundary), interior needs
 *           dist > bestDist to win. So left boundary wins all ties with
 *           the same distance interior gap. But seat 0 < any interior seat. ✓
 *
 * Trace — n=10
 * -------------
 * seat(): empty → return 0. occupied={0}
 * seat(): bestSeat=0, bestDist=0 (first=0)
 *   right: n-1-0=9 > 0 → bestSeat=9
 *   return 9. occupied={0,9}
 * seat(): bestDist=0 (first=0, left boundary dist=0)
 *   gap [0,9]: mid=4, dist=4 > 0 → bestSeat=4, bestDist=4
 *   right: 9-9=0, not > 4
 *   return 4. occupied={0,4,9}
 * seat(): bestDist=0
 *   gap [0,4]: mid=2, dist=2 > 0 → bestSeat=2, bestDist=2
 *   gap [4,9]: mid=6, dist=2, NOT > bestDist=2 → skip (tie goes to lower=2)
 *   right: 0
 *   return 2. occupied={0,2,4,9}
 * leave(4): occupied={0,2,9}
 * seat(): bestDist=0
 *   gap [0,2]: mid=1, dist=1 > 0 → bestSeat=1, bestDist=1
 *   gap [2,9]: mid=5, dist=3 > 1 → bestSeat=5, bestDist=3
 *   right: 9-9=0
 *   return 5 ✓. occupied={0,2,5,9}
 *
 * Optimised O(log k) approach — TreeSet of Intervals:
 *   Instead of iterating all gaps, maintain a TreeSet of (distance, seat) pairs.
 *   Each gap [a,b] maps to its best candidate (midpoint, distance).
 *   seat(): O(log k) — pick max from interval set.
 *   leave(p): O(log k) — merge two adjacent intervals back.
 *   Implementation is more complex (handle boundary intervals specially).
 */
