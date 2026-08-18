# Reconstruct Itinerary — Notes & Intuition

**LeetCode #332** | Eulerian path (Hierholzer's algorithm) | Hard
Use **every ticket exactly once**, start at JFK, return the lexicographically smallest itinerary.
The repo's first Eulerian-path problem — a different beast from
[topological sort](#guides/GRAPH_ALGORITHMS) (which uses every *node* once; this uses every *edge*).

---

## Problem

Given flight tickets `[from, to]`, reconstruct the itinerary that **uses all tickets exactly once**,
beginning at `"JFK"`. If several are valid, return the one that's smallest in lexical order.

```
[["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
→ ["JFK","ATL","JFK","SFO","ATL","SFO"]     (not JFK→SFO→…, which is lexically larger)
```

---

## The Key Recognition: This Is an Eulerian Path

An **Eulerian path** visits **every edge exactly once** (vs. a Hamiltonian path, every *vertex* once —
which is NP-hard). Tickets are edges, airports are nodes; "use every ticket once" *is* "walk every
edge once." That reframing is the whole problem — once you see it, **Hierholzer's algorithm** solves
it in linear time.

> **Eulerian vs. Hamiltonian is a classic trap:** they sound similar, but every-edge-once is
> polynomial and every-vertex-once is NP-hard. Spotting which one you have is the difference between
> an O(E log E) solution and a hopeless brute force.

**When does an Eulerian path exist?** In a directed graph: at most one vertex with
`outdegree − indegree = 1` (the start), at most one with `indegree − outdegree = 1` (the end), all
others balanced — and all edges in one connected component. #332 *guarantees* a valid itinerary, so
you can skip the check and just run Hierholzer.

---

## The Naive Greedy Fails — and Why

"At each airport, always fly to the smallest-lexical unused destination" seems right. **It gets stuck.**

```
[["JFK","KUL"],["JFK","NRT"],["NRT","JFK"]]
greedy: JFK →(smallest) KUL … dead end, but NRT→JFK is unused → WRONG
```

KUL is a **dead end** (a sink), but it must appear **last**, not abort the walk. The naive greedy
can't know that. Hierholzer's fixes it with one idea: **add an airport to the answer only when it has
no unused edges left, then reverse.**

---

## Hierholzer's Algorithm

```
build adj: source → min-heap of destinations   (min-heap gives lexical order for free)
dfs(node):
    while node has unused edges:
        next = remove smallest destination
        dfs(next)
    append node to route          ← POST-ORDER: only after edges are exhausted
answer = reverse(route)
```

**Why post-order + reverse works.** When a node runs out of edges, it's a dead end for *now* — so
it belongs at the *end* of the path from here. By appending on the way *out* of the recursion
(post-order) and reversing at the end, dead-ends naturally sink to their correct late positions, and
any detour (a cycle) gets stitched in ahead of them. The stuck-at-KUL problem solves itself: KUL is
appended first (it's an immediate dead end), so after reversing it lands last.

```java
class Solution {
    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> adj = new HashMap<>();
        for (List<String> t : tickets)
            adj.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));

        LinkedList<String> route = new LinkedList<>();   // addFirst = build reversed
        Deque<String> stack = new ArrayDeque<>();
        stack.push("JFK");
        while (!stack.isEmpty()) {
            String node = stack.peek();
            PriorityQueue<String> pq = adj.get(node);
            if (pq != null && !pq.isEmpty()) {
                stack.push(pq.poll());       // dive into smallest unused destination
            } else {
                route.addFirst(stack.pop()); // exhausted → prepend (== post-order + reverse)
            }
        }
        return route;
    }
}
```

This is the **iterative** Hierholzer (explicit stack) — no recursion-depth risk on long itineraries.
The recursive version is shorter but can overflow on ~10⁴+ tickets in a chain; same logic.

> **Note:** `LinkedList` here is deliberate — `addFirst` (O(1) prepend) is exactly what builds the
> reversed route, and we never index it. This is a genuine use of `LinkedList`-as-list, distinct
> from the [BFS-queue case where `ArrayDeque` wins](#guides/GRAPH_ALGORITHMS).

---

## Complexity

| | |
|---|---|
| Time | **O(E log E)** — each edge traversed once; the log is the per-source heap ordering |
| Space | O(E) for the graph + route |

E = number of tickets. The min-heaps cost the only non-linear factor; with pre-sorted lists it's O(E).

---

## Trace — `[["JFK","KUL"],["JFK","NRT"],["NRT","JFK"]]`

```
adj: JFK → [KUL, NRT] (heap), NRT → [JFK]
dive JFK → KUL (smallest)      KUL has no edges → prepend KUL     route=[KUL]
back at JFK → NRT              NRT → JFK
  JFK has no edges left        prepend JFK                        route=[JFK,KUL]
  back at NRT, exhausted       prepend NRT                        route=[NRT,JFK,KUL]
back at JFK, exhausted         prepend JFK                        route=[JFK,NRT,JFK,KUL]
answer = JFK → NRT → JFK → KUL ✓   (KUL correctly last)
```

Watch KUL get prepended *first* and end up *last* — that's the post-order mechanism doing its job.

---

## Edge Cases

| Case | Handling |
|------|----------|
| dead-end airport mid-graph | prepended early → ends up late; **not** a failure |
| multiple tickets between same pair | multigraph; heap holds duplicates, each an edge |
| single ticket | `[JFK, X]` |
| lexical ties | min-heap per source guarantees smallest-first |

---

## The Eulerian Family & de Bruijn

| Problem | Eulerian flavor |
|---------|-----------------|
| **#332 Reconstruct Itinerary** | Eulerian **path** in a directed multigraph, lexical-smallest |
| #2097 Valid Arrangement of Pairs | Eulerian path, construct start from degree balance |
| [Cracking the Safe #753](#graphs/cracking-the-safe) | Eulerian **circuit** on a **de Bruijn graph** (all codes as one string) |

That last one is the deep connection: **de Bruijn sequences** — cyclic strings containing every
length-`n` string over a `k`-symbol alphabet exactly once — are literally **Eulerian circuits on a de
Bruijn graph**, and they power keypad-cracking, DNA assembly, and position encoding. See the
**[Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN)** for the full story.

**The through-line:** the moment "use every edge/ticket/k-mer exactly once" appears, it's Eulerian,
and Hierholzer's post-order walk is the tool — whether you're routing a traveler, cracking a safe, or
assembling a genome.
