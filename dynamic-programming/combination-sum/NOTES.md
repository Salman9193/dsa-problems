# Combination Sum — Notes & Intuition

**LeetCode #39** | Backtracking | Medium

---

## Problem

Find all combinations of candidates (reusable, distinct) summing to target.

```
candidates=[2,3,6,7], target=7 → [[2,2,3],[7]]
candidates=[2,3], target=8 → [[2,2,2,2],[2,3,3],[3,5]...wait 5 not in candidates]
```

---

## Key Decisions

**Reuse:** pass same index `i` (not `i+1`) — can pick same candidate again.
**No duplicates in output:** `start` index prevents re-picking earlier candidates.
**Pruning:** sort candidates; if `candidates[i] > remaining`, break.

```
backtrack(remaining, start):
    if remaining == 0: record; return
    for i from start to n-1:
        if candidates[i] > remaining: BREAK  ← pruning
        pick candidates[i]
        backtrack(remaining - candidates[i], i)  ← i, not i+1
        unpick
```

---

## Variants

| Problem | Change from #39 |
|---------|----------------|
| #39 Combination Sum | Candidates reusable, no duplicates in candidates |
| #40 Combination Sum II | Candidates NOT reusable, may have duplicates → sort + skip |
| #216 Combination Sum III | Fixed k picks from 1-9 |

---

## Complexity

Time O(n^(t/min)) · Space O(t/min) stack depth

---

## DP Tabulation Alternative — and When It Wins

You *can* rewrite this as a DP over sub-targets: `dp[i]` = every combination summing to
`i`, built by looping **candidates on the outside** — that ordering is what avoids
duplicates (the same trick as *Coin Change II*, #518):

```java
// dp[0..target], dp[0] = [[]]  (one empty combination for sum 0)
for (int c : candidates)
    for (int i = c; i <= target; i++)
        for (List<Integer> sub : dp.get(i - c))
            dp.get(i).add(prepend(c, sub));   // copy sub, add c
return dp.get(target);
```

It is **correct**, but **not more efficient** for enumeration:

- The output is inherently **exponential**, so both approaches pay `O(output)`.
- This DP **materializes every combination for every sub-target** and copies each sub-list
  on every extension → usually *more* memory than backtracking, which keeps a single path
  (`O(depth)` extra space) and copies only completed combinations.

> **Rule of thumb:** for *enumeration / generation*, backtracking is typically optimal
> (output-sensitive) — memoization can't compress an exponential output. DP tabulation wins
> only when you can replace the lists with a **scalar**.

**When DP genuinely wins — drop the lists, keep a number:**

| You need | Store per amount | Result |
|----------|------------------|--------|
| **Count** of combinations (Coin Change II, #518) | an `int` | O(n·target) time, O(target) space |
| **Fewest** coins ([Coin Change #322](#dynamic-programming/coin-change)) | a min `int` | O(n·target) time, O(target) space |

```java
// counting variant — same candidate-outer loop, O(target) space
int[] dp = new int[target + 1];
dp[0] = 1;
for (int c : candidates)
    for (int i = c; i <= target; i++)
        dp[i] += dp[i - c];
return dp[target];              // number of combinations summing to target
```

Same recurrence, same candidate-outer trick — the only change is storing a *count* instead
of every list. That's exactly why [`coin-change` (#322)](#dynamic-programming/coin-change) is
a DP problem while this one is backtracking: one asks for a compressible count/min, the
other for the full (exponential) enumeration.
