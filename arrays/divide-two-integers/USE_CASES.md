# Divide Two Integers — Real-World Use Cases

Bit-shifting division — the algorithm at the core of this problem — is
implemented in every embedded system that lacks a hardware divide instruction,
and its inverse (multiply-by-reciprocal + shift) is what GCC and LLVM emit
for every constant division in your code.

---

## 1. ARM Cortex-M0 — Software Division via Shift-and-Subtract

The ARM Cortex-M0 and Cortex-M0+ are the most widely deployed 32-bit
microcontroller cores in the world (billions of units in IoT, wearables,
motor control, and medical devices). They intentionally omit a hardware
divide instruction to reduce die size and power consumption.

When your C code on a Cortex-M0 writes `a / b`, the compiler links to
`__aeabi_uidiv` (unsigned) or `__aeabi_idiv` (signed) from the ARM CMSIS
library. These routines implement **exactly the shift-and-subtract algorithm**
in this problem — in ARM assembly.

### How it works (matches this solution exactly)

```
Cortex-M0 __aeabi_uidiv (pseudocode):
  1. Left shift divisor until it exceeds dividend (find largest 2^k * divisor)
  2. Subtract shifted divisor from dividend
  3. Record shift count in quotient
  4. Repeat until divisor > remaining dividend
  5. Return quotient (shift count sum) and remainder
```

This is word-for-word the inner and outer loops of our solution.

### Where this matters

- **Industrial motor control** — PID loop calculations require integer division
  at 10-100 kHz sample rates
- **Battery management systems** — state of charge calculations
- **Bluetooth/Zigbee IoT nodes** — packet processing on Cortex-M0 cores
- **Medical wearables** — real-time sensor data processing

### References

- **ARM CMSIS `__aeabi_uidiv` documentation:**
  https://developer.arm.com/documentation/dui0475/latest/the-arm-c-and-c---libraries/real-time-integer-division-in-the-arm-libraries

- **s-o-c.org — ARM Cortex M0+ Integer Division:**
  https://s-o-c.org/arm-cortex-m0-integer-division/
  "Like most microcontroller CPUs, the Cortex-M0+ does not have dedicated
  hardware to perform integer division. The bit shifting approach is preferred
  for better performance — the algorithm bit shifts the divisor left,
  subtracting from the dividend each time. The quotient is obtained from the
  shift count, the remainder from the result."

- **Microchip TB3178 — Faster Mathematical Calculation on Cortex-M0+:**
  https://ww1.microchip.com/downloads/en/DeviceDoc/90003178A.pdf
  "The Cortex-M0+ architecture does not have an assembly instruction for
  division. The DIVAS peripheral allows rapid division for the SAMC21 MCU
  — a hardware accelerator for the same shift-and-subtract algorithm."

---

## 2. Compiler Constant Folding — Granlund-Montgomery Method (GCC / LLVM / Clang)

When you write `x / 7` in C/C++/Java and the divisor is a compile-time
constant, **no division instruction is emitted**. Instead, the compiler
replaces it with a multiply-by-magic-number + right-shift sequence that
is mathematically equivalent but 3-10x faster.

### Why: division is slow, multiply+shift is fast

```
x / 7  →  (x * 0x24924925) >> 32  (approximately, for unsigned 32-bit)
```

The "magic number" `0x24924925` is a precomputed approximation of `2^32 / 7`.
Multiplying by it and shifting right gives the quotient.

This is the compiler expressing division AS bit shifting — the inverse
of what this LeetCode problem does.

### The Granlund-Montgomery algorithm

Torbjörn Granlund and Peter Montgomery implemented this technique in GCC
in 1994 — it has been standard in every major compiler since.

Given any non-zero 32-bit divisor known at compile time, the optimising
compiler can replace the division by a multiplication followed by a shift.
Warren's slightly better approach is found in LLVM's Clang compiler.
Many optimising compilers rely on equivalent techniques, either based
on the original Granlund-Montgomery article or on Warren's technique.

```c
// What you write:
uint32_t quotient = x / 7;

// What GCC emits (x86-64):
movl   %edi, %eax
movl   $613566757, %edx    ; = 0x24924925 (magic number for /7)
mull   %edx
shrl   $2, %edx            ; right shift
movl   %edx, %eax
```

### Latest research (2026)

The Granlund-Montgomery method has been continuously refined. A 2026 paper
achieved 1.67x speedup on Intel Sapphire Rapids and 1.98x speedup on Apple
M4 by optimising the 33-bit magic constant case for 64-bit targets — a pure
software win with no hardware changes, implemented as a merged LLVM patch.

### References

- **Original paper:** Granlund & Montgomery — *Division by Invariant Integers
  using Multiplication*, ACM SIGPLAN Notices 29(6):61–72, 1994.
  https://gmplib.org/~tege/divcnst-pldi94.pdf

- **Optimal bounds paper:** Lemire, Bartlett & Kaser — *Integer Division by
  Constants: Optimal Bounds*, arXiv:2012.12369 / Heliyon 2021.
  https://arxiv.org/pdf/2012.12369
  https://www.ncbi.nlm.nih.gov/pmc/articles/PMC8258644/

- **2026 LLVM optimisation:** *Optimization of 32-bit Unsigned Division by
  Constants on 64-bit Targets*, arXiv:2604.07902.
  https://arxiv.org/pdf/2604.07902
  "Achieved 1.67x speedup on Intel Xeon Sapphire Rapids and 1.98x on Apple
  M4. The LLVM patch has already been merged into llvm:main."

- **Russ Cox — Division via Multiplication:**
  https://research.swtch.com/divmult

---

## The Connection Between Both Use Cases

Both are two sides of the same coin — division expressed as bit operations:

| | What the algorithm does | Where used |
|--|-------------------------|-----------|
| **This problem** (shift-and-subtract) | Express division as repeated bit shifts + subtractions | Cortex-M0 `__aeabi_idiv`, embedded systems |
| **Granlund-Montgomery** (multiply + shift) | Express division as multiply by reciprocal + shift | GCC/LLVM constant folding, all modern compilers |

The LeetCode solution and the embedded CMSIS routine implement identical
logic. The Granlund-Montgomery method is the compiler's way of doing the
inverse — precomputing the "how many times does divisor fit" answer for
constant divisors at compile time.

---

## Further Reading

- ARM `__aeabi_uidiv`: https://developer.arm.com/documentation/dui0475/latest/the-arm-c-and-c---libraries/real-time-integer-division-in-the-arm-libraries
- Cortex-M0 division: https://s-o-c.org/arm-cortex-m0-integer-division/
- Granlund-Montgomery (1994): https://gmplib.org/~tege/divcnst-pldi94.pdf
- Optimal bounds (Lemire 2021): https://arxiv.org/pdf/2012.12369
- LLVM 2026 optimisation: https://arxiv.org/pdf/2604.07902
- Russ Cox explainer: https://research.swtch.com/divmult
