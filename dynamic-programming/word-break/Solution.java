import java.util.*;

class Solution {

    // Approach 1: Top-Down DP (Memoisation on indices) — O(n^2) time, O(n) space
    //
    // Key improvement over substring-key memoisation:
    //   - Memo key is an int (start index), not a String object.
    //   - Avoids O(n^2) String allocations for memo keys.
    //   - resultMap as instance field risks state leak across test cases — fixed here.
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        Boolean[] memo = new Boolean[s.length()];
        return dp(s, wordSet, 0, memo);
    }

    private boolean dp(String s, Set<String> wordSet, int start, Boolean[] memo) {
        if (start == s.length()) return true;          // consumed entire string
        if (memo[start] != null) return memo[start];   // already solved

        for (int end = start + 1; end <= s.length(); end++) {
            if (wordSet.contains(s.substring(start, end))
                    && dp(s, wordSet, end, memo)) {
                return memo[start] = true;
            }
        }

        return memo[start] = false;
    }

    // Approach 2: Bottom-Up DP — O(n^2) time, O(n) space
    //
    // dp[i] = true means prefix s[0..i) can be segmented using wordDict.
    // Base case: dp[0] = true (empty prefix is always valid).
    //
    // For each position i, check all split points j < i:
    //   if dp[j] is true AND s[j..i) is in the dictionary → dp[i] = true.
    public boolean wordBreakBottomUp(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[n];
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n^2) — two nested loops over string indices
 * Space: O(n)   — memo array or dp array of length n+1
 *
 * Issues in the original substring-key solution
 * -----------------------------------------------
 * 1. Memo key was the remaining substring, not the index.
 *    s.substring(i) allocates a new String on every call → O(n^2) allocations.
 *    Fix: use int start index as memo key.
 *
 * 2. resultMap was an instance field → state leaks across LeetCode test cases.
 *    Fix: use a local Boolean[] array indexed by position.
 *
 * 3. Early return `if (wordSet.contains(s)) return true` before the loop
 *    is redundant — the loop handles it when end == s.length().
 *
 * Trace — s="leetcode", dict=["leet","code"]
 * -------------------------------------------
 * dp[0] = true  (base)
 * dp[1]: j=0, s[0,1)="l"    → not in dict
 * dp[2]: j=0, s[0,2)="le"   → not in dict
 * dp[3]: j=0, s[0,3)="lee"  → not in dict
 * dp[4]: j=0, s[0,4)="leet" → in dict, dp[0]=true → dp[4]=true
 * dp[5..7]: no match
 * dp[8]: j=4, s[4,8)="code" → in dict, dp[4]=true → dp[8]=true ✓
 */
