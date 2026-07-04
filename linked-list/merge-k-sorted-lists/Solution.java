import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {

    // Approach 1: Min-Heap — O(N log k) time, O(k) space
    //
    // N = total number of nodes across all lists, k = number of lists.
    //
    // Key idea: at any moment, the next node in the merged output is the
    // smallest current head among the k lists. A min-heap of size k gives us
    // that smallest head in O(log k). We pop it, append it, then push its
    // successor. Each of the N nodes is pushed and popped once -> O(N log k).
    public ListNode mergeKLists(ListNode[] lists) {
        // Comparator orders nodes by value so the heap root is the global min.
        PriorityQueue<ListNode> heap =
            new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

        // Seed the heap with the head of every non-empty list.
        for (ListNode node : lists) {
            if (node != null) heap.offer(node);
        }

        ListNode dummy = new ListNode(0);   // dummy simplifies the empty-output case
        ListNode tail = dummy;

        while (!heap.isEmpty()) {
            ListNode smallest = heap.poll();     // current global minimum head
            tail.next = smallest;                // append to merged list
            tail = tail.next;

            if (smallest.next != null) {         // push the popped node's successor
                heap.offer(smallest.next);
            }
        }

        return dummy.next;
    }

    // Approach 2: Divide & Conquer (pairwise merge) — O(N log k) time, O(log k) space
    //
    // Merge lists in pairs: k -> k/2 -> k/4 -> ... -> 1. Each of the log k
    // "rounds" touches all N nodes once, giving O(N log k). Uses no heap;
    // space is the O(log k) recursion depth.
    public ListNode mergeKListsDC(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return merge(lists, 0, lists.length - 1);
    }

    private ListNode merge(ListNode[] lists, int lo, int hi) {
        if (lo == hi) return lists[lo];
        int mid = lo + (hi - lo) / 2;
        ListNode left  = merge(lists, lo, mid);
        ListNode right = merge(lists, mid + 1, hi);
        return mergeTwo(left, right);
    }

    // Standard merge of two sorted lists — O(n+m).
    private ListNode mergeTwo(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (a != null && b != null) {
            if (a.val <= b.val) { tail.next = a; a = a.next; }
            else                { tail.next = b; b = b.next; }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;   // attach the remaining tail
        return dummy.next;
    }
}

/*
 * Complexity
 * ----------
 * Min-heap:          O(N log k) time, O(k) space (heap holds <= k nodes)
 * Divide & conquer:  O(N log k) time, O(log k) space (recursion depth)
 *   N = total nodes, k = number of lists
 *
 * Why NOT O(N log N)?
 * -------------------
 * Concatenate-then-sort would be O(N log N). The merge exploits the fact that
 * each list is ALREADY sorted: we only ever compare k "front" candidates, so
 * the log factor is log k (usually k << N), not log N.
 *
 * Why the dummy head?
 * -------------------
 * It removes the special case for the first appended node. Without it we'd
 * branch on "is this the first node?" every iteration. `dummy.next` is the
 * real head, and it is naturally null when every input list is empty.
 *
 * Trace — lists = [ [1,4,5], [1,3,4], [2,6] ]  (min-heap)
 * ------------------------------------------------------
 * seed heap with heads: {1(A), 1(B), 2(C)}
 * poll 1(A) -> out:1 ; push A.next=4      heap {1(B), 2(C), 4(A)}
 * poll 1(B) -> out:1 ; push B.next=3      heap {2(C), 4(A), 3(B)}
 * poll 2(C) -> out:2 ; push C.next=6      heap {3(B), 4(A), 6(C)}
 * poll 3(B) -> out:3 ; push B.next=4      heap {4(A), 4(B), 6(C)}
 * poll 4(A) -> out:4 ; push A.next=5      heap {4(B), 5(A), 6(C)}
 * poll 4(B) -> out:4 ; B.next=null        heap {5(A), 6(C)}
 * poll 5(A) -> out:5 ; A.next=null        heap {6(C)}
 * poll 6(C) -> out:6 ; C.next=null        heap {}
 * merged: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6  ✓
 *
 * Heap vs divide-and-conquer
 * --------------------------
 * Same time bound. Heap is simplest to reason about and extends naturally to
 * "merge k sorted streams" (external merge sort). Divide & conquer avoids the
 * heap's constant factor and per-node comparator overhead, and uses less space
 * when k is large.
 */
