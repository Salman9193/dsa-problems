# Trie (Prefix Tree) — Complete Guide

A Trie is a tree where each path from root to a node spells a prefix.
Used for O(L) insert/search/prefix-check where L = word length —
independent of the number of words stored.

---

## Core Implementation

```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEnd = false;
}

class Trie {
    private TrieNode root = new TrieNode();

    // Insert a word — O(L)
    public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null)
                curr.children[idx] = new TrieNode();
            curr = curr.children[idx];
        }
        curr.isEnd = true;
    }

    // Search for exact word — O(L)
    public boolean search(String word) {
        TrieNode node = traverse(word);
        return node != null && node.isEnd;
    }

    // Check if any word starts with prefix — O(L)
    public boolean startsWith(String prefix) {
        return traverse(prefix) != null;
    }

    private TrieNode traverse(String s) {
        TrieNode curr = root;
        for (char c : s.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return null;
            curr = curr.children[idx];
        }
        return curr;
    }
}
```

---

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Insert word of length L | O(L) | O(L × alphabet) per new word |
| Search exact word | O(L) | O(1) extra |
| Prefix check | O(L) | O(1) extra |
| Total space for n words of avg len L | — | O(n × L × 26) worst case |

---

## Core LeetCode Problems

### #208 Implement Trie
Standard implementation above.

---

### #211 Design Add and Search Words (Wildcard)

`search("a.c")` where `.` matches any character.

```java
public boolean search(String word) {
    return dfs(word, 0, root);
}

private boolean dfs(String word, int i, TrieNode node) {
    if (i == word.length()) return node.isEnd;
    char c = word.charAt(i);
    if (c != '.') {
        TrieNode next = node.children[c - 'a'];
        return next != null && dfs(word, i+1, next);
    }
    // wildcard: try all children
    for (TrieNode child : node.children)
        if (child != null && dfs(word, i+1, child)) return true;
    return false;
}
```

---

### #212 Word Search II

Find all words from a dictionary that exist in a character grid.
Brute force: DFS for each word — O(words × rows × cols × 4^L).
Trie approach: build Trie from dictionary, single DFS over grid — O(rows × cols × 4^L).

```java
public List<String> findWords(char[][] board, String[] words) {
    Trie trie = new Trie();
    for (String w : words) trie.insert(w);

    List<String> result = new ArrayList<>();
    int r = board.length, c = board[0].length;
    for (int i = 0; i < r; i++)
        for (int j = 0; j < c; j++)
            dfs(board, i, j, trie.root, result);
    return result;
}

private void dfs(char[][] board, int i, int j, TrieNode node, List<String> result) {
    if (i<0 || i>=board.length || j<0 || j>=board[0].length) return;
    char c = board[i][j];
    if (c == '#' || node.children[c-'a'] == null) return;

    node = node.children[c-'a'];
    if (node.word != null) {     // found a complete word
        result.add(node.word);
        node.word = null;        // avoid duplicates
    }
    board[i][j] = '#';           // mark visited
    dfs(board, i+1, j, node, result);
    dfs(board, i-1, j, node, result);
    dfs(board, i, j+1, node, result);
    dfs(board, i, j-1, node, result);
    board[i][j] = c;             // restore
}
```

*Note: Add `String word` field to TrieNode; set `node.word = word` at end of insert.*

---

### #648 Replace Words

Replace every word in a sentence with its shortest prefix in the dictionary.

```java
public String replaceWords(List<String> dictionary, String sentence) {
    Trie trie = new Trie();
    for (String root : dictionary) trie.insert(root);

    String[] words = sentence.split(" ");
    StringBuilder sb = new StringBuilder();
    for (String word : words) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(shortestRoot(trie, word));
    }
    return sb.toString();
}

private String shortestRoot(Trie trie, String word) {
    TrieNode curr = trie.root;
    for (int i = 0; i < word.length(); i++) {
        int idx = word.charAt(i) - 'a';
        if (curr.children[idx] == null) break;
        curr = curr.children[idx];
        if (curr.isEnd) return word.substring(0, i+1);  // found prefix
    }
    return word;  // no prefix found
}
```

---

### #820 Short Encoding of Words

Find minimum length of encoded string using `#` as separator.
Words that are suffixes of other words share the same `#`.

```java
// Key insight: insert REVERSED words into a Trie.
// A word is NOT a suffix of another iff its reversed form is NOT a prefix
// of any other reversed word — i.e., it IS a leaf node in the Trie.
public int minimumLengthEncoding(String[] words) {
    Trie trie = new Trie();
    Map<TrieNode, Integer> nodeToLen = new HashMap<>();

    for (String w : words) {
        String rev = new StringBuilder(w).reverse().toString();
        TrieNode curr = trie.root;
        for (int i = 0; i < rev.length(); i++) {
            int idx = rev.charAt(i) - 'a';
            if (curr.children[idx] == null)
                curr.children[idx] = new TrieNode();
            curr = curr.children[idx];
        }
        nodeToLen.put(curr, w.length());
    }
    // Leaf nodes are words not suffixed by others
    int total = 0;
    for (Map.Entry<TrieNode, Integer> e : nodeToLen.entrySet()) {
        TrieNode node = e.getKey();
        if (isLeaf(node)) total += e.getValue() + 1;
    }
    return total;
}

private boolean isLeaf(TrieNode node) {
    for (TrieNode c : node.children) if (c != null) return false;
    return true;
}
```

---

## Optimisations

### 1. Compressed Trie (Patricia Trie)
Instead of one character per node, store a string segment per node.
Reduces space from O(n × L × 26) to O(n) nodes for sparse tries.

### 2. HashMap children (for large alphabets)
```java
Map<Character, TrieNode> children = new HashMap<>();
// Instead of TrieNode[] children = new TrieNode[26];
```
Better for Unicode, emoji, or arbitrary character sets.

### 3. Pruning during Word Search II
After finding a word, remove it from the Trie (set `word = null`).
Prune leaf nodes that no longer lead to any word — reduces DFS branches.

---

## When to Use Trie vs HashMap

| Scenario | Use |
|----------|-----|
| Exact word lookup only | HashMap — O(L) same, simpler |
| Prefix queries | Trie — HashMap can't do prefix in O(L) |
| Multiple pattern matching | Trie — share prefixes across patterns |
| Autocomplete | Trie — traverse to prefix node, collect all words |
| Wildcard matching | Trie + DFS — HashMap doesn't support |
| Space is critical | DAWG (Directed Acyclic Word Graph) — shared suffixes too |

---

## Extensions

| Variant | Approach |
|---------|---------|
| Autocomplete system (#642) | Trie + heap at each node for top-k suggestions |
| XOR maximisation (#421) | Binary trie; at each bit, go to opposite if available |
| Palindrome pairs (#336) | Trie of reversed words |
| Longest word in dictionary | BFS on Trie; only extend from valid words |
