# Word Search II — Notes & Intuition

**LeetCode #212** | Trie / Backtracking | Hard

---

## Problem

Given an `m x n` board of characters and a list of `words`, return all words
that can be formed by a path of **horizontally or vertically adjacent** cells,
where no cell is used more than once in a single word.

This is the capstone Trie problem — it combines a Trie, grid DFS, and
backtracking. Google favours it because the naive solution is exponential and
the interviewer wants to see you reach the Trie-pruned version.

---

## Why Not Just Run Word Search I Per Word

Word Search I finds *one* word via DFS from every cell: `O(M · 4 · 3^(L-1))`.
Doing that once **per word** gives `O(W · M · 4 · 3^(L-1))` for `W` words —
the board is re-searched `W` times with zero shared work, and common prefixes
("app", "apple", "apply") are re-walked every time.

---

## The Trie Insight

Put **all** words into a Trie, then DFS the board **once**. At each cell, follow
the Trie edge for that character:

- If there is no such edge, **stop immediately** — no word has this prefix.
- If you reach a terminal node, you've spelled a complete word.

One board traversal searches for every word simultaneously, and shared prefixes
are walked a single time. The Trie *is* the pruning mechanism.

---

## Building the Trie

Store the **full word** at the terminal node (not a boolean), so DFS can add it
directly with no path reconstruction:

```java
static class TrieNode {
    TrieNode[] children = new TrieNode[26];
    String word = null;   // set only at a node that completes a word
}

TrieNode buildTrie(String[] words) {
    TrieNode root = new TrieNode();
    for (String w : words) {
        TrieNode node = root;
        for (char ch : w.toCharArray()) {
            int idx = ch - 'a';
            if (node.children[idx] == null) node.children[idx] = new TrieNode();
            node = node.children[idx];
        }
        node.word = w;
    }
    return root;
}
```

---

## The DFS

```java
void dfs(char[][] board, int r, int c, TrieNode node, List<String> result) {
    if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;
    char ch = board[r][c];
    if (ch == '#') return;                        // used on this path

    TrieNode next = node.children[ch - 'a'];
    if (next == null) return;                     // PRUNE: no word has this prefix

    if (next.word != null) {                      // completed a word
        result.add(next.word);
        next.word = null;                         // de-dup without a HashSet
    }

    board[r][c] = '#';                            // mark visited
    dfs(board, r+1, c, next, result);
    dfs(board, r-1, c, next, result);
    dfs(board, r, c+1, next, result);
    dfs(board, r, c-1, next, result);
    board[r][c] = ch;                             // restore (backtrack)
}
```

---

## The Two Tricks That Matter

1. **Store the word at the terminal node.** When DFS reaches it, add the word
   directly — no need to track the path of characters.

2. **Null out `next.word` after adding.** If the same word can be spelled by a
   second path, this prevents a duplicate in the result — cheaper than
   de-duping the result list afterward.

3. **In-place `#` marker + restore.** Classic grid backtracking: mark the cell
   used, recurse, then restore the original character so *other* paths can use
   that cell.

---

## Full Trace

```
board = o a a n     words = ["oath","pea","eat","rain"]
        e t a e
        i h k r
        i f l v

Trie paths: o-a-t-h, p-e-a, e-a-t, r-a-i-n

DFS (0,0)='o': follow o->a->t->h  => "oath" found, null it
DFS (1,0)='e': follow e->a->t     => "eat"  found, null it
Any DFS starting with 'p': no 'p' cell -> Trie has no 'p' child at some
   point -> pruned instantly.

result = ["oath", "eat"]   (order depends on scan order)
```

---

## Complexity

| | Bound |
|--|-------|
| Build Trie | O(total chars across all words) |
| Search | O(M · 4 · 3^(L-1)) — M cells, L = longest word |
| Space | O(total chars) for Trie + O(L) recursion |

The Trie shares prefix work and prunes dead paths, so in practice this is far
below the worst case for realistic word lists.

---

## Edge Cases

| Case | Handling |
|------|----------|
| Empty board or empty words | Return empty list |
| Word longer than board cells | Naturally pruned — path runs out |
| Duplicate words in input | Trie stores once; found once |
| Single cell board | Only length-1 words can match |
| Word using same letter twice adjacent | `#` marker prevents cell reuse |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| **Word Search I** (#79) | Single word | Plain DFS + backtracking, no Trie needed |
| Diagonal moves allowed | 8 directions | Add 4 diagonal recursions |
| Return **positions** of each word | Track path | Push/pop coordinates during DFS |
| Very large dictionary | Millions of words | Prune dead Trie leaves after finding a word |
| Words with wildcards (`.`) | Pattern match | At `.`, try all 26 children (like #211) |
| Boggle scoring | Weighted result | Accumulate score per found word during DFS |

**Trade-off:** the Trie costs O(total characters) memory up front, but turns
`W` independent board searches into one pruned traversal. For large word lists
this is the difference between passing and timing out. Pruning dead Trie leaves
as words are consumed keeps the effective structure small over a long search.
