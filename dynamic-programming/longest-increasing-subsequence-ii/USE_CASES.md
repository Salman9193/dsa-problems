# Longest Increasing Subsequence II — Real-World Use Cases

The shape is: **build the longest chain where each step must land inside a bounded window** —
progress that can't jump too far at once. That constraint is everywhere in systems.

---

## 1. Upgrade / Migration Paths (the motivating case)

Software releases declare the **oldest version they can upgrade directly from**. You may upgrade
`j → i` only if `j` is old enough to precede `i` but **not so old that `i` dropped support** for
it. Finding the longest (or shortest) valid chain of successive upgrades is exactly this DP.

```
release i:  version v[i],  minimum supported source m[i]
upgrade j → i  iff  m[i] ≤ v[j] < v[i]
dp[v[i]] = 1 + max( dp over the value range [ m[i], v[i]−1 ] )
```

**The crucial real-world property:** this relation is **not transitive**. You can go `1 → 5` and
`5 → 10`, yet `1 → 10` may be forbidden — which is precisely *why* multi-hop upgrade paths exist
and why "just upgrade to latest" often fails. Database engines, Kubernetes, and OS distributions
all publish such support windows.

---

## 2. Schema / Data Migrations

Migration scripts often support only a bounded range of prior schema versions. Chaining
migrations through intermediate versions — and knowing when **no** path exists — is the same
computation.

---

## 3. Bounded-Step Progressions

- **Skill/level progression** where each step can't exceed a difficulty jump.
- **Signal / sensor tracking**: the longest run of increasing readings with no jump larger than
  `k` (a plausible-continuity filter).
- **Stock or metric analysis**: longest increasing streak with no gap larger than a threshold.
- **Physical processes**: chains of states where each transition is bounded by a rate limit.

---

## The Unifying Idea

```
chain must increase, but each step must land in a bounded WINDOW
  ⇒ dp keyed by VALUE:  dp[x] = 1 + max( dp over [lowerBound(x), x−1] )
  ⇒ the eligible predecessors are CONTIGUOUS  ⇒ range-max query (segment tree)
```

| Domain | The "value" | The window |
|--------|-------------|-----------|
| Upgrade paths | Version number | `[minSupported, current)` |
| Migrations | Schema version | Supported source range |
| Signal tracking | Reading | `[x−k, x)` — max plausible jump |

---

## Further Reading

- Segment tree: https://en.wikipedia.org/wiki/Segment_tree
- Related: [LIS #300](#dynamic-programming/longest-increasing-subsequence) (the unbounded case —
  where patience sorting *does* work), and
  [Parallel Courses III #2050](#graphs/parallel-courses-iii) (longest path in a DAG with weights).
