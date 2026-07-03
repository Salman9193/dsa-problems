# Daily Temperatures — Notes & Intuition

**LeetCode #739** | Stack / Monotonic Stack | Medium

---

## Problem

For each day in the temperature array, find how many days until a warmer
temperature. If no warmer day, answer is 0.

```
[73,74,75,71,69,72,76,73] → [1,1,4,2,1,1,0,0]
```

---

## The Monotonic Stack Pattern

Maintain a stack of INDICES with UNRESOLVED "next warmer" queries.
Stack stays in DECREASING order of temperatures.

```
for each day i:
    while stack not empty AND temps[i] > temps[stack.top]:
        j = stack.pop()
        result[j] = i - j    ← day j's next warmer day = day i
    stack.push(i)
```

**Why O(n)?** Each index is pushed exactly once and popped at most once.

---

## The Monotonic Stack Family

| Problem | Stack order | "Resolve" when |
|---------|------------|----------------|
| #739 Daily Temperatures | Decreasing | Found something GREATER |
| #496 Next Greater Element | Decreasing | Found something GREATER |
| #84 Largest Rectangle | Increasing | Found something SMALLER |
| #42 Trapping Rain Water | Alternative to two-pointers | Either direction |

**Decreasing stack:** resolves when finding something GREATER → "next greater element"
**Increasing stack:** resolves when finding something SMALLER → "next smaller element"

---

## Complexity

Time O(n) · Space O(n)
