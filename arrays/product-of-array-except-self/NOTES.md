# Product of Array Except Self — Notes & Intuition

**LeetCode #238** | Arrays | Medium
Constraints: O(n) time, no division operator.

---

## Problem

Given an integer array `nums`, return an array `answer` where `answer[i]`
is the product of all elements except `nums[i]`.

```
Input:  [1, 2, 3, 4]
Output: [24, 12, 8, 6]
```

---

## Why Division Is Forbidden (and Why That's a Good Constraint)

Naive approach: compute total product, divide by each element.

This breaks when:
- Any element is `0` → division by zero
- Multiple zeros → which zero to cancel is ambiguous
- Large arrays → overflow before division

The no-division constraint forces a cleaner, more robust solution.

---

## The Key Insight — Prefix × Suffix

For each position `i`, the product of everything except `nums[i]` =
product of everything **left** of i × product of everything **right** of i.

```
nums    = [1,  2,  3,  4 ]

prefix  = [1,  1,  2,  6 ]   prefix[i] = product of nums[0..i-1]
suffix  = [24, 12, 4,  1 ]   suffix[i] = product of nums[i+1..n-1]

answer  = [24, 12, 8,  6 ]   answer[i] = prefix[i] * suffix[i]
```

---

## Approach 1 — Two Arrays O(n) space

```java
prefix[0] = 1;
for (int i = 1; i < n; i++)
    prefix[i] = prefix[i-1] * nums[i-1];

suffix[n-1] = 1;
for (int i = n-2; i >= 0; i--)
    suffix[i] = suffix[i+1] * nums[i+1];

answer[i] = prefix[i] * suffix[i];
```

---

## Approach 2 — Space-Optimised O(1) extra

Use the output array itself to store prefix products, then fold in
the suffix with a single right-to-left pass and a running variable.

```java
// Pass 1: fill answer with prefix products
answer[0] = 1;
for (int i = 1; i < n; i++)
    answer[i] = answer[i-1] * nums[i-1];

// Pass 2: multiply running suffix from right
int suffix = 1;
for (int i = n-1; i >= 0; i--) {
    answer[i] *= suffix;
    suffix    *= nums[i];
}
```

**Why this works:**
At the start of each iteration of Pass 2, `suffix` holds the product
of `nums[i+1..n-1]`. Multiplying it into `answer[i]` (which already
holds the prefix product) gives the correct result. Then `suffix` is
extended one step left by multiplying in `nums[i]`.

---

## Full Trace — `[1, 2, 3, 4]`

**Pass 1 (prefix):**

| i | answer[i] = answer[i-1] * nums[i-1] |
|---|--------------------------------------|
| 0 | 1 (base) |
| 1 | 1 * nums[0]=1 = 1 |
| 2 | 1 * nums[1]=2 = 2 |
| 3 | 2 * nums[2]=3 = 6 |

`answer = [1, 1, 2, 6]`

**Pass 2 (suffix, right-to-left):**

| i | answer[i] *= suffix | suffix *= nums[i] |
|---|---------------------|-------------------|
| 3 | 6  * 1  = 6  | 1  * 4 = 4  |
| 2 | 2  * 4  = 8  | 4  * 3 = 12 |
| 1 | 1  * 12 = 12 | 12 * 2 = 24 |
| 0 | 1  * 24 = 24 | 24 * 1 = 24 |

`answer = [24, 12, 8, 6]` ✓

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[1, 0]` | `[0, 1]` | Zero propagates through prefix/suffix correctly |
| `[0, 0]` | `[0, 0]` | Both positions see the other zero |
| `[-1, 1, -1]` | `[-1, 1, -1]` | Negative numbers handled naturally |
| `[2]` | `[1]` | Single element — no other elements, product = 1 |

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Two arrays | O(n) | O(n) |
| Space-optimised | O(n) | O(1) extra |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Allow division | If no zeros | Total product / element — handle zeros separately |
| With zeros | Multiple zeros in array | Track zero count; elements at non-zero positions get 0 if any zeros exist |
| Modular product | Result mod p | Apply mod at each multiplication step |
| Floating point | Float array | Same algorithm; beware precision accumulation |
| Generalise to arbitrary operation | Product → XOR, GCD, etc. | Same prefix/suffix structure, different operator |
| 2D version | Grid, product of all except row and column | Prefix row products × prefix column products |
| Streaming | Elements arrive one at a time | Maintain prefix product; suffix requires two-pass — buffer or approximate |

**Division approach when legal:** If no zeros, `total_product / nums[i]` in O(n) — but overflows for large arrays and requires floating point for non-divisible cases. The prefix×suffix approach is universally correct.

**The operator generalisation is deep:** Prefix XOR is used in range XOR queries (#1310). Prefix GCD for range GCD. Same O(n) two-pass structure throughout.
