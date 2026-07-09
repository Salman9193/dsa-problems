# Most Stones Removed — Research & Foundations

Counting connected components where stones share a row or column — the removable count is total stones minus components, a union-find result.

- **R. E. Tarjan (1975), “Efficiency of a good but not linear set union algorithm,”** *Journal of the ACM* 22(2):215–225. DOI: 10.1145/321879.321884. Union–find with union by rank and path compression — near-linear (inverse-Ackermann) disjoint-set operations. https://doi.org/10.1145/321879.321884

**Why it matters here:** Stones in the same row/column form components; you can remove all but one per component, so the answer is n − (component count), computed with union-find.

*Citations verified against JACM / SIAM / Numerische Mathematik / CACM records this session — not from memory.*
