# Decode String — Notes & Intuition

**LeetCode #394** | Strings / Stack | Medium

---

## Problem

Decode `k[encoded_string]` by repeating `encoded_string` exactly `k` times.
Brackets can be nested.

```
"3[a]2[bc]"     →  "aaabcbc"
"3[a2[c]]"      →  "accaccacc"
"2[abc]3[cd]ef" →  "abcabccdcdcdef"
```

---

## Core Insight — Stack Simulates Recursion

Nested brackets naturally suggest recursion — process innermost first, then
outer. The iterative approach simulates the call stack explicitly with two stacks:

```
countStack  — saves repeat count k at each nesting level
stringStack — saves the accumulated string before each '['
```

---

## Four Character Cases

```java
if      (isDigit(c))   k = k * 10 + (c - '0')           // build k
else if (c == '[')     push(k, current); current = ""; k=0 // enter bracket
else if (c == ']')     current = prev + inner × repeat    // exit bracket
else                   current.append(c)                   // regular char
```

---

## State at Each `[` and `]`

```
"3[a2[c]]"

At first '[':   countStack=[3],   stringStack=[""],  current=""
At second '[':  countStack=[3,2], stringStack=["","a"], current=""
At first ']':   pop 2 and "a" → current = "a" + "c"×2 = "acc"
At second ']':  pop 3 and ""  → current = "" + "acc"×3 = "accaccacc"
```

---

## Why k = k×10 + digit?

Multi-digit numbers like `123[a]` are read one character at a time.
Each new digit shifts the previous value left one decimal place:

```
c='1': k = 0×10 + 1 = 1
c='2': k = 1×10 + 2 = 12
c='3': k = 12×10 + 3 = 123
```

`k` is reset to 0 after each `[`.

---

## Why Two Stacks Not One?

On `]`, we need:
1. How many times to repeat → from `countStack`
2. What string came before `[` → from `stringStack`

These are independent quantities. A single stack would require either
encoding both into one value (error-prone) or two pops (same thing).

---

## Full Traces

**`"3[a2[c]]"`**

| char | k | countStack | stringStack | current |
|------|---|-----------|-------------|---------|
| 3 | 3 | [] | [] | "" |
| [ | 0 | [3] | [""] | "" |
| a | 0 | [3] | [""] | "a" |
| 2 | 2 | [3] | [""] | "a" |
| [ | 0 | [3,2] | ["","a"] | "" |
| c | 0 | [3,2] | ["","a"] | "c" |
| ] | — | [3] | [""] | "a"+"c"×2="acc" |
| ] | — | [] | [] | ""+"acc"×3="accaccacc" |

---

## Recursive Descent Alternative

```java
private int idx = 0;
String decode(String s) {
    StringBuilder sb = new StringBuilder();
    while (idx < s.length() && s.charAt(idx) != ']') {
        if (!isDigit(s.charAt(idx))) { sb.append(s.charAt(idx++)); }
        else {
            int k = 0;
            while (isDigit(s.charAt(idx))) k = k*10 + s.charAt(idx++)-'0';
            idx++; // skip '['
            String inner = decode(s);  // recurse into inner bracket
            idx++; // skip ']'
            sb.append(inner.repeat(k));
        }
    }
    return sb.toString();
}
```

Elegant but O(depth) call stack. Iterative is safer for deep nesting.

---

## Complexity

| | |
|--|--|
| Time | O(maxK^depth × n) — string construction dominates |
| Space | O(depth) stacks + O(output length) |

The problem guarantees output ≤ 10⁵, bounding the real-world runtime.

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `"abc"` | `"abc"` | No brackets — returned as-is |
| `"1[a]"` | `"a"` | k=1 means no repetition |
| `"10[a]"` | `"aaaaaaaaaa"` | Multi-digit k |
| `"3[a]3[a]"` | `"aaaaaaaaa"` | Two consecutive brackets |
| `"2[3[a]]"` | `"aaaaaaaaaa"` | Deeply nested: inner×3×2=6 |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Encode a string (RLE) | Produce the compact form | Scan runs; output count+char |
| Nested with variables | `k[x]` where x is a variable | Symbol table + same stack |
| Infinite expansion | k can be 0 or fractional | Validate before expanding |
| Decode in-place | No extra string buffer | Difficult; use deque |
| Streaming decode | Characters arrive one at a time | Same stack; O(1) per character |
| Maximum depth limit | Reject if depth > D | Add depth counter; throw on exceed |
