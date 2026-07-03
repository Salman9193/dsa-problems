class Solution {

    // Word Search: does the word exist in the grid following adjacent cells
    // (up/down/left/right), not reusing cells?
    //
    // Backtracking on a 2D grid: mark cell as visited (in-place with '#'),
    // recurse into all 4 neighbours, then restore the cell.

    public boolean exist(char[][] board, String word) {
        int m = board.length, n = board[0].length;

        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                if (board[r][c] == word.charAt(0) && backtrack(board, word, r, c, 0))
                    return true;

        return false;
    }

    private boolean backtrack(char[][] board, String word, int r, int c, int idx) {
        if (idx == word.length()) return true; // found entire word

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return false;
        if (board[r][c] != word.charAt(idx)) return false;

        char tmp = board[r][c];
        board[r][c] = '#'; // mark as visited (in-place — no extra visited array)

        boolean found = backtrack(board, word, r+1, c, idx+1)
                     || backtrack(board, word, r-1, c, idx+1)
                     || backtrack(board, word, r, c+1, idx+1)
                     || backtrack(board, word, r, c-1, idx+1);

        board[r][c] = tmp; // restore (backtrack)
        return found;
    }
}

/*
 * Complexity: Time O(m × n × 4^L) where L = word length
 *   For each starting cell, DFS explores at most 4^L paths.
 *   In practice much faster due to early termination on character mismatch.
 * Space: O(L) — recursion depth = word length
 *
 * Key tricks:
 *   1. In-place visited marking (board[r][c]='#') — avoids extra O(m×n) space
 *   2. Early termination: check character match BEFORE recursing
 *   3. Short-circuit OR: stop as soon as any branch returns true
 *
 * Word Search II (#212 — find ALL words from a list):
 *   Build a Trie from all words. DFS with Trie traversal.
 *   Prune when no Trie node exists for the current prefix.
 *   This reduces from O(m×n×L×W) to O(m×n×4^L) amortised.
 */
