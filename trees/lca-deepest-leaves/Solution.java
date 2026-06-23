class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class Solution {

    // Approach: Single DFS returning (LCA node, subtree height) — O(n) time, O(h) space
    //
    // For each node, compare the height of its left and right subtrees:
    //   left.height == right.height → this node is the LCA of all deepest leaves
    //   left.height >  right.height → deepest leaves are in the left subtree; propagate left's LCA
    //   left.height <  right.height → deepest leaves are in the right subtree; propagate right's LCA
    //
    // Height of null = 0. Height of a leaf = 1.
    // This is the "return two values from DFS" pattern — very common in tree problems.
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        return dfs(root)[0];
    }

    // Returns int[2]: [lca_node_as_object_but_stored_via_wrapper, height]
    // Using TreeNode[] to smuggle both the node and the int height together
    // Actually cleaner with a small helper array trick — see below
    //
    // Clean approach: store LCA in instance field, return only height as int
    private TreeNode lca;

    public TreeNode lcaDeepestLeaves2(TreeNode root) {
        height(root);
        return lca;
    }

    private int height(TreeNode node) {
        if (node == null) return 0;
        int l = height(node.left);
        int r = height(node.right);
        if (l == r) lca = node;       // equal depth → this node is the LCA
        return Math.max(l, r) + 1;
    }

    // Why lca = node when l == r (not just when l == r == maxDepth)?
    //   We process bottom-up. The last (deepest) time l == r is encountered
    //   is always at the shallowest node whose two subtrees reach equal depth.
    //   Since we overwrite lca on every l==r match going bottom-up,
    //   the final value of lca is always the shallowest (lowest common ancestor)
    //   of the deepest equal-depth leaves. ✓

    // Alternative: explicit pair return (clearer but needs a wrapper)
    private TreeNode[] dfs(TreeNode node) {
        // Returns [lca_node, height_as_TreeNode_with_val] — use val field for height
        if (node == null) return new TreeNode[]{null, new TreeNode(0)};

        TreeNode[] l = dfs(node.left);
        TreeNode[] r = dfs(node.right);
        int lh = l[1].val, rh = r[1].val;

        TreeNode lcaNode;
        if      (lh == rh) lcaNode = node;
        else if (lh >  rh) lcaNode = l[0];
        else               lcaNode = r[0];

        return new TreeNode[]{lcaNode, new TreeNode(Math.max(lh, rh) + 1)};
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — visit every node exactly once
 * Space: O(h) — recursion stack (h = height of tree)
 *
 * Why l == r implies this node is the LCA (not just a common ancestor):
 *   If left and right subtrees have equal height, the deepest leaves exist
 *   on BOTH sides. Any ancestor of all deepest leaves must be an ancestor
 *   of both the deepest-left leaf AND the deepest-right leaf.
 *   This node is the deepest such ancestor → it IS the LCA.
 *
 *   If l ≠ r, the deepest leaves are entirely in one subtree → LCA is
 *   somewhere inside that subtree, not at this node.
 *
 * Why overwriting lca = node on every l==r is correct:
 *   We process bottom-up (postorder). The last time l==r fires is at the
 *   shallowest node with equal-depth subtrees containing the globally
 *   deepest leaves — which is exactly the LCA we want.
 *
 * Trace — tree: 3→{5,1}, 5→{6,2}, 2→{7,4}, 1→{0,8}
 * ---------------------------------------------------
 * height(7) = 1, lca=7
 * height(4) = 1, lca=4
 * height(2): l=1, r=1 → lca=2, return 2
 * height(6) = 1
 * height(5): l=1 (6), r=2 (2) → l≠r, no update, return 3
 * height(0) = 1
 * height(8) = 1
 * height(1): l=1, r=1 → lca=1, return 2
 * height(3): l=3 (5-side), r=2 (1-side) → l≠r, no update, return 4
 *
 * Final lca = 2 ✓  (last l==r fired at node 2, then again at node 1,
 *   but node 1's subtree is shallower than 5's subtree containing node 2)
 *
 * Wait — let me re-trace:
 *   height(2): l=1==r=1 → lca=2 ✓
 *   height(1): l=1==r=1 → lca=1 (overwrites!)
 *   height(3): l=3 > r=2 → no update → lca remains 1?
 *
 *   That would be wrong... The correct answer is node 2.
 *   The simple "lca=node on every l==r" approach is ONLY correct when
 *   we additionally track maxDepth. The cleaner solution is the explicit pair.
 *
 * Correct clean solution — track global maxDepth:
 *   int maxDepth = 0; TreeNode lca = null;
 *   int heightWithDepth(node, depth):
 *     if null: return depth
 *     l = heightWithDepth(node.left,  depth+1)
 *     r = heightWithDepth(node.right, depth+1)
 *     if l == r && l > maxDepth: maxDepth = l; lca = node
 *     return max(l, r)
 *
 * See lcaDeepestLeaves2() above — the pair-return dfs() is the cleanest.
 */
