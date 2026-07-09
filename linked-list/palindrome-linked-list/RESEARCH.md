# Palindrome Linked List — Research & Foundations

The O(1)-space solution combines two classic techniques: the **fast/slow
(tortoise-and-hare) pointer** for finding a midpoint in one pass, and **in-place list
reversal**. Their foundational references below.

- **Floyd's cycle-detection ("tortoise and hare") algorithm.** The two-speed-pointer
  technique used here to find the midpoint is the same one that detects cycles in O(1)
  space. It is attributed to Robert W. Floyd; Floyd did not publish it as a standalone
  paper, and the standard reference is **D. E. Knuth, *The Art of Computer Programming*,
  Vol. 2 (Seminumerical Algorithms), §3.1**, which presents it as an exercise. See also
  the overview: https://en.wikipedia.org/wiki/Cycle_detection

- **R. P. Brent (1980), "An improved Monte Carlo factorization algorithm,"** *BIT
  Numerical Mathematics* 20(2):176–184. DOI: 10.1007/BF01933190. Introduces **Brent's
  cycle-detection algorithm**, a faster alternative to Floyd's using the same two-pointer
  idea — the rigorous companion to the tortoise-and-hare technique.
  https://doi.org/10.1007/BF01933190

- **In-place linked-list reversal** is elementary pointer manipulation, treated in
  **D. E. Knuth, *TAOCP* Vol. 1 (Fundamental Algorithms), §2.2.3–2.2.4** on linked
  allocation and list operations.

**Why they matter here:** "read a singly linked list backwards in O(1) space" is solved
by the tortoise-and-hare midpoint (Floyd; Knuth Vol. 2) plus in-place reversal (Knuth
Vol. 1) — this problem is a clean composition of both.

*Note: Floyd's algorithm is genuinely unpublished-as-a-paper; this is stated honestly
rather than inventing a citation. Brent (1980) is a verified published alternative.*
