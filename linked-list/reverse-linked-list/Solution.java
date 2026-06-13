class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

class Solution {

    // Approach 1: Iterative — O(n) time, O(1) space  ← PREFERRED
    //
    // Three pointers: prev, curr, next.
    // At each step:
    //   1. Save curr.next before overwriting it
    //   2. Redirect curr.next to prev (reverse the pointer)
    //   3. Advance prev and curr forward
    //
    // The ORDER of operations is critical:
    //   Save next FIRST, then redirect, then advance.
    //   Changing this order corrupts the list.
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next;  // 1. save next
            curr.next = prev;           // 2. reverse pointer
            prev = curr;                // 3. advance prev
            curr = next;                // 4. advance curr
        }

        return prev;  // prev is the new head
    }

    // Approach 2: Recursive — O(n) time, O(n) space (call stack)
    //
    // Recurse to the last node, then wire reverse pointers on the way back up.
    //
    // Key lines:
    //   head.next.next = head  → make the NEXT node point BACK to current
    //   head.next = null       → break the FORWARD link (prevent cycle)
    //
    // Not preferred in production — O(n) stack space risks overflow for large n.
    public ListNode reverseListRecursive(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = reverseListRecursive(head.next);
        head.next.next = head;   // wire the reverse pointer
        head.next = null;        // break the forward link

        return newHead;          // newHead is always the original tail
    }
}

/*
 * Complexity
 * ----------
 * Iterative:  Time O(n), Space O(1)
 * Recursive:  Time O(n), Space O(n) — call stack depth = n
 *
 * Iterative Trace — 1 → 2 → 3 → null
 * -------------------------------------
 * Initial: prev=null, curr=1
 *
 * Step 1: next=2, 1.next=null, prev=1, curr=2
 *   null ← 1    2 → 3 → null
 *
 * Step 2: next=3, 2.next=1, prev=2, curr=3
 *   null ← 1 ← 2    3 → null
 *
 * Step 3: next=null, 3.next=2, prev=3, curr=null
 *   null ← 1 ← 2 ← 3
 *
 * curr==null → return prev=3 ✓
 *
 * Recursive Trace — 1 → 2 → 3 → null
 * --------------------------------------
 * reverseList(1)
 *   reverseList(2)
 *     reverseList(3)
 *       3.next==null → return 3 (base case)
 *     head=2: 2.next(3).next = 2  →  3→2
 *             2.next = null       →  3→2→null
 *     return 3
 *   head=1: 1.next(2).next = 1  →  2→1
 *           1.next = null        →  2→1→null
 *   return 3
 * Result: 3→2→1→null ✓
 *
 * Common variants that use this as a subroutine:
 *   #92  Reverse Linked List II     — reverse a sublist [left, right]
 *   #25  Reverse Nodes in k-Group   — reverse every k nodes
 *   #234 Palindrome Linked List     — reverse second half, compare
 *   #143 Reorder List               — reverse second half, interleave
 */
