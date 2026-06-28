# Surrounded Regions — Notes & Intuition

**LeetCode #130** | Graphs / BFS / Union-Find | Medium

---

## Problem

Capture all `'O'` regions completely surrounded by `'X'` — flip them to `'X'`.
Any `'O'` touching the border (directly or via a chain of `'O'`s) is NOT captured.

```
X X X X        X X X X
X O O X   →   X X X X
X X O X        X X X X
X O X X        X O X X   ← border-connected O stays
```

---

## Core Insight — Reverse: Find Safe Cells First

**Hard direction:** "Is this 'O' surrounded?" — must verify all paths avoid border.

**Easy direction:** "Is this 'O' connected to the border?" — BFS from border.

An `'O'` is surrounded iff it is NOT reachable from any border `'O'`.

```
Step 1: BFS from all border 'O' cells → mark connected 'O' as 'S' (safe)
Step 2: Flip remaining 'O' → 'X'  (surrounded)
Step 3: Restore 'S' → 'O'         (safe)
```

---

## Two Implementations

### BFS (3-step marker)

```java
// Seed border 'O' cells, mark 'S', BFS to spread safety
// Then: O→X (surrounded), S→O (safe)
```

Pros: Simple, O(mn), in-place (no extra arrays beyond queue).
Cons: Mutates board temporarily (needs 3 distinct values: X, O, S).

### Union-Find (virtual node)

```java
// virtual = rows×cols (extra node representing "border-connected")
// For each border 'O': union(cell, virtual)
// For each adjacent 'O' pair: union them
// Flip: 'O' cells where find(cell) ≠ find(virtual)
```

Pros: No board mutation. O(α) per query after O(mn) build.
Cons: O(mn) extra space for parent + rank arrays.

---

## The Virtual Node Pattern

When you want to mark a GROUP of nodes as "special":
1. Create one virtual node at index `n` (beyond the real nodes)
2. Union every "special" node with the virtual node
3. Query: `find(cell) == find(virtual)` → cell is special

| Problem | Virtual node = |
|---------|---------------|
| Surrounded Regions | "connected to border" |
| Number of Islands (variant) | "connected to ocean" |
| Graph Valid Tree | — (no virtual needed) |

---

## Comparison of Approaches

| Aspect | BFS (marker) | Union-Find |
|--------|-------------|-----------|
| Time | O(mn) | O(mn α(mn)) ≈ O(mn) |
| Board mutation | Yes (temp 'S') | No |
| Multiple queries | Re-run BFS | O(α) per query |
| Dynamic updates | Hard | Easy (just call union()) |
| Code complexity | Lower | Higher setup |

---

## Full Trace — BFS

```
Input:            After step 1 (BFS):    After steps 2&3:
X X X X           X X X X                X X X X
X O O X    →     X O O X    →           X X X X
X X O X           X X O X                X X X X
X O X X           X S X X                X O X X
```

(Only (3,1) is border-connected → marked 'S'. Interior O's get flipped.)

---

## Connection to Pacific Atlantic Water Flow (#417)

Both use **reverse BFS from borders**:

| Feature | Pacific Atlantic (#417) | Surrounded Regions (#130) |
|---------|------------------------|--------------------------|
| Borders | Two (Pacific + Atlantic) | One (frame) |
| Condition | Uphill ≥ | Same type 'O' |
| Result | Intersection of two sets | Complement of one set |
| Marker | Two boolean arrays | 'S' temp marker in-place |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| All border 'O' | Unchanged | All safe |
| No 'O' at all | Unchanged | Nothing to flip |
| Single 'O' not on border | Flipped | Surrounded by X |
| 1×1 board `['O']` | `['O']` | Border cell — safe |
| Fully enclosed region | All flipped | None connected to border |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #1020 Number of Enclaves | Count surrounded 'O' cells | Same BFS; count non-'S' after |
| 8-connectivity | Diagonals count | Add 4 diagonal dirs |
| Multiple frame layers | n-cell border is "safe" | Expand border seed by n layers |
| Dynamic: cells added | Online updates | Union-Find handles incrementally |
| 3D grid | Depth dimension | Same with 6 directions |
