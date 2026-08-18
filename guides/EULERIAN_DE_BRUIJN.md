# Eulerian Paths & de Bruijn Sequences

Two ideas, one graph. An **Eulerian path** walks **every edge exactly once**; a **de Bruijn
sequence** is the string you get when you do that on a specially built graph so that every possible
substring appears once. Together they answer questions from *"crack this keypad"* to *"assemble this
genome"* — and they trace back to the **founding problem of graph theory.**

This is the every-*edge* counterpart to [Graph Algorithms](#guides/GRAPH_ALGORITHMS) (topological
sort, shortest paths — every *node*), and the theory behind
[Reconstruct Itinerary #332](#graphs/reconstruct-itinerary).

---

## Eulerian vs. Hamiltonian — Get This Straight First

| | **Eulerian** | **Hamiltonian** |
|---|--------------|-----------------|
| Visit every… | **edge** once | **vertex** once |
| Complexity | **polynomial** (linear!) | **NP-hard** |
| Existence test | degree condition (below) | no simple characterization |
| Construct | Hierholzer, O(E) | brute force / TSP |

They sound like twins; they're worlds apart. **"Use every connection once" is easy; "visit every
place once" is intractable.** The single most valuable skill in this area is recognizing which one a
problem is — the difference between an O(E) walk and a hopeless search.

---

## When Does an Eulerian Path Exist?

**Euler (1736), the Seven Bridges of Königsberg** — the problem that *founded graph theory.* Can you
cross all seven bridges exactly once? Euler proved **no**, by reducing it to vertex degrees:

**Undirected graph** (all edges in one connected component):
- **Eulerian circuit** (starts = ends): **every vertex has even degree.**
- **Eulerian path** (open): **exactly 0 or 2 vertices have odd degree** (those two are the endpoints).

**Directed graph** (one strongly connected component over non-isolated vertices):
- **Circuit:** every vertex has `indegree == outdegree`.
- **Path:** at most one vertex with `outdeg − indeg = 1` (start), at most one with
  `indeg − outdeg = 1` (end), the rest balanced.

> **Königsberg had four landmasses all with odd degree** — more than two odd vertices, so no Eulerian
> path exists. That negative answer is the birth of graph theory: Euler turned a walking puzzle into a
> statement about *degrees*, inventing the whole idea of modeling structure as a graph.

---

## Hierholzer's Algorithm — Constructing the Path in O(E)

**Hierholzer (1873)** gave the linear-time constructor. The intuition: **follow edges until you get
stuck (that must happen at the start/end vertex), then splice in cycles from any vertex that still
has unused edges.**

The clean recursive form is a **post-order DFS**:

```
dfs(node):
    while node has an unused edge (node → next):
        remove that edge
        dfs(next)
    prepend node to the path         ← record only when the node is EXHAUSTED
```

**Why post-order + reverse is the trick:** when a vertex runs out of edges it's a local dead end, so
it belongs *later* in the path. Recording on the way *out* of recursion (and reversing at the end)
places dead-ends correctly and stitches detours (cycles) in ahead of them. This is exactly the
mechanism worked through in [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary), where a
naive greedy strands tickets and Hierholzer repairs it automatically.

```java
// directed graph as adj list; returns the Eulerian path (reversed post-order)
void hierholzer(int start, Map<Integer, Deque<Integer>> adj, LinkedList<Integer> path) {
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(start);
    while (!stack.isEmpty()) {
        int v = stack.peek();
        Deque<Integer> out = adj.get(v);
        if (out != null && !out.isEmpty()) stack.push(out.pop());  // walk an unused edge
        else path.addFirst(stack.pop());                           // exhausted → prepend
    }
}
```

**O(E)** — each edge consumed exactly once. (Iterative form shown to avoid recursion-depth limits.)

---

## de Bruijn Sequences — Eulerian Circuits That Build Strings

A **de Bruijn sequence** `B(k, n)` is a **cyclic** string over a `k`-symbol alphabet in which **every
possible length-`n` string appears exactly once** as a (wrap-around) substring.

```
B(2,3) = 00111010   (length 8 = 2³)
  windows: 001 011 111 110 101 010 100 000  ← all eight 3-bit strings, once each
```

**The magic:** listing all kⁿ strings separately takes `kⁿ · n` symbols; a de Bruijn sequence packs
them into just **kⁿ** symbols via maximal overlap — each new symbol reveals a whole new n-window.

### How to build one: it's an Eulerian circuit

Construct the **de Bruijn graph**:
- **Nodes** = all `(n−1)`-length strings (there are `kⁿ⁻¹`).
- **Edges** = all `n`-length strings: the n-string `s` is an edge from `s[0..n-1]` to `s[1..n]`.

Every node has in-degree = out-degree = `k`, so an **Eulerian circuit always exists** — and walking
it, emitting one symbol per edge, produces `B(k, n)`. **The de Bruijn sequence *is* an Eulerian
circuit on the de Bruijn graph.** (de Bruijn, 1946.)

```
B(2,3): nodes = {00,01,10,11}, each with 2 in / 2 out edges
Eulerian circuit visiting all 8 edges → 8-symbol cyclic sequence containing every 3-bit string
```

*(LeetCode [Cracking the Safe #753] is exactly this: build the de Bruijn graph, walk the Eulerian
circuit.)*

---

## Real-World Use Cases

Every application below is one of the two superpowers: **maximal overlap** (cram all patterns into
minimal space) or **local uniqueness** (any window pins your global position).

### 1. Genome Assembly — the heavyweight

A DNA sequencer emits millions of short overlapping **reads**. To reconstruct the genome:

```
break every read into overlapping k-mers
build a de Bruijn graph  (nodes = (k-1)-mers, edges = k-mers)
genome ≈ an Eulerian path through it  (use every k-mer edge once)
```

**Pevzner, Tang & Waterman (2001)** introduced this Eulerian-path formulation; it underlies modern
assemblers **Velvet, SPAdes, ABySS, IDBA.** *(Honest nuance: the popular "Eulerian-easy-beats-
Hamiltonian-hard" story is real history but an oversimplification — real assemblers don't literally
solve for a unique Eulerian path, because genome reconstruction is inherently ambiguous with repeats.
The de Bruijn graph's true advantages are the **k-mer abstraction, error correction, and memory
efficiency**, per Compeau–Pevzner–Tesler 2011. Worth knowing, because interviewers who know genomics
will appreciate the precision.)*

### 2. Cracking Keypads / Minimal Test Vectors — maximal overlap

A keypad with no "enter" unlocks the instant the right `n` digits pass through. Feed it a **de Bruijn
sequence** and *every* code appears in the stream:

```
4-digit PIN: 10,000 codes × 4 = 40,000 keystrokes to try separately
de Bruijn B(10,4): all of them in 10,003 keystrokes
```

Same idea generates **minimal-length test sequences** that exercise every `n`-symbol state
combination — the shortest input covering all cases.

### 3. Position Encoding — local uniqueness

Because every n-window is unique, **seeing any window tells you exactly where you are**:
- **Rotary/shaft encoders & robotics:** a strip printed with a de Bruijn pattern lets a small sensor
  reading `n` marks know its **absolute position** with no homing.
- **Structured-light 3D scanning:** project a de Bruijn-coded stripe pattern; any small patch
  identifies *which* stripes it sees, enabling reconstruction.
- **The "magic" card trick:** 32 cards in de Bruijn order — any 5 consecutive cards identify the
  whole deck.

### 4. Fast Bit Scan — the `deBruijn` multiply trick

A specific de Bruijn constant computes the index of the **lowest set bit** with one multiply + table
lookup: `index = table[(x & -x) * DEBRUIJN >>> shift]`. The standard fast "count trailing zeros"
before hardware `CTZ`/`BSF` was universal — still seen in chess engines and low-level code.

| Application | Which superpower | Structure |
|-------------|------------------|-----------|
| Genome assembly | overlap (compact k-mer graph) | de Bruijn graph + Eulerian path |
| Keypad cracking | maximal overlap | de Bruijn sequence |
| Minimal test vectors | maximal overlap | de Bruijn sequence |
| Rotary encoders / structured light | local uniqueness | de Bruijn sequence |
| Lowest-set-bit | perfect-hash overlap | de Bruijn constant |

---

## The Problem Family

| Problem | Flavor |
|---------|--------|
| [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary) | Eulerian **path**, lexical-smallest, Hierholzer |
| Cracking the Safe #753 | Eulerian **circuit** on a de Bruijn graph |
| Valid Arrangement of Pairs #2097 | Eulerian path; find start from degree imbalance |
| Chinese Postman (theory) | make a non-Eulerian graph Eulerian with fewest added edges |

---

## Interview Notes

- **First move: "is this every-edge or every-vertex?"** Every-edge ⇒ Eulerian ⇒ polynomial ⇒
  Hierholzer. Every-vertex ⇒ Hamiltonian/TSP ⇒ NP-hard. Naming it correctly is 80% of the signal.
- **State the existence condition** (degree balance) — it shows you know *why* the walk exists, not
  just how to code it.
- **Post-order + reverse** is the Hierholzer detail to volunteer; the naive greedy's dead-end failure
  is the follow-up they'll ask about.
- **de Bruijn = Eulerian circuit on the de Bruijn graph** is the one-liner that connects the string
  problem to the graph problem — and lets you mention genome assembly credibly.

---

## Research & Foundations

*Citations verified against the historical record and primary literature — not from memory.*

- **L. Euler (1736), "Solutio problematis ad geometriam situs pertinentis,"** *Commentarii Academiae
  Scientiarum Petropolitanae* 8:128–140. The **Seven Bridges of Königsberg** — founds graph theory
  and gives the degree condition. https://en.wikipedia.org/wiki/Seven_Bridges_of_K%C3%B6nigsberg

- **C. Hierholzer (1873), "Ueber die Möglichkeit, einen Linienzug ohne Wiederholung und ohne
  Unterbrechung zu umfahren,"** *Mathematische Annalen* 6(1):30–32. DOI:
  [10.1007/BF01442866](https://doi.org/10.1007/BF01442866). The **linear-time constructive** algorithm.

- **N. G. de Bruijn (1946), "A Combinatorial Problem,"** *Proc. Koninklijke Nederlandse Akademie van
  Wetenschappen* 49:758–764. de Bruijn sequences and their Eulerian-circuit construction.

- **P. A. Pevzner, H. Tang & M. S. Waterman (2001), "An Eulerian path approach to DNA fragment
  assembly,"** *PNAS* 98(17):9748–9753. DOI:
  [10.1073/pnas.171285098](https://doi.org/10.1073/pnas.171285098). Brought de Bruijn graphs to
  genome assembly.

- **P. E. C. Compeau, P. A. Pevzner & G. Tesler (2011), "How to apply de Bruijn graphs to genome
  assembly,"** *Nature Biotechnology* 29(11):987–991. DOI:
  [10.1038/nbt.2023](https://doi.org/10.1038/nbt.2023). The accessible modern account (and the source
  for the honest nuance above).

> **The through-line:** Euler asked *whether* you can walk every edge (1736), Hierholzer showed *how*
> in linear time (1873), and de Bruijn made it *generative* — walk a cleverly built graph and you
> emit a string containing every substring (1946). Three centuries later that exact chain assembles
> genomes and cracks keypads. **Reframing "use every X once" as an Eulerian walk imports all of it at
> once** — the recognition is the skill.

**Related in this repo:** [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary),
[Graph Algorithms](#guides/GRAPH_ALGORITHMS), [Trie](#guides/TRIE) (another string-as-structure idea),
and the k-mer/streaming connection in [Klee's Algorithm](#guides/KLEES_ALGORITHM).
