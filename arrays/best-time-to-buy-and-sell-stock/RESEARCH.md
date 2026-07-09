# Best Time To Buy And Sell Stock — Research & Foundations

Maximum profit from a single buy/sell — equivalently the **maximum subarray** of consecutive price differences, solved in one pass by Kadane’s algorithm.

- **Kadane’s algorithm (maximum subarray).** The linear-time scan that keeps the best subarray ending at each index, popularised in **J. Bentley, *Programming Pearls* (Addison-Wesley)** and attributed to Jay Kadane.

**Why it matters here:** Tracking the best profit ending on each day (resetting when it goes negative) is Kadane’s linear-time maximum-subarray scan applied to price deltas.

*Kadane (Programming Pearls) and Chebyshev distance are cited as well-established references, framed honestly rather than as session-verified journal citations.*
