import java.util.*;

class Solution {

    // Course Schedule = cycle detection in a directed graph.
    // Scheduling is possible iff the prerequisite graph is a DAG.
    // A cycle means: A requires B requires ... requires A — impossible.
    //
    // Why NOT Union-Find?
    //   DSU merges components symmetrically. It cannot distinguish edge direction.
    //   In a directed graph, A→B→C is NOT a cycle, but DSU would put all three
    //   in the same component and might flag it as a cycle. DSU only works for
    //   undirected cycle detection. Use DFS (3-colour) or Kahn's BFS for directed graphs.

    // ── Approach 1: Kahn's Algorithm (BFS Topological Sort) ──────────────────
    //
    // Process nodes with in-degree 0 (no prerequisites) one level at a time.
    // If a cycle exists, nodes in the cycle never reach in-degree 0 → never processed.
    // Cycle detected iff processed < numCourses.
    //
    // Also naturally returns the topological order (Course Schedule II, #210).
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses];
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());

        for (int[] pre : prerequisites) {
            // pre[1] must be taken before pre[0]  →  edge pre[1] → pre[0]
            adj.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++)
            if (inDegree[i] == 0) queue.offer(i); // courses with no prerequisites

        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : adj.get(course))
                if (--inDegree[next] == 0) queue.offer(next);
        }

        // If all courses processed → no cycle → can finish
        return processed == numCourses;
    }

    // ── Approach 2: DFS with 3-Colour Marking ────────────────────────────────
    //
    // White (0) = unvisited
    // Grey  (1) = currently in DFS call stack (being explored)
    // Black (2) = fully processed (all descendants explored)
    //
    // Back edge: DFS reaches a GREY node → cycle detected.
    // Why grey and not just visited?
    //   A black node is fully explored — reaching it again is safe (already processed).
    //   A grey node is on the current DFS path — reaching it means we found a cycle
    //   back to an ancestor in the current path.
    public boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) adj.get(pre[1]).add(pre[0]);

        int[] colour = new int[numCourses]; // 0=white, 1=grey, 2=black

        for (int i = 0; i < numCourses; i++)
            if (colour[i] == 0 && hasCycle(adj, colour, i))
                return false;

        return true;
    }

    // Returns true if a cycle is reachable from 'node'
    private boolean hasCycle(List<List<Integer>> adj, int[] colour, int node) {
        colour[node] = 1; // mark grey — currently visiting
        for (int next : adj.get(node)) {
            if (colour[next] == 1) return true;  // back edge to grey ancestor → cycle
            if (colour[next] == 0 && hasCycle(adj, colour, next)) return true;
        }
        colour[node] = 2; // mark black — fully explored, no cycle through here
        return false;
    }
}

/*
 * Complexity
 * ----------
 * Both approaches: Time O(V+E), Space O(V+E)
 *   V = numCourses, E = prerequisites.length
 *
 * Kahn's vs DFS — when to choose which:
 *   Kahn's:  naturally gives topological order; easy to count processed nodes for cycle check
 *   DFS:     more intuitive cycle detection (grey = in-stack); handles disconnected graphs naturally
 *   Both:    O(V+E) — equivalent complexity
 *
 * Why 3 colours (not 2) in DFS?
 *   2-colour (visited/unvisited): can't distinguish "currently exploring" from "done"
 *   If we use visited=true for both grey and black, we'd skip black nodes (correct) but
 *   also skip grey nodes (incorrect — we'd miss cycles to ancestors).
 *   Grey (in-stack) is the key: reaching a grey node = we found a path back to an ancestor
 *   = back edge = directed cycle.
 *
 * Why NOT Union-Find for directed graphs?
 *   DSU merges components symmetrically: union(A,B) means A and B are in the same group.
 *   In a directed graph, A→B→C is valid (no cycle). But DSU would union all three and
 *   potentially report A and C as "same component = cycle" — WRONG.
 *   DSU can only detect cycles in UNDIRECTED graphs.
 *   For directed graphs: use DFS (back edge detection) or Kahn's BFS.
 *
 * Trace — numCourses=4, prerequisites=[[1,0],[2,0],[3,1],[3,2]]
 * ---------------------------------------------------------------
 * Graph: 0→1, 0→2, 1→3, 2→3
 * inDegree: [0, 1, 1, 2]
 *
 * Kahn's:
 *   Queue: [0]
 *   Process 0: processed=1, inDegree[1]=0→queue, inDegree[2]=0→queue
 *   Queue: [1,2]
 *   Process 1: processed=2, inDegree[3]=1
 *   Process 2: processed=3, inDegree[3]=0→queue
 *   Queue: [3]
 *   Process 3: processed=4
 *   processed(4) == numCourses(4) → true ✓
 *
 * Trace — numCourses=2, prerequisites=[[0,1],[1,0]]
 * --------------------------------------------------
 * Graph: 1→0, 0→1 (cycle)
 * inDegree: [1, 1]
 * Kahn's: no node reaches inDegree 0 → queue stays empty → processed=0 ≠ 2 → false ✓
 *
 * DFS from 0: colour[0]=grey
 *   visit 1: colour[1]=grey
 *     visit 0: colour[0]==grey → CYCLE → return true → canFinish returns false ✓
 */
