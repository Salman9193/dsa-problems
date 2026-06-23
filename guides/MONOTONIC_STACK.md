# Monotonic Stack — Complete Pattern Guide

A monotonic stack maintains elements in sorted (increasing or decreasing)
order. The key operation: when a new element breaks the monotone property,
pop elements until the property is restored — each pop resolves a pending
"next greater/smaller" query.

---

## The Pattern

```java
Deque<Integer> stack = new ArrayDeque<>();   // stores indices

for (int i = 0; i < n; i++) {
    // While stack is non-empty AND new element breaks monotone:
    while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
        int idx = stack.pop();
        // nums[i] is the NEXT GREATER ELEMENT for nums[idx]
        result[idx] = nums[i];
    }
    stack.push(i);
}
```

**Decreasing stack:** pop when new element is LARGER → resolves "next greater"
**Increasing stack:** pop when new element is SMALLER → resolves "next smaller"

---

## Core Problems

### #739 Daily Temperatures

For each day, find how many days until a warmer temperature.

```java
public int[] dailyTemperatures(int[] temps) {
    int n = temps.length;
    int[] result = new int[n];
    Deque<Integer> stack = new ArrayDeque<>();  // decreasing stack of indices

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && temps[stack.peek()] < temps[i]) {
            int idx = stack.pop();
            result[idx] = i - idx;  // days until warmer
        }
        stack.push(i);
    }
    return result;
}
```

---

### #496 Next Greater Element I & II

```java
// #496: Find next greater element for each element of nums1 in nums2
public int[] nextGreaterElement(int[] nums1, int[] nums2) {
    Map<Integer, Integer> nextGreater = new HashMap<>();
    Deque<Integer> stack = new ArrayDeque<>();

    for (int num : nums2) {
        while (!stack.isEmpty() && stack.peek() < num)
            nextGreater.put(stack.pop(), num);
        stack.push(num);
    }
    // Remaining in stack have no next greater → -1 (default in map)
    int[] result = new int[nums1.length];
    for (int i = 0; i < nums1.length; i++)
        result[i] = nextGreater.getOrDefault(nums1[i], -1);
    return result;
}

// #503: Circular array — double the array
for (int i = 0; i < 2 * n; i++) {
    int num = nums[i % n];
    // same stack logic
    if (i < n) stack.push(i);  // only push indices in first pass
}
```

---

### #84 Largest Rectangle in Histogram

For each bar, find the largest rectangle using it as the height.
The rectangle extends left to the nearest shorter bar and right to the nearest shorter bar.

```java
public int largestRectangleArea(int[] heights) {
    int n = heights.length;
    int maxArea = 0;
    Deque<Integer> stack = new ArrayDeque<>();  // increasing stack

    for (int i = 0; i <= n; i++) {
        int h = (i == n) ? 0 : heights[i];  // sentinel 0 at end

        while (!stack.isEmpty() && heights[stack.peek()] > h) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        stack.push(i);
    }
    return maxArea;
}
```

**Why increasing stack:** We pop when a shorter bar is found — the popped bar's
height is the limiting height. The width spans from the new stack top to the current index.

---

### #42 Trapping Rain Water (Stack Approach)

Alternative to the two-pointer approach — also uses monotonic stack.

```java
public int trap(int[] height) {
    int water = 0;
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < height.length; i++) {
        while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
            int bottom = height[stack.pop()];
            if (stack.isEmpty()) break;
            int left = stack.peek();
            int width = i - left - 1;
            int waterHeight = Math.min(height[left], height[i]) - bottom;
            water += width * waterHeight;
        }
        stack.push(i);
    }
    return water;
}
```

---

## Comparison: Two Types

| Stack type | Pop when | Resolves |
|-----------|----------|---------|
| Monotone decreasing | New element is LARGER | Next Greater Element |
| Monotone increasing | New element is SMALLER | Next Smaller Element |

---

## The Amortised Argument

Each element is pushed ONCE and popped AT MOST ONCE → O(n) total across all iterations.
This is the same amortised argument as BST Iterator and sliding window algorithms.

---

## Extensions

| Problem | Pattern |
|---------|---------|
| #85 Maximal Rectangle | Apply Largest Rectangle to each row as histogram |
| #907 Sum of Subarray Minimums | Contribution of each element × its "range of dominance" |
| #1762 Buildings with Ocean View | Decreasing stack; elements remaining at end |
| #2104 Sum of Subarray Ranges | Two monotonic stacks (one for max, one for min) |
