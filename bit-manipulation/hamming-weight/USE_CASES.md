# Hamming Weight — Real-World Use Cases

Counting set bits (popcount) appears in more domains than you'd expect.
Below are concrete examples with links to the original papers.

---

## 1. Similarity Search at Scale (Machine Learning)

### The problem
Finding the nearest neighbours in a dataset of 100 million+ vectors (images, audio,
text embeddings) by Euclidean distance requires thousands of floating-point ops per
comparison — too slow at scale.

### The trick
Compress each vector into a compact **binary code** (e.g. 128 bits) using
Locality-Sensitive Hashing (LSH). Similar vectors get similar codes.
Similarity then becomes:

```
distance = hammingWeight(codeA ^ codeB)
```

XOR flags differing bits; popcount counts them. On modern CPUs this is a **single
`POPCNT` instruction** — orders of magnitude faster than floating-point distance.

### FAISS (Meta)
Meta built FAISS specifically for billion-scale similarity search using this idea
(extended via Product Quantization).

- **Engineering blog:** https://engineering.fb.com/2017/03/29/data-infrastructure/faiss-a-library-for-efficient-similarity-search/
- **Original paper:** Johnson, Douze & Jégou — *Billion-scale similarity search with GPUs*, arXiv:1702.08734 (2017)
- **GitHub:** https://github.com/facebookresearch/faiss

---

## 2. Audio Fingerprinting (Shazam)

Shazam converts audio into a series of **binary hash fingerprints** derived from
spectrogram peaks (landmarks). Song identification is a Hamming lookup:

```
match = argmin over database { hammingWeight(queryHash ^ storedHash) }
```

This lets Shazam identify a song from a short, noisy recording against a database
of 40 million+ songs in milliseconds.

- **Original Shazam paper:** Wang, A. — *An Industrial-Strength Audio Search Algorithm*, ISMIR 2003
- **Patent (landmark hashing):** EP1307833 (assigned to Shazam)
- **Academic follow-up:** *Audio Fingerprint based on Power Spectral Density and Hamming Distance Measure*, JARDCS 2020
  https://www.researchgate.net/publication/340564958

---

## 3. Near-Duplicate Web Page Detection (Google / SimHash)

Google's web crawler uses **SimHash** (created by Moses Charikar) to detect
near-duplicate pages across a multi-billion page index.

Each page is hashed into a 64-bit fingerprint such that similar pages produce
fingerprints differing in only a few bits. Deduplication becomes:

```
isDuplicate = hammingWeight(fpA ^ fpB) < threshold
```

- **Google research paper:** Manku, Jaiswal, Sarma — *Detecting Near-Duplicates for Web Crawling*, WWW 2007
  https://research.google.com/pubs/archive/33026.pdf
- **Original SimHash paper:** Charikar — *Similarity Estimation Techniques from Rounding Algorithms*, ACM STOC 2002
- **Google patent:** US8548972B1 — Near-duplicate document detection for web crawling

---

## 4. Chess Engines (Bitboards)

Chess engines represent the board as a set of 64-bit integers (**bitboards**),
one per piece type. Hamming weight gives instant piece counts:

```java
int whitePawns = Long.bitCount(whitePawnBitboard);
```

Fast popcount is critical in engines like Stockfish that evaluate millions of
positions per second.

---

## 5. Error Detection & Correction

**Hamming distance** between two binary strings = hamming weight of their XOR.
The minimum Hamming distance of an error-correcting code determines how many bit
errors it can detect and correct. Used in RAID, QR codes, and satellite comms.

---

## 6. Cryptography & Side-Channel Attacks

Power consumption of a chip is proportional to the number of 1 bits being
processed (**Hamming weight model**). Attackers can infer secret keys by measuring
power draw — this is the basis of **Simple Power Analysis (SPA)** attacks.

---

## 7. Advanced Hardware Bit Manipulation — PDEP/PEXT Instructions

Popcount is just one of a family of advanced bit manipulation operations now
supported directly in hardware. Two closely related instructions — **PDEP**
(parallel bit deposit / bit scatter) and **PEXT** (parallel bit extract /
bit gather) — extend the popcount idea to arbitrary bit permutations and
field operations.

### What PDEP/PEXT do

```
PEXT (parallel extract / bit gather):
  Given a source register and a mask register,
  extract the bits of source at positions where mask=1
  and pack them into contiguous low-order bits.

PDEP (parallel deposit / bit scatter):
  The inverse — take contiguous low-order bits
  and scatter them into positions where mask=1.
```

### Applications (from hardware/compiler research)

| Domain | How PDEP/PEXT is used |
|--------|-----------------------|
| **Bitboards (chess, card games)** | Bit permutation, mirror/reverse bit arrays, dealing card hands from a single register |
| **Morton codes (Z-order curves)** | Bit interleaving/de-interleaving for spatial indexing — PDEP/PEXT replaces multi-step multiply tricks |
| **Succinct data structures** | Rank/select operations — popcount + PDEP/PEXT power Roaring Bitmaps and HOT (Height Optimized Trie) |
| **CSV/high-performance parsers** | Extracting parsed bits/markers into final structures — same technique as SIMD parsers |
| **RISC-V / x86 instruction encoding** | Assembling/disassembling immediates scattered across instruction words (RISC-V scatter design, x86 REX2 prefix) |
| **Genome parsing** | Extracting nucleotide fields from packed bit representations |
| **Cryptography** | Arbitrary bit permutations in block cipher S-boxes and P-boxes |
| **Constraint solvers / SAT / BDD** | Binary Decision Diagrams and 4-colouring problems benefit significantly from PDEP/PEXT |

### Key references

- **Paper:** *Performing Advanced Bit Manipulations Efficiently in General-Purpose Processors*
  The foundational hardware paper proposing PDEP/PEXT — covering perm (bit permutation),
  pex (parallel extract / bit gather), and pdep (parallel deposit / bit scatter).

- **Hacker's Delight** (Warren, 2nd ed.) — Chapter on bit permutations and the
  multiply trick for bit interleaving (superseded by PDEP/PEXT on modern CPUs).

- **Succinct data structures paper:**
  *A General-Purpose Counting Filter: Making Every Bit Count*
  https://dl.acm.org/doi/10.1145/3035918.3035963

- **Bit manipulation for succinct structures and CSV parsing:**
  *Bit-manipulation operations for high-performance succinct data-structures and CSV parsing*

- **Morton code / spatial indexing:**
  https://stackoverflow.com/questions/4909263/how-to-de-interleave-bits-unmortonizing

- **Zig language proposals tracking PDEP/PEXT support:**
  https://github.com/ziglang/zig/issues/14995
  https://github.com/ziglang/zig/issues/15837

- **Hacker News discussion — PDEP/PEXT implications:**
  https://news.ycombinator.com/item?id=19137260

### The connection to popcount

POPCNT (popcount) was the first "advanced" bit instruction added to x86 (SSE4.2, 2008).
PDEP/PEXT followed in Intel Haswell (2013) as part of the BMI2 extension.
All three — POPCNT, PDEP, PEXT — are part of the same hardware story: tasks that
previously required dozens of shift/mask/OR operations now execute in a single cycle.

---

## Summary

| Domain | What gets counted/manipulated | Why popcount/PDEP/PEXT |
|--------|-------------------------------|------------------------|
| ML / ANN search | Image/text binary codes | Fast Hamming distance via popcount |
| Audio fingerprinting | Spectrogram landmark hashes | Sub-ms song lookup |
| Web crawling | Page content fingerprints | Deduplicate billion-page index |
| Chess engines | Board state bitboards | Count pieces in O(1) |
| Error correction | Codewords | Measure bit-flip distance |
| Cryptography | Key bits / S-box permutations | Side-channel model + PDEP/PEXT permutations |
| Succinct structures | Rank/select indices | POPCNT + PDEP/PEXT for Roaring Bitmaps |
| Parsers (CSV, genome) | Packed bit fields | PEXT extracts fields in one instruction |
| Spatial indexing | Morton / Z-order codes | PDEP/PEXT replaces multiply interleave trick |
