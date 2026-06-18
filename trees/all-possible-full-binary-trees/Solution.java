import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class Solution {

    // Memoisation map: n → list of all structurally distinct FBTs with n nodes
    private final Map<Integer, List<TreeNode>> memo = new HashMap<>();

    // Approach: Recursive Enumeration with Memoisation — O(2^n) time and space
    //
    // Key observations:
    //   1. Full binary trees always have an ODD number of nodes.
    //      (root=1, each internal node adds exactly 2 children → 1 + 2k nodes)
    //      → Return empty list immediately for even n.
    //
    //   2. Root takes 1 node; remaining n-1 nodes split between left (L) and
    //      right (R=n-1-L) subtrees. Both L and R must be odd.
    //      L ranges over odd values from 1 to n-2.
    //
    //   3. The number of FBTs with n nodes = C((n-1)/2), the ((n-1)/2)-th
    //      Catalan number. Grows exponentially → must enumerate all of them.
    //
    //   4. Memoisation: allPossibleFBT(5) is needed for both L=5,R=1 and
    //      L=1,R=5 when computing allPossibleFBT(7). Without memo, same
    //      subtrees are reconstructed multiple times.
    public List<TreeNode> allPossibleFBT(int n) {
        // Even n → no full binary tree possible
        if (n % 2 == 0) return new ArrayList<>();

        // Base case: single root node
        if (n == 1) return Collections.singletonList(new TreeNode(0));

        // Return cached result if already computed
        if (memo.containsKey(n)) return memo.get(n);

        List<TreeNode> result = new ArrayList<>();

        // Left subtree has L nodes (odd), right has R = n-1-L nodes (odd)
        // L ranges from 1 to n-2, stepping by 2 (odd values only)
        for (int L = 1; L < n - 1; L += 2) {
            int R = n - 1 - L;
            for (TreeNode left : allPossibleFBT(L)) {
                for (TreeNode right : allPossibleFBT(R)) {
                    TreeNode root = new TreeNode(0);
                    root.left  = left;
                    root.right = right;
                    result.add(root);
                }
            }
        }

        memo.put(n, result);
        return result;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(2^n) — number of FBTs grows as Catalan numbers C((n-1)/2)
 * Space: O(2^n) — storing all trees in memo + result lists
 *
 * Memoisation is necessary but cannot reduce the inherent exponential output.
 * It eliminates recomputation of subtrees but the total work is still bounded
 * by the number of distinct trees, which grows as 4^k / (k^(3/2) * sqrt(π)).
 *
 * Catalan number connection:
 *   n=1:  C(0) = 1  tree
 *   n=3:  C(1) = 1  tree
 *   n=5:  C(2) = 2  trees
 *   n=7:  C(3) = 5  trees
 *   n=9:  C(4) = 14 trees
 *   n=11: C(5) = 42 trees
 *
 *   C(k) = (1/(k+1)) * C(2k,k)   where k = (n-1)/2
 *
 * Trace — n=7:
 *   L=1,R=5: FBT(1) × FBT(5) = 1 × 2 = 2 trees
 *   L=3,R=3: FBT(3) × FBT(3) = 1 × 1 = 1 tree
 *   L=5,R=1: FBT(5) × FBT(1) = 2 × 1 = 2 trees
 *   Total: 5 trees = C(3) ✓
 *
 * Real-world connection:
 *   This algorithm is structurally identical to CYK parsing in NLP —
 *   enumerating all binary parse trees for an ambiguous grammar. The
 *   Catalan numbers count both full binary trees AND valid parse trees
 *   under binary branching constraints.
 */
