# Best Time to Buy and Sell Stock III — Real-World Use Cases

The k=2 transaction DP is the discrete-time model of bounded-trade
optimal execution studied in quantitative finance theory.

---

## 1. Optimal 2-Trade Strategy — Theoretical Finance

The k-transaction stock problem with k=2 is studied directly in the
quantitative finance literature as "optimal trading with bounded trades":

- **Paper:** *Optimal Online Two-way Trading with Bounded Number of
  Transactions*, arXiv:1706.05320.
  https://arxiv.org/pdf/1706.05320
  "For k trades, the algorithm has competitive ratio φ^((2k+1)/3).
  For k=2 this gives the optimal 2-trade online algorithm. Furthermore
  one of the algorithms resembles common trading strategies involving
  stop-loss limits."

- **Paper:** *A posteriori multi-stage optimal trading under transaction
  costs and a diversification constraint*, arXiv:1709.07527.
  https://arxiv.org/pdf/1709.07527
  "A dynamic programming solution to optimal investment under a fixed
  number of total admissible trades. This is related to the LeetCode #123
  DP — the a posteriori (hindsight) version with k=2 trades."

---

## 2. Pairs Trading — 2-Leg Strategy Optimisation

In pairs trading and spread trading, quants enter two positions (legs)
sequentially. Finding the optimal entry/exit for each leg over a historical
price series uses exactly the 4-state DP — buy1/sell1/buy2/sell2 correspond
to the four events of a 2-leg trade lifecycle.

---

## Further Reading

- Optimal k-trade theory: https://arxiv.org/pdf/1706.05320
- Multi-stage DP trading: https://arxiv.org/pdf/1709.07527
- FinRL backtesting: https://arxiv.org/pdf/2011.09607
