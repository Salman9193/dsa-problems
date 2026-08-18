import java.util.*;

class Solution {

    // Cracking the Safe — LeetCode #753
    //
    // Build the SHORTEST string containing every length-n password over digits 0..k-1.
    // Such a string is a DE BRUIJN SEQUENCE B(k, n), and constructing one is an
    // EULERIAN CIRCUIT on the de Bruijn graph:
    //
    //   nodes = the k^(n-1) strings of length (n-1)
    //   edges = the k^n   strings of length n      (each edge IS a password)
    //   edge "s" goes from s[0..n-1) to s[1..n)     (append a digit, drop the oldest)
    //
    // Every node has in-degree = out-degree = k, so an Eulerian circuit always exists.
    // Hierholzer's post-order DFS walks every edge once; emitting one digit per edge
    // yields the sequence. Post-order append (after a node's edges are exhausted) is what
    // prevents getting stuck — exactly as in #332 Reconstruct Itinerary.
    //
    // Time: O(k^n * n).  Space: O(k^n * n).  Output length: k^n + (n-1), provably minimal.
    public String crackSafe(int n, int k) {
        if (n == 1) {                              // no overlap possible; list the digits
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < k; i++) s.append(i);
            return s.toString();
        }

        Set<String> seen = new HashSet<>();        // visited EDGES (n-length passwords)
        StringBuilder ans = new StringBuilder();
        String start = "0".repeat(n - 1);          // start at any (n-1)-node
        dfs(start, k, seen, ans);
        return ans.append(start).toString();       // seed the first (n-1)-window
    }

    private void dfs(String node, int k, Set<String> seen, StringBuilder ans) {
        for (int x = 0; x < k; x++) {
            String edge = node + x;                // the n-length password for this edge
            if (seen.add(edge)) {                  // add() == false if already visited
                dfs(edge.substring(1), k, seen, ans);  // next node drops the oldest digit
                ans.append(x);                     // POST-ORDER: append after edges exhausted
            }
        }
    }
}

/*
 * Trace — n = 2, k = 2
 * --------------------
 *   start node "0"; edges are {00,01,10,11}
 *   dfs("0"): edge 00 -> dfs("0")[0-edge used]; edge 01 -> dfs("1")
 *               dfs("1"): edge 10 -> dfs("0") exhausted, append 0
 *                         edge 11 -> dfs("1") exhausted, append 1
 *                         append 0
 *             append 1
 *   append 0
 *   ans = "0110"; + start "0" = "00110"   (contains 00, 01, 11, 10 — all four)
 *
 * Same Hierholzer engine as #332; the only difference is this graph is GENERATED (de Bruijn)
 * and we read off EDGE labels (digits) rather than vertex names.
 *
 * Real use: the literal keypad-cracking sequence, plus DNA assembly and rotary-encoder position
 * codes — all de Bruijn sequences = Eulerian circuits on a de Bruijn graph.
 */
