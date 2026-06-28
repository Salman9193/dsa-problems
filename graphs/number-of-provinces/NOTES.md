# Number of Provinces — Notes & Intuition

**LeetCode #547** | Graphs / DFS / BFS / Union-Find | Medium

---

## Problem

Given an n×n adjacency matrix `isConnected`, count the number of connected
components (provinces). Cities i and j are in the same province if there is
a path between them (directly or through other cities).

```
[[1,1,0],[1,1,0],[0,0,1]]  →  2  ({0,1} and {2})
[[1,0,0],[0,1,0],[0,0,1]]  →  3  ({0}, {1}, {2})
```

---

## This vs Number of Islands (#200)

| Feature | #200 Number of Islands | #547 Number of Provinces |
|---------|----------------------|--------------------------|
| Graph repr | Implicit (2D grid) | Explicit (adjacency matrix) |
| Node | Grid cell | City index |
| Neighbour finding | 4-directional moves | Scan adjacency matrix row |
| "Visited" | Sink '1'→'0' or boolean[][] | boolean[] visited |
| Time | O(mn) | O(n²) |

Same connected component counting — different graph representation.

---

## Three Approaches

### DFS — cleanest code

```java
for each unvisited city i:
    provinces++
    dfs(i)  // marks all cities in i's province as visited
```

### BFS — no recursion risk

```java
for each unvisited city i:
    provinces++
    BFS from i, marking all reachable cities visited
```

### Union-Find — best for multiple queries

```java
components = n
for i in 0..n-1:
    for j in i+1..n-1:  // upper triangle only (matrix is symmetric)
        if isConnected[i][j] == 1:
            if find(i) != find(j): union(i,j); components--
return components
```

**Why upper triangle only?** Matrix is symmetric: `isConnected[i][j] == isConnected[j][i]`.
Processing `(i,j)` and `(j,i)` would produce redundant union operations.
Upper triangle `j > i` covers each edge exactly once.

---

## Comparison

| Approach | Time | Space | Best when |
|----------|------|-------|-----------|
| DFS | O(n²) | O(n) | Simple, one-time query |
| BFS | O(n²) | O(n) | Large n, avoid recursion |
| Union-Find | O(n² α(n)) | O(n) | Multiple connectivity queries |

---

## Full Trace — `[[1,1,0],[1,1,0],[0,0,1]]`

**DFS:**
```
i=0: not visited → provinces=1
  dfs(0): visited[0]=true
    row 0: col 1 connected, !visited → dfs(1)
      dfs(1): visited[1]=true
        row 1: col 0 visited; col 2 not connected
i=1: visited → skip
i=2: not visited → provinces=2
  dfs(2): visited[2]=true
return 2 ✓
```

**Union-Find:**
```
components=3
(0,1): connected → union → components=2
(0,2): not connected → skip
(1,2): not connected → skip
return 2 ✓
```

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| n=1, `[[1]]` | 1 | Single city, one province |
| All connected | 1 | One giant province |
| No edges (diagonal only) | n | n isolated cities |
| Already grouped | k | k distinct components |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #323 Number of Connected Components | Edge list instead of matrix | Union-Find or BFS |
| #684 Redundant Connection | Find the edge creating a cycle | Union-Find |
| #399 Evaluate Division | Weighted edges | Weighted Union-Find or BFS |
| #1319 Number of Operations | Make fully connected | (n-components) edges needed |
| Online: city added | Dynamic graph | Union-Find handles incremental |
| Query: same province? | Is city i in city j's province? | find(i)==find(j) after build |
