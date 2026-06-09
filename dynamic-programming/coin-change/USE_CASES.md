# Coin Change — Real-World Use Cases

The coin change problem — minimise the number of fixed-denomination units
to reach a target — is one of the most broadly applicable DP patterns,
appearing in hardware, compilers, and OS kernels.

---

## 1. ATMs & Vending Machines

The most direct real-world instance. An ATM must dispense a requested
withdrawal using available bill denominations while minimising the number
of notes dispensed. A vending machine returning change from a purchase
faces the same problem — if one coin tube is empty, the machine must
find an alternative minimum combination from remaining denominations.

> "This algorithm can be used to distribute change in a vending machine.
> Buying a 60-cent soda with a dollar leaves 40 cents change — 1 quarter,
> 1 dime, and 1 nickel. If the nickel tube were empty, the machine would
> dispense 4 dimes instead." — OpenGenus, The Coin Change Problem

Greedy works for canonical currency systems (US coins), but DP is required
for arbitrary denomination sets (e.g. some countries use denominations
like 1, 4, 15, 20, 50 where greedy fails).

- **Textbook reference:**
  Runestone Academy — *Problem Solving with Algorithms and Data Structures*,
  Chapter 4: Dynamic Programming — uses the vending machine / coin change
  problem as the canonical introduction to DP.
  https://runestone.academy/ns/books/published/pythonds/Recursion/DynamicProgramming.html

- **ATM DP implementation (CashMan):**
  Formal problem statement: given amount W and denominations, find the
  minimum set of notes xj such that Sum(wj * xj) = W and Sum(xj) is minimum.
  https://github.com/sedukull/CashMan

---

## 2. Compiler Register Allocation — Spill Cost Minimisation

When a compiler has more live variables than available CPU registers, it
must "spill" some variables to memory (RAM). Deciding which variables to
spill and how many load/store instructions to insert is a minimisation
problem structurally identical to coin change:

- **Coins** = spill instruction costs for each variable
- **Amount** = register budget constraint
- **Minimise** = total number of load/store (spill/reload) instructions

The classic formulation uses graph colouring to find the minimum register
count, and DP/cost estimation to minimise spill instructions when registers
are insufficient.

- **Canonical paper:**
  Chaitin, G.J. — *Register Allocation and Spilling via Graph Coloring*,
  ACM SIGPLAN Symposium on Compiler Construction, 1982.
  https://web.eecs.umich.edu/~mahlke/courses/583f12/reading/chaitin82.pdf
  "Spill decisions are now made on the basis of the register conflict graph
  and cost estimates — when the compiler cannot colour the graph with k
  colours (k = available registers), it must add spill/reload code."

- **Modern extension:**
  Burroughs — *Register allocation and spilling using the expected distance
  heuristic*, Software: Practice and Experience, 46:1499–1523, 2016.
  https://onlinelibrary.wiley.com/doi/abs/10.1002/spe.2393
  "The primary goal of the register allocation phase is to minimize register
  spills to memory. Spill decisions are made based on the costs of spilling
  a virtual register and the assumed placement of spill instructions."

- **LLVM implementation:**
  LLVM's greedy register allocator (`lib/CodeGen/RegAllocGreedy.cpp`) uses
  spill cost estimation — a direct application of the DP minimisation pattern.
  https://github.com/llvm/llvm-project/blob/main/llvm/lib/CodeGen/RegAllocGreedy.cpp

---

## 3. Linux Slab Allocator — Memory Unit Minimisation

The Linux kernel slab allocator satisfies memory allocation requests using
fixed-size slab caches (denominations: 32, 64, 128, 256, 512... bytes).
When `kmalloc(size)` is called, the kernel selects the smallest slab cache
that covers the request — equivalent to picking the minimum number of
fixed-size units to satisfy the required allocation size.

- **How it works:**
  Sets of pages ("slabs") are set aside for objects of a fixed size,
  allowing them to be efficiently packed with minimum overhead and waste.
  Linux has three slab allocator implementations: SLAB (original),
  SLUB (default on most systems), and SLOB (minimal systems).

- **Linux kernel documentation:**
  https://www.kernel.org/doc/gorman/html/understand/understand011.html
  Linux keeps caches for small memory allocations (size-N and size-N DMA
  caches), viewable from /proc/slabinfo.

- **LWN article — Making slab-allocated objects movable:**
  https://lwn.net/Articles/784964/
  "Sets of pages ('slabs') are set aside for objects of a fixed size,
  allowing them to be efficiently packed with a minimum of overhead and waste."

- **Wikipedia — Slab allocation:**
  https://en.wikipedia.org/wiki/Slab_allocation

---

## Summary

| Domain | Coins (denominations) | Amount (target) | Minimise |
|--------|-----------------------|-----------------|---------|
| ATM / vending machine | Bill/coin denominations | Withdrawal or change amount | Notes/coins dispensed |
| Compiler (LLVM/GCC) | Spill instruction costs per variable | Register budget | Load/store instructions inserted |
| Linux slab allocator | Slab sizes (32, 64, 128... bytes) | Requested allocation size | Slab units used, fragmentation |

---

## Further Reading

- Vending machine DP (textbook): https://runestone.academy/ns/books/published/pythonds/Recursion/DynamicProgramming.html
- ATM CashMan DP: https://github.com/sedukull/CashMan
- Chaitin register allocation (1982): https://web.eecs.umich.edu/~mahlke/courses/583f12/reading/chaitin82.pdf
- LLVM greedy register allocator: https://github.com/llvm/llvm-project/blob/main/llvm/lib/CodeGen/RegAllocGreedy.cpp
- Linux slab allocator docs: https://www.kernel.org/doc/gorman/html/understand/understand011.html
- LWN slab article: https://lwn.net/Articles/784964/
