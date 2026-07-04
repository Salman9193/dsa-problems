import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public class Codec {

    // Approach: Preorder DFS with explicit null markers — O(n) time, O(n) space
    //
    // The insight that makes this work: a preorder traversal (root, left, right)
    // that ALSO records nulls is enough to reconstruct the tree uniquely.
    // Without null markers, preorder alone is ambiguous (you'd need inorder too).
    // The "#" sentinel tells deserialize exactly where each subtree ends.

    private static final String NULL = "#";
    private static final String SEP  = ",";

    // ---- SERIALIZE ----
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }

    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NULL).append(SEP);        // record the null and stop
            return;
        }
        sb.append(node.val).append(SEP);        // root
        serializeHelper(node.left, sb);         // left subtree
        serializeHelper(node.right, sb);        // right subtree
    }

    // ---- DESERIALIZE ----
    public TreeNode deserialize(String data) {
        // A queue lets us consume tokens left-to-right in the SAME preorder
        // that serialize produced them — the structure is implicit in the order.
        Queue<String> tokens = new LinkedList<>(Arrays.asList(data.split(SEP)));
        return deserializeHelper(tokens);
    }

    private TreeNode deserializeHelper(Queue<String> tokens) {
        String token = tokens.poll();
        if (token.equals(NULL)) return null;    // this position was a null leaf edge

        TreeNode node = new TreeNode(Integer.parseInt(token));
        node.left  = deserializeHelper(tokens);  // build left FIRST (preorder)
        node.right = deserializeHelper(tokens);  // then right
        return node;
    }
}

/*
 * Complexity
 * ----------
 * serialize:   O(n) time, O(n) space (output string + O(h) call stack)
 * deserialize: O(n) time, O(n) space (token queue + O(h) call stack)
 *   where n = number of nodes, h = tree height
 *
 * Why preorder + null markers is sufficient
 * -----------------------------------------
 * Normally you need TWO traversals (e.g. preorder + inorder) to rebuild a tree,
 * because a single traversal is ambiguous. But that ambiguity comes entirely
 * from not knowing where subtrees end. By emitting "#" for every null child,
 * we remove the ambiguity: the first token is always the root, and each recursive
 * call consumes exactly one complete subtree before returning.
 *
 * Trace — tree:      1
 *                   / \
 *                  2   3
 *                     / \
 *                    4   5
 *
 * serialize  -> "1,2,#,#,3,4,#,#,5,#,#,"
 *
 * deserialize consumes tokens in order:
 *   "1" -> node(1)
 *     left:  "2" -> node(2), left "#"->null, right "#"->null   => leaf 2
 *     right: "3" -> node(3)
 *              left:  "4" -> node(4), "#"->null, "#"->null      => leaf 4
 *              right: "5" -> node(5), "#"->null, "#"->null      => leaf 5
 *   => original tree reconstructed exactly
 *
 * Alternative: BFS level-order (the LeetCode "official" format "1,2,3,null,null,4,5")
 * is equally valid — use a queue of parents and attach children as tokens arrive.
 * Preorder DFS is shorter to write under interview pressure and has the same bounds.
 */
