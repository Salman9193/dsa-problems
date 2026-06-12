# Two Sum II — Real-World Use Cases

The two-pointer scan on sorted arrays is one of the most widely deployed
database algorithms — it is the foundation of the sort-merge join used
in every major relational database system.

---

## 1. Database Sort-Merge Join (PostgreSQL, Oracle, SQL Server)

The sort-merge join is the direct production implementation of the
two-pointer pattern on sorted arrays. Given two sorted relations R and S
joined on a key attribute, the algorithm runs two pointers — one through
R, one through S — advancing the pointer with the smaller key until a
match is found. This is exactly Two Sum II with the target replaced by
equality (`R[i].key == S[j].key`).

```
Sort-Merge Join pseudocode (structurally identical to Two Sum II):

i = 0, j = 0
while i < |R| and j < |S|:
    if   R[i].key < S[j].key:  i++   ← same as: sum < target → left++
    elif R[i].key > S[j].key:  j++   ← same as: sum > target → right--
    else: emit (R[i], S[j]), i++, j++ ← same as: sum == target → return
```

### Key properties (shared with Two Sum II)

- Requires both inputs to be sorted on the join key.
- Completes in a single pass — O(n + m) time.
- Requires no extra memory beyond the two pointers — O(1) space.
- Most efficient when inputs are already sorted (e.g. via index scans).

### When PostgreSQL uses it

PostgreSQL's query planner chooses sort-merge join when:
- Both tables have indexes on the join columns (pre-sorted inputs).
- The query has a range join condition (>=, <=) — hash join can't handle ranges.
- The output needs to be ordered — sort-merge produces sorted output for free.

### References

- **PostgreSQL merge join internals:**
  https://postgrespro.com/blog/pgsql/5969770
  "The merge join algorithm uses two pointers which point at the current
  rows of the two sorted sets. The merge is completed in one pass over
  the two datasets and does not require any extra memory."

- **Original paper:** Blasgen & Eswaran — *Storage and Access in
  Relational Data Bases*, IBM Research, 1977.
  Introduced the sort-merge join as part of the IBM System R prototype —
  the first relational database system. This paper is the origin of both
  sort-merge join and hash join.

- **Sort-merge join (Wikipedia):**
  https://en.wikipedia.org/wiki/Sort-merge_join
  "The key idea of the sort-merge algorithm is to first sort the relations
  by the join attribute, so that interleaved linear scans will encounter
  matching sets at the same time."

- **Use-the-index-luke.com:**
  https://use-the-index-luke.com/sql/join/sort-merge-join
  "The strength of the sort-merge join emerges when the inputs are already
  sorted — the index order can be exploited to avoid sort operations entirely."

- **Vlad Mihalcea — Merge Join Algorithm:**
  https://vladmihalcea.com/merge-join-algorithm/
  "The Merge Join algorithm is used when joining larger tables in sorted
  order — supported by Oracle, SQL Server, and PostgreSQL."

---

## The Algorithm Correspondence

| Two Sum II | Sort-Merge Join |
|-----------|----------------|
| `numbers[left]` | Current row in relation R |
| `numbers[right]` | Current row in relation S |
| `sum < target` → `left++` | `R[i].key < S[j].key` → `i++` |
| `sum > target` → `right--` | `R[i].key > S[j].key` → `j++` |
| `sum == target` → return | `R[i].key == S[j].key` → emit join |
| O(1) space | O(1) space (no hash table) |
| Single pass | Single pass |

Two Sum II is literally a simplified sort-merge join where both relations
are the same array and we're looking for a specific sum rather than equality.

---

## Further Reading

- PostgreSQL sort-merge join: https://postgrespro.com/blog/pgsql/5969770
- Sort-merge join (Wikipedia): https://en.wikipedia.org/wiki/Sort-merge_join
- Use-the-index-luke.com: https://use-the-index-luke.com/sql/join/sort-merge-join
- Vlad Mihalcea merge join: https://vladmihalcea.com/merge-join-algorithm/
