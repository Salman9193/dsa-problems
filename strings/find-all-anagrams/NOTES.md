# Find All Anagrams in a String — Notes & Intuition

**LeetCode #438** | Strings / Sliding Window | Medium

---

## Problem

Given strings `s` and `p`, return all starting indices where an anagram
of `p` begins in `s`.

```
s="cbaebabacd", p="abc"  →  [0, 6]
s="abab",       p="ab"   →  [0, 1, 2]
```

---

## Core Insight

Two strings are anagrams iff they have identical character frequency maps.
Since we need a window of exactly `|p|` characters, this is a **fixed-size
sliding window** — add one char on the right, remove one on the left,
check if frequencies match.

---

## Approach 1 — Satisfied Counter (Cleaner Logic)

Track `satisfied` = number of distinct character types where
`have[c] >= need[c]`. Window is a valid anagram when `satisfied == distinct`.

```java
for (int right = 0; right < s.length(); right++) {
    int rc = s.charAt(right) - 'a';
    have[rc]++;
    if (need[rc] > 0 && have[rc] == need[rc]) satisfied++;

    if (right - left + 1 > p.length()) {    // shrink if too large
        int lc = s.charAt(left) - 'a';
        if (need[lc] > 0 && have[lc] == need[lc]) satisfied--;
        have[lc]--;
        left++;
    }

    if (right - left + 1 == p.length() && satisfied == distinct)
        result.add(left);
}
```

---

## Approach 2 — Direct Array Comparison (Simpler Code)

```java
// Fill initial window
for (int i = 0; i < m; i++) wCount[s.charAt(i) - 'a']++;
if (Arrays.equals(pCount, wCount)) result.add(0);

// Slide window
for (int i = m; i < n; i++) {
    wCount[s.charAt(i) - 'a']++;        // add right
    wCount[s.charAt(i - m) - 'a']--;    // remove left
    if (Arrays.equals(pCount, wCount)) result.add(i - m + 1);
}
```

`Arrays.equals` on `int[26]` = 26 comparisons = O(1) constant.

---

## Full Trace — `s="cbaebabacd"`, `p="abc"`

`need: a=1, b=1, c=1, distinct=3`

| right | char | action | satisfied | window | result |
|-------|------|--------|-----------|--------|--------|
| 0 | c | have[c]=1==need | 1 | "c" | — |
| 1 | b | have[b]=1==need | 2 | "cb" | — |
| 2 | a | have[a]=1==need | **3** | "cba" | **add 0** |
| 3 | e | shrink: remove c→satisfied=2; add e | 2 | "bae" | — |
| ... | | | | | |
| 8 | a | window "bac" at left=6 | **3** | "bac" | **add 6** |

Output: `[0, 6]` ✓

---

## Sliding Window Family

| Problem | Window | Invalid when | Fixed? |
|---------|--------|-------------|--------|
| #3 No repeat chars | variable | any char repeats | No |
| #76 Min window substring | variable | missing a required char | No |
| **#438 Find all anagrams** | **fixed = \|p\|** | **freq mismatch** | **Yes** |
| #567 Permutation in string | fixed = \|p\| | freq mismatch | Yes |

#438 and #567 are identical — one returns all indices, one returns bool.

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Satisfied counter | O(n) | O(1) |
| Direct array compare | O(n × 26) = O(n) | O(1) |

---

## Edge Cases

| s | p | Output | Reason |
|---|---|--------|--------|
| `"a"` | `"a"` | `[0]` | Single char match |
| `"aa"` | `"bb"` | `[]` | Different chars |
| `"ab"` | `"ab"` | `[0]` | Exact match |
| `s.length() < p.length()` | — | `[]` | Window can't fit |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Permutation in String (#567) | Return bool, not indices | Same algorithm; return `satisfied == distinct` |
| Find anagrams in stream | s arrives character by character | Maintained sliding window; O(1) per new char |
| Wildcard anagram | p contains `*` matching any char | Modify need[] to track wildcard budget |
| Overlapping anagrams | Count overlapping occurrences | Already handled — every valid window is reported |
| Multiple patterns | Find anagrams of any pattern in set | Aho-Corasick automaton for multi-pattern matching |
| Sorted anagram detection | Characters need not be contiguous | Subsequence anagram — different problem, DP-based |

**Aho-Corasick for multi-pattern:** When searching for anagrams of k patterns simultaneously, building a trie of all patterns and running Aho-Corasick gives O(n + total_pattern_length + matches) — far better than running the sliding window k times.

**Alphabet generalisation:**
- DNA sequences: alphabet size 4 (A/T/G/C) → `int[4]`
- ASCII text: alphabet size 128 → `int[128]`
- Network payloads: alphabet size 256 → `int[256]`
- Unicode: arbitrary → `HashMap<Integer, Integer>`

The satisfied counter approach works identically for any alphabet; only the array size changes.
