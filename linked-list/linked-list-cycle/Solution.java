import java.util.HashSet;
import java.util.Set;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

class Solution {

    // Approach 1: HashSet — O(n) time, O(n) space
    // Store every visited node. If we see one twice, there's a cycle.
    public boolean hasCycleHashSet(ListNode head) {
        Set<ListNode> seen = new HashSet<>();
        ListNode curr = head;
        while (curr != null) {
            if (seen.contains(curr)) return true;
            seen.add(curr);
            curr = curr.next;
        }
        return false;
    }

    // Approach 2: Floyd's Cycle Detection (Tortoise & Hare) — O(n) time, O(1) space
    //
    // Two pointers: slow moves 1 step, fast moves 2 steps.
    // If a cycle exists, fast will lap slow and they meet inside the cycle.
    // If no cycle, fast reaches null.
    //
    // Why they always meet (never skip):
    //   Once both are in the cycle, fast gains exactly 1 node on slow per step.
    //   The gap closes by 1 each iteration → reaches 0 in at most cycle_length steps.
    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;        // move 1 step
            fast = fast.next.next;   // move 2 steps

            if (slow == fast) return true;  // met inside cycle
        }

        return false;  // fast reached null → no cycle
    }
}

/*
 * Complexity
 * ----------
 * Floyd's:  Time O(n), Space O(1)
 * HashSet:  Time O(n), Space O(n)
 *
 * Why both null checks?
 *   fast != null      → fast itself might land on null (even-length list)
 *   fast.next != null → we call fast.next.next, so fast.next must exist first
 *
 * Extensions of Floyd's Algorithm
 * --------------------------------
 * 1. Find cycle START node (LeetCode #142):
 *    After slow==fast, reset one pointer to head.
 *    Advance both by 1 step — they meet exactly at the cycle start.
 *    (Mathematical proof: distance from head to cycle start
 *     equals distance from meeting point to cycle start.)
 *
 * 2. Find cycle LENGTH:
 *    After slow==fast, keep one fixed and advance the other
 *    until they meet again — count the steps.
 *
 * 3. Find MIDDLE of linked list:
 *    When fast reaches the end, slow is at the middle.
 *
 * Trace — 3 → 2 → 0 → -4 → (back to 2)
 * ----------------------------------------
 * Step 0: slow=3,  fast=3
 * Step 1: slow=2,  fast=0
 * Step 2: slow=0,  fast=2   (fast wraps through cycle)
 * Step 3: slow=-4, fast=-4  ← slow == fast → cycle detected ✓
 */
