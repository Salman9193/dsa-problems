# Burst Balloons — Notes & Intuition

**LeetCode #312** | Dynamic Programming (Interval DP) | Hard

---

## Problem

Given balloons with values nums[], bursting balloon i (with neighbours l and r)
gives nums[l]*nums[i]*nums[r] coins. Burst all balloons for maximum coins.

```
nums=[3,1,5,8] → 167
Optimal: burst 1(coins 3*1*5=15), 5(3*5*8=120), 3(1*3*8=24), 8(1*8*1=8) = 167
```

---

## Why "Last to Burst" (Not First)

If we pick the FIRST balloon to burst, its coins depend on which neighbours
remain — which depends on the full sequence. State explosion.

If we pick the LAST balloon to burst in interval (i,j), its neighbours are
FIXED as the sentinels i and j. Everything else in (i,j) is already gone.
Clean independent subproblems.

---

## The Interval DP Recurrence

```
Add sentinels: nums[-1]=nums[n]=1
dp[i][j] = max coins bursting all balloons strictly between i and j

dp[i][j] = max over k in (i,j) of:
    dp[i][k] + nums[i]*nums[k]*nums[j] + dp[k][j]

k = the LAST balloon burst in (i,j)
```

Fill in order of increasing interval length.

---

## The Interval DP Template

```java
for (int len = 2; len < n; len++)
    for (int left = 0; left < n-len; left++)
        int right = left + len;
        for (int k = left+1; k < right; k++)
            dp[left][right] = max(dp[left][right],
                dp[left][k] + cost(k) + dp[k][right])
```

This template solves: Burst Balloons, Matrix Chain Multiplication,
Minimum Cost to Merge Stones, Optimal BST.

---

## Complexity

Time O(n³) · Space O(n²)

---

## Extensions

| Problem | Variant |
|---------|---------|
| #1000 Merge Stones | Interval DP with merge cost |
| Matrix Chain | Classic O(n³) interval DP |
| Optimal BST | Weighted search cost minimisation |
