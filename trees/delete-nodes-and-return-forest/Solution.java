import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class Solution {

    // Approach: Postorder DFS — O(n) time, O(n) space
    //
    // Key insight: process children BEFORE parents (postorder).
    // By the time we process a node, we already know if its children survived.
    //
    // When a node is deleted:
    //   - Return null to parent (disconnects from parent)
    //   - Its non-null children become new roots (add to forest)
    //
    // When a node is NOT deleted:
    //   - Return it to parent (stays connected)
    //   - If it was a root (isRoot=true), add to forest
    //
    // isRoot propagation:
    //   - Root of whole tree: isRoot=true (initial call)
    //   - Children of a deleted node: isRoot=true (parent being removed)
    //   - Children of a surviving node: isRoot=false (still have a parent)
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        Set<Integer> deleteSet = new HashSet<>();
        for (int val : to_delete) deleteSet.add(val);

        List<TreeNode> forest = new ArrayList<>();
        dfs(root, deleteSet, forest, true);
        return forest;
    }

    private TreeNode dfs(TreeNode node, Set<Integer> deleteSet,
                         List<TreeNode> forest, boolean isRoot) {
        if (node == null) return null;

        boolean deleted = deleteSet.contains(node.val);

        // Add to forest if: this node is a root AND it survives deletion
        if (isRoot && !deleted) forest.add(node);

        // Postorder: process children first
        // Children become roots if current node is being deleted
        node.left  = dfs(node.left,  deleteSet, forest, deleted);
        node.right = dfs(node.right, deleteSet, forest, deleted);

        // Return null if deleted (severs connection to parent)
        // Return node if surviving (stays connected to parent)
        return deleted ? null : node;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — visit every node exactly once
 * Space: O(n) — deleteSet + recursion stack + result list
 *
 * Why postorder (not preorder)?
 *   Postorder processes children before parents.
 *   When we set node.left = dfs(node.left, ...), the returned value is:
 *     - The child node (if it survived)
 *     - null (if it was deleted)
 *   This automatically cleans up the parent's pointer without extra work.
 *   Preorder would require a second pass to clean up child pointers.
 *
 * The isRoot flag:
 *   Tracks whether a node is currently the root of a tree in the forest.
 *   Initial call: isRoot=true (the input root is always a candidate)
 *   When recursing into children:
 *     isRoot = deleted  (children become roots only if parent is deleted)
 *
 * Trace — tree: 1→{2,3}, 2→{4,5}, 3→{6,7}; delete=[3,5]
 * ----------------------------------------------------------
 * dfs(1, isRoot=true):
 *   deleted=false → add 1 to forest
 *   left  = dfs(2, isRoot=false):
 *     deleted=false
 *     left  = dfs(4, isRoot=false): deleted=false → return 4
 *     right = dfs(5, isRoot=false): deleted=true
 *       left  = dfs(null,...) = null
 *       right = dfs(null,...) = null
 *       return null  (5 deleted, no children to promote)
 *     2.right = null
 *     return 2
 *   right = dfs(3, isRoot=false):
 *     deleted=true  (3 is deleted, children become roots)
 *     left  = dfs(6, isRoot=true):  deleted=false → add 6 to forest → return 6
 *     right = dfs(7, isRoot=true):  deleted=false → add 7 to forest → return 7
 *     return null  (3 deleted)
 *   1.right = null
 *   return 1
 *
 * forest = [1, 6, 7]
 * Tree 1: 1 → 2 → 4   Tree 2: 6   Tree 3: 7  ✓
 */
