# Shortest Common Supersequence — Real-World Use Cases

SCS is one node in a small, powerful family of two-sequence DP problems — **LCS, edit
distance, and sequence alignment all live on the same grid**. Wherever software needs
to compare, merge, or align two sequences, this machinery is running underneath.

---

## 1. Version Control — `diff` and Merge (git, Mercurial, …)

A `diff` between two file versions is computed as their **Longest Common Subsequence**:
the unchanged lines are the LCS, and everything not in it is an insertion or deletion.
Producing a single file that preserves *both* sides' content — a merge — is a
supersequence-style construction over those line sequences.

- git's default diff uses **Myers' algorithm**, an efficient LCS-based diff:
  Eugene W. Myers, *An O(ND) Difference Algorithm and Its Variations*, Algorithmica
  1(1):251–266, 1986 — the paper behind `git diff`.
- **Diff (Wikipedia):** https://en.wikipedia.org/wiki/Diff
  "The core of many diff implementations is solving the longest common subsequence
  problem."
- **Longest common subsequence:** https://en.wikipedia.org/wiki/Longest_common_subsequence

---

## 2. Bioinformatics — Sequence Alignment

Aligning two DNA, RNA, or protein sequences to reveal shared structure and evolutionary
relationships uses the same DP grid, generalised with a scoring scheme. Global alignment
(**Needleman–Wunsch**) fills a matrix over the two sequences exactly like the LCS table:
matches run diagonally, gaps step along an axis — the same moves as the SCS backtrack.
Merging reads into a consensus or superstring relates directly to the
common-supersequence idea.

- **Needleman–Wunsch algorithm:** https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm
- **Sequence alignment:** https://en.wikipedia.org/wiki/Sequence_alignment

---

## 3. Delta Encoding & Data Synchronisation

Storing or transmitting the *difference* between two versions of data (delta/patch
encoding — used by version control, backup systems, and sync tools) rests on finding
the common subsequence and encoding only what was added or removed. The combined,
merged representation is a supersequence of both versions.

- **Edit distance (Levenshtein):** https://en.wikipedia.org/wiki/Edit_distance
  (the closely-related metric — LCS and edit distance are computed on the same grid.)

---

## The Unified Pattern

```
Grid over two sequences A (rows) and B (columns):
    match on the diagonal  →  a shared element
    step along an axis     →  an element unique to one sequence

LCS            : maximise diagonal matches
Edit distance  : minimise axis steps (insert / delete / substitute)
SCS            : emit shared elements once + all unique elements  (len = |A| + |B| − |LCS|)
Alignment      : the same grid with a biological scoring function
```

| Domain | The two sequences | What the grid computes |
|--------|-------------------|------------------------|
| git diff / merge | Two file versions (lines) | LCS = unchanged lines; the rest = the patch |
| Bioinformatics | Two DNA / protein sequences | Optimal alignment / shared structure |
| Delta encoding | Two data versions | Minimal edit script to transform or merge |

---

## Further Reading

- Longest common subsequence: https://en.wikipedia.org/wiki/Longest_common_subsequence
- Diff (LCS-based): https://en.wikipedia.org/wiki/Diff
- Myers diff paper (git's algorithm): search "Myers An O(ND) Difference Algorithm 1986"
- Needleman–Wunsch alignment: https://en.wikipedia.org/wiki/Needleman%E2%80%93Wunsch_algorithm
- Edit distance: https://en.wikipedia.org/wiki/Edit_distance
