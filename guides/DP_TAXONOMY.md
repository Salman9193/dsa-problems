# DP Taxonomy — A Map of Every Dynamic Programming Type

Dynamic programming feels like a hundred unrelated tricks until you see that **there are only a
few axes of variation**. This guide is the map: name each DP type, give its state/transition
signature, and point at the problems (in this repo and on LeetCode) that drill it.

For the deep dives, it links out to [Knapsack Variants](#guides/KNAPSACK_VARIANTS),
[String DP](#guides/STRING_DP), [Intervals](#guides/INTERVALS), and
[Greedy vs DP](#guides/GREEDY_VS_DP).

---

## The One Idea Behind the Whole Taxonomy

Every DP has the same cost structure:

```
total time  =  (number of states)  ×  (cost per transition)
```

So the entire taxonomy is really **two questions**:

1. **What indexes the state?** (a position, a cell, two positions, a subset, a tree node, a range…)
2. **How expensive is one transition?** (O(1) constant, or a loop over choices?)

Almost every "type" below is just a different answer to those two questions. Get in the habit of
asking them and DP stops being a bag of tricks.

| Type | State indexed by | Transition | Typical total |
|------|------------------|-----------|---------------|
| Constant-transition | a position | **O(1)** | O(n) |
| Grid | a cell `(r,c)` | O(1) | O(mn) |
| Dual-sequence | two positions `(i,j)` | O(1) | O(mn) |
| Non-constant-transition | a position | **O(k) loop** | O(n·k), often O(n²) |
| Knapsack | `(item, capacity)` | O(1) | O(n·W) |
| Interval | a range `(i,j)` | **O(n) split** | O(n³) |
| Topological-order (DAG) | a node | O(indegree) | O(V+E) |
| Tree | a subtree (node) | O(children) | O(n) |
| Bitmask | `(subset, …)` | O(n) | O(2ⁿ·n) |

Read on for what each one *is*, and the full problem mapping at the end.

---

## 1. Constant-Transition DP

**State:** one index. **Transition:** a fixed, O(1) combination of a bounded number of previous
states. The purest DP — a linear recurrence.

```
dp[i] = f(dp[i-1], dp[i-2], …)      // fixed arity, no inner loop
```

- **House Robber:** `dp[i] = max(dp[i-1], dp[i-2] + nums[i])`
- **Fibonacci / Climbing Stairs:** `dp[i] = dp[i-1] + dp[i-2]`
- **Jump Game** (reachability collapses to a running max)

**Signature move:** almost always **O(1)-space** optimisable, because you only look back a constant
number of steps — keep two variables, not an array.

> Repo: [House Robber [#198](https://leetcode.com/problems/house-robber/)](#dynamic-programming/house-robber),
> [Jump Game [#55](https://leetcode.com/problems/jump-game/)](#dynamic-programming/jump-game).

---

## 2. Grid DP

**State:** a cell `(r, c)`. **Transition:** O(1), from a fixed set of neighbours (usually up/left).
Constant-transition DP in two dimensions.

```
dp[r][c] = grid[r][c] + min(dp[r-1][c], dp[r][c-1])     // e.g. min path sum
```

- **Unique Paths / with obstacles**
- **Minimum Path Sum**, **Maximal Square**, **Dungeon Game**

**Signature move:** rolling array — you only need the previous row, so O(n) space.

> Repo: [Unique Paths [#62](https://leetcode.com/problems/unique-paths/)](#dynamic-programming/unique-paths).
> Multi-agent grid DP (state = *both* agents' positions) appears in
> [Cherry Pickup II [#1463](https://leetcode.com/problems/cherry-pickup-ii/)](#dynamic-programming/cherry-pickup-ii) — a grid DP whose state is a pair
> of columns.

---

## 3. Dual-Sequence DP

**State:** a position in *each* of two sequences, `(i, j)`. **Transition:** O(1), branching on
whether `a[i]` and `b[j]` match.

```
dp[i][j] = (a[i]==b[j]) ? dp[i-1][j-1] + 1
                        : max(dp[i-1][j], dp[i][j-1])   // LCS
```

The whole **string-DP** family lives here: LCS, edit distance, distinct subsequences, shortest
common supersequence, regex/wildcard matching.

> Deep dive: **[String DP](#guides/STRING_DP)**. Repo:
> [Shortest Common Supersequence [#1092](https://leetcode.com/problems/shortest-common-supersequence/)](#dynamic-programming/shortest-common-supersequence)
> (built directly on LCS).

---

## 4. Non-Constant-Transition DP

**State:** one index — *but* the transition is a **loop over all earlier states**, so it's not O(1).
This is the jump from O(n) to O(n²).

```
dp[i] = 1 + max( dp[j] for all j < i with condition(j, i) )     // LIS
```

- **Longest Increasing Subsequence** (`condition`: `nums[j] < nums[i]`)
- **Largest Divisible Subset** (`condition`: `nums[i] % nums[j] == 0`)
- **Word Break** (`condition`: `s[j..i]` is a word) — the "position" is a string index

**Signature move:** the inner loop is where **acceleration** lives — a monotone `tails` array
(patience sorting) or a segment tree can sometimes cut the transition from O(n) to O(log n). That's
the entire subject of the LIS cluster.

> **Grid × non-constant hybrid:** [Minimum Path Cost in a Grid #2304](#dynamic-programming/minimum-path-cost-in-a-grid)
> is a grid DP whose transition scans the whole previous row — the clearest example of a non-constant
> transition sitting on grid-shaped state.
>
> Repo: [LIS [#300](https://leetcode.com/problems/longest-increasing-subsequence/)](#dynamic-programming/longest-increasing-subsequence),
> [Largest Divisible Subset [#368](https://leetcode.com/problems/largest-divisible-subset/)](#dynamic-programming/largest-divisible-subset),
> [Number of LIS [#673](https://leetcode.com/problems/number-of-longest-increasing-subsequence/)](#dynamic-programming/number-of-lis),
> [LIS II [#2407](https://leetcode.com/problems/longest-increasing-subsequence-ii/)](#dynamic-programming/longest-increasing-subsequence-ii) (segment-tree accelerated),
> [Word Break [#139](https://leetcode.com/problems/word-break/)](#dynamic-programming/word-break).

---

## 5. Knapsack DP

**State:** `(items considered, capacity used)`. **Transition:** O(1) — take the item or don't. The
family is defined by **how many copies of each item** you may take.

| Variant | Rule | Loop direction |
|---------|------|----------------|
| **0/1 knapsack** | each item **at most once** | capacity **descending** |
| **Unbounded knapsack** | each item **unlimited** | capacity **ascending** |
| **Bounded knapsack** | each item **≤ kᵢ** times | binary-split into 0/1 items, or a monotonic-deque transition |
| **Weight-only (subset-sum)** | value = weight; *can* we hit the target? | boolean DP |

**The one rule that separates them:** loop capacity **descending for 0/1** (so an item isn't reused
within one pass) and **ascending for unbounded** (so it can be). Getting this backwards is *the*
knapsack bug.

- **Weight-only:** Partition Equal Subset Sum, Target Sum
- **Unbounded:** Coin Change, Coin Change II, Combination Sum IV
- **0/1:** classic 0/1 knapsack, Last Stone Weight II

> Deep dive: **[Knapsack Variants](#guides/KNAPSACK_VARIANTS)**. Repo:
> [Partition Equal Subset Sum [#416](https://leetcode.com/problems/partition-equal-subset-sum/)](#dynamic-programming/partition-equal-subset-sum) (weight-only),
> [Coin Change [#322](https://leetcode.com/problems/coin-change/)](#dynamic-programming/coin-change) &
> [Coin Change II [#518](https://leetcode.com/problems/coin-change-ii/)](#dynamic-programming/coin-change-ii) (unbounded),
> [Combination Sum IV [#377](https://leetcode.com/problems/combination-sum-iv/)](#dynamic-programming/combination-sum-iv) (unbounded, order-sensitive).

---

## 6. Interval DP

**State:** a **range** `(i, j)`. **Transition:** try every **split point** `k` inside the range — an
O(n) loop — so it's typically O(n³) over O(n²) states.

```
dp[i][j] = best over k in (i, j) of ( dp[i][k] + dp[k][j] + cost(i, k, j) )
```

The tell is *"the answer for a range depends on choosing a point/partition inside it."*

- **Burst Balloons** (the split is the *last* balloon to pop — the key reframing)
- **Matrix Chain Multiplication**, **Palindrome Partitioning II**, **Stone Game** variants

**Signature move:** iterate by **increasing interval length**, so shorter ranges are solved first.

> Repo: [Burst Balloons [#312](https://leetcode.com/problems/burst-balloons/)](#dynamic-programming/burst-balloons).

---

## 7. Topological-Order (DAG) DP

**State:** a node. **Transition:** relax over incoming (or outgoing) edges — but only once every
predecessor is final, which a **topological order** guarantees. This is *linearize, then relax.*

```
dp[v] = f( dp[u] for each edge u → v )      // processed in topological order
```

- **Longest path in a DAG** — the critical path (CPM)
- **Course Schedule II** ordering, **Parallel Courses III** (weighted longest path)

**Key fact:** longest path is NP-hard in a general graph but **O(V+E) on a DAG** — acyclicity is
what makes the DP legal. Note that **sequence DPs are secretly this**: string position *is* a
topological order, which is why LIS/Word Break need no explicit sort.

> Repo: [Parallel Courses III [#2050](https://leetcode.com/problems/parallel-courses-iii/)](#graphs/parallel-courses-iii),
> [Course Schedule II [#210](https://leetcode.com/problems/course-schedule-ii/)](#graphs/course-schedule-ii). Guide:
> [Graph Algorithms](#guides/GRAPH_ALGORITHMS).

---

## 8. Tree DP

**State:** a subtree, identified by its root node. **Transition:** combine children's results —
computed in **post-order** (children before parent). A DAG DP where the DAG is a tree.

```
dp[node] = combine( dp[child] for child in children(node) )
```

Often each node carries **two states** — "including this node" vs "excluding it":

- **House Robber III** (rob this node or not)
- **Binary Tree Maximum Path Sum**, **Diameter of a Tree** (the answer "passes through" a node)
- **Tree diameter / re-rooting** techniques

**Signature move:** return a tuple/array per node, and often track a global answer separately from
the value you return upward (the "path through me" vs "path I can extend" distinction).

> Related in repo: tree traversals under `trees/`; the pattern also underlies
> [Lowest Common Ancestor](#trees/lowest-common-ancestor).

---

## 9. Bitmask DP

**State:** a **subset**, encoded as the bits of an integer (often with an extra coordinate like
"current position"). **Transition:** O(n) — add one element to the subset. Feasible only for small
`n` (≤ ~20), since there are 2ⁿ subsets.

```
dp[mask][i] = best way to have visited exactly `mask`, currently at i
```

- **Travelling Salesman** (`dp[mask][i]` = shortest path visiting `mask`, ending at `i`)
- **Assignment problem**, **Shortest Superstring**, partition-into-k-subsets

**Signature move:** the mask *is* a canonical ID for a subset — which is exactly why
[Subsets [#78](https://leetcode.com/problems/subsets/)](#dynamic-programming/subsets) (whose bitmask enumeration is the base case) is the
gateway to this whole family.

> Repo gateway: [Subsets [#78](https://leetcode.com/problems/subsets/)](#dynamic-programming/subsets) (bitmask enumeration of subsets).
> Bit mechanics live in the `bit-manipulation/` category; the mask is just an integer whose
> bits you set and test.

---

## The Complete Problem Map

Every DP problem in this repo, by type. (Canonical LeetCode numbers for problems not yet in the repo
are listed so you can find them.)

### Constant-Transition
| # | Problem | In repo |
|---|---------|---------|
| [198](https://leetcode.com/problems/house-robber/) | [House Robber](#dynamic-programming/house-robber) | ✅ |
| [55](https://leetcode.com/problems/jump-game/) | [Jump Game](#dynamic-programming/jump-game) | ✅ |
| [70](https://leetcode.com/problems/climbing-stairs/) | Climbing Stairs | — |
| [213](https://leetcode.com/problems/house-robber-ii/)/[337](https://leetcode.com/problems/house-robber-iii/) | House Robber II / III | — (III is Tree DP) |

### Grid
| # | Problem | In repo |
|---|---------|---------|
| [62](https://leetcode.com/problems/unique-paths/) | [Unique Paths](#dynamic-programming/unique-paths) | ✅ |
| [1463](https://leetcode.com/problems/cherry-pickup-ii/) | [Cherry Pickup II](#dynamic-programming/cherry-pickup-ii) | ✅ |
| [2304](https://leetcode.com/problems/minimum-path-cost-in-a-grid/) | [Minimum Path Cost in a Grid](#dynamic-programming/minimum-path-cost-in-a-grid) | ✅ |
| [1411](https://leetcode.com/problems/number-of-ways-to-paint-n-3-grid/) | [Number of Ways to Paint Grid](#dynamic-programming/number-of-ways-paint-grid) | ✅ |
| [64](https://leetcode.com/problems/minimum-path-sum/) | Minimum Path Sum | — |
| [221](https://leetcode.com/problems/maximal-square/) | Maximal Square | — |
| [63](https://leetcode.com/problems/unique-paths-ii/) | Unique Paths II | — |

### Dual-Sequence → see [String DP](#guides/STRING_DP)
| # | Problem | In repo |
|---|---------|---------|
| [1092](https://leetcode.com/problems/shortest-common-supersequence/) | [Shortest Common Supersequence](#dynamic-programming/shortest-common-supersequence) | ✅ |
| [1143](https://leetcode.com/problems/longest-common-subsequence/) | Longest Common Subsequence | — |
| [72](https://leetcode.com/problems/edit-distance/) | Edit Distance | — |
| [115](https://leetcode.com/problems/distinct-subsequences/) | Distinct Subsequences | — |
| [10](https://leetcode.com/problems/regular-expression-matching/)/[44](https://leetcode.com/problems/wildcard-matching/) | Regex / Wildcard Matching | — |

### Non-Constant-Transition (the LIS cluster)
| # | Problem | In repo |
|---|---------|---------|
| [300](https://leetcode.com/problems/longest-increasing-subsequence/) | [Longest Increasing Subsequence](#dynamic-programming/longest-increasing-subsequence) | ✅ |
| [368](https://leetcode.com/problems/largest-divisible-subset/) | [Largest Divisible Subset](#dynamic-programming/largest-divisible-subset) | ✅ |
| [673](https://leetcode.com/problems/number-of-longest-increasing-subsequence/) | [Number of LIS](#dynamic-programming/number-of-lis) | ✅ |
| [354](https://leetcode.com/problems/russian-doll-envelopes/) | [Russian Doll Envelopes](#dynamic-programming/russian-doll-envelopes) | ✅ |
| [2407](https://leetcode.com/problems/longest-increasing-subsequence-ii/) | [Longest Increasing Subsequence II](#dynamic-programming/longest-increasing-subsequence-ii) | ✅ |
| [139](https://leetcode.com/problems/word-break/) | [Word Break](#dynamic-programming/word-break) | ✅ |
| [140](https://leetcode.com/problems/word-break-ii/) | [Word Break II](#dynamic-programming/word-break-ii) | ✅ |

### Knapsack → see [Knapsack Variants](#guides/KNAPSACK_VARIANTS)
| # | Problem | Variant | In repo |
|---|---------|---------|---------|
| [416](https://leetcode.com/problems/partition-equal-subset-sum/) | [Partition Equal Subset Sum](#dynamic-programming/partition-equal-subset-sum) | weight-only | ✅ |
| [322](https://leetcode.com/problems/coin-change/) | [Coin Change](#dynamic-programming/coin-change) | unbounded (min) | ✅ |
| [518](https://leetcode.com/problems/coin-change-ii/) | [Coin Change II](#dynamic-programming/coin-change-ii) | unbounded (count, combos) | ✅ |
| [377](https://leetcode.com/problems/combination-sum-iv/) | [Combination Sum IV](#dynamic-programming/combination-sum-iv) | unbounded (count, perms) | ✅ |
| [3592](https://leetcode.com/problems/inverse-coin-change/) | [Inverse Coin Change](#dynamic-programming/inverse-coin-change) | inverts the count DP | ✅ |
| [494](https://leetcode.com/problems/target-sum/) | Target Sum | weight-only (signed) | — |
| [1049](https://leetcode.com/problems/last-stone-weight-ii/) | Last Stone Weight II | 0/1 | — |
| [474](https://leetcode.com/problems/ones-and-zeroes/) | Ones and Zeroes | 2-D knapsack | — |

### Interval
| # | Problem | In repo |
|---|---------|---------|
| [312](https://leetcode.com/problems/burst-balloons/) | [Burst Balloons](#dynamic-programming/burst-balloons) | ✅ |
| [1039](https://leetcode.com/problems/minimum-score-triangulation-of-polygon/) | Minimum Score Triangulation | — |
| [132](https://leetcode.com/problems/palindrome-partitioning-ii/) | Palindrome Partitioning II | — |
| [375](https://leetcode.com/problems/guess-number-higher-or-lower-ii/) | Guess Number Higher or Lower II | — |
| [1000](https://leetcode.com/problems/minimum-cost-to-merge-stones/) | Merge Stones | — |

### Topological-Order (DAG)
| # | Problem | In repo |
|---|---------|---------|
| [2050](https://leetcode.com/problems/parallel-courses-iii/) | [Parallel Courses III](#graphs/parallel-courses-iii) | ✅ |
| [210](https://leetcode.com/problems/course-schedule-ii/) | [Course Schedule II](#graphs/course-schedule-ii) | ✅ |
| [329](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/) | Longest Increasing Path in a Matrix | — |
| [1857](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) | Largest Color Value in a Directed Graph | — |

### Tree
| # | Problem | In repo |
|---|---------|---------|
| [337](https://leetcode.com/problems/house-robber-iii/) | House Robber III | — |
| [124](https://leetcode.com/problems/binary-tree-maximum-path-sum/) | Binary Tree Maximum Path Sum | — |
| [543](https://leetcode.com/problems/diameter-of-binary-tree/) | Diameter of Binary Tree | — |
| [968](https://leetcode.com/problems/binary-tree-cameras/) | Binary Tree Cameras | — |
| [834](https://leetcode.com/problems/sum-of-distances-in-tree/) | Sum of Distances in Tree (re-rooting) | — |

### Bitmask
| # | Problem | In repo |
|---|---------|---------|
| [78](https://leetcode.com/problems/subsets/) | [Subsets](#dynamic-programming/subsets) (enumeration base case) | ✅ |
| [943](https://leetcode.com/problems/find-the-shortest-superstring/) | Find the Shortest Superstring | — |
| [847](https://leetcode.com/problems/shortest-path-visiting-all-nodes/) | Shortest Path Visiting All Nodes | — |
| [1349](https://leetcode.com/problems/maximum-students-taking-exam/) | Maximum Students Taking Exam | — |
| [698](https://leetcode.com/problems/partition-to-k-equal-sum-subsets/) | Partition to K Equal Sum Subsets | — |

---

## How to Classify a New DP Problem

A quick decision procedure — ask in order:

1. **Is `n` tiny (≤ ~20) and about subsets/orderings?** → **Bitmask DP.**
2. **Is the data a tree?** → **Tree DP** (post-order, combine children).
3. **Is it a DAG / precedence / ordering?** → **Topological-order DP.**
4. **Does "answer for a range" need a split point inside it?** → **Interval DP.**
5. **Is it "pick items under a capacity"?** → **Knapsack** (then ask: once / unlimited / bounded?).
6. **Are there two sequences aligned position-by-position?** → **Dual-sequence DP.**
7. **Is it a grid with cell-to-neighbour moves?** → **Grid DP.**
8. **One index, transition loops over earlier states?** → **Non-constant-transition** (then look for
   an acceleration).
9. **One index, O(1) fixed recurrence?** → **Constant-transition** (and O(1) space).

Then size it: **states × transition cost**. If that product is too big, the optimisation you need is
almost always *"shrink the transition"* (accelerate the inner loop) or *"shrink the state"* (drop a
dimension via rolling array).

---

## Related Guides

- **[Knapsack Variants](#guides/KNAPSACK_VARIANTS)** — the 6 knapsack scenarios and the loop-order rule.
- **[String DP](#guides/STRING_DP)** — LCS, edit distance, and the dual-sequence family in depth.
- **[Intervals](#guides/INTERVALS)** — the sweep/merge patterns that interval *problems* (distinct
  from interval *DP*) use.
- **[Greedy vs DP](#guides/GREEDY_VS_DP)** — when a greedy beats a DP, and how to tell.
- **[Segment Tree](#guides/SEGMENT_TREE)** — the structure that accelerates non-constant transitions.
