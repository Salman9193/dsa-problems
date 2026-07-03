# Word Search — Notes & Intuition

**LeetCode #79** | Backtracking | Medium

---

## Problem

Given a 2D grid of characters, find if a word exists using adjacent cells
(4-directional), without reusing the same cell.

---

## Backtracking on a 2D Grid

```
for each starting cell (r,c) matching word[0]:
    backtrack(r, c, idx=0)

backtrack(r, c, idx):
    if idx == word.length: return true  (found!)
    if out of bounds or board[r][c] != word[idx]: return false
    mark board[r][c] = '#'             (in-place visited)
    found = recurse into 4 neighbours with idx+1
    board[r][c] = restore              (backtrack)
    return found
```

---

## In-Place Visited Marking

Instead of a separate `boolean[][] visited`, temporarily overwrite the cell
with `'#'` (or any non-alphabetic sentinel). Restore on backtrack.

Saves O(m×n) space per recursive call. The value is always restored before
the function returns → no side effects outside the call.

---

## Pruning Strategies

1. **Character mismatch early check** — check `board[r][c] != word[idx]` before recursing
2. **Short-circuit OR** — stop exploring once any branch returns true
3. **Frequency check** (optional) — count character frequencies in grid vs word;
   if grid lacks required characters, return false immediately

---

## Word Search II (#212)

Find ALL words from a list in the grid. Build a Trie from all words,
DFS the grid matching against Trie nodes. Prune when no Trie prefix exists.
Reduces from O(W × m×n × 4^L) to O(m×n × 4^L) amortised.

---

## Complexity

Time O(m×n×4^L) · Space O(L) stack depth
