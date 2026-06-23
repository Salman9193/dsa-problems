# Union-Find (Disjoint Set Union) — Complete Guide

Union-Find is a data structure that tracks a set of elements partitioned
into disjoint (non-overlapping) groups. It supports two core operations in
near-O(1) amortised time — critical for graph connectivity problems.

---

## The Two Operations

```
find(x)  → which group does x belong to? (returns group representative)
union(x, y) → merge the groups containing x and y
```

---

## Implementation with Path Compression + Union by Rank

```java
class UnionFind {
    int[] parent;
    int[] rank;
    int components;  // number of distinct groups

    UnionFind(int n) {
        parent = new int[n];
        rank   = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;  // each node is its own root
    }

    // Path compression: make every node point directly to root
    int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);  // recursive path compression
        return parent[x];
    }

    // Union by rank: attach smaller tree under larger tree
    boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false;  // already in same group

        if      (rank[px] < rank[py]) parent[px] = py;
        else if (rank[px] > rank[py]) parent[py] = px;
        else { parent[py] = px; rank[px]++; }

        components--;
        return true;
    }

    boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
```

---

## Why Near-O(1) Per Operation?

- **Path compression:** After `find(x)`, every node on the path to root
  points directly to the root. Future finds along this path are O(1).
- **Union by rank:** Always attach the shorter tree under the taller one.
  Prevents chains — tree height stays O(log n).
- **Together:** Amortised O(α(n)) per operation where α is the inverse
  Ackermann function — effectively O(1) for all practical n.

---

## Core LeetCode Problems

### #200 Number of Islands
Count connected components in a 2D grid.

```java
public int numIslands(char[][] grid) {
    int r = grid.length, c = grid[0].length;
    UnionFind uf = new UnionFind(r * c);
    int water = 0;

    int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
    for (int i = 0; i < r; i++) {
        for (int j = 0; j < c; j++) {
            if (grid[i][j] == '0') { water++; continue; }
            for (int[] d : dirs) {
                int ni = i+d[0], nj = j+d[1];
                if (ni>=0 && ni<r && nj>=0 && nj<c && grid[ni][nj]=='1')
                    uf.union(i*c+j, ni*c+nj);
            }
        }
    }
    return uf.components - water;  // subtract water cells
}
```

---

### #684 Redundant Connection
Find the edge that creates a cycle. The first union() that returns false
(both nodes already connected) is the redundant edge.

```java
public int[] findRedundantConnection(int[][] edges) {
    UnionFind uf = new UnionFind(edges.length + 1);
    for (int[] e : edges)
        if (!uf.union(e[0], e[1])) return e;
    return new int[]{};
}
```

**Key insight:** Process edges one by one. When union() returns false,
both endpoints are already in the same component → this edge creates a cycle.

---

### #261 Graph Valid Tree
n nodes form a valid tree iff:
1. Exactly n-1 edges
2. All nodes are connected (one component)

```java
public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) return false;
    UnionFind uf = new UnionFind(n);
    for (int[] e : edges)
        if (!uf.union(e[0], e[1])) return false;  // cycle found
    return uf.components == 1;
}
```

---

### #323 Number of Connected Components
```java
public int countComponents(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);
    for (int[] e : edges) uf.union(e[0], e[1]);
    return uf.components;
}
```

---

### #721 Accounts Merge
Group emails belonging to the same person.

```java
public List<List<String>> accountsMerge(List<List<String>> accounts) {
    Map<String, String> emailToName = new HashMap<>();
    Map<String, String> parent = new HashMap<>();

    // Helper find with path compression on strings
    // For each account, union all emails under the first email
    for (List<String> account : accounts) {
        String name = account.get(0);
        String root = account.get(1);
        for (int i = 1; i < account.size(); i++) {
            String email = account.get(i);
            parent.putIfAbsent(email, email);
            emailToName.put(email, name);
            // union email with root
            String pe = find(parent, email), pr = find(parent, root);
            if (!pe.equals(pr)) parent.put(pe, pr);
        }
    }
    // Group by root
    Map<String, List<String>> groups = new HashMap<>();
    for (String email : parent.keySet()) {
        String root = find(parent, email);
        groups.computeIfAbsent(root, k -> new ArrayList<>()).add(email);
    }
    List<List<String>> result = new ArrayList<>();
    for (Map.Entry<String, List<String>> e : groups.entrySet()) {
        List<String> group = e.getValue();
        Collections.sort(group);
        group.add(0, emailToName.get(e.getKey()));
        result.add(group);
    }
    return result;
}

private String find(Map<String, String> parent, String x) {
    if (!parent.get(x).equals(x))
        parent.put(x, find(parent, parent.get(x)));
    return parent.get(x);
}
```

---

### #1202 Smallest String with Swaps
If (i,j) is a swap pair, characters at i and j can be swapped freely.
Nodes in the same component can be rearranged among themselves.

```java
public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
    int n = s.length();
    UnionFind uf = new UnionFind(n);
    for (List<Integer> p : pairs) uf.union(p.get(0), p.get(1));

    // Group indices by root
    Map<Integer, PriorityQueue<Character>> groups = new HashMap<>();
    for (int i = 0; i < n; i++) {
        int root = uf.find(i);
        groups.computeIfAbsent(root, k -> new PriorityQueue<>()).offer(s.charAt(i));
    }
    // Build result: for each index, take smallest char from its group
    char[] result = new char[n];
    for (int i = 0; i < n; i++)
        result[i] = groups.get(uf.find(i)).poll();
    return new String(result);
}
```

---

### Kruskal's MST (Union-Find Application)
Build a Minimum Spanning Tree by greedily adding cheapest edges that
don't form cycles.

```java
public int minimumCostMST(int n, int[][] edges) {
    Arrays.sort(edges, (a, b) -> a[2] - b[2]);  // sort by weight
    UnionFind uf = new UnionFind(n);
    int cost = 0, edgesUsed = 0;

    for (int[] e : edges) {
        if (uf.union(e[0], e[1])) {
            cost += e[2];
            if (++edgesUsed == n - 1) break;
        }
    }
    return edgesUsed == n - 1 ? cost : -1;  // -1 if graph disconnected
}
```

---

## Complexity Summary

| Operation | Naive | With Path Compression + Union by Rank |
|-----------|-------|---------------------------------------|
| find(x) | O(n) | O(α(n)) ≈ O(1) amortised |
| union(x,y) | O(n) | O(α(n)) ≈ O(1) amortised |
| n operations | O(n²) | O(n α(n)) ≈ O(n) |

α(n) = inverse Ackermann function. α(n) ≤ 4 for all n ≤ 10^(10^(10^10)).

---

## When to Use Union-Find vs BFS/DFS

| Scenario | Use |
|----------|-----|
| Static graph, check connectivity | Union-Find — simpler, O(1) per query after build |
| Dynamic graph, edges added online | Union-Find — supports incremental edge additions |
| Need shortest path | BFS/DFS — Union-Find doesn't track distances |
| Find cycle in undirected graph | Union-Find — cleaner than DFS |
| Need the actual path/cycle | BFS/DFS — Union-Find doesn't store paths |
| Detect back-edge in directed graph | DFS with colour marking — Union-Find is for undirected |

---

## Extensions

| Variant | Approach |
|---------|---------|
| Weighted union-find | Store weight/rank relative to parent |
| Rollback (offline) | Store union history; undo with stack |
| Parallel union-find | Lock per root; concurrent find is safe |
| Persistence | Functional data structure; copy-on-write |

---

## Problems in This Repo Using Union-Find Concepts

- **#4 Clone Graph** — connected component traversal
- **#38 Is Graph Bipartite?** — 2-colouring = 2-component check
