import java.util.*;

class Solution {

    // Key insight: EMAILS are the real graph nodes, not account indices or
    // names. Names are misleading — multiple people can share a name, and
    // the same person's accounts are identified ONLY through shared emails.
    //
    // Two approaches: DSU (preferred) and BFS/DFS on an explicit email graph.

    // ── Approach 1: Union-Find (preferred) ────────────────────────────────────
    //
    // Assign each unique email an integer id. Union all emails within the
    // same account (they belong to one person). Group by root afterward.
    //
    // Why union WITHIN each account (not across accounts directly)?
    //   Unioning account[1] with every other email in the SAME account
    //   correctly models "these emails belong to one person." Two separate
    //   accounts sharing an email merge TRANSITIVELY through DSU's union
    //   structure: if account A has {x,y} and account B has {y,z}, then
    //   union(x,y) and union(y,z) put x,y,z all in one component automatically
    //   — no need to explicitly compare every pair of accounts.
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, String>  emailToName = new HashMap<>();
        Map<String, Integer> emailToId   = new HashMap<>();
        int id = 0;

        // Step 1: assign each unique email an integer id
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                if (!emailToId.containsKey(email))
                    emailToId.put(email, id++);
            }
        }

        UnionFind uf = new UnionFind(id);

        // Step 2: union all emails within the same account
        for (List<String> account : accounts) {
            int firstId = emailToId.get(account.get(1));
            for (int i = 2; i < account.size(); i++)
                uf.union(firstId, emailToId.get(account.get(i)));
        }

        // Step 3: group emails by their root
        Map<Integer, List<String>> rootToEmails = new HashMap<>();
        for (String email : emailToId.keySet()) {
            int root = uf.find(emailToId.get(email));
            rootToEmails.computeIfAbsent(root, k -> new ArrayList<>()).add(email);
        }

        // Step 4: build result — sort emails, prepend name
        List<List<String>> result = new ArrayList<>();
        for (List<String> emails : rootToEmails.values()) {
            Collections.sort(emails);
            List<String> merged = new ArrayList<>();
            merged.add(emailToName.get(emails.get(0)));
            merged.addAll(emails);
            result.add(merged);
        }

        return result;
    }

    // Reference UnionFind (see guides/UNION_FIND.md for the full template)
    static class UnionFind {
        int[] parent, rank;
        UnionFind(int n) {
            parent = new int[n]; rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]); // path compression
            return parent[x];
        }
        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if      (rank[px] < rank[py]) parent[px] = py;
            else if (rank[px] > rank[py]) parent[py] = px;
            else { parent[py] = px; rank[px]++; }
        }
    }

    // ── Approach 2: BFS/DFS on Explicit Email Graph ───────────────────────────
    //
    // Build an adjacency list connecting every email in an account to the
    // account's first email (forming a star within each account). Then find
    // connected components via BFS/DFS.
    //
    // Comparison with DSU:
    //   Time:  O(NK) both (DSU has extra α(NK) factor, usually negligible)
    //   Space: DSU needs only id maps + parent/rank arrays.
    //          Graph approach needs a full adjacency list — denser memory
    //          footprint since each account creates K-1 explicit edges.
    //   Code:  DSU avoids building any graph structure — union() calls do
    //          the grouping directly. Graph approach requires building AND
    //          separately traversing — more code, same result.
    //
    // When BFS/DFS is preferable:
    //   - You need the actual graph structure for OTHER purposes (e.g. also
    //     computing the shortest "introduction path" between two emails)
    //   - DSU isn't readily available/idiomatic in your environment
    public List<List<String>> accountsMergeBFS(List<List<String>> accounts) {
        Map<String, String> emailToName = new HashMap<>();
        Map<String, List<String>> graph = new HashMap<>();

        // Build graph: connect all emails within the same account to the first
        for (List<String> account : accounts) {
            String name = account.get(0);
            String firstEmail = account.get(1);
            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                emailToName.put(email, name);
                graph.putIfAbsent(email, new ArrayList<>());
                graph.putIfAbsent(firstEmail, new ArrayList<>());
                if (!email.equals(firstEmail)) {
                    graph.get(email).add(firstEmail);
                    graph.get(firstEmail).add(email);
                }
            }
        }

        Set<String> visited = new HashSet<>();
        List<List<String>> result = new ArrayList<>();

        for (String email : graph.keySet()) {
            if (visited.contains(email)) continue;

            List<String> component = new ArrayList<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.push(email);
            visited.add(email);

            while (!stack.isEmpty()) {
                String curr = stack.pop();
                component.add(curr);
                for (String neighbour : graph.get(curr)) {
                    if (!visited.contains(neighbour)) {
                        visited.add(neighbour);
                        stack.push(neighbour);
                    }
                }
            }

            Collections.sort(component);
            List<String> merged = new ArrayList<>();
            merged.add(emailToName.get(email));
            merged.addAll(component);
            result.add(merged);
        }

        return result;
    }
}

/*
 * Complexity
 * ----------
 * DSU:      Time O(NK α(NK) + NK log(NK)) — dominated by sorting emails per group
 *           Space O(NK) — id maps + parent/rank arrays
 * BFS/DFS:  Time O(NK + NK log(NK)) — graph build + traversal + sort
 *           Space O(NK) — adjacency list (denser than DSU's flat arrays)
 *
 * N = number of accounts, K = average emails per account
 *
 * Why emails (not names) must be the DSU/graph nodes:
 *   Multiple different people can share the same name (see the 4th "John"
 *   in the classic example — could be a different John with no shared email).
 *   Only shared EMAILS reliably indicate "same person." Using names as the
 *   grouping key would incorrectly merge unrelated people who happen to
 *   share a name.
 *
 * Trace — accounts=[
 *   ["John","johnsmith@mail.com","john_newyork@mail.com"],
 *   ["John","johnsmith@mail.com","john00@mail.com"],
 *   ["Mary","mary@mail.com"],
 *   ["John","johnsmith@mail.com"]]
 * ------------------------------------------------------------------------
 * ids: johnsmith=0, john_newyork=1, john00=2, mary=3
 *
 * Account 1: union(0,1) → {0,1}
 * Account 2: union(0,2) → {0,1,2}
 * Account 3: no union (single email) → {3}
 * Account 4: single email (id 0) → no union needed, already in {0,1,2}
 *
 * Groups by root:
 *   root(0) = {johnsmith, john_newyork, john00}
 *     sorted: [john00, john_newyork, johnsmith]
 *   root(3) = {mary}
 *
 * Result:
 *   ["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"]
 *   ["Mary","mary@mail.com"]
 */
