import java.util.*;

class Node {
    public int val;
    public List<Node> neighbors;
    public Node(int val) {
        this.val = val;
        this.neighbors = new ArrayList<>();
    }
}

class Solution {

    // Approach 1: DFS with HashMap — O(V+E) time, O(V) space
    //
    // The HashMap serves two purposes:
    //   1. Cycle detection  — if we've already cloned a node, return the clone
    //   2. Shared reference — ensures a node cloned as neighbor of A is the
    //                         same object used as neighbor of B
    //
    // CRITICAL: put the clone in the map BEFORE recursing into neighbors.
    // If we put it after, a cycle (1→2→1) causes infinite recursion before
    // node 1 is ever registered.
    private Map<Node, Node> visited = new HashMap<>();

    public Node cloneGraph(Node node) {
        if (node == null) return null;

        if (visited.containsKey(node)) return visited.get(node);

        Node clone = new Node(node.val);
        visited.put(node, clone);           // register BEFORE recursing

        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraph(neighbor));
        }

        return clone;
    }

    // Approach 2: BFS with HashMap — O(V+E) time, O(V) space
    // Preferred when the graph is very deep (avoids call stack overflow).
    public Node cloneGraphBFS(Node node) {
        if (node == null) return null;

        Map<Node, Node> map = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        map.put(node, new Node(node.val));
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Node neighbor : curr.neighbors) {
                if (!map.containsKey(neighbor)) {
                    map.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                // Wire the edge in the cloned graph
                map.get(curr).neighbors.add(map.get(neighbor));
            }
        }

        return map.get(node);
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(V + E) — every node and every edge visited exactly once
 * Space: O(V)     — HashMap holds one entry per node; DFS also uses call stack
 *
 * Key invariant:
 *   visited.get(original) == clone of original, for every visited node.
 *   When we encounter a node already in the map we return its clone immediately,
 *   breaking cycles and ensuring shared neighbors point to the same clone object.
 *
 * Trace — 4-node cycle: 1-2-3-4-1
 * ---------------------------------
 * cloneGraph(1)
 *   clone1 created, map={1→c1}
 *   recurse neighbor 2
 *     clone2 created, map={1→c1, 2→c2}
 *     recurse neighbor 1 → already in map, return c1  ← cycle handled
 *     recurse neighbor 3
 *       clone3 created, map={..., 3→c3}
 *       recurse neighbor 2 → already in map, return c2
 *       recurse neighbor 4
 *         clone4 created, map={..., 4→c4}
 *         recurse neighbor 3 → return c3
 *         recurse neighbor 1 → return c1
 *       c3.neighbors = [c2, c4]
 *     c2.neighbors = [c1, c3]
 *   recurse neighbor 4 → already in map, return c4
 *   c1.neighbors = [c2, c4]
 * return c1
 */
