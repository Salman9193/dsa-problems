public class Solution {

    // Reverse Bits — LeetCode #190
    //
    // Reverse the 32 bits of an unsigned integer.
    //
    // Approach 1: bit-by-bit loop. Pull n's low bit and shift it into result from the
    // opposite side, 32 times. Use >>> (logical shift) because Java has no unsigned int.
    // O(1) time (32 iterations), O(1) space.
    public int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) {
            result = (result << 1) | (n & 1);
            n >>>= 1;
        }
        return result;
    }

    // Approach 2: divide & conquer — swap adjacent bit-groups of size 16, 8, 4, 2, 1.
    // A constant 5 steps regardless of input (Hacker's Delight). O(1) time, O(1) space.
    public int reverseBitsDivideConquer(int n) {
        n = (n >>> 16) | (n << 16);
        n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);
        return n;
    }

    // Approach 3 (follow-up: many calls): precompute reversals of all 256 byte values,
    // then reverse the four bytes and swap their order. Four lookups per call.
    private final int[] table = buildTable();

    private static int[] buildTable() {
        int[] t = new int[256];
        for (int b = 0; b < 256; b++) {
            int r = 0;
            for (int i = 0; i < 8; i++) r = (r << 1) | ((b >> i) & 1);
            t[b] = r;
        }
        return t;
    }

    public int reverseBitsLookup(int n) {
        return (table[n & 0xff] << 24)
             | (table[(n >>> 8) & 0xff] << 16)
             | (table[(n >>> 16) & 0xff] << 8)
             | (table[(n >>> 24) & 0xff]);
    }
}

/*
 * Trace (Approach 1) — reversing the low 3 bits shown, n = ...101
 *   start result = 0
 *   step 0: result = (0<<1)|1 = 1 ,   n = ...10
 *   step 1: result = (1<<1)|0 = 10,   n = ...1
 *   step 2: result = (10<<1)|1 = 101, n = ...0
 *   ... after 32 steps result holds n's bits fully reversed.
 *
 * Why >>> matters in Java
 * -----------------------
 * Java's int is signed and has no unsigned counterpart, so >> is arithmetic and copies
 * the sign bit downward. >>> is the logical shift that brings in zeros, which is what an
 * unsigned bit reversal requires.
 *
 * Why divide & conquer works
 * --------------------------
 * Reversing a sequence = recursively swapping its two halves and reversing each half.
 * Doing all swaps of a given group-size in parallel with a mask gives the 5 fixed steps
 * (16,8,4,2,1). The 0xaaaa.../0x5555... style masks pick the two members of each pair.
 */
