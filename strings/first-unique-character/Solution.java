import java.util.LinkedHashMap;
import java.util.Map;

class Solution {

    // Approach 1: Two-Pass Frequency Array — O(n) time, O(1) space  ← OPTIMAL
    //
    // Pass 1: count character frequencies using int[26].
    // Pass 2: scan left-to-right, return index of first char with count=1.
    //
    // Why int[26] beats HashMap:
    //   - O(1) per character with no hashing overhead
    //   - No boxing/unboxing (int vs Integer)
    //   - Cache-friendly: 26 ints fit in a single cache line (104 bytes)
    //   - Fixed charset (a-z): direct indexing with c - 'a'
    public int firstUniqChar(String s) {
        int[] count = new int[26];

        for (char c : s.toCharArray())
            count[c - 'a']++;

        for (int i = 0; i < s.length(); i++)
            if (count[s.charAt(i) - 'a'] == 1) return i;

        return -1;
    }

    // Approach 2: LinkedHashMap — O(n) time, O(k) space
    //
    // Preserves insertion order — iterate map entries in first-seen order.
    // Less efficient than int[26] due to HashMap overhead, but works for
    // arbitrary character sets (Unicode, not just a-z).
    public int firstUniqCharLinkedHashMap(String s) {
        Map<Character, Integer> count = new LinkedHashMap<>();
        for (char c : s.toCharArray())
            count.merge(c, 1, Integer::sum);

        for (Map.Entry<Character, Integer> e : count.entrySet())
            if (e.getValue() == 1) return s.indexOf(e.getKey());

        return -1;
    }
}

/*
 * Complexity
 * ----------
 * Approach 1: Time O(n), Space O(1)  — int[26] is constant size
 * Approach 2: Time O(n), Space O(k)  — k = distinct characters
 *
 * Why two passes instead of one?
 *   In a single pass, we can't know if a character is unique until we've
 *   seen all of it — the second 'e' in "leetcode" only appears at index 2.
 *   We need to finish the full frequency count before identifying uniqueness.
 *
 * Trace — "leetcode"
 * --------------------
 * Pass 1: count = [l:1, e:3, t:1, c:1, o:1, d:1]
 * Pass 2:
 *   i=0 'l': count=1 → return 0 ✓
 *
 * Trace — "loveleetcode"
 * ----------------------
 * Pass 1: count = [l:2, o:2, v:1, e:4, t:1, c:1, d:1]
 * Pass 2:
 *   i=0 'l': count=2 → skip
 *   i=1 'o': count=2 → skip
 *   i=2 'v': count=1 → return 2 ✓
 *
 * Trace — "aabb"
 * ---------------
 * Pass 1: count = [a:2, b:2]
 * Pass 2: all counts > 1 → return -1 ✓
 *
 * Variant — first unique character in a stream (LeetCode #1429):
 *   Elements arrive one at a time → use doubly linked list + HashMap
 *   for O(1) updates. The list head is always the first unique character.
 */
