# 3Sum — Real-World Use Cases

The 3Sum problem has two remarkable real-world connections: it is a
foundational hardness benchmark in theoretical computer science, and
it directly models triangular arbitrage in currency markets.

---

## 1. Computational Geometry — 3SUM-Hardness

The 3Sum problem is the canonical benchmark for O(n²) lower bounds
in computational geometry. Dozens of geometric problems have been
proven "3SUM-hard" — meaning they require at least O(n²) time unless
3Sum itself can be beaten.

### What 3SUM-hard means

A problem P is 3SUM-hard if 3Sum can be reduced to P in subquadratic
time. This means: if you could solve P in o(n²) time, you could solve
3Sum in o(n²) time — which is conjectured to be impossible.

### Problems proven to be 3SUM-hard (from the original paper)

- Given n lines in the plane, do any three meet at a single point?
- Given n triangles in the plane, does their union contain a hole?
- Certain visibility and motion planning problems in robotics
- Determining whether a set of n points contains three collinear points

All of these reduce to 3Sum and inherit its O(n²) lower bound.

### The conjecture and its evolution

The original conjecture (Gajentaan & Overmars, 1995) stated that 3Sum
requires Ω(n²) time in the worst case. In 2014, Grønlund & Pettie gave
a slightly subquadratic algorithm, refining the conjecture: 3Sum cannot
be solved in O(n^{2-ε}) time for any ε > 0 (the "truly subquadratic"
barrier). This modern conjecture remains widely accepted and continues
to drive lower-bound research in fine-grained complexity theory.

### References

- **Foundational paper:** Gajentaan & Overmars — *On a class of O(n²)
  problems in computational geometry*, Computational Geometry: Theory
  and Applications, 5:165–185, 1995.
  https://people.csail.mit.edu/virgi/6.s078/papers/gajovermars.pdf
  "There is no hope of obtaining o(n²) solutions for 3SUM-hard problems
  unless we can improve the solution for the base problem."

- **Subquadratic breakthrough:** Grønlund & Pettie — *Threesomes,
  Degenerates, and Love Triangles*, FOCS 2014.
  https://arxiv.org/pdf/1404.0799
  "The 3SUM conjecture has been replaced by a relaxed, modern variant
  asserting that 3SUM cannot be solved in O(n^{2-ε}) time for any ε > 0."

- **Survey:** King — *A Survey of 3SUM-Hard Problems*, 2004.
  https://www.cs.mcgill.ca/~jking/papers/3sumhard.pdf

---

## 2. Triangular Arbitrage in Currency Markets

In foreign exchange markets, triangular arbitrage exploits a pricing
discrepancy among three currencies. A trader starts with currency A,
converts to B, then to C, then back to A — if the product of the three
exchange rates is not exactly 1, a profit (or loss) results.

### The connection to 3Sum

On log-transformed exchange rates, a profitable triangle means:

```
log(r_AB) + log(r_BC) + log(r_CA) ≠ 0
```

Detecting zero-sum triplets (no arbitrage, rates are consistent) vs
non-zero triplets (arbitrage opportunity) is a direct instance of the
3Sum problem on real-valued arrays. The sort + two-pointer approach
gives O(n²) detection across all currency pairs — the same complexity
as the LeetCode solution.

### How it works in practice

A triangular arbitrage strategy involves three trades:
1. Exchange currency A for B
2. Exchange B for C
3. Exchange C back to A

If the final amount exceeds the starting amount (after fees), an
arbitrage opportunity exists. High-frequency trading systems scan all
three-currency combinations in milliseconds.

### References

- **Wikipedia — Triangular Arbitrage:**
  https://en.wikipedia.org/wiki/Triangular_arbitrage
  "A triangular arbitrage strategy involves three trades, exchanging
  the initial currency for a second, the second for a third, and the
  third for the initial. The arbitrageur locks in a zero-risk profit
  from the discrepancy when the market cross exchange rate is not
  aligned with the implicit cross exchange rate."

- **Paper:** *Efficient Triangular Arbitrage Detection via Graph Neural
  Networks*, arXiv:2502.03194 (2025).
  https://arxiv.org/abs/2502.03194
  "Traditional methods for detecting triangular arbitrage opportunities,
  such as exhaustive search algorithms and linear programming solvers,
  often suffer from high computational complexity and may miss potential
  opportunities in dynamic markets."

- **Research paper:** *Detecting correlations and triangular arbitrage
  opportunities in the Forex by means of multifractal detrended
  cross-correlations analysis*, arXiv:1906.07491.
  https://arxiv.org/pdf/1906.07491
  Models the EUR→USD→CHF→EUR cycle as a product of three exchange rates
  and detects arbitrage when the product deviates from 1.

---

## Summary

| Domain | Three elements | Sum/product condition | Reference |
|--------|---------------|-----------------------|-----------|
| Computational geometry (3SUM-hard) | Integers / geometric primitives | Sum = 0 | Gajentaan & Overmars, CGTA 1995 |
| Triangular currency arbitrage | Log exchange rates | Log-sum ≠ 0 → profit | arXiv:2502.03194; Wikipedia |

---

## Further Reading

- Gajentaan & Overmars (1995): https://people.csail.mit.edu/virgi/6.s078/papers/gajovermars.pdf
- Grønlund & Pettie (2014): https://arxiv.org/pdf/1404.0799
- 3SUM survey: https://www.cs.mcgill.ca/~jking/papers/3sumhard.pdf
- Triangular arbitrage (Wikipedia): https://en.wikipedia.org/wiki/Triangular_arbitrage
- GNN arbitrage detection: https://arxiv.org/abs/2502.03194
