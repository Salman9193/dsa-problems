# Clone Graph — Notes & Intuition

**LeetCode #133** | Graphs | Medium

---

## Problem

Given a reference to a node in a connected undirected graph, return a
**deep copy** of the entire graph. Each node contains an integer value
and a list of its neighbors.

```java
class Node {
    int val;
    List<Node> neighbors;
}
```

The cloned graph must be completely independent — no shared references
with the original.

---

## The Two Core Challenges

### 1. Cycles
An undirected graph has bidirectional edges, so naive recursion loops:
```
1 → 2 → 1 → 2 → ...  (infinite)
```

### 2. Shared Nodes
If node 3 is a neighbor of both node 2 and node 4, the clone of 3
must be the **same object** in both neighbor lists — not two separate copies.

Both problems are solved by a single `HashMap<Node, Node>` from original → clone.

---

## DFS Approach

```java
Map<Node, Node> visited = new HashMap<>();

Node cloneGraph(Node node) {
    if (node == null) return null;
    if (visited.containsKey(node)) return visited.get(node); // handles cycles + sharing

    Node clone = new Node(node.val);
    visited.put(node, clone);   // ← MUST come before recursing into neighbors

    for (Node neighbor : node.neighbors)
        clone.neighbors.add(cloneGraph(neighbor));

    return clone;
}
```

### Why `visited.put` must come BEFORE the recursive loop

Consider the cycle: node 1 ↔ node 2.

**Incorrect order (put AFTER loop):**
```
cloneGraph(1)
  recurse into neighbor 2
    recurse into neighbor 1   ← 1 not in map yet!
      recurse into neighbor 2
        ... infinite recursion
```

**Correct order (put BEFORE loop):**
```
cloneGraph(1)
  clone1 = new Node(1)
  map.put(node1, clone1)      ← registered immediately
  recurse into neighbor 2
    clone2 = new Node(2)
    map.put(node2, clone2)
    recurse into neighbor 1   ← already in map, return clone1 ✓
```

---

## BFS Approach

```java
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
        map.get(curr).neighbors.add(map.get(neighbor));  // wire the edge
    }
}
return map.get(node);
```

BFS avoids deep call stacks — safer for very large graphs.

---

## DFS vs BFS

| | DFS | BFS |
|--|-----|-----|
| Time | O(V+E) | O(V+E) |
| Space | O(V) map + O(V) call stack | O(V) map + O(V) queue |
| Style | Concise, recursive | Iterative, explicit queue |
| Risk | Stack overflow on deep graphs | None |

---

## Full Trace — 4-Node Cycle (1-2-3-4)

```
DFS from node 1:

cloneGraph(1)
  c1 = Node(1), map = {1→c1}
  neighbor 2 → cloneGraph(2)
    c2 = Node(2), map = {1→c1, 2→c2}
    neighbor 1 → in map → return c1   ← cycle broken
    neighbor 3 → cloneGraph(3)
      c3 = Node(3), map = {..., 3→c3}
      neighbor 2 → in map → return c2
      neighbor 4 → cloneGraph(4)
        c4 = Node(4), map = {..., 4→c4}
        neighbor 3 → in map → return c3
        neighbor 1 → in map → return c1
      c3.neighbors = [c2, c4]
    c2.neighbors = [c1, c3]
  neighbor 4 → in map → return c4
  c1.neighbors = [c2, c4]
return c1 ✓
```

---

## Edge Cases

| Input | Output |
|-------|--------|
| `null` | `null` |
| Single node, no neighbors | Clone with empty neighbor list |
| Single node, self-loop | Clone pointing to itself |
| Two nodes connected bidirectionally | Two clones, each in the other's neighbor list |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Deep copy with cycles | Already handled here | HashMap as visited+mapping |
| Clone directed graph | Directed edges only | Same approach — adjacency list is directed |
| Clone weighted graph | Edges have weights | Store weight alongside neighbour in cloned node |
| Clone tree (no cycles) | Simpler case of this | Recursive clone without visited map needed |
| Serialise + deserialise graph (#297) | Convert to string and back | BFS + adjacency encoding |
| Detect isomorphism | Are two graphs structurally identical? | Certificate-based hashing or VF2 algorithm |
| Clone with n nodes and m edges | Large scale | BFS is O(V+E); for sparse graphs V>>E so O(V) dominates |

**Trade-off:** BFS clone uses O(V) queue space; DFS clone uses O(V) call stack. For very deep graphs (V = 10⁶, star topology), DFS risks stack overflow — BFS is safer in production.
