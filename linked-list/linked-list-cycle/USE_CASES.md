# Linked List Cycle — Real-World Use Cases

Floyd's cycle detection algorithm — and cycle detection in general — appears
in cryptography, OS design, compilers, and garbage collectors.

---

## 1. Pollard's Rho — Integer Factorisation (Cryptography)

Pollard's Rho algorithm factors large integers by deliberately constructing
a sequence of pseudorandom values that eventually cycles, then using Floyd's
algorithm to detect the cycle. The cycle period reveals a non-trivial factor
of n — used in RSA key analysis and cryptographic attacks.

- **Paper:** Pollard, J.M. — *A Monte Carlo method for factorization*,
  BIT Numerical Mathematics, 15:331–334, 1975.
- **Wikipedia:** https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm
- **Relation to Floyd's:** Pollard's Rho uses Floyd's cycle detection as
  its core subroutine — without it, the algorithm cannot identify when the
  pseudorandom sequence has looped.

---

## 2. Pseudorandom Number Generator Period Detection

Linear Congruential Generators (LCGs) and other PRNGs produce sequences
that eventually cycle. The cycle length (period) determines the quality of
the PRNG — a short period means predictable output, which is a security
vulnerability. Floyd's algorithm detects the period in O(1) space without
storing the full sequence.

- **Reference:** Knuth, D.E. — *The Art of Computer Programming, Vol. 2:
  Seminumerical Algorithms*, Section 3.1 (Linear Congruential Method) and
  Section 3.5 (What Is a Random Sequence?), Addison-Wesley.
- **Wikipedia — LCG:** https://en.wikipedia.org/wiki/Linear_congruential_generator

---

## 3. OS Deadlock Detection

A deadlock in an OS is a cycle in the resource allocation graph — process A
holds resource R1 and waits for R2, process B holds R2 and waits for R1.
Cycle detection in this graph identifies the deadlock. Floyd's is used
in lightweight single-path detectors; DFS-based detection handles the
general multi-process case.

- **Reference:** Coffman, Elphick & Shoshani — *System Deadlocks*,
  ACM Computing Surveys, 3(2):67–78, 1971.
  https://dl.acm.org/doi/10.1145/356586.356588
- **OS textbook coverage:** Silberschatz, Galvin & Gagne —
  *Operating System Concepts*, Chapter 8 (Deadlocks).

---

## 4. Compiler Static Analysis — Infinite Loop Detection

Compilers traverse the Control Flow Graph (CFG) of a program to detect
cycles with no exit condition (infinite loops). Static analysis tools like
FindBugs, SonarQube, and LLVM's analyzer use DFS-based cycle detection
on the CFG as a core pass.

- **LLVM cycle detection source:**
  https://github.com/llvm/llvm-project/blob/main/llvm/include/llvm/ADT/CycleInfo.h
- **Paper:** Cytron et al. — *Efficiently computing static single assignment
  form and the control dependence graph*, ACM TOPLAS, 1991.
  https://dl.acm.org/doi/10.1145/115372.115320

---

## 5. Garbage Collection — Reference Cycle Detection

Object reference graphs in garbage-collected runtimes (JVM, .NET CLR,
CPython) can contain reference cycles that prevent simple reference-counting
collectors from freeing memory. Cycle detection is a core component of
tracing GCs (mark-and-sweep, tri-color marking).

- **CPython cycle GC:** CPython uses a dedicated cycle detector module
  (`Modules/gcmodule.c`) because reference counting alone cannot handle
  cycles. https://github.com/python/cpython/blob/main/Modules/gcmodule.c
- **Paper:** Wilson, P.R. — *Uniprocessor Garbage Collection Techniques*,
  IWMM 1992. https://dl.acm.org/doi/10.5555/645648.664824

---

## Summary

| Domain | Cycle in what structure | Algorithm used |
|--------|------------------------|----------------|
| Cryptography (Pollard's Rho) | Pseudorandom value sequence | Floyd's (tortoise & hare) |
| PRNG period detection | LCG output sequence | Floyd's |
| OS deadlock detection | Resource allocation graph | DFS / Floyd's |
| Compiler static analysis | Control flow graph (CFG) | DFS with inStack |
| Garbage collection | Object reference graph | Tri-color mark & sweep |

---

## Further Reading

- Pollard's Rho: https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm
- OS deadlock (Coffman 1971): https://dl.acm.org/doi/10.1145/356586.356588
- CPython GC source: https://github.com/python/cpython/blob/main/Modules/gcmodule.c
- LLVM cycle info: https://github.com/llvm/llvm-project/blob/main/llvm/include/llvm/ADT/CycleInfo.h
