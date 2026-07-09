# Sort Characters By Frequency — Notes & Intuition

**LeetCode #451** | Strings / Bucket Sort | Medium
Constraints: 1 ≤ len ≤ 5·10⁵, letters and digits. Same characters must be grouped.

---

## Problem

Sort a string in **decreasing order of character frequency**, with each character's
copies contiguous. Any valid ordering of equal-frequency characters is accepted.

```
Input:  "tree"      Output: "eert"    (e×2, then r and t)
Input:  "cccaaa"    Output: "aaaccc"  (both ×3; each run stays together)
Input:  "Aabb"      Output: "bbAa"    (case-sensitive: b×2, then A, a)
```

---

## The Shape — Count, Order, Build

It's not really a "sort the characters" problem; it's **order the distinct characters
by frequency, then emit each as a run**:

1. **Count** each character's frequency.
2. **Order** the distinct characters by frequency, descending.
3. **Build** the result — each character repeated `frequency` times.

The only real decision is *how* to order by frequency — and there's a linear-time way.

---

## Ordering A — Sort: O(n + k log k)

Put the distinct characters in a list, sort by count descending, emit. With only
`k ≤ 62` distinct characters (letters + digits), this sort is trivially cheap.

```java
List<Character> chars = new ArrayList<>(freq.keySet());
chars.sort((a, b) -> freq.get(b) - freq.get(a));   // descending
```

---

## Ordering B — Bucket Sort: O(n)  ← the pattern to know

**Key insight:** a frequency can never exceed `n`, so you can **index by frequency**
instead of sorting it. Make buckets `0..n`, drop each character into
`bucket[itsFrequency]`, then walk buckets from high to low. No comparison sort, no log
factor.

```java
List<Character>[] bucket = new List[n + 1];   // bucket[f] = chars with frequency f
for (var e : freq.entrySet())
    bucket[e.getValue()] ... .add(e.getKey());

for (int f = n; f >= 1; f--)                  // most frequent first
    if (bucket[f] != null)
        for (char c : bucket[f]) append c, f times;
```

This is the same **"the key range is bounded, so counting/bucket sort beats comparison
sort"** trick behind *Top K Frequent Elements*.

---

## Full Trace — "tree"

1. **Count:** `t:1, r:1, e:2`.
2. **Bucket:** `bucket[2] = [e]`, `bucket[1] = [t, r]`.
3. **Walk high→low:** `f=2` → `"ee"`; `f=1` → `"t"`, `"r"`.
4. Result: `"eert"`. (`"eetr"` appears if the map yields `r` before `t` — also valid.)

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| Count + sort | O(n + k log k) | O(n) |
| Count + **bucket sort** | **O(n)** | O(n) |

With `k` (distinct chars) capped at 62, both are effectively linear; the bucket
framing is the transferable one.

---

## Edge Cases

| Input | Output | Note |
|-------|--------|------|
| single character | itself | frequency 1 |
| all identical | the string unchanged | one bucket |
| ties in frequency | any order of the tied chars | problem accepts any |
| mixed case | treated as distinct | `A` and `a` are different characters |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Sort **words** by frequency | tokens, not chars | same count→bucket, key by word |
| Top-K frequent only | don't emit all | bucket sort then take first K (LeetCode 347) |
| Stable / tie-break order | deterministic ties | sort ties within each bucket |
| Streaming frequency | can't hold all | approximate counters (Count–Min sketch) |
| Ascending order | least frequent first | walk buckets low→high |

**The through-line:** counting frequencies turns a "sort" into a tally; and because
the max frequency is bounded by `n`, bucketing by count gives an O(n) ordering without
a comparison sort — the core idea behind frequency-ranking and Huffman coding.
