import java.util.*;

class Solution {

    // Subsets = enumerate all 2^n subsets of a set.
    // The foundational backtracking template: at each index, choose to include
    // or exclude the element, recurse, then backtrack.
    //
    // Three approaches: backtracking, iterative bit masking, cascading.

    // ── Approach 1: Backtracking (canonical template) ─────────────────────────
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> curr,
                           List<List<Integer>> result) {
        result.add(new ArrayList<>(curr)); // add current subset (including empty)

        for (int i = start; i < nums.length; i++) {
            curr.add(nums[i]);                          // choose
            backtrack(nums, i + 1, curr, result);       // explore
            curr.remove(curr.size() - 1);               // un-choose (backtrack)
        }
    }

    // ── Approach 2: Iterative / Cascading ────────────────────────────────────
    // Start with [[]]. For each num, add num to every existing subset.
    public List<List<Integer>> subsetsCascading(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (int num : nums) {
            int size = result.size();
            for (int i = 0; i < size; i++) {
                List<Integer> newSubset = new ArrayList<>(result.get(i));
                newSubset.add(num);
                result.add(newSubset);
            }
        }
        return result;
    }
}

/*
 * Complexity: Time O(n × 2^n), Space O(n × 2^n)
 *   2^n subsets, each of average length n/2 → O(n × 2^n) total
 *
 * The Backtracking Template (memorise this):
 *   void backtrack(state, choices):
 *       if goal_reached: record(state); return
 *       for each choice in choices:
 *           make_choice(choice)
 *           backtrack(state, remaining_choices)
 *           undo_choice(choice)   ← the "backtrack" step
 *
 * This template solves:
 *   Subsets (#78):       include/exclude each element
 *   Permutations (#46):  choose each unused element
 *   Combination Sum (#39): choose from candidates with repetition
 *   N-Queens (#51):      place queens with constraint checking
 *   Word Search (#79):   explore grid with visited marking
 */
