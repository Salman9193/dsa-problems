class Solution {

    // Approach: Expand Around Center — O(n^2) time, O(1) space
    //
    // Every palindrome has a center. Expand outward from each possible center
    // as long as characters on both sides match.
    //
    // Two types of centers:
    //   Odd-length  → single character center  e.g. "aba"  centers on 'b'
    //   Even-length → gap between two chars     e.g. "abba" centers on 'bb'
    //
    // For a string of length n there are 2n-1 possible centers to try.
    public String longestPalindrome(String s) {
        int start = 0, maxLen = 1;

        for (int i = 0; i < s.length(); i++) {
            int len1 = expand(s, i, i);         // odd-length palindrome
            int len2 = expand(s, i, i + 1);     // even-length palindrome

            int len = Math.max(len1, len2);
            if (len > maxLen) {
                maxLen = len;
                // back-calculate start index from center and length
                start = i - (len - 1) / 2;
            }
        }

        return s.substring(start, start + maxLen);
    }

    // Expands outward from (left, right) while characters match.
    // Returns the length of the palindrome found.
    private int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // when loop exits, left and right are one step past the palindrome boundary
        return right - left - 1;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n^2) — n centers, each expansion up to O(n) in the worst case
 * Space: O(1)   — no auxiliary data structures; only two pointers
 *
 * Why not DP?
 *   DP also runs in O(n^2) time but uses O(n^2) space for the table.
 *   Expand-around-center gives the same time with O(1) space.
 *
 * Why not Manacher's?
 *   Manacher's is O(n) time and O(n) space. It reuses previously computed
 *   palindrome radii to skip redundant expansions. Correct for production,
 *   but expand-around-center is what interviews expect.
 *   Original paper: Manacher, G. — J. ACM 22:346-351, 1975.
 *
 * Start index derivation
 * ----------------------
 * expand() returns length `len` centered at index i.
 *   Odd  len=3 at i=2: start = 2 - (3-1)/2 = 2 - 1 = 1  ✓
 *   Even len=4 at i=2: start = 2 - (4-1)/2 = 2 - 1 = 1  ✓
 *
 * Trace — "babad"
 * ---------------
 *  i=0 'b' → odd=1  even=1 → maxLen=1
 *  i=1 'a' → odd=3  even=1 → maxLen=3, start=0  ("bab")
 *  i=2 'b' → odd=3  even=1 → maxLen=3 (no change, "aba" same length)
 *  i=3 'a' → odd=1  even=1 → no change
 *  i=4 'd' → odd=1  even=1 → no change
 *  Result: "bab"
 */
