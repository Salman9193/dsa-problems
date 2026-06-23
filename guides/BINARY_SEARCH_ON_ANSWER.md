# Binary Search on Answer — Complete Pattern Guide

Instead of binary searching on an array, search on the ANSWER SPACE.
Given a monotone feasibility function, find the optimal answer in O(log(range)).

---

## The Template

```java
int left = minPossibleAnswer, right = maxPossibleAnswer;

while (left < right) {
    int mid = left + (right - left) / 2;

    if (feasible(mid)) {
        right = mid;      // mid works; try smaller (minimise)
    } else {
        left = mid + 1;   // mid doesn't work; need larger
    }
}
return left;
```

**Key insight:** If `feasible(mid)` is monotone (once false, always false;
once true, always true), binary search finds the boundary in O(log n).

---

## Core Problems

### #875 Koko Eating Bananas

Minimum eating speed k such that all bananas eaten in h hours.

```java
public int minEatingSpeed(int[] piles, int h) {
    int left = 1, right = Arrays.stream(piles).max().getAsInt();

    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canFinish(piles, mid, h)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canFinish(int[] piles, int speed, int h) {
    int hours = 0;
    for (int pile : piles) hours += (pile + speed - 1) / speed;  // ceil division
    return hours <= h;
}
```

---

### #1011 Capacity to Ship Packages Within D Days

Minimum ship capacity to ship all packages within d days.

```java
public int shipWithinDays(int[] weights, int days) {
    int left = Arrays.stream(weights).max().getAsInt();  // min: max single weight
    int right = Arrays.stream(weights).sum();            // max: all in one day

    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canShip(weights, mid, days)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canShip(int[] weights, int capacity, int days) {
    int daysNeeded = 1, currentLoad = 0;
    for (int w : weights) {
        if (currentLoad + w > capacity) { daysNeeded++; currentLoad = 0; }
        currentLoad += w;
    }
    return daysNeeded <= days;
}
```

---

### #410 Split Array Largest Sum

Minimise the largest subarray sum when splitting into k parts.

```java
public int splitArray(int[] nums, int k) {
    int left = Arrays.stream(nums).max().getAsInt();
    int right = Arrays.stream(nums).sum();

    while (left < right) {
        int mid = left + (right - left) / 2;
        if (canSplit(nums, mid, k)) right = mid;
        else left = mid + 1;
    }
    return left;
}

private boolean canSplit(int[] nums, int maxSum, int k) {
    int parts = 1, curr = 0;
    for (int n : nums) {
        if (curr + n > maxSum) { parts++; curr = 0; }
        curr += n;
    }
    return parts <= k;
}
```

---

### #378 Kth Smallest in Sorted Matrix

```java
public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    int left = matrix[0][0], right = matrix[n-1][n-1];

    while (left < right) {
        int mid = left + (right - left) / 2;
        if (countLE(matrix, mid) >= k) right = mid;
        else left = mid + 1;
    }
    return left;
}

private int countLE(int[][] matrix, int target) {
    int count = 0, n = matrix.length;
    int row = n-1, col = 0;
    while (row >= 0 && col < n) {
        if (matrix[row][col] <= target) { count += row+1; col++; }
        else row--;
    }
    return count;
}
```

---

## Identifying the Pattern

Ask three questions:

1. **Can I define a feasibility function?** — "Is it possible to achieve X with answer = mid?"
2. **Is the feasibility monotone?** — Once it's possible for mid=M, is it possible for all mid>M?
3. **Can I compute feasibility in O(n) or O(n log n)?** — Binary search adds O(log(range)) factor.

If all three: binary search on answer applies.

---

## Common Answer Spaces

| Problem type | left | right |
|-------------|------|-------|
| Eating speed, capacity | max single item | sum of all |
| Days, time | 1 | n × max |
| Kth element | min value | max value |
| Distance | 0 | max possible |
| Index in sorted structure | 1 | n |

---

## Extensions

| Problem | Feasibility check |
|---------|-----------------|
| #774 Minimize Max Distance to Gas Station | Count stations needed ≤ k |
| #1231 Divide Chocolate | Count pieces ≥ k |
| #1552 Magnetic Force Between Balls | Count balls placed with gap ≥ mid |
| #2064 Minimized Maximum Products | Balance products across stores |
