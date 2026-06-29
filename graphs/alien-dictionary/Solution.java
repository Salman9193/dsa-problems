import java.util.*;

class Solution {

    // Alien Dictionary = two-phase problem:
    //   Phase 1: Build character ordering graph from adjacent word comparisons
    //   Phase 2: Topological sort (Kahn's BFS) to find valid character ordering
    //
    // Key insight: comparing adjacent sorted words pairwise, the FIRST differing
    // character gives one ordering constraint. Characters after the first difference
    // give NO information — the earlier mismatch already determines word order.
    //
    // Why NOT Union-Find?
    //   This is a directed ordering problem (a comes BEFORE b, not just "a and b
    //   are related"). DSU is symmetric — union(a,b) cannot model direction.
    //   Use Kahn's BFS or DFS postorder for directed topological sort.

    // ── Approach 1: Kahn's BFS (preferred) ────────────────────────────────────
    //
    // Naturally produces topological order; cycle detection via result.length() check.
    // Handles disconnected character sets cleanly (isolated characters get inDegree=0).
    public String alienOrder(String[] words) {
        // Step 1: Initialise all unique characters (even those with no ordering edges)
        Map<Character, List<Character>> adj      = new HashMap<>();
        Map<Character, Integer>         inDegree = new HashMap<>();
        for (String word : words)
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new ArrayList<>());
                inDegree.putIfAbsent(c, 0);
            }

        // Step 2: Build edges from adjacent word comparisons
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i+1];

            // CRITICAL edge case: longer word before its own prefix → invalid
            // e.g. ["abc","ab"] — impossible in any valid sorted dictionary
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";

            int minLen = Math.min(w1.length(), w2.length());
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    // First differing character: w1[j] < w2[j] in alien alphabet
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    inDegree.merge(w2.charAt(j), 1, Integer::sum);
                    break; // STOP after first difference — subsequent chars give no info
                }
            }
        }

        // Step 3: Kahn's BFS topological sort
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet())
            if (inDegree.get(c) == 0) queue.offer(c);

        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            char c = queue.poll();
            result.append(c);
            for (char next : adj.get(c)) {
                inDegree.merge(next, -1, Integer::sum);
                if (inDegree.get(next) == 0) queue.offer(next);
            }
        }

        // Cycle check: if not all unique characters included → cycle exists
        return result.length() == inDegree.size() ? result.toString() : "";
    }

    // ── Approach 2: DFS Postorder ─────────────────────────────────────────────
    //
    // Performs DFS, adds character to result AFTER all its successors are explored.
    // Reverse postorder = topological order.
    // 3-colour marking: 0=white, 1=grey(visiting), 2=black(done)
    public String alienOrderDFS(String[] words) {
        Map<Character, List<Character>> adj    = new HashMap<>();
        Map<Character, Integer>         colour = new HashMap<>();

        for (String word : words)
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new ArrayList<>());
                colour.putIfAbsent(c, 0);
            }

        // Build edges (same logic as above)
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i+1];
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
            int minLen = Math.min(w1.length(), w2.length());
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    break;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (char c : adj.keySet())
            if (colour.get(c) == 0 && !dfs(adj, colour, result, c))
                return "";

        return result.reverse().toString(); // reverse postorder = topological order
    }

    private boolean dfs(Map<Character, List<Character>> adj,
                        Map<Character, Integer> colour,
                        StringBuilder result, char node) {
        colour.put(node, 1); // grey
        for (char next : adj.get(node)) {
            if (colour.get(next) == 1) return false;  // back edge → cycle
            if (colour.get(next) == 0 && !dfs(adj, colour, result, next)) return false;
        }
        colour.put(node, 2); // black
        result.append(node); // postorder: add after all successors explored
        return true;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(C) where C = total characters across all words
 *   Phase 1 (graph build): each character visited once = O(C)
 *   Phase 2 (topo sort):   at most 26 nodes and 26 edges (fixed alphabet)
 *   Overall: O(C) dominated by input scanning
 *
 * Space: O(1) — at most 26 characters in graph (fixed alphabet size)
 *
 * Why BREAK after first differing character?
 *   "wrt" vs "wrf": positions 0(w==w) and 1(r==r) give no info.
 *   Position 2: t≠f → t comes before f. DONE.
 *   Position 3 doesn't exist. Even if it did, character ordering beyond
 *   the first mismatch is not determined by this pair comparison.
 *   Continuing would add false constraints not supported by the input.
 *
 * Why initialise ALL characters (even those with no edges)?
 *   A character appearing in words but never in any ordering constraint
 *   still has inDegree=0 → must be in the initial BFS queue.
 *   If not initialised, it would be missing from the result → wrong answer.
 *   Example: single word ["abc"] → a, b, c have no ordering constraints
 *   between them, but all must appear in the output.
 *
 * Why is ["abc","ab"] invalid?
 *   In any valid dictionary, "ab" (prefix of "abc") must come BEFORE "abc".
 *   If "abc" appears first, the dictionary itself is invalid → return "".
 *
 * Trace — words=["wrt","wrf","er","ett","rftt"]
 * -----------------------------------------------
 * Compare adjacent pairs:
 *   "wrt" vs "wrf": w==w, r==r, t≠f → edge t→f
 *   "wrf" vs "er":  w≠e           → edge w→e
 *   "er"  vs "ett": e==e, r≠t     → edge r→t
 *   "ett" vs "rftt": e≠r          → edge e→r
 *
 * Graph: t→f, w→e, r→t, e→r
 * inDegree: w=0, e=1, r=1, t=1, f=1
 *
 * Kahn's BFS:
 *   Queue=[w]
 *   w → result="w"; e inDegree 0 → enqueue
 *   e → result="we"; r inDegree 0 → enqueue
 *   r → result="wer"; t inDegree 0 → enqueue
 *   t → result="wert"; f inDegree 0 → enqueue
 *   f → result="wertf"
 *
 *   result.length()=5 == inDegree.size()=5 → return "wertf" ✓
 *
 * Trace — cycle: ["z","x","z"]
 * ------------------------------
 *   "z" vs "x": z≠x → edge z→x
 *   "x" vs "z": x≠z → edge x→z
 *   Graph: z→x, x→z  (cycle)
 *   inDegree: z=1, x=1
 *   No node with inDegree=0 → queue empty → result="" → return "" ✓
 */
