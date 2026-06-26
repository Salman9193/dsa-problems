# Number of Islands — Real-World Use Cases

The Number of Islands problem is the discrete version of Connected Component
Labelling (CCL) — one of the most fundamental operations in computer vision,
used in every image processing pipeline from OCR to medical imaging.

---

## 1. Connected Component Labelling (CCL) — Computer Vision

CCL assigns a unique label to every pixel belonging to the same connected
foreground region in a binary image. This is the Number of Islands problem:
`'1'` = foreground pixel, `'0'` = background pixel, count = number of objects.

### Applications

- **Object detection:** count and locate distinct objects in a scene
- **OCR (Optical Character Recognition):** each character is a connected
  component in a binarised text image
- **Medical imaging:** count cells, lesions, or anomalies in microscope/MRI images
- **Industrial inspection:** detect defects or components on circuit boards
- **Blob tracking in video:** track moving objects frame by frame

### The flood-fill connection

The BFS approach in this problem is literally OpenCV's `floodFill()` function.
The Union-Find approach is the two-pass CCL algorithm (Rosenfeld-Pfaltz 1966),
still widely used in hardware-accelerated implementations.

### References

- **Survey:** *The connected-component labeling problem: A review of
  state-of-the-art algorithms*, Pattern Recognition, 2017.
  https://www.sciencedirect.com/science/article/pii/S0031320317301693
  "Connected-component labeling is indispensable for distinguishing different
  objects in a binary image, and is prerequisite for image analysis and object
  recognition. CCL assigns a unique label to all pixels of each connected
  component. After labeling, each object can be extracted by its label and
  further analysed for shape features such as area, perimeter, and centroid."

- **Wikipedia — Connected-component labeling:**
  https://en.wikipedia.org/wiki/Connected-component_labeling
  "Connected-component labeling is used in computer vision to detect connected
  regions in binary digital images. Blob extraction is generally performed on
  the resulting binary image from a thresholding step. Blobs may be counted,
  filtered, and tracked."

- **Flood fill in OpenCV:**
  https://dioph.github.io/casual-code/using-the-flood-fill-algorithm-to-label-objects-in-binary-images/
  "The flood-fill algorithm resembles the 'ink bucket' of image editors —
  from a source pixel, all neighbours in the same connected region are filled
  with the same value. This can be implemented using BFS or DFS. In OpenCV,
  this is implemented in the floodFill() function."

- **Hardware CCL paper:** arXiv:2105.09658 — *A Connected Component Labelling
  algorithm for multi-pixel per clock cycle video stream*.
  https://arxiv.org/pdf/2105.09658
  Implements real-time CCL for 4K/60fps video using a streaming Union-Find
  variant — the same Union-Find approach as Approach 3 in this problem,
  accelerated for hardware.

- **USPTO Patent 9042652 — Techniques for connected component labeling:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9042652
  "Connected component labeling scans an image pixel-by-pixel to identify
  connected pixel regions. The number of distinct connected components in the
  image is defined by the number of labels used, excluding the background —
  exactly the island count in this problem."

---

## 2. Geographic Information Systems — Landmass Detection

Satellite imagery processing uses Number of Islands to:
- Count distinct landmasses in a geographic raster image
- Measure island areas (variant: max area of island)
- Detect coastlines (variant: island perimeter)
- Track changes in land area over time (Arctic ice sheet monitoring)

The grid IS the satellite image; `'1'` is land pixels; the island count is
the number of distinct landmasses.

---

## 3. PCB and VLSI Design — Connected Net Detection

In printed circuit board (PCB) design and VLSI layout:
- A "net" is a set of copper traces all electrically connected
- Finding all nets = finding connected components in the routing grid
- Verifying no unintended connections between nets = checking that
  certain components are in different islands

EDA (Electronic Design Automation) tools use CCL internally to identify
and label nets during Design Rule Check (DRC).

---

## The Algorithm Family

| Algorithm | Implementation | Best for |
|-----------|---------------|---------|
| BFS flood fill | Queue + mark on enqueue | Production systems (no stack overflow) |
| DFS flood fill | Recursion + in-place mark | Simple code, small grids |
| Two-pass Union-Find | Scan right+down, then resolve | Streaming/hardware, multiple queries |
| Parallel CCL | Multiple threads, merge at boundaries | GPU/FPGA real-time video |

---

## Further Reading

- CCL survey (Pattern Recognition 2017): https://www.sciencedirect.com/science/article/pii/S0031320317301693
- CCL Wikipedia: https://en.wikipedia.org/wiki/Connected-component_labeling
- Flood fill in OpenCV: https://dioph.github.io/casual-code/using-the-flood-fill-algorithm-to-label-objects-in-binary-images/
- Hardware CCL (arXiv): https://arxiv.org/pdf/2105.09658
- Scaler CCL guide: https://www.scaler.com/topics/connected-component-analysis-in-image-processing/
