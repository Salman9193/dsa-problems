class Solution {

    // Shortest Common Supersequence — LeetCode #1092
    //
    // Return the shortest string that has both str1 and str2 as subsequences.
    //
    // Insight: share as much as possible. The maximal shareable part is the Longest
    // Common Subsequence (LCS) — those characters are written once; everything else
    // from each string is written separately. So:
    //     len(SCS) = len(str1) + len(str2) - len(LCS)
    //
    // Build the LCS DP table, then walk it back from (m, n) to (0, 0), emitting:
    //   - matched chars once (step diagonally),
    //   - otherwise the char on the side the LCS came from (step along that axis),
    //   - then flush whatever remains of the unfinished string,
    //   - and reverse (we emit back-to-front).
    //
    // Time: O(m*n).  Space: O(m*n) — the full table is needed to reconstruct.
    public String shortestCommonSupersequence(String str1, String str2) {
        int m = str1.length(), n = str2.length();

        // dp[i][j] = LCS length of str1[0..i-1] and str2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        // Reconstruct the supersequence by walking the table backwards.
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                sb.append(str1.charAt(i - 1));       // shared char, written once
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                sb.append(str1.charAt(i - 1));       // unique to str1
                i--;
            } else {
                sb.append(str2.charAt(j - 1));       // unique to str2
                j--;
            }
        }
        while (i > 0) sb.append(str1.charAt(--i));   // flush str1 remainder
        while (j > 0) sb.append(str2.charAt(--j));   // flush str2 remainder

        return sb.reverse().toString();
    }
}

/*
 * Trace — str1 = "abac", str2 = "cab"
 * -----------------------------------
 * LCS = "ab" (length 2)  =>  SCS length = 4 + 3 - 2 = 5
 *
 * Backtrack from (i=4, j=3):
 *   (4,3) c != b, dp[3][3]=2 >= dp[4][2]=1 -> emit 'c', i=3
 *   (3,3) a != b, dp[2][3]=2 >= dp[3][2]=1 -> emit 'a', i=2
 *   (2,3) b == b                            -> emit 'b', i=1, j=2
 *   (1,2) a == a                            -> emit 'a', i=0, j=1
 *   str1 done, flush str2 leftover          -> emit 'c', j=0
 *   emitted "cabac" -> reverse -> "cabac"   (a palindrome here)
 *
 * Why LCS gives the shortest supersequence
 * ----------------------------------------
 * Any common supersequence must contain every char of str1 and every char of str2 in
 * order. Characters can be *shared* only where the two strings agree in a common
 * subsequence; the largest such overlap is the LCS. Sharing the maximum number of
 * characters minimises the total length, which is exactly m + n - |LCS|.
 */
