import java.util.*;

class Solution {

    // Approach 1: satisfied counter — O(n) time, O(1) space
    //
    // An anagram = same character frequencies. Fixed window size = |p|.
    // Track `satisfied` = number of distinct chars where have[c] >= need[c].
    // Window is valid when satisfied == distinct.
    // Same pattern as #76 Minimum Window Substring — fixed window version.
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s.length() < p.length()) return result;

        int[] need = new int[26];
        int[] have = new int[26];
        for (char c : p.toCharArray()) need[c - 'a']++;

        int distinct = 0;
        for (int f : need) if (f > 0) distinct++;

        int satisfied = 0, left = 0;

        for (int right = 0; right < s.length(); right++) {
            int rc = s.charAt(right) - 'a';
            have[rc]++;
            if (need[rc] > 0 && have[rc] == need[rc]) satisfied++;

            // shrink window if too large
            if (right - left + 1 > p.length()) {
                int lc = s.charAt(left) - 'a';
                if (need[lc] > 0 && have[lc] == need[lc]) satisfied--;
                have[lc]--;
                left++;
            }

            // valid anagram window
            if (right - left + 1 == p.length() && satisfied == distinct)
                result.add(left);
        }

        return result;
    }

    // Approach 2: Direct array comparison — simpler code, same O(n) time
    //
    // Since window is always exactly |p|, compare int[26] arrays after each slide.
    // Arrays.equals on 26 elements = O(26) = O(1) constant.
    public List<Integer> findAnagramsDirect(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int n = s.length(), m = p.length();
        if (n < m) return result;

        int[] pCount = new int[26];
        int[] wCount = new int[26];
        for (char c : p.toCharArray()) pCount[c - 'a']++;
        for (int i = 0; i < m; i++) wCount[s.charAt(i) - 'a']++;

        if (Arrays.equals(pCount, wCount)) result.add(0);

        for (int i = m; i < n; i++) {
            wCount[s.charAt(i) - 'a']++;
            wCount[s.charAt(i - m) - 'a']--;
            if (Arrays.equals(pCount, wCount)) result.add(i - m + 1);
        }

        return result;
    }
}

/*
 * Complexity
 * ----------
 * Approach 1 (satisfied): Time O(n), Space O(1)
 * Approach 2 (direct):    Time O(n × 26) = O(n), Space O(1)
 *
 * Both are O(n) — the constant factor for approach 2 is larger but code is simpler.
 * Prefer approach 1 when p has a large alphabet; approach 2 for clarity.
 *
 * Connection to sliding window family:
 *   #3  No repeating chars:    variable window, shrink on any repeat
 *   #424 Char replacement:     variable window, shrink on length-maxFreq > k
 *   #76  Min window substring: variable window, shrink while valid
 *   #438 Find all anagrams:    FIXED window = |p|, check equality
 *   #567 Permutation in string: same as #438, return bool not list
 *
 * Trace — s="cbaebabacd", p="abc"
 * ----------------------------------
 * need: a=1, b=1, c=1, distinct=3
 *
 * right=0('c'): have[c]=1, c==need[c]=1 → satisfied=1
 * right=1('b'): have[b]=1 → satisfied=2
 * right=2('a'): have[a]=1 → satisfied=3 == distinct → add index 0 ✓
 * right=3('e'): window too large, remove left='c': have[c]=0<need[c]=1 → satisfied=2
 *   left=1, add 'e': have[e]=1, need[e]=0 → no change → satisfied=2 ≠ 3
 * ...
 * right=8('a'): window "bac" at index 6 → satisfied=3 → add index 6 ✓
 *
 * Output: [0, 6] ✓
 */
