import java.util.*;

class Solution {

    // A node is SAFE iff every path from it eventually reaches a terminal node
    // (no outgoing edges) — i.e., no path from it leads into a cycle.
    // A node is UNSAFE iff it lies on a cycle OR has any path leading to a cycle.
    //
    // Why NOT Union-Find?
    //   DSU merges components symmetrically. It cannot model directed edges.
    //   A→B→C is a valid directed path (no cycle), but DSU would put all three
    //   in the same component — it cannot distinguish a path from a cycle.
    //   Additionally, a node LEADING TO a cycle is unsafe even if not IN it —
    //   DSU tracks membership, not reachability direction.
    //   Use DFS 3-colour or Reverse Kahn's for directed safety analysis.

    // ── Approach 1: DFS with 3-Colour Marking ────────────────────────────────
    //
    // 3-colour invariant:
    //   White (0) = unvisited
    //   Grey  (1) = currently on DFS stack (being explored)
    //   Black (2) = fully explored AND confirmed safe
    //
    // Critical: unsafe nodes are NOT marked black — left grey so other
    // paths hitting them immediately return false without re-exploring.
    //
    // When is DFS better than Reverse Kahn's?
    //   - Less code: no need to build reverse adjacency list
    //   - Less space: only O(V) colour array vs O(V+E) reverse graph
    //   - Early termination: returns false the moment a grey node is hit
    //   - Works directly on input graph — no transformation needed
    //
    // When is DFS WORSE (stack overflow risk)?
    //   - Deep graphs: recursion depth = longest path length
    //   - If V = 10^5 and graph is a chain 0→1→2→...→n, DFS recurses n times
    //     → StackOverflowError in Java (default stack ~512 frames)
    //   - Solution: use iterative DFS with explicit stack (see below)
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] colour = new int[n]; // 0=white, 1=grey, 2=black(safe)
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++)
            if (isSafe(graph, colour, i))
                result.add(i); // nodes added in order 0..n-1 → already sorted

        return result;
    }

    // Returns true if node is safe (no cycle reachable from it)
    private boolean isSafe(int[][] graph, int[] colour, int node) {
        if (colour[node] == 1) return false; // grey = on current path = cycle
        if (colour[node] == 2) return true;  // black = already confirmed safe

        colour[node] = 1; // mark grey: currently visiting
        for (int next : graph[node]) {
            if (!isSafe(graph, colour, next)) {
                // Do NOT mark black here — leave grey so callers know this
                // node leads to a cycle (unsafe). Marking black would incorrectly
                // declare it safe for other paths.
                return false;
            }
        }
        colour[node] = 2; // mark black: all paths lead to terminals → safe
        return true;
    }

    // ── Iterative DFS — avoids StackOverflowError for deep graphs ─────────────
    //
    // Simulates the recursive call stack with an explicit Deque.
    // Each stack frame stores {node, childIndex} — when childIndex == 0,
    // the node is being entered (mark grey); when all children are processed,
    // the node is exited (mark black = safe).
    public List<Integer> eventualSafeNodesIterative(int[][] graph) {
        int n = graph.length;
        int[] colour = new int[n];
        List<Integer> result = new ArrayList<>();

        for (int start = 0; start < n; start++) {
            if (colour[start] != 0) {
                if (colour[start] == 2) result.add(start);
                continue;
            }
            if (isSafeIterative(graph, colour, start))
                result.add(start);
        }
        return result;
    }

    private boolean isSafeIterative(int[][] graph, int[] colour, int start) {
        Deque<int[]> stack = new ArrayDeque<>(); // {node, childIndex}
        stack.push(new int[]{start, 0});

        while (!stack.isEmpty()) {
            int[] frame = stack.peek();
            int node = frame[0], idx = frame[1];

            if (idx == 0) {                         // entering node
                if (colour[node] == 1) return false; // grey → cycle
                if (colour[node] == 2) { stack.pop(); continue; } // already safe
                colour[node] = 1;                    // mark grey
            }

            if (idx < graph[node].length) {
                frame[1]++;                          // advance to next child
                int next = graph[node][idx];
                if (colour[next] == 0)
                    stack.push(new int[]{next, 0}); // recurse into unvisited child
                else if (colour[next] == 1)
                    return false;                    // grey → cycle
                // colour[next]==2: safe, skip
            } else {
                colour[node] = 2;                   // all children processed → safe
                stack.pop();
            }
        }
        return true;
    }

    // ── Approach 2: Reverse Graph + Kahn's BFS ────────────────────────────────
    //
    // Insight: in the original graph, safe nodes = nodes whose ALL paths
    // terminate. In the reversed graph, terminal nodes become sources.
    // BFS from reversed-graph sources propagates safety backward.
    //
    // When an original node's out-degree reaches 0 (all successors confirmed safe),
    // it too is safe — analogous to Kahn's in-degree countdown.
    //
    // Best when: graph is very wide/deep (avoids stack overflow risk of DFS).
    // Downside: O(V+E) extra space for reverse adjacency list.
    public List<Integer> eventualSafeNodesKahn(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> reverseAdj = new ArrayList<>();
        int[] outDegree = new int[n];

        for (int i = 0; i < n; i++) reverseAdj.add(new ArrayList<>());
        for (int u = 0; u < n; u++) {
            outDegree[u] = graph[u].length;
            for (int v : graph[u]) reverseAdj.get(v).add(u);
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++)
            if (outDegree[i] == 0) queue.offer(i); // terminal nodes are safe

        boolean[] safe = new boolean[n];
        while (!queue.isEmpty()) {
            int node = queue.poll();
            safe[node] = true;
            for (int prev : reverseAdj.get(node))
                if (--outDegree[prev] == 0) queue.offer(prev);
        }

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) if (safe[i]) result.add(i);
        return result;
    }
}

/*
 * Complexity
 * ----------
 * DFS recursive:  Time O(V+E), Space O(V) colour + O(V) call stack
 * DFS iterative:  Time O(V+E), Space O(V) colour + O(V) explicit stack
 * Reverse Kahn's: Time O(V+E), Space O(V+E) reverse adj + O(V) safe array
 *
 * DFS vs Reverse Kahn's — when to choose:
 *
 *   Choose DFS when:
 *     - Graph is sparse or shallow (no stack overflow risk)
 *     - Want minimal code and space (only O(V) extra)
 *     - Early termination on first cycle is valuable
 *     - Don't want to build a separate reverse graph
 *
 *   Choose Reverse Kahn's when:
 *     - Graph can be very deep (V up to 10^5 in a chain) → stack overflow risk
 *     - Already building a reverse graph for other purposes
 *     - Want purely iterative implementation (no recursion at all)
 *     - Need to process all safe nodes in BFS-level order
 *
 *   Choose Iterative DFS when:
 *     - Want DFS logic with zero stack overflow risk
 *     - Want O(V) space (no reverse graph) but still iterative
 *     - Most flexible: combine DFS early-exit with iterative safety
 *
 * Why grey nodes are NOT marked black on unsafe return:
 *   If node A leads to a cycle via B, and we mark A black (safe) after
 *   returning false from B, then another path C→A would see black(A)
 *   and incorrectly conclude A is safe. By leaving A grey, C→A immediately
 *   returns false — correctly identifying C as unsafe too.
 *
 * Trace — graph=[[1,2],[2,3],[5],[0],[5],[],[]]
 * -----------------------------------------------
 * Terminal nodes (no outgoing): 5, 6
 *
 * DFS:
 *   isSafe(0): grey(0) → isSafe(1): grey(1) → isSafe(2): grey(2)
 *     → isSafe(5): white→black(5)=safe. return true
 *   back to 2: grey(2)→black(2)=safe. return true
 *   back to 1: isSafe(3): grey(3) → isSafe(0): grey(0)!→ return false
 *   3 stays grey. 1 stays grey. 0 stays grey.
 *
 *   isSafe(1): grey(1) → return false (already grey)
 *   isSafe(2): black → return true
 *   isSafe(3): grey → return false
 *   isSafe(4): white→grey(4) → isSafe(5): black → return true. 4→black=safe
 *   isSafe(5): black → true
 *   isSafe(6): white→black(6)=safe (no children)
 *
 *   Safe: {2,4,5,6} → [2,4,5,6] ✓
 */
