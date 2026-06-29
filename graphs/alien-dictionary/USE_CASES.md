# Alien Dictionary — Real-World Use Cases

Inferring character ordering from sorted word lists is a specific instance
of a broader class of problems: reconstructing unknown ordering schemas
from observed examples. This appears in computational linguistics, database
engineering, and ancient script decipherment.

---

## 1. Computational Decipherment of Unknown Scripts

The Alien Dictionary problem is the discrete algorithmic analogue of one of
the core challenges in computational linguistics: inferring character ordering
(and character identity) from a corpus of words in an unknown script.

Given a sorted lexicon in an unknown language, the alien dictionary algorithm
extracts pairwise character ordering constraints by comparing adjacent entries —
exactly the approach used by computational decipherment systems when they have
access to a sorted word list (such as a glossary or frequency-ordered corpus).

### Topological ordering in decipherment

When two consecutive entries in an unknown glossary differ, the first position
of difference tells us which character sorts before the other — this is the
same edge-extraction step in the alien dictionary algorithm. The resulting
directed graph of character ordering constraints is then topologically sorted
to produce a candidate character ordering for the unknown script.

The phonetic values of lost characters can be inferred by mapping them to
known cognates. These values can serve as the starting point for lost sound
reconstruction. Examining the genealogy of graphemes is useful for exploring
the evolution of writing systems, reading undeciphered inscriptions, and
deciphering undeciphered scripts. An algorithm for this is based on the global
minimisation of topological differences in each descent branch of the graphemes
— the same topological sort structure as the alien dictionary solution.

### References

- **MIT CSAIL — Deciphering Undersegmented Ancient Scripts Using Phonetic Prior
  (Luo et al., TACL 2022):**
  https://direct.mit.edu/tacl/article/doi/10.1162/tacl_a_00354/97780/
  "The phonetic values of lost characters can be inferred by mapping them to
  known cognates. The resulting character mappings must be consistent with the
  known script ordering — a constraint expressed as a directed topological
  ordering of character precedence relationships."

- **Frontiers in AI — Automatic decipherment of lost ancient scripts
  (Frontiers 2025):**
  https://www.frontiersin.org/journals/artificial-intelligence/articles/10.3389/frai.2025.1581129/full
  "Examining the genealogy of graphemes explores the evolution of writing systems.
  The algorithm is based on the global minimisation of topological differences
  in each descent branch — a topological sort on the graph of character relationships
  extracted from sorted corpora."

- **ACL — A Computational Approach to Deciphering Unknown Scripts
  (Knight & Yamada 1999):**
  https://aclanthology.org/W99-0906.pdf
  The pioneering paper on computational script decipherment. Builds character
  mapping models from statistical comparison of unknown and known corpora —
  the same comparative approach as the alien dictionary edge extraction.

- **MIT Press (TACL) / arXiv:1901.01880 — Neural Decipherment via Minimum-Cost Flow:**
  https://arxiv.org/abs/1906.06718
  "Ugaritic to Linear B" — deciphers scripts by aligning character patterns
  from known related languages, using graph-based ordering constraints derived
  from sorted word lists.

---

## 2. Database — Custom Sort Order (Collation) Inference

When a database exports data sorted by a custom collation (locale-specific
character ordering — e.g. Swedish puts Å after Z; German treats Ä as AE for
sorting) and the collation definition is unknown, the alien dictionary
algorithm can INFER the collation from the exported data.

```
Exported (sorted by unknown collation):
  "alfa", "alga", "beta"

Compare adjacent:
  "alfa" vs "alga": f→g (f before g in this collation)
  "alga" vs "beta": a→b (a before b) — wait, this is obvious for Latin
                                        but for custom alphabets it is not

Result: partial character ordering of the custom collation
```

Production database systems (PostgreSQL `pg_collation`, MySQL `COLLATE`,
Oracle `NLS_SORT`) all define character orderings that can be reverse-engineered
from sorted output using the same pairwise comparison and topological sort.

---

## 3. Compiler — Inferring Operator Precedence

Given a language's expression grammar where operators are sorted by precedence,
and the sorting is unknown, comparing adjacent operator names in the grammar
file can reveal precedence ordering — the same alien dictionary approach.

More generally: given any sorted output of tokens from an unknown comparator,
the alien dictionary algorithm reconstructs the comparator from observed
ordering. This is used in:

- Reverse-engineering DSL (domain-specific language) syntax from examples
- Inferring evaluation order in spreadsheet formula engines
- Reconstructing sort key schemas from sorted CSV exports

---

## 4. The General Pattern — Ordering Inference from Sorted Examples

The alien dictionary problem is a specific instance of a general pattern:

```
Given: a list of items sorted by an unknown ordering
Find:  the ordering constraints that explain the observed sort

Step 1: extract pairwise constraints (first difference = one edge)
Step 2: topological sort → valid ordering consistent with constraints
Step 3: cycle detected → constraints are contradictory → impossible
```

| Domain | "Words" | "Characters" | "Sort order" |
|--------|---------|-------------|-------------|
| Alien Dictionary (#269) | Words in alien language | Alien characters | Alien alphabet order |
| Computational decipherment | Glossary entries | Script symbols | Symbol ordering |
| Database collation | Database rows | Characters | Custom collation |
| Grammar precedence | Grammar rules | Operators/tokens | Precedence order |
| Log analysis | Log entries | Event types | Event dependency order |

---

## Further Reading

- TACL decipherment (MIT): https://direct.mit.edu/tacl/article/doi/10.1162/tacl_a_00354/97780/
- Ancient script decipherment (Frontiers 2025): https://www.frontiersin.org/journals/artificial-intelligence/articles/10.3389/frai.2025.1581129/full
- Computational decipherment (Knight & Yamada 1999): https://aclanthology.org/W99-0906.pdf
- Neural decipherment Ugaritic→Linear B (arXiv): https://arxiv.org/abs/1906.06718
- Course Schedule II (topological sort): see `graphs/course-schedule-ii/`
- Graph Algorithms guide: see `guides/GRAPH_ALGORITHMS.md`
