import java.util.LinkedList;
import java.util.Queue;

class Solution {

    // Approach 1: BFS 2-colouring — O(V+E) time, O(V) space
    //
    // A graph is bipartite iff it can be 2-coloured (no two adjacent nodes
    // share the same colour) iff it has no odd-length cycles.
    //
    // Colour 0 = unvisited, 1 = red, -1 = blue.
    // Negating flips between 1 and -1 cleanly.
    //
    // Must check every component (graph may be disconnected).
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] colour = new int[n];  // 0=uncoloured, 1=red, -1=blue

        for (int start = 0; start < n; start++) {
            if (colour[start] != 0) continue;  // already processed

            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            colour[start] = 1;

            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int neighbour : graph[node]) {
                    if (colour[neighbour] == 0) {
                        colour[neighbour] = -colour[node];  // assign opposite colour
                        queue.offer(neighbour);
                    } else if (colour[neighbour] == colour[node]) {
                        return false;  // same colour → conflict → not bipartite
                    }
                }
            }
        }
        return true;
    }

    // Approach 2: DFS 2-colouring — O(V+E) time, O(V) space
    public boolean isBipartiteDFS(int[][] graph) {
        int n = graph.length;
        int[] colour = new int[n];

        for (int i = 0; i < n; i++) {
            if (colour[i] == 0 && !dfs(graph, colour, i, 1))
                return false;
        }
        return true;
    }

    private boolean dfs(int[][] graph, int[] colour, int node, int c) {
        colour[node] = c;
        for (int neighbour : graph[node]) {
            if (colour[neighbour] == 0) {
                if (!dfs(graph, colour, neighbour, -c)) return false;
            } else if (colour[neighbour] == c) {
                return false;
            }
        }
        return true;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(V + E) — visit each node and edge once
 * Space: O(V) — colour array + BFS queue (at most V nodes)
 *
 * Key equivalences (all equivalent conditions):
 *   Bipartite ↔ 2-colourable ↔ no odd-length cycles
 *   ↔ graph spectrum symmetric about 0 (Hückel graph theorem)
 *
 * Why use 1 and -1 (not 0 and 1)?
 *   Negating flips between them: colour[neighbour] = -colour[node]
 *   0 serves as "uncoloured" sentinel — distinct from both colours.
 *   Alternative: use 0/1 and XOR to flip (c ^ 1), with -1 as uncoloured.
 *
 * Why the outer for-loop?
 *   The graph may be DISCONNECTED. Without the outer loop,
 *   isolated components would be skipped and could contain odd cycles.
 *
 * Trace — graph=[[1,3],[0,2],[1,3],[0,2]]
 * ----------------------------------------
 * BFS from 0: colour[0]=1
 *   Process 0: neighbours=[1,3]
 *     colour[1]=0 → colour[1]=-1, enqueue 1
 *     colour[3]=0 → colour[3]=-1, enqueue 3
 *   Process 1: neighbours=[0,2]
 *     colour[0]=1 ≠ colour[1]=-1 ✓ (no conflict)
 *     colour[2]=0 → colour[2]=1,  enqueue 2
 *   Process 3: neighbours=[0,2]
 *     colour[0]=1 ≠ -1 ✓
 *     colour[2]=1 ≠ -1 ✓
 *   Process 2: neighbours=[1,3]
 *     colour[1]=-1 ≠ 1 ✓
 *     colour[3]=-1 ≠ 1 ✓
 * All consistent → true ✓
 *
 * Counter-example — graph=[[1,2,3],[0,2],[0,1,3],[0,2]]
 *   BFS from 0: colour[0]=1
 *   Process 0: colour[1]=-1, colour[2]=-1, colour[3]=-1
 *   Process 1: neighbours include 2; colour[2]=-1 == colour[1]=-1 → CONFLICT
 *   return false ✓ (0-1-2 forms a triangle, odd cycle)
 */
