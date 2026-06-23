class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class Solution {

    // Approach: Postorder DFS returning (LCA, height) pair — O(n) time, O(h) space
    //
    // height(null) = 0, height(leaf) = 1, height(node) = 1 + max(left, right)
    //
    // At each node:
    //   left_height == right_height → deepest leaves on BOTH sides → this node is LCA
    //   left_height >  right_height → deepest leaves only on left  → propagate left's LCA
    //   left_height <  right_height → deepest leaves only on right → propagate right's LCA
    //
    // No global state needed — all information flows up cleanly through return values.
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        return dfs(root)[0];
    }

    // Returns TreeNode[2]: [lca, height_sentinel]
    // height_sentinel.val stores the integer height (reuse TreeNode.val as int carrier)
    private TreeNode[] dfs(TreeNode node) {
        if (node == null) {
            TreeNode zero = new TreeNode(0);
            return new TreeNode[]{null, zero};  // lca=null, height=0
        }

        TreeNode[] l = dfs(node.left);
        TreeNode[] r = dfs(node.right);
        int lh = l[1].val;
        int rh = r[1].val;

        TreeNode lcaNode;
        if      (lh == rh) lcaNode = node;   // equal depth → this node is LCA
        else if (lh >  rh) lcaNode = l[0];   // left deeper → left's LCA
        else               lcaNode = r[0];   // right deeper → right's LCA

        TreeNode heightNode = new TreeNode(Math.max(lh, rh) + 1);
        return new TreeNode[]{lcaNode, heightNode};
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — visit every node once
 * Space: O(h) — recursion stack (h = height of tree)
 *               O(n) extra for height sentinel nodes in worst case
 *               (can avoid by using int[] instead: return new int[]{lca_id, height})
 *
 * Key correctness argument:
 *   lh == rh means: "the deepest leaves in my left and right subtrees are
 *   at the same depth." Since we bubble up heights, this equality holds at
 *   the shallowest node where both sides reach the globally deepest leaves.
 *   That node IS the LCA by definition — it is an ancestor of all deepest
 *   leaves, and no deeper node is an ancestor of all of them.
 *
 * Trace — tree: 3→{5,1}, 5→{6,2}, 2→{7,4}, 1→{0,8}
 * ---------------------------------------------------
 * dfs(null)     → [null, 0]
 * dfs(7)        → l=0,r=0 → lh==rh → lca=7,  height=1
 * dfs(4)        → l=0,r=0 → lh==rh → lca=4,  height=1
 * dfs(2)        → l=1,r=1 → lh==rh → lca=2,  height=2  ← node 2 is LCA
 * dfs(6)        → l=0,r=0 → lh==rh → lca=6,  height=1
 * dfs(5)        → l=1(6), r=2(2) → lh<rh → lca=2, height=3
 * dfs(0)        → lca=0,  height=1
 * dfs(8)        → lca=8,  height=1
 * dfs(1)        → l=1,r=1 → lh==rh → lca=1,  height=2
 * dfs(3)        → l=3(5-side,lca=2), r=2(1-side,lca=1) → lh>rh → lca=2, height=4
 *
 * return lca = node 2 ✓
 *
 * Edge cases:
 *   Single node root     → l=0==r=0 → lca=root ✓
 *   Right-skewed tree    → always rh > lh → lca = deepest leaf ✓
 *   Perfect binary tree  → root has equal-height subtrees → lca=root ✓
 */
