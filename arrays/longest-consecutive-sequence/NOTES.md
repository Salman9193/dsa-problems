# Longest Consecutive Sequence — Notes & Intuition

**LeetCode #128** | Arrays | Medium  
Constraint: must run in **O(n)** time.

---

## Problem

Given an unsorted array of integers, return the length of the longest
sequence of consecutive integers.

```
Input:  [100, 4, 200, 1, 3, 2]
Output: 4   →  sequence [1, 2, 3, 4]
```

---

## Why Naive Approaches Fail

| Approach | Complexity | Problem |
|----------|-----------|---------|
| Sort, then scan | O(n log n) | Violates the O(n) constraint |
| For each number, search for next | O(n²) | Too slow for n=10^5 |

---

## The O(n) Insight — HashSet

**Observation:** every consecutive sequence has exactly one starting point —
the number `x` for which `x - 1` does NOT exist in the array.

This lets us skip every non-starting number in the outer loop,
ensuring each element is only ever processed inside one chain's while-loop.

```java
Set<Integer> set = new HashSet<>();
for (int n : nums) set.add(n);   // O(n) build

for (int n : set) {
    if (!set.contains(n - 1)) {  // only sequence starts enter here
        int length = 1;
        while (set.contains(n + length)) length++;
        longest = Math.max(longest, length);
    }
}
```

---

## Why is the while-loop O(n) total (not O(n²))?

The guard `!set.contains(n - 1)` ensures the while-loop runs for a given
number **only if that number is the start of its chain**.

- Each number belongs to exactly one chain.
- Each number is visited inside a while-loop exactly once across the entire run.
- Total while-loop iterations across all chains ≤ n.

Combined with the outer loop (also n iterations), total work = O(2n) = **O(n)**.

---

## Worked Trace

`nums = [100, 4, 200, 1, 3, 2]`  
`set  = {1, 2, 3, 4, 100, 200}`

| n | n-1 in set? | Action | Length |
|---|-------------|--------|--------|
| 100 | 99 → No | chain: 101? No | 1 |
| 4 | 3 → Yes | skip | — |
| 200 | 199 → No | chain: 201? No | 1 |
| 1 | 0 → No | chain: 2✓ 3✓ 4✓ 5? No | **4** |
| 3 | 2 → Yes | skip | — |
| 2 | 1 → Yes | skip | — |

**Answer: 4**

---

## Complexity

| | |
|--|--|
| Time | O(n) |
| Space | O(n) — HashSet stores all elements |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[]` | `0` | Empty array |
| `[1]` | `1` | Single element |
| `[1,1,1,1]` | `1` | All duplicates — HashSet deduplicates |
| `[-3,-2,-1,0]` | `4` | Negative numbers work identically |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return the sequence itself | Not just length | Track start of best sequence |
| Allow gaps of at most k | Consecutive within tolerance | Sort + scan, extend if gap ≤ k |
| 2D grid consecutive | Consecutive in any direction | BFS/DFS from each unvisited cell |
| Sorted input | Already sorted | Linear scan, no HashSet needed |
| Streaming input | Elements arrive one at a time | HashSet + boundary tracking; update both ends on insert |
| Count all sequences | Not just longest | Same HashSet approach, collect all |

**Scaling:** For n = 10⁸ elements, the HashSet approach uses ~4GB memory. Alternative: sort + scan uses O(n log n) time but O(1) extra space. For distributed settings (elements across shards), partial sequences need merging at boundaries — requires coordination.
