# Inverse Coin Change — Real-World Use Cases

This is an **inverse problem**: given the *outputs* of a process (how many ways each amount
can be formed), recover its hidden *parameters* (the denominations). That "recover the
generator from its counts" shape appears across reverse-engineering and system
identification.

---

## 1. Recovering Hidden Parameters From Observed Counts

The core pattern: you observe aggregate counts produced by an unknown discrete system and
must reconstruct the system. Here it's denominations from way-counts; elsewhere it's
inferring the set of allowed step sizes, packet sizes, or transaction units from observed
frequency-of-total data — a small instance of **system identification** / model recovery.

---

## 2. Generating-Function Inversion

Coin Change II evaluates the generating function `∏ 1/(1 − x^c)`; this problem **inverts**
it — reading off the factors (the coins) from the series coefficients. The same idea
(recover the factors of a product / the structure behind a sequence) underlies decoding
convolutional structure, factoring generating functions, and reconstructing rule sets from
enumeration data.

---

## 3. Consistency Checking / Validation

Because most count arrays are *not* realizable, the algorithm doubles as a **validator**: it
confirms whether an observed way-count table could have come from *any* denomination set,
and rejects impossible ones. That's the reverse-audit of a counting pipeline — useful for
detecting corrupted or fabricated aggregate data.

---

## The Unifying Idea

```
forward:  coins  ──(Coin Change II DP / ∏ 1/(1−x^c))──▶  way-counts
inverse:  way-counts ──(sweep; a +1 bump reveals a coin)──▶  coins
```

| Domain | Observed | Recovered |
|--------|----------|-----------|
| Currency forensics | Ways-to-make each amount | The denomination set |
| System identification | Aggregate outcome counts | Allowed unit sizes |
| Data validation | A claimed count table | Realizable? (or reject) |

---

## Further Reading

- Forward problem: [Coin Change II #518](#dynamic-programming/coin-change-ii).
- Generating functions & partitions: https://en.wikipedia.org/wiki/Partition_(number_theory)
- Inverse problems (recovering parameters from observations): https://en.wikipedia.org/wiki/Inverse_problem
