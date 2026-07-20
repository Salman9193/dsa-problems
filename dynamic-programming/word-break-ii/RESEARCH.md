# Word Break II — Research & Foundations

Enumerating all segmentations of a string over a dictionary is **context-free recognition**
restricted to a linear (regular) grammar — and the practical, weighted version of it is the
foundation of Chinese NLP. *Citations verified this session — not from memory.*

- **R. Bellman, *Dynamic Programming*, Princeton University Press, 1957.** The position-indexed
  recurrence (`solve(i)` depends on `solve(j)` for `j > i`) is Bellman DP over a DAG whose
  topological order is simply *string position*.

- **J. Earley (1970), "An efficient context-free parsing algorithm,"** *Communications of the ACM*
  13(2):94–102. DOI: 10.1145/362007.362035. Word Break is the degenerate case of general CF
  parsing: a **chart parser** over a one-level grammar (`S → word S | ε`). The memo table here is
  a stripped-down Earley chart. https://doi.org/10.1145/362007.362035

- **A. J. Viterbi (1967), "Error bounds for convolutional codes and an asymptotically optimum
  decoding algorithm,"** *IEEE Transactions on Information Theory* 13(2):260–269.
  DOI: 10.1109/TIT.1967.1054010. The **Viterbi algorithm** — max-probability path through a
  lattice. Production Chinese segmenters use it twice: for the max-probability word path, and for
  HMM-based unknown-word recovery. https://doi.org/10.1109/TIT.1967.1054010

- **N. Xue (2003), "Chinese Word Segmentation as Character Tagging,"** *Computational Linguistics
  and Chinese Language Processing* 8(1):29–48. Established the **B/M/E/S character-tagging**
  formulation (Begin / Middle / End / Single) that jieba's HMM fallback uses for out-of-vocabulary
  words.

- **Fredkin (1960), "Trie Memory,"** *Communications of the ACM* 3(9):490–499.
  DOI: 10.1145/367390.367400. The dictionary structure — though production segmenters often prefer
  a **flat prefix hash map** (better cache locality, trivial serialization) or a **double-array
  Trie** (compact + fast). https://doi.org/10.1145/367390.367400

**Why it matters here:** the problem is a **path enumeration in a DAG** where nodes are string
positions and edges are dictionary words. Because positions are inherently topologically ordered,
no sorting is needed. The three variants differ only in the accumulator:

| Accumulator | Result | Complexity |
|-------------|--------|-----------|
| boolean OR | *is it segmentable?* ([#139](#dynamic-programming/word-break)) | **polynomial** O(n²·L) |
| list concat | *all segmentations* (**this problem**) | **exponential output** (optimal = output-sensitive) |
| max of log-probabilities | *the most likely segmentation* (jieba) | **linear** O(n·L) |

**The lesson:** the *decision* problem is polynomial, the *enumeration* problem is exponential
**by output size**, and the *optimization* problem is linear again. Enumeration is the expensive
one — which is precisely why real segmenters compute the best path rather than all paths.

**Related in this repo:** [Word Break #139](#dynamic-programming/word-break),
[Implement Trie #208](#strings/implement-trie),
[Parallel Courses III #2050](#graphs/parallel-courses-iii) (weighted DAG longest path), and the
applied systems:
[Chinese Word Segmenter LLD](https://salman9193.github.io/system-design/#lld-chinese-word-segmenter),
[Text Segmentation Service HLD](https://salman9193.github.io/system-design/#hld-text-segmentation-service).
