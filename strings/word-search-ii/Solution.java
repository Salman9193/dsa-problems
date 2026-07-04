import java.util.*;

class Solution {

    // Approach: Trie + DFS with pruning — O(M * 4 * 3^(L-1)) worst case
    //
    // M = cells in board, L = max word length.
    //
    // Naive idea: for each word, run Word Search I (a DFS from every cell).
    // That re-walks the board once PER WORD and shares no work.
    //
    // Better: put ALL words into a Trie, then DFS the board ONCE. At each cell
    // we follow Trie edges — the moment the current path isn't a prefix of any
    // word, we stop. One board traversal simultaneously searches for every word,
    // and the Trie prunes dead paths immediately.

    // Trie node: 26 children + the full word stored at its terminal node.
    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word = null;   // non-null only at a node that completes a word
    }

    public List<String> findWords(char[][] board, String[] words) {
        TrieNode root = buildTrie(words);
        List<String> result = new ArrayList<>();

        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dfs(board, r, c, root, result);
            }
        }
        return result;
    }

    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode node = root;
            for (char ch : w.toCharArray()) {
                int idx = ch - 'a';
                if (node.children[idx] == null) node.children[idx] = new TrieNode();
                node = node.children[idx];
            }
            node.word = w;   // mark the terminal node with the complete word
        }
        return root;
    }

    private void dfs(char[][] board, int r, int c, TrieNode node, List<String> result) {
        // Bounds check.
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;

        char ch = board[r][c];
        if (ch == '#') return;                       // already used on this path

        TrieNode next = node.children[ch - 'a'];
        if (next == null) return;                    // PRUNE: no word has this prefix

        if (next.word != null) {                     // reached a complete word
            result.add(next.word);
            next.word = null;                        // de-dup: don't add it twice
        }

        // Mark visited, explore 4 neighbours, then restore (backtrack).
        board[r][c] = '#';
        dfs(board, r + 1, c, next, result);
        dfs(board, r - 1, c, next, result);
        dfs(board, r, c + 1, next, result);
        dfs(board, r, c - 1, next, result);
        board[r][c] = ch;                            // restore for other paths
    }
}

/*
 * Complexity
 * ----------
 * Build trie: O(total characters in all words)
 * Search:     O(M * 4 * 3^(L-1)) worst case
 *   M = board cells, L = longest word.
 *   From each of M start cells, the first step has 4 directions; every step
 *   after cannot revisit the cell it came from, so ~3 choices, for up to L deep.
 * Space:      O(total characters in all words) for the trie + O(L) recursion.
 *
 * Why a Trie beats running Word Search I per word
 * -----------------------------------------------
 * Per-word DFS: O(W * M * 4 * 3^(L-1)) for W words — the board is re-searched
 * W times with no shared work. The Trie collapses all W searches into ONE board
 * traversal, and prunes the instant a path stops being a prefix of ANY word.
 * Shared prefixes ("app", "apple", "apply") are walked once, not three times.
 *
 * Two important tricks
 * --------------------
 * 1) Store the full word AT the terminal Trie node (not a boolean isEnd).
 *    When DFS reaches it, we can add the word directly with no path bookkeeping.
 * 2) Set `next.word = null` after adding, so the same word found via a second
 *    path isn't added twice — cheaper than a HashSet on the result.
 *
 * Trace — board = [[o,a,a,n],       words = ["oath","pea","eat","rain"]
 *                  [e,t,a,e],
 *                  [i,h,k,r],
 *                  [i,f,l,v]]
 * Trie has paths o-a-t-h, p-e-a, e-a-t, r-a-i-n.
 * DFS from (0,0)='o' follows o->a->t->h and finds "oath" -> add, null it.
 * DFS from (1,0)='e' follows e->a->t and finds "eat"  -> add, null it.
 * Paths like 'p...' never start (no 'p' on board) — pruned instantly.
 * result = ["oath", "eat"]  (order depends on traversal)
 *
 * Optimisation for very large word lists: prune dead Trie leaves after a word
 * is found (remove terminal nodes with no children) so later DFS steps skip
 * them — keeps the effective Trie small as words get consumed.
 */
