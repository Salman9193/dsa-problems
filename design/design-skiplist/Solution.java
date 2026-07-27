import java.util.Random;

// Design Skiplist — LeetCode #1206
//
// A sorted structure with O(log n) EXPECTED search/insert/delete, achieved by RANDOMIZING node
// heights instead of enforcing balance. Each node participates in level 1 always, level 2 with
// probability p, level 3 with p^2, ... so there are ~log(n) levels and each is an "express lane"
// skipping ~half the nodes below it.
//
// Randomization replaces rebalancing: no rotations, no recoloring, trivial concurrency.
//
// Ref: William Pugh, "Skip Lists: A Probabilistic Alternative to Balanced Trees,"
//      Communications of the ACM 33(6):668-676, 1990.  DOI 10.1145/78973.78977
class Skiplist {

    private static final int MAX_LEVEL = 16;      // ~log2(65536); raise to 32 for billions
    private static final double P = 0.5;

    private static class Node {
        int val;
        Node[] next;
        Node(int val, int level) { this.val = val; this.next = new Node[level]; }
    }

    private final Node head = new Node(-1, MAX_LEVEL);
    private int level = 1;                          // highest currently-occupied level
    private final Random rng = new Random();

    // geometric level: keep "flipping heads" while below the cap
    private int randomLevel() {
        int lvl = 1;
        while (rng.nextDouble() < P && lvl < MAX_LEVEL) lvl++;
        return lvl;
    }

    public boolean search(int target) {
        Node cur = head;
        for (int i = level - 1; i >= 0; i--) {                     // top lane down to level 0
            while (cur.next[i] != null && cur.next[i].val < target) {
                cur = cur.next[i];                                 // skip forward
            }
        }
        cur = cur.next[0];
        return cur != null && cur.val == target;
    }

    public void add(int num) {
        Node[] update = new Node[MAX_LEVEL];                       // predecessor at each level
        Node cur = head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            update[i] = cur;                                       // where we dropped a level
        }

        int lvl = randomLevel();
        if (lvl > level) {
            for (int i = level; i < lvl; i++) update[i] = head;    // new lanes start at head
            level = lvl;
        }

        Node node = new Node(num, lvl);
        for (int i = 0; i < lvl; i++) {                            // splice in at each of its levels
            node.next[i] = update[i].next[i];
            update[i].next[i] = node;
        }
    }

    public boolean erase(int num) {
        Node[] update = new Node[MAX_LEVEL];
        Node cur = head;
        for (int i = level - 1; i >= 0; i--) {
            while (cur.next[i] != null && cur.next[i].val < num) {
                cur = cur.next[i];
            }
            update[i] = cur;
        }

        cur = cur.next[0];
        if (cur == null || cur.val != num) return false;          // not present

        for (int i = 0; i < level; i++) {                         // unlink at every level it's on
            if (update[i].next[i] == cur) {
                update[i].next[i] = cur.next[i];
            }
        }
        while (level > 1 && head.next[level - 1] == null) level--; // shrink now-empty top lanes
        return true;
    }
}

/*
 * The shared engine of all three ops: walk the lanes top-to-bottom, and at each level record the
 * last node before where `num` belongs (the update[] array). search() ignores update[]; add() and
 * erase() splice/unlink through it. Master that loop and the structure is trivial.
 *
 * Why O(log n) expected: ~n nodes on level 1, ~n/2 on level 2, ~n/4 on level 3, ... => ~log2(n)
 * levels, and you traverse O(1/p)=O(2) nodes per level before dropping down. Worst case O(n) is
 * possible but no input FORCES it (levels are random, not data-driven) — like randomized quicksort.
 *
 * Real use: Redis sorted sets (ZSET); LSM-tree memtables in LevelDB/RocksDB/Cassandra, where easy
 * concurrent sorted insertion is exactly what a skip list gives.
 */
