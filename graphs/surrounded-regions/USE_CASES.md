# Surrounded Regions — Real-World Use Cases

The surrounded regions problem — finding enclosed background regions in a
binary grid — is one of the fundamental operations in binary image processing,
directly implementing morphological hole filling and Go territory scoring.

---

## 1. Binary Image Hole Filling — Morphological Image Processing

In binary image processing, a **hole** is a background region (0s) completely
surrounded by a connected border of foreground pixels (1s). Filling holes
is a standard preprocessing step in OCR, medical imaging, and industrial vision.

The Surrounded Regions algorithm IS the standard hole-filling algorithm:
`'O'` = background (hole pixel), `'X'` = foreground (boundary pixel).

### The border-seed flood fill

The production algorithm exactly matches the BFS approach in this problem:

```
Step 1: flood-fill from all image border background pixels → mark safe
Step 2: remaining background pixels are holes → fill with foreground
Step 3: restore safe background pixels
```

A seed fill starting from seeds around the image boundary will fill
everything except for the holes within characters — for an image of text,
this isolates the enclosed holes within letters like 'O', 'A', 'B', 'D'.

### The Suzuki-Abe algorithm (1985)

The foundational paper on topological analysis of binary images by border
following establishes the formal equivalence between "holes" and "background
regions not connected to the image border" — the mathematical basis of the
Surrounded Regions problem.

The algorithm determines the surroundness relations among the borders of a
binary image. Hole borders have a one-to-one correspondence to connected
components of foreground pixels and to the holes. These algorithms can be
effectively used in component counting, shrinking, and topological structural
analysis of binary images.

### References

- **Suzuki & Abe (1985) — Topological Structural Analysis of Digitized
  Binary Images by Border Following, Computer Vision, Graphics and Image
  Processing, 30:32-46:**
  https://www.academia.edu/15495158/Topological_Structural_Analysis_of_Digitized_Binary_Images_by_Border_Following
  "Two border following algorithms are proposed. The first determines the
  surroundness relations among the borders of a binary image. Since outer
  borders and hole borders have a one-to-one correspondence to connected
  components and to holes respectively, the algorithm yields a representation
  from which features can be extracted without reconstructing the image."

- **Leptonica — Seed Filling and Connected Components:**
  https://tpgit.github.io/UnOfficialLeptDocs/leptonica/filling.html
  "A seed fill is an image processing operation where pixels in some region
  of an image are assigned a label. A nice example of this is to flood fill
  starting from seeds around the image boundary. For an image of text, this
  will fill everything except for the holes within characters — exactly the
  'safe marker' step in Surrounded Regions."

- **Morphological Hole Filling Algorithms (Academia.edu):**
  https://www.academia.edu/115051431/COMPUTATIONAL_PERFORMANCE_OF_HOLE_FILLING_MORPHOLOGICAL_ALGORITHMS_FOR_BINARY_IMAGES
  "A hole may be defined as a background region surrounded by a connected
  border of foreground pixels. For further processing or quantitative binary
  image analysis, those artifacts must be removed by filling the corresponding
  object regions. Supervised techniques use marker images formed by external
  nearby points to object regions — analogous to the border-seeding step."

- **USPTO Patent 11042969 — Content-aware fill using hole-filling:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/11042969
  "The padding band is identified as a width of pixels surrounding the hole.
  The resulting sampling region (binary mask) is passed to a hole-filling
  algorithm to synthesize a fill for the hole from patches sampled from the
  sampling region — the same enclosed-region identification as Surrounded Regions."

---

## 2. Union-Find in Connected Component Labelling

The Union-Find approach to Surrounded Regions mirrors the standard two-pass
Connected Component Labelling (CCL) algorithm:

```
Pass 1 (scan): union adjacent foreground pixels → build component structure
Pass 2 (label): for each pixel, find(pixel) → assign component label

Surrounded Regions extension:
  virtual node = "border" component
  Pass 1: union border 'O' cells to virtual; union adjacent 'O' cells
  Pass 2: find(cell) == find(virtual) → safe; else → flip to 'X'
```

The find and union algorithms are implemented as in Union-Find. Scanning
is done left-to-right, top-to-bottom: for every pixel check the north and
west pixels (4-connectivity) for a given region criterion — exactly the
scan order used in the Union-Find approach above.

### Reference

- **Wikipedia — Connected-component labeling:**
  https://en.wikipedia.org/wiki/Connected-component_labeling
  "The find and union algorithms are implemented as described in Union-Find.
  Scan the image from left to right and from top to bottom: for every pixel
  check the north and west pixel (4-connectivity) or the northeast, north,
  northwest, and west pixel for 8-connectivity."

---

## 3. Game of Go — Territory and Capture

The Surrounded Regions problem is a direct model of two mechanics in the
board game Go:

### Stone capture (atari)

In Go, a group of stones (same colour) is **captured** when all its
liberties (adjacent empty intersections) are occupied by the opponent.
A group with no liberties is removed from the board — identical to
flipping surrounded `'O'`s to `'X'`.

```
Go board:          Surrounded Regions:
  . B B .            X X X X
  B W W B    →      X O O X   → surrounded W group captured
  B B . .            X X . X
  . . . .            . . . .
```

### Territory scoring

At game end, empty intersections (O) completely surrounded by one player's
stones (X) count as that player's territory — exactly the surrounded regions
problem applied to empty intersections.

The Go board game uses identical logic for determining when groups of stones
are captured (no remaining liberties). Image processing uses boundary traversal
for filling holes in binary images. Terrain analysis applies it to find enclosed
basins, lakes, or depressions in digital elevation models.

---

## Summary

| Domain | 'O' = | 'X' = | "Surrounded" means | Action |
|--------|--------|--------|-------------------|--------|
| Surrounded Regions (#130) | Empty region | Wall | Not connected to border | Flip to X |
| Binary image hole filling | Background pixel | Foreground | Not connected to image border | Fill with foreground |
| Go territory | Empty intersection | Stone | Enclosed by one player's stones | Count as territory |
| Go stone capture | Stone group | Opponent | No liberties (adjacent empties) | Remove from board |

---

## Further Reading

- Suzuki & Abe 1985 (border following): https://www.academia.edu/15495158/Topological_Structural_Analysis_of_Digitized_Binary_Images_by_Border_Following
- Leptonica seed filling: https://tpgit.github.io/UnOfficialLeptDocs/leptonica/filling.html
- Morphological hole filling: https://www.academia.edu/115051431/COMPUTATIONAL_PERFORMANCE_OF_HOLE_FILLING_MORPHOLOGICAL_ALGORITHMS_FOR_BINARY_IMAGES
- CCL Wikipedia (Union-Find): https://en.wikipedia.org/wiki/Connected-component_labeling
- Game of Go rules: https://en.wikipedia.org/wiki/Rules_of_go
