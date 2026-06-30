# Most Stones Removed with Same Row or Column — Notes & Intuition

**LeetCode #947** | Graphs / Union-Find | Medium

---

## Problem

A stone can be removed if it shares a row or column with another stone
still on the plane. Return the maximum number of stones removable.

```
stones=[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]  →  5
stones=[[0,0],[0,2],[1,1],[2,0],[2,2]]        →  3
stones=[[0,0]]                                 →  0
```

---

## Core Insight — Connected Components

Stones sharing a row or column form a connected component (think of each
shared row/column as an edge linking the stones). Within each component,
you can always remove all stones but one — repeatedly remove a stone
connected to any remaining stone in the group.

```
Max removable stones = total stones - number of connected components
```

A lone stone with no shared row/column is its own component of size 1 —
contributes 0 removable stones (it can never be removed).

---

## Why Row/Column Union (Not Pairwise Stone Comparison)

Comparing every pair of stones for shared row/column is O(n²). Instead,
union each stone's ROW id with its COLUMN id — rows and columns become
DSU "hub" nodes. Stones sharing a row automatically land in the same
component because they all touch the same row hub.

```java
for each stone (r, c):
    union(rowId(r), colId(c))
```

Each stone does exactly ONE union → O(n α(n)) total.

---

## The Offset Trick — and Why It's Fragile

A common implementation shares one `int[]` DSU array between rows and
columns by offsetting column indices: `col + 10000`.

```java
UnionFind uf = new UnionFind(20001);
uf.union(row, col + 10000);
```

**This silently breaks if any coordinate ≥ 10000** — columns start
colliding with row ids, producing wrong answers with no error thrown.
The magic number `10000` only happens to work because LeetCode's
constraint caps coordinates below that value.

---

## Robust Alternative — Dynamic Compact ID Space

Instead of assuming a coordinate range, assign sequential integer ids to
rows and columns AS THEY ARE FIRST ENCOUNTERED:

```java
Map<Integer,Integer> rowId = new HashMap<>(), colId = new HashMap<>();
int id = 0;
for (stone : stones) {
    rowId.putIfAbsent(stone[0], id++);
    colId.putIfAbsent(stone[1], id++);
}
UnionFind uf = new UnionFind(id);  // exactly the size needed
```

**Why this is strictly better:**
- No magic number, no assumption about coordinate range
- Correct for negative coordinates, huge coordinates, sparse coordinates
- DSU array sized exactly — no wasted space

---

## Third Option — No DSU at All (BFS/DFS)

Build independent row-group and column-group adjacency maps (keyed by
stone index, not row/col id). DFS each unvisited stone, following
whichever group applies.

```java
rowGroups: row value -> list of stone indices in that row
colGroups: col value -> list of stone indices in that column
DFS from each unvisited stone, exploring both its row group and col group
```

No shared index space needed at all — rows and columns are looked up
independently.

---

## Comparison

| Approach | Robust to coordinate range? | Extra space | Notes |
|----------|------------------------------|--------------|-------|
| Offset trick | No — silently breaks | O(fixed constant) | Avoid in production code |
| Dynamic-ID DSU | Yes | O(n) | **Recommended** |
| BFS/DFS | Yes (no DSU needed) | O(n) | Good if DSU unavailable/non-idiomatic |

---

## Full Trace — `[[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]]` (dynamic-ID)

```
ids: row0=0, col0=1, col1=2, row1=3, col2=4, row2=5

union(row0,col0): {0,1}
union(row0,col1): {0,1,2}
union(row1,col0): {0,1,2,3}
union(row1,col2): {0,1,2,3,4}
union(row2,col1): {0,1,2,3,4,5}
union(row2,col2): already same

1 component, 6 stones → removable = 6-1 = 5 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(n α(n)) — n stones, each does O(1) amortised union |
| Space | O(n) — DSU + id maps (dynamic-ID approach) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| Single stone | 0 | No shared row/col — can never be removed |
| All stones same row | n-1 | One giant component |
| No two stones share row/col | 0 | n separate components, each size 1 |
| Negative coordinates | Correct with dynamic-ID; WRONG with offset trick | Offset assumes non-negative range |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return WHICH stones remain | Need actual survivors, not just count | Track one representative per DSU root |
| 3D stones (row, col, depth) | Three shared dimensions | Union three hub types per stone |
| Weighted removal cost | Minimise cost, not maximise count | More complex: track minimum-cost spanning structure |
| Streaming stones | Stones added incrementally | DSU naturally supports online unions |
