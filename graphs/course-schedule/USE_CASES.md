# Course Schedule — Real-World Use Cases

Directed cycle detection via topological sort is the algorithm behind
dependency resolution in build systems, package managers, and compilers —
some of the most widely used software infrastructure.

---

## 1. Build Systems — Make, Gradle, Maven, Bazel

Every build system represents compilation targets as a directed dependency
graph. A module must be compiled after all its dependencies are ready.
Topological sort gives the valid compilation order. A cycle is a fatal error.

Tools like Make, Bazel, Gradle, and npm topologically sort their build
targets so each target is compiled only after all its dependencies.
A cycle in the dependency graph is usually reported as a fatal error —
the build system cannot decide where to start.

Modern tools like Maven or Gradle construct dependency graphs behind the
scenes. The entire idea depends on the absence of cycles — without that
ordering, builds would fail or require manual sequencing every time.

### The structural parallel

```
Course Schedule (#207):              Build System:
  courses          ↔  build targets / modules
  prerequisite [a,b]  ↔  module A depends on module B
  cycle detected   ↔  "circular dependency" fatal error
  topological order ↔  valid build order
  return false     ↔  build aborted
```

### Tartarian — Maven dependency circular detection

A dependency graph can be defined as a directed graph G = (V, E) where V
is a set of modules and E is a set of dependencies. V(X,Y) indicates
module X depends on module Y. Using the dependency graph, Tartarian can
address "dependency hell" problems including circular dependency —
e.g. V(X,Y), V(Y,Z), V(Z,X) — the exact scenario in this problem.

### References

- **arXiv:1708.02393 — Cherry-Picking and Maven Dependency Graphs:**
  https://arxiv.org/pdf/1708.02393
  "A dependency graph is a directed graph G=(V,E) where V is modules and
  E is dependencies. V(X,Y) indicates module X depends on modules Y. The
  system addresses circular dependency e.g. V(X,Y), V(Y,Z), V(Z,X) —
  the Course Schedule cycle."

- **Topological Sort Calculator (build systems):**
  https://miniwebtool.com/topological-sort-calculator/
  "Tools like Make, Bazel, Gradle, and npm topologically sort their build
  targets so each target is compiled only after all its dependencies.
  A cycle in the dependency graph is usually reported as a fatal error."

- **Dependency Analysis Gradle Plugin (GitHub):**
  https://github.com/autonomousapps/dependency-analysis-gradle-plugin
  "Generates views of the project graph for multi-project builds. In addition
  to the graphviz output, there is also a topological sort, which might be
  useful for debugging issues with evaluation order."

- **USPTO Patent 7536401 — Graph-based circular dependency resolution
  in object persistence:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7536401
  "Topologically sorting the graph to order dependencies among nodes,
  including circular dependences. Traversing the graph; reversing directional
  connections of the nodes; collapsing nodes previously visited until all
  circular paths are removed from the graph."

---

## 2. Package Managers — npm, pip, apt, Homebrew

Package managers compute a topological order of packages before installation.
Each package is a node; each dependency is a directed edge. A circular
dependency causes an abort with "circular dependency detected" error.

```
pip install requests
  → requests depends on certifi, urllib3, charset-normalizer, idna
  → each of those has further dependencies
  → topological sort gives the installation order
  → cycle detected → "Cannot install: circular dependency"
```

npm, pip, apt, Homebrew, and Cargo all use the same DFS/Kahn's topological
sort internally to resolve installation order and detect cycles.

---

## 3. Spreadsheet Recalculation — Cell Dependency DAG

When a cell changes, a spreadsheet must recompute every downstream cell in
dependency order — a topological sort of the cell-dependency DAG.
Circular references (cycles) are rejected by the application.

```
A1 = B1 + 1
B1 = C1 * 2
C1 = A1 / 3    ← circular reference! (A1→B1→C1→A1)
```

Microsoft Excel and Google Sheets detect this cycle (topological sort fails)
and show "Circular Reference Warning" — exactly the cycle detection in #207.

---

## 4. Operating Systems — Deadlock Detection

A **deadlock** in an OS occurs when a set of processes each holds a resource
needed by another — forming a cycle in the resource-allocation graph.
Deadlock detection = cycle detection in the directed resource graph.

```
Process A holds Lock 1, waits for Lock 2
Process B holds Lock 2, waits for Lock 1
→ Cycle: A→(needs)→B→(needs)→A → DEADLOCK
```

Deadlock detection in operating systems uses DFS cycle detection on the
process-resource graph — the same algorithm as Course Schedule.

---

## 5. Compiler — Module Import Order and Symbol Resolution

Compilers use topological sort to determine:
- Which modules to compile first (import dependency graph)
- Which type/function definitions to process first (forward declaration order)

Circular imports (`module A imports module B imports module A`) are detected
as cycles and reported as compilation errors in Python, Java, Rust, and Go.

---

## Summary

| Domain | Node = | Edge = | Cycle means |
|--------|--------|--------|------------|
| Course Schedule (#207) | Course | Prerequisite | Impossible to complete all |
| Build system | Module | Compile dependency | Build fails |
| Package manager | Package | Install dependency | Cannot install |
| Spreadsheet | Cell formula | Cell reference | Circular reference error |
| OS deadlock | Process | Resource wait | Deadlock |
| Compiler | Module/symbol | Import/use | Circular import error |

---

## Further Reading

- Topological sort in build systems: https://miniwebtool.com/topological-sort-calculator/
- Maven dependency graph (arXiv): https://arxiv.org/pdf/1708.02393
- Dependency circular resolution (USPTO): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/7536401
- Gradle dependency analysis: https://github.com/autonomousapps/dependency-analysis-gradle-plugin
- Kahn's algorithm guide: `guides/GRAPH_ALGORITHMS.md`
