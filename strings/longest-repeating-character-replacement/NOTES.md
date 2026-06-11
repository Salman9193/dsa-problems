# Longest Repeating Character Replacement — Notes & Intuition

**LeetCode #424** | Strings / Sliding Window | Medium

---

## Problem

Given a string `s` and integer `k`, you can replace at most `k` characters
with any letter. Return the length of the longest substring containing
the same letter after performing at most `k` replacements.

```
Input:  s = "ABAB", k = 2   →  4  (replace both A's or both B's)
Input:  s = "AABABBA", k = 1  →  4  ("AABA" → replace one B → "AAAA")
```

---

## Core Insight

For any window `[left, right]`, the minimum replacements needed to make
all characters the same:

```
replacements = window_length - max_frequency_in_window
```

We keep the most frequent character, replace everything else.
The window is valid iff: `(right - left + 1) - maxFreq <= k`

---

## Algorithm

```java
int[] count = new int[26];
int left = 0, maxFreq = 0, maxLen = 0;

for (int right = 0; right < s.length(); right++) {
    count[s.charAt(right) - 'A']++;
    maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);

    if ((right - left + 1) - maxFreq > k) {
        count[s.charAt(left) - 'A']--;
        left++;
    }

    maxLen = Math.max(maxLen, right - left + 1);
}
```

---

## Why maxFreq Never Decreases (Key Optimisation)

When we shrink the window (left++), we decrement the count of the
removed character. `maxFreq` might now be stale — the true max frequency
in the window could be lower.

**But we don't need to recompute it.** Here's why:

We're looking for the *longest* valid window. The window only grows when
a new character beats `maxFreq` (increasing it). If no character beats
`maxFreq`, the window slides at the same size — it never grows, but it
never shrinks below the best size seen so far either.

`maxFreq` is monotonically non-decreasing — it only goes up. The window
size (`right - left + 1`) is also monotonically non-decreasing.

This is the insight that makes the algorithm O(n): we never shrink the
window below its best size, so `left` can only ever advance by at most
`n` total steps.

---

## Why 'if' Not 'while'

```java
if ((right - left + 1) - maxFreq > k) {   // shrink by at most 1
    count[s.charAt(left) - 'A']--;
    left++;
}
```

The condition can only trigger once per `right` iteration. After removing
one character from the left, the window shrinks by exactly 1, making it
valid again. A `while` loop would also work but is equivalent — it
never runs more than once per step.

---

## Full Trace — `"AABABBA"`, k=1

| r | char | count | maxFreq | window | valid? | left | maxLen |
|---|------|-------|---------|--------|--------|------|--------|
| 0 | A | A:1 | 1 | 1 | 0≤1 ✓ | 0 | 1 |
| 1 | A | A:2 | 2 | 2 | 0≤1 ✓ | 0 | 2 |
| 2 | B | A:2,B:1 | 2 | 3 | 1≤1 ✓ | 0 | 3 |
| 3 | A | A:3,B:1 | 3 | 4 | 1≤1 ✓ | 0 | **4** |
| 4 | B | A:3,B:2 | 3 | 5 | 2>1 ✗ | 1 | 4 |
| 5 | B | A:2,B:3 | 3 | 5 | 2>1 ✗ | 2 | 4 |
| 6 | A | A:3,B:3 | 3 | 5 | 2>1 ✗ | 3 | 4 |

Result: `4` ✓

---

## Generalisation — The k=0 Case Is LeetCode #3

Setting k=0:
```
invalid when (length - maxFreq) > 0
         ↔ when any character repeats
```
This is exactly the longest-substring-without-repeating condition from #3.
This problem is the strict generalisation of #3.

| Problem | Condition | k |
|---------|-----------|---|
| #3 Longest Substring Without Repeating | length - maxFreq > 0 | 0 |
| #424 (this) | length - maxFreq > k | any k |

---

## Complexity

| | |
|--|--|
| Time | O(n) — right moves n steps, left at most n steps |
| Space | O(1) — int[26], fixed size |

---

## Edge Cases

| Input | k | Output | Reason |
|-------|---|--------|--------|
| `"AAAA"` | 0 | 4 | Already uniform |
| `"ABCD"` | 0 | 1 | No repeats, k=0 |
| `"ABCD"` | 3 | 4 | Replace 3 chars → all same |
| `"A"` | 2 | 1 | Single char |
