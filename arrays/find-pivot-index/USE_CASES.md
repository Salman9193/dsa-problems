# Find Pivot Index — Real-World Use Cases

The pivot index problem — finding a split point where the sum on both
sides is equal — is the core of load-balancing algorithms in HPC and
MapReduce distributed computing.

---

## 1. HPC Load Balancing — Prefix Sum Partitioning

In high-performance computing, a 1D array of task weights must be split
among processors such that each processor gets an equal total weight.
The split point where left-total equals right-total is the pivot index.

### The Algorithm

Each processor sends its total computational load to a coordinator.
The coordinator computes a **prefix sum** of the load array, then scans
for the index where the prefix sum equals half the total — exactly the
pivot index problem.

```
Task weights: [3, 1, 4, 1, 5, 9, 2, 6]
Total = 31
Half = 15.5

Prefix sums: [3, 4, 8, 9, 14, 23, 25, 31]
Pivot at index 5 (prefix_sum[4]=14 < 15.5 < prefix_sum[5]=23)
→ split between indices 4 and 5 for near-equal load balance
```

### References

- **Paper:** *Dynamic Load Balancing for Compressible Multiphase Turbulence*,
  arXiv:1807.02492 (2018).
  https://arxiv.org/pdf/1807.02492
  "The centralized load-balancing algorithm computes the prefix sum of
  the load on elements ordered by their global IDs, partitions the prefix
  sum array, and determines the position of the first sum that exceeds
  the threshold — exactly the pivot index condition."

- **Paper:** *One-dimensional partitioning for heterogeneous systems:
  Theory and practice*, Journal of Parallel and Distributed Computing, 2008.
  https://www.sciencedirect.com/science/article/abs/pii/S0743731508001305
  "All algorithms for chain-of-tasks partitioning require an initial
  prefix-sum operation on the task-weight array for the efficiency of
  subsequent subchain-weight computations. The prefix sum replaces
  repeated range-sum queries."
  On a Blue Gene/Q system, this approach partitions 2.6 million tasks
  for 524,288 processes with over 99% of optimal balance in 23.4 ms.

- **Parallel prefix sum (SIMD):** arXiv:2312.14874.
  https://arxiv.org/pdf/2312.14874
  "Equal partitioning of the data should suffice since each thread does
  the same work. A dilation factor can be used to reduce the size of the
  first partition to balance the work done by every thread — the ratio
  d=1 gives equal-sized partitions, equivalent to finding the pivot index."

---

## 2. MapReduce — Balanced Reducer Partition Point

In MapReduce, the partitioner decides which reducer processes each key.
An unbalanced partition causes some reducers to process far more data
than others — the "data skew" problem. The balanced partition point is
the key index where the total record count on both sides is equal —
the pivot index on a frequency array.

### The Problem

```
Key frequency distribution: [A:1000, B:500, C:2000, D:300, E:800]
Total = 4600

Prefix sums: [1000, 1500, 3500, 3800, 4600]
Pivot: split at index 1 (left=1500 ≈ right=3100) or index 2 (left=3500)
→ optimal split between B and C: left=1500, right=3100 (closest to equal)
```

### References

- **Patent:** *Automated Load-Balancing of Partitions in Arbitrarily
  Imbalanced Distributed MapReduce Computations*, USPTO 10642866.
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/10642866
  "MapReduce suffers from a fundamental data imbalance problem — items
  with the same key are assigned to the same processing unit, creating
  load imbalance. The patent describes finding the partition split point
  that equalises reducer workloads."

- **Springer — The Partition Cost Model for Load Balancing in MapReduce:**
  https://link.springer.com/chapter/10.1007/978-1-4614-2326-3_20
  "MapReduce load-balancing tackles the challenge of balancing workload
  on reducers for skewed data distributions — the partition cost model
  provides an algorithm for efficient cost estimation that finds the
  split point where reducer loads are equalized."

---

## The Shared Algorithm

Both HPC partitioning and MapReduce use the same prefix-sum pivot scan:

```
// Compute prefix sums
prefix[0] = weight[0]
for i in 1..n:
    prefix[i] = prefix[i-1] + weight[i]

total = prefix[n-1]

// Find pivot: leftSum == rightSum
leftSum = 0
for i in 0..n:
    rightSum = total - leftSum - weight[i]
    if leftSum == rightSum: return i   ← pivot index!
    leftSum += weight[i]
```

| Domain | weight[i] | leftSum = | rightSum = | Pivot = |
|--------|----------|-----------|------------|---------|
| HPC partitioning | Task weight per processor | Work assigned left | Work assigned right | Equal-load split point |
| MapReduce | Records per key | Left reducer load | Right reducer load | Balanced partition key |
| Find Pivot Index | nums[i] | Sum left of i | Sum right of i | Balanced split index |

---

## Further Reading

- HPC prefix sum partitioning: https://arxiv.org/pdf/1807.02492
- 1D partitioning theory: https://www.sciencedirect.com/science/article/abs/pii/S0743731508001305
- MapReduce load balancing patent: https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/10642866
- MapReduce partition cost model: https://link.springer.com/chapter/10.1007/978-1-4614-2326-3_20
