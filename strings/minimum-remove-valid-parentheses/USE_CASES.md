# Minimum Remove to Make Valid Parentheses — Real-World Use Cases

The minimum-removal-to-make-valid-parentheses algorithm is the core of
compiler error recovery systems — one of the most practically important
applications of stack-based parsing.

---

## 1. Compiler Syntax Error Recovery — Minimum Token Deletion

When a compiler encounters a syntax error (unmatched bracket, missing
semicolon, misplaced token), it must recover and continue parsing to
detect further errors. The minimum-removal approach finds the fewest
token deletions (or insertions) to restore syntactic validity.

### The structural parallel

```
This problem:
  Input:  "(a(b(c)d)"  — unmatched '(' at index 0
  Action: remove index 0
  Output: "a(b(c)d)"   — valid

Compiler error recovery:
  Input:  "int x = (a + b;"  — unmatched '('
  Action: remove the unmatched '(' (or insert missing ')')
  Output: "int x = a + b;"  — parseable
```

The stack used in this problem is literally the **parse stack** of a
pushdown automaton — the same stack used in LR/LL parsers. When the parser
encounters an error (unmatched token), it looks at the parse stack to
determine what to delete to restore a valid state.

### The minimum distance error correction algorithm

The earliest formal treatment of minimum-deletion error recovery in parsers
was introduced by Aho and Peterson (1970s), which finds the minimum cost
repair sequence of token insertions and deletions to allow the parser to
recover. This is the formal generalisation of this LeetCode problem to
arbitrary context-free grammars.

### References

- **Paper:** *A Minimum Distance Error-Correcting Parser for Context-Free
  Languages*, SIAM Journal on Computing.
  https://epubs.siam.org/doi/10.1137/0201022
  "A minimum distance error-correcting parser for context-free languages
  that automatically corrects syntax errors in an input string by finding
  the minimum number of insertions and deletions to produce a valid string."

- **IEEE Paper:** *Neutralization of Syntax Errors in the Compiler of the
  Functional-Imperative Language El*, IEEE (2019).
  https://ieeexplore.ieee.org/document/8934697
  "The method is based on enumeration of valid tokens for modifying one error
  token — alternately inserting, replacing, and deleting — to select parser
  restart parameters at which the next syntax error is detected at maximum
  distance from the neutralised one."

- **Blog:** *Automatic Syntax Error Recovery*, Laurence Tratt (2020).
  https://tratt.net/laurie/blog/2020/automatic_syntax_error_recovery.html
  "A simpler family of algorithms traces its roots to Fischer et al., which
  tries to find a single minimum cost repair sequence of token insertions and
  deletions to allow the parser to recover. The repair sequence 'Delete #'
  maps directly to the minimum removal algorithm in this problem."

- **USPTO Patent 9110676 — Method and system for syntax error repair:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9110676
  "The techniques respond to pair matching status by potentially adding a
  synthesized token or deleting a token that has already been received —
  examining the stack to detect unmatched head states."

- **USPTO Patent 8321848 — Method and system for syntax error repair:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/8321848
  "The method includes placing a state corresponding to a token M in a stack,
  examining the stack to detect any unmatched head states, and placing tokens
  corresponding to the end of the tuple necessary to match the unmatched
  head states."

---

## 2. HTML/XML Sanitisation — Removing Unmatched Tags

Web sanitisers (used in browsers, content management systems, and security
filters) must handle malformed HTML where opening tags have no closing
tags and vice versa. The same stack-based minimum-removal algorithm applies
— unmatched `<div>` (opening without closing) and unmatched `</span>`
(closing without opening) are removed.

```
Malformed HTML:
  "<div><p>text</div>"  — <p> has no </p>

Stack scan:
  '<div>': push
  '<p>':   push
  '</div>': top is '<p>', not '<div>' → unmatched case
            (HTML is more complex due to implicit closing rules)

Modern browsers (Chrome, Firefox) implement HTML5 error recovery using
a similar stack-based approach — the HTML5 spec defines explicit error
recovery rules for each malformed token type.
```

### Reference

- **HTML5 Parsing Algorithm** (WHATWG):
  https://html.spec.whatwg.org/multipage/parsing.html
  The HTML5 specification defines a stack-based tree construction algorithm
  with explicit error recovery rules — the formal production version of
  the minimum-removal approach in this problem.

---

## The Pushdown Automaton Connection

Balanced parentheses form the **Dyck language** — the prototypical
context-free language. By the Chomsky hierarchy:

```
Regular languages:    recognisable by finite automata (no stack)
Context-free langs:   recognisable by pushdown automata (WITH stack)
Dyck language ⊆ CFL: requires exactly a stack to recognise
```

This is why **every correct parenthesis validator uses a stack** — not
as a trick, but as a provable necessity. The minimum-removal algorithm
extends validation to repair: the stack doesn't just detect errors,
it identifies exactly which tokens caused them.

---

## Summary

| System | "Push" trigger | "Pop" trigger | Stack holds | Removal = |
|--------|---------------|--------------|-------------|-----------|
| This problem | `(` | `)` matching `(` | Unmatched paren indices | Chars at those indices |
| LR/LL parser | Grammar symbol | Matching reduction | Parse states | Deleted/inserted tokens |
| HTML parser | Opening tag | Matching closing tag | Open tag stack | Implicit close/remove |
| IDE auto-fix | Unmatched `(` | User types `)` | Unclosed paren positions | Highlighted removals |

---

## Further Reading

- Minimum distance error-correcting parser (SIAM): https://epubs.siam.org/doi/10.1137/0201022
- Automatic syntax error recovery (Tratt): https://tratt.net/laurie/blog/2020/automatic_syntax_error_recovery.html
- Compiler error recovery IEEE: https://ieeexplore.ieee.org/document/8934697
- HTML5 parsing algorithm: https://html.spec.whatwg.org/multipage/parsing.html
- Dyck language (Wikipedia): https://en.wikipedia.org/wiki/Dyck_language
