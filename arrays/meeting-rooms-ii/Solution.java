import java.util.*;

class Solution {

    // Meeting Rooms II: minimum conference rooms needed.
    // Key insight: rooms needed = maximum number of concurrent meetings at any time.
    //
    // Approach 1: Min-heap of end times — O(n log n)
    // Approach 2: Sweep line (two sorted arrays) — O(n log n), often cleaner

    // ── Approach 1: Min-Heap ──────────────────────────────────────────────────
    // Sort by start time. Use min-heap of end times.
    // For each meeting, if heap's min end <= current start: reuse that room.
    // Else: allocate new room.
    public int minMeetingRooms(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // tracks end times

        for (int[] meeting : intervals) {
            if (!minHeap.isEmpty() && minHeap.peek() <= meeting[0])
                minHeap.poll(); // reuse room: earliest ending meeting is done
            minHeap.offer(meeting[1]); // assign end time to a room
        }

        return minHeap.size(); // each element = one room in use
    }

    // ── Approach 2: Sweep Line ────────────────────────────────────────────────
    // +1 at each start, -1 at each end. Max running sum = max concurrent rooms.
    public int minMeetingRoomsSweep(int[][] intervals) {
        int n = intervals.length;
        int[] starts = new int[n], ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i][0];
            ends[i]   = intervals[i][1];
        }
        Arrays.sort(starts);
        Arrays.sort(ends);

        int rooms = 0, maxRooms = 0, e = 0;
        for (int s = 0; s < n; s++) {
            if (starts[s] < ends[e]) {
                rooms++;    // meeting starts before earliest end → new room
            } else {
                e++;        // a meeting has ended → reuse its room
            }
            maxRooms = Math.max(maxRooms, rooms);
        }
        return maxRooms;
    }
}

/*
 * Complexity: Time O(n log n), Space O(n)
 *
 * Why min-heap? We only care about the EARLIEST ending room — if it's free,
 * we reuse it. We never need to know the others. Min-heap gives O(log n) access.
 *
 * Trace — [[0,30],[5,10],[15,20]]:
 *   Sort: [[0,30],[5,10],[15,20]]
 *   [0,30]: heap empty → add 30. heap=[30]
 *   [5,10]: peek=30 > 5 → new room. heap=[10,30]
 *   [15,20]: peek=10 ≤ 15 → reuse room. poll 10, add 20. heap=[20,30]
 *   return heap.size()=2 ✓
 */
