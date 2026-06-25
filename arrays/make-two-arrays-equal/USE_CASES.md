# Make Two Arrays Equal by Reversing Subarrays — Real-World Use Cases

The core algorithm — multiset equality via frequency comparison — is the
foundation of anagram detection used throughout NLP, search engines, and
bioinformatics. The deeper theory connects to genome rearrangement.

---

## 1. Anagram Detection — NLP, Spell Checking, Search Engines

Anagram detection is precisely the multiset equality problem: two strings
are anagrams iff they have the same character frequency map. This is the
same algorithm as this problem applied to character arrays.

### Production applications

- **Spell checkers:** Detecting transposition errors (`"teh"` vs `"the"`)
  uses anagram detection — the misspelled word is an anagram of the correct one
- **Word games (Scrabble):** Validating whether a set of tiles can spell a word
  = checking if the tile multiset contains the word's character multiset
- **Search engine synonym expansion:** Near-duplicate query detection uses
  character/word-level anagram hashing (SimHash, MinHash)
- **Cryptanalysis:** Frequency analysis of substitution ciphers — the cipher
  text is an "anagram" of the plaintext over a different alphabet

### The algorithm used in production

```
// Sorting approach (O(n log n)) — used when string length is short
sorted(s1) == sorted(s2)

// Frequency map approach (O(n)) — used for longer strings
Counter(s1) == Counter(s2)
```

Both reduce to the same problem as `canBeEqual()`.

### References

- **CodeLucky — Anagram Detection: Efficient String Comparison Methods:**
  https://codelucky.com/anagram-detection/
  "Anagram detection is important in text analysis, natural language processing,
  and dictionary-based algorithms. Counting character frequencies is the most
  optimised approach — if both strings have exactly the same frequency counts
  for each character, they are anagrams. Time complexity O(n), Space O(1) for
  fixed character sets."

- **Baeldung — Check if Two Strings Are Anagrams in Java:**
  https://www.baeldung.com/java-strings-anagrams
  "MultiSet is a collection that supports order-independent equality with
  duplicate elements. For example, the multisets {a,a,b} and {a,b,a} are equal.
  Anagram detection reduces to multiset equality — exactly the operation in
  this problem."

---

## 2. Genome Rearrangement — Sorting Permutations by Reversals

The deeper theory behind this problem is the **sorting by reversals** problem
in computational biology — inferring evolutionary relationships between two
species by comparing their gene orderings.

### The biological model

Two species have the same set of genes but in different orders (a permutation).
During evolution, chromosomal inversions (reversals of gene segments) rearrange
the gene order. The **reversal distance** between two species is the minimum
number of reversals needed to transform one gene ordering into the other.

```
Species A gene order: [1, 2, 3, 4, 5]
Species B gene order: [1, 3, 2, 4, 5]
Reversal distance = 1  (reverse segment [2,3])
```

### The connection to this problem

LeetCode #1460 asks: can `arr` be made equal to `target` with **unlimited**
reversals? Answer: yes iff they are permutations (multiset equality).

The **hard** version: what is the MINIMUM number of reversals?
- Signed permutations: solvable in O(n²) (Hannenhalli-Pevzner algorithm)
- Unsigned permutations: NP-hard (proven by Caprara 1997)

LeetCode #1460 sits at the trivial end of this spectrum — unlimited reversals
collapses the hard problem into a simple multiset check.

### Reference

- **Paper:** *Sorting Permutations by Reversals and Eulerian Cycle Decompositions*,
  SIAM Journal on Discrete Mathematics.
  https://epubs.siam.org/doi/10.1137/S089548019731994X
  "We analyze the strong relationship among the problem of sorting a permutation
  by the minimum number of reversals (MIN-SBR) and the problem of finding
  edge-disjoint alternating cycles in a breakpoint graph. MIN-SBR is shown to
  be NP-hard."

- **Paper:** *Estimating Genome Reversal Distance by Genetic Algorithm*,
  arXiv:cs/0405014.
  https://arxiv.org/pdf/cs/0405014
  "Sorting by reversals is an important problem in inferring the evolutionary
  relationship between two genomes. The problem of sorting unsigned permutations
  has been proven to be NP-hard. The problem of sorting signed permutations
  can be solved in O(n²) time."

---

## The Complexity Spectrum of Reversal Problems

All share the same operation (subarray reversal) but differ in what is counted:

| Problem | Constraint | Complexity | Algorithm |
|---------|-----------|------------|-----------|
| LeetCode #1460 (this) | Unlimited reversals | O(n) or O(n log n) | Multiset equality |
| Sort by adjacent swaps | Count swaps | O(n log n) | Merge sort (inversion count) |
| Sort signed permutation by reversals | Min reversals | O(n²) | Hannenhalli-Pevzner |
| Sort unsigned permutation by reversals | Min reversals | NP-hard | 3/2-approximation |

---

## Further Reading

- Anagram detection guide: https://codelucky.com/anagram-detection/
- Java anagram (Baeldung): https://www.baeldung.com/java-strings-anagrams
- Genome reversal SIAM paper: https://epubs.siam.org/doi/10.1137/S089548019731994X
- Unsigned reversal NP-hard (arXiv): https://arxiv.org/pdf/cs/0405014
- Hannenhalli-Pevzner algorithm (signed reversals): Wikipedia Reversal distance
