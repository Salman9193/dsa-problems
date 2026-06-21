# Best Time to Buy and Sell Stock II — Real-World Use Cases

The greedy "sum all positive differences" strategy is the theoretical upper
bound for unlimited-transaction trading — used as a benchmark and baseline
in quantitative finance backtesting.

---

## 1. Backtesting Benchmark — Perfect Hindsight Upper Bound

The unlimited-transaction greedy strategy represents the maximum possible
profit achievable with perfect hindsight and no transaction costs. Every
quantitative trading backtesting framework uses this as a theoretical ceiling:

```
Theoretical max (greedy sum) = sum of all positive daily diffs
Actual strategy profit ≤ Theoretical max
```

This benchmark tells practitioners how much profit a strategy "leaves on the
table" compared to the theoretical optimum.

- **FinRL** — A DRL library for automated stock trading uses unlimited-transaction
  backtesting as the baseline benchmark environment:
  *FinRL: A Deep Reinforcement Learning Library for Automated Stock Trading*,
  arXiv:2011.09607. https://arxiv.org/pdf/2011.09607

- **Zipline / Backtrader** — Both frameworks compute the "theoretical max"
  using exactly this greedy scan as a sanity-check baseline before evaluating
  real strategies with transaction constraints.

---

## 2. Multi-Period Trading — Optimal Historical Strategy

The greedy decomposition proof (any multi-day profit = sum of daily diffs)
is the foundation of multi-period trading theory:

- **Paper:** *A posteriori multi-stage optimal trading under transaction costs
  and a diversification constraint*, arXiv:1709.07527.
  https://arxiv.org/pdf/1709.07527
  "The mathematical approaches differ significantly (convex optimization vs.
  graph search). Optimal trading based on historical data maximises the sum of
  positive price increments under various constraints."

---

## Further Reading

- FinRL backtesting: https://arxiv.org/pdf/2011.09607
- Multi-stage optimal trading: https://arxiv.org/pdf/1709.07527
- Greedy vs DP guide: see `GREEDY_VS_DP.md`
