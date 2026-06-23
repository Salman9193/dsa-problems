# Divide Two Integers — Notes & Intuition

**LeetCode #29** | Arrays / Bit Manipulation | Medium
Constraint: no `*`, `/`, or `%` operators.

---

## Problem

Divide two integers without using multiplication, division, or mod.
Return the quotient truncated toward zero, clamped to `[−2³¹, 2³¹−1]`.

```
10  / 3   →  3    (10/3 = 3.33... → truncate)
7   / -2  →  -3   (7/-2 = -3.5 → truncate toward 0)
-2147483648 / -1  →  2147483647  (overflow → clamp)
```

---

## Why Naive Subtraction Is Too Slow

Subtract `divisor` from `dividend` repeatedly, count iterations → O(n/divisor).
For `dividend=2^31, divisor=1`: 2 billion iterations. Unacceptable.

---

## The Exponential Shift Insight

Instead of subtracting `b` (divisor) once per step, find the **largest multiple of b that is a power of 2** still ≤ `a` (dividend), subtract it, and add that power to the quotient.

```
Find largest k where b * 2^k <= a  →  subtract b * 2^k  →  result += 2^k
```

Left shift doubles `b` each inner loop iteration — exponential search.
This is the same "double until overshoot" pattern as fast exponentiation.

---

## Algorithm

```java
long a = Math.abs((long) dividend);
long b = Math.abs((long) divisor);
int result = 0;

while (a >= b) {
    long temp = b, multiple = 1;

    while (a >= (temp << 1)) {   // double until overshoot
        temp   <<= 1;
        multiple <<= 1;
    }

    a -= temp;
    result += multiple;
}
```

---

## Why `long` Is Required

```
Math.abs(Integer.MIN_VALUE) = Math.abs(-2^31) = 2^31
2^31 doesn't fit in int (max = 2^31 - 1) → silent overflow to negative!

Fix: cast to long before taking abs:
  Math.abs((long) dividend)  ← always safe
```

---

## The Only Overflow Case

```
dividend = -2147483648 = -2^31  (Integer.MIN_VALUE)
divisor  = -1
result   = +2^31 = 2147483648  > Integer.MAX_VALUE (2^31 - 1)
```

This is the **only** input combination that overflows. Handle it first:
```java
if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
```

All other combinations fit in int.

---

## Why XOR for Sign Detection

```java
int sign = (dividend > 0) ^ (divisor > 0) ? -1 : 1;
```

| dividend | divisor | XOR result | sign |
|----------|---------|-----------|------|
| + | + | false | +1 |
| + | - | true | -1 |
| - | + | true | -1 |
| - | - | false | +1 |

XOR is true when signs differ → result is negative.

---

## Full Trace — `dividend=10, divisor=3`

```
a=10, b=3, result=0

Iteration 1:
  temp=3,  multiple=1
  10 >= 6?  YES → temp=6,  multiple=2
  10 >= 12? NO  → stop
  a = 10-6 = 4,  result = 2

Iteration 2:
  temp=3,  multiple=1
  4 >= 6?   NO  → stop
  a = 4-3 = 1,  result = 3

a=1 < b=3 → outer loop ends
return 3 ✓
```

---

## Complexity

| | |
|--|--|
| Time | O(log² n) — outer loop O(log n), inner loop O(log n) per iteration |
| Space | O(1) |

---

## Connection to Algorithmic Patterns

| Algorithm | "Double until overshoot" pattern |
|-----------|----------------------------------|
| This problem | Double divisor via left shift |
| Fast exponentiation (x^n) | Square base, halve exponent |
| Exponential search | Double index to find range |
| Karatsuba multiplication | Recursive halving of digit length |

---

## Edge Cases

| dividend | divisor | Output | Reason |
|----------|---------|--------|--------|
| `-2147483648` | `-1` | `2147483647` | Only overflow case |
| `0` | `1` | `0` | Zero dividend |
| `1` | `1` | `1` | Same values |
| `-1` | `1` | `-1` | Negative dividend |
| `7` | `-2` | `-3` | Truncate toward zero (not -4) |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Division with remainder | Return both quotient and remainder | `remainder = dividend - quotient * divisor` |
| Floating-point division | Return float result | Continue shifting after quotient found; build decimal |
| Big integer division | Arbitrary precision | Apply this algorithm digit-by-digit on multi-word integers |
| Division by power of 2 | Divisor = 2^k | Right shift: `dividend >> k` — O(1) |
| Modulo without `%` | Find remainder | `dividend - divide(dividend, divisor) * divisor` |
| Ceiling division | Round up instead of truncate | `(dividend + divisor - 1) / divisor` using this algorithm |
| Exact division check | Is dividend divisible by divisor? | Check if `dividend - quotient*divisor == 0` |

**Granlund-Montgomery inverse:** The compiler-optimised version computes `x / C` as `(x * M) >> shift` where M is a precomputed magic number. This is the inverse of the shift-and-subtract approach — precomputing the inverse at compile time so division becomes multiply+shift at runtime. See USE_CASES.md for the full connection.

**Why the only overflow is MIN_VALUE / -1:** In 32-bit two's complement, MIN_VALUE = -2³¹ has no positive counterpart (MAX_VALUE = 2³¹ - 1). All other divisions either produce results in range or are undefined (division by zero, excluded by problem statement).
