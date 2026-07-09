# Linked List Cycle — Research & Foundations

Detecting whether a linked list has a cycle in O(1) space — the canonical **tortoise-and-hare** application.

- **Floyd’s cycle-detection (“tortoise and hare”) algorithm.** Two pointers at different speeds meet iff there is a cycle. Attributed to Robert W. Floyd; unpublished as a standalone paper, so the standard reference is **D. E. Knuth, *TAOCP* Vol. 2 (Seminumerical Algorithms), §3.1**. Overview: https://en.wikipedia.org/wiki/Cycle_detection
- **R. P. Brent (1980), “An improved Monte Carlo factorization algorithm,”** *BIT Numerical Mathematics* 20(2):176–184. DOI: 10.1007/BF01933190. **Brent’s cycle-detection algorithm**, a faster tortoise-and-hare variant. https://doi.org/10.1007/BF01933190

**Why it matters here:** Two pointers moving at speeds 1 and 2 meet inside any cycle and never meet without one — Floyd’s cycle detection, with Brent’s (1980) as the faster variant.

*Citations verified against ACM TOMS / CACM / BIT records this session — not from memory.*
