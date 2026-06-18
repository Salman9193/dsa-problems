# All Possible Full Binary Trees — Notes & Intuition

**LeetCode #894** | Trees / Recursion / Memoisation | Medium

---

## Problem

Return all structurally distinct full binary trees with `n` nodes.
A full binary tree has every node with either 0 or 2 children.

```
n=1: [o]
n=3: [o-{o,o}]
n=5: 2 trees
n=7: 5 trees
```

---

## Key Observation 1 — Only Odd n Works

Full binary trees always have an **odd** number of nodes:
- Root = 1 node
- Each internal node contributes exactly 2 children
- Total = 1 + 2 × (internal nodes) = always odd

**If n is even → return empty list immediately.**

---

## Key Observation 2 — Recursive Split

Root takes 1 node. Remaining n-1 nodes split between left (L) and right (R):
```
R = n - 1 - L
```
Both L and R must be odd (since each subtree is also a full binary tree).
L ranges over: 1, 3, 5, ..., n-2

For each valid (L, R) pair, take the Cartesian product of all L-trees × R-trees,
combining them under a new root.

---

## Algorithm

```java
Map<Integer, List<TreeNode>> memo = new HashMap<>();

List<TreeNode> allPossibleFBT(int n) {
    if (n % 2 == 0) return new ArrayList<>();
    if (n == 1) return List.of(new TreeNode(0));
    if (memo.containsKey(n)) return memo.get(n);

    List<TreeNode> result = new ArrayList<>();
    for (int L = 1; L < n - 1; L += 2) {
        int R = n - 1 - L;
        for (TreeNode left  : allPossibleFBT(L))
        for (TreeNode right : allPossibleFBT(R)) {
            TreeNode root = new TreeNode(0);
            root.left = left; root.right = right;
            result.add(root);
        }
    }
    memo.put(n, result);
    return result;
}
```

---

## Why Memoisation

`allPossibleFBT(5)` is needed both for (L=5, R=1) and (L=1, R=5) when computing n=7.
Without memoisation, the same 2 trees for n=5 would be reconstructed twice.
With memoisation, each unique n is computed exactly once.

However, memoisation cannot reduce the total output size — we must
enumerate all trees, which grows exponentially as Catalan numbers.

---

## The Catalan Numbers

The number of structurally distinct full binary trees with n nodes (n odd)
equals the **((n-1)/2)-th Catalan number**:

```
C(k) = (1/(k+1)) × C(2k, k)   where k = (n-1)/2

n → k → C(k)
1 → 0 → 1
3 → 1 → 1
5 → 2 → 2
7 → 3 → 5
9 → 4 → 14
11 → 5 → 42
13 → 6 → 132
```

Growth rate: C(k) ~ 4^k / (k^(3/2) × sqrt(π)) — exponential.

---

## Full Trace — n=7

```
FBT(7):
  L=1, R=5: FBT(1) × FBT(5) = 1 × 2 = 2 combinations
  L=3, R=3: FBT(3) × FBT(3) = 1 × 1 = 1 combination
  L=5, R=1: FBT(5) × FBT(1) = 2 × 1 = 2 combinations
  Total = 5 trees = C(3) ✓

FBT(5):
  L=1, R=3: FBT(1) × FBT(3) = 1 × 1 = 1 combination
  L=3, R=1: FBT(3) × FBT(1) = 1 × 1 = 1 combination
  Total = 2 trees = C(2) ✓

FBT(3): L=1, R=1 → 1 tree = C(1) ✓
FBT(1): base case → 1 tree = C(0) ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(2^n) — Catalan number of trees, all must be built |
| Space | O(2^n) — memo stores all trees |

---

## The 66 Equivalences

Richard Stanley's *Enumerative Combinatorics Vol. 2* lists **66 combinatorial
structures** all counted by the same Catalan numbers — all bijectively
equivalent to full binary trees. A few:

| Structure | Count |
|-----------|-------|
| Full binary trees with n+1 leaves | C(n) |
| Valid bracket sequences of length 2n | C(n) |
| Triangulations of convex (n+2)-gon | C(n) |
| Binary parse trees (NLP) | C(n) |
| Monotonic lattice paths (n×n grid) | C(n) |
| Stack-sortable permutations of length n | C(n) |
