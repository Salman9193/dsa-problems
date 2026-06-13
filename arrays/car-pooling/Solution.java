import java.util.TreeMap;

class Solution {

    // Approach 1: Difference Array (Bucket Sort) — O(n + R) time, O(R) space
    //
    // Each trip contributes two events:
    //   +numPassengers at stop `from`  (passengers board)
    //   -numPassengers at stop `to`    (passengers alight)
    //
    // Fill a stops[] array with these deltas, then scan left-to-right
    // accumulating a running sum. If it ever exceeds capacity → false.
    //
    // Stop numbers are bounded by [0, 1000] so R = 1001 is a constant.
    // This makes the approach effectively O(n).
    public boolean carPooling(int[][] trips, int capacity) {
        int[] stops = new int[1001];

        for (int[] trip : trips) {
            stops[trip[1]] += trip[0];   // board at from
            stops[trip[2]] -= trip[0];   // alight at to
        }

        int passengers = 0;
        for (int delta : stops) {
            passengers += delta;
            if (passengers > capacity) return false;
        }

        return true;
    }

    // Approach 2: TreeMap / Event Sweep — O(n log n) time, O(n) space
    //
    // General solution for unbounded stop numbers.
    // TreeMap keeps events sorted by stop number automatically.
    public boolean carPoolingTreeMap(int[][] trips, int capacity) {
        TreeMap<Integer, Integer> events = new TreeMap<>();

        for (int[] trip : trips) {
            events.merge(trip[1],  trip[0], Integer::sum);  // board
            events.merge(trip[2], -trip[0], Integer::sum);  // alight
        }

        int passengers = 0;
        for (int delta : events.values()) {
            passengers += delta;
            if (passengers > capacity) return false;
        }

        return true;
    }
}

/*
 * Complexity
 * ----------
 * Approach 1: Time O(n + R), Space O(R)  — R=1001 (constant), effectively O(n)
 * Approach 2: Time O(n log n),  Space O(n)
 *
 * Why `to` is exclusive (alight BEFORE boarding at the same stop):
 *   The problem states passengers travel from `from` to `to` — they leave
 *   AT `to`, so new passengers can board at `to`. The difference array
 *   naturally handles this: both board and alight events at the same stop
 *   are accumulated into the same index, and the running sum reflects
 *   the state AFTER processing all events at that stop.
 *
 * This is the SWEEP LINE pattern — a fundamental technique for all
 * interval/resource problems:
 *   Transform intervals → events (start/end)
 *   Sort events by position
 *   Sweep left to right, track running aggregate
 *   Check constraint at each step
 *
 * Sweep line family:
 *   Car Pooling (#1094)     → passengers in vehicle at each stop
 *   Meeting Rooms II (#253) → concurrent meetings (min rooms needed)
 *   Merge Intervals (#56)   → active overlapping intervals
 *   Skyline Problem (#218)  → active building heights (running max)
 *   Range Addition (#370)   → cumulative range updates
 *
 * Trace — trips=[[2,1,5],[3,3,7]], capacity=4
 * --------------------------------------------
 * stops after processing:
 *   index: 0  1  2  3  4  5  6  7
 *   stops: 0 +2  0 +3  0 -2  0 -3
 *
 * Running sum:
 *   idx=1: passengers=2  (≤4 ✓)
 *   idx=3: passengers=5  (>4 ✗) → return false ✓
 *
 * Trace — trips=[[2,1,5],[3,5,7]], capacity=3
 * --------------------------------------------
 * stops: 0 +2  0  0  0 +3-2  0 -3
 *          1             5=+1    7
 *
 * Running sum:
 *   idx=1: passengers=2  (≤3 ✓)
 *   idx=5: passengers=2+1=3 (≤3 ✓)
 *   idx=7: passengers=0
 *   → return true ✓  (drop-off at 5 before board at 5)
 */
