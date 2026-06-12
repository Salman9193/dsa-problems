# Trapping Rain Water — Notes & Intuition

**LeetCode #42** | Arrays / Two Pointers | Hard

---

## Problem

Given an elevation map `height[]`, compute how much water can be trapped
after raining.

```
Input:  [0,1,0,2,1,0,1,3,1,0,1,2]  →  6
Input:  [4,2,0,3,2,5]               →  9
```

---

## Core Insight

Water at position `i` is bounded by the shorter of the tallest wall to
its left and the tallest wall to its right:

```
water[i] = min(maxLeft[i], maxRight[i]) - height[i]
```

If that level is below the ground (`< height[i]`), no water sits there.

---

## Approach 1 — Prefix/Suffix Arrays (O(n) space)

Build `maxLeft[i]` and `maxRight[i]` in two passes, then sum:

```java
maxLeft[0] = height[0];
for (int i = 1; i < n; i++)
    maxLeft[i] = Math.max(maxLeft[i-1], height[i]);

maxRight[n-1] = height[n-1];
for (int i = n-2; i >= 0; i--)
    maxRight[i] = Math.max(maxRight[i+1], height[i]);

for (int i = 0; i < n; i++)
    water += Math.min(maxLeft[i], maxRight[i]) - height[i];
```

**Example:** `[4, 2, 0, 3, 2, 5]`

```
maxLeft  = [4, 4, 4, 4, 4, 5]
maxRight = [5, 5, 5, 5, 5, 5]
water    = [0, 2, 4, 1, 2, 0]  → total = 9 ✓
```

---

## Approach 2 — Two Pointers (O(1) space) ← Optimal

Eliminate both arrays using the observation:

> When `height[left] <= height[right]`:
> `maxRight (running) >= height[right] >= height[left]`
> ∴ `min(maxLeft, maxRight) = maxLeft` — left is the bottleneck.
> Water at `left` = `maxLeft - height[left]` — exact, no need for `maxRight`.

```java
int left = 0, right = n-1, maxLeft = 0, maxRight = 0, water = 0;

while (left < right) {
    if (height[left] <= height[right]) {
        if (height[left] >= maxLeft) maxLeft = height[left];
        else water += maxLeft - height[left];
        left++;
    } else {
        if (height[right] >= maxRight) maxRight = height[right];
        else water += maxRight - height[right];
        right--;
    }
}
```

---

## Why This Is Different From Container With Most Water

| | Container (#11) | Trapping Rain (#42) |
|--|-----------------|---------------------|
| Goal | Maximise ONE container | Sum water at ALL positions |
| Pointers move | Always move shorter wall | Move whichever side is shorter |
| Per-position water | Not computed | min(maxL, maxR) - height[i] |
| Space | O(1) | O(1) two-pointer / O(n) prefix |

---

## Left-Right Decomposition Family

All three problems decompose per-position values into a left contribution
and a right contribution:

| Problem | Left value | Right value | Combined |
|---------|-----------|-------------|---------|
| #238 Product Except Self | prefix product | suffix product | product |
| #42 Trapping Rain Water | max height left | max height right | min → water |
| #84 Largest Rectangle | nearest smaller left | nearest smaller right | area |

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Prefix/Suffix | O(n) | O(n) |
| Two Pointers | O(n) | O(1) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1,2,3]` | `0` | Monotone increasing — no water |
| `[3,2,1]` | `0` | Monotone decreasing — no water |
| `[3,0,3]` | `3` | Single valley depth 3 |
| `[1]` | `0` | Single bar |
| `[2,0,2]` | `2` | Symmetric valley |
