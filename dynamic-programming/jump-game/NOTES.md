# Jump Game — Notes & Intuition

**LeetCode #55** | Greedy / Dynamic Programming | Medium

---

## Problem

Given array `nums` where `nums[i]` is the max jump from position i,
can you reach the last index starting from index 0?

```
[2,3,1,1,4] → true
[3,2,1,0,4] → false  (stuck at index 3, nums[3]=0)
```

---

## Core Insight — Track Maximum Reach

At any point, only one thing matters: the farthest position reachable so far.
If current index > maxReach → unreachable → false.

```java
for i in 0..n-1:
    if i > maxReach: return false   // can't get here
    maxReach = max(maxReach, i + nums[i])
return true
```

---

## Why Greedy Works

We don't need to track HOW we reached position i — just that we can.
If i <= maxReach, we can stand there. From there, we extend maxReach further.
The maximum reachable position is a monotonically non-decreasing scan.

---

## DP Alternative O(n²)

```java
dp[i] = true if index i is reachable
dp[0] = true
for i in 1..n-1:
    for j in 0..i-1:
        if dp[j] && j + nums[j] >= i: dp[i] = true
return dp[n-1]
```

Use greedy (O(n)) over DP (O(n²)) whenever possible.

---

## Jump Game II (#45) — Minimum Jumps

Same greedy, track BFS windows:
```java
jumps=0, currEnd=0, farthest=0
for i in 0..n-2:
    farthest = max(farthest, i + nums[i])
    if i == currEnd:
        jumps++
        currEnd = farthest
```
Each "window" [prev_currEnd+1, currEnd] = one jump.

---

## Complexity

Time O(n) · Space O(1)
