import java.util.HashMap;
import java.util.Map;

class LRUCache {

    // Implementation: Doubly Linked List + HashMap
    //
    // HashMap: O(1) lookup of any node by key
    // Doubly Linked List: O(1) move-to-front and remove-from-anywhere
    //
    // Most Recently Used (MRU) → head of list
    // Least Recently Used (LRU) → tail of list
    //
    // Dummy head and tail sentinels eliminate null checks on boundary operations.

    private static class Node {
        int key, val;
        Node prev, next;
        Node(int k, int v) { key = k; val = v; }
    }

    private final Map<Integer, Node> map;
    private final int capacity;
    private final Node head, tail;  // dummy sentinels

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    // O(1) — lookup + move to front
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        moveToFront(node);
        return node.val;
    }

    // O(1) — insert at front or update + move to front; evict tail if over capacity
    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.val = value;
            moveToFront(node);
        } else {
            Node node = new Node(key, value);
            map.put(key, node);
            insertAtFront(node);
            if (map.size() > capacity) {
                Node lru = tail.prev;
                remove(lru);
                map.remove(lru.key);
            }
        }
    }

    private void moveToFront(Node node) { remove(node); insertAtFront(node); }

    private void insertAtFront(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}

/*
 * Complexity
 * ----------
 * get():  O(1) — HashMap lookup + O(1) linked list move
 * put():  O(1) — HashMap insert + O(1) linked list operations
 * Space:  O(capacity) — at most capacity nodes + map entries
 *
 * Why doubly linked list (not singly)?
 *   To remove a node in O(1), we need access to its PREVIOUS node.
 *   Singly linked list requires O(n) traversal to find prev.
 *   Doubly linked list: node.prev gives O(1) access.
 *
 * Why dummy head and tail?
 *   Without sentinels, every insert/remove must handle the case where
 *   the list is empty or the node is at the boundary.
 *   Sentinels eliminate ALL boundary checks — the list is never "empty"
 *   (always contains at least head and tail).
 *
 * Pointer operations — updateing 4 pointers on insert:
 *   node.next = head.next    (1. node points to old first)
 *   node.prev = head         (2. node points back to head)
 *   head.next.prev = node    (3. old first points back to node)
 *   head.next = node         (4. head points to node)
 *   ORDER MATTERS: step 3 must use head.next BEFORE step 4 changes it.
 */
