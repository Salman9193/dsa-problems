# Partition Equal Subset Sum — Real-World Use Cases

The equal subset sum / 0-1 knapsack DP is foundational in combinatorial
optimisation, cryptography, and resource allocation.

---

## 1. Cryptography — Merkle-Hellman Knapsack Cryptosystem

The subset sum problem is the mathematical foundation of the first
public-key cryptosystem after RSA — the Merkle-Hellman knapsack
cryptosystem (1978).

### How it works

```
Private key: a super-increasing sequence (a₁, a₂, ..., aₙ)
  where each aᵢ > sum of all previous terms
  → subset sum is easy to solve greedily for super-increasing sequences

Public key: disguise the sequence using modular arithmetic
  b = W × a mod q (where W, q are large numbers)
  → looks like a hard general subset sum instance

Encryption: encode binary message m as sum of bᵢ where mᵢ = 1
Decryption: solve subset sum using the easy super-increasing private key
```

The partition problem (this LeetCode problem) is a special case where
`target = total/2` — exactly the problem of splitting an encrypted message
into two equal halves.

### Why it was broken

Shamir (1982) broke Merkle-Hellman using lattice reduction (LLL algorithm),
showing that **NP-complete average-case** difficulty doesn't guarantee
cryptographic hardness. The easy instances (super-increasing disguised as
hard) are detectable.

- **Wikipedia — Subset sum problem (cryptography section):**
  https://en.wikipedia.org/wiki/Subset_sum_problem
  "The special case where target T = (a₁+...+aₙ)/2 is the partition problem.
  The Merkle-Hellman knapsack cryptosystem is based on the hardness of subset sum."

- **arXiv:1211.2338 — Knapsack-based P2P encryption:**
  https://arxiv.org/pdf/1211.2338
  "Knapsack public-key encryption schemes are based on the subset sum problem,
  which is NP-complete. The original knapsack set serves as the private key
  while the transformed knapsack set serves as the public key."

- **Wikipedia — Merkle-Hellman:**
  https://en.wikipedia.org/wiki/Merkle%E2%80%93Hellman_knapsack_cryptosystem

---

## 2. Resource Allocation & Fair Division

The partition problem is the formal model for splitting resources fairly
between two parties:

### Applications

- **Load balancing:** split computational tasks equally across 2 servers —
  task processing times are the array values, equal partition = balanced load
- **Fair division:** estate/inheritance splitting — can assets be divided
  so both parties receive equal value?
- **Manufacturing:** can production jobs be split equally between two shifts?
- **Scheduling:** can n jobs be scheduled on 2 identical machines with equal
  completion time (makespan = total/2)?

### References

- **Wikipedia — Knapsack problem (applications):**
  https://en.wikipedia.org/wiki/Knapsack_problem
  "Knapsack problems appear in real-world decision-making processes such as
  finding the least wasteful way to cut raw materials, selection of investments
  and portfolios, selection of assets for asset-backed securitisation, and
  generating keys for knapsack cryptosystems."

- **CMSC 451 — Subset Sum NP-Completeness lecture notes:**
  https://www.cs.umd.edu/class/spring2025/cmsc451-0101/Lects/lect21-apx-sub-sum.pdf
  "Subset sum is a special case of 0/1 knapsack where weight = value.
  It is one of Karp's 21 NP-complete problems. A DP pseudo-polynomial
  algorithm exists with O(n × target) time."

- **Garey & Johnson — *Computers and Intractability: A Guide to the Theory
  of NP-Completeness*, 1979.**
  The definitive reference establishing the partition problem as NP-complete.
  Karp's original 1972 paper (21 NP-complete problems) includes subset sum.

---

## 3. Why the DP Is Pseudo-Polynomial (Not Truly Polynomial)

The DP runs in O(n × target) = O(n × sum/2). This is polynomial in n and
the NUMERIC VALUE of the input, but exponential in the INPUT SIZE (number of
bits needed to represent the values).

```
Input size = n × log(max_value) bits
O(n × sum) = O(n × n × max_value) = O(n² × 2^{log(max_value)})
                                      exponential in log(max_value)!
```

This is why the problem is NP-complete in general (large values):

- **arXiv:1802.09465 — Strong NP-Completeness of Rational Problems:**
  https://arxiv.org/pdf/1802.09465
  "The Partition problem is strongly NP-complete. The target weight W chosen
  in the reduction is exactly equal to half of the total weights of all items
  — the PARTITION problem instance. Since SUBSET SUM is a generalisation of
  PARTITION, the SUBSET SUM problem with rational weights is strongly NP-complete."

---

## Summary

| Domain | Array values | Target | DP gives |
|--------|-------------|--------|---------|
| Partition problem (this) | Integers | total/2 | Boolean: equal split possible? |
| Merkle-Hellman cryptosystem | Disguised super-increasing seq. | Ciphertext | Decrypt message |
| Load balancing (2 machines) | Task durations | total_time/2 | Boolean: equal load possible? |
| Fair division | Asset values | estate_value/2 | Boolean: equal split possible? |

---

## Further Reading

- Subset sum problem (Wikipedia): https://en.wikipedia.org/wiki/Subset_sum_problem
- Merkle-Hellman cryptosystem: https://en.wikipedia.org/wiki/Merkle%E2%80%93Hellman_knapsack_cryptosystem
- Knapsack problem (Wikipedia): https://en.wikipedia.org/wiki/Knapsack_problem
- Subset sum NP-completeness (UMD): https://www.cs.umd.edu/class/spring2025/cmsc451-0101/Lects/lect21-apx-sub-sum.pdf
- Strong NP-completeness: https://arxiv.org/pdf/1802.09465
- Garey & Johnson 1979: search "Garey Johnson Computers and Intractability"
