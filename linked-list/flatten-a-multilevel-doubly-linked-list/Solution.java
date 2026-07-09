class Node {
    public int val;
    public Node prev;
    public Node next;
    public Node child;
}

class Solution {

    // Flatten a Multilevel Doubly Linked List — LeetCode #430
    //
    // The output is a preorder DFS: when a node has a child, the whole child list is
    // spliced between the node and its next. The structure is a general tree in the
    // left-child (child) / right-sibling (next) representation, and flattening is its
    // DFS linearisation.
    //
    // Splice-as-you-go, O(1) space: for each node with a child, connect node -> child,
    // find the child list's tail, connect tail -> saved next, and null the child. Keep
    // walking; advancing into the spliced child list discovers nested children too.
    //
    // Time: O(n).  Space: O(1).
    public Node flatten(Node head) {
        Node curr = head;
        while (curr != null) {
            if (curr.child != null) {
                Node next  = curr.next;      // remember where to reconnect
                Node child = curr.child;

                // splice curr -> child
                curr.next  = child;
                child.prev = curr;
                curr.child = null;           // required: clear the child pointer

                // find the child list's tail
                Node tail = child;
                while (tail.next != null) tail = tail.next;

                // connect tail -> saved next
                tail.next = next;
                if (next != null) next.prev = tail;
            }
            curr = curr.next;                // walks into the child, then onward
        }
        return head;
    }
}

/*
 * Trace
 * -----
 *   1-2-3-4-5-6 , with 3.child = 7-8-9-10 , and 8.child = 11-12
 *
 *   at 3: next=4, wire 3->7, child tail=10, wire 10->4
 *         => 1-2-3-7-8-9-10-4-5-6
 *   curr advances into 7, then 8:
 *   at 8: next=9, wire 8->11, child tail=12, wire 12->9
 *         => 1-2-3-7-8-11-12-9-10-4-5-6   (final)
 *
 * Why walking handles arbitrary nesting
 * -------------------------------------
 * After splicing a child list in, curr's next is the child list's head, so the walk
 * descends into it. Any node in that sublist with its own child is spliced the same
 * way when curr reaches it. One linear pass therefore flattens all levels.
 */
