import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class Solution {

    // Parallel Courses III — LeetCode #2050
    //
    // Courses have durations and prerequisites; unlimited courses may run in parallel.
    // A course starts once ALL its prerequisites finish, so the makespan is dictated by the
    // longest DURATION-WEIGHTED chain of dependencies — the CRITICAL PATH.
    //
    // This is exactly the Critical Path Method (CPM) forward pass:
    //     finish[v] = time[v] + max( finish[u] : u is a prerequisite of v )
    //     answer    = max over v of finish[v]
    //
    // Longest path is NP-hard in a general graph, but O(V + E) in a DAG: topological order
    // guarantees every prerequisite is final before v is computed. Acyclicity buys the DP.
    //
    // Time: O(V + E).  Space: O(V + E).
    public int minimumTime(int n, int[][] relations, int[] time) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[n];

        for (int[] r : relations) {
            int u = r[0] - 1, v = r[1] - 1;      // input is 1-indexed
            adj.get(u).add(v);
            indeg[v]++;
        }

        int[] finish = new int[n];
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {                 // no prerequisites -> can start at month 0
                finish[i] = time[i];
                q.offer(i);
            }
        }

        int ans = 0;
        while (!q.isEmpty()) {                   // Kahn's topological sort
            int u = q.poll();
            ans = Math.max(ans, finish[u]);
            for (int v : adj.get(u)) {
                // MAX, not overwrite: v waits for ALL prerequisites, not just the first
                finish[v] = Math.max(finish[v], finish[u] + time[v]);
                if (--indeg[v] == 0) q.offer(v); // enqueue only once every prereq is relaxed in
            }
        }
        return ans;
    }
}

/*
 * Trace — n = 5, relations = [[1,5],[2,5],[3,5],[3,4],[4,5]], time = [1,2,3,4,5]
 * -----------------------------------------------------------------------------
 *   courses 1,2,3 have no prerequisites -> finish = 1, 2, 3   (all start at month 0)
 *   course 4 needs 3         -> finish[4] = 3 + 4  = 7
 *   course 5 needs 1,2,3,4   -> finish[5] = max(1,2,3,7) + 5 = 12
 *   answer = 12
 *
 *   CRITICAL PATH: 3 -> 4 -> 5  (3 + 4 + 5 = 12). Courses 1 and 2 have slack and are
 *   irrelevant to the makespan.
 *
 * The pitfall: using finish[v] = finish[u] + time[v] (overwriting instead of maxing) would let
 * a course start as soon as ANY single prerequisite finished — the classic bug here.
 *
 * CPM vocabulary maps 1:1 — activity = course, forward pass = this relaxation, makespan =
 * max(finish), critical path = the longest weighted path. The backward pass (reverse
 * topological order) yields each activity's latest finish, and slack = latest - earliest;
 * zero-slack activities ARE the critical path.
 */
