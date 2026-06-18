# All Possible Full Binary Trees — Real-World Use Cases

Enumerating full binary trees is equivalent to enumerating Catalan-counted
structures — connected to NLP parsing, matrix chain multiplication,
and combinatorial optimisation.

---

## 1. Catalan Numbers — The Mathematical Foundation

The number of structurally distinct full binary trees with n nodes is the
((n-1)/2)-th Catalan number. The Catalan numbers appear in over 200
combinatorial contexts — all bijectively equivalent to full binary tree
enumeration.

```
C(k) = 1/(k+1) × C(2k, k)

k: 0  1  2  3   4   5    6
C: 1  1  2  5  14  42  132
```

Full binary trees with n nodes ↔ Catalan number C((n-1)/2).

### The 66 Equivalences (Stanley 1999)

Richard Stanley's *Enumerative Combinatorics: Volume 2* lists 66 combinatorial
structures all counted by the same Catalan numbers:

| Structure | Catalan interpretation |
|-----------|----------------------|
| Full binary trees with n+1 leaves | C(n) structures |
| Valid bracket sequences `()()...` of length 2n | C(n) sequences |
| Triangulations of convex (n+2)-gon | C(n) triangulations |
| Binary parse trees for n+1 tokens | C(n) parse trees |
| Monotonic n×n lattice paths | C(n) paths |
| Stack-sortable permutations | C(n) permutations |
| Non-crossing partitions of n elements | C(n) partitions |

### References

- **Wikipedia — Catalan number:**
  https://en.wikipedia.org/wiki/Catalan_number
  "Successive applications of a binary operator can be represented in terms
  of a full binary tree. Cₙ is the number of full binary trees with n+1 leaves,
  or equivalently, with a total of n internal nodes. Cₙ is the number of
  possible parse trees for a sentence in natural language processing."

- **CP-Algorithms — Catalan Numbers:**
  https://cp-algorithms.com/combinatorics/catalan-numbers.html
  "A rooted binary tree is full if every vertex has either two children or no
  children. The number of rooted full binary trees with n+1 leaves is C(n).
  Catalan numbers also count triangulations of convex polygons, stack-sorted
  permutations, and monotonic lattice paths."

- **Stanley, R.P. — *Enumerative Combinatorics: Volume 2*, Cambridge University
  Press, 1999.** The definitive reference — Exercise 6.19 lists 66 Catalan
  families with bijective proofs.

- **arXiv:1903.00813 — Enumeration and Asymptotic Formulas:**
  https://arxiv.org/pdf/1903.00813
  "C(n) counts the plane rooted binary trees with n-1 internal nodes and n
  leaves, where every internal node has two children — the C(4)=5 rooted
  full binary trees with 4 leaves and 3 internal nodes."

- **arXiv:2503.02663 — Equivalence Classes of Binary Tree Isomorphism:**
  https://arxiv.org/pdf/2503.02663
  "The enumeration of non-isomorphic rooted binary trees is expressed via
  Catalan Numbers using generating functions."

---

## 2. NLP — Binary Parse Trees (CYK Algorithm)

In natural language processing, every sentence can be parsed into a
binary branching tree (binary phrase structure grammar). The number of
distinct parse trees for a sentence of n+1 words is exactly C(n) — the
Catalan number.

The **CYK (Cocke-Younger-Kasami) algorithm** is the NLP equivalent of
this LeetCode problem: it enumerates all valid binary parse trees for a
sentence under a context-free grammar, using the same recursive split-and-combine structure.

```
Sentence: "John saw Mary with binoculars" (5 words)

Binary parse trees:
  [John [saw [Mary [with binoculars]]]]
  [John [saw [[Mary with] binoculars]]]
  [John [[saw Mary] [with binoculars]]]
  [[John saw] [Mary [with binoculars]]]
  [[John saw] [[Mary with] binoculars]]

Count = C(4) = 14 possible binary trees for 5 words ✓
```

The structural recursion is identical:
- Split the sentence at position k (left=k words, right=n-k words)
- Recursively enumerate parse trees for each half
- Combine under a new parent node

### References

- **Wikipedia — Catalan number (NLP connection):**
  https://en.wikipedia.org/wiki/Catalan_number
  "Cₙ is the number of possible parse trees for a sentence (assuming binary
  branching) in natural language processing."

- **CYK algorithm:** https://en.wikipedia.org/wiki/CYK_algorithm
  "CYK parses sentences by filling a parse table bottom-up, enumerating
  all valid binary derivation trees — the count is bounded by the Catalan
  number of the sentence length."

---

## 3. Matrix Chain Multiplication — Parenthesisation Enumeration

The matrix chain multiplication problem (LeetCode #1039, classic DP) asks:
given n matrices, what is the optimal order of multiplications?

The number of distinct parenthesisations of n matrices = C(n-1) — a Catalan
number. **Enumerating all full binary trees with n leaves enumerates all
parenthesisations** — every internal node is a multiplication, every leaf
is a matrix.

```
Matrices: A × B × C × D  (4 matrices, C(3)=5 parenthesisations)

((AB)(CD))      ↔  internal nodes: [*,*,*]
(A(B(CD)))      ↔  right-skewed tree
((A(BC))D)      ↔  mixed
(A((BC)D))      ↔  mixed
(((AB)C)D)      ↔  left-skewed tree
```

This problem generates all 5 trees. The DP solution (LeetCode #1039) finds
the minimum-cost one in O(n³) without enumerating all C(n-1) options.

### References

- **arXiv:1903.00813:**
  https://arxiv.org/pdf/1903.00813
  "C(n) counts the ways to parenthesise a sequence with n-1 operations and
  n factors. The C(4)=5 ways to fully parenthesise a product with 4 factors
  using 3 operations."

- **Wikipedia — Matrix chain multiplication:**
  https://en.wikipedia.org/wiki/Matrix_chain_multiplication
  "The number of ways to fully parenthesise a sequence of n matrices is the
  (n-1)-th Catalan number C(n-1)."

---

## The Unified View

All three use cases share the same recursive structure:

```
Split n items at position k:
  Left  = k items  → recurse
  Right = n-k items → recurse
  Combine left × right under a new root

This generates C(n) distinct structures.
```

| Domain | "Items" | "Split" | Root = |
|--------|---------|---------|--------|
| Full binary trees | n nodes | L nodes left, R nodes right | TreeNode |
| NLP parse trees | n+1 words | k words left, n-k+1 words right | Grammar rule |
| Matrix multiplication | n matrices | k matrices left, n-k matrices right | Multiplication node |

---

## Further Reading

- Catalan numbers (Wikipedia): https://en.wikipedia.org/wiki/Catalan_number
- CP-Algorithms Catalan: https://cp-algorithms.com/combinatorics/catalan-numbers.html
- Stanley Vol. 2 (66 interpretations): search "Stanley Enumerative Combinatorics Volume 2"
- CYK algorithm: https://en.wikipedia.org/wiki/CYK_algorithm
- Matrix chain multiplication: https://en.wikipedia.org/wiki/Matrix_chain_multiplication
- arXiv binary tree isomorphism: https://arxiv.org/pdf/2503.02663
