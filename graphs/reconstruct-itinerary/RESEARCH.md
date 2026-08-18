# Reconstruct Itinerary — Research & Foundations

The Eulerian path is arguably **the founding problem of graph theory** — and Hierholzer's method for
constructing one is a small classic. *Citations verified against the historical record — not from
memory.*

- **L. Euler (1736), "Solutio problematis ad geometriam situs pertinentis."** *Commentarii Academiae
  Scientiarum Petropolitanae* 8:128–140. The **Seven Bridges of Königsberg** — Euler proved no walk
  crosses all seven bridges exactly once, founding graph theory in the process. The existence
  condition (vertex degrees) is his. English discussion:
  https://en.wikipedia.org/wiki/Seven_Bridges_of_K%C3%B6nigsberg

- **C. Hierholzer (1873), "Über die Möglichkeit, einen Linienzug ohne Wiederholung und ohne
  Unterbrechung zu umfahren."** *Mathematische Annalen* 6(1):30–32. DOI:
  [10.1007/BF01442866](https://doi.org/10.1007/BF01442866). The **constructive** algorithm: build the
  path by splicing in cycles at exhausted vertices — precisely the post-order traversal this problem
  uses, in **linear time**.

- **N. G. de Bruijn (1946), "A Combinatorial Problem."** *Proceedings of the Koninklijke Nederlandse
  Akademie van Wetenschappen* 49:758–764. Counts the number of de Bruijn sequences and connects them
  to Eulerian circuits on what are now called **de Bruijn graphs** — the bridge from this problem to
  DNA assembly and keypad cracking.

**Why it matters:** the Königsberg result is the moment "can I traverse this structure?" became a
*mathematical* question about degrees rather than a puzzle. Euler's condition tells you **whether** an
Eulerian path exists; Hierholzer tells you **how** to build one efficiently; de Bruijn's work makes it
*generative* (construct a string with every substring). Together they turn a bridge-walking riddle
into the algorithm behind modern genome assemblers.

> **The generalizable lesson:** reframing a concrete task ("use every ticket") as an abstract graph
> property ("Eulerian path") instantly imports 285 years of theory — existence conditions, a
> linear-time constructor, and a whole family of applications. Recognizing the abstraction *is* the
> senior-engineer skill.

**Related in this repo:** [Graph Algorithms](#guides/GRAPH_ALGORITHMS),
[Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN),
[Course Schedule II](#graphs/course-schedule-ii) (topological order — the every-*node* analogue).
