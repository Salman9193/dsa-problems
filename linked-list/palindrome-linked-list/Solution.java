class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

class Solution {

    // Palindrome Linked List — LeetCode #234
    //
    // Approach 1: values into a list, two-pointer check.
    // O(n) time, O(n) space — simple baseline, but ignores the follow-up.
    public boolean isPalindromeArray(ListNode head) {
        java.util.List<Integer> vals = new java.util.ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) vals.add(c.val);
        int i = 0, j = vals.size() - 1;
        while (i < j) {
            if (!vals.get(i).equals(vals.get(j))) return false;
            i++;
            j--;
        }
        return true;
    }

    // Approach 2: find the middle, reverse the second half, compare.
    // O(n) time, O(1) space — the follow-up answer.
    //
    // 1. Fast/slow pointers: when fast reaches the end, slow is at the midpoint
    //    (start of the second half).
    // 2. Reverse the list from slow onward.
    // 3. Walk the first half (head) and reversed second half (prev) inward; the second
    //    half is <= the first, so loop while the back pointer is non-null.
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        // 1. Find the middle.
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2. Reverse the second half, starting at slow.
        ListNode prev = null, curr = slow;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // 3. Compare first half (head) with reversed second half (prev).
        ListNode left = head, right = prev;   // right is the shorter side
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }
}

/*
 * Trace — 1 -> 2 -> 3 -> 2 -> 1  (odd length)
 * -------------------------------------------
 * Find middle: slow stops at node '3' (fast walks 2x and falls off the end).
 * Reverse from '3':  3 -> 2 -> 1  becomes  1 -> 2 -> 3  (prev heads it).
 * Compare:  head 1 vs back 1  ok
 *           head 2 vs back 2  ok
 *           head 3 vs back 3  ok   (shared middle node, harmless)
 *           back reaches null -> return true.
 *
 * Trace — 1 -> 2  (even length)
 * -----------------------------
 * Middle: slow stops at '2'. Reverse -> [2]. Compare 1 vs 2 -> mismatch -> false.
 *
 * Mutation note
 * -------------
 * This reverses the second half in place, so the list is modified. If the caller needs
 * the original order preserved, reverse the second half again after comparing — still
 * O(1) extra space.
 */
