# Knapsack Variants — A Complete DSA Guide

The knapsack pattern is one of the most widely recurring DP patterns in
competitive programming and technical interviews. Recognising which variant
applies — and knowing the one direction rule that separates them — unlocks
a large family of problems.

---

## The Knapsack Mental Model

Every knapsack problem has four components:

```
Items      — each with a weight/cost and optionally a value
Capacity   — a hard constraint that cannot be exceeded
Decision   — include or exclude each item (0/1), or allow repeats (unbounded)
Objective  — maximise value, count ways, check feasibility, minimise cost
```

---

## The One Direction Rule

This single rule distinguishes the two most important variants:

```
RIGHT TO LEFT  →  0/1 Knapsack  (each item used at most once)
LEFT  TO RIGHT →  Unbounded     (each item may be reused)
```

### Why it works

In a 1D rolling DP array:

```java
// 0/1: right to left — dp[s - weight] reads PREVIOUS iteration's value
for (int s = capacity; s >= weight; s--)
    dp[s] = op(dp[s], dp[s - weight]);

// Unbounded: left to right — dp[s - weight] reads CURRENT iteration's value
// → same item can be "added" again in the same pass
for (int s = weight; s <= capacity; s++)
    dp[s] = op(dp[s], dp[s - weight]);
```

---

## The 6 Knapsack Scenarios

---

### Scenario 1 — Decision Knapsack

**Question:** Is it possible to select a subset summing to exactly the target?

```
dp[s] = true/false
dp[0] = true

// 0/1 (right to left)
for each item weight w:
    for s = target down to w:
        dp[s] = dp[s] || dp[s - w]

Answer: dp[target]
```

**LeetCode problems:**

| # | Problem | Items | Target |
|---|---------|-------|--------|
| #416 | Partition Equal Subset Sum | array values | sum/2 |
| #1049 | Last Stone Weight II | stone weights | sum/2 |

**Key insight:** If `total` is odd → return false immediately. Otherwise reduce
to "can we reach total/2?"

---

### Scenario 2 — Count Knapsack

**Question:** In how many distinct ways can we form the target?

```
dp[s] = number of ways
dp[0] = 1

// 0/1 (right to left)
for each item weight w:
    for s = target down to w:
        dp[s] += dp[s - w]

// Unbounded (left to right)
for each item weight w:
    for s = w to target:
        dp[s] += dp[s - w]
```

**LeetCode problems:**

| # | Problem | 0/1 or Unbounded | Order matters? |
|---|---------|-----------------|----------------|
| #494 | Target Sum | 0/1 | No |
| #518 | Coin Change II | Unbounded | No |
| #377 | Combination Sum IV | Unbounded | YES — use outer loop on s |
| #62 | Unique Paths | Unbounded 2D | No (grid paths) |

**Order matters vs doesn't:**
- `{1,2}` reaching 3: without order `{1,2}` and `{2,1}` are ONE way
- `{1,2}` reaching 3: with order they are TWO ways
- When order matters: loop over **sums in outer loop**, items in inner loop
- When order doesn't: loop over **items in outer loop**, sums in inner loop

```java
// Order doesn't matter (#518 Coin Change II)
for (int coin : coins)               // items outer
    for (int s = coin; s <= amount; s++)
        dp[s] += dp[s - coin];

// Order matters (#377 Combination Sum IV)
for (int s = 1; s <= target; s++)    // sums outer
    for (int num : nums)
        if (s >= num) dp[s] += dp[s - num];
```

---

### Scenario 3 — Optimisation Knapsack

**Question:** What is the minimum cost / maximum value achievable?

```
// Minimum cost to reach exactly target (e.g. #322 Coin Change)
dp[0] = 0
dp[s > 0] = INF

for each item (weight w, cost c):
    for s = w to target:          // unbounded: left to right
        dp[s] = min(dp[s], dp[s - w] + c)

// Maximum value within capacity (classic 0/1 knapsack)
dp[0] = 0

for each item (weight w, value v):
    for s = capacity down to w:   // 0/1: right to left
        dp[s] = max(dp[s], dp[s - w] + v)
```

**LeetCode problems:**

| # | Problem | Min/Max | 0/1 or Unbounded |
|---|---------|---------|-----------------|
| #322 | Coin Change | Min coins | Unbounded (left→right) |
| #279 | Perfect Squares | Min squares | Unbounded (left→right) |
| #474 | Ones and Zeroes | Max strings | 0/1 2D (right→left) |
| #1235 | Max Profit Job Scheduling | Max profit | 0/1 + binary search |

---

### Scenario 4 — Unbounded Knapsack

Items can be reused any number of times. The only change: left-to-right update.

```java
dp[0] = initial_value;
for (int item : items) {
    for (int s = item; s <= target; s++) {   // LEFT TO RIGHT
        dp[s] = op(dp[s], dp[s - item] + contribution);
    }
}
```

**LeetCode problems:**

| # | Problem | Why unbounded |
|---|---------|--------------|
| #322 | Coin Change | Same coin used multiple times |
| #518 | Coin Change II | Same coin used multiple times |
| #279 | Perfect Squares | Same square (1,4,9,...) multiple times |
| #343 | Integer Break | Same factor multiple times |

---

### Scenario 5 — 2D Knapsack

Each item has **two** costs; there are **two** capacities.

```java
// #474 Ones and Zeroes: each string has zeros_count and ones_count
// capacity = (i zeros, j ones), maximise number of strings taken

int[][] dp = new int[i + 1][j + 1];

for (String s : strs) {
    int zeros = countZeros(s), ones = countOnes(s);
    // right-to-left on BOTH dimensions (0/1)
    for (int a = i; a >= zeros; a--)
        for (int b = j; b >= ones; b--)
            dp[a][b] = Math.max(dp[a][b], dp[a - zeros][b - ones] + 1);
}
```

**LeetCode problems:**

| # | Problem | Dimension 1 | Dimension 2 |
|---|---------|-------------|-------------|
| #474 | Ones and Zeroes | number of 0s used | number of 1s used |
| #879 | Profitable Schemes | number of crimes done | profit earned |

---

### Scenario 6 — Bounded Knapsack

Each item can be used at most `k[i]` times (not 0/1, not unbounded).

**Approaches:**
1. **Naive expansion:** create `k[i]` copies of each item → treat as 0/1 → O(n × k × capacity)
2. **Binary grouping:** split `k[i]` into powers of 2 → O(n × log(k) × capacity)
3. **Monotonic deque:** optimal O(n × capacity)

```java
// Binary grouping approach
List<Integer> items = new ArrayList<>();
for (int i = 0; i < n; i++) {
    int rem = k[i];
    for (int t = 1; rem > 0; t *= 2) {
        int take = Math.min(t, rem);
        items.add(weight[i] * take);  // group into one "meta-item"
        rem -= take;
    }
}
// Now solve as 0/1 knapsack on expanded item list
```

Less common in LeetCode but appears in scheduling and resource allocation.

---

## Complete LeetCode Knapsack Map

| # | Problem | Scenario | Direction | 0/1 or Unbounded |
|---|---------|----------|-----------|-----------------|
| #62 | Unique Paths | Count | Left→Right | Unbounded (grid) |
| #279 | Perfect Squares | Optimise (min) | Left→Right | Unbounded |
| #322 | Coin Change | Optimise (min) | Left→Right | Unbounded |
| #343 | Integer Break | Optimise (max) | Left→Right | Unbounded |
| #377 | Combination Sum IV | Count (ordered) | Left→Right | Unbounded |
| #416 | Partition Equal Subset Sum | Decision | Right→Left | 0/1 |
| #474 | Ones and Zeroes | Optimise (max) | Right→Left | 0/1 2D |
| #494 | Target Sum | Count | Right→Left | 0/1 |
| #518 | Coin Change II | Count | Left→Right | Unbounded |
| #879 | Profitable Schemes | Count | Right→Left | 0/1 2D |
| #1049 | Last Stone Weight II | Decision | Right→Left | 0/1 |
| #1235 | Max Profit Job Scheduling | Optimise (max) | Right→Left | 0/1 |

---

## The Decision Tree — Identify the Variant

```
Can the same item be used more than once?
│
├── NO  →  0/1 Knapsack
│          Update: RIGHT TO LEFT
│          │
│          ├── Yes/No answer?       → Decision (#416, #1049)
│          ├── Count the ways?      → Count (#494)
│          ├── Max or Min?          → Optimise (#474, #1235)
│          └── Two constraints?     → 2D Knapsack (#474, #879)
│
└── YES →  Unbounded Knapsack
           Update: LEFT TO RIGHT
           │
           ├── Count ways (ordered)?  → Sums outer loop (#377)
           ├── Count ways (unordered)?→ Items outer loop (#518)
           └── Max or Min?            → Optimise (#322, #279)
```

---

## Summary of Transitions

| Scenario | Init | Transition | Direction |
|----------|------|-----------|-----------|
| Decision | dp[0]=true | dp[s] \|= dp[s-w] | Right→Left (0/1) |
| Count (0/1) | dp[0]=1 | dp[s] += dp[s-w] | Right→Left |
| Count (unbounded, unordered) | dp[0]=1 | dp[s] += dp[s-w] | Left→Right |
| Count (unbounded, ordered) | dp[0]=1 | dp[s] += dp[s-w] | Left→Right, sums outer |
| Min cost | dp[0]=0, rest=INF | dp[s] = min(dp[s], dp[s-w]+c) | Left→Right (unbounded) |
| Max value (0/1) | dp[0]=0 | dp[s] = max(dp[s], dp[s-w]+v) | Right→Left |
| Max value (unbounded) | dp[0]=0 | dp[s] = max(dp[s], dp[s-w]+v) | Left→Right |

---

## Common Pitfalls

1. **Wrong direction:** confusing 0/1 (right→left) with unbounded (left→right) gives wrong counts
2. **Order matters:** for ordered combinations (#377), the loop structure must change
3. **Initialisation:** for min-cost, initialise `dp[1..target] = INF`; for count, `dp[0] = 1`
4. **2D knapsack:** both dimensions must iterate right→left for 0/1 property
5. **Odd total:** for partition problems, check parity before running DP

---

## See Also

- `GREEDY_VS_DP.md` — when does greedy suffice instead of DP?
- `COMPLEXITY_THEORY.md` — why subset sum is NP-complete (pseudo-polynomial DP)
- Problem #416 (Partition Equal Subset Sum) — the canonical knapsack entry point
