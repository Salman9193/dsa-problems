import java.util.*;

class Solution {

    // ── Approach 1: Reverse BFS with temp marker — O(mn) time, O(mn) space ──
    //
    // Key insight: instead of finding surrounded 'O' regions (hard — must check
    // all 4 directions reach border), REVERSE the question:
    //   "Is this 'O' surrounded?"  →  hard
    //   "Is this 'O' connected to the border?"  →  easy (BFS from border)
    //
    // An 'O' is NOT surrounded iff it is 4-directionally connected to a border 'O'.
    //
    // Three steps:
    //   1. BFS from all border 'O' cells — mark connected 'O's as 'S' (safe)
    //   2. Flip all remaining 'O' → 'X' (they are surrounded)
    //   3. Restore all 'S' → 'O' (they were safe)
    //
    // This is the same reverse-BFS insight as Pacific Atlantic Water Flow (#417):
    // instead of asking "can I reach the border?", ask "can the border reach me?"
    public void solve(char[][] board) {
        int rows = board.length, cols = board[0].length;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        // Step 1: BFS from all border 'O' cells — mark safe as 'S'
        Queue<int[]> queue = new LinkedList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == 0 || r == rows-1 || c == 0 || c == cols-1)
                    && board[r][c] == 'O') {
                    board[r][c] = 'S';   // mark safe on enqueue
                    queue.offer(new int[]{r, c});
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : dirs) {
                int nr = cell[0]+d[0], nc = cell[1]+d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                    && board[nr][nc] == 'O') {
                    board[nr][nc] = 'S'; // mark safe on enqueue
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        // Steps 2 & 3: single pass — flip 'O'→'X', restore 'S'→'O'
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if      (board[r][c] == 'O') board[r][c] = 'X'; // surrounded
                else if (board[r][c] == 'S') board[r][c] = 'O'; // safe
    }

    // ── Approach 2: Union-Find with virtual border node ───────────────────────
    //
    // Key insight: create a virtual node (index = rows×cols) representing
    // "connected to border". Union every border 'O' cell with this virtual node.
    // After processing all cells, any 'O' whose root ≠ virtual node root is surrounded.
    //
    // Virtual node pattern: when you want to mark a GROUP of nodes as "special",
    // create one virtual node and union all special nodes to it.
    // Then check: find(cell) == find(virtual) in O(α) per query.
    //
    // Advantage over BFS: no board mutation, O(α) connectivity queries after build,
    // handles dynamic boards (new cells added online) via incremental union().
    public void solveUF(char[][] board) {
        int rows = board.length, cols = board[0].length;
        int total = rows * cols;
        int virtual = total; // virtual border node

        int[] parent = new int[total + 1];
        int[] rank   = new int[total + 1];
        for (int i = 0; i <= total; i++) parent[i] = i;

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != 'O') continue;

                int idx = r * cols + c;
                boolean onBorder = (r == 0 || r == rows-1 || c == 0 || c == cols-1);

                // Border 'O' → union with virtual node (mark entire component as safe)
                if (onBorder) union(parent, rank, idx, virtual);

                // Union with adjacent 'O' cells already processed (right + down suffice
                // since we scan left→right top→bottom; BFS approach handles all 4 dirs)
                for (int[] d : dirs) {
                    int nr = r+d[0], nc = c+d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && board[nr][nc] == 'O')
                        union(parent, rank, idx, nr*cols+nc);
                }
            }
        }

        // Flip: any 'O' not connected to virtual node is surrounded
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (board[r][c] == 'O'
                    && find(parent, r*cols+c) != find(parent, virtual))
                    board[r][c] = 'X';
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) parent[x] = find(parent, parent[x]);
        return parent[x];
    }

    private void union(int[] parent, int[] rank, int x, int y) {
        int px = find(parent, x), py = find(parent, y);
        if (px == py) return;
        if      (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }
    }
}

/*
 * Complexity
 * ----------
 * BFS:         Time O(mn),          Space O(mn) queue + in-place marker
 * Union-Find:  Time O(mn α(mn)),    Space O(mn) parent + rank arrays
 *
 * When to prefer Union-Find over BFS:
 *   - No board mutation needed (immutable input constraint)
 *   - Multiple "is this cell surrounded?" queries after one-time build
 *   - Dynamic board: new cells added online → just call union() incrementally
 *   - Cleaner separation of build phase and query phase
 *
 * Why the virtual node trick works:
 *   Connectivity is an equivalence relation. The virtual node creates one
 *   representative for the entire "safe" equivalence class. After all unions,
 *   find(cell) == find(virtual) iff cell is in the same component as any
 *   border 'O' — i.e., not surrounded.
 *
 * Trace — BFS approach
 * ----------------------
 * Board:
 *   X X X X
 *   X O O X
 *   X X O X
 *   X O X X
 *
 * Border scan: 'O' at (3,1) → mark 'S', enqueue
 * BFS from (3,1): neighbours are 'X' — no 'O' reachable
 *
 * Pass 2:
 *   (1,1)='O' → 'X'  (surrounded)
 *   (1,2)='O' → 'X'  (surrounded)
 *   (2,2)='O' → 'X'  (surrounded)
 *   (3,1)='S' → 'O'  (restored)
 *
 * Result:
 *   X X X X
 *   X X X X
 *   X X X X
 *   X O X X ✓
 *
 * Trace — Union-Find approach
 * ----------------------------
 * Indices: (1,1)=5, (1,2)=6, (2,2)=10, (3,1)=13, virtual=16
 *
 * (3,1) on border → union(13, 16): component {13, 16}
 * (1,1): union(5, 6)  [with right neighbour (1,2)]
 * (1,2): union(6, 10) [with below neighbour (2,2)]
 * Components: {5,6,10}, {13,16}
 *
 * find(5) ≠ find(16) → (1,1) surrounded → 'X'
 * find(6) ≠ find(16) → (1,2) surrounded → 'X'
 * find(10) ≠ find(16) → (2,2) surrounded → 'X'
 * find(13) == find(16) → (3,1) safe → keep 'O' ✓
 */
