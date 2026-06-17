class Solution {

    // Approach: Exponential Shift-and-Subtract — O(log^2 n) time, O(1) space
    //
    // Without *, /, or %, use bit shifting to multiply divisor by powers of 2.
    // Find the largest multiple of divisor (via left shifts) that fits in dividend,
    // subtract it, add the corresponding power of 2 to quotient. Repeat.
    //
    // Why use long?
    //   Math.abs(Integer.MIN_VALUE) overflows int (-(-2^31) = 2^31 doesn't fit).
    //   Using long prevents this overflow throughout the computation.
    //
    // Only overflow case: MIN_VALUE / -1 = 2^31 > Integer.MAX_VALUE
    public int divide(int dividend, int divisor) {
        // Handle the only overflow case
        if (dividend == Integer.MIN_VALUE && divisor == -1)
            return Integer.MAX_VALUE;

        // Determine sign of result
        int sign = (dividend > 0) ^ (divisor > 0) ? -1 : 1;

        // Work with absolute values as longs to avoid overflow
        long a = Math.abs((long) dividend);
        long b = Math.abs((long) divisor);

        int result = 0;

        while (a >= b) {
            long temp = b, multiple = 1;

            // Double temp until it would exceed a (find largest 2^k * b <= a)
            while (a >= (temp << 1)) {
                temp   <<= 1;
                multiple <<= 1;
            }

            a -= temp;
            result += multiple;
        }

        return sign == 1 ? result : -result;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(log^2 n) — outer loop runs O(log n) times, inner loop O(log n) per iteration
 * Space: O(1)
 *
 * How it works — exponential search for the largest multiple:
 *   Instead of subtracting b once per iteration (O(n/b) iterations, too slow),
 *   we find the largest k such that b * 2^k <= a using left shifts.
 *   This reduces a by b*2^k in one step, so outer loop runs O(log n) times.
 *
 *   This is analogous to binary search: rather than scanning linearly,
 *   we double the candidate until it overshoots, then peel off that amount.
 *   Same pattern as: fast exponentiation, exponential search, Karatsuba.
 *
 * The Only Overflow Case:
 *   dividend = Integer.MIN_VALUE = -2^31
 *   divisor  = -1
 *   result   = 2^31 which exceeds Integer.MAX_VALUE (2^31 - 1)
 *   → clamp to Integer.MAX_VALUE
 *
 *   All other combinations fit in int. This is the ONLY case that needs
 *   special handling.
 *
 * Why XOR for sign:
 *   (positive ^ positive) = false → positive result
 *   (negative ^ positive) = true  → negative result
 *   (positive ^ negative) = true  → negative result
 *   (negative ^ negative) = false → positive result
 *
 * Trace — dividend=10, divisor=3
 * ---------------------------------
 * a=10, b=3, result=0
 *
 * Iter 1: temp=3, multiple=1
 *   10 >= 6? YES → temp=6,  multiple=2
 *   10 >= 12? NO → stop
 *   a = 10-6 = 4, result = 2
 *
 * Iter 2: temp=3, multiple=1
 *   4 >= 6? NO → stop
 *   a = 4-3 = 1, result = 3
 *
 * a=1 < b=3 → stop
 * return 3 ✓
 *
 * Real-world connection:
 *   This is exactly the __aeabi_uidiv algorithm used by ARM CMSIS on Cortex-M0+
 *   (which has no hardware divide instruction). The bit-shifting approach here
 *   IS the software division implementation on millions of embedded devices.
 */
