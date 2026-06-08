# Longest Palindromic Substring — Notes & Intuition

**LeetCode #5** | Strings | Medium

---

## Problem

Given a string `s`, return the longest substring that reads the same
forward and backward.

```
Input:  "babad"   Output: "bab"   (or "aba" — both valid)
Input:  "cbbd"    Output: "bb"
Input:  "a"       Output: "a"
```

---

## Approaches Compared

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute force (check all substrings) | O(n³) | O(1) | Too slow |
| Dynamic programming | O(n²) | O(n²) | dp[i][j] table |
| **Expand around center** | **O(n²)** | **O(1)** | ✓ Best practical |
| Manacher's algorithm | O(n) | O(n) | Optimal, complex |

---

## Core Insight — Every Palindrome Has a Center

Instead of checking all substrings, observe that every palindrome is
symmetric around a center point. Expand outward from that center as long
as characters on both sides match.

**Two types of centers:**

```
Odd-length:   a b a       → center is the character 'b' at index i
Even-length:  a b b a     → center is the gap between the two 'b's
```

For a string of length n, there are `2n - 1` possible centers:
- n single characters (odd-length palindromes)
- n-1 gaps between consecutive characters (even-length palindromes)

---

## The expand() Helper

```java
private int expand(String s, int left, int right) {
    while (left >= 0 && right < s.length()
           && s.charAt(left) == s.charAt(right)) {
        left--;
        right++;
    }
    return right - left - 1;
}
```

- Called with `(i, i)` for odd-length centers.
- Called with `(i, i+1)` for even-length centers.
- When the loop exits, `left` and `right` are **one step past** the
  palindrome boundary, so the length is `right - left - 1`.

**Example:** `s = "racecar"`, center at index 3 ('e')

```
Step 0: left=3, right=3  → s[3]=s[3]='e'  ✓ expand
Step 1: left=2, right=4  → s[2]=s[4]='c'  ✓ expand
Step 2: left=1, right=5  → s[1]=s[5]='a'  ✓ expand
Step 3: left=0, right=6  → s[0]=s[6]='r'  ✓ expand
Step 4: left=-1, right=7 → out of bounds, stop
length = 7 - (-1) - 1 = 7  ✓
```

---

## Recovering the Start Index

After expand returns `len` for center at index `i`:

```java
start = i - (len - 1) / 2;
```

| len | center i | start | substring |
|-----|----------|-------|-----------|
| 3 (odd) | 2 | 2 - 1 = 1 | s[1..3] |
| 4 (even) | 2 | 2 - 1 = 1 | s[1..4] |
| 7 (odd) | 3 | 3 - 3 = 0 | s[0..6] |

---

## Full Trace — "babad"

| i | char | odd len | even len | max len | action |
|---|------|---------|----------|---------|--------|
| 0 | b | 1 | 1 | 1 | start=0 |
| 1 | a | 3 | 1 | 3 | start=0, maxLen=3 → **"bab"** |
| 2 | b | 3 | 1 | 3 | same length, no update |
| 3 | a | 1 | 1 | 3 | no update |
| 4 | d | 1 | 1 | 3 | no update |

Result: `"bab"`

---

## Manacher's Algorithm (O(n)) — Key Idea

Manacher avoids re-expanding palindromes we've already seen by maintaining
a "rightmost palindrome boundary" R and its center C.

For any new center i within R, we can mirror it: its palindrome radius is
**at least** the radius of its mirror across C — no need to start from 0.

- Original paper: Manacher, G. — *A new linear-time "on-line" algorithm
  for finding the smallest initial palindrome of a string*,
  J. ACM 22:346–351, 1975.
- Reference implementation: https://cp-algorithms.com/string/manacher.html

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `"a"` | `"a"` | Single char is always a palindrome |
| `"ac"` | `"a"` | No palindrome longer than 1 |
| `"aacabdkacaa"` | `"aca"` | Multiple candidates, pick longest |
| `"aaaa"` | `"aaaa"` | Entire string is palindrome |
