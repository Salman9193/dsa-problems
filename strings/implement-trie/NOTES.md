# Implement Trie (Prefix Tree) — Notes & Intuition

**LeetCode #208** | Trie / Design | Medium

---

## Problem

Implement a Trie with insert, search, and startsWith operations.

---

## Structure

```
TrieNode:
    children[26]  // one per lowercase letter
    isEnd         // true if a word ends here

Trie:
    root = TrieNode()
```

---

## Three Operations

```
insert(word):   traverse/create nodes, mark last as isEnd
search(word):   traverse, return node != null AND isEnd
startsWith(s):  traverse, return node != null (any endpoint)
```

All O(L) where L = word length.

---

## Trie vs HashMap

| Operation | HashMap | Trie |
|-----------|---------|------|
| insert | O(L) | O(L) |
| search | O(L) avg | O(L) |
| startsWith | O(W×L) — check all words | **O(L)** |
| Autocomplete | O(W×L) | O(L + results) |

**Trie wins for prefix queries.** HashMap cannot efficiently answer
"how many words start with prefix X?" without scanning all words.

---

## Key Trie Applications

| Problem | How Trie helps |
|---------|---------------|
| #208 Implement Trie | Foundation |
| #211 Design Add and Search Words | Wildcard: DFS on '.' |
| #212 Word Search II | Prune DFS with Trie prefix |
| #421 Max XOR of Two Numbers | Binary Trie for greedy bit selection |
| Autocomplete system | Enumerate subtree of prefix node |

---

## Complexity

All operations: Time O(L) · Space O(26 × n) = O(n)
