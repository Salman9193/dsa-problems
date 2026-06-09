import java.util.*;

class Solution {

    // Approach 1: Sort as Key — O(n * k log k) time, O(n * k) space
    //
    // Canonical form: sort each string's characters.
    // Anagrams produce the same sorted string → same HashMap key.
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }

    // Approach 2: Frequency Array as Key — O(n * k) time, O(n * k) space
    //
    // Canonical form: count character frequencies, encode as "c1#c2#...#c26#".
    // Avoids sorting → strictly faster than Approach 1.
    //
    // The '#' delimiter is CRITICAL — without it:
    //   count=[1,11,...] → "111..." 
    //   count=[11,1,...] → "111..."  (same key — wrong!)
    // With '#':
    //   count=[1,11,...] → "1#11#..."
    //   count=[11,1,...] → "11#1#..."  (different keys — correct)
    public List<List<String>> groupAnagramsFreq(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) count[c - 'a']++;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) sb.append(count[i]).append('#');
            String key = sb.toString();

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }
}

/*
 * Complexity
 * ----------
 * Approach 1 (sort):  Time O(n * k log k), Space O(n * k)
 * Approach 2 (freq):  Time O(n * k),       Space O(n * k)
 *
 * n = number of strings, k = max string length
 *
 * Why prime product (Approach 3) is risky:
 *   Assign a prime to each letter (a=2, b=3, c=5...).
 *   Product of all character primes is the key.
 *   Mathematically elegant but overflows long for long strings — not safe.
 *
 * Trace — ["eat","tea","tan","ate","nat","bat"]
 * ----------------------------------------------
 * "eat" → sort → "aet" → map: {"aet": ["eat"]}
 * "tea" → sort → "aet" → map: {"aet": ["eat","tea"]}
 * "tan" → sort → "ant" → map: {"aet":[...], "ant": ["tan"]}
 * "ate" → sort → "aet" → map: {"aet": ["eat","tea","ate"]}
 * "nat" → sort → "ant" → map: {..., "ant": ["tan","nat"]}
 * "bat" → sort → "abt" → map: {..., "abt": ["bat"]}
 *
 * Result: [["eat","tea","ate"], ["tan","nat"], ["bat"]]
 */
