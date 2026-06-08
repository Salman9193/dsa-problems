class Solution {

    // Approach 1: Bit shift — O(log n), loops over every bit position
    public int hammingWeight(int n) {
        int res = 0;
        while (n != 0) {
            res += n & 1;   // check lowest bit
            n >>>= 1;       // unsigned right shift (safe for negative ints)
        }
        return res;
    }

    // Approach 2: Brian Kernighan — O(k) where k = number of set bits
    // Each iteration clears exactly the lowest set bit.
    // Faster than Approach 1 when the number is sparse (few 1s).
    public int hammingWeightBK(int n) {
        int res = 0;
        while (n != 0) {
            n &= (n - 1);  // clear lowest set bit
            res++;
        }
        return res;
    }
}

/*
 * Key insight for n & (n-1):
 *   n     = ...1 0 0 0  (some trailing zeros after the lowest set bit)
 *   n-1   = ...0 1 1 1  (lowest set bit flips to 0, all below flip to 1)
 *   AND   = ...0 0 0 0  → lowest set bit cleared, everything above unchanged
 *
 * Note: Java's Integer.bitCount(n) compiles to a single POPCNT CPU instruction
 * on modern hardware — use that in production.
 */
