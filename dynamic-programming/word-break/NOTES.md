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

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Return all sentences (#140) | Return all valid breaks | DP for feasibility + backtracking for sentences |
| Word Break with word count limit | At most k words used | Add k dimension to DP state |
| Word Break with Trie | Large dictionary | Replace `wordSet.contains()` with Trie traversal — faster for long words |
| Minimum words needed | Fewest words to break s | Change `||` to `min` in transition |
| Reuse allowed (already is) | Already the case | This is the unbounded variant |
| Each word used at most once | 0/1 word break | Right-to-left DP update |
| Streaming dictionary | Words added over time | Trie with dynamic insertion; re-run DP on change |

**Trie optimisation:** Instead of checking every word in the dictionary at each position i (O(n × |dict| × L)), use a Trie to match all possible endings from position i in O(L) time — reducing to O(n × L) where L = max word length.

**Scaling:** For s of length 10⁴ and dictionary of 10⁵ words each of length up to 20, naive DP is O(n² × dict) = 2×10¹² operations. Trie reduces to O(n × L) = 2×10⁵.

---

## The Bigger Picture — Same DAG, Three Questions

This problem builds a **DAG**: node `i` = position in the string, edge `i → j` whenever
`s[i..j-1]` is a dictionary word. Positions are inherently topologically ordered, so it's the
familiar *linearize, then relax* skeleton — only the accumulator changes:

| Accumulator | Question | Problem | Complexity |
|-------------|----------|---------|-----------|
| boolean OR | *is there a path?* | **Word Break #139** (this) | **polynomial** O(n²·L) |
| list concat | *all paths?* | [Word Break II #140](#dynamic-programming/word-break-ii) | **exponential output** |
| max log-probability | *the best path?* | Chinese segmentation (jieba) | **linear** O(n·L) |

**The decision problem is polynomial, enumeration is exponential, and optimization is linear
again.** That's why production segmenters compute the *most probable* segmentation rather than all
of them.

### Real-World: this is Chinese text segmentation

Chinese is written **without spaces**, so *every sentence* is a Word Break problem — and the
ambiguity is real: `北京大学生` = `北京大学 / 生` (Peking University / student) **or**
`北京 / 大学生` (Beijing / university student). Word frequencies pick the winner via a
max-log-probability DP over this same DAG.

> See the [Chinese Word Segmenter LLD](https://salman9193.github.io/system-design/#lld-chinese-word-segmenter)
> (prefix dictionary, DAG, route DP, HMM/Viterbi for unknown words) and the
> [Text Segmentation Service HLD](https://salman9193.github.io/system-design/#hld-text-segmentation-service)
> (why a search engine must pin its dictionary version, or lose recall silently).
