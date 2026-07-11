# Combination Sum IV — Real-World Use Cases

This counts **ordered ways to reach a total using allowed step sizes** — the number of
*integer compositions* of the target with parts restricted to `nums`. That shape appears
wherever order-of-steps matters and you want a count rather than the list.

---

## 1. Counting Paths / Step Sequences

The canonical instance: **ways to climb a staircase** of `n` steps taking allowed step
sizes, where the *order* of steps is a distinct path.

- With steps `{1, 2}` this is exactly **Climbing Stairs** (#70) — the **Fibonacci**
  numbers, since it counts the {1,2}-restricted compositions of `n`.
- Generalized step sets (`{1,2,3}`, dice-like `{1..6}`, custom jumps) count the ordered
  ways to cover a distance — board-game move counting, tiling a 1×n strip with ordered
  colored pieces, rhythmic patterns of given note lengths summing to a bar.

---

## 2. Ordered Make-a-Total Counting

Counting the number of *ordered* ways to reach an exact amount from a set of unit sizes —
sequences of packet sizes filling a buffer, ordered denominations forming an amount when
sequence matters (e.g. a keypad entry order), or the number of distinct time-ordered ways a
process can accumulate to a threshold.

---

## 3. Restricted Compositions in Combinatorics

Directly, this is enumeration of **A-restricted compositions** — a studied combinatorial
object (Heubach & Mansour). Applications include generating-function analysis, random
composition sampling, and modelling ordered partitions of resources or time.

---

## The Unifying Idea

```
allowed parts = nums,  order matters
count ordered sequences summing to target  =  A-restricted integer compositions
dp[t] = Σ dp[t − num]   with the amount loop OUTSIDE (order-sensitive)
```

| Domain | Parts | An "ordered sequence" is |
|--------|-------|--------------------------|
| Stairs / paths | Step sizes | A distinct climb / route |
| Tiling a 1×n strip | Piece lengths | A left-to-right arrangement |
| Rhythm / scheduling | Durations | A time-ordered pattern hitting the total |

---

## Further Reading

- Composition (combinatorics): https://en.wikipedia.org/wiki/Composition_(combinatorics)
- Climbing Stairs (#70) — the {1,2} special case (Fibonacci).
- Contrast: Coin Change II (#518) counts order-*independent* combinations.
