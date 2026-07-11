# Combination Sum II — Real-World Use Cases

This is constrained subset enumeration: pick a sub-multiset of items that hits an exact
target, using each physical item at most once, and never report the same selection twice.
That shape appears wherever you choose *from a finite pool of distinct-but-possibly-equal
items*.

---

## 1. Exact Selection From a Finite Inventory

Unlike Combination Sum (#39), items are **not reusable** — you have a specific pile with
some equal-valued duplicates and can use each piece once. Examples:

- **Making an exact amount from the coins/notes actually in hand** (each physical coin used
  once) — contrast with a change machine's reusable denominations.
- **Fulfilling an order from specific stock units**, where two units may have identical
  value but are distinct pieces.

The `sort + skip-equal-at-same-level` rule is what stops the two identical units from
producing duplicate selections.

---

## 2. Deduplicated Combinatorial Configuration

Choosing a subset of options that meets a budget/target exactly, where the option list has
repeats and you don't want to present the same effective configuration twice — pricing
bundles, resource packs, or test-case selection over a multiset of parameters.

---

## 3. Subset-Sum With Physical Multiplicity

The bounded, "use-each-once" cousin of subset sum: given a multiset of weights, find all
distinct sub-multisets summing to a capacity — knapsack-style feasibility enumeration where
identical weights are distinct items but equivalent selections collapse.

---

## The Unifying Idea

```
sort the multiset                 → equal items are adjacent
recurse from i + 1                → each physical item used at most once
skip equal values at same level   → distinct items, but equivalent selections reported once
```

| Domain | Items (with equal-valued dups) | A "combination" is |
|--------|-------------------------------|--------------------|
| Cash handling | The specific coins in hand | An exact-amount selection |
| Order fulfillment | Distinct stock units | A way to meet the order |
| Config / budgeting | Repeatable options | A distinct bundle hitting the target |

---

## Further Reading

- Backtracking (systematic search with pruning): https://en.wikipedia.org/wiki/Backtracking
- Subset sum problem: https://en.wikipedia.org/wiki/Subset_sum_problem
- Sibling problems: Subsets II (#90), Permutations II (#47) — same duplicate-skip pattern.
