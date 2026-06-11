# Valid Parentheses — Real-World Use Cases

Stack-based bracket matching is the foundation of every compiler, parser,
and editor — it is one of the most widely deployed algorithms in software.

---

## 1. Compiler Theory — Pushdown Automata & the Dyck Language

The valid parentheses algorithm is the simplest possible **pushdown
automaton (PDA)** — the theoretical model underlying all context-free
language parsers, including every programming language compiler.

The set of valid bracket strings is the **Dyck language**, the canonical
context-free language. It cannot be recognised by a finite automaton
(which has no memory) but is exactly recognised by a PDA (which has a
stack). Every grammar you write in Java, Python, C, or Rust is validated
by a PDA whose core operation is this algorithm.

```
Context-free language hierarchy:
  Regular languages (finite automaton, O(1) memory)
     ↓
  Context-free languages (pushdown automaton, O(n) stack memory) ← here
     ↓
  Context-sensitive languages (linear-bounded automaton)
     ↓
  Recursively enumerable (Turing machine)
```

### References

- **Textbook:** Crespi Reghizzi, Breveglieri & Morzenti —
  *Pushdown Automata and Parsing*, in *Formal Languages and Compilation*,
  Texts in Computer Science, Springer, 2013.
  https://link.springer.com/chapter/10.1007/978-1-4471-5514-0_4
  "Deterministic pushdown automata recognise the context-free languages
  defined by LR(1) and LL(1) grammars — the basis of all practical
  compiler front-ends."

- **Dyck language definition:**
  "The Dyck language of properly nested bracket words is context-free
  and is recognised by a pushdown automaton that uses its stack to track
  unmatched opening brackets." — *Quantum Automata and Quantum Grammars*,
  arXiv:quant-ph/9707031

- **Compiler design tutorial:**
  "Pushdown automata handle nested constructs like parentheses, brackets,
  and function calls. The stack operates on a LIFO basis — the last item
  added is the first removed. For example, checking if ((a+b)*c) has
  balanced parentheses uses a PDA."
  https://www.tutorialspoint.com/compiler_design/compiler_design_pushdown_automata.htm

---

## 2. HTML5 Parsing — The Open Elements Stack

The HTML5 specification (WHATWG Living Standard) defines a stack-based
parser called the **tree construction algorithm**. It maintains a
**stack of open elements** — a direct application of valid parentheses:

- Every opening tag (`<div>`, `<p>`, `<span>`) is pushed onto the stack.
- Every closing tag (`</div>`) must match the element at the top of the
  stack (with complex error recovery for mismatches).
- When the document ends, the stack must be empty.

```
HTML parsing stack trace for "<div><p></p></div>":
  <div>  → push div  stack=[div]
  <p>    → push p    stack=[div, p]
  </p>   → top=p ✓ pop  stack=[div]
  </div> → top=div ✓ pop  stack=[]
  valid ✓
```

The HTML5 parser adds error recovery (the "adoption agency algorithm")
for malformed inputs like `<b><i></b></i>`, but the core mechanism is
valid parentheses applied to tag names.

### References

- **WHATWG HTML Living Standard — Tree Construction:**
  https://html.spec.whatwg.org/multipage/parsing.html#tree-construction
  Defines the "stack of open elements" — the central data structure of
  the HTML5 parser. Every browser (Chrome/Blink, Firefox/Gecko,
  Safari/WebKit) implements this spec.

- **HTML5 parser implementation note:**
  "The HTML5 parser uses insertion modes and a stack of open elements
  for tree construction and error recovery — no context-free grammar,
  but the stack-based tag matching is identical to valid parentheses."
  https://dev.to/milky2018/aaom-03-a-whatwg-html5-parser-driven-by-tests-40o7

---

## 3. Code Editors — Bracket Highlighting & Matching

Every modern editor (VSCode, IntelliJ, Vim, Emacs) uses the valid
parentheses algorithm to:

- Highlight matching brackets when the cursor is on one
- Draw bracket pair guides (the vertical lines in VSCode)
- Warn when brackets are unbalanced (red underlines)
- Enable "jump to matching bracket" (Ctrl+Shift+\)

VSCode's bracket pair colorisation (shipped in v1.60, 2021) runs this
algorithm incrementally as you type — re-validating only the affected
portion of the document on each keystroke.

- **VSCode blog — High-performance bracket pair colorisation:**
  https://code.visualstudio.com/blogs/2021/09/29/bracket-pair-colorization
  "The algorithm uses a stack to track unmatched opening brackets and
  assigns colours based on nesting depth."

---

## Summary

| Domain | "Brackets" | Stack tracks | Reference |
|--------|-----------|--------------|-----------|
| Compiler theory (PDA / Dyck language) | `()`, `[]`, `{}` in source code | Unmatched openers | Crespi Reghizzi et al., Springer 2013 |
| HTML5 browser parsing | `<div>`, `<p>`, `<span>` tags | Open element stack | WHATWG HTML Living Standard |
| Code editors (VSCode) | `()`, `[]`, `{}` as you type | Nesting depth stack | VSCode bracket pair blog, 2021 |

---

## Further Reading

- Pushdown automata and parsing (Springer): https://link.springer.com/chapter/10.1007/978-1-4471-5514-0_4
- WHATWG HTML tree construction: https://html.spec.whatwg.org/multipage/parsing.html#tree-construction
- VSCode bracket pair colorisation: https://code.visualstudio.com/blogs/2021/09/29/bracket-pair-colorization
- Dyck language (Wikipedia): https://en.wikipedia.org/wiki/Dyck_language
