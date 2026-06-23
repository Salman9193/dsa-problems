# Longest Substring Without Repeating Characters — Notes & Intuition

**LeetCode #3** | Strings / Sliding Window | Medium

---

## Problem

Given a string `s`, find the length of the longest substring that contains
no repeating characters.

```
Input:  "abcabcbb"  →  3  ("abc")
Input:  "bbbbb"     →  1  ("b")
Input:  "pwwkew"    →  3  ("wke")
```

---

## Why Brute Force Fails

Check every substring, verify uniqueness with a HashSet → O(n³).
Even the O(n²) variant (one loop + HashSet per starting position) is
too slow for n = 50,000.

---

## Sliding Window Insight

Maintain a window `[left, right]` that always contains unique characters.
Expand `right` one step at a time. If `s[right]` is already in the window,
shrink from `left` until the duplicate is removed.

The naive shrinking approach is O(n²) — we'd move `left` one step at a time.

**The O(n) improvement:** instead of shrinking one step at a time, jump
`left` directly to `lastSeen[c] + 1` — the position just after the previous
occurrence of the duplicate character.

```java
Map<Character, Integer> lastSeen = new HashMap<>();
int left = 0, maxLen = 0;

for (int right = 0; right < s.length(); right++) {
    char c = s.charAt(right);
    if (lastSeen.containsKey(c) && lastSeen.get(c) >= left)
        left = lastSeen.get(c) + 1;
    lastSeen.put(c, right);
    maxLen = Math.max(maxLen, right - left + 1);
}
```

---

## Why `lastSeen.get(c) >= left` Is Critical

The map retains positions from previous windows. Without the `>= left`
check, we might move `left` backward for a character no longer in the window.

```
s = "abba"
         left=2, right=3 ('a')
         lastSeen['a'] = 0

Without check: left = 0+1 = 1  ← WRONG (moves left backward)
With check:    0 >= 2? NO → left stays at 2  ✓
```

The check ensures we never move `left` backward — the window only shrinks
from the right (equivalent to moving left forward).

---

## Why This Is O(n)

- `right` visits each character exactly once: n steps total.
- `left` only ever moves rightward: at most n steps total.
- Both pointers together: at most 2n steps → O(n).

The direct jump (`left = lastSeen[c] + 1`) is what makes this O(n) instead
of O(n²) — no inner loop needed.

---

## Full Trace — `"abcabcbb"`

| right | char | lastSeen[c] | left (before) | left (after) | window | maxLen |
|-------|------|-------------|---------------|--------------|--------|--------|
| 0 | a | — | 0 | 0 | "a" | 1 |
| 1 | b | — | 0 | 0 | "ab" | 2 |
| 2 | c | — | 0 | 0 | "abc" | 3 |
| 3 | a | 0 ≥ 0 | 0 | 1 | "bca" | 3 |
| 4 | b | 1 ≥ 1 | 1 | 2 | "cab" | 3 |
| 5 | c | 2 ≥ 2 | 2 | 3 | "abc" | 3 |
| 6 | b | 4 ≥ 3 | 3 | 5 | "cb" | 3 |
| 7 | b | 6 ≥ 5 | 5 | 7 | "b" | 3 |

Result: `3` ✓

---

## Complexity

| | |
|--|--|
| Time | O(n) — two pointers, each moves at most n steps |
| Space | O(min(n, \|charset\|)) — map bounded by character set size |

---

## Sliding Window Variants

The same pattern generalises to related problems:

| Problem | Constraint | Modification |
|---------|-----------|--------------|
| #3 (this) | No repeating chars | Shrink when any char repeats |
| #340 At most k distinct | ≤ k distinct chars | Shrink when distinct count > k |
| #159 At most 2 distinct | ≤ 2 distinct chars | k=2 special case |
| #76 Minimum Window Substring | Contains all of T | Shrink while still valid |
| #438 Find Anagrams | Fixed freq pattern | Fixed-size window + freq map |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `""` | `0` | Empty string |
| `"a"` | `1` | Single character |
| `"au"` | `2` | Two distinct chars |
| `"dvdf"` | `3` | "vdf" — jump skips over 'd' correctly |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| At most k distinct characters (#340) | Allow up to k different characters | Same sliding window; shrink when distinct count > k |
| At most 2 distinct (#159) | Special case k=2 | Same, k=2 |
| Longest substring with all unique + k replacements (#424) | Allow k replacements | Window invalid when length - maxFreq > k |
| All substrings of length k with no repeats | Fixed window, check uniqueness | Sliding window of fixed size; use HashSet |
| Minimum window with all unique (#3 reversed) | Shortest version | Not standard; binary search on length + check |
| Streaming characters | Characters arrive one at a time | Same lastSeen map; O(1) per character |
| Unicode / emoji | Non-ASCII characters | Use `HashMap<Integer,Integer>` keyed by codepoint |

**The sliding window family progression:**
```
#3  No repeats:            shrink when ANY char repeats
#159 At most 2 distinct:   shrink when DISTINCT COUNT > 2
#340 At most k distinct:   shrink when DISTINCT COUNT > k
#424 k replacements:       shrink when LENGTH - MAX_FREQ > k
#76  Min window substring: shrink while ALL REQUIRED CHARS SATISFIED
```
Each step adds one more dimension of constraint to the shrink condition.
