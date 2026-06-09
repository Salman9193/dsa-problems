# Group Anagrams — Notes & Intuition

**LeetCode #49** | Arrays / HashMap | Medium

---

## Problem

Given an array of strings, group all anagrams together and return the groups
in any order.

```
Input:  ["eat","tea","tan","ate","nat","bat"]
Output: [["eat","tea","ate"], ["tan","nat"], ["bat"]]
```

Two strings are anagrams if one is a rearrangement of the other.

---

## Core Insight — Canonical Form

Two strings are anagrams iff they have the **same character frequencies**.
If you normalise both to a canonical form, anagrams produce the same key
and can be grouped with a single HashMap pass.

Two canonical forms work:

| Form | Example | Cost |
|------|---------|------|
| Sort the string | `"eat"` → `"aet"` | O(k log k) per string |
| Character frequency array | `"eat"` → `"1#0#0#0#1#..."` | O(k) per string |

---

## Approach 1 — Sort as Key

```java
char[] chars = s.toCharArray();
Arrays.sort(chars);
String key = new String(chars);
map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
```

Simple and interview-standard. O(n * k log k) total.

---

## Approach 2 — Frequency Array as Key (Optimal)

```java
int[] count = new int[26];
for (char c : s.toCharArray()) count[c - 'a']++;

StringBuilder sb = new StringBuilder();
for (int i = 0; i < 26; i++) sb.append(count[i]).append('#');
String key = sb.toString();
```

O(n * k) — no sorting, one linear scan per string.

### Why the '#' Delimiter Is Critical

Without delimiter, different frequency arrays can produce the same string:

```
count = [1, 11, 0, ...] → "111..."
count = [11, 1, 0, ...] → "111..."   ← collision! wrong grouping

With '#':
count = [1, 11, 0, ...] → "1#11#0#..."
count = [11, 1, 0, ...] → "11#1#0#..."  ← different keys ✓
```

---

## Approach 3 — Prime Product (Elegant but Risky)

Assign a prime to each letter (a=2, b=3, c=5, ...).
Product of all character primes is the key.
Multiplication is commutative → anagrams produce the same product.

```java
int[] primes = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101};
long product = 1;
for (char c : s.toCharArray()) product *= primes[c - 'a'];
```

**Problem:** overflows `long` for strings with many high-prime characters.
Not safe for production. Good to mention as a curiosity in interviews.

---

## Comparison

| Approach | Time | Space | Safe? |
|----------|------|-------|-------|
| Sort as key | O(n * k log k) | O(n * k) | ✓ |
| Frequency key | O(n * k) | O(n * k) | ✓ |
| Prime product | O(n * k) | O(n) | ✗ overflow |

---

## Full Trace

`strs = ["eat","tea","tan","ate","nat","bat"]`

| String | Sorted key | Map state |
|--------|-----------|-----------|
| "eat" | "aet" | {"aet": ["eat"]} |
| "tea" | "aet" | {"aet": ["eat","tea"]} |
| "tan" | "ant" | {"aet":[...], "ant": ["tan"]} |
| "ate" | "aet" | {"aet": ["eat","tea","ate"]} |
| "nat" | "ant" | {..., "ant": ["tan","nat"]} |
| "bat" | "abt" | {..., "abt": ["bat"]} |

Result: `[["eat","tea","ate"], ["tan","nat"], ["bat"]]`

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `[""]` | `[[""]]` | Empty string maps to empty key |
| `["a"]` | `[["a"]]` | Single char |
| All same anagram | One group | All map to same key |
| No anagrams | n groups | Each string is its own key |

---

## computeIfAbsent Pattern

```java
map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
```

Equivalent to:
```java
if (!map.containsKey(key)) map.put(key, new ArrayList<>());
map.get(key).add(s);
```

`computeIfAbsent` is atomic and more concise — prefer it when building
a map of lists.
