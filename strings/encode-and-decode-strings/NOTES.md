# Encode and Decode Strings — Notes & Intuition

**LeetCode #271** | Strings / Design | Medium

---

## Problem

Design an algorithm to encode a list of strings into a single string,
and decode it back to the original list. The encoded string must survive
network transmission and be reconstructed exactly.

```
["hello","world"]  →  encode  →  "5#hello5#world"  →  decode  →  ["hello","world"]
["we","say",":","yes"]  →  "2#we3#say1#:3#yes"  →  ["we","say",":","yes"]
```

---

## Why Naive Delimiters Fail

Any fixed delimiter breaks as soon as that character appears in a string:

```
delimiter = ","
["hello,world", "foo"]  →  "hello,world,foo"  →  ["hello","world","foo"]  WRONG

delimiter = "|"
["a|b", "c"]            →  "a|b|c"            →  ["a","b","c"]           WRONG
```

The problem: a fixed delimiter can't be distinguished from the same character
appearing inside the data. You need an encoding that is **self-describing**.

---

## The Solution — Length-Prefix Encoding

Prefix each string with its length followed by a separator character.
The decoder reads the length first, then consumes exactly that many characters
— no scanning for delimiters, no ambiguity.

```
Format: "<length>#<string>"

"hello"    →  "5#hello"
"5#tricky" →  "8#5#tricky"   ← length is 8, includes the # and digits
""         →  "0#"
```

### Why '#' as separator doesn't cause ambiguity

The decoder NEVER searches for '#' to find the end of a string.
It reads the length digits up to '#', then jumps directly `len` characters
forward. The '#' and any other characters within the string are consumed
as part of the payload.

```
Encoded: "8#5#tricky3#foo"

i=0: j = indexOf('#', 0) = 1
     len = parseInt("8") = 8
     payload = s[2..10) = "5#tricky"   ← 8 chars, includes the interior '#'
     i = 10

i=10: j = indexOf('#', 10) = 12
      len = parseInt("3") = 3
      payload = s[13..16) = "foo"
      i = 16  → done ✓
```

---

## Algorithm

### Encode
```java
for (String s : strs) {
    sb.append(s.length()).append('#').append(s);
}
```

### Decode
```java
int i = 0;
while (i < s.length()) {
    int j = s.indexOf('#', i);               // find next '#'
    int len = Integer.parseInt(s.substring(i, j));
    result.add(s.substring(j + 1, j + 1 + len));
    i = j + 1 + len;                        // jump past this string
}
```

---

## Full Trace

`strs = ["we", "say", ":", "yes"]`

**Encode:**
```
"we"  → "2#we"
"say" → "3#say"
":"   → "1#:"
"yes" → "3#yes"
encoded = "2#we3#say1#:3#yes"
```

**Decode:** `"2#we3#say1#:3#yes"`

| i | j | len | payload | next i |
|---|---|-----|---------|--------|
| 0 | 1 | 2 | "we" | 4 |
| 4 | 5 | 3 | "say" | 9 |
| 9 | 10 | 1 | ":" | 12 |
| 12 | 13 | 3 | "yes" | 17 |

---

## Alternative — 4-Byte Fixed-Width Header

Instead of variable ASCII length digits, use 4 big-endian bytes per string.
This is how binary protocols (HTTP/2, Kafka, gRPC) are structured:

```java
// Encode: write 4-byte big-endian length + string bytes
for (String s : strs) {
    int len = s.length();
    sb.append((char)(len >> 24));
    sb.append((char)(len >> 16));
    sb.append((char)(len >> 8));
    sb.append((char)(len));
    sb.append(s);
}

// Decode: read 4 bytes → length, then read that many bytes
int len = ((int)s.charAt(i) << 24) | ((int)s.charAt(i+1) << 16)
        | ((int)s.charAt(i+2) << 8)  | (int)s.charAt(i+3);
result.add(s.substring(i + 4, i + 4 + len));
i += 4 + len;
```

Pros: no scanning for delimiter, fixed overhead per string.
Cons: not human-readable, harder to debug.

---

## Edge Cases

| Input | Encoded | Notes |
|-------|---------|-------|
| `[""]` | `"0#"` | Empty string — length 0, still decodable |
| `["#","##"]` | `"1##2###"` | Hash chars handled by length prefix |
| `["5#trick"]` | `"8#5#trick"` | String that looks like encoding — safe |
| `[]` | `""` | Empty list → empty string → empty list |

---

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Encode | O(n) | O(n) |
| Decode | O(n) | O(n) |

n = total characters across all strings.
