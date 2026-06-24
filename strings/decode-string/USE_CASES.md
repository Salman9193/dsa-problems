# Decode String — Real-World Use Cases

The stack-based `k[string]` decoding pattern is the foundation of two of
the most important compression algorithms in computing: Run-Length Encoding
(used in image formats) and LZW (used in GIF, TIFF, PDF, and ZIP).

---

## 1. Run-Length Encoding (RLE) — Direct Ancestor

Run-Length Encoding is the simplest lossless compression algorithm.
A sequence of identical values is represented as `(count, value)` pairs.
Decoding RLE is exactly the flat (non-nested) case of this problem.

```
RLE encoded:   3A 2B 4C
RLE decoded:   AAABBCCCC

k[string] form:   3[A]2[B]4[C]
k[string] decoded: AAABBCCCC
```

The `k[string]` encoding in this problem extends flat RLE to **nested RLE**,
where repeated sequences can themselves contain repeated sub-sequences.
This is also called "hierarchical RLE" — used in document compression and
structured data encoding.

### Where RLE is used in production

- **TIFF images** — row-by-row RLE compression (PackBits algorithm)
- **BMP files** — optional RLE-4 and RLE-8 compression modes
- **PDF streams** — RunLengthDecode filter
- **Fax transmission (CCITT Group 3/4)** — RLE of black/white pixel runs
- **PCX images** — RLE compression throughout

### How RLE decoding works (flat case)

```
loadCount(); // read k
outputValue(count times); // repeat value k times
advance(); // move to next (count, value) pair
```

This is exactly `if digit: k = k*10 + digit` followed by `if ']': repeat inner`.
The nested extension adds a stack to handle structured repetition.

### References

- **arXiv:2101.05329 — Improving Run Length Encoding by Preprocessing:**
  https://arxiv.org/abs/2101.05329
  "The Run Length Encoding compression method is a long standing simple
  lossless compression scheme which is easy to implement and achieves a
  good compression on input data which contains repeating consecutive symbols."

- **USPTO Patent 4551706 — Apparatus for decoding run-length encoded data:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/4551706
  "In a run-length encoding scheme, each run is represented by a run-length
  code indicating the number of signals in the run. The conventional way of
  decoding is to load the run-length code into a counter, and then count down
  to zero, outputting a signal of the required value at each count — exactly
  the `repeat inner` step in the decode string algorithm."

- **USPTO Patent 4355306 — Dynamic stack data compression and decompression:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/4355306
  Describes a hardware encoder/decoder that uses a **run-length stack**
  tracking counts and their associated string values — the same two-stack
  structure as this problem's iterative solution.

---

## 2. LZW Decompression — Stack-Based Nested Symbol Expansion

LZW (Lempel-Ziv-Welch) is the compression algorithm used in GIF images,
TIFF files, PDF streams, and Unix compress. It works by building a dictionary
of repeated substrings during compression and referencing them by code during
decompression. Crucially, symbols can be **nested** — a symbol can expand to
a string that contains references to other symbols, requiring a LIFO stack
for resolution.

### The structural parallel

```
Decode String:
  "3[a2[c]]"
  stack holds: (k=3, prefix="") when entering outer bracket
               (k=2, prefix="a") when entering inner bracket

LZW Decompressor:
  code 108h expands to "LZ" + code 105h
  code 105h expands to "LZW"
  stack holds partial expansions until all nested codes resolved
```

The two stacks in `decodeString` (countStack, stringStack) directly model
the LIFO r-stack in LZW hardware decompressors — each entry represents
a pending repetition context that must be resumed after the inner content
is resolved.

### Where LZW is used in production

- **GIF images** — all GIF files use LZW compression (color table + pixel data)
- **TIFF files** — LZW compression option widely used for lossless TIFF
- **PDF streams** — LZWDecode filter for compressed PDF content streams
- **PostScript** — LZW compression of print job data

### References

- **USPTO Patent 9054730 — Method and system for LZW based decompression:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9054730
  "Symbols can be nested — multi-stage nested symbol decoder retrieves
  the representation of the full value from the dictionary and decreases
  the stack counter accordingly. The full value is written to the r-stack.
  This optional feature improves decoder performance for data sets where
  many multi-stage nested symbols are included."

- **USPTO Patent 7071854 — Hardware-implemented LZW data decompression:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7071854
  "As the final stage of the decompressor, the stack-processing module
  performs operations associated with buffering decoded data (strings output
  from dictionary processing) and assembles the decoded data in an order
  that matches uncompressed data — exactly what stringStack does in
  the decode string algorithm."

- **LZW Wikipedia:** https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch

---

## The Shared Stack Algorithm

Both RLE decoding and LZW decompression use the same two-element context
stack that `decodeString` uses:

```
On entering a repeated section:
  push(repeat_count)      // countStack
  push(accumulated_so_far) // stringStack

On exiting a repeated section:
  repeat_count = pop()
  prefix = pop()
  result = prefix + inner × repeat_count
```

| System | "Enter bracket" trigger | "Exit bracket" trigger | Stack holds |
|--------|------------------------|----------------------|-------------|
| Decode String (#394) | `[` character | `]` character | (k, prefix_string) |
| RLE decoder | New (count, value) pair | After count exhausted | (count, value) |
| LZW decompressor | Nested code reference | Code fully resolved | (code, partial_expansion) |
| Recursive parser | Function call | Return statement | (caller_state, return_addr) |

---

## Further Reading

- RLE compression (arXiv): https://arxiv.org/abs/2101.05329
- RLE decoding apparatus (USPTO 4551706): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/4551706
- Dynamic stack RLE (USPTO 4355306): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/4355306
- LZW nested decompression (USPTO 9054730): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9054730
- LZW hardware stack (USPTO 7071854): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7071854
- LZW (Wikipedia): https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Welch
