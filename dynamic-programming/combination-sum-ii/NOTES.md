# Combination Sum II — Notes & Intuition

**LeetCode #40** | Backtracking | Medium
Candidates may contain duplicates. Each element used **at most once**. No duplicate
combinations in the output.

---

## Problem

Given candidates (possibly with duplicates) and a target, return all **unique**
combinations summing to the target, using each array element **once**.

```
Input:  candidates = [10,1,2,7,6,1,5], target = 8
Output: [[1,1,6], [1,2,5], [1,7], [2,6]]
```

---

## Two Changes From Combination Sum (#39), Two Fixes

| | [Combination Sum #39](#dynamic-programming/combination-sum) | Combination Sum II (#40) |
|--|--------------------|--------------------------|
| Reuse an element? | yes → recurse with `i` | **no** → recurse with `i + 1` |
| Duplicate values in input? | no | **yes** → must avoid duplicate *combinations* |

Same backtracking skeleton, with two adjustments: **recurse from `i + 1`** (each element
used once) and **skip duplicate values at the same tree level** so the same combination
isn't emitted twice.

```java
Arrays.sort(candidates);                 // group duplicates + enable pruning
backtrack(candidates, target, 0, path, res);

void backtrack(int[] c, int remaining, int start, path, res) {
    if (remaining == 0) { res.add(copy(path)); return; }
    for (int i = start; i < c.length; i++) {
        if (i > start && c[i] == c[i - 1]) continue;   // skip dup at THIS level
        if (c[i] > remaining) break;                    // pruning (sorted)
        path.add(c[i]);
        backtrack(c, remaining - c[i], i + 1, path, res);  // i+1: use each once
        path.removeLast();
    }
}
```

---

## The One Subtle Line — `if (i > start && c[i] == c[i-1]) continue;`

This is the whole trick. At a fixed recursion level (fixed `start`), if a value repeats,
only the **first occurrence** starts a branch; later equal values at that level are
skipped — which is exactly what prevents duplicate combinations.

The guard is `i > start`, **not** `i > 0`. A duplicate value is still usable *deeper* in
the recursion (a different `start`), where it means picking the *second copy* as the next
element — legitimately different. You only suppress duplicates among the choices at the
**same position**.

---

## Full Trace — `[10,1,2,7,6,1,5]`, target `8`

Sorted: `[1,1,2,5,6,7,10]`.

- Top level (`start = 0`): the second `1` (index 1) is skipped (`i > start && c[1]==c[0]`)
  — no two identical `[1,…]` branches.
- Inside the branch that picked the first `1` (now `start = 1`): the second `1` has
  `i == start`, so it **is** used → `[1,1,6]`.

Result: `[[1,1,6], [1,2,5], [1,7], [2,6]]` — no duplicates. ✓

---

## Complexity

| | Time | Space |
|--|------|-------|
| Backtracking | O(2ⁿ) subsets × O(n) copy = O(n · 2ⁿ) | O(n) recursion depth |

Same exponential family as #39 — it's an **enumeration** problem, so backtracking is the
right tool; there's no polynomial DP for *listing* all combinations (only for counting /
min — see the DP-tabulation note on #39).

---

## Edge Cases

| Case | Handling |
|------|----------|
| all duplicates, e.g. `[1,1,1,1]` | level-skip yields each distinct combination once |
| no valid combination | returns empty list |
| candidate > target | pruned by the sorted `break` |
| single element equal to target | valid one-element combination |

## The Duplicate-Skipping Family

The `sort + skip-equal-at-same-level` pattern recurs across:

| Problem | Same idea |
|---------|-----------|
| **Subsets II (#90)** | subsets of a multiset without duplicate subsets |
| **Permutations II (#47)** | permutations of a multiset without duplicate permutations |
| **Combination Sum II (#40)** | this problem |

**The through-line:** sort, recurse from `i + 1` (use each once), and skip equal values at
the same level (`i > start`) to dedupe — the canonical "combinations with duplicates"
backtracking pattern.
