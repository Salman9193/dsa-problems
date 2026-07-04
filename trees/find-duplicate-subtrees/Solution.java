import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

class Solution {

    // Approach: Postorder serialization + HashMap counting — O(n^2) worst / O(n) with IDs
    //
    // The problem: return one representative root of every subtree shape that
    // occurs more than once (identical structure AND identical values).
    //
    // Core idea: give every subtree a canonical "signature" string built in
    // POSTORDER (left, right, root). Two subtrees are duplicates exactly when
    // their signatures are equal. Count signatures in a HashMap; the first time
    // a signature's count hits 2, record that subtree's root.
    //
    // Why postorder: a node's signature depends on its children's signatures,
    // so children must be serialized BEFORE the node — that is postorder.
    // Why include nulls: without a null marker, different shapes can collide
    // (e.g. a left-only child vs a right-only child would look the same).

    public List<TreeNode> findDuplicateSubtrees(TreeNode root) {
        Map<String, Integer> count = new HashMap<>();
        List<TreeNode> result = new ArrayList<>();
        serialize(root, count, result);
        return result;
    }

    private String serialize(TreeNode node, Map<String, Integer> count, List<TreeNode> result) {
        if (node == null) return "#";                 // null marker keeps shapes distinct

        // Postorder: build children signatures first, then combine with this node.
        String left  = serialize(node.left,  count, result);
        String right = serialize(node.right, count, result);
        String sig = node.val + "," + left + "," + right;

        int seen = count.getOrDefault(sig, 0) + 1;
        count.put(sig, seen);

        // Record the root the FIRST time this signature becomes a duplicate.
        // Guarding on == 2 ensures each duplicated shape is added exactly once.
        if (seen == 2) result.add(node);

        return sig;
    }

    // ---- Optimized: subtree IDs instead of strings — O(n) time & space ----
    //
    // Serialization strings can be O(n) long, so building them at every node is
    // O(n^2) total. Replace each unique subtree signature with a small integer
    // ID. A node's signature becomes (val, leftId, rightId) — an O(1) key —
    // so the whole pass is O(n).
    private int idCounter = 1;

    public List<TreeNode> findDuplicateSubtreesFast(TreeNode root) {
        Map<String, Integer> sigToId = new HashMap<>();   // "val,leftId,rightId" -> id
        Map<Integer, Integer> idCount = new HashMap<>();  // id -> occurrence count
        List<TreeNode> result = new ArrayList<>();
        encode(root, sigToId, idCount, result);
        return result;
    }

    private int encode(TreeNode node,
                       Map<String, Integer> sigToId,
                       Map<Integer, Integer> idCount,
                       List<TreeNode> result) {
        if (node == null) return 0;                       // id 0 == null subtree

        int leftId  = encode(node.left,  sigToId, idCount, result);
        int rightId = encode(node.right, sigToId, idCount, result);

        String sig = node.val + "," + leftId + "," + rightId;   // O(1)-size key
        int id = sigToId.computeIfAbsent(sig, k -> idCounter++);

        int seen = idCount.getOrDefault(id, 0) + 1;
        idCount.put(id, seen);
        if (seen == 2) result.add(node);

        return id;
    }
}

/*
 * Complexity
 * ----------
 * String signatures:  O(n^2) time (each sig up to O(n) long), O(n^2) space
 * Subtree IDs:        O(n) time, O(n) space  (each key is O(1) size)
 *   n = number of nodes
 *
 * Why postorder serialization identifies duplicates
 * -------------------------------------------------
 * Two subtrees are "the same" iff they have identical structure and values.
 * A postorder signature that includes null markers is a UNIQUE fingerprint of
 * a subtree's shape+values: equal fingerprints <=> identical subtrees. So the
 * problem reduces to "find fingerprints that occur >= 2 times".
 *
 * Why guard on seen == 2 (not >= 2)
 * ---------------------------------
 * We want ONE representative root per duplicated shape. Adding when the count
 * first reaches 2 records it exactly once; a third+ occurrence (seen 3,4,...)
 * does not re-add it.
 *
 * Trace — tree:        1
 *                     / \
 *                    2   3
 *                   /   / \
 *                  4   2   4
 *                     /
 *                    4
 *
 * postorder signatures (val,left,right):
 *   leaf 4 (under 2 on left):   "4,#,#"   count=1
 *   node 2 (left):              "2,4,#,#" count=1        sig uses child "4,#,#"
 *   leaf 4 (under inner 2):     "4,#,#"   count=2  -> add this 4  ← duplicate leaf
 *   inner 2 (under 3):          "2,4,#,#" count=2  -> add this 2  ← duplicate subtree
 *   leaf 4 (right of 3):        "4,#,#"   count=3  (already recorded, skip)
 *   node 3, node 1: unique
 * result = [ subtree "4", subtree "2->4" ]   (one root each)
 *
 * The ID version produces the same result but keys are (val,leftId,rightId),
 * each O(1), so it scales to large trees without the O(n^2) string blowup.
 */
