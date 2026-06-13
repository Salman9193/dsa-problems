# Car Pooling — Real-World Use Cases

The sweep line algorithm — transforming intervals into events and scanning
left-to-right — is one of the most broadly applicable patterns in systems
engineering, powering ride-sharing, calendar scheduling, and computational geometry.

---

## 1. Ride-Sharing — UberX Share / Lyft Shared Capacity Checking

Car pooling is literally the product being engineered at Uber and Lyft.
Before adding a new passenger to a vehicle already carrying passengers,
the matching algorithm must verify that at no route segment does the total
passenger count exceed vehicle capacity — exactly the car pooling algorithm.

### How it works in production

Each vehicle has a sequence of stops (existing pickups and dropoffs).
Adding a new passenger inserts a new pickup and dropoff into this sequence.
The capacity check sweeps through all stops in order, maintaining the
running passenger count, and rejects the assignment if it ever exceeds
the vehicle's capacity.

```
Existing vehicle route:  pick A at stop 2, drop A at stop 8
Candidate new passenger: pick B at stop 4, drop B at stop 6

Combined events:
  stop 2: +1 (A boards)
  stop 4: +1 (B boards)
  stop 6: -1 (B alights)
  stop 8: -1 (A alights)

Running count: 0 → 1 → 2 → 1 → 0
If vehicle capacity = 2 → feasible ✓
```

### References

- **Uber Research — Dynamic Pricing and Matching in Ride-Hailing Platforms:**
  https://www.uber.com/blog/research/dynamic-pricing-and-matching-in-ride-hailing-platforms/
  "Advanced matching algorithms are continuously designed at industrial scales.
  Uber's pool-matching mechanism called dynamic waiting is inspired by
  Express Pool — varying rider waiting before dispatch to improve matches."

- **UberX Share product logic (2022 relaunch):**
  https://getridewise.com/blog/uber-pool-uberx-share-lyft-shared-complete-guide
  "UberX Share's algorithm tries to match you with at most one other rider
  whose route overlaps yours significantly. The algorithm limits detours to
  no more than eight additional minutes — verifying at each segment that
  the vehicle does not exceed its capacity."

- **Springer — Modeling ridesharing trip matching:**
  https://link.springer.com/article/10.1007/s11116-018-9957-5
  "Uber Pool and Lyft Line prove that dynamic ridesharing works. The
  algorithm compares path sets of suppliers and demanders to find suitable
  matches while respecting vehicle capacity constraints along the shared route."

---

## 2. Sweep Line Algorithm — Computational Geometry Foundation

The sweep line is a foundational algorithmic paradigm in computational
geometry, used to solve interval intersection, closest pair, convex hull,
and Voronoi diagram problems — all by processing events in sorted order
rather than comparing all pairs.

### The General Pattern

```
// Transform 2D interval problem into 1D event sweep
events = []
for each interval [start, end]:
    events.push((start, +1))   // interval begins
    events.push((end,   -1))   // interval ends

sort(events)
running = 0
for (time, delta) in events:
    running += delta
    check_constraint(running)
```

Car Pooling, Meeting Rooms II, and the Skyline Problem all instantiate
this pattern with different running values and constraints.

### References

- **Wikipedia — Sweep line algorithm:**
  https://en.wikipedia.org/wiki/Sweep_line_algorithm
  "In computational geometry, a sweep line algorithm is an algorithmic
  paradigm that uses a conceptual sweep line to solve various problems
  in Euclidean space. It is one of the critical techniques in computational
  geometry."

- **Rajendra Bisoi — Mastering the Line Sweep Algorithm:**
  https://coderraj07.medium.com/mastering-the-line-sweep-algorithm-for-interval-problems-298c4dc562aa
  "Interval problems are everywhere — meetings, calendars, CPU scheduling,
  bookings, car pooling. They look different, but many reduce to one
  powerful idea: transform intervals into events (start/end) and sweep
  left to right. The maximum active count answers most overlap-based questions."

---

## The Sweep Line Family

All of these problems use the same core algorithm:

| Problem | Events | Running value | Constraint |
|---------|--------|--------------|-----------|
| Car Pooling (#1094) | board/alight at stops | passengers in car | ≤ capacity |
| Meeting Rooms II (#253) | meeting start/end | concurrent meetings | find min rooms |
| Merge Intervals (#56) | interval start/end | active intervals | merge overlaps |
| Skyline Problem (#218) | building enter/exit | current max height | track outline |
| Network bandwidth | stream start/end | reserved bandwidth | ≤ link capacity |
| Airline overbooking | passenger board/alight | load on flight leg | ≤ seat capacity |

---

## Further Reading

- Uber pool matching research: https://www.uber.com/blog/research/dynamic-pricing-and-matching-in-ride-hailing-platforms/
- Springer ridesharing model: https://link.springer.com/article/10.1007/s11116-018-9957-5
- Sweep line algorithm (Wikipedia): https://en.wikipedia.org/wiki/Sweep_line_algorithm
- Line sweep for interval problems: https://coderraj07.medium.com/mastering-the-line-sweep-algorithm-for-interval-problems-298c4dc562aa
