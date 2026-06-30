# Most Stones Removed with Same Row or Column — Real-World Use Cases

The "union on shared attribute, not pairwise comparison" pattern — connecting
items via shared row/column hub nodes — is a standard technique for finding
connected groups in relational and tabular data without expensive pairwise
comparisons.

---

## 1. This Problem IS a Standard Union-Find Teaching Example

Multiple independent technical references confirm this is one of the
canonical Union-Find problems for connected component identification.

The solution uses the Union-Find (Disjoint Set Union) data structure to
efficiently track and merge connected components of stones. Since all
stones are connected through shared rows or columns, the maximum
removable stones equals the total number of successful union operations,
which is equivalent to total stones minus the number of connected components.

We use Union-Find to group stones that share a row or column into the
same connected component. Once grouped, the number of removable stones
equals the total number of stones minus the number of connected components.

### References

- **AlgoMonster — 947. Most Stones Removed (In-Depth Explanation):**
  https://algo.monster/liteproblems/947
  "This problem maps to Disjoint Set Union through a short path in the
  full flowchart. Union stones that share a row or column, then the answer
  is the number of stones minus the number of connected components. DSU
  (Union-Find) is the suggested approach for handling connectivity
  problems efficiently."

- **GeeksforGeeks — Maximum Stones Removal:**
  https://www.geeksforgeeks.org/dsa/maximum-stones-removal/
  "Any two stones in the same row or column are connected by an edge. By
  repeatedly merging the stones connected by an edge, we can form a
  connected component. In each such component, all stones except one can
  be removed, because the final remaining stone has no other stone in its
  row or column to justify its removal."

- **Data Structure Notebook (Union-Find applications):**
  https://blog.enkr1.com/data-structure-notebook/
  "Union-Find is particularly useful in problems that involve finding
  connected components in a graph — for instance, grouping stones in the
  Most Stones Removed with Same Row or Column problem. Path Compression
  and Union by Rank make Union-Find operations almost constant time, O(α(n))."

---

## 2. Relational Database Connected Components — Drug-Protein Interaction Networks

The "union on shared attribute" pattern (row hub / column hub) generalises
directly to finding connected components in any relational table where
rows represent links between two entity types — exactly the structure of
this problem applied to biomedical interaction data.

In a drug-protein interaction table with columns drug_id and protein_id,
each row represents a link between a particular drug and a particular
protein. The goal is to identify all connected components — drug and
protein interaction networks — from the table, using Union-Find on the
bipartite drug-protein graph.

```
Most Stones Removed:              Drug-Protein Interaction Network:
  stone (row, col)             ↔   interaction record (drug_id, protein_id)
  union(row hub, col hub)      ↔   union(drug node, protein node)
  connected component          ↔   one interaction network/cluster
  stones - components          ↔   (not directly analogous — the goal here
                                     is finding the networks, not maximising
                                     removal, but the UNDERLYING algorithm —
                                     bipartite connectivity via DSU — is identical)
```

### Reference

- **PharmaSUG 2015 — Implementing Union-Find Algorithm with Base SAS:**
  https://pharmasug.org/proceedings/2015/BB/PharmaSUG-2015-BB06.pdf
  "Table 1 contains drug_id and protein_id, the primary keys of drugs and
  proteins in a relational database. Each row represents an interaction or
  link between a particular drug and protein. In graph theory terminology,
  we need to find all connected components of a graph from a given set of
  vertices and edges — here, vertices are drugs and proteins, and edges are
  the ordered pairs of drug and protein interactions."

---

## 3. Tabular Data Connected Components — General Pattern

Beyond stones and drug-protein pairs, the row/column union pattern applies
to any tabular dataset where you want to find clusters of records connected
through shared field values, without comparing every pair of records.

We start with basic graph concepts and walk through algorithms based on
graph traversal and disjoint set union, demonstrating how to compute
connected components directly on tabular data without data migration or ETL.

```
General pattern:
  Records sharing a field value (row, column, customer_id, device_id, etc.)
  → union(record, field_value_hub)
  → connected components reveal clusters of related records

Applications:
  - Spreadsheet cells sharing a formula dependency
  - Customer records sharing a payment method (fraud rings)
  - Log events sharing a session ID or trace ID (distributed tracing clusters)
```

### Reference

- **PuppyGraph — Connected Components: Graph Algorithm Guide:**
  https://www.puppygraph.com/blog/connected-components
  "We'll walk through algorithms based on graph traversal and disjoint set
  union, and demonstrate how to compute connected components directly on
  tabular data using a graph layer, without data migration or ETL."

---

## Summary

| Domain | "Row" hub = | "Column" hub = | Connected component = |
|--------|------------|-----------------|------------------------|
| Most Stones Removed (#947) | Row value | Column value | Removable stone group |
| Drug-protein networks | Drug ID | Protein ID | Interaction cluster |
| Spreadsheet dependencies | Row formula group | Column formula group | Recalculation cluster |
| Fraud detection | Shared device/IP | Shared payment method | Suspicious account ring |

---

## Further Reading

- AlgoMonster explanation: https://algo.monster/liteproblems/947
- GeeksforGeeks maximum stones removal: https://www.geeksforgeeks.org/dsa/maximum-stones-removal/
- PharmaSUG Union-Find in SAS: https://pharmasug.org/proceedings/2015/BB/PharmaSUG-2015-BB06.pdf
- Connected components on tabular data: https://www.puppygraph.com/blog/connected-components
- Union-Find guide: see `guides/UNION_FIND.md`
- Accounts Merge (related DSU problem): see `graphs/accounts-merge/`
