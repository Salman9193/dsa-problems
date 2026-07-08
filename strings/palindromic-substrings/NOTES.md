# Palindromic Substrings — Notes & Intuition

**LeetCode #647** | Strings | Medium

---

## Problem

Given a string `s`, return the **number** of palindromic substrings in it.
A substring is a contiguous run of characters, and it's palindromic if it
reads the same forward and backward. Single characters count.

```
Input:  "abc"   Output: 3    ("a", "b", "c")
Input:  "aaa"   Output: 6    ("a","a","a", "aa","aa", "aaa")
```

This is the **counting** sibling of *Longest Palindromic Substring* (#5):
same machinery, but we tally every palindrome instead of tracking the
longest one.

---

## Approaches Compared

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute force (check all substrings) | O(n³) | O(1) | n² substrings × O(n) check |
| Dynamic programming | O(n²) | O(n²) | dp[i][j] palindrome table |
| **Expand around center** | **O(n²)** | **O(1)** | ✓ Best practical |
| Manacher's algorithm | O(n) | O(n) | Optimal, complex |

---

## Core Insight — Every Palindrome Has a Center

Rather than examine all O(n²) substrings, use the fact that every palindrome
is symmetric around a center. Expand outward from each center while the
characters on both sides match.

**Two types of centers:**

```
Odd-length:   a b a       → center is the character 'b' at index i
Even-length:  a b b a     → center is the gap between the two 'b's
```

For a string of length n there are `2n - 1` centers:
- n single characters (odd-length palindromes)
- n-1 gaps between consecutive characters (even-length palindromes)

---

## The Key Difference From "Longest"

In *Longest Palindromic Substring*, each center contributes **one** value —
the length of its longest palindrome. Here, the elegant observation is:

> **Every successful expansion step *is* another palindrome.**

When you expand from a center and the two ends match, you've just discovered
one more distinct palindrome centered there. So you don't compute a length
and convert it — you simply **count each matching expansion**.

```java
private int countExpand(String s, int left, int right) {
    int count = 0;
    while (left >= 0 && right < s.length()
           && s.charAt(left) == s.charAt(right)) {
        count++;        // this matched pair is a new palindrome
        left--;
        right++;
    }
    return count;
}
```

- Called with `(i, i)` for odd-length centers.
- Called with `(i, i+1)` for even-length centers.
- The returned `count` is exactly the number of palindromes centered there.

The main loop just sums both center types over every index:

```java
for (int i = 0; i < s.length(); i++) {
    total += countExpand(s, i, i);      // odd-length
    total += countExpand(s, i, i + 1);  // even-length
}
```

---

## Full Trace — "aaa"

| i | char | odd count | even count | running total | palindromes found |
|---|------|-----------|------------|---------------|-------------------|
| 0 | a | 1 | 1 | 2 | "a"@0, "aa"@0‑1 |
| 1 | a | 2 | 1 | 5 | "a"@1, "aaa"@0‑2, "aa"@1‑2 |
| 2 | a | 1 | 0 | 6 | "a"@2 |

Result: **6** — matching `"a","a","a","aa","aa","aaa"`.

At `i=1` the odd expansion matches twice: first `s[1]` alone, then `s[0]==s[2]`
extends it to `"aaa"` — two palindromes from one center, which is exactly the
"count each expansion" idea.

---

## DP Alternative (O(n²) space)

Define `dp[i][j] = true` if `s[i..j]` is a palindrome:

```
dp[i][j] = (s[i] == s[j]) && (j - i < 2 || dp[i+1][j-1])
```

Fill by increasing substring length (so `dp[i+1][j-1]` is ready), and count
every `true`. Same O(n²) time, but O(n²) space — expand-around-center gives
the same time with O(1) space, so it's preferred unless you already need the
table for a related query.

---

## Manacher's Algorithm (O(n)) — Key Idea

Manacher computes, for every center, the radius of its longest palindrome in
linear time by reusing a "rightmost palindrome boundary" R and its center C:
a new center inside R inherits at least the radius of its mirror across C, so
we skip redundant re-expansion.

For **counting**, the number of palindromes centered at a position equals its
palindrome radius (in the transformed string), so the answer is the **sum of
radii** — still O(n).

- Original paper: Manacher, G. — *A new linear-time "on-line" algorithm for
  finding the smallest initial palindrome of a string*, J. ACM 22:346–351, 1975.
- Reference implementation: https://cp-algorithms.com/string/manacher.html

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `"a"` | 1 | Single char is a palindrome |
| `"ab"` | 2 | Only the two single chars |
| `"aaa"` | 6 | 3 singles + 2 "aa" + 1 "aaa" |
| `""` | 0 | No substrings |
| `"abba"` | 6 | 4 singles + "bb" + "abba" |

A string of n identical characters yields `n(n+1)/2` palindromic substrings —
the maximum possible — a good sanity check for the counting logic.

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Longest palindromic substring (#5) | Track longest, not count | Same expand-from-center; keep max length |
| Longest palindromic subsequence (#516) | Subsequence, not substring | DP: `dp[i][j] = dp[i+1][j-1] + 2` if match |
| Palindrome partitioning (#131) | Enumerate palindrome splits | DP + backtracking |
| Palindrome partitioning II (#132) | Min cuts to split into palindromes | DP on cuts + palindrome table |
| Count distinct palindromic substrings | Dedupe identical palindromes | Palindromic tree (eertree) / suffix automaton |
| Count in O(n) | Linear instead of O(n²) | Manacher: sum of palindrome radii |

**Theoretical note:** the *palindromic tree* (eertree) counts even **distinct**
palindromic substrings in O(n), a stronger result than #647's total count —
it exploits the fact that a string of length n has at most n distinct
palindromic substrings.
