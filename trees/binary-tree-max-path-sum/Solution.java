class Solution {

    // Binary Tree Maximum Path Sum — important Hard.
    // A path is any sequence of nodes (no repeats) connected parent-child.
    // Path can start and end anywhere — doesn't have to go through root.
    //
    // Key insight: DFS postorder, track two things at each node:
    //   1. Max "gain" this node contributes to a path going UPWARD to parent
    //      = node.val + max(leftGain, rightGain, 0)
    //   2. Max path THROUGH this node (which stays here, doesn't go up)
    //      = node.val + leftGain + rightGain  (can use BOTH children)
    //
    // Global max is updated for case 2 at every node.

    int maxSum;

    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        dfs(root);
        return maxSum;
    }

    private int dfs(TreeNode node) {
        if (node == null) return 0;

        int leftGain  = Math.max(dfs(node.left),  0); // ignore negative subtrees
        int rightGain = Math.max(dfs(node.right), 0);

        // Max path THROUGH this node (local candidate for answer)
        maxSum = Math.max(maxSum, node.val + leftGain + rightGain);

        // Max gain this node contributes to a path going UP to parent
        // Can only pick ONE side (can't go up through both children)
        return node.val + Math.max(leftGain, rightGain);
    }
}

/*
 * Complexity: Time O(n), Space O(h) call stack
 *
 * The two-return-value pattern:
 *   - Global update: node.val + left + right  (uses both branches, stays here)
 *   - Return to parent: node.val + max(left, right)  (one branch, continues up)
 *
 * This pattern also solves:
 *   #543 Diameter of Binary Tree (replace + with max, track edge count)
 *   #687 Longest Univalue Path (track streak instead of sum)
 *   #1372 Longest Zigzag Path
 */
