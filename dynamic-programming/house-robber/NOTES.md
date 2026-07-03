# House Robber — Notes & Intuition

**LeetCode #198** | Dynamic Programming | Medium

---

## Problem

Given an array of non-negative integers, find the maximum sum of
non-adjacent elements. No two adjacent elements can be picked.

```
[1,2,3,1] → 4   (pick index 0 and 2: 1+3)
[2,7,9,3,1] → 12  (pick index 0,2,4: 2+9+1)
```

---

## Core Insight — Take or Skip

At each position i, two choices:
```
Skip: best answer from i-1 positions
Rob:  nums[i] + best answer from i-2 positions

dp[i] = max(dp[i-1], dp[i-2] + nums[i])
```

Only the last two values are needed → O(1) space.

---

## The "Take-or-Skip" DP Family

This is the BASE PATTERN for a whole family of problems:

| Problem | Variant | Extra complexity |
|---------|---------|-----------------|
| #198 House Robber | Linear array | Base case |
| #213 House Robber II | Circular array | Split into two linear runs |
| #337 House Robber III | Tree | DFS returning (rob, skip) pair per node |
| #740 Delete and Earn | Value-indexed | Convert to house robber on bucket sums |

---

## Trace — `[2,7,9,3,1]`

```
prev2=0, prev1=0
num=2: curr=max(0,0+2)=2    [prev2=0, prev1=2]
num=7: curr=max(2,0+7)=7    [prev2=2, prev1=7]
num=9: curr=max(7,2+9)=11   [prev2=7, prev1=11]
num=3: curr=max(11,7+3)=11  [prev2=11,prev1=11]
num=1: curr=max(11,11+1)=12 → return 12 ✓
```

---

## Complexity

Time O(n) · Space O(1)
