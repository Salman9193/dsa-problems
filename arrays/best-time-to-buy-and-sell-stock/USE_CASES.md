# Best Time to Buy and Sell Stock — Real-World Use Cases

The prefix-minimum scan — find the best buy point before the best sell point
in a price series — is foundational in algorithmic trading and cloud cost
optimisation.

---

## 1. Algorithmic Trading — Backtesting Frameworks

The maximum theoretical profit of a single long position over a historical
price window is the foundational benchmark every backtesting framework
computes. It establishes the upper bound against which real strategies are
evaluated — no real strategy can beat the "perfect hindsight" single trade.

### How backtesting frameworks use it

Vectorized backtesting applies the prefix-minimum scan across entire price
series using array operations. Event-driven backtesting simulates the same
scan one bar at a time, enforcing the buy-before-sell constraint via a
`shift()` on the price data to prevent look-ahead bias.

- **Zipline** (Quantopian's backtesting engine):
  https://github.com/quantopian/zipline
  https://zipline.ml4trading.io/
  "Zipline is a Pythonic event-driven system for backtesting. It provides
  numerous built-in computations and separates alpha factor computation
  from order placement and portfolio bookkeeping."

- **QuantConnect / Lean** (production-grade backtesting):
  https://github.com/QuantConnect/Lean

- **Backtrader** (most popular Python backtesting library):
  https://www.backtrader.com/

- **Vectorized vs event-driven:**
  "When using vectorized backtests, it's essential to apply a shift()
  operation to ensure decisions are based only on prior-period data —
  exactly the constraint that buy must precede sell."
  https://www.quantvps.com/blog/guide-to-quantitative-trading-strategies-and-backtesting

---

## 2. Cloud Spot Instance Cost Optimisation

AWS, GCP, and Azure spot/preemptible instances have fluctuating prices.
Finding the cheapest window to run a compute job — start at the minimum
price, finish before the price spikes — is the best-time-to-buy-and-sell
problem applied to compute costs:

```
stock price series  →  spot instance price series
buy day             →  job start time
sell day            →  job completion time
max profit          →  max cost savings vs on-demand price
```

The constraint is identical: you must start (buy) before you finish (sell),
and you want to maximise the price difference (savings).

- **Paper:** Wu et al. — *Optimizing Spot Instance Savings under Deadlines*,
  USENIX NSDI 2024.
  https://www.usenix.org/system/files/nsdi24-wu-zhanghao.pdf
  "Scheduling policies periodically decide whether a job should remain in
  the same state or switch to another — striking a balance between cost
  optimisation and deadline adherence to leverage low-cost spot instances."

- **Paper:** Bhuyan et al. — *Exploiting Spot Instances for Time-Critical
  Cloud Workloads Using Optimal Randomized Strategies*,
  arXiv:2601.14612 (2025).
  https://arxiv.org/pdf/2601.14612
  "The ROSS randomized scheduling algorithm achieves a provably optimal
  competitive ratio of √K under reasonable deadlines, outperforming the
  state-of-the-art by up to 30% in cost savings across diverse spot market
  conditions on Azure and AWS traces."

- **AWS Spot Instance documentation:**
  https://aws.amazon.com/blogs/compute/cost-optimization-and-resilience-eks-with-spot-instances/
  "Companies like Netflix, Uber, and Intel report saving millions in compute
  costs by offloading batch jobs to Spot instances."

---

## The Shared Algorithm

Both domains apply the same prefix-minimum scan:

```
minCost    = ∞
maxSavings = 0

for each time step t:
    if cost[t] < minCost:
        minCost = cost[t]          // best entry point so far
    else:
        savings = cost[t] - minCost  // profit if we "exit" here
        // for stock: profit = sell - buy
        // for spot:  savings = on_demand_price - spot_price_at_start
        maxSavings = max(maxSavings, savings)
```

---

## Summary

| Domain | Price series | minPrice = | maxProfit = | Reference |
|--------|-------------|------------|-------------|-----------|
| Stock trading (backtesting) | Historical stock prices | Best buy day | Max single-trade return | Zipline; QuantConnect/Lean |
| Cloud spot scheduling | Spot instance price per hour | Cheapest compute window | Max cost savings vs on-demand | Wu et al. NSDI 2024; arXiv:2601.14612 |

---

## Further Reading

- Zipline backtesting: https://github.com/quantopian/zipline
- QuantConnect Lean: https://github.com/QuantConnect/Lean
- Spot instance optimisation (NSDI 2024): https://www.usenix.org/system/files/nsdi24-wu-zhanghao.pdf
- ROSS scheduling (arXiv 2025): https://arxiv.org/pdf/2601.14612
- AWS Spot blog: https://aws.amazon.com/blogs/compute/cost-optimization-and-resilience-eks-with-spot-instances/
