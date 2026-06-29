import java.util.*;

class Solution {

    // Course Schedule II = Course Schedule I (#207) + return the actual order.
    // Both detect cycles; this problem additionally builds the topological ordering.
    //
    // Two approaches:
    //   Kahn's BFS:  naturally produces order during processing — no reversal needed
    //   DFS postorder: adds nodes AFTER all dependencies explored — reverse at end
    //
    // Why NOT Union-Find:
    //   DSU is symmetric — cannot model directed edges correctly.
    //   A→B→C is not a cycle but DSU would merge all three.
    //   Directed cycle detection requires DFS (back-edge) or Kahn's BFS.

    // ── Approach 1: Kahn's Algorithm (BFS) — preferred ───────────────────────
    //
    // Nodes dequeued in valid topological order — no reversal needed.
    // Cycle detection: if idx < numCourses after BFS, some nodes were stuck
    // in a cycle (never reached in-degree 0) → return empty array.
    public int[] findOrder(int numCourses, int[][] prerequisites) {
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
            if (inDegree[i] == 0) queue.offer(i);

        int[] order = new int[numCourses];
        int idx = 0;

        while (!queue.isEmpty()) {
            int course = queue.poll();
            order[idx++] = course;                    // record in topological order
            for (int next : adj.get(course))
                if (--inDegree[next] == 0) queue.offer(next);
        }

        return idx == numCourses ? order : new int[0]; // empty array if cycle
    }

    // ── Approach 2: DFS Postorder (reverse postorder = topological order) ────
    //
    // A course is added to result AFTER all its dependents are fully explored.
    // Courses with no further dependencies are added first → result is reversed order.
    // Reversing gives the correct topological order.
    //
    // 3-colour marking:
    //   White (0) = unvisited
    //   Grey  (1) = currently on DFS call stack
    //   Black (2) = fully explored
    // Back edge (grey→grey) = directed cycle → return empty.
    public int[] findOrderDFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        for (int[] pre : prerequisites) adj.get(pre[1]).add(pre[0]);

        int[] colour = new int[numCourses];
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < numCourses; i++)
            if (colour[i] == 0 && !dfs(adj, colour, result, i))
                return new int[0];

        Collections.reverse(result);
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    // Returns false if cycle detected
    private boolean dfs(List<List<Integer>> adj, int[] colour,
                        List<Integer> result, int node) {
        colour[node] = 1; // grey — currently visiting
        for (int next : adj.get(node)) {
            if (colour[next] == 1) return false;  // back edge → cycle
            if (colour[next] == 0 && !dfs(adj, colour, result, next)) return false;
        }
        colour[node] = 2; // black — fully explored
        result.add(node); // postorder: add AFTER all dependencies explored
        return true;
    }
}

/*
 * Complexity
 * ----------
 * Both: Time O(V+E), Space O(V+E)
 *   V = numCourses, E = prerequisites.length
 *
 * Kahn's vs DFS — trade-offs:
 *   Kahn's: order produced directly; easy cycle detection (idx < numCourses)
 *           iterative → no call stack depth risk; easier to parallelise
 *   DFS:    builds reverse order (must reverse at end); needs List→int[] conversion
 *           recursive → stack overflow risk for deep dependency chains
 *           natural for problems needing DFS tree structure (critical path)
 *
 * Why DFS postorder gives REVERSE topological order:
 *   A node is added to result only after ALL its descendants are added.
 *   So the root (no prerequisites) is added LAST.
 *   Reversing makes the root FIRST — correct topological order.
 *
 *   Example: 0→1→3, 0→2→3
 *   DFS postorder: [3, 1, 2, 0]   (3 added first, 0 added last)
 *   Reversed:      [0, 2, 1, 3]   ✓ topological order
 *
 * Kahn's BFS levels = minimum semesters for parallel scheduling:
 *   Level 0: all courses with inDegree=0 (can take in semester 1)
 *   Level 1: courses unlocked after level 0 (semester 2)
 *   Level k: courses requiring exactly k prerequisites resolved
 *   This directly solves "Parallel Courses" (#1136).
 *
 * Trace — prerequisites=[[1,0],[2,0],[3,1],[3,2]], numCourses=4
 * ---------------------------------------------------------------
 * Graph: 0→1, 0→2, 1→3, 2→3
 * inDegree: [0,1,1,2]
 *
 * Kahn's:
 *   Queue=[0] → order=[0]; inDegree[1]=0, inDegree[2]=0 → enqueue
 *   Queue=[1,2] → order=[0,1]; inDegree[3]=1
 *              → order=[0,1,2]; inDegree[3]=0 → enqueue
 *   Queue=[3] → order=[0,1,2,3]
 *   idx=4 == numCourses=4 → return [0,1,2,3] ✓
 *
 * DFS postorder:
 *   dfs(0): grey(0) → dfs(1): grey(1) → dfs(3): grey(3)
 *     3 has no children → result=[3], black(3)
 *   back to 1: result=[3,1], black(1)
 *   back to 0: dfs(2): grey(2) → 3 is black (skip) → result=[3,1,2], black(2)
 *   result=[3,1,2,0] → reversed: [0,2,1,3] ✓ (also valid topological order)
 */
