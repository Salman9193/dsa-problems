# Minimum Window Substring — Notes & Intuition

**LeetCode #76** | Strings / Sliding Window | Hard

---

## Problem

Given strings `s` and `t`, return the minimum window substring of `s`
that contains all characters of `t` (including duplicates).

```
Input:  s = "ADOBECODEBANC", t = "ABC"  →  "BANC"
Input:  s = "a",             t = "a"    →  "a"
Input:  s = "a",             t = "aa"   →  ""
```

---

## Core Insight

For a window `[left, right]` to be valid, every character `c` in `t` must
appear in the window with count `>= need[c]`.

Checking validity naively is O(|t|) per step → O(n·|t|) total — too slow.

**The O(1) validity trick:** track `satisfied` — the number of distinct
character types whose window count is currently `>= need[c]`.
When `satisfied == distinct` (number of distinct chars in t), the window is valid.

`satisfied` only changes at the exact transition:
- `have[c]` goes from `need[c]-1` → `need[c]`: increment `satisfied`
- `have[c]` goes from `need[c]` → `need[c]-1`: decrement `satisfied`

---

## The Two-Phase Loop

```
Expand (right++):
  have[s[right]]++
  if have[c] == need[c]: satisfied++

Shrink (left++) while valid:
  record window as candidate minimum
  have[s[left]]--
  if have[c] < need[c]: satisfied--
  left++
```

Each character added once (right++) and removed at most once (left++) → O(n).

---

## Why `have[c] == need[c]` Not `>=`

```java
// CORRECT: only fires on the exact transition unsatisfied → satisfied
if (need[c] > 0 && have[c] == need[c]) satisfied++;

// WRONG: would fire every time have[c] >= need[c], double-counting
if (need[c] > 0 && have[c] >= need[c]) satisfied++;
```

Example with t = "AA" (need[A]=2):
- have[A] goes 0→1: not satisfied yet (1 < 2)
- have[A] goes 1→2: satisfied! (2 == 2) ← only fires here ✓
- have[A] goes 2→3: already satisfied, don't fire again

Decrement is symmetric: only fire when dropping from 2→1 (need[A]=2).

---

## Full Trace — `s="ADOBECODEBANC"`, `t="ABC"`

`need: A=1, B=1, C=1, distinct=3`

| right | char | satisfied | action |
|-------|------|-----------|--------|
| 0 | A | 1 | have[A]=1==need[A]=1 |
| 1 | D | 1 | — |
| 2 | O | 1 | — |
| 3 | B | 2 | have[B]=1==need[B]=1 |
| 4 | E | 2 | — |
| 5 | C | **3** | VALID → shrink |
| | | | record "ADOBEC"(6), remove 'A'→ satisfied=2, left=1 |
| 6-9 | D,O,B,E | 2 | — |
| 10 | A | **3** | VALID → shrink left=1..5 |
| | | | remove until 'C' drops out → satisfied=2 |
| 11 | N | 2 | — |
| 12 | C | **3** | VALID → shrink |
| | | | left=9: record "BANC"(4) ← **NEW MIN** |
| | | | remove 'A'→ satisfied=2, stop |

Result: `"BANC"` ✓

---

## Sliding Window Family Comparison

| Problem | Valid condition | Complexity |
|---------|----------------|------------|
| #3 No repeating chars | all unique (k=0) | O(n) |
| #424 Char replacement | length - maxFreq ≤ k | O(n) |
| **#76 Min window** | satisfied == distinct | O(n + m) |

All three use expand-then-shrink. #76 is the most general form — the
validity condition spans multiple character types simultaneously.

---

## Complexity

| | |
|--|--|
| Time | O(n + m) — n=\|s\|, m=\|t\| |
| Space | O(1) — two fixed int[128] arrays |

---

## Edge Cases

| s | t | Output | Reason |
|---|---|--------|--------|
| `"a"` | `"a"` | `"a"` | Exact match |
| `"a"` | `"aa"` | `""` | Not enough 'a' |
| `"aa"` | `"aa"` | `"aa"` | Both a's needed |
| `"abc"` | `"ac"` | `"abc"` | Must include all chars between |
