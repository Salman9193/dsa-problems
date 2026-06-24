# Evaluate Division — Real-World Use Cases

Weighted path products on directed graphs appear in two production domains:
currency exchange rate inference and unit conversion systems.

---

## 1. Currency Exchange Rate Chains & Arbitrage Detection

The evaluate division problem is the exact mathematical model of currency
exchange via intermediate currencies. When no direct exchange rate exists
between two currencies, you chain through intermediate currencies —
multiplying conversion rates along the path.

### The structural parallel

```
Evaluate Division:
  equation a/b = 2.0, b/c = 3.0
  query a/c → 2.0 × 3.0 = 6.0

Currency exchange:
  GBP/USD = 1.27, USD/AUD = 1.43
  query GBP/AUD → 1.27 × 1.43 = 1.82 (no direct rate needed)
```

### Arbitrage detection

A profitable round-trip (buy GBP → USD → AUD → GBP and end with more than
you started with) corresponds to a cycle whose edge weight product > 1.
Detecting this uses Bellman-Ford on log-negated weights:

```
log(GBP/USD) + log(USD/AUD) + log(AUD/GBP) > 0  →  arbitrage!

Negate logs: apply Bellman-Ford to find negative-weight cycle
A negative cycle in the negated-log graph = arbitrage opportunity
```

### References

- **Reasonable Deviations — Graph algorithms and currency arbitrage:**
  https://reasonabledeviations.com/2019/03/02/currency-arbitrage-graphs/
  "To find the exchange rate between two currencies that do not share an
  edge, we simply find a path between the two currency vertices and walk
  along it, multiplying by each successive edge weight. Bellman-Ford on
  log-negated weights detects negative cycles = arbitrage opportunities."

- **Paper:** *Graph Learning for Foreign Exchange Rate Prediction and
  Statistical Arbitrage*, arXiv:2508.14784.
  https://arxiv.org/pdf/2508.14784
  "Consider a currency triplet A, B, C. Exchange A→B→C→A. In theory the
  final amount of A should be 1 (no arbitrage). In practice it sometimes
  exceeds 1, allowing potential profits. We formulate FX rate prediction
  as an edge-level regression problem on a spatiotemporal graph."

- **Paper:** *Graph-based Semi-Supervised & Active Learning for Edge Flows*,
  arXiv:1905.07451.
  https://arxiv.org/pdf/1905.07451
  "The arbitrage-free condition translates into requiring the edge flows in
  the exchange network to be cycle-free (curl-free condition)."

---

## 2. Unit Conversion Systems — USPTO Patent 6944595

Production unit conversion systems (financial platforms, measurement tools,
scientific calculators) implement the evaluate division algorithm when a
direct conversion rate is unavailable.

### How it works

```
Known rates:   km/mile = 1.609,   mile/yard = 1760
Query:         km/yard = ?
Path:          km → mile → yard
Product:       1.609 × 1760 = 2831.84
```

The system finds a path of conversion rates and multiplies them — identical
to the BFS path product in the evaluate division solution.

### Reference

- **USPTO Patent 6944595 — Apparatus and method for performing conversion
  between different units of currency using an encapsulated conversion path:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/6944595
  "Building a compound exchange rate object containing an exchange rate path —
  an ordered list of exchange rate objects each specifying a from-currency,
  to-currency, and conversion multiplier. If no direct rate exists, the system
  determines whether multiple exchange rates may be combined to form a path
  spanning from source to target. The compound rate is computed by multiplying
  along the ordered list."

---

## The Log-Transform Connection

For arbitrage detection, the multiplicative path product is converted to an
additive path sum via logarithms:

```
product of edge weights = exp(sum of log-weights)
max product path       = min (-log-weight) path  → Dijkstra
cycle with product > 1 = cycle with log-sum > 0  → Bellman-Ford detects negative cycle
```

This log-transform connects the evaluate division graph to classical
shortest-path algorithms — making the full suite of graph algorithms
(Dijkstra, Bellman-Ford, Floyd-Warshall) applicable to multiplicative graphs.

---

## Summary

| Domain | Nodes | Edge weight | Path product = | Reference |
|--------|-------|-------------|----------------|-----------|
| Evaluate Division (#399) | Variables | equation ratio | Answer to query | LeetCode |
| Currency exchange | Currencies | exchange rate | Cross rate (e.g. GBP/AUD) | Reasonable Deviations |
| Arbitrage detection | Currencies | log exchange rate | Negative cycle = profit | arXiv:1905.07451 |
| Unit conversion | Units | conversion factor | Unknown unit ratio | USPTO 6944595 |

---

## Further Reading

- Currency arbitrage graphs: https://reasonabledeviations.com/2019/03/02/currency-arbitrage-graphs/
- FX graph learning: https://arxiv.org/pdf/2508.14784
- Edge flow arbitrage condition: https://arxiv.org/pdf/1905.07451
- Unit conversion patent: https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/6944595
