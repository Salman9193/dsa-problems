import java.util.ArrayDeque;
import java.util.Deque;

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

class BSTIterator {

    // Approach 1: Flatten to List — O(n) space (simple but violates constraint)
    // Approach 2: Controlled Stack — O(h) space  ← CORRECT

    // Key insight: inorder traversal of BST yields sorted order.
    // Instead of materialising the full sorted list, simulate the call stack
    // with an explicit stack — push only the "left spine" at each step.
    //
    // Invariant: stack always contains the left spine from the current
    // position down to the leftmost unvisited node.
    //
    // This is exactly how database cursors traverse a B-tree index lazily —
    // maintaining the path from root to current position (O(h) space)
    // and advancing one row at a time.

    private final Deque<TreeNode> stack = new ArrayDeque<>();

    public BSTIterator(TreeNode root) {
        pushLeft(root);  // push entire left spine of the tree
    }

    // Push all left children from node to the leftmost descendant
    // This is the "go all the way left" step of inorder traversal
    private void pushLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    // next() — O(1) amortised
    //   Pop the top (next smallest), then push left spine of its right subtree.
    //   Each node is pushed once and popped once across all calls → O(n) total.
    public int next() {
        TreeNode node = stack.pop();
        pushLeft(node.right);   // right subtree's left spine becomes the new frontier
        return node.val;
    }

    // hasNext() — O(1)
    public boolean hasNext() {
        return !stack.isEmpty();
    }
}

/*
 * Complexity
 * ----------
 * Constructor: O(h) — pushes at most h nodes (the left spine)
 * next():      O(1) amortised — each node pushed once, popped once across n calls
 * hasNext():   O(1) — stack.isEmpty() is constant time
 * Space:       O(h) — stack holds at most h nodes at any time
 *
 * Why O(h) space?
 *   The stack holds the "left spine" from the current node down to the leftmost
 *   unvisited leaf. A left spine has at most h nodes (tree height).
 *   For a balanced BST: h = O(log n). For a skewed BST: h = O(n).
 *
 * Why O(1) amortised for next()?
 *   pushLeft() may push up to h nodes — not O(1) in the worst case for one call.
 *   But across all n next() calls: each of the n nodes is pushed exactly once
 *   and popped exactly once. Total work = O(2n) = O(n). Per call: O(1) amortised.
 *
 * Trace — BST: 7→{3,15}, 15→{9,20}
 * ------------------------------------
 * Init:     pushLeft(7) → stack=[7, 3]   (push 7, then 3; 3.left=null)
 *
 * next():   pop 3, pushLeft(3.right=null) → nothing
 *           stack=[7]  →  return 3
 *
 * next():   pop 7, pushLeft(7.right=15)
 *             push 15, push 9  (15.left=9, 9.left=null)
 *           stack=[15, 9]  →  return 7
 *
 * next():   pop 9, pushLeft(9.right=null) → nothing
 *           stack=[15]  →  return 9
 *
 * next():   pop 15, pushLeft(15.right=20)
 *             push 20  (20.left=null)
 *           stack=[20]  →  return 15
 *
 * next():   pop 20, pushLeft(20.right=null) → nothing
 *           stack=[]  →  return 20
 *
 * hasNext() → false ✓
 * Output: 3,7,9,15,20 (sorted) ✓
 *
 * Connection to Java TreeMap:
 *   TreeMap.KeyIterator uses successor(entry) — advancing to the inorder
 *   successor by going right then all the way left. This is equivalent to
 *   popping the stack and calling pushLeft(node.right) — the same algorithm.
 */
