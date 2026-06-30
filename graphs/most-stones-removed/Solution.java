import java.util.*;

class Solution {

    // Key insight: stones sharing a row or column form a connected component.
    // Within each component, all stones but one can be removed (repeatedly
    // remove a stone connected to any remaining stone in the same group).
    //
    //   max removable = total stones - number of connected components
    //
    // The interesting engineering question: HOW do we union stones that
    // share a row or column without comparing every pair (O(n^2))?
    // Answer: union each stone's ROW with its COLUMN, using row/column ids
    // as the DSU nodes (not stone indices). Stones sharing a row or column
    // end up in the same component because they touch the same row/column
    // hub node, which connects them transitively.

    // ── Approach 1 (FRAGILE): numeric offset trick ────────────────────────
    //
    // Shares one int[] DSU array between rows and columns by offsetting
    // column indices by a constant (10000) to avoid collision with row indices.
    //
    // WHY THIS BREAKS: it silently assumes max coordinate < 10000.
    // If y >= 10000, columns start colliding with row ids — WRONG results,
    // no exception thrown. This is a correctness bug waiting to happen on
    // any coordinate range not matching LeetCode's specific constraints.
    public int removeStonesOffset(int[][] stones) {
        int n = stones.length;
        UnionFind uf = new UnionFind(20001); // rows: 0-9999, cols offset by 10000

        for (int[] stone : stones) {
            int row = stone[0];
            int col = stone[1] + 10000; // FRAGILE: magic number, range-dependent
            uf.union(row, col);
        }

        Set<Integer> roots = new HashSet<>();
        for (int[] stone : stones) roots.add(uf.find(stone[0]));

        return n - roots.size();
    }

    // ── Approach 2 (PREFERRED): dynamic compact ID space — no offset ──────
    //
    // Instead of assuming a coordinate range, assign sequential integer ids
    // to rows and columns AS THEY ARE ENCOUNTERED. This works for ANY
    // coordinate range — negative numbers, huge values, sparse coordinates —
    // because we never reserve space based on assumed magnitude.
    //
    // Why this is strictly better than the offset trick:
    //   - No magic number, no range assumption
    //   - DSU array is sized EXACTLY to what's needed (no wasted 20001 slots)
    //   - Correctness doesn't silently depend on input constraints
    public int removeStones(int[][] stones) {
        Map<Integer, Integer> rowId = new HashMap<>();
        Map<Integer, Integer> colId = new HashMap<>();
        int id = 0;

        // Assign compact ids dynamically as rows/columns are first seen
        for (int[] stone : stones) {
            rowId.putIfAbsent(stone[0], id++);
            colId.putIfAbsent(stone[1], id++);
        }

        UnionFind uf = new UnionFind(id); // exactly the size needed

        for (int[] stone : stones)
            uf.union(rowId.get(stone[0]), colId.get(stone[1]));

        Set<Integer> roots = new HashSet<>();
        for (int[] stone : stones)
            roots.add(uf.find(rowId.get(stone[0])));

        return stones.length - roots.size();
    }

    // Reference UnionFind (see guides/UNION_FIND.md for the full template)
    static class UnionFind {
        int[] parent, rank;
        UnionFind(int n) {
            parent = new int[n]; rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]); // path compression
            return parent[x];
        }
        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if      (rank[px] < rank[py]) parent[px] = py;
            else if (rank[px] > rank[py]) parent[py] = px;
            else { parent[py] = px; rank[px]++; }
        }
    }

    // ── Approach 3 (NO DSU): BFS/DFS on explicit row/column maps ───────────
    //
    // Avoids both the offset problem AND Union-Find entirely. Build
    // independent row-group and column-group maps (stone indices, not
    // row/col ids), then DFS each unvisited stone, following whichever
    // map applies. No shared index space needed at all.
    public int removeStonesBFS(int[][] stones) {
        int n = stones.length;
        Map<Integer, List<Integer>> rowGroups = new HashMap<>();
        Map<Integer, List<Integer>> colGroups = new HashMap<>();

        for (int i = 0; i < n; i++) {
            rowGroups.computeIfAbsent(stones[i][0], k -> new ArrayList<>()).add(i);
            colGroups.computeIfAbsent(stones[i][1], k -> new ArrayList<>()).add(i);
        }

        boolean[] visited = new boolean[n];
        int components = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            components++;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(i);
            visited[i] = true;

            while (!stack.isEmpty()) {
                int curr = stack.pop();
                for (int next : rowGroups.get(stones[curr][0]))
                    if (!visited[next]) { visited[next] = true; stack.push(next); }
                for (int next : colGroups.get(stones[curr][1]))
                    if (!visited[next]) { visited[next] = true; stack.push(next); }
            }
        }

        return n - components;
    }
}

/*
 * Complexity
 * ----------
 * Offset DSU:        Time O(n α(n)), Space O(20001) — wasteful, fragile
 * Dynamic-ID DSU:     Time O(n α(n)), Space O(n) — exact, robust
 * BFS/DFS (no DSU):   Time O(n) amortised, Space O(n) — adjacency-list overhead
 *
 * Why row/column union (not stone-to-stone pairwise comparison)?
 *   Pairwise: for each pair of stones, check shared row/col → O(n^2)
 *   Row/col hub: each stone does exactly ONE union (row id, col id) → O(n α(n))
 *   Stones sharing a row/col automatically land in the same component because
 *   they all union with the SAME row or column hub node — no pairwise check needed.
 *
 * Why is the offset trick fragile (and the dynamic-ID approach robust)?
 *   Offset hardcodes an assumption: col + 10000 only works if col < 10000.
 *   If a stone has y=15000, it COLLIDES with row id 5000 — silent wrong answer,
 *   no crash, no warning. The dynamic-ID approach NEVER makes this assumption:
 *   ids are assigned purely by "first time seen," so it is correct for ANY
 *   coordinate range, including negative coordinates or coordinates up to
 *   Integer.MAX_VALUE.
 *
 * Trace — stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]] (dynamic-ID approach)
 * ------------------------------------------------------------------------
 * Assign ids as encountered:
 *   row0 -> 0, col0 -> 1, col1 -> 2, row1 -> 3, col2 -> 4, row2 -> 5
 *
 * union(row0=0, col0=1)
 * union(row0=0, col1=2)   -> {0,1,2}
 * union(row1=3, col0=1)   -> {0,1,2,3}
 * union(row1=3, col2=4)   -> {0,1,2,3,4}
 * union(row2=5, col1=2)   -> {0,1,2,3,4,5}
 * union(row2=5, col2=4)   -> already same
 *
 * All stones end up in ONE component (everything connects through shared
 * rows/columns) → roots.size()=1 → removable = 6-1 = 5 ✓
 */
