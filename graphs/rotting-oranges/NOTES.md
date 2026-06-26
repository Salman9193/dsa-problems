# Rotting Oranges — Notes & Intuition

**LeetCode #994** | Graphs / Multi-Source BFS | Medium

---

## Problem

Grid: `0`=empty, `1`=fresh, `2`=rotten. Each minute, rotten spreads to
4-adjacent fresh oranges. Return minimum minutes until all oranges are rotten,
or `-1` if impossible.

```
[[2,1,1],[1,1,0],[0,1,1]]  →  4
[[2,1,1],[0,1,1],[1,0,1]]  →  -1
[[0,2]]                    →  0
```

---

## Core Insight — Multi-Source BFS

Rot spreads from ALL rotten oranges simultaneously. A single-source BFS
from one rotten orange gives wrong answers when multiple rotten sources exist.

**Solution:** Enqueue ALL initially rotten oranges before BFS begins.
BFS then naturally propagates level by level, where each level = one minute.

Each fresh orange's rot time = its BFS distance to the nearest rotten source.
The answer = maximum such distance (last orange to rot).

---

## Why Not DFS?

DFS doesn't compute shortest paths. It might reach a fresh orange via a
long DFS path before finding the short path from a nearby rotten source —
producing a time that's too high.

BFS guarantees: the first time a cell is reached is via the shortest path.

---

## Why Mark on Enqueue (Not Dequeue)?

```java
// WRONG: mark on dequeue
// Cell (r,c) could be enqueued by (r-1,c) and (r,c-1) before either processes it
// → same cell in queue twice → processed twice → wrong fresh count

// CORRECT: mark on enqueue
grid[nr][nc] = 2;  // mark immediately when adding to queue
fresh--;
queue.offer(new int[]{nr, nc});
// → each cell enters queue exactly once
```

---

## Why the levelSize Snapshot?

```java
int levelSize = queue.size(); // freeze this minute's count
for (int i = 0; i < levelSize; i++) { ... }
minutes++;
```

Without the snapshot, cells from minute 2 would be mixed into minute 1's
processing loop — we'd count the wrong number of minutes. The snapshot
ensures each minute processes exactly the cells that rotted at that minute.

---

## Full Trace — `[[2,1,1],[1,1,0],[0,1,1]]`

```
Initial: queue=[(0,0)], fresh=6

Min 1: level={(0,0)}
  rot (0,1): fresh=5, enqueue (0,1)
  rot (1,0): fresh=4, enqueue (1,0)
  queue=[(0,1),(1,0)]

Min 2: level={(0,1),(1,0)}
  from (0,1): rot (0,2)=fresh=3, enqueue (0,2)
  from (1,0): rot (1,1)=fresh=2, enqueue (1,1)
  queue=[(0,2),(1,1)]

Min 3: level={(0,2),(1,1)}
  from (1,1): rot (2,1)=fresh=1, enqueue (2,1)
  queue=[(2,1)]

Min 4: level={(2,1)}
  rot (2,2)=fresh=0, enqueue (2,2)
  queue=[]

fresh==0 → return 4 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(mn) — each cell enqueued/dequeued at most once |
| Space | O(mn) — queue at most mn cells |

---

## Edge Cases

| Grid | Return | Reason |
|------|--------|--------|
| No fresh oranges | 0 | Already done |
| No rotten, fresh exist | -1 | BFS never runs; fresh remains |
| Isolated fresh orange | -1 | No path from any rotten source |
| All rotten | 0 | fresh=0 initially |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| 8-directional spread | Diagonals too | Add 4 diagonal directions |
| Multiple decay rates | Some cells slower | Weighted BFS (Dijkstra) |
| Timed rotten oranges | Some rot at minute k | Use priority queue or delayed seeding |
| Count total rotted | Not time, but cells | Track cells converted |
| 3D grid | Depth dimension | Same BFS with 6 directions |
| Restore grid | Don't mutate | Use `dist[][]` array instead of overwriting |

---

## Pattern — Multi-Source BFS Template

```java
// 1. Seed ALL sources into queue
for each source: queue.offer(source); visited[source] = true;

// 2. BFS level by level
int steps = 0;
while (!queue.isEmpty()) {
    steps++;
    int size = queue.size();  // snapshot this level
    for (int i = 0; i < size; i++) {
        node = queue.poll();
        for each neighbour:
            if not visited: visited = true; queue.offer(neighbour);
    }
}
```

Other problems using this pattern:
- #286 Walls and Gates: multi-source BFS from all gates
- #317 Shortest Distance from All Buildings: multi-source BFS from buildings
- #1162 As Far from Land as Possible: multi-source BFS from all land cells
