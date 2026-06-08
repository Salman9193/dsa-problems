import java.util.HashSet;
import java.util.Set;

class Solution {

    // Approach: HashSet — O(n) time, O(n) space
    //
    // Key insight: a consecutive sequence has exactly one starting point —
    // the number x where (x-1) does NOT exist in the set.
    // Only start counting from sequence starts; every element is visited at most twice.
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int longest = 0;

        for (int n : set) {
            // Only process sequence starts to avoid O(n^2) behaviour.
            // If n-1 exists, n is not the start — skip it.
            if (!set.contains(n - 1)) {
                int length = 1;
                while (set.contains(n + length)) {
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }

        return longest;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — each element is added to the set once and visited inside
 *               a while-loop at most once across the entire execution.
 * Space: O(n) — the HashSet stores all n elements.
 *
 * Why not sort?
 * Sorting gives O(n log n) which violates the problem constraint.
 * The HashSet gives O(1) membership checks, reducing the problem to
 * a single linear scan.
 *
 * Trace — [100, 4, 200, 1, 3, 2]
 * set = {1, 2, 3, 4, 100, 200}
 *
 *  n=100 → 99 not in set → start chain → 101? No → length=1
 *  n=4   → 3 in set      → skip
 *  n=200 → 199 not in set → start chain → 201? No → length=1
 *  n=1   → 0 not in set  → start chain → 2✓ 3✓ 4✓ 5? No → length=4  ← answer
 *  n=3   → 2 in set      → skip
 *  n=2   → 1 in set      → skip
 */
