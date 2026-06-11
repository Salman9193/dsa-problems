import java.util.HashMap;
import java.util.Map;

class Solution {

    // Approach: Sliding Window with Last-Seen Map — O(n) time, O(min(n,charset)) space
    //
    // Maintain a window [left, right] that always contains unique characters.
    // For each new character at `right`:
    //   - If it was seen before AND its last position is inside the current window
    //     → jump left to lastSeen[c] + 1 (skip past the duplicate in O(1))
    //   - Update lastSeen[c] = right
    //   - Update maxLen
    //
    // Key: we jump left directly to lastSeen[c]+1 instead of shrinking one step
    // at a time → O(n) total, not O(n^2).
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // only move left if the duplicate is inside the current window
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }

            lastSeen.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — right visits each char once; left only moves rightward
 * Space: O(min(n, |charset|)) — map holds at most charset entries
 *        O(26)  for lowercase letters
 *        O(128) for ASCII
 *        O(n)   for unicode
 *
 * Why lastSeen.get(c) >= left matters:
 *   The map retains positions from PREVIOUS windows.
 *   Without the check, we'd move left backward for a character
 *   that's no longer inside the current window.
 *
 *   Example: s = "abba"
 *     right=3 'a': lastSeen['a']=0, left=2
 *     0 >= 2? NO → don't move left ('a' is not in window [2,3])
 *     window = "ba", length = 2  ✓
 *     Without check: left=1 → window "b", length=1  WRONG
 *
 * Trace — s = "abcabcbb"
 * -------------------------
 * r=0 'a': window=[0,0] "a",   left=0, max=1
 * r=1 'b': window=[0,1] "ab",  left=0, max=2
 * r=2 'c': window=[0,2] "abc", left=0, max=3
 * r=3 'a': 'a' at 0 >= 0 → left=1; window=[1,3] "bca", max=3
 * r=4 'b': 'b' at 1 >= 1 → left=2; window=[2,4] "cab", max=3
 * r=5 'c': 'c' at 2 >= 2 → left=3; window=[3,5] "abc", max=3
 * r=6 'b': 'b' at 4 >= 3 → left=5; window=[5,6] "cb",  max=3
 * r=7 'b': 'b' at 6 >= 5 → left=7; window=[7,7] "b",   max=3
 * return 3 ✓
 *
 * Variant — fixed ASCII charset: replace HashMap with int[128]
 *   int[] lastSeen = new int[128]; Arrays.fill(lastSeen, -1);
 *   Faster constant factor, same O(n) complexity.
 */
