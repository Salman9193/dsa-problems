# Word Break II — Notes & Intuition

**LeetCode #140** | Backtracking + Memoization | Hard
Return **every** way to segment the string into dictionary words — not just whether one exists.

---

## Problem

```
s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
→ ["cats and dog", "cat sand dog"]

s = "catsandog",  wordDict = ["cats","dog","sand","and","cat"]
→ []
```

The `#139` sibling asks *"is there **a** segmentation?"* (boolean). This asks for **all of them**,
which changes the complexity class of the answer.

---

## The Key Insight — Same DAG, Different Question

Both problems build the **same graph**: node `i` = position in the string, edge `i → j` whenever
`s[i..j-1]` is a dictionary word.

```
"catsanddog" DAG:  {0:[2,3], 3:[6], 4:[6], 7:[9]}
                    (0→"cat"/"cats", 3→"sand", 4→"and", 7→"dog")
```

| Question | Relaxation | Problem |
|----------|-----------|---------|
| Is there a path `0 → n`? | boolean OR | [Word Break #139](#dynamic-programming/word-break) |
| **Enumerate all paths** | collect lists | **Word Break II #140** |
| Path with max weight | max log-probability | **jieba's Chinese segmenter** |

**It's the same *linearize-then-relax* skeleton** we use everywhere — only the accumulator changes.

---

## The Solution — Memoized Backtracking

Recurse from each position; memoize **the list of completions from that position**:

```java
Map<Integer, List<String>> memo = new HashMap<>();

private List<String> solve(String s, int start, Set<String> dict) {
    if (memo.containsKey(start)) return memo.get(start);

    List<String> res = new ArrayList<>();
    if (start == s.length()) { res.add(""); return res; }   // one empty completion

    for (int end = start + 1; end <= s.length(); end++) {
        String word = s.substring(start, end);
        if (!dict.contains(word)) continue;
        for (String rest : solve(s, end, dict)) {           // all completions after `word`
            res.add(rest.isEmpty() ? word : word + " " + rest);
        }
    }
    memo.put(start, res);
    return res;
}
```

**Why memoize a *list* and not a number:** `solve(end)` may be reached from many different
prefixes. Without memo, the shared suffix is re-explored every time → exponential *recomputation*
on top of exponential output.

---

## The Complexity Trap — Memoization Doesn't Save You

This is the part interviewers probe:

> Memoization removes redundant **computation**, but the **output itself is exponential**, so no
> algorithm can be polynomial.

```
s = "aaaaaaaaaaaaaaaaaaaa"  (20 a's),  dict = ["a","aa","aaa"]
   → 121,415 distinct segmentations
```

Every one must be materialised, so **Ω(number of segmentations)** is a hard floor. Memoization
makes you **output-sensitive** — time proportional to the *size of the answer*, not to the number
of paths *explored* — which is the best achievable.

| | Time | Space |
|--|------|-------|
| Naive backtracking | O(2ⁿ) explored, with recomputation | O(n) |
| **Memoized (this)** | **O(n² · L + total output size)** | O(n · output) |
| #139 (boolean only) | **O(n² · L)** — polynomial! | O(n) |

**The gap between #139 and #140 is the gap between deciding and enumerating.** #139 is polynomial
because it collapses all paths into one bit; #140 can't collapse anything.

---

## Pruning — Check Feasibility First

A big practical win: run the **#139 boolean DP first**. If the string isn't breakable at all,
return `[]` immediately instead of exploring:

```java
if (!canBreak(s, dict)) return new ArrayList<>();   // O(n²) guard against the worst case
```

Without it, `"aaaa…aab"` with dict `["a","aa","aaa"]` explores exponentially before discovering
that nothing works. **The pathological input is the one with many partial paths and zero complete
ones** — exactly what the guard kills.

---

## Trace — `"catsanddog"`

```
solve(0):  "cat"  → solve(3) = ["sand dog"]        → "cat sand dog"
           "cats" → solve(4) = ["and dog"]         → "cats and dog"
solve(3):  "sand" → solve(7) = ["dog"]             → "sand dog"
solve(4):  "and"  → solve(7) = ["dog"]  ← MEMO HIT (computed once, used twice)
solve(7):  "dog"  → solve(10) = [""]               → "dog"
```

`solve(7)` is reached from both `solve(3)` and `solve(4)` — the memo is what makes the shared
suffix free.

---

## Edge Cases

| Case | Result |
|------|--------|
| no valid segmentation | `[]` (guard with #139 first) |
| whole string is a word | that single string |
| dictionary word repeated | fine — reuse is unlimited |
| empty string | one empty segmentation |
| many overlapping words | **exponential output** — the expected behaviour |

## Real-World Connection — Chinese Segmentation

This is **exactly** jieba's **full mode**: enumerate every dictionary word combination. Chinese has
no spaces, so *every* sentence is a Word Break problem — and the practical version doesn't want all
segmentations, it wants the **most probable** one, which turns this enumeration into a
**max-log-probability DAG path DP**.

> See the [Chinese Word Segmenter LLD](https://salman9193.github.io/system-design/#lld-chinese-word-segmenter)
> and the [Text Segmentation Service HLD](https://salman9193.github.io/system-design/#hld-text-segmentation-service)
> for the production version — including why a search engine must pin its dictionary version.

**The through-line:** same DAG, three relaxations — *is there a path* (#139), *all paths* (#140),
*best path* (jieba). Enumeration is exponential by nature; the DP for the *best* path is linear.
