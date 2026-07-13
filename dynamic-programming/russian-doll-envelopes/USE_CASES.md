# Russian Doll Envelopes — Real-World Use Cases

This is **longest chain under a multi-dimensional "fits inside" relation** — nesting, packing,
and dominance problems where an item must beat another on *every* attribute.

---

## 1. Physical Nesting & Packing

The literal case: nesting boxes, containers, or cases to minimise shipping volume; stacking
items where each must be strictly smaller in every dimension. Warehouse and logistics systems
solve exactly this to collapse empty space.

---

## 2. Dominance & Pareto Chains

An envelope "fits inside" another iff it is **dominated** on every dimension. So this is the
longest chain in a **dominance order** — used in:

- **Multi-objective optimisation** — chains of solutions each strictly dominated by the next.
- **Benchmarking** — longest sequence of configurations each strictly better on all metrics.
- **Skyline / Pareto-front queries** in databases (the *antichain* of this same poset is the
  Pareto front — the incomparable elements).

---

## 3. Versioned / Multi-Attribute Compatibility

Chains where each item must exceed the previous on every requirement (memory *and* CPU *and*
disk) — capacity planning, tiered plan design, hardware upgrade paths.

---

## The Unifying Idea

```
"fits inside" = strictly smaller on EVERY dimension = a dominance partial order
longest nesting chain = longest chain in that poset
sort by dim-1 (with a descending tiebreak) → collapses to LIS on dim-2
```

| Domain | Dimensions | A "chain" is |
|--------|-----------|--------------|
| Logistics | width × height | A nesting of containers |
| Multi-objective opt. | metrics | A strictly-improving sequence |
| Capacity planning | CPU × RAM × disk | An upgrade path |

---

## Further Reading

- Order dimension / Dushnik–Miller: https://en.wikipedia.org/wiki/Order_dimension
- Pareto efficiency (the antichain of this poset): https://en.wikipedia.org/wiki/Pareto_efficiency
- Related: [LIS #300](#dynamic-programming/longest-increasing-subsequence),
  [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset).
