import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {

    // Combination Sum II — LeetCode #40
    //
    // Candidates may contain duplicates; each element may be used at most once; the output
    // must contain no duplicate combinations.
    //
    // Backtracking with two adjustments vs. Combination Sum (#39):
    //   1. recurse from i + 1  -> each element used once,
    //   2. skip equal values at the same level (i > start && c[i] == c[i-1]) -> dedupe.
    // Sorting groups duplicates (so the skip works) and enables the pruning break.
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private void backtrack(int[] c, int remaining, int start,
                           List<Integer> path, List<List<Integer>> res) {
        if (remaining == 0) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < c.length; i++) {
            if (i > start && c[i] == c[i - 1]) continue;   // skip duplicate at this level
            if (c[i] > remaining) break;                    // pruning (array is sorted)
            path.add(c[i]);
            backtrack(c, remaining - c[i], i + 1, path, res);   // i + 1: use each once
            path.remove(path.size() - 1);
        }
    }
}

/*
 * Why "i > start", not "i > 0"
 * ----------------------------
 * The skip suppresses duplicate *choices at the same recursion level*. The first equal
 * value at a level (i == start) is allowed; later equal values there are skipped, so we
 * never start two identical branches. Deeper in the recursion (a different start), the
 * same value can still be picked — that is the *second copy* of the number as the next
 * element, which is a genuinely different combination.
 *
 * Trace — [10,1,2,7,6,1,5], target 8  ->  sorted [1,1,2,5,6,7,10]
 *   top level start=0: second 1 (index 1) skipped -> no duplicate [1,...] branch
 *   inside first-1 branch start=1: second 1 has i==start -> used -> [1,1,6]
 *   => [[1,1,6], [1,2,5], [1,7], [2,6]]
 */
