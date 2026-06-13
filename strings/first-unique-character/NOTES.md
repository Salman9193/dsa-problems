# First Unique Character in a String — Notes & Intuition

**LeetCode #387** | Strings / Hash Table | Easy

---

## Problem

Given string `s`, find the index of the first non-repeating character.
Return `-1` if none exists.

```
"leetcode"     →  0  ('l' appears once)
"loveleetcode" →  2  ('v' appears once, before 't','c','d')
"aabb"         →  -1 (all chars repeat)
```

---

## Why One Pass Isn't Enough

You can't identify a unique character until you've seen all occurrences.
The second 'e' in "leetcode" appears at index 2 — you can't know at index 1
that 'e' will repeat. A full frequency count (pass 1) is required before
uniqueness can be determined (pass 2).

---

## Approach 1 — int[26] Two-Pass (Optimal)

```java
int[] count = new int[26];
for (char c : s.toCharArray())
    count[c - 'a']++;

for (int i = 0; i < s.length(); i++)
    if (count[s.charAt(i) - 'a'] == 1) return i;

return -1;
```

**Why int[26] beats HashMap:**

| | int[26] | HashMap |
|--|---------|---------|
| Lookup | O(1), direct index | O(1) amortised, with hashing |
| Memory | 104 bytes, one cache line | ~hundreds of bytes |
| Boxing | None (primitive int) | int → Integer |
| Charset | a-z only | Any character |

For lowercase ASCII problems, always prefer `int[26]`.

---

## Approach 2 — LinkedHashMap (Unicode-safe)

```java
Map<Character, Integer> count = new LinkedHashMap<>();
for (char c : s.toCharArray())
    count.merge(c, 1, Integer::sum);

for (Map.Entry<Character, Integer> e : count.entrySet())
    if (e.getValue() == 1) return s.indexOf(e.getKey());
```

`LinkedHashMap` preserves insertion order — iterating its entries gives
characters in first-seen order. Works for any character set.

---

## Full Traces

**`"leetcode"`**

| Pass 1 | l:1 e:3 t:1 c:1 o:1 d:1 |
|--------|--------------------------|

| i | char | count | action |
|---|------|-------|--------|
| 0 | l | 1 | **return 0** ✓ |

**`"loveleetcode"`**

| Pass 1 | l:2 o:2 v:1 e:4 t:1 c:1 d:1 |
|--------|------------------------------|

| i | char | count | action |
|---|------|-------|--------|
| 0 | l | 2 | skip |
| 1 | o | 2 | skip |
| 2 | v | 1 | **return 2** ✓ |

---

## The Streaming Variant (LeetCode #1429)

When characters arrive one at a time and you need the first unique after
each insertion, `int[26]` + two-pass won't work (you'd rescan on every insert).

**Solution:** Doubly Linked List + HashMap
- List stores characters in insertion order, skipping repeats
- HashMap maps char → list node for O(1) deletion
- When a character repeats, remove its node from the list
- The list head is always the first unique character

```
Insert 'a': list=[a], map={a→nodeA}
Insert 'b': list=[a,b], map={a→nodeA, b→nodeB}
Insert 'a': remove nodeA from list → list=[b]
Query: return head = 'b'
```

This is O(1) per insert and O(1) per query — vs O(n) rescan with int[26].

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| int[26] two-pass | O(n) | O(1) |
| LinkedHashMap | O(n) | O(k) |
| Streaming (DLL + HashMap) | O(1) per op | O(k) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `"a"` | `0` | Single char, always unique |
| `"aaa"` | `-1` | Only one char, all repeat |
| `"z"` | `0` | Last letter of alphabet |
| `"abcabc"` | `-1` | All chars repeat exactly twice |
