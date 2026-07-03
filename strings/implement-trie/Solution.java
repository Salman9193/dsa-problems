class Trie {

    // Trie (Prefix Tree) — the foundational implementation.
    // Each node has up to 26 children (one per lowercase letter) + isEnd flag.
    //
    // Three operations: insert, search, startsWith.
    // All run in O(L) where L = word length, regardless of number of stored words.
    //
    // Trie excels at:
    //   - Prefix queries (startsWith): O(L) — impossible with HashMap
    //   - Autocomplete: enumerate all words sharing a prefix
    //   - Word Search II: prune DFS by Trie prefix
    //   - XOR maximisation: binary Trie

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null)
                node.children[idx] = new TrieNode();
            node = node.children[idx];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = traverse(word);
        return node != null && node.isEnd; // word must end at this node
    }

    public boolean startsWith(String prefix) {
        return traverse(prefix) != null; // any node reached = prefix exists
    }

    private TrieNode traverse(String s) {
        TrieNode node = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return null;
            node = node.children[idx];
        }
        return node;
    }

    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isEnd = false;
    }
}

/*
 * Complexity: All operations O(L) time, O(L) space per insert
 * Total space: O(ALPHABET × total_chars) — O(26n) for lowercase English
 *
 * search vs startsWith:
 *   search("apple"):     traverse to 'e', check isEnd=true
 *   startsWith("app"):   traverse to second 'p', just check != null
 *
 * HashMap alternative:
 *   HashSet<String> handles search in O(L) average.
 *   But startsWith requires iterating all words → O(W×L).
 *   Trie makes startsWith O(L) — this is the key advantage.
 *
 * Common Trie extensions:
 *   - Count words with prefix (store count at each node)
 *   - Delete word (DFS + remove dead branches)
 *   - Wildcard search (DFS with '.' matching any char)
 *   - Binary Trie (for XOR maximisation problems)
 */
