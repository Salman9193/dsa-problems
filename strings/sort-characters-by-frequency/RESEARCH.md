# Sort Characters By Frequency — Research & Foundations

Ranking symbols by frequency is the first step of optimal prefix coding, and the
linear-time method rests on distribution/counting sort. The foundational references
below. *Citations verified against Proceedings of the IRE / Bell System Technical
Journal / DOI records — not from memory.*

- **D. A. Huffman (1952), "A Method for the Construction of Minimum-Redundancy Codes,"**
  *Proceedings of the IRE* 40(9):1098–1101. DOI: 10.1109/JRPROC.1952.273898. The classic
  algorithm that assigns the **shortest codes to the most frequent symbols** — a
  frequency ranking is its first step. The basis of DEFLATE (ZIP, gzip, PNG) and much of
  data compression.
  https://doi.org/10.1109/JRPROC.1952.273898

- **C. E. Shannon (1948), "A Mathematical Theory of Communication,"** *Bell System
  Technical Journal* 27(3):379–423. DOI: 10.1002/j.1538-7305.1948.tb01338.x. Establishes
  **entropy** as the limit of lossless coding — the theoretical reason frequent symbols
  *should* get shorter codes, which frequency ordering operationalises.
  https://doi.org/10.1002/j.1538-7305.1948.tb01338.x

- **D. E. Knuth, *The Art of Computer Programming*, Vol. 3 (Sorting and Searching),
  §5.2 — distribution/counting sort.** The authoritative treatment of sorting by a
  bounded-range key without comparisons, which is why this problem's bucket sort is
  O(n): a frequency can't exceed `n`, so you index by it rather than compare.

**Why they matter here:** the problem is "order characters by frequency," which is the
input to Huffman coding (Huffman 1952), justified by Shannon's entropy (1948), and made
linear-time by the counting/bucket-sort technique (Knuth Vol. 3).
