import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Solution {

    // Word Break II — LeetCode #140
    //
    // Return EVERY segmentation of s into dictionary words.
    //
    // Same DAG as Word Break (#139): node i = position, edge i -> j when s[i..j-1] is a word.
    //   #139 asks "is there a path 0 -> n?"   (boolean OR -> polynomial)
    //   #140 asks "enumerate all paths"        (output is exponential)
    //   jieba's Chinese segmenter asks "the max-probability path" (weighted DAG DP -> linear)
    //
    // Memoize the LIST OF COMPLETIONS from each position: solve(end) is reached from many
    // different prefixes, so without a memo the shared suffix is re-explored every time.
    //
    // Time:  O(n^2 * L) exploration + O(total output size)  -- output-sensitive, which is optimal
    //        because the answer itself can be exponential.
    // Space: O(n * output).
    public List<String> wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);

        // Prune: if no segmentation exists at all, bail out before exploring exponentially.
        if (!canBreak(s, dict)) return new ArrayList<>();

        return solve(s, 0, dict, new HashMap<>());
    }

    private List<String> solve(String s, int start, Set<String> dict, Map<Integer, List<String>> memo) {
        List<String> cached = memo.get(start);
        if (cached != null) return cached;

        List<String> res = new ArrayList<>();
        if (start == s.length()) {
            res.add("");                                   // exactly one empty completion
            return res;
        }

        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            if (!dict.contains(word)) continue;
            for (String rest : solve(s, end, dict, memo)) {
                res.add(rest.isEmpty() ? word : word + " " + rest);
            }
        }

        memo.put(start, res);
        return res;
    }

    // The #139 boolean DP — used here purely as a feasibility guard.
    // O(n^2 * L) and polynomial, because it collapses every path into a single bit.
    private boolean canBreak(String s, Set<String> dict) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
            }
        }
        return dp[n];
    }
}

/*
 * Trace — s = "catsanddog", dict = [cat, cats, and, sand, dog]
 * -----------------------------------------------------------
 *   solve(0): "cat"  -> solve(3) = ["sand dog"]  => "cat sand dog"
 *             "cats" -> solve(4) = ["and dog"]   => "cats and dog"
 *   solve(3): "sand" -> solve(7) = ["dog"]       => "sand dog"
 *   solve(4): "and"  -> solve(7) = ["dog"]        <- MEMO HIT (computed once, used twice)
 *   solve(7): "dog"  -> solve(10) = [""]          => "dog"
 *   result: ["cat sand dog", "cats and dog"]
 *
 * The complexity trap: memoization removes redundant COMPUTATION, but the OUTPUT is exponential,
 * so no algorithm can be polynomial. "aaaaaaaaaaaaaaaaaaaa" with dict [a, aa, aaa] has 121,415
 * segmentations, and every one must be materialized. Memoization makes the algorithm
 * OUTPUT-SENSITIVE, which is the best achievable.
 *
 * Real-world: this is jieba's "full mode" for Chinese text (no spaces, so every sentence is a Word
 * Break problem). Production segmenters want the MOST PROBABLE path instead, which turns this
 * exponential enumeration into a linear max-log-probability DAG DP.
 */
