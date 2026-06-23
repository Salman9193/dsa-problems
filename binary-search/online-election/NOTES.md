# Online Election — Notes & Intuition

**LeetCode #911** | Binary Search / Design | Medium

---

## Problem

Given vote history `persons[]` at timestamps `times[]`, design a structure
that answers: who was leading the election at time `t`?

Tie-breaking: the most recently voted person leads.

```
persons = [0,1,1,0,0,1,0]
times   = [0,5,10,15,20,25,30]

q(3)  → 0   (only t=0 vote, p0 leads)
q(12) → 1   (votes at t=0,5,10 → p0:1, p1:2 → p1 leads)
q(25) → 1   (p0:3, p1:3 tied → p1 voted most recently at t=25)
```

---

## Naive Approach — O(n) per Query

For each query `t`, scan all votes up to `t` and tally.

```java
int q(int t) {
    int[] votes = new int[n];
    int leader = -1;
    for (int i = 0; i < times.length && times[i] <= t; i++) {
        votes[persons[i]]++;
        if (leader == -1 || votes[persons[i]] >= votes[leader])
            leader = persons[i];
    }
    return leader;
}
```

O(n) per query. For Q queries: O(nQ). Too slow for large inputs.

---

## Optimal: Precompute + Binary Search — O(log n) per Query

**Key insight:** precompute `leader[i]` = who was leading right after the
`i`-th vote. Then for any query time `t`:
1. Binary search `times[]` for the rightmost index where `times[i] <= t`
2. Return `leader[i]`

```java
// Precompute — O(n)
for (int i = 0; i < n; i++) {
    votes[persons[i]]++;
    if (curLeader == -1 || votes[persons[i]] >= votes[curLeader])
        curLeader = persons[i];
    leader[i] = curLeader;
}

// Query — O(log n)
int left = 0, right = times.length - 1;
while (left < right) {
    int mid = left + (right - left + 1) / 2;  // upper-bias
    if (times[mid] <= t) left = mid;
    else right = mid - 1;
}
return leader[left];
```

---

## The Binary Search Pattern — Rightmost Element ≤ t

This is one of four standard binary search templates:

| Pattern | Condition | mid bias | Use case |
|---------|-----------|---------|---------|
| Exact match | `arr[mid] == t` | down | Standard search |
| Leftmost ≥ t | `arr[mid] >= t` → `right=mid` | down | Lower bound |
| **Rightmost ≤ t** | `arr[mid] <= t` → `left=mid` | **up** | **This problem** |
| First true | predicate(mid) | depends | Search on answer |

### Why upper-bias is required

```
mid = left + (right - left + 1) / 2   // upper-bias ← correct
mid = left + (right - left) / 2       // lower-bias ← infinite loop!
```

When `left = right - 1` and `times[left] <= t`:
- Lower-bias: `mid = left` → condition true → `left = mid = left` → **infinite loop**
- Upper-bias: `mid = right` → condition decides correctly → pointers converge

---

## Why `>=` for Tie-Breaking

```java
if (votes[persons[i]] >= votes[curLeader]) curLeader = persons[i];
```

When the new voter ties the current leader, `>=` transfers the lead to the
new voter — "most recent vote wins in a tie." Using `>` would not update
on a tie, giving the wrong answer for cases like `q(25)` above.

---

## Full Trace

`persons=[0,1,1,0,0,1,0]`, `times=[0,5,10,15,20,25,30]`

| i | persons[i] | votes | curLeader | leader[i] |
|---|-----------|-------|-----------|-----------|
| 0 | 0 | p0:1 | 0 | 0 |
| 1 | 1 | p0:1, p1:1 | 1 (tie→new) | 1 |
| 2 | 1 | p0:1, p1:2 | 1 | 1 |
| 3 | 0 | p0:2, p1:2 | 0 (tie→new) | 0 |
| 4 | 0 | p0:3, p1:2 | 0 | 0 |
| 5 | 1 | p0:3, p1:3 | 1 (tie→new) | 1 |
| 6 | 0 | p0:4, p1:3 | 0 | 0 |

`leader = [0,1,1,0,0,1,0]`

Binary search queries:
- `q(3)`:  find rightmost `times[i] <= 3`  → index 0 → `leader[0] = 0` ✓
- `q(12)`: find rightmost `times[i] <= 12` → index 2 → `leader[2] = 1` ✓
- `q(25)`: find rightmost `times[i] <= 25` → index 5 → `leader[5] = 1` ✓

---

## Complexity

| | Time | Space |
|--|------|-------|
| Constructor | O(n) | O(n) |
| Query q(t) | O(log n) | O(1) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Online queries (each answer affects next) | State changes with queries | Precomputed leader array still works |
| Real-time vote stream | Votes arrive continuously | Maintain a max-heap or leader variable; binary search still valid for past queries |
| Multiple candidates with ranked choice | Instant runoff | Recompute with elimination rounds |
| Find when leadership changed | All timestamps where leader changed | Scan leader[] for consecutive differences |
| Range queries: who leads between t1 and t2 | Not just at t | 2D range query; requires segment tree |
| Fractional votes | Votes have weights | Same algorithm; compare weighted counts |

**The precompute+binary-search pattern:** This problem is an instance of the general technique: precompute answers for all "interesting" time points, then answer arbitrary queries via binary search. The same pattern powers:
- Prometheus instant queries (precomputed TSDB samples)
- PostgreSQL MVCC (precomputed row versions)
- Sorted leaderboards (precomputed rankings)

See USE_CASES.md for the full connection.
