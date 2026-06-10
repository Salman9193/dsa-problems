class Solution {

    // Approach 1: Filter + Reverse — O(n) time, O(n) space
    // Build a cleaned string (alphanumeric only, lowercased),
    // then check if it equals its reverse.
    public boolean isPalindromeFilter(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c))
                sb.append(Character.toLowerCase(c));
        }
        String clean = sb.toString();
        return clean.equals(new StringBuilder(clean).reverse().toString());
    }

    // Approach 2: Two Pointers — O(n) time, O(1) space  ← OPTIMAL
    //
    // Two pointers start from both ends and move inward.
    // Skip non-alphanumeric characters from both sides.
    // Compare characters (case-insensitive) — mismatch → false.
    // If pointers cross without mismatch → true.
    //
    // No extra allocation — operates directly on the original string.
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            // skip non-alphanumeric from left
            while (left < right && !Character.isLetterOrDigit(s.charAt(left)))
                left++;
            // skip non-alphanumeric from right
            while (left < right && !Character.isLetterOrDigit(s.charAt(right)))
                right--;

            if (Character.toLowerCase(s.charAt(left)) !=
                Character.toLowerCase(s.charAt(right)))
                return false;

            left++;
            right--;
        }

        return true;
    }
}

/*
 * Complexity
 * ----------
 * Filter + Reverse: Time O(n), Space O(n)
 * Two Pointers:     Time O(n), Space O(1)
 *
 * Why two pointers works:
 *   A palindrome reads the same from both ends.
 *   Two pointers mirror that definition exactly —
 *   left starts at the beginning, right at the end,
 *   they move toward each other comparing valid characters.
 *   Skipping non-alphanumeric characters inline avoids preprocessing.
 *
 * Trace — "A man, a plan, a canal: Panama"
 * -----------------------------------------
 * left=0 'A', right=29 'a' → 'a'=='a' ✓ → left=1,  right=28
 * left=1 ' ', skip         → left=2   'm'
 * right=28 'm'              → 'm'=='m' ✓ → left=3,  right=27
 * left=3  'a', right=27 'a' → 'a'=='a' ✓ → ...
 * ... continues until left >= right
 * return true ✓
 *
 * Edge cases:
 *   " "     → true  (all spaces → no alphanumeric chars → vacuously palindrome)
 *   ".,"    → true  (all punctuation → empty after filter)
 *   "0P"    → false ('0' != 'p')
 *   "a"     → true  (single char)
 *   "aA"    → true  (case-insensitive: 'a' == 'a')
 */
