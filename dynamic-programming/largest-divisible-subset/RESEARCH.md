# Largest Divisible Subset — Research & Foundations

Divisibility is a **partial order**, so the largest divisible subset is the **longest
chain in the divisibility poset** — which sits on two classical foundations: the
chain/antichain theory of partial orders, and the longest-increasing-subsequence
algorithm it reduces to. *Citations verified against Annals of Mathematics / American
Mathematical Monthly / Canad. J. Math records this session — not from memory.*

- **R. P. Dilworth (1950), "A Decomposition Theorem for Partially Ordered Sets,"**
  *Annals of Mathematics* (2nd series) 51(1):161–166. DOI: 10.2307/1969503. The
  foundational theorem of order theory: in a finite poset, the largest **antichain**
  equals the minimum number of **chains** covering the set. Establishes the chain/antichain
  duality that frames this problem. https://doi.org/10.2307/1969503

- **L. Mirsky (1971), "A dual of Dilworth's decomposition theorem,"** *American
  Mathematical Monthly* 78(8):876–877. DOI: 10.2307/2316481. The dual result: the length
  of the **longest chain** (the poset's *height*) equals the minimum number of antichains
  partitioning it. The longest chain is exactly what this problem computes.
  https://doi.org/10.2307/2316481

- **C. Schensted (1961), "Longest increasing and decreasing subsequences,"** *Canadian
  Journal of Mathematics* 13:179–191. DOI: 10.4153/CJM-1961-015-3. The combinatorial
  foundation of the LIS algorithm this problem reduces to after sorting (divisibility
  replacing the `<` relation). https://doi.org/10.4153/CJM-1961-015-3

**Why it matters here:** sorting turns "every pair divides" into "form the longest chain
under divisibility." That is the poset *height* studied by Dilworth (1950) and Mirsky
(1971), and it is computed by the longest-increasing-subsequence method (Schensted, 1961)
with `%` in place of `<`.

**Related in this repo:** the same LIS machinery underlies `longest-increasing-subsequence`
(patience sorting); this problem is its partial-order generalisation.
