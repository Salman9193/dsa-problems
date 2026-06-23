# Valid Palindrome — Notes & Intuition

**LeetCode #125** | Strings / Two Pointers | Easy

---

## Problem

Given a string `s`, return `true` if it is a palindrome considering only
alphanumeric characters and ignoring case.

```
Input:  "A man, a plan, a canal: Panama"  →  true
Input:  "race a car"                       →  false
Input:  " "                                →  true
```

---

## Approach 1 — Filter + Reverse (O(n) space)

Build a cleaned string, check if it equals its reverse.

```java
StringBuilder sb = new StringBuilder();
for (char c : s.toCharArray())
    if (Character.isLetterOrDigit(c))
        sb.append(Character.toLowerCase(c));
String clean = sb.toString();
return clean.equals(new StringBuilder(clean).reverse().toString());
```

Simple but allocates O(n) for the cleaned string and its reverse.

---

## Approach 2 — Two Pointers (O(1) space) ← Optimal

```java
int left = 0, right = s.length() - 1;
while (left < right) {
    while (left < right && !Character.isLetterOrDigit(s.charAt(left)))  left++;
    while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;
    if (Character.toLowerCase(s.charAt(left)) !=
        Character.toLowerCase(s.charAt(right))) return false;
    left++; right--;
}
return true;
```

No extra allocation — operates directly on the original string.

---

## Why Two Pointers Works

A palindrome reads identically from both ends. Two pointers mirror
that definition exactly:

```
"A man, a plan, a canal: Panama"
 ↑                             ↑
left                         right

Step 1: 'A' vs 'a' → equal (case-insensitive) → move inward
Step 2: skip ' ' from left → 'm' vs 'm' → equal
...
```

The inner while-loops skip non-alphanumeric characters inline —
no preprocessing, no extra allocation.

---

## Full Trace — `"A man, a plan, a canal: Panama"`

| left | left char | right | right char | compare |
|------|-----------|-------|------------|---------|
| 0 | A | 29 | a | a==a ✓ |
| 2 | m | 27 | m | m==m ✓ |
| 3 | a | 26 | a | a==a ✓ |
| 4 | n | 25 | n | n==n ✓ |
| 6 | a | 23 | a | a==a ✓ |
| 8 | p | 21 | p | p==p ✓ |
| 9 | l | 20 | l | l==l ✓ |
| 10 | a | 19 | a | a==a ✓ |
| 11 | n | 18 | n | n==n ✓ |
| 13 | a | 16 | a | a==a ✓ |
| left >= right → return true ✓ |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `" "` | `true` | Only spaces → no valid chars → vacuously palindrome |
| `".,"` | `true` | Only punctuation → empty after filter |
| `"0P"` | `false` | '0' != 'p' |
| `"a"` | `true` | Single char is always palindrome |
| `"aA"` | `true` | Case-insensitive: 'a' == 'a' |
| `"ab"` | `false` | 'a' != 'b' |

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Filter + Reverse | O(n) | O(n) |
| Two Pointers | O(n) | O(1) |

---

## Connection to Other Problems

The two-pointer technique generalises naturally:

| Problem | Two pointer usage |
|---------|-----------------|
| Valid Palindrome (#125) | Both ends inward, skip invalid chars |
| Palindrome II (#680) | Allow one deletion — branch on mismatch |
| Longest Palindromic Substring (#5) | Expand from center outward |
| Reverse String (#344) | Swap from both ends inward |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Allow one deletion (#680) | Return true if palindrome after ≤ 1 delete | Two pointers; on mismatch try both skip-left and skip-right |
| Count min deletions | Minimum chars to remove | `n - LPS` where LPS = longest palindromic subsequence |
| Case-sensitive | Don't ignore case | Remove `toLowerCase()` call |
| Unicode strings | Emoji, non-ASCII | Use `Character.isLetterOrDigit()` — handles Unicode automatically |
| Valid palindrome after one replacement | Change one char | Two pointers + try both characters on mismatch |
| Shortest palindrome by insertion | Min insertions to make palindrome | `n - LPS` insertions needed |

**The #680 extension is critical:** On mismatch at `(left, right)`, try validating `(left+1, right)` and `(left, right-1)`. If either is a palindrome, return true. The helper function must not recurse — only one deletion allowed.

**Depth of extensions:** Valid Palindrome (#125) → Allow 1 deletion (#680) → Min deletions (LPS DP) → Palindrome partitioning (#131). Each extension requires a stronger algorithmic tool: two pointers → greedy → DP → backtracking.
