import java.util.*;

class Solution {

    // Combination Sum: find all combinations of candidates summing to target.
    // Candidates can be reused (unlimited). All candidates are distinct.
    //
    // Key insight: sort candidates, backtrack with start index.
    // Reuse: pass same index i (not i+1) on recursion.
    // Prune: if candidates[i] > remaining, break (sorted → all following too big).

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates); // enables pruning
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
                           List<Integer> curr, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(curr));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) break; // pruning: sorted array
            curr.add(candidates[i]);
            backtrack(candidates, remaining - candidates[i], i, curr, result); // i not i+1 (reuse!)
            curr.remove(curr.size() - 1);
        }
    }
}

/*
 * Complexity: Time O(n^(t/min) × n), Space O(t/min) stack depth
 *   where t = target, min = smallest candidate
 *   (At most t/min candidates chosen, branching factor n)
 *
 * Combination Sum II (#40 — no reuse, may have duplicates):
 *   Pass i+1 instead of i. Sort and skip duplicates:
 *   if (i > start && candidates[i] == candidates[i-1]) continue
 *
 * Combination Sum III (#216 — pick k numbers from 1-9):
 *   candidates = [1..9], additional constraint curr.size() == k
 */
