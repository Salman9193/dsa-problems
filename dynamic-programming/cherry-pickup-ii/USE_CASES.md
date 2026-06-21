# Cherry Pickup II — Real-World Use Cases

The cooperative two-agent grid DP in this problem is a simplified model
of Multi-Agent Path Finding (MAPF) — the core algorithmic problem behind
warehouse robotics at Amazon, Ocado, and autonomous vehicle fleets.

---

## 1. Warehouse Robotics — Cooperative Multi-Agent Path Finding (MAPF)

The Cherry Pickup II problem is a pedagogical version of the MAPF problem
with shared resource collection: two robots navigate a grid simultaneously,
cooperate to collect items, and shared items are counted once (not twice).

Real warehouse systems face this problem at scale — Amazon's Kiva robots
(now Amazon Robotics), Ocado's warehouse bots, and Alibaba's warehouse
robots all solve variants of cooperative MAPF to maximise throughput while
avoiding redundant work.

### The structural parallel

```
Cherry Pickup II:
  2 robots, r×c grid, move simultaneously row by row
  Shared cell → collect once
  Maximise total cherries

Warehouse MAPF with shared resource collection:
  n robots, warehouse floor grid, move simultaneously
  Same pod picked up by one robot (not duplicated)
  Minimise makespan / maximise throughput
```

The 3D DP state `(row, col1, col2)` in this problem corresponds to the
joint state `(position1, position2)` in 2-agent MAPF — the key insight
that agents on the same timestep share a row reduces the state space.

### The Co-MAPF Framework

The classical MAPF problem (collision avoidance only) was extended to
"Cooperative MAPF" (Co-MAPF) to handle truly cooperative settings where
agents must actively coordinate, not merely avoid each other — including
shared resource collection.

Cherry Pickup II is a 2-agent, layered-grid instance of Co-MAPF where:
- Agents share a timestep (simultaneous movement)
- Shared cells are collected once (cooperative reward structure)
- The goal is to maximise total collected value

### References

- **Paper:** *Cooperative Multi-Agent Path Finding: Beyond Path Planning
  and Collision Avoidance*, arXiv:2105.10993.
  https://arxiv.org/pdf/2105.10993
  "In Co-MAPF, achieving goals and completing tasks may not depend only
  on avoiding collisions between agents, but also on actively coordinating
  their actions. Our motivating problem is taken from the warehouse automation
  domain — robots of two types cooperate to transport inventory pods."

- **Paper:** Wurman, D'Andrea & Mountz — *Coordinating hundreds of
  cooperative, autonomous vehicles in warehouses*, AI Magazine, 29(1):9–19, 2008.
  The foundational paper on Amazon Kiva (now Amazon Robotics) warehouse
  automation — the first large-scale deployment of cooperative robot MAPF.

- **Paper:** *Multi-Agent Target Assignment and Path Finding for Intelligent
  Warehouse*, arXiv:2408.13750.
  https://arxiv.org/pdf/2408.13750
  "This paper models the Task Assignment and Path Finding (TAPF) problem
  for intelligent warehouses using cooperative multi-agent deep RL,
  simultaneously addressing task assignment and path finding."

---

## 2. Multi-Robot Warehouse Systems in Production

The algorithms powering real warehouse robots are direct generalisations
of the Cherry Pickup II DP:

| System | Robots | Algorithm | Scale |
|--------|--------|-----------|-------|
| Amazon Robotics (Kiva) | Hundreds | Cooperative MAPF + assignment | ~750,000 pods |
| Ocado Smart Platform | ~1000 | CBS (Conflict-Based Search) | Grid density >1 bot/m² |
| Alibaba DAMO Academy | Hundreds | MARL + MAPF hybrid | Large-scale warehouses |

### The Conflict-Based Search (CBS) connection

CBS is the standard optimal algorithm for small-scale MAPF. It works by:
1. Planning optimal paths for each robot independently
2. Detecting conflicts (two robots, same cell, same time)
3. Resolving conflicts by adding constraints and replanning

Step 1 is a single-robot version of Cherry Pickup (grid DP).
The full CBS is Cherry Pickup extended to n agents with constraint propagation.

- **Paper:** Sharon et al. — *Conflict-Based Search for Optimal Multi-Agent
  Pathfinding*, Artificial Intelligence, 219:40–66, 2015.

---

## 3. Multi-Agent Reinforcement Learning — Shared Reward Structure

Cherry Pickup II's "shared cells count once" rule directly models the
**shared reward** structure in cooperative multi-agent RL (MARL). When
two agents both visit the same state and receive a joint reward (not double),
the reward function is identical to cherry pickup's overlap handling.

This structure appears in:
- **StarCraft II** multi-agent challenges (shared unit kills)
- **Cooperative navigation** environments (shared goal credit)
- **Inventory management** (shared replenishment credit)

### Reference

- **Paper:** *Karma Mechanisms for Decentralised, Cooperative Multi Agent
  Path Finding*, arXiv:2604.07970.
  https://arxiv.org/pdf/2604.07970
  "Multi-robot cyber-physical systems are deployed in automated environments
  including warehouse logistics robots and multi-arm assembly systems.
  The Karma mechanism enables agents to resolve conflicts through pairwise
  replanning while balancing replanning effort across agents."

---

## The DP → Real-World Connection

```
Cherry Pickup II DP:
  State:      (row, col1, col2)
  Transition: 9 combinations of (dc1, dc2)
  Objective:  maximise total cherries

Warehouse 2-Robot MAPF:
  State:      (timestep, pos1, pos2)
  Transition: (move1, move2) combinations
  Objective:  maximise items collected / minimise makespan

n-Robot MAPF:
  State:      (timestep, pos1, ..., posn)
  Transition: exponential — requires CBS or MARL approximation
  Objective:  maximise throughput / minimise conflicts
```

Cherry Pickup II is the exactly-solvable 2-agent, layered-grid special
case. Real warehouse systems scale this to n agents using CBS, MARL, or
hybrid approaches because the full n-agent joint DP is intractable.

---

## Further Reading

- Co-MAPF paper: https://arxiv.org/pdf/2105.10993
- Warehouse TAPF (MARL): https://arxiv.org/pdf/2408.13750
- CBS (conflict-based search): Sharon et al., AI Journal 2015
- Amazon Kiva: Wurman, D'Andrea & Mountz, AI Magazine 2008
- Karma MAPF: https://arxiv.org/pdf/2604.07970
