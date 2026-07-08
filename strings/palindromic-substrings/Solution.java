class Solution {

    // Approach: Expand Around Center — O(n^2) time, O(1) space
    //
    // Every palindrome is symmetric around a center. Expand outward from each
    // possible center while both sides match. The key idea for COUNTING (vs.
    // finding the longest) is that each successful expansion step is itself a
    // new palindrome — so we simply count every match.
    //
    // Two types of centers:
    //   Odd-length  → single character center   e.g. "aba"  centers on 'b'
    //   Even-length → gap between two chars      e.g. "abba" centers on 'bb'
    //
    // For a string of length n there are 2n-1 centers to try.
    public int countSubstrings(String s) {
        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            total += countExpand(s, i, i);      // odd-length palindromes
            total += countExpand(s, i, i + 1);  // even-length palindromes
        }

        return total;
    }

    // Expands outward from (left, right), counting one palindrome per matching
    // step. Returns the number of palindromes centered here.
    private int countExpand(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            count++;   // this matched pair forms a new palindrome
            left--;
            right++;
        }
        return count;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n^2) — 2n-1 centers, each expansion up to O(n) in the worst case
 * Space: O(1)   — only two pointers and a counter
 *
 * Why count-per-expansion works
 * -----------------------------
 * Unlike "longest palindrome" (where each center contributes only its longest),
 * every time the two ends match during expansion we have found a distinct
 * palindrome centered at that point. Incrementing on each match tallies them
 * all without computing lengths or start indices.
 *
 * Why not DP?
 *   DP fills dp[i][j] = s[i]==s[j] && (j-i<2 || dp[i+1][j-1]) and counts the
 *   trues — same O(n^2) time but O(n^2) space. Expand-around-center matches the
 *   time with O(1) space.
 *
 * Why not Manacher's?
 *   Manacher's is O(n): the count is the sum of palindrome radii over all
 *   centers. Optimal, but expand-around-center is what interviews expect.
 *   Original paper: Manacher, G. — J. ACM 22:346-351, 1975.
 *
 * Trace — "aaa"
 * -------------
 *  i=0 'a' → odd countExpand(0,0)=1 ("a")        even countExpand(0,1)=1 ("aa")   total=2
 *  i=1 'a' → odd countExpand(1,1)=2 ("a","aaa")  even countExpand(1,2)=1 ("aa")   total=5
 *  i=2 'a' → odd countExpand(2,2)=1 ("a")        even countExpand(2,3)=0          total=6
 *  Result: 6
 *
 * Sanity check: n identical chars → n(n+1)/2 palindromes (the maximum).
 */
