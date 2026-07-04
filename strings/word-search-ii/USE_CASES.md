# Word Search II — Real-World Use Cases

Searching many patterns at once by walking a shared prefix structure (a Trie)
is the backbone of autocomplete, spell-checking, multi-pattern text scanning,
and word games. The "one traversal searches for all patterns" idea powers
production systems that would be far too slow searching one pattern at a time.

---

## 1. Autocomplete & Search Suggestions

Search boxes suggest completions by walking a Trie of the dictionary (or query
log) along the typed prefix and collecting all words below that node. The same
prefix-sharing that makes Word Search II fast makes autocomplete respond in
microseconds over millions of terms.

- **Elasticsearch completion suggester (FST/Trie-backed):**
  https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester
  Uses a finite-state transducer (a compressed Trie) for prefix suggestions.

- **Apache Lucene FST (compressed Trie):**
  https://blog.mikemccandless.com/2010/12/using-finite-state-transducers-in.html
  How Lucene stores terms in a memory-efficient Trie-like automaton.

---

## 2. Multi-Pattern Text Search — Aho-Corasick

Scanning a document for **many** keywords at once uses the Aho-Corasick
automaton — a Trie of all patterns augmented with failure links. Like Word
Search II, it walks the text once and matches all patterns simultaneously,
instead of running a separate search per keyword. Used in intrusion detection,
content filtering, and DNA sequence scanning.

- **Aho-Corasick algorithm — Wikipedia:**
  https://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_algorithm
  "The algorithm locates all occurrences of a finite set of patterns within an
  input text simultaneously... it constructs a finite-state machine resembling
  a trie."

- **Snort / Suricata IDS** use Aho-Corasick for multi-signature matching:
  https://suricata.io/

---

## 3. Spell Checking & Fuzzy Word Matching

Spell checkers store the dictionary as a Trie (or DAWG) and traverse it with a
bounded edit-distance search to find valid words near a misspelling — the same
prune-as-you-walk pattern, but the "board" is the edit-distance search space.

- **Directed Acyclic Word Graph (DAWG) — Wikipedia:**
  https://en.wikipedia.org/wiki/Deterministic_acyclic_finite_state_automaton
  A minimized Trie used by spell checkers and word-game validators to store
  huge word lists compactly.

- **Hunspell** (spell checker used by LibreOffice, Chrome, Firefox):
  https://github.com/hunspell/hunspell

---

## 4. Word Games — Boggle & Scrabble Solvers

Word Search II is essentially a Boggle solver. Real Boggle and Scrabble engines
store the legal-word dictionary in a Trie (or GADDAG for Scrabble) and DFS the
board, pruning the instant the current path is not a valid prefix — exactly this
problem's algorithm.

- **GADDAG data structure (Scrabble move generation) — Wikipedia:**
  https://en.wikipedia.org/wiki/GADDAG
  A specialized Trie variant that generates Scrabble moves by walking from any
  point in a word outward.

- **Steven Gordon, "A Faster Scrabble Move Generation Algorithm" (1994)** —
  the paper introducing GADDAG:
  https://ericsink.com/downloads/faster-scrabble-gordon.pdf

---

## Summary

| Domain | Patterns in the Trie | Traversal being pruned |
|--------|---------------------|------------------------|
| Autocomplete | Dictionary / query terms | Prefix walk to collect completions |
| Multi-pattern search | Keyword set (Aho-Corasick) | Single pass over the text |
| Spell checking | Valid words (Trie / DAWG) | Bounded edit-distance search |
| Word games | Legal words (Trie / GADDAG) | Board DFS with prefix pruning |

---

## Further Reading

- Aho-Corasick: https://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_algorithm
- Lucene FST: https://blog.mikemccandless.com/2010/12/using-finite-state-transducers-in.html
- Elasticsearch completion suggester: https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester
- GADDAG (Scrabble): https://en.wikipedia.org/wiki/GADDAG
- DAWG: https://en.wikipedia.org/wiki/Deterministic_acyclic_finite_state_automaton
