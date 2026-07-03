import java.util.*;

class Solution {

    // Permutations: all n! orderings of distinct integers.
    // Key difference from Subsets: we use ALL elements, in different orders.
    // Use a boolean[] visited to track which elements are currently in the path.

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, new boolean[nums.length], new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] visited,
                           List<Integer> curr, List<List<Integer>> result) {
        if (curr.size() == nums.length) {
            result.add(new ArrayList<>(curr));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;       // skip already-used elements
            visited[i] = true;
            curr.add(nums[i]);
            backtrack(nums, visited, curr, result);
            curr.remove(curr.size() - 1);   // backtrack
            visited[i] = false;
        }
    }
}

/*
 * Complexity: Time O(n! × n), Space O(n) stack + O(n! × n) output
 *
 * Subsets vs Permutations:
 *   Subsets: start index moves forward (no revisiting earlier elements)
 *   Permutations: visited[] lets us pick any unused element at each step
 *
 * #47 Permutations II (with duplicates):
 *   Sort first. Skip if nums[i]==nums[i-1] && !visited[i-1]
 *   This prevents generating duplicate permutations.
 */
