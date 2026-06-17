# Is Graph Bipartite? — Real-World Use Cases

Bipartite graph detection underlies recommendation systems, scheduling,
and even the quantum chemistry of organic molecules.

---

## 1. Recommendation Systems — User-Item Bipartite Graph

Netflix, Amazon, Spotify, and YouTube model user-item interactions as
bipartite graphs: users on one side, items (movies, products, songs) on
the other. Every edge (user liked/watched/bought an item) connects a node
from the user group to a node from the item group — the defining property
of a bipartite graph.

### Why bipartiteness matters

Collaborative filtering algorithms (matrix factorisation, GNN-based CF)
assume the interaction graph is bipartite — users never interact with
other users, items never interact with other items directly. Verifying
bipartiteness validates the graph structure before training.

```
Bipartite user-item graph G = (U, I, E):
  U = user set
  I = item set
  E ⊆ U × I  (edges only between U and I, never within)

Adjacency matrix structure:
    A = [ 0   R  ]
        [ R^T  0 ]
where R is the user-item rating matrix.
```

Graph Neural Networks for recommendation (NGCF, LightGCN) exploit this
block-diagonal adjacency structure — it only works because the graph is bipartite.

### References

- **Paper:** Maier & Simovici — *Bipartite Graphs and Recommendation Systems*,
  Journal of Advances in Information Technology (JAIT), Vol. 13, No. 3, 2022.
  https://www.jait.us/uploadfile/2022/0428/20220428061814827.pdf
  "Recommendation systems focus on items in which users might be interested.
  For streaming companies such as Netflix or Amazon Prime, the user-item
  interaction is naturally modelled as a bipartite graph — persons on one
  side, movies they like on the other."

- **Paper:** *Recommendation as link prediction in bipartite graphs*,
  Decision Support Systems, Elsevier.
  https://dl.acm.org/doi/10.1016/j.dss.2012.09.019
  "By mapping transactions to a bipartite user-item interaction graph,
  a recommendation problem is converted into a link prediction problem,
  where the graph structure captures subtle information on relations
  between users and items."

- **Paper:** *Incremental Collaborative Filtering Considering Temporal Effects*,
  arXiv:1203.5415.
  https://arxiv.org/pdf/1203.5415
  "The rating matrix of the collaborative filtering domain can be elegantly
  represented by a weighted undirected bipartite graph — one set of nodes
  are items, the other set are users, with links going only between nodes
  of different sets."

- **Medium — How Graph Theory Powers Recommendation Systems:**
  https://medium.com/@pranjith.athira/how-graph-theory-powers-recommendation-systems-like-netflix-88a41d9a3226

---

## 2. Chemistry — Alternant Hydrocarbons & Hückel MO Theory

In organic chemistry, **alternant hydrocarbons** are conjugated carbon
molecules whose carbon skeleton forms a bipartite graph. The 2-colouring
(starred ★ / unstarred ○ atoms) is a fundamental concept in Hückel
Molecular Orbital (HMO) theory, predicting molecular orbital properties.

### The bipartite structure of alternant hydrocarbons

Carbon atoms in the skeleton are divided into two groups:
- **Starred (★):** every neighbour is unstarred
- **Unstarred (○):** every neighbour is starred

This is exactly the 2-colouring from the bipartite check algorithm.

```
Butadiene (CH2=CH-CH=CH2):
  C1★ — C2○ — C3★ — C4○
  Bipartite: A={C1,C3}, B={C2,C4}

Benzene (C6H6):
  C1★-C2○-C3★-C4○-C5★-C6○-C1★
  Bipartite: A={C1,C3,C5}, B={C2,C4,C6}

Azulene (non-alternant):
  Contains a 5-membered ring → odd cycle → NOT bipartite
  → asymmetric MO spectrum → large dipole moment
```

### What bipartiteness predicts

- **Alternant (bipartite):** molecular orbital energies are symmetric about
  zero (paired as ±β values). Small dipole moments.
- **Non-alternant (not bipartite):** asymmetric MO energies. Large dipole
  moments. Different reactivity patterns.

The **Hückel theorem**: a graph is bipartite if and only if its adjacency
matrix spectrum is symmetric about 0 — a fundamental result connecting
bipartite graph theory to quantum chemistry.

### References

- **Paper:** *Two Mathematical Papers Relevant for the Hückel MO Theory*,
  MATCH Communications in Mathematical and Computer Chemistry, Vol. 72, 2014.
  https://match.pmf.kg.ac.rs/electronic_versions/Match72/n2/match72n2_565-572.pdf
  "A graph G is bipartite if and only if its spectrum is symmetric with
  respect to the origin. The adjacency matrix of the Hückel graph gives
  the molecular orbital energies directly — bipartiteness predicts the
  pairing of energy levels."

- **Wikipedia — Hückel method:**
  https://en.wikipedia.org/wiki/H%C3%BCckel_method
  "Molecules with molecular orbitals paired up such that only the sign
  differs (e.g. α ± β) are called alternant hydrocarbons. This is in
  contrast to non-alternant hydrocarbons such as azulene that have large
  dipole moments. The Hückel theory is more accurate for alternant hydrocarbons."

- **Chemistry LibreTexts — Hückel MO Model of Conjugation:**
  https://chem.libretexts.org/Courses/University_of_Wisconsin_Oshkosh/Chem_370

---

## Summary

| Domain | Two groups (bipartition) | Edge = | Bipartite check validates |
|--------|-------------------------|--------|--------------------------|
| Recommendation systems | Users ↔ Items | User-item interaction | Graph structure for CF/GNN models |
| Chemistry (HMO theory) | Starred ★ ↔ Unstarred ○ carbons | C-C bond | Symmetric MO spectrum, small dipole |

---

## Further Reading

- Bipartite graphs in recommender systems: https://www.jait.us/uploadfile/2022/0428/20220428061814827.pdf
- Recommendation as link prediction: https://dl.acm.org/doi/10.1016/j.dss.2012.09.019
- Hückel MO bipartite theorem: https://match.pmf.kg.ac.rs/electronic_versions/Match72/n2/match72n2_565-572.pdf
- Hückel method (Wikipedia): https://en.wikipedia.org/wiki/H%C3%BCckel_method
- König's theorem (bipartite matching): https://en.wikipedia.org/wiki/K%C3%B6nig%27s_theorem_(graph_theory)
