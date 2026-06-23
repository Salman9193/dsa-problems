# Valid Parentheses — Notes & Intuition

**LeetCode #20** | Stack | Easy

---

## Problem

Given a string of `(`, `)`, `{`, `}`, `[`, `]`, determine if it is valid:
1. Every open bracket is closed by the same type.
2. Open brackets are closed in the correct order.

```
"()"      →  true
"()[]{}"  →  true
"(]"      →  false
"([)]"    →  false
"{[]}"    →  true
```

---

## Why a Counter Doesn't Work

For a single bracket type, a counter suffices:

```java
int count = 0;
for (char c : s) {
    if (c == '(') count++;
    else if (c == ')') count--;
    if (count < 0) return false;
}
return count == 0;
```

But with multiple types, a counter can't distinguish `"(]"` from `"()"` —
both have count[open]=1, count[close]=1. A counter discards **order**.

The stack preserves order — the LIFO property mirrors how nesting works.

---

## The Stack Insight

Brackets nest like a stack: the most recently opened must be the first closed.

```
"{ [ ( ) ] }"
 ↑           push }, stack=[}]
   ↑         push ], stack=[},]]
     ↑       push ), stack=[},],)]
       ↑     close ): top=) ✓ pop, stack=[},]]
         ↑   close ]: top=] ✓ pop, stack=[}]
           ↑ close }: top=} ✓ pop, stack=[]
stack empty → valid ✓
```

---

## Two Implementation Variants

### Variant 1 — Push Expected Closer (Cleaner)

```java
if      (c == '(') stack.push(')');
else if (c == '{') stack.push('}');
else if (c == '[') stack.push(']');
else {
    if (stack.isEmpty() || stack.pop() != c) return false;
}
```

Encodes the match at push time. Closing check is always one comparison.

### Variant 2 — Push Opener, Check at Pop

```java
if (isOpen(c)) stack.push(c);
else {
    if (stack.isEmpty()) return false;
    char top = stack.pop();
    if (c == ')' && top != '(') return false;
    if (c == '}' && top != '{') return false;
    if (c == ']' && top != '[') return false;
}
```

Same logic, slightly more verbose.

---

## Full Traces

**`"([)]"` → false**

| c | action | stack |
|---|--------|-------|
| ( | push ) | [)] |
| [ | push ] | [), ]] |
| ) | pop ] ≠ ) | → **false** |

**`"{[]}"` → true**

| c | action | stack |
|---|--------|-------|
| { | push } | [}] |
| [ | push ] | [}, ]] |
| ] | pop ] == ] ✓ | [}] |
| } | pop } == } ✓ | [] |
| end | empty ✓ | → **true** |

---

## Theoretical Foundation — The Dyck Language

The set of all valid bracket strings is called the **Dyck language** —
the canonical context-free language. It is recognised by a pushdown
automaton (PDA), which is exactly a finite automaton augmented with a stack.

The valid parentheses algorithm IS a pushdown automaton:
- States: scanning, accepting
- Stack alphabet: `{), ], }}`
- Transition: push expected closer on open, pop on close

This is why bracket matching is O(n) but cannot be solved with just O(1)
memory (regular languages) — it requires a stack.

---

## Complexity

| | |
|--|--|
| Time | O(n) — single pass |
| Space | O(n) — stack, worst case n/2 for `"(((...)))"` |

---

## Edge Cases

| Input | Output | Reason |
|-------|--------|--------|
| `""` | `true` | Empty — vacuously valid |
| `"("` | `false` | Unclosed opener, stack not empty |
| `")"` | `false` | Closer with empty stack |
| `"([])"` | `true` | Properly nested |
| `"([)]"` | `false` | Wrong nesting order |

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Minimum removals to make valid (#1249) | Count invalid brackets to remove | Two counters: open and close mismatches |
| Minimum additions to make valid (#921) | Count chars to insert | Same two-counter approach |
| Score of parentheses (#856) | Nested depth contributes 2^depth | Stack of scores |
| Longest valid parentheses (#32) | Longest contiguous valid substring | Stack storing indices |
| Valid parentheses with wildcards (#678) | `*` can be `(`, `)`, or `` | Greedy with min/max open count range |
| k different bracket types | `()`, `[]`, `{}`, `<>`, ... | Same stack algorithm; add more pairs to the map |
| Check XML/HTML tags | Variable-length tag names | Stack of strings; match on closing tag |

**Wildcard extension (#678):** Track the range `[minOpen, maxOpen]` of possible open bracket counts. On `(`: both increase. On `)`: both decrease. On `*`: minOpen decreases, maxOpen increases. Valid if at any point `maxOpen >= 0` and final `minOpen == 0`.

**Formal language theory:** Valid parentheses recognises the Dyck language — the canonical context-free language not recognisable by finite automata. k different bracket types with nesting creates a more complex context-free language, still recognisable in O(n) by the same stack algorithm. See USE_CASES.md.
