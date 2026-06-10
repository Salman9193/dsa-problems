# Container With Most Water — Notes & Intuition

**LeetCode #11** | Arrays / Two Pointers | Medium

---

## Problem

Given an array `height` of `n` integers, find two lines that together
with the x-axis form a container holding the most water.

```
Input:  [1, 8, 6, 2, 5, 4, 8, 3, 7]
Output: 49   →  lines at index 1 (h=8) and index 8 (h=7)
              area = (8-1) * min(8,7) = 7*7 = 49
```

---

## The Formula

```
area(left, right) = (right - left) * min(height[left], height[right])
```

Two factors: **width** (distance between walls) and **height** (the shorter wall).

---

## Why Brute Force Fails

Checking every pair → O(n²). For n=100,000: 10 billion comparisons.

---

## Two Pointer Insight

Start at the widest possible container (left=0, right=n-1) and work inward.

**Key question:** which pointer to move?

Moving the **taller** wall inward is useless:
- Width decreases ✗
- Height is still bounded by the shorter wall ✗
- Area can only decrease or stay the same ✗

Moving the **shorter** wall inward is the only hope:
- Width decreases, but height might increase
- The only chance to find a larger area

---

## Correctness Proof (Interchange Argument)

Suppose `height[left] <= height[right]`. Consider ALL containers
with `left` as the left wall:

- The current one (`left..right`) is the widest possible.
- Any other (`left..right'` where `right' < right`) has:
  - `width' = right' - left < right - left` (narrower)
  - `height' = min(height[left], height[right']) <= height[left]` (still bounded by left wall)
  - Therefore `area' <= width' * height[left] < width * height[left] <= current area`

No container with `left` as the left wall can beat what we already computed.
We can safely discard `left` → move `left++`.

This is the **interchange argument** — the greedy choice (moving the shorter
wall) never forecloses a better global solution. See `GREEDY_VS_DP.md`.

---

## Algorithm

```java
int left = 0, right = height.length - 1;
int maxWater = 0;

while (left < right) {
    int water = (right - left) * Math.min(height[left], height[right]);
    maxWater = Math.max(maxWater, water);

    if (height[left] <= height[right]) left++;
    else right--;
}
return maxWater;
```

---

## Full Trace — `[1, 8, 6, 2, 5, 4, 8, 3, 7]`

| left | h[l] | right | h[r] | area | action |
|------|------|-------|------|------|--------|
| 0 | 1 | 8 | 7 | 8 | l++ |
| 1 | 8 | 8 | 7 | **49** | r-- |
| 1 | 8 | 7 | 3 | 18 | r-- |
| 1 | 8 | 6 | 8 | 40 | l++ |
| 2 | 6 | 6 | 8 | 24 | l++ |
| 3 | 2 | 6 | 8 | 6 | l++ |
| 4 | 5 | 6 | 8 | 10 | l++ |
| 5 | 4 | 6 | 8 | 4 | l++ |
| 6 >= 6 → stop | | | | | |

Result: `49` ✓

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1,1]` | `1` | Minimum two walls |
| `[4,3,2,1,4]` | `16` | Both ends are height 4, width 4 |
| `[1,2,1]` | `2` | Middle higher wall doesn't help |
| Decreasing: `[5,4,3,2,1]` | `6` | Best is 0..3: 3*min(5,2)=6 |

---

## Complexity

| | |
|--|--|
| Time | O(n) — each pointer advances at most n times total |
| Space | O(1) — two pointers, one running max |

---

## Connection to Related Problems

| Problem | Technique | Difference |
|---------|-----------|------------|
| Container With Most Water (#11) | Two pointers from ends | Walls can vary, maximise area |
| Trapping Rain Water (#42) | Two pointers + prefix max | Every gap holds water, not just endpoints |
| Largest Rectangle in Histogram (#84) | Stack | Every bar is a potential wall |
