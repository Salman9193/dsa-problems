class Solution {

    // Approach: Sliding Window — O(n) time, O(1) space
    //
    // Key insight: for any window [left, right], the minimum replacements needed
    // to make all characters the same = window_length - max_frequency_in_window.
    // We keep the most frequent character and replace everything else.
    // The window is valid iff: (right - left + 1) - maxFreq <= k
    //
    // Optimisation: maxFreq never decreases.
    // We only care whether a new character beats the current maxFreq.
    // If not, the window slides (stays same size) rather than grows.
    // This means the window size is monotonically non-decreasing → O(n).
    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int left = 0, maxFreq = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            int idx = s.charAt(right) - 'A';
            count[idx]++;
            maxFreq = Math.max(maxFreq, count[idx]);

            // window is invalid: need more than k replacements
            // use 'if' not 'while' — window shrinks by at most 1 per step
            if ((right - left + 1) - maxFreq > k) {
                count[s.charAt(left) - 'A']--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — right visits each char once; left advances at most n times
 * Space: O(1) — fixed int[26] array
 *
 * Why maxFreq never needs to decrease:
 *   We only want to GROW the window. If the current char doesn't beat maxFreq,
 *   we slide the window (left++, right++) without growing it.
 *   maxFreq only updates when we find a strictly better frequency.
 *   This means the answer (maxLen) is monotonically non-decreasing.
 *
 * Why 'if' instead of 'while':
 *   The window can shrink by at most 1 per step (one left++ per right++).
 *   'while' would also work but is equivalent here — the condition can only
 *   trigger once per iteration because removing one character from the left
 *   reduces the window size by 1, making it valid again.
 *
 * Connection to #3 (Longest Substring Without Repeating Characters):
 *   Setting k=0:
 *     invalid when (length - maxFreq) > 0
 *     i.e. when any character repeats
 *   This is exactly the condition from #3.
 *   This problem is the generalisation of #3 to k allowed replacements.
 *
 * Trace — s="AABABBA", k=1
 * --------------------------
 * r=0 'A': count[A]=1, maxFreq=1, window=1, valid(0≤1), maxLen=1
 * r=1 'A': count[A]=2, maxFreq=2, window=2, valid(0≤1), maxLen=2
 * r=2 'B': count[B]=1, maxFreq=2, window=3, valid(1≤1), maxLen=3
 * r=3 'A': count[A]=3, maxFreq=3, window=4, valid(1≤1), maxLen=4 ✓
 * r=4 'B': count[B]=2, maxFreq=3, window=5, invalid(2>1)
 *          → left=1, count[A]=2, window=4, maxLen=4
 * r=5 'B': count[B]=3, maxFreq=3, window=5, invalid(2>1)
 *          → left=2, count[A]=1, window=4, maxLen=4
 * r=6 'A': count[A]=2, maxFreq=3, window=5, invalid(2>1)
 *          → left=3, count[B]=2, window=4, maxLen=4
 * return 4 ✓
 */
