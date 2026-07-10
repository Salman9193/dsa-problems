# Largest Divisible Subset — Real-World Use Cases

The problem is a disguised **longest chain in a partial order**. Wherever items have a
"one must come before / build on / contain the other" relation that isn't total, finding
the longest chain (or the fewest chains to cover everything) is a recurring task.

---

## 1. Dependency & Build Chains — Critical Path

Tasks with prerequisites form a partial order (a DAG). The **longest chain** is the
**critical path** — the minimum time to finish everything when independent tasks run in
parallel. Project scheduling (CPM/PERT), build systems, and compiler pass ordering all
compute longest chains over a poset, exactly the structure here with divisibility as the
order.

- **Critical path method:** https://en.wikipedia.org/wiki/Critical_path_method

---

## 2. Version & Compatibility Chains

When versions or formats upgrade in steps (each compatible with the previous), the longest
valid upgrade path is a chain in the compatibility order. Selecting the longest sequence of
mutually compatible components is the same longest-chain computation.

---

## 3. Nesting Problems

"Which items nest inside each other the most?" — nested envelopes, boxes, or intervals —
is a longest chain under the "fits inside" partial order. The Russian Doll Envelopes
problem (LeetCode 354) is the 2-D version, solved by sorting plus an LIS on the second
dimension — the same reduction used here.

- **Partially ordered set:** https://en.wikipedia.org/wiki/Partially_ordered_set

---

## The Unifying Idea

```
items + a "divides / precedes / nests-in" relation  →  a partial order (poset)
the largest mutually-related subset  →  the longest chain in that poset
sort to linearize + LIS-style DP  →  compute it in O(n^2)
```

| Domain | Partial order | Longest chain means |
|--------|---------------|---------------------|
| Scheduling / builds | "must precede" | Critical path / min completion time |
| Versioning | "upgrades to" | Longest compatible upgrade path |
| Nesting | "fits inside" | Deepest nesting |
| Number theory | "divides" | Largest divisible subset (this problem) |

---

## Further Reading

- Partially ordered set: https://en.wikipedia.org/wiki/Partially_ordered_set
- Critical path method: https://en.wikipedia.org/wiki/Critical_path_method
- Dilworth's theorem (chains & antichains): https://en.wikipedia.org/wiki/Dilworth%27s_theorem
