# Minimum Window Substring — Real-World Use Cases

Finding the shortest span covering all required elements appears in
search engines, NLP pipelines, and log analysis.

---

## 1. Apache Lucene / Elasticsearch — Span Queries & Proximity Scoring

Apache Lucene (the engine powering Elasticsearch, Solr, and OpenSearch)
implements the minimum window substring algorithm as its core span query
mechanism. A `SpanNearQuery` finds the minimum covering span — the
shortest document window containing all query terms — and uses span width
as a proximity signal for relevance scoring.

```
Query: "machine learning"
Document: "...recent advances in machine translation and deep learning..."
Minimum covering span: "machine translation and deep learning" (width=5)
→ narrow span → high proximity score
```

The tighter the minimum window, the higher the relevance score.

### How Lucene implements it

A span is a `<doc, startPosition, endPosition>` tuple. `SpanNearQuery`
finds the minimum `endPosition - startPosition` across all positions where
all required terms co-occur — exactly the minimum window substring algorithm
applied to a token position array.

### References

- **Apache Lucene SpanQuery API:**
  https://lucene.apache.org/core/6_2_1/core/org/apache/lucene/search/spans/package-summary.html
  "A SpanNearQuery matches spans which occur near one another. In all
  cases, output spans are minimally inclusive — a span starts at the lesser
  of the two starts and ends at the greater of the two ends."

- **Proximity scoring paper:** *A Short Note on Proximity-based Scoring
  of Documents with Multiple Fields*, arXiv:1709.03260.
  https://arxiv.org/pdf/1709.03260
  "The Expanded Span method extracts the minimum covering span of ordered
  query term matches and scores documents based on span width — a narrower
  span (all query terms close together) scores higher than a wider one.
  The relevance contribution is inversely proportional to width(s)."

- **Elasticsearch full-text search docs:**
  https://www.elastic.co/docs/solutions/search/full-text/how-full-text-works
  "Relevance scoring ranks results by how relevant they are. Query terms
  that are closer together in a document are more supportive of each other
  — proximity metrics identify how well individual term occurrences
  represent the intent of the query."

---

## 2. NLP — Minimum Span for Relation Extraction (Stanford CoreNLP)

Relation extraction systems identify relationships between named entities
(e.g. "Apple acquired Beats") by finding the minimum dependency path
between entity mentions in a sentence — structurally identical to minimum
window substring on a token stream.

```
Sentence:  "Tim Cook, CEO of Apple, announced the acquisition of Beats."
Entities:  t = {PERSON="Tim Cook", ORG="Apple", ORG="Beats"}
Min window: "Tim Cook, CEO of Apple, announced the acquisition of Beats"
            ↑ shortest span containing all three entity types
```

The minimum window determines the candidate relation span — if the
entities are far apart, they are less likely to be in a direct relation.

### References

- **Paper:** Manning et al. — *The Stanford CoreNLP Natural Language
  Processing Toolkit*, ACL System Demonstrations, 2014.
  https://aclanthology.org/P14-5010.pdf
  CoreNLP's relation extractor finds the minimum dependency path between
  entity mentions — analogous to the minimum window — as a feature for
  relation classification.

- **SpaCy relation extraction:**
  https://spacy.io/usage/linguistic-features#dependency-parse
  SpaCy's dependency parser identifies the shortest path between two
  entity tokens (minimum window in dependency tree space) as a feature
  for downstream relation extraction models.

---

## 3. Log Analysis — Minimum Time Window Covering All Event Types

Security and observability systems find the shortest time window in a log
stream that contains all required event types for a complete transaction
or attack pattern.

```
Required events: t = {LOGIN, ACCESS, LOGOUT}
Log stream:      INFO LOGIN ... DEBUG ... ERROR ACCESS ... INFO LOGOUT

Minimum window = smallest time range containing all three event types
→ used for compliance checking, session reconstruction, attack detection
```

This is minimum window substring on an event type stream — `s` is the
log event sequence, `t` is the required event type set.

### References

- **SIEM use case:** Elastic Security SIEM detects attack sequences by
  finding the minimum time window containing all required IOC (Indicator
  of Compromise) event types.
  https://www.elastic.co/security-labs/

- **Apache Flink CEP (Complex Event Processing):**
  https://nightlies.apache.org/flink/flink-docs-release-1.20/docs/libs/cep/
  Flink CEP's `within()` operator finds the minimum time window containing
  a pattern sequence — minimum window substring on a timed event stream.

---

## Summary

| Domain | String s | Pattern t | Minimum window = | Reference |
|--------|----------|-----------|-----------------|-----------|
| Lucene/Elasticsearch | Document token stream | Query terms | Minimum span for proximity scoring | Lucene SpanQuery; arXiv:1709.03260 |
| NLP relation extraction | Sentence token stream | Named entity types | Minimum dependency span between entities | Stanford CoreNLP, ACL 2014 |
| Log analysis / SIEM | Event type stream | Required event types | Shortest time window for complete pattern | Elastic SIEM; Flink CEP |

---

## Further Reading

- Lucene SpanQuery API: https://lucene.apache.org/core/6_2_1/core/org/apache/lucene/search/spans/package-summary.html
- Proximity scoring paper: https://arxiv.org/pdf/1709.03260
- Elasticsearch full-text search: https://www.elastic.co/docs/solutions/search/full-text/how-full-text-works
- Stanford CoreNLP paper: https://aclanthology.org/P14-5010.pdf
- Flink CEP: https://nightlies.apache.org/flink/flink-docs-release-1.20/docs/libs/cep/
