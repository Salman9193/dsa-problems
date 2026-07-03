class Solution {

    // Validate BST: every node must satisfy strict bounds from its ancestors.
    //
    // Common mistake: only checking node vs immediate children.
    // A BST requires ALL nodes in left subtree < root < ALL nodes in right subtree.
    //
    // Correct approach: pass down valid range [min, max] for each subtree.

    public boolean isValidBST(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;

        return validate(node.left,  min,      node.val)  // left must be < node.val
            && validate(node.right, node.val, max);       // right must be > node.val
    }
}

/*
 * Complexity: Time O(n), Space O(h)
 *
 * Why Long (not int) for min/max?
 *   Node values can be Integer.MIN_VALUE or Integer.MAX_VALUE.
 *   Using int would cause incorrect comparison at boundary values.
 *   Using Long avoids this edge case cleanly.
 *
 * Alternative: inorder traversal produces sorted sequence for valid BST.
 *   Track previous node; if current <= previous → invalid.
 *   Time O(n), Space O(h) — same complexity but different approach.
 *
 * The range-passing pattern generalises to many BST problems:
 *   - BST insertion: follow range to find correct position
 *   - BST range query: prune subtrees outside query range
 *   - BST floor/ceiling: track bounds during traversal
 */
