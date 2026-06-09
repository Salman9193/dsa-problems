# Word Break — Real-World Use Cases

The word break problem — segment an unseparated string into valid dictionary
words using DP — is foundational to NLP, search engines, and developer tooling.

---

## 1. Chinese Word Segmentation — Jieba

Chinese text has no spaces between words. Segmenting a Chinese sentence into
words is exactly the word break problem: given a character string and a
dictionary of valid words, find a valid segmentation.

Jieba, the most widely used Chinese NLP library (used in production at
Alibaba, Tencent, and Baidu), solves this with the same DP approach:

1. Build a Directed Acyclic Graph (DAG) of all possible word matches from the dictionary.
2. Run dynamic programming over the DAG to find the maximum-probability path.
3. For unknown words not in the dictionary, fall back to a Hidden Markov Model
   with the Viterbi algorithm (also a DP variant).

- **Jieba source:** https://github.com/fxsjy/jieba
- **Technical deep-dive:** https://deepwiki.com/fxsjy/jieba

The Viterbi algorithm assigns a state to each character to define word
boundaries — B (beginning), M (middle), E (end), S (single) — finding the
most likely sequence via DP over a trellis structure.

---

## 2. URL Slug & Variable Name Segmentation

URLs, domain names, and programming variable names are often written without
spaces. Search engines, code indexers, and developer tools must segment them
into meaningful words:

```
"speedoflight"    → "speed of light"
"getMaxValue"     → "get Max Value"
"https://github.com/wordbreak" → ["word", "break"]
```

This is word break applied to identifiers — the dictionary is a general
English word list, and DP finds the optimal segmentation.

- **Library:** `WordSegmentationDP` by wolfgarbe
  https://github.com/wolfgarbe/WordSegmentationDP

  Documented use cases include:
  - Keyword extraction from URLs and domain names
  - Automatic CamelCasing of programming variables
  - Correction of OCR errors where spaces are dropped
  - Password analysis (extracting dictionary words from passwords)
  - Speech recognition post-processing (fixing missing word boundaries)

---

## 3. Text Segmentation Research (DP on Scoring)

Academic NLP research formalises text segmentation as a DP optimisation
problem — identical in structure to word break but with a scoring function
instead of a boolean dictionary check.

The optimal segmentation of the first n tokens into k segments is expressed
in terms of the best choice for the last segmentation boundary, accumulating
scores S(n,k) bottom-up. This is the word break recurrence generalised to
continuous scoring.

- **Paper:** *Text Segmentation based on Semantic Word Embeddings*,
  arXiv:1503.05543 (2015)
  https://arxiv.org/pdf/1503.05543
  "Dynamic programming can be used for the text segmentation problem
  formulated in terms of optimizing a scoring objective — expressing the
  optimal segmentation of the first n elements into k segments in terms
  of the best choice for the last segmentation boundary."

---

## 4. Search Engine Query Segmentation

Search engines must parse queries typed without spaces (common on mobile,
or in languages like Chinese/Japanese) into meaningful terms for index lookup.

"newyorktimes" → ["new", "york", "times"]
"applemacbookpro" → ["apple", "macbook", "pro"]

Word break DP over a query dictionary gives the most likely segmentation.
This is a core preprocessing step in query understanding pipelines at
Google, Bing, and Baidu.

- **Wikipedia — Query Understanding:**
  https://en.wikipedia.org/wiki/Query_understanding
  "For languages like Chinese, where words are not separated by spaces,
  segmentation is essential, as individual characters often lack standalone
  meaning."

---

## Summary

| Domain | Input string | Dictionary | Goal |
|--------|-------------|------------|------|
| Chinese NLP (Jieba) | Chinese character sequence | Chinese word list | Segment into words |
| URL / variable parsing | `"speedoflight"` | English word list | Recover word boundaries |
| Text segmentation research | Token sequence | Scored segment boundaries | Optimal boundary placement |
| Search query parsing | `"newyorktimes"` | Search index terms | Split into query terms |

---

## Further Reading

- Jieba Chinese NLP: https://github.com/fxsjy/jieba
- WordSegmentationDP library: https://github.com/wolfgarbe/WordSegmentationDP
- Text segmentation DP paper: https://arxiv.org/pdf/1503.05543
- Query understanding (Wikipedia): https://en.wikipedia.org/wiki/Query_understanding
- Chinese word segmentation survey: Huang & Zhao, *Chinese Word Segmentation: A Decade Review*, 2007
