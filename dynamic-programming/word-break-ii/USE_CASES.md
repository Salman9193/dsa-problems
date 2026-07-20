# Word Break II — Real-World Use Cases

Enumerating **all valid ways to split a string into known units** is the core of tokenizing any
language without explicit word boundaries — and of several parsing and forensics problems.

---

## 1. Chinese / Japanese / Thai Word Segmentation (the big one)

These languages are written **without spaces**, so *every sentence is a Word Break problem*.
Segmenters build exactly this DAG — position → all dictionary words starting there — and then:

- **Full mode** = this problem: enumerate all combinations (used to maximise search-index recall).
- **Default mode** = pick the **most probable** path via a max-log-probability DP over the same DAG.

```
我爱北京天安门  →  我 / 爱 / 北京 / 天安门
```

The ambiguity is genuine: `北京大学生` parses as `北京大学 / 生` (Peking University / student) or
`北京 / 大学生` (Beijing / university student). Frequencies decide.

> Production version in this repo:
> [Chinese Word Segmenter LLD](https://salman9193.github.io/system-design/#lld-chinese-word-segmenter)
> and [Text Segmentation Service HLD](https://salman9193.github.io/system-design/#hld-text-segmentation-service).

---

## 2. Search Indexing & Query Parsing

Index-time tokenization often wants **all** plausible splits (better recall), while query-time wants
the single best one (better precision). Same DAG, two relaxations — and the two **must agree on the
dictionary**, or documents become unreachable.

---

## 3. URL, Hashtag & Identifier Splitting

Splitting concatenated text into words: `#thisisagreatday` → `this is a great day`,
domain names (`expertsexchange` — famously ambiguous), and code identifiers. Same problem, English
dictionary.

---

## 4. Compound-Word Languages

German and Dutch productively compound (`Donaudampfschifffahrtsgesellschaft`). Decompounding for
search or translation is Word Break with a morpheme dictionary.

---

## 5. Password / Passphrase Analysis

Security auditing: can a password be decomposed into dictionary words? Enumerating decompositions
measures how guessable it is against a wordlist attack.

---

## The Unifying Idea

```
string with no delimiters + a dictionary of units
   ⇒ DAG: position i → j whenever s[i..j-1] is a known unit
   ⇒ "is there a path?"  = Word Break #139   (polynomial)
   ⇒ "all paths?"        = Word Break II #140 (exponential output)
   ⇒ "best path?"        = weighted DP        (linear — what production uses)
```

| Domain | The "units" | What you want |
|--------|-------------|---------------|
| Chinese segmentation | dictionary words | the **most probable** path |
| Search indexing | dictionary words | **all** paths (recall) |
| Hashtag splitting | English words | the most probable path |
| Decompounding | morphemes | all paths, then rank |

---

## Further Reading

- Chinese word segmentation: https://en.wikipedia.org/wiki/Chinese_word_segmentation
- jieba (the reference implementation): https://github.com/fxsjy/jieba
- Related: [Word Break #139](#dynamic-programming/word-break),
  [Implement Trie #208](#strings/implement-trie) (the dictionary structure).
