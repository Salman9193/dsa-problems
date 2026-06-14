import java.util.Random;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

class Solution {

    private ListNode head;
    private Random rand;

    public Solution(ListNode head) {
        this.head = head;
        this.rand = new Random();
    }

    // Approach 1: Reservoir Sampling (Algorithm R) — O(n) time, O(1) space
    //
    // Vitter's Algorithm R for k=1:
    //   When visiting the i-th node (1-indexed), replace the current selection
    //   with this node's value with probability 1/i.
    //
    // Proof each node has probability 1/n of being selected:
    //   P(node k selected) = P(selected at step k) × P(not replaced at k+1..n)
    //                      = (1/k) × (k/(k+1)) × ... × ((n-1)/n)
    //                      = (1/k) × (k/n)     [telescoping product]
    //                      = 1/n  ✓
    //
    // Works on streams of UNKNOWN size — no pre-pass needed.
    public int getRandom() {
        int result = 0;
        ListNode curr = head;
        int i = 1;

        while (curr != null) {
            // replace with probability 1/i
            if (rand.nextInt(i) == 0) result = curr.val;
            i++;
            curr = curr.next;
        }

        return result;
    }

    // Approach 2: Naive Two-Pass — O(n) time, O(1) space
    // Count length, then generate random index, then walk to it.
    // Requires two passes — does NOT work on streams.
    public int getRandomNaive() {
        int n = 0;
        ListNode curr = head;
        while (curr != null) { n++; curr = curr.next; }

        int target = rand.nextInt(n);
        curr = head;
        for (int i = 0; i < target; i++) curr = curr.next;
        return curr.val;
    }
}

/*
 * Complexity
 * ----------
 *                 Naive       Reservoir Sampling
 * Time per call:  O(n)        O(n)
 * Space:          O(1)        O(1)
 * Passes:         2           1
 * Works on stream: NO         YES
 *
 * Generalisation — sample k of n (Algorithm R for k > 1):
 *   Keep first k nodes in reservoir.
 *   For each subsequent node i (i > k):
 *     replace reservoir[rand.nextInt(i)] with probability k/i.
 *   This problem is the k=1 special case.
 *
 * Trace — list=[1,2,3]
 * ----------------------
 * i=1, curr=1: rand.nextInt(1)=0 always → result=1
 * i=2, curr=2: rand.nextInt(2)=0 with prob 1/2 → maybe result=2
 * i=3, curr=3: rand.nextInt(3)=0 with prob 1/3 → maybe result=3
 *
 * P(result=1) = 1 × (1/2 kept) × (2/3 kept) = 1×1/2×2/3 = 1/3 ✓
 * P(result=2) = 1/2 × 2/3                    = 1/3 ✓
 * P(result=3) = 1/3                           = 1/3 ✓
 *
 * Original paper: Vitter, J.S. — Random sampling with a reservoir,
 * ACM Transactions on Mathematical Software, 11(1):37-57, 1985.
 * https://dl.acm.org/doi/10.1145/3147.3165
 */
