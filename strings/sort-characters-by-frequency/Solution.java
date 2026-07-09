import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {

    // Sort Characters By Frequency — LeetCode #451
    //
    // Order the distinct characters by frequency (descending), then emit each as a run.
    //
    // Approach 1: count, then sort the distinct characters by frequency.
    // O(n + k log k) time, where k = distinct characters (<= 62 here).
    public String frequencySortBySort(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) freq.merge(c, 1, Integer::sum);

        List<Character> chars = new ArrayList<>(freq.keySet());
        chars.sort((a, b) -> freq.get(b) - freq.get(a));   // descending by frequency

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            int count = freq.get(c);
            for (int i = 0; i < count; i++) sb.append(c);
        }
        return sb.toString();
    }

    // Approach 2: bucket sort by frequency — O(n).
    //
    // A frequency can never exceed n, so index characters by their count instead of
    // sorting: bucket[f] holds every character that appears exactly f times. Walking
    // buckets from n down to 1 yields most-frequent-first order with no comparison sort.
    @SuppressWarnings("unchecked")
    public String frequencySort(String s) {
        int n = s.length();
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) freq.merge(c, 1, Integer::sum);

        // bucket[f] = list of characters that appear exactly f times
        List<Character>[] bucket = new List[n + 1];
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            int f = e.getValue();
            if (bucket[f] == null) bucket[f] = new ArrayList<>();
            bucket[f].add(e.getKey());
        }

        StringBuilder sb = new StringBuilder();
        for (int f = n; f >= 1; f--) {                 // most frequent first
            if (bucket[f] != null) {
                for (char c : bucket[f]) {
                    for (int i = 0; i < f; i++) sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}

/*
 * Trace — "tree"
 * --------------
 * Count:  t:1, r:1, e:2
 * Bucket: bucket[2] = [e], bucket[1] = [t, r]
 * Walk f = 4..1:
 *   f=2 -> append "ee"
 *   f=1 -> append "t", then "r"
 * Result: "eert"   ("eetr" is equally valid if the map yields r before t)
 *
 * Why bucket sort is O(n)
 * -----------------------
 * The largest possible frequency is n (a single repeated character), so frequencies
 * live in the fixed range [1, n]. Placing each distinct character into bucket[freq] and
 * scanning the buckets is linear — no k log k comparison sort. This is the same
 * bounded-key-range idea used by Top K Frequent Elements.
 */
