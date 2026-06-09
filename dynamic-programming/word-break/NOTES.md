# Word Break — Notes & Intuition

**LeetCode #139** | Dynamic Programming | Medium

---

## Problem

Given a string `s` and a dictionary `wordDict`, return `true` if `s` can be
segmented into a space-separated sequence of one or more dictionary words.

```
Input:  s = "leetcode",      wordDict = ["leet","code"]      → true
Input:  s = "applepenapple", wordDict = ["apple","pen"]       → true
Input:  s = "catsandog",     wordDict = ["cats","dog","sand","and","cat"] → false
```

---

## Why Greedy Fails

Greedily matching the longest (or first) prefix fails for overlapping words:

```
s = "catsandog", dict = ["cats","dog","sand","and","cat"]
Greedy picks "cats" first → "andog" left → no valid segmentation
Optimal:      "cat" + "sand" → but "og" is left → still false
Both fail, which is correct — but greedy can give wrong TRUE answers too.
```

DP is needed because an early word choice can foreclose better segmentations.

---

## The DP Recurrence

`dp[i]` = can prefix `s[0..i)` be segmented using `wordDict`?

**Base case:** `dp[0] = true` — empty string is always valid.

**Recurrence:**
```
for each i from 1 to n:
    for each j from 0 to i-1:
        if dp[j] == true AND s[j..i) is in wordDict:
            dp[i] = true
            break
```

**Intuition:** `dp[i]` is true if there exists some split point `j` where
the prefix up to `j` is valid (`dp[j]`) and the word `s[j..i)` is in the dict.

---

## Visualisation

```
s = "l e e t c o d e"
     0 1 2 3 4 5 6 7 8

dp:  T . . . . . . . .    (initialised, dp[0]=true)

i=4: j=0, s[0,4)="leet" in dict, dp[0]=T  →  dp[4]=T
i=8: j=4, s[4,8)="code" in dict, dp[4]=T  →  dp[8]=T  ✓
```

---

## Original Solution Analysis

```java
// Problem 1: substring as memo key
Map<String, Boolean> resultMap = new HashMap();
// s.substring(i) allocates a new String on EVERY call
// → O(n^2) String objects created across all recursive calls

// Problem 2: instance field
Map<String,Boolean> resultMap = new HashMap();
// persists across LeetCode test cases if Solution object is reused
// → wrong answers on subsequent tests

// Problem 3: redundant early check
if (wordSet.contains(s)) return true;
// handled by the loop when end == s.length()
// not wrong, just noisy
```

**Fix:** use `Boolean[] memo` indexed by start position — O(n) integers, zero allocations.

---

## Top-Down vs Bottom-Up

### Top-Down (Memoisation)
```java
Boolean[] memo = new Boolean[s.length()];
boolean dp(String s, Set<String> wordSet, int start, Boolean[] memo) {
    if (start == s.length()) return true;
    if (memo[start] != null) return memo[start];
    for (int end = start + 1; end <= s.length(); end++) {
        if (wordSet.contains(s.substring(start, end)) && dp(s, wordSet, end, memo))
            return memo[start] = true;
    }
    return memo[start] = false;
}
```
- Natural recursion, early exit on first valid path found.
- Recursion stack depth = O(n).

### Bottom-Up (Tabulation)
```java
boolean[] dp = new boolean[n + 1];
dp[0] = true;
for (int i = 1; i <= n; i++)
    for (int j = 0; j < i; j++)
        if (dp[j] && wordSet.contains(s.substring(j, i))) { dp[i] = true; break; }
return dp[n];
```
- No recursion, no stack overflow risk.
- Most readable — preferred in interviews.

---

## Complexity

| | Time | Space |
|--|------|-------|
| Naive recursion (no memo) | O(2^n) | O(n) stack |
| Top-down DP (index memo) | O(n^2) | O(n) |
| Bottom-up DP | O(n^2) | O(n) |

Note: substring operations are O(k) where k = substring length, so the
true complexity is O(n^3) in the worst case. Using a Trie reduces this
to O(n^2) by avoiding substring allocations entirely.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `s=""` | `true` | dp[0] = true by base case |
| `s="a"`, dict=`["a"]` | `true` | Single char word |
| `s="aaaaaab"`, dict=`["a","aa","aaa"]` | `false` | Exhaustive backtracking needed |
| Same word reused | `true` | `"applepenapple"` uses "apple" twice |
