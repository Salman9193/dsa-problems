# Shortest Common Supersequence — Research & Foundations

SCS reduces to the **longest common subsequence**, which sits in the classic family of
two-sequence dynamic-programming problems (LCS, edit distance, sequence alignment). The
foundational papers below define that grid and its uses. *Citations verified against
JACM / Journal of Molecular Biology / DOI records — not from memory.*

- **R. A. Wagner & M. J. Fischer (1974), "The String-to-String Correction Problem,"**
  *Journal of the ACM* 21(1):168–173. DOI: 10.1145/321796.321811. The paper that
  formalises the O(m·n) DP over two strings for edit distance — and its abstract
  explicitly lists *"determining the longest subsequence of characters common to two
  strings"* as an application, which is exactly the LCS that SCS is built on.
  https://dl.acm.org/doi/10.1145/321796.321811

- **S. B. Needleman & C. D. Wunsch (1970), "A general method applicable to the search
  for similarities in the amino acid sequence of two proteins,"** *Journal of Molecular
  Biology* 48(3):443–453. DOI: 10.1016/0022-2836(70)90057-4. The origin of global
  **sequence alignment** — the same match-diagonally / gap-along-an-axis DP grid used
  here, generalised with a biological scoring scheme.
  https://doi.org/10.1016/0022-2836(70)90057-4

- **E. W. Myers (1986), "An O(ND) Difference Algorithm and Its Variations,"**
  *Algorithmica* 1(2):251–266. DOI: 10.1007/BF01840446. The efficient LCS-based diff
  behind `git diff` — computing the edit script between two sequences, a direct
  application of the LCS machinery SCS uses.
  https://doi.org/10.1007/BF01840446

- **D. S. Hirschberg (1975), "A linear space algorithm for computing maximal common
  subsequences,"** *Communications of the ACM* 18(6):341–343. DOI: 10.1145/360825.360861.
  Computes LCS in O(m·n) time but **O(min(m,n)) space** via divide-and-conquer — the
  answer to "can I avoid the full table?" that this problem's reconstruction otherwise
  requires.
  https://doi.org/10.1145/360825.360861

**Why they matter here:** SCS length is `m + n − |LCS|`, so every result about computing
and reconstructing the LCS (Wagner–Fischer's DP, Hirschberg's linear space) and every
application of the same grid (alignment, diff) is directly relevant.
