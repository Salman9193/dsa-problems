# Hamming Weight — Notes & Intuition

## Problem
Count the number of `1` bits in the binary representation of an integer.  
Also known as the **popcount** or **population count**.

---

## Approach 1: Bit Shift

```java
while (n != 0) {
    res += n & 1;
    n >>>= 1;
}
```

- `n & 1` isolates the lowest bit.
- `>>>=` is an **unsigned** right shift — critical in Java because `>>` is arithmetic
  (preserves the sign bit), so a negative `n` would never reach 0.
- Loops exactly 32 times for a 32-bit integer regardless of value.

---

## Approach 2: Brian Kernighan's Trick

```java
while (n != 0) {
    n &= (n - 1);
    res++;
}
```

### Why does `n & (n-1)` clear exactly the lowest set bit?

Subtracting 1 from `n` affects only the bits at and below the lowest set bit:

```
n       =  1 1 0 0   (lowest set bit at position 2)
n - 1   =  1 0 1 1   (position 2 flips 1→0, positions below flip 0→1)
n&(n-1) =  1 0 0 0   (AND zeroes out position 2 and everything below it)
```

Everything **above** the lowest set bit is identical in `n` and `n-1`,
so AND leaves it untouched. The net effect: exactly one set bit removed per iteration.

### Why is this faster?

The loop runs **k** times where k = number of set bits, not 32.  
For a sparse number like `1024` (only 1 bit set), it exits in 1 iteration instead of 11.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Bit shift | O(32) = O(1) | O(1) |
| Brian Kernighan | O(k), k = set bits | O(1) |

Both are O(1) since input is bounded to 32 bits.

---

## Production Note

Java's `Integer.bitCount(n)` compiles down to a single `POPCNT` CPU instruction
on modern hardware. Use it in production — no loop needed.

```java
int count = Integer.bitCount(n);
```

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Count trailing zeros | Find the position of lowest set bit | `n & (-n)` isolates lowest bit; log2 gives position |
| Parity check | Is number of 1-bits even or odd? | XOR all bits: `n ^= n >> k` for k=16,8,4,2,1 |
| Reverse bits (#190) | Flip bit order of 32-bit integer | Bit-by-bit construction or lookup table |
| PDEP/PEXT | Scatter/gather arbitrary bit positions | Hardware instruction (BMI2 extension) — see USE_CASES.md |
| Popcount for 64-bit | Extend to 64-bit integers | Same algorithm; Java `Long.bitCount()` |
| Hamming distance (#461) | Count differing bits between two numbers | `bitCount(x ^ y)` — XOR then popcount |
| k-th set bit | Find position of k-th 1-bit | Loop with `n & (n-1)` to clear bits one by one |

**Scaling:** For a stream of n numbers each with b bits, computing all popcounts takes O(nb) time. With SIMD/POPCNT instructions, throughput is ~4 integers per CPU cycle — critical for billion-scale similarity search (FAISS).
