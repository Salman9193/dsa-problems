import java.util.*;

class Solution {

    // Three approaches — all correct, different trade-offs.
    // Input: n×n adjacency matrix isConnected where isConnected[i][j]=1
    //        means city i and city j are directly connected.
    // Output: number of connected components (provinces).

    // ── Approach 1: DFS — O(n²) time, O(n) space ─────────────────────────
    //
    // For each unvisited city, start a DFS that visits all cities in its
    // province. Each DFS invocation = one new province.
    //
    // Key difference from grid DFS (#200 Number of Islands):
    //   Grid DFS: neighbours found by 4-directional movement on 2D array
    //   This DFS: neighbours found by scanning the adjacency matrix row
    //   Both are O(n²) but the structure differs.
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                dfs(isConnected, visited, i);
            }
        }
        return provinces;
    }

    private void dfs(int[][] isConnected, boolean[] visited, int city) {
        visited[city] = true;
        for (int next = 0; next < isConnected.length; next++)
            if (isConnected[city][next] == 1 && !visited[next])
                dfs(isConnected, visited, next);
    }

    // ── Approach 2: BFS — O(n²) time, O(n) space ─────────────────────────
    //
    // Iterative alternative to DFS — no call-stack depth risk.
    // Preferred for large n where DFS recursion depth = province size.
    public int findCircleNumBFS(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            provinces++;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(i);
            visited[i] = true;
            while (!queue.isEmpty()) {
                int city = queue.poll();
                for (int next = 0; next < n; next++)
                    if (isConnected[city][next] == 1 && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
            }
        }
        return provinces;
    }

    // ── Approach 3: Union-Find — O(n² α(n)) time, O(n) space ─────────────
    //
    // Union all directly connected city pairs. Count distinct roots = provinces.
    //
    // Why only j > i?
    //   Matrix is symmetric (isConnected[i][j] == isConnected[j][i]).
    //   Processing only the upper triangle avoids duplicate unions.
    //   union(0,1) and union(1,0) produce the same result — do one.
    //
    // Best when: multiple "are cities i and j in the same province?" queries
    // after one build pass — each query is O(α(n)) ≈ O(1).
    public int findCircleNumUF(int[][] isConnected) {
        int n = isConnected.length;
        int[] parent = new int[n];
        int[] rank   = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int components = n; // start with n separate provinces
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++) // upper triangle only
                if (isConnected[i][j] == 1) {
                    int pi = find(parent, i), pj = find(parent, j);
                    if (pi != pj) { union(parent, rank, pi, pj); components--; }
                }

        return components;
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        if      (rank[x] < rank[y]) parent[x] = y;
        else if (rank[x] > rank[y]) parent[y] = x;
        else { parent[y] = x; rank[x]++; }
    }
}

/*
 * Complexity
 * ----------
 * DFS/BFS:     Time O(n²) — scan full adjacency matrix row per city
 * Union-Find:  Time O(n² α(n)) — process upper triangle, O(α) per union/find
 * All:         Space O(n) — visited array or parent array
 *
 * Adjacency matrix vs adjacency list:
 *   This problem gives an adjacency MATRIX — scanning for neighbours is O(n) per city.
 *   If given as adjacency LIST, scanning is O(degree) per city → more efficient for sparse graphs.
 *   Total: O(n²) with matrix (density-independent), O(V+E) with list (sparse-friendly).
 *
 * Why components starts at n (not 0) in Union-Find:
 *   Initially each city is its own province (n provinces).
 *   Each successful union merges two provinces into one → components--.
 *   Final count = n - (number of merges performed).
 *
 * Trace — [[1,1,0],[1,1,0],[0,0,1]]
 * ------------------------------------
 * Union-Find:
 *   components = 3
 *   (0,1): isConnected=1 → find(0)=0, find(1)=1, different → union, components=2
 *   (0,2): isConnected=0 → skip
 *   (1,2): isConnected=0 → skip
 *   return 2 ✓
 *
 * DFS:
 *   i=0: not visited → provinces=1, dfs(0)
 *     visited[0]=true, check row 0: isConnected[0][1]=1, !visited[1] → dfs(1)
 *       visited[1]=true, check row 1: isConnected[1][0]=1 but visited[0] → skip
 *   i=1: visited → skip
 *   i=2: not visited → provinces=2, dfs(2) → visited[2]=true
 *   return 2 ✓
 */
