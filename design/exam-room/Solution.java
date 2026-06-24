import java.util.TreeSet;

class ExamRoom {

    // Approach 1: TreeSet of occupied seats — O(k) seat(), O(log k) leave()
    //
    // Key insight: between consecutive occupied seats [a, b], the optimal
    // new seat is the midpoint (a+b)/2, giving distance (b-a)/2.
    // Two boundary gaps need special handling:
    //   Left  [virtual -inf, first]:  best seat = 0,   distance = first
    //   Right [last, virtual +inf]:   best seat = n-1, distance = n-1-last
    //
    // Tie-breaking (equal distance → lower seat index):
    //   Using strict '>' means the FIRST valid candidate seen wins.
    //   Iterating in ascending seat order guarantees we see lower seats first.
    //   Right boundary (n-1) only wins if STRICTLY greater than all interior gaps —
    //   so on a tie, any interior seat (lower index) is kept. ✓
    private final TreeSet<Integer> occupied;
    private final int n;

    public ExamRoom(int n) {
        this.n = n;
        this.occupied = new TreeSet<>();
    }

    // O(k) — iterate k occupied seats to evaluate all k+1 gaps
    public int seat() {
        if (occupied.isEmpty()) {
            occupied.add(0);
            return 0;
        }

        // Baseline: sit at seat 0 (left boundary gap)
        // Distance at seat 0 = first occupied seat - 0 = occupied.first()
        int bestSeat = 0;
        int bestDist = occupied.first();

        // Evaluate each interior gap [prev, curr]
        // Candidate seat = midpoint, candidate distance = midpoint - prev
        Integer prev = null;
        for (int curr : occupied) {
            if (prev != null) {
                int candidateSeat = (prev + curr) / 2;
                int candidateDist = candidateSeat - prev;
                // Strict '>' preserves left-to-right (lower seat) on ties
                if (candidateDist > bestDist) {
                    bestDist = candidateDist;
                    bestSeat = candidateSeat;
                }
            }
            prev = curr;
        }

        // Evaluate right boundary gap: sit at seat n-1
        // Distance = n-1 - last occupied seat
        int rightDist = n - 1 - occupied.last();
        // Strict '>' means interior gaps (lower seat) win ties with right boundary
        if (rightDist > bestDist) {
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
 * seat():   O(k) — iterate k occupied seats
 * leave(p): O(log k) — TreeSet remove
 * Space:    O(k) — k ≤ 10^4 seats, n ≤ 10^9 (sparse — only store occupied seats)
 *
 * For 10^4 total calls, k ≤ 10^4: worst-case total work = O(k^2) = O(10^8) — fine.
 *
 * Tie-breaking correctness proof:
 *   Three tie cases, all resolved correctly by strict '>':
 *
 *   Case 1 — Two interior gaps with equal distance:
 *     Sorted iteration visits the leftmost gap first.
 *     Its midpoint sets bestSeat/bestDist.
 *     Later equal-distance gaps fail the '>' check and are skipped. ✓
 *
 *   Case 2 — Interior gap ties with left boundary (seat 0):
 *     bestDist initialised from left boundary (seat 0).
 *     Interior gap needs strictly '>' to displace seat 0.
 *     On tie, seat 0 is kept (lower index). ✓
 *
 *   Case 3 — Interior gap ties with right boundary (seat n-1):
 *     Right boundary check uses strict '>'.
 *     On tie, interior candidate already in bestSeat (lower index). ✓
 *
 * Trace — n=10
 * -------------
 * seat(): empty → 0. occupied={0}
 *
 * seat(): bestSeat=0, bestDist=0 (first=0)
 *   no interior gaps (one seat)
 *   rightDist=9-0=9 > 0 → bestSeat=9
 *   return 9. occupied={0,9}
 *
 * seat(): bestSeat=0, bestDist=0
 *   gap [0,9]: candidateSeat=4, candidateDist=4 > 0 → bestSeat=4, bestDist=4
 *   rightDist=9-9=0, NOT > 4
 *   return 4. occupied={0,4,9}
 *
 * seat(): bestSeat=0, bestDist=0
 *   gap [0,4]: candidateSeat=2, candidateDist=2 > 0 → bestSeat=2, bestDist=2
 *   gap [4,9]: candidateSeat=6, candidateDist=2, NOT > 2 (tie → keep seat 2)
 *   rightDist=0
 *   return 2. occupied={0,2,4,9}
 *
 * leave(4): occupied={0,2,9}
 *
 * seat(): bestSeat=0, bestDist=0
 *   gap [0,2]: candidateSeat=1, candidateDist=1 > 0 → bestSeat=1, bestDist=1
 *   gap [2,9]: candidateSeat=5, candidateDist=3 > 1 → bestSeat=5, bestDist=3
 *   rightDist=0
 *   return 5 ✓. occupied={0,2,5,9}
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * Approach 2: TreeSet of Intervals — O(log k) seat() and leave()
 *
 * For large k (many students), O(k) per seat() becomes expensive.
 * Maintain a TreeSet<int[]> of intervals sorted by (distance DESC, seat ASC).
 * Each interval [a, b] stores its best candidate seat and distance.
 *
 * seat():
 *   Pop the best interval (max distance, lowest seat on tie).
 *   Split [a, bestSeat] and [bestSeat, b]; push both back.
 *   O(log k)
 *
 * leave(p):
 *   Find intervals [a, p] and [p, b] in the set (use a HashMap<seat, interval>).
 *   Remove both, merge into [a, b], push back.
 *   O(log k)
 *
 * Boundary intervals: use virtual sentinels -1 and n to unify boundary/interior logic.
 *   Interval [-1, first]: distance = first (left boundary)
 *   Interval [last, n]:   distance = n-1-last (right boundary)
 *
 * This approach is preferred at Staff Engineer level when k is large.
 * ─────────────────────────────────────────────────────────────────────────────
 */
