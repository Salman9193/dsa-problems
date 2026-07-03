import java.util.*;

class Solution {

    // Lowest Common Ancestor (LCA) of a binary tree.
    //
    // The LCA of two nodes p and q is the deepest node that has both p and q
    // as descendants. A node is a descendant of itself.
    //
    // Key insight: a node is the LCA iff:
    //   Case 1: p and q are in DIFFERENT subtrees → this node is the LCA
    //   Case 2: this node IS p or q → it is the LCA (the other is in its subtree)
    //
    // Two approaches: recursive DFS (preferred) and iterative with parent map.

    // ── Approach 1: Recursive DFS — O(n) time, O(h) space ────────────────────
    //
    // Postorder DFS: search both subtrees, then decide at current node.
    //
    // Return value semantics (the key to understanding this algorithm):
    //   null:      neither p nor q found in this subtree
    //   non-null:  either (a) p or q found (bubble up for parent to combine), OR
    //              (b) this IS the LCA (both found in different subtrees)
    //
    // These two cases are indistinguishable by return value alone — but they
    // don't need to be! Once we find the LCA, it bubbles up unchanged to root.
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // Base case: null node or found p or q
        if (root == null) return null;
        if (root == p || root == q) return root; // found one — return immediately

        TreeNode left  = lowestCommonAncestor(root.left,  p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        // Case 1: p and q in DIFFERENT subtrees → current node is LCA
        if (left != null && right != null) return root;

        // Case 2: both in SAME subtree → bubble up the non-null result
        // (either the LCA found deeper, or a single found node)
        return left != null ? left : right;
    }

    // ── Approach 2: Iterative with Parent Map — O(n) time, O(n) space ────────
    //
    // Build a parent map for every node via DFS/BFS. Then walk ancestors of p
    // into a set, then walk ancestors of q until one is in p's ancestor set.
    //
    // When to prefer over recursive:
    //   - Very deep trees → call stack overflow risk (recursive is O(h) stack)
    //   - Multiple LCA queries on the same tree → build parent map once, O(h) per query
    //   - Language/environment where recursion is discouraged
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        // Build parent map via DFS until both p and q are found
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            if (node.left  != null) { parent.put(node.left,  node); stack.push(node.left); }
            if (node.right != null) { parent.put(node.right, node); stack.push(node.right); }
        }

        // Collect all ancestors of p (including p itself)
        Set<TreeNode> ancestors = new HashSet<>();
        while (p != null) {
            ancestors.add(p);
            p = parent.get(p);
        }

        // Walk q's ancestors until we find one in p's ancestor set
        while (!ancestors.contains(q))
            q = parent.get(q);

        return q; // first common ancestor found
    }
}

/*
 * Complexity
 * ----------
 * Recursive:  Time O(n) — visits every node once in worst case
 *             Space O(h) — call stack depth = tree height
 *             (O(n) worst case for skewed tree, O(log n) for balanced)
 *
 * Iterative:  Time O(n) — parent map build + ancestor walks
 *             Space O(n) — parent map + ancestor set
 *
 * Why early return at (root == p || root == q)?
 *   Once we find p (or q), we return it immediately WITHOUT searching deeper.
 *   This works because of the GUARANTEE in the problem: both p and q exist
 *   in the tree. So if root == p, the other node (q) must be somewhere in
 *   root's subtree — making root the LCA. We don't need to find q explicitly;
 *   the parent call will see root returned from one side and correctly
 *   identify root as the LCA.
 *
 * Why the two return cases are sufficient:
 *   left!=null && right!=null → found p in one subtree, q in other → LCA here
 *   left!=null only              → LCA is somewhere in left subtree (already found)
 *   right!=null only             → LCA is somewhere in right subtree (already found)
 *   both null                    → neither p nor q in this subtree
 *
 * LCA in BST (#235) vs LCA in binary tree (#236):
 *   BST: use BST property → O(h) time, no need to search both subtrees
 *     if p.val < root.val && q.val < root.val: go left
 *     if p.val > root.val && q.val > root.val: go right
 *     else: root is LCA
 *   Binary tree: must check both subtrees → O(n) time (can't exploit ordering)
 *
 * Trace — tree [3,5,1,6,2,0,8], LCA(5,4) where 4 is child of 2 (child of 5)
 * --------------------------------------------------------------------------
 * dfs(3): not p/q
 *   left = dfs(5): root==p=5 → return 5 immediately
 *   right = dfs(1): neither 4 nor p/q here → eventually returns null
 *   left=5≠null, right=null → return left=5 ✓
 *
 * Trace — LCA(5,1):
 * dfs(3): not p/q
 *   left = dfs(5):  root==p=5 → return 5
 *   right = dfs(1): root==q=1 → return 1
 *   left=5≠null, right=1≠null → return root=3 ✓
 */
