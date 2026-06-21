# Best Time to Buy and Sell Stock IV — Real-World Use Cases

The k-transaction DP is the discrete-time model of bounded-trade
optimal execution — directly studied in theoretical and applied
quantitative finance.

---

## 1. Optimal k-Transaction Trading — Theory

The k-transaction problem (LeetCode #188) is formalized in
quantitative finance as "optimal trading with a bounded number of trades."
Two key papers study exactly this problem:

### Optimal Online Algorithm for k Trades

For the online version (prices revealed one at a time, decide when to buy/sell),
the optimal competitive ratio for k trades is φ^((2k+1)/3) where φ is the
golden ratio. This matches the offline DP (LeetCode #188) as a lower bound.

- **Paper:** *Optimal Online Two-way Trading with Bounded Number of Transactions*,
  arXiv:1706.05320.
  https://arxiv.org/pdf/1706.05320
  "For k trades the algorithm has competitive ratio φ^((2k+1)/3).
  Furthermore we show that this ratio is the best possible by giving
  a matching lower bound."

### Historical (Offline) k-Transaction DP

The offline version (all prices known in advance) is exactly LeetCode #188.
It arises in backtesting as the theoretical maximum profit achievable with
exactly k trades over a historical price series.

- **Paper:** *A posteriori multi-stage optimal trading under transaction costs
  and a diversification constraint*, arXiv:1709.07527.
  https://arxiv.org/pdf/1709.07527
  "A simple algorithm for a posteriori multi-variate multi-stage optimal
  trading, including the discussion of a fixed number of total admissible
  trades. This paper discusses a dynamic programming solution to optimal
  investment under a bound on the admissible number of trades."

---

## 2. Deep Reinforcement Learning for k-Transaction Trading (FinRL)

Modern automated trading systems learn policies that implicitly respect
transaction limits. The FinRL library implements the k-transaction
constraint as a trading environment where the DP solution provides the
optimal oracle policy:

- **Paper:** *FinRL: A Deep Reinforcement Learning Library for Automated
  Stock Trading in Quantitative Finance*, arXiv:2011.09607.
  https://arxiv.org/pdf/2011.09607
  "FinRL incorporates important trading constraints such as transaction cost,
  market liquidity, and the investor's degree of risk-aversion. The k-transaction
  limit is a key constraint modelled in the framework."

---

## 3. The k=2 Special Case — 2-Leg Strategies in Practice

With k=2 (LeetCode #123), the two buy/sell pairs model:
- **Pairs trading:** enter/exit two correlated positions sequentially
- **Calendar spreads:** buy near-month, sell far-month (or vice versa)
- **Swing trading:** capture two independent price swings

The 4-state DP (`buy1, sell1, buy2, sell2`) gives the optimal entry/exit
for each leg over a historical price window.

---

## Summary

| k value | Problem | Finance equivalent |
|---------|---------|-------------------|
| k=1 | #121 | Single long position |
| k=∞ | #122 | Unlimited swing trading |
| k=2 | #123 | 2-leg strategy (pairs, spreads) |
| k=any | **#188** | **Bounded-trade optimal execution** |

---

## Further Reading

- Optimal k-trade theory: https://arxiv.org/pdf/1706.05320
- Multi-stage DP trading (historical): https://arxiv.org/pdf/1709.07527
- FinRL automated trading: https://arxiv.org/pdf/2011.09607
- Optimal trading without backtesting: https://arxiv.org/pdf/1408.1159
