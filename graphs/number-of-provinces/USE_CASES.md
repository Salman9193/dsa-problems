# Number of Provinces — Real-World Use Cases

Counting connected components in an adjacency matrix is the foundational
operation behind social network community detection and telecom user clustering.

---

## 1. Social Network Community Detection

The `isConnected` matrix in this problem IS a social network adjacency matrix.
The number of provinces = the number of distinct communities at the coarsest
partition level (weakly connected components).

For a non-directional graph, the adjacency matrix can be expressed as the
direct sum of the several adjacency matrices respectively representing each
of the components. Two nodes are in the same weakly connected component if
they are connected through at least one path, regardless of the directions
on any edge along the path. This applies to many real-world examples,
especially social networks.

### The adjacency matrix as community structure

A non-directional social graph (Facebook friendships, LinkedIn connections)
has a symmetric adjacency matrix — `isConnected[i][j] == isConnected[j][i]`
— exactly the matrix in this problem. Rearranging the matrix rows/columns
by component reveals a block-diagonal structure where each block is one
province (community).

Community structure can be identified through the adjacency matrix when it
is close to a block-diagonal form — detecting this structure is the same
as counting connected components.

### References

- **Medium — Community Detection Algorithms Explained:**
  https://sabrinazhengliu.medium.com/community-detection-algorithms-explained-263fde3ab74b
  "For a non-directional graph, the adjacency matrix can be expressed as
  the direct sum of the several adjacency matrices respectively representing
  each of the components. This is the most intuitive type of community
  topology and applies to many real-world examples, especially social networks."

- **ScienceDirect — Community structures detection in time-evolving social networks:**
  https://www.sciencedirect.com/science/article/pii/S1319157821002196
  "The adjacency matrix represents connectivity: connected and disconnected
  nodes are represented by 1 and 0 respectively. Facebook friendship is
  non-directed (symmetric matrix) while Twitter following is directed
  (asymmetric matrix)."

- **arXiv:2405.04371 — Community Detection for Heterogeneous Multiple Social Networks:**
  https://arxiv.org/pdf/2405.04371
  "The proposed method creates adjacency matrices based on network structure
  and content similarity. Community detection across multiple social networks
  identifies the global community structure by finding connected components
  in the combined adjacency matrix."

- **arXiv:2210.08989 — Finding community structure using the ordered random graph model:**
  https://arxiv.org/pdf/2210.08989
  "Visualisation of the adjacency matrix enables us to capture macroscopic
  features of a network when the matrix elements are aligned properly.
  Community structure — a network consisting of several densely connected
  components — can be identified through the adjacency matrix when it is
  close to a block-diagonal form."

---

## 2. Telecom Network — User Community Detection via Adjacency Matrix

Production telecom systems build adjacency matrices from call/message logs
and run connected component analysis to find user communities. The same
`isConnected[i][j]` structure represents whether two users communicated.

A community detection node monitors usage activity for each user of a
communication network. The usage activity is represented by activity links
between various user nodes. Community detection processes the usage activity
to generate an adjacency matrix comprising connectivity information between
individual users. The adjacency matrix includes an indication, in matrix form,
of connectivity information based upon the usage activity between each user node.

This is the exact input format of this LeetCode problem — the telecom system
processes the adjacency matrix using eigenvectors/DFS/Union-Find to count
and identify user communities (provinces).

### Reference

- **USPTO Patent 9491055 — Determining user communities in communication networks:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9491055
  "Community detection node processes usage activity to generate an adjacency
  matrix comprising connectivity information between individual users.
  The adjacency matrix includes connectivity information based upon usage
  activity between each user node — then processes the adjacency matrix
  to determine eigenvectors and identify distinct user communities."

---

## 3. Epidemiology — Contact Network Provinces

In contact tracing (COVID-19, tuberculosis), the adjacency matrix represents
who has had contact with whom. Connected components = distinct transmission
clusters. Each province = one independent outbreak cluster.

```
isConnected[i][j] = 1  ↔  person i and person j had contact
province count         =  number of independent transmission clusters
province membership    =  which cluster each person belongs to
```

Finding provinces answers: "How many independent outbreak clusters exist?"
This prevents cross-cluster quarantine measures from being confused.

---

## Summary

| Domain | City i = | isConnected[i][j]=1 means | Province = |
|--------|---------|--------------------------|-----------|
| Geography (#547) | City | Direct transport link | Connected region |
| Social network | User | Friendship/connection | Friend cluster |
| Telecom | Subscriber | Called/messaged each other | Call community |
| Epidemiology | Person | Had contact | Transmission cluster |
| Chemistry | Molecule | Chemical bond | Distinct molecule |

---

## Further Reading

- Community detection explained: https://sabrinazhengliu.medium.com/community-detection-algorithms-explained-263fde3ab74b
- Time-evolving networks (ScienceDirect): https://www.sciencedirect.com/science/article/pii/S1319157821002196
- Multiple network community detection: https://arxiv.org/pdf/2405.04371
- Adjacency matrix community structure: https://arxiv.org/pdf/2210.08989
- Telecom community detection (USPTO): https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9491055
- Union-Find guide: see `guides/UNION_FIND.md`
