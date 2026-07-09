# Reverse Bits — Research & Foundations

Bit reversal's importance comes from the **bit-reversal permutation**, the reordering step
at the heart of the Fast Fourier Transform, and its constant-time implementation is a
classic of bit-manipulation literature. *Citations verified against Mathematics of
Computation / CACM records this session — not from memory.*

- **J. W. Cooley & J. W. Tukey (1965), "An algorithm for the machine calculation of
  complex Fourier series,"** *Mathematics of Computation* 19(90):297–301. DOI:
  10.1090/S0025-5718-1965-0178586-1. The **Cooley–Tukey FFT** — O(N log N) DFT — whose
  in-place radix-2 form reorders data by the **bit-reversal permutation**, i.e. this
  problem applied to every array index.
  https://doi.org/10.1090/S0025-5718-1965-0178586-1

- **H. S. Warren Jr., *Hacker's Delight*, 2nd ed., Addison-Wesley, 2012.** The standard
  reference for the **divide-and-conquer bit reversal** (swap adjacent groups of size
  16/8/4/2/1 with masks) and population-count / bit-twiddling techniques in general.

- **P. Wegner (1960), "A technique for counting ones in a binary computer,"**
  *Communications of the ACM* 3(5):322. DOI: 10.1145/367236.367286. Same lineage of
  mask-and-shift bit algorithms that the constant-time reversal belongs to (see Hamming
  Weight, #191). https://doi.org/10.1145/367236.367286

**Why it matters here:** the loop solves the problem, but the *reason it's worth knowing*
is the bit-reversal permutation — reversing every index's bits is exactly the data
reordering the Cooley–Tukey FFT (1965) requires, and the O(1) divide-and-conquer reversal
(Warren) is how it's done in practice and in hardware.
