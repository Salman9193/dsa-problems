# Cracking the Safe — Notes & Intuition

**LeetCode #753** | Eulerian circuit on a de Bruijn graph | Hard
The problem that makes the **de Bruijn sequence** concrete: build the shortest string that contains
*every* possible password. It's [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary)'s twin —
same Hierholzer engine, but the graph is **generated**, not given.

---

## Problem

A safe opens when the last `n` entered digits match the password. Digits are `0 … k-1`. Because it
checks the **most recent `n`** after every keypress, you don't need to type each password separately —
you want the **shortest** string such that all `kⁿ` length-`n` combinations appear as a substring.

```
n = 2, k = 2  → passwords {00,01,10,11}
answer "00110"  →  00, 01, 11, 10   (all four, in 5 digits instead of 4×2=8)
```

Return any shortest such string.

---

## The Recognition: This Is a de Bruijn Sequence

A string over `k` symbols containing every length-`n` string exactly once **is** a
**de Bruijn sequence** `B(k, n)` — and its minimal length is `kⁿ + (n−1)` (the `kⁿ` combinations,
overlapping maximally, plus the `n−1` to seed the first window). The safe problem is *literally*
asking you to construct one. See the [Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN).

**Why brute force fails:** trying combinations independently is `kⁿ · n` digits, and searching for a
maximal-overlap ordering by backtracking is exponential. The de Bruijn construction is **linear in the
output size.**

---

## The Graph Construction (the whole trick)

Model it as a graph and the answer is an **Eulerian circuit**:

- **Nodes** = the `k^(n−1)` strings of length `n−1` (the "current window minus its oldest digit").
- **Edges** = the `kⁿ` strings of length `n` (each *is* a password). Edge `s` goes from its first
  `n−1` chars to its last `n−1` chars — i.e., appending one digit and dropping the oldest.

Every node has **in-degree = out-degree = k** (append any of `k` digits; arrive by prepending any of
`k`). That balance guarantees an **Eulerian circuit exists** — a walk using every edge (password)
exactly once. Walk it, emit one digit per edge, and you've typed every password with maximal overlap.

```
n=2, k=2:  nodes {0,1},  edges {00,01,10,11}
  0 --0--> 0     0 --1--> 1     1 --0--> 0     1 --1--> 1
Eulerian circuit over all 4 edges → "00110" (each digit = one edge traversal)
```

**This is the payoff of the theory:** "shortest string with all substrings" sounded like a hard
combinatorial search; as a graph it's a standard linear-time Eulerian walk.

---

## Solution — Hierholzer (post-order DFS)

Identical engine to [#332](#graphs/reconstruct-itinerary): walk edges, and **append a digit only
after exhausting a node's edges** (post-order), so you never get stuck.

```java
class Solution {
    public String crackSafe(int n, int k) {
        if (n == 1) {                      // trivial: just list the k digits
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < k; i++) s.append(i);
            return s.toString();
        }
        Set<String> seen = new HashSet<>(); // visited EDGES (n-length strings)
        StringBuilder ans = new StringBuilder();
        String start = "0".repeat(n - 1);   // any (n-1)-node; all-zeros is convenient
        dfs(start, k, seen, ans);
        return ans.append(start).toString(); // seed the first window
    }

    private void dfs(String node, int k, Set<String> seen, StringBuilder ans) {
        for (int x = 0; x < k; x++) {
            String edge = node + x;          // the n-length password = this edge
            if (seen.add(edge)) {            // add() returns false if already present
                dfs(edge.substring(1), k, seen, ans); // next node = drop the oldest digit
                ans.append(x);               // POST-ORDER: append after edges exhausted
            }
        }
    }
}
```

**Why post-order works (same as #332):** appending *after* the recursion means a node whose edges are
all used gets its digit written first, and detours get stitched in ahead — so you never strand an
edge. Building `ans` this way yields the reversed circuit; appending `start` at the end seeds the
initial `n−1` window. (You can reverse explicitly instead; this form folds the reverse into the
append order.)

---

## Complexity

| | |
|---|---|
| Time | **O(kⁿ · n)** — visit each of `kⁿ` edges once; the `n` is per-edge string work |
| Space | **O(kⁿ · n)** — the `seen` set holds all `kⁿ` edges; recursion depth up to `kⁿ` |
| Output length | **kⁿ + (n−1)** — provably minimal |

The output *itself* is size `kⁿ`, so this is linear in the answer — you cannot do asymptotically
better, since you must emit every combination.

---

## Trace — `n = 2, k = 2`

```
start node = "0", seen = {}
dfs("0"):
  x=0: edge "00" new → dfs("0")… (its 0-edge used) x=1: edge "01" new → dfs("1")
        dfs("1"): x=0: edge "10" new → dfs("0") exhausted; append 0
                  x=1: edge "11" new → dfs("1") exhausted; append 1
                  exhausted; append 0            [ans so far: 0,1,0 building reversed]
       append 1
  append 0
ans = "0110", + start "0" → "00110" ✓   (contains 00,01,11,10)
```

---

## Edge Cases

| Case | Result |
|------|--------|
| `n == 1` | just `0,1,…,k-1` — no overlap possible, each password is one digit |
| `k == 1` | `"000…0"` (n zeros); only one password exists |
| large `n,k` | output is `kⁿ` — grows fast; the algorithm stays linear in it |

---

## #332 vs #753 — the Eulerian pair

| | [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary) | **Cracking the Safe #753** |
|---|---|---|
| Graph | **given** (tickets are edges) | **generated** (de Bruijn graph) |
| Goal | Eulerian **path**, lexical-smallest | Eulerian **circuit**, any valid |
| Engine | Hierholzer, post-order | Hierholzer, post-order |
| Output | the vertex sequence | the **edge labels** (= a de Bruijn sequence) |

**They're the same algorithm** — #753 just constructs its own graph and reads off edge labels instead
of vertices. Seeing that equivalence is the point: once "every combination exactly once" clicks as
"Eulerian circuit on a de Bruijn graph," this Hard problem becomes a 15-line walk.

**The through-line:** de Bruijn sequences power keypad cracking (this problem, literally), DNA
assembly, and position encoding — all Eulerian circuits on a de Bruijn graph. See the
[guide](#guides/EULERIAN_DE_BRUIJN) for the full family.
