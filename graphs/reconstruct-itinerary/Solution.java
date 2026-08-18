import java.util.*;

class Solution {

    // Reconstruct Itinerary — LeetCode #332
    //
    // EULERIAN PATH: use every ticket (edge) exactly once, start at JFK, return the
    // lexicographically smallest itinerary. Solved with HIERHOLZER'S ALGORITHM.
    //
    // Key idea — POST-ORDER + REVERSE: a naive "always fly to the smallest destination" greedy
    // gets stuck at dead ends (a sink airport reached before all tickets are used). Hierholzer's
    // fix: append an airport to the route only AFTER its outgoing edges are exhausted, then
    // reverse. Dead-ends get appended first and thus land last — exactly where they belong.
    //
    // A min-heap of destinations per airport gives lexical order for free.
    //
    // Time: O(E log E)  (E = tickets; log from the per-airport heaps).  Space: O(E).
    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> adj = new HashMap<>();
        for (List<String> t : tickets) {
            adj.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
        }

        // Iterative Hierholzer (explicit stack — no recursion-depth risk on long chains).
        LinkedList<String> route = new LinkedList<>();   // addFirst builds the reversed route
        Deque<String> stack = new ArrayDeque<>();
        stack.push("JFK");

        while (!stack.isEmpty()) {
            String node = stack.peek();
            PriorityQueue<String> pq = adj.get(node);
            if (pq != null && !pq.isEmpty()) {
                stack.push(pq.poll());        // dive into the smallest unused destination
            } else {
                route.addFirst(stack.pop());  // exhausted → prepend (post-order + reverse in one)
            }
        }
        return route;
    }

    // Recursive Hierholzer — shorter, but can overflow the call stack on ~10^4+ tickets in a chain.
    private final Map<String, PriorityQueue<String>> graph = new HashMap<>();
    private final LinkedList<String> result = new LinkedList<>();

    public List<String> findItineraryRecursive(List<List<String>> tickets) {
        for (List<String> t : tickets) {
            graph.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
        }
        dfs("JFK");
        return result;
    }

    private void dfs(String node) {
        PriorityQueue<String> pq = graph.get(node);
        while (pq != null && !pq.isEmpty()) {
            dfs(pq.poll());                   // visit smallest unused destination first
        }
        result.addFirst(node);                // post-order: prepend once edges are exhausted
    }
}

/*
 * Trace — [["JFK","KUL"],["JFK","NRT"],["NRT","JFK"]]
 * ---------------------------------------------------
 *   adj: JFK -> {KUL, NRT},  NRT -> {JFK}
 *   dive JFK -> KUL; KUL exhausted -> prepend KUL         route = [KUL]
 *   back JFK -> NRT -> JFK; JFK exhausted -> prepend JFK   route = [JFK, KUL]
 *      NRT exhausted -> prepend NRT                        route = [NRT, JFK, KUL]
 *   JFK exhausted -> prepend JFK                           route = [JFK, NRT, JFK, KUL]
 *   answer: JFK -> NRT -> JFK -> KUL   (KUL, the dead end, correctly ends up LAST)
 *
 * Why not naive greedy? Taking KUL first (it's lexically smallest) strands the NRT->JFK ticket.
 * Post-order insertion is what repairs this: a premature dead end sinks to the end after reversal.
 *
 * Eulerian path (every EDGE once) is polynomial; Hamiltonian (every VERTEX once) is NP-hard — don't
 * confuse them. de Bruijn sequences are Eulerian CIRCUITS on a de Bruijn graph (see the guide).
 */
