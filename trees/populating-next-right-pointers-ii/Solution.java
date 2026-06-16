import java.util.LinkedList;
import java.util.Queue;

class Node {
    int val;
    Node left, right, next;
    Node(int val) { this.val = val; }
}

class Solution {

    // Approach 1: BFS Level-Order — O(n) time, O(n) space
    //
    // Standard level-order traversal. Process each level from a queue,
    // link consecutive nodes with next pointers.
    public Node connectBFS(Node root) {
        if (root == null) return null;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            Node prev = null;
            for (int i = 0; i < size; i++) {
                Node curr = queue.poll();
                if (prev != null) prev.next = curr;
                prev = curr;
                if (curr.left  != null) queue.offer(curr.left);
                if (curr.right != null) queue.offer(curr.right);
            }
        }
        return root;
    }

    // Approach 2: O(1) Space — Using Already-Established next Pointers  ← OPTIMAL
    //
    // Once level L's next pointers are set, traverse level L using those pointers
    // to build next pointers for level L+1 — no queue needed.
    //
    // Dummy node trick: sentinel head for the next level's linked list.
    //   - Avoids null-check when appending the first child of a new level.
    //   - dummy.next always points to the first node of the next level.
    //
    // This works because:
    //   1. By the time we process level L, all its next pointers are already set.
    //   2. We build level L+1's next pointers as we traverse level L.
    //   3. We then drop down using dummy.next — the head of the new level.
    public Node connect(Node root) {
        Node curr = root;

        while (curr != null) {
            Node dummy = new Node(0);  // sentinel head for next level
            Node tail = dummy;         // tail of next level's linked list

            // traverse current level using established next pointers
            while (curr != null) {
                if (curr.left != null) {
                    tail.next = curr.left;
                    tail = tail.next;
                }
                if (curr.right != null) {
                    tail.next = curr.right;
                    tail = tail.next;
                }
                curr = curr.next;      // advance to next node on current level
            }

            curr = dummy.next;         // drop to next level
        }

        return root;
    }
}

/*
 * Complexity
 * ----------
 * BFS:        Time O(n), Space O(n)  — queue holds up to n/2 nodes (widest level)
 * O(1) space: Time O(n), Space O(1)  — only dummy node + two pointers per level
 *
 * Why the dummy node?
 *   Without it, we'd need: if (tail == null) { head = child; tail = child; }
 *   With dummy:            tail.next = child; tail = tail.next;  (always safe)
 *   Same trick used in merge sort linked list and LRU cache implementation.
 *
 * Connection to B+ tree:
 *   The next pointers established here ARE the leaf-level linked list of a B+ tree.
 *   MySQL InnoDB, PostgreSQL, and SQL Server use this exact structure for O(1)-per-step
 *   range scans without re-traversing the tree.
 *
 * Trace — tree: 1 → {2,3}, 2 → {4,5}, 3 → {null,7}
 * ---------------------------------------------------
 * Level 0: curr=1
 *   1.left=2:  tail→2,  nextLevel=[2]
 *   1.right=3: tail→3,  nextLevel=[2→3]
 *   curr=1.next=null → done, curr=dummy.next=2
 *
 * Level 1: curr=2
 *   2.left=4:  tail→4,  nextLevel=[4]
 *   2.right=5: tail→5,  nextLevel=[4→5]
 *   curr=2.next=3
 *   3.left=null (skip)
 *   3.right=7: tail→7,  nextLevel=[4→5→7]
 *   curr=3.next=null → done, curr=dummy.next=4
 *
 * Level 2: all leaves, no children added
 *   curr=4→5→7→null → done, curr=dummy.next=null → outer loop ends ✓
 *
 * Result: 1→null | 2→3→null | 4→5→7→null ✓
 */
