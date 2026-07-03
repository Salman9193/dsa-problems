import java.util.*;

class Solution {

    // Merge overlapping intervals.
    // Sort by start. Scan: if current overlaps previous, merge (extend end).
    // Two intervals overlap iff: start_b <= end_a

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start
        List<int[]> merged = new ArrayList<>();

        for (int[] interval : intervals) {
            if (merged.isEmpty() || interval[0] > merged.get(merged.size()-1)[1]) {
                merged.add(interval); // no overlap: add new interval
            } else {
                // overlap: extend the end of the last interval
                merged.get(merged.size()-1)[1] =
                    Math.max(merged.get(merged.size()-1)[1], interval[1]);
            }
        }

        return merged.toArray(new int[0][]);
    }
}

/*
 * Complexity: Time O(n log n) — sort dominates; O(n) merge scan
 * Space: O(n) — output list
 *
 * Overlap condition: interval[0] <= last[1]
 *   If NOT overlapping: interval[0] > last[1] → add new
 *   If overlapping: merge by extending end = max(last[1], interval[1])
 *
 * Trace — [[1,3],[2,6],[8,10],[15,18]]:
 *   Sort: [[1,3],[2,6],[8,10],[15,18]]
 *   [1,3]: merged=[[1,3]]
 *   [2,6]: 2<=3 → merge → [[1,6]]
 *   [8,10]: 8>6 → add → [[1,6],[8,10]]
 *   [15,18]: 15>10 → add → [[1,6],[8,10],[15,18]] ✓
 */
