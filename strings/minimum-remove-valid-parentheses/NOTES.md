# Minimum Remove to Make Valid Parentheses — Notes & Intuition

**LeetCode #1249** | Strings / Stack | Medium

---

## Problem

Remove the minimum number of `(` or `)` to make the string valid.
Return any valid result.

```
"lee(t(c)o)de"  →  "lee(t(c)o)de"  (already valid)
"a)b(c)d"       →  "ab(c)d"
"))(("          →  ""
"(a(b(c)d)"     →  "a(b(c)d)"
```

---

## What Makes a Parenthesis Invalid?

```
Unmatched ')': appears when there is no prior unmatched '('
Unmatched '(': appears when there is no later unmatched ')'
```

We must remove exactly these characters — no more, no fewer.

---

## Algorithm — Stack of Indices

Use a **stack** to track indices of unmatched parentheses as we scan:

```java
for each char c at index i:
    if c == '(':
        push(i)              // candidate for removal if unmatched
    else if c == ')':
        if top is '(':
            pop()            // matched pair — both stay
        else:
            push(i)          // unmatched ')' — mark for removal

After scan: stack = indices of ALL unmatched parentheses
```

Sort indices, extract substrings between removal points, concatenate.

---

## Why Stack (Not Full Deque)?

A `)` must match the **most recent** unmatched `(` — LIFO order.
A stack exposes only the top, which is exactly what we need.
A deque exposes both ends — useful for matching from both sides
(e.g. palindrome checking), but adds unnecessary complexity here.

---

## Why Check `s.charAt(stack.peek()) == '('`?

The stack holds both unmatched `(` and unmatched `)` indices.
When we see `)`, we only match it with a prior `(`, not a prior `)`.
Without the check, we'd pair two unmatched `)` together — incorrect.

---

## Substring Extraction vs HashSet Membership

Two ways to build the result:

```java
// HashSet approach: check every character
Set<Integer> toRemove = new HashSet<>(stack);
for (int i = 0; i < s.length(); i++)
    if (!toRemove.contains(i)) result.append(s.charAt(i));

// Substring approach: copy ranges between removals
for (int idx : sortedRemoveIdx) {
    result.append(s, prev, idx);
    prev = idx + 1;
}
result.append(s, prev, s.length());
```

Both are O(n). Substring extraction is more cache-friendly — sequential
memory reads rather than hash lookups per character.

---

## Full Traces

**`"a)b(c)d"`**

| i | char | stack action | stack |
|---|------|-------------|-------|
| 1 | ) | empty → push 1 | [1] |
| 3 | ( | push 3 | [1,3] |
| 5 | ) | top=3 is '(' → pop | [1] |

removeIdx=[1] → `s[0,1)` + `s[2,7)` = "a" + "b(c)d" = **"ab(c)d"** ✓

**`"(a(b(c)d)"`**

| i | char | stack action | stack |
|---|------|-------------|-------|
| 0 | ( | push 0 | [0] |
| 2 | ( | push 2 | [0,2] |
| 4 | ( | push 4 | [0,2,4] |
| 6 | ) | top=4 is '(' → pop | [0,2] |
| 8 | ) | top=2 is '(' → pop | [0] |

removeIdx=[0] → `s[0,0)` + `s[1,9)` = "" + "a(b(c)d)" = **"a(b(c)d)"** ✓

**`"))(("`**

All 4 indices pushed → removeIdx=[0,1,2,3] → all removed → **""** ✓

---

## Complexity

| | |
|--|--|
| Time | O(n log k) — scan O(n) + sort k indices O(k log k); effectively O(n) |
| Space | O(n) — stack + result buffer |

---

## Why This Problem Uses a Stack (Formal Justification)

Parentheses form a **context-free language** — specifically the Dyck language.
The Dyck language is recognisable by a pushdown automaton (stack machine).
Validating parentheses with a stack is not just a trick — it's the provably
minimal machine needed (finite automata cannot recognise balanced parentheses).

This is why every correct parenthesis validator uses a stack.

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| #20 Valid Parentheses | Just check validity, no fix | Same stack, return stack.isEmpty() |
| #32 Longest Valid Parentheses | Length of longest valid substring | Stack of indices; track spans |
| #301 Remove Invalid Parentheses | Return ALL minimal removals | BFS level-by-level removal |
| #678 Valid Parenthesis String | `*` can be `(`, `)`, or `` | Greedy with min/max open count |
| Multiple bracket types `()[]{}` | k types simultaneously | Same stack; check matching type |
| HTML/XML tag matching | Variable-length tags | Stack of strings (tag names) |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `""` | `""` | Empty string is valid |
| `"abc"` | `"abc"` | No parentheses — already valid |
| `"()"` | `"()"` | Already valid — stack empty |
| `")))"` | `""` | All unmatched |
| `"((("` | `""` | All unmatched |
