# Reverse Bits — Notes & Intuition

**LeetCode #190** | Bit Manipulation | Easy
Reverse the 32 bits of an unsigned integer. Follow-up: what if the function is called many times?

---

## Problem

Given a 32-bit unsigned integer, return the integer with its bits in reverse order.

```
Input:  00000010100101000001111010011100
Output: 00111001011110000010100101000000
```

---

## The Straightforward Answer — 32-Step Loop

Pull the low bit off `n`, shift it into `result` from the *other* side, 32 times.

```java
int result = 0;
for (int i = 0; i < 32; i++) {
    result = (result << 1) | (n & 1);   // append n's low bit to result
    n >>>= 1;                           // logical shift (see gotcha)
}
return result;
```

O(1) time (a fixed 32 iterations) and O(1) space. This is the expected solution — there's
no hidden trick.

### The one real gotcha (Java): use `>>>`, not `>>`
Java has no unsigned `int`, so `>>` is *arithmetic* (sign-extending) and smears the sign
bit; `>>>` is the *logical* shift you want. Masking with `n & 1` each step saves you here
either way, but `>>>` is the habit to keep.

---

## Optimization 1 — Divide & Conquer (constant 5 steps)

Swap halves, then bytes, then nibbles, then bit-pairs, then adjacent bits. Reverses in 5
mask-and-shift steps regardless of input — the *Hacker's Delight* trick.

```java
n = (n >>> 16) | (n << 16);
n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);
n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);
return n;
```

Each line swaps adjacent groups of size 16, 8, 4, 2, 1. The hex masks select the
"left" (`0xaaaa…`, `0xcccc…`, …) and "right" (`0x5555…`, `0x3333…`, …) members of each pair.

---

## Optimization 2 — Byte Lookup Table (the follow-up)

If called many times, precompute the reverse of all 256 byte values once, then reverse the
four bytes of `n` and swap their order:

```
reverse(n) = table[n & 0xff]        << 24
           | table[(n >>> 8) & 0xff] << 16
           | table[(n >>> 16) & 0xff] << 8
           | table[(n >>> 24) & 0xff]
```

Amortizes to four lookups per call. Optionally cache whole results.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Bit-by-bit loop | O(1) — 32 steps | O(1) |
| Divide & conquer | O(1) — 5 steps | O(1) |
| Byte lookup table | O(1) — 4 lookups | O(256) table |

---

## Edge Cases

| Input | Output | Note |
|-------|--------|------|
| 0 | 0 | all zeros reverse to all zeros |
| all ones | all ones | palindromic bit pattern |
| a single low bit (1) | high bit set (0x80000000) | bit 0 maps to bit 31 |
| sign bit set | fine with `>>>` | the reason to avoid `>>` in Java |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Reverse bits in a byte / 64-bit word | different width | same divide & conquer with adjusted masks |
| Count set bits | not reversal | `n & (n-1)` loop (see Hamming Weight, #191) |
| Bit-reversal permutation of an array | reorder N elements | reverse each index's bits — the FFT reordering step |

**The through-line:** reversing bits is either a fixed 32-step loop or, better, a constant
5-step divide-and-conquer swap of adjacent bit-groups. The same **bit-reversal permutation**
is the data-reordering step of the Cooley–Tukey FFT (see Research & Foundations).
