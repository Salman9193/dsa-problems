# Possible Bipartition — Real-World Use Cases

Possible Bipartition models the 2-group conflict partitioning problem —
a foundational structure in social network analysis, recommendation systems,
and scheduling. Production platforms like LinkedIn and Facebook use bipartite
graph algorithms at scale across their core products.

---

## 1. LinkedIn — Job Matching via Bipartite Graphs (Direct Production Use)

LinkedIn's job recommendation system is built on a bipartite graph of
candidates × job listings. The same bipartite structure in Possible
Bipartition — two distinct groups with edges only between groups, not
within — is the foundation of LinkedIn's production matching algorithms.

LinkedIn's job matching platform (1 billion+ members) uses Graph Neural
Networks on a bipartite graph where candidates and jobs are the two
node types, and edges represent applications, views, and interactions.

Job matching on LinkedIn presents unique challenges including sparse
engagement and dynamic job listings. The system constructs a bipartite
graph of job offers and job seekers, where the weights of edges can vary
to adapt to different datasets. Graph Neural Networks make use of network
effects, enhancing the use of semantic information.

### Connection to Possible Bipartition

```
Possible Bipartition (#886):        LinkedIn job matching:
  people = nodes                  ↔  candidates + jobs = nodes
  dislike edge = conflict         ↔  interaction edge = interest/application
  2 groups = bipartite partition  ↔  candidates | jobs = bipartite split
  check bipartiteness             ↔  build bipartite embedding for GNN
```

### References

- **arXiv:2402.13430 — LinkSAGE: Optimizing Job Matching Using Graph
  Neural Networks (LinkedIn, 2024):**
  https://arxiv.org/pdf/2402.13430
  "Job matching on LinkedIn presents unique challenges. The platform,
  with its extensive data on job opportunities and members exceeding 1
  billion, uses GNNs on bipartite graphs. GNNs are adept at handling
  dynamic relationships, managing complex relational datasets, and
  representing diverse data types as node types in a bipartite structure."

- **ScienceDirect — A general graph learning based framework for job
  offer recommender systems:**
  https://www.sciencedirect.com/science/article/abs/pii/S0952197625017154
  "This framework constructs a bipartite graph of job offers and job
  seekers from characteristics based on descriptions and interactions.
  Experimented on Nigham and LinkedIn datasets using Node2Vec and
  GraphSAGE algorithms on the candidate-job bipartite graph."

- **arXiv:1801.00377 — Help Me Find a Job: A Graph-based Approach for
  Job Recommendation at Scale:**
  https://arxiv.org/pdf/1801.00377
  "The CF approach can be expressed as a link prediction problem in a
  user-item bipartite graph in which edges reflect the interaction
  between users and items. Bipartite graphs are used for grocery product
  recommendations where nodes include both products and consumers."

---

## 2. Social Network Conflict Partitioning — Community Detection

Possible Bipartition is a special case of community detection: given a
conflict graph, can we 2-partition nodes so that all conflicts are between
groups? The general version (k > 2 groups) is the core of community
detection research, and bipartite detection (k=2) is the foundation.

Community detection using bipartite network analysis identifies hidden
group structures in social networks. Facebook's social graph uses bipartite
subgraph analysis (HITS algorithm) to identify authorities vs hubs in
community structures. The bipartite graph represents, on one hand, the
interests of a community (authorities) and, on the other hand, those who
cite the community (hubs).

A bipartite check of the conflict graph is the first step before any
k-community detection — if k=2 suffices, bipartite checking is O(V+E);
if not (odd cycle found), more groups are needed (potentially NP-hard for k≥3).

### References

- **arXiv:1406.6705 — Link Analysis for Communities Detection on Facebook:**
  https://arxiv.org/pdf/1406.6705
  "Subgraphs with a bipartite structure in Facebook's graph: the bipartite
  graph represents, on the one hand, the interests of the community
  (authorities according to HITS) and, on the other hand, those who cite
  the community (hubs). This highlights the potential of sharing similar
  interests by communities."

- **ResearchGate — Modularity and Community Detection in Bipartite Networks:**
  https://www.researchgate.net/publication/5617826_Modularity_and_community_detection_in_bipartite_networks
  "Bipartite modularity quantifies the strength of partitions in two-way
  networks, based on how people vote in social elections. The BiVoting
  algorithm efficiently mines communities in large-scale bipartite networks."

- **arXiv:0804.3636 — Overlapping Community Detection in Bipartite Networks:**
  https://arxiv.org/pdf/0804.3636
  "In the scientific collaboration network, two different types of nodes
  represent authors and papers respectively; in the movie-actor network,
  actors connect to films; in the recommendation network, edges link each
  customer to products. All are bipartite structures where Possible
  Bipartition's 2-group check is the foundational feasibility test."

---

## 3. Exam and Resource Scheduling — 2-Slot Conflict Scheduling

If the conflict graph between exams (or tasks) is bipartite, exactly 2
time slots suffice to schedule everything. Possible Bipartition directly
answers: "Can all exams be scheduled in 2 slots with no conflicts?"

```
Scheduling application:
  nodes = exams / tasks
  edge = two exams share a student (conflict — can't be same slot)
  bipartite? = can schedule in exactly 2 slots
  groups = {slot 1 exams}, {slot 2 exams}
```

If false (odd cycle), at least 3 slots are needed (3-colouring, NP-hard in general).

---

## 4. Maximum Bipartite Matching — The Next Step After Checking

Once we confirm the graph IS bipartite (Possible Bipartition returns true),
the natural next step is **maximum bipartite matching**: find the maximum
number of conflict-free pairs (e.g., maximum job assignments).

There are many real-world problems that can be formed as bipartite matching.
For example: M job applicants and N jobs, each applicant interested in a
subset of jobs. Find an assignment such that as many applicants as possible
get jobs. This is solved via Ford-Fulkerson on the bipartite graph.

Possible Bipartition (#886) answers the feasibility question.
Maximum Bipartite Matching then optimises WITHIN the feasible partition.

### Reference

- **GeeksforGeeks — Maximum Bipartite Matching:**
  https://www.geeksforgeeks.org/dsa/maximum-bipartite-matching/
  "There are many real-world problems that can be formed as Bipartite
  Matching. For example: M job applicants and N jobs. Each job opening
  can only accept one applicant. Find an assignment of jobs to applicants
  such that as many applicants as possible get jobs."

---

## What Platforms Actually Use — vs What This Problem Models

| Platform | What they run | Connection to #886 |
|----------|--------------|-------------------|
| LinkedIn | Bipartite GNN (LinkSAGE) on candidates×jobs | Bipartite graph foundation |
| Facebook | HITS algorithm on authority/hub bipartite subgraphs | Community bipartite structure |
| Netflix/Spotify | Collaborative filtering on users×items bipartite graph | Recommendation bipartite |
| Compilers | Register allocation — interference graph 2-colouring | Direct 2-group bipartition |
| Universities | Exam scheduling — conflict graph 2-colouring | Direct application |

In production, bipartite checking (#886) is step 1. Production systems then
run maximum matching, GNN embedding, or community detection on the bipartite
structure. The 2-colouring feasibility check of Possible Bipartition is the
prerequisite that enables all downstream bipartite algorithms.

---

## Further Reading

- LinkSAGE LinkedIn job matching (arXiv): https://arxiv.org/pdf/2402.13430
- Job recommendation bipartite graph (ScienceDirect): https://www.sciencedirect.com/science/article/abs/pii/S0952197625017154
- Facebook community bipartite (arXiv): https://arxiv.org/pdf/1406.6705
- Bipartite community detection (ResearchGate): https://www.researchgate.net/publication/5617826_Modularity_and_community_detection_in_bipartite_networks
- Maximum bipartite matching (GeeksforGeeks): https://www.geeksforgeeks.org/dsa/maximum-bipartite-matching/
- Is Graph Bipartite? (#785): see `graphs/is-graph-bipartite/`
