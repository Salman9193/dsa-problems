# Coin Change II — Real-World Use Cases

This counts the **order-independent ways to hit an exact total from repeatable unit sizes**
— the number of *partitions* of the amount with parts restricted to `coins` (the
denumerant). It shows up wherever you want *how many distinct multisets* reach a target, not
the list.

---

## 1. Making Change — the Literal Case

The number of distinct ways to make an amount from available denominations (order of coins
irrelevant) — a classic cashier/kiosk and currency-design question: given a denomination
set, how many ways can each amount be formed? Currency systems are partly designed around
these counts.

---

## 2. Resource / Budget Partitioning

Counting distinct ways to fill a fixed budget or capacity from repeatable resource units —
packaging a quota from standard pack sizes, allocating a total across repeatable slot sizes,
or counting the distinct bill-of-materials multisets that meet a spec.

---

## 3. Restricted Partition Counting (Combinatorics)

Directly, `dp[amount]` is the number of **partitions of `amount` into parts from `coins`** —
a studied object with an exact generating function. Applications include generating-function
analysis, integer-programming feasibility counts, and probability over discrete sums.

---

## The Unifying Idea

```
repeatable parts = coins,  order irrelevant
count multisets summing to amount  =  restricted partitions (the denumerant)
dp[a] += dp[a - coin]  with the COIN loop OUTSIDE (order-independent)
```

| Domain | Repeatable parts | A "combination" is |
|--------|------------------|--------------------|
| Cash / kiosks | Coin denominations | A distinct way to make the amount |
| Budgeting | Standard unit sizes | A distinct allocation multiset |
| Combinatorics | Allowed part sizes | A restricted partition |

---

## Further Reading

- Partition (number theory): https://en.wikipedia.org/wiki/Partition_(number_theory)
- Contrast: [Combination Sum IV #377](#dynamic-programming/combination-sum-iv) counts ordered
  sequences (compositions).
- Inverse: [Inverse Coin Change #3592](#dynamic-programming/inverse-coin-change) recovers the
  coins from these counts.
