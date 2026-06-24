# Exam Room — Notes & Intuition

**LeetCode #855** | Design / Sorted Set | Medium

---

## Problem

Design a class where:
- `seat()` returns the seat maximising distance to the nearest occupied seat
- `leave(p)` removes the student at seat `p`
- Ties broken by lowest seat index

```
n=10:
seat() → 0   (empty room)
seat() → 9   (gap [0..9], best = n-1, dist=9)
seat() → 4   (gap [0..9] midpoint=4, dist=4)
seat() → 2   (gap [0..4] mid=2 dist=2 ties gap [4..9] mid=6 dist=2; 2<6)
leave(4)
seat() → 5   (gap [2..9] mid=5 dist=3 beats gap [0..2] mid=1 dist=1)
```

---

## Core Insight — Gaps Between Occupied Seats

The room is partitioned into gaps by occupied seats.
For each gap `[a, b]`, the best new seat is the midpoint `(a+b)/2`,
offering distance `(b-a)/2`.

**Two special boundary gaps:**

```
Left  gap [_, first]:  best seat = 0,   distance = first
Right gap [last, _]:   best seat = n-1, distance = n-1-last
```

---

## Algorithm

```java
TreeSet<Integer> occupied;  // sorted set of occupied seats

seat():
  if empty: return 0
  bestSeat=0, bestDist=first  // left boundary baseline
  for each gap [prev, curr] in sorted occupied seats:
      mid = (prev + curr) / 2
      dist = mid - prev
      if dist > bestDist: update bestSeat, bestDist
  if n-1-last > bestDist: bestSeat = n-1
  add bestSeat; return bestSeat

leave(p): remove p
```

---

## Tie-Breaking — Why Strict `>` Is Enough

The `if dist > bestDist` check (not `>=`) ensures ties go to the LOWER seat:

| Comparison | Who wins | Why |
|------------|---------|-----|
| Left boundary vs interior (same dist) | Left boundary (seat 0) | Interior needs `>` to displace |
| Interior gap A vs gap B (same dist) | Leftmost gap | Sorted iteration finds A first; B needs `>` to displace |
| Interior vs right boundary (same dist) | Interior | Right boundary needs `>` to displace |

All three cases favour the lower seat. No special-case code needed.

---

## Full Trace — n=10

```
occupied = {}
seat(): empty → return 0. occupied={0}

seat(): bestDist=0 (seat 0, left boundary)
  right: 9-0=9 > 0 → bestSeat=9, bestDist=9
  return 9. occupied={0,9}

seat(): bestDist=0
  gap [0,9]: mid=4, dist=4 > 0 → bestSeat=4, bestDist=4
  right: 0 (not >)
  return 4. occupied={0,4,9}

seat(): bestDist=0
  gap [0,4]: mid=2, dist=2 → bestSeat=2, bestDist=2
  gap [4,9]: mid=6, dist=2 → NOT > 2, skip
  return 2. occupied={0,2,4,9}

leave(4): occupied={0,2,9}

seat(): bestDist=0
  gap [0,2]: mid=1, dist=1 → bestSeat=1, bestDist=1
  gap [2,9]: mid=5, dist=3 → bestSeat=5, bestDist=3
  right: 0
  return 5 ✓. occupied={0,2,5,9}
```

---

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| `seat()` | O(k) iteration | O(k) |
| `leave(p)` | O(log k) | O(1) |

For 10⁴ calls, k ≤ 10⁴: O(k²) total = 10⁸ ops — acceptable.

---

## Optimised O(log k) Approach

Maintain a **TreeSet of intervals** sorted by (distance, -seat) for O(log k) seat():

```
Key insight: each gap [a,b] is itself a "candidate" with score (dist, -midpoint).
TreeSet of candidates → O(log k) peek-max and update.

On seat():
  pop best interval [a,b]
  split into [a, mid] and [mid, b]
  push both back

On leave(p):
  merge adjacent intervals [a,p] and [p,b] → [a,b]
  remove [a,p] and [p,b]; push [a,b]
```

Boundary gaps need special handling (virtual sentinels at -1 and n).

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| 2D exam room | Grid of seats | 2D gap analysis; harder |
| Weighted distances | Some seats preferred | Priority queue with custom score |
| Group seating | Must seat k adjacent students | Scan for gaps of size ≥ k |
| Reserved seats | Some seats unavailable | Pre-populate occupied set |
| Maximum capacity | Room has max k seats | Track count; reject if full |

---

## Edge Cases

| Scenario | Behaviour |
|----------|-----------|
| n=1 | Only seat 0; always return 0 |
| Leave then seat repeatedly | Handles correctly via sorted iteration |
| All seats filled | seat() is never called (problem guarantee) |
| Leave seat 0 or n-1 | Boundary gap recalculated on next seat() |
