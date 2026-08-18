# Reconstruct Itinerary — Real-World Use Cases

The pattern is **"traverse every connection exactly once"** — the Eulerian path. Whenever a problem
is about *covering all edges* (routes, links, segments) rather than *visiting all places*, this is
the tool.

---

## 1. Route Planning With Mandatory Segments

The direct generalization: **mail/delivery routes, street sweepers, snow plows, meter readers** —
services that must traverse **every street** (edge), not just visit every intersection. This is the
**Chinese Postman Problem** (Eulerian path's optimization cousin: if the graph isn't Eulerian, add
the fewest duplicate edges to make it so). GPS route planning with "cover all these roads" constraints
is the same shape.

---

## 2. DNA Sequence Assembly (the industrial heavyweight)

Genome assembly is **the** killer app of Eulerian paths. A sequencer emits millions of short
overlapping reads; to reconstruct the genome:

```
break reads into overlapping k-mers → build a de Bruijn graph
  (nodes = (k-1)-mers, edges = k-mers)
reconstruct genome = find an EULERIAN PATH (use every k-mer edge once)
```

Modern assemblers — **Velvet, SPAdes, ABySS** — are built on exactly this. Using Hierholzer-style
traversal to walk every k-mer once *is* stitching the fragments back into a genome. See the
[Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN).

---

## 3. Circuit Board / Chip Trace Routing

Laying out a trace that must pass through **every required connection point once** — PCB routing and
some VLSI problems map to Eulerian traversal, minimizing wire that doubles back.

---

## 4. Cracking Keypads & Minimal Test Sequences

A keypad with no "enter" that unlocks when the right `n` digits pass through: feed it a **de Bruijn
sequence** (an Eulerian circuit on the de Bruijn graph) and *every* code appears in the stream — a
4-digit PIN in **10,003** keystrokes instead of 40,000. The same idea generates **minimal-length test
vectors** that exercise every `n`-symbol combination. (LeetCode **#753 Cracking the Safe** is this
exactly.)

---

## 5. Network / Traffic Engineering

Verifying a link is exercised, or building a monitoring walk that traverses **every network link
once**, is an Eulerian-path construction — efficient full-coverage sweeps without redundant hops.

---

## The Unifying Idea

```
must use every EDGE (connection) exactly once → Eulerian path → Hierholzer's algorithm
must visit every VERTEX (place) exactly once  → Hamiltonian path → NP-hard (different problem!)
```

| Domain | Edges are | Goal |
|--------|-----------|------|
| Delivery/plowing | streets | cover every street once (Chinese Postman) |
| Genome assembly | k-mers | walk every fragment once |
| PCB routing | required connections | trace without doubling back |
| Keypad cracking | code transitions | all codes in one stream (de Bruijn) |
| Network sweeps | links | full coverage, no redundancy |

**The recognition skill:** "every edge/connection/segment exactly once" ⇒ Eulerian ⇒ polynomial.
Don't mistake it for "every place once" (Hamiltonian/TSP), which is NP-hard.

---

## Further Reading

- [Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN) — the graph theory, existence
  conditions, and de Bruijn sequences in depth.
- [Graph Algorithms](#guides/GRAPH_ALGORITHMS) — topological sort & shortest paths (the every-*node*
  cousins).
- Chinese Postman Problem: https://en.wikipedia.org/wiki/Chinese_postman_problem
