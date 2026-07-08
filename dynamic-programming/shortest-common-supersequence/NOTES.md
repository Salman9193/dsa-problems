# Shortest Common Supersequence — Notes & Intuition

**LeetCode #1092** | Dynamic Programming (LCS) | Hard
Constraints: 1 ≤ len(str1), len(str2) ≤ 1000, lowercase letters.

---

## Problem

Return the shortest string that has **both** `str1` and `str2` as subsequences. If
several are valid, return any.

```
Input:  str1 = "abac", str2 = "cab"
Output: "cabac"          ("cab" and "abac" are both subsequences)
```

---

## The Key Insight — It's LCS in Disguise

To merge two strings into the shortest string containing both, **share as much as
possible**. The maximal shareable part is exactly their **Longest Common Subsequence
(LCS)**: every LCS character is written **once** (both strings reuse it), and
everything else from each string is written separately.

That gives the length immediately:

```
len(SCS) = len(str1) + len(str2) − len(LCS)
```

You'd emit all `m + n` characters, but each of the `LCS` shared characters was
double-counted, so subtract it once.

For `str1="abac"`, `str2="cab"`: LCS is `"ab"` (length 2), so
`len(SCS) = 4 + 3 − 2 = 5` → `"cabac"`.

---

## Step 1 — LCS DP Table

Standard LCS: `dp[i][j]` = length of the LCS of `str1[0..i-1]` and `str2[0..j-1]`.

```java
if (str1[i-1] == str2[j-1])  dp[i][j] = dp[i-1][j-1] + 1;
else                          dp[i][j] = max(dp[i-1][j], dp[i][j-1]);
```

---

## Step 2 — Reconstruct by Walking the Table Back

The length is the easy part; the work is building an actual SCS. Walk from
`(m, n)` back to `(0, 0)`, emitting characters (result comes out reversed):

- **Match** `str1[i-1] == str2[j-1]` → a shared (LCS) char: emit it **once**, step
  diagonally `(i-1, j-1)`.
- **Otherwise** move the way the LCS came from and emit the character you step over —
  it's unique to one string but still must appear:
  - if `dp[i-1][j] ≥ dp[i][j-1]` → emit `str1[i-1]`, step up `(i-1, j)`
  - else → emit `str2[j-1]`, step left `(i, j-1)`
- When one string is exhausted, **flush the remainder** of the other.
- **Reverse** the emitted string.

```java
StringBuilder sb = new StringBuilder();
int i = m, j = n;
while (i > 0 && j > 0) {
    if (str1.charAt(i-1) == str2.charAt(j-1)) { sb.append(str1.charAt(i-1)); i--; j--; }
    else if (dp[i-1][j] >= dp[i][j-1])         { sb.append(str1.charAt(i-1)); i--; }
    else                                        { sb.append(str2.charAt(j-1)); j--; }
}
while (i > 0) sb.append(str1.charAt(--i));
while (j > 0) sb.append(str2.charAt(--j));
return sb.reverse().toString();
```

---

## Full Trace — str1="abac", str2="cab"

LCS table (`dp[i][j]`), rows = str1 `a b a c`, cols = str2 `c a b`:

| | "" | c | a | b |
|---|----|---|---|---|
| **""** | 0 | 0 | 0 | 0 |
| **a** | 0 | 0 | 1 | 1 |
| **b** | 0 | 0 | 1 | 2 |
| **a** | 0 | 0 | 1 | 2 |
| **c** | 0 | 1 | 1 | 2 |

`dp[4][3] = 2` → LCS length 2 (`"ab"`), so SCS length = 4 + 3 − 2 = 5.

Backtrack from `(4,3)`:

| Step | i,j | compare | action | emit |
|------|-----|---------|--------|------|
| 1 | 4,3 | c ≠ b, dp[3][3]=2 ≥ dp[4][2]=1 | up (str1) | c |
| 2 | 3,3 | a ≠ b, dp[2][3]=2 ≥ dp[3][2]=1 | up (str1) | a |
| 3 | 2,3 | b = b | diagonal (shared) | b |
| 4 | 1,2 | a = a | diagonal (shared) | a |
| 5 | 0,1 | str1 done, flush str2 | left | c |

Emitted `"cabac"` → reversed → **`"cabac"`** (a palindrome here). ✓

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| identical strings | the string itself | LCS = whole string; nothing extra to add |
| no common characters | `str1 + str2` | LCS = 0; nothing shared, concatenate |
| one is a subsequence of the other | the longer string | it already contains both |

---

## Complexity

| | Time | Space |
|--|------|-------|
| Length only | O(m·n) | O(n) rolling row |
| With reconstruction | O(m·n) | O(m·n) — **full table needed to backtrack** |

Unlike computing LCS *length* (which can use two rows), reconstructing the string
requires the whole table to walk back through, so space stays O(m·n).

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| SCS length only | don't rebuild the string | `m + n − LCS`, O(n) space |
| SCS of k strings | more than two | NP-hard in general (unlike the 2-string case) |
| Longest Common Supersequence | trivial | unbounded — just interleave with padding; not meaningful |
| Shortest Common Superstring | substrings, not subsequences | different, NP-hard (relates to assembly / TSP) |
| Count distinct SCS | how many shortest ones | DP over the same table counting equal-cost branches |
| Edit distance / diff | related metric | LCS drives diff; SCS is the "union" of two sequences |

**The through-line:** SCS, LCS, and edit distance are the same DP family — a grid over
two sequences where you match diagonally or skip along an axis. Master the LCS table
and its backtrack, and this whole family follows.
