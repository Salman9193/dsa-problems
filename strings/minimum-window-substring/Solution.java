class Solution {

    // Approach: Sliding Window with Frequency Maps — O(n + m) time, O(1) space
    //
    // Two phases per iteration:
    //   Phase 1 — Expand (right++): add s[right], check if a requirement is newly satisfied
    //   Phase 2 — Shrink (left++) while valid: record minimum window, remove s[left]
    //
    // Key insight: track `satisfied` — the number of distinct char types whose
    // window count >= required count. When satisfied == distinct, window is valid.
    // This avoids O(|t|) validity checks per step → O(1) per step.
    public String minWindow(String s, String t) {
        if (s.isEmpty() || t.isEmpty()) return "";

        int[] need = new int[128];   // required frequencies from t
        int[] have = new int[128];   // current window frequencies

        for (char c : t.toCharArray()) need[c]++;

        int distinct = 0;            // number of distinct chars needed
        for (int f : need) if (f > 0) distinct++;

        int satisfied = 0;           // distinct chars currently satisfied
        int left = 0;
        int minLen = Integer.MAX_VALUE, minLeft = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            have[c]++;

            // only increment satisfied on the exact transition need[c]-1 → need[c]
            if (need[c] > 0 && have[c] == need[c]) satisfied++;

            // shrink from left while window contains all of t
            while (satisfied == distinct) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minLeft = left;
                }

                char lc = s.charAt(left);
                have[lc]--;
                // only decrement satisfied on the transition need[lc] → need[lc]-1
                if (need[lc] > 0 && have[lc] < need[lc]) satisfied--;
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n + m) — n=|s|, m=|t|; right visits each char of s once,
 *                   left advances at most n times total
 * Space: O(1)     — two int[128] arrays (fixed ASCII charset)
 *
 * Why have[c] == need[c] (not >=):
 *   We only increment `satisfied` on the EXACT transition from unsatisfied
 *   to satisfied (count goes from need[c]-1 to need[c]).
 *   Using >= would increment satisfied multiple times for the same char type.
 *   Symmetric logic applies when decrementing.
 *
 * Connection to sliding window family:
 *   #3  No repeating chars:     invalid when any char repeats (k=0)
 *   #424 Char replacement:      invalid when length - maxFreq > k
 *   #76  Min window (this):     valid when satisfied == distinct
 *   This is the most general form — the validity condition is multi-char.
 *
 * Trace — s="ADOBECODEBANC", t="ABC" (need: A=1, B=1, C=1, distinct=3)
 * -----------------------------------------------------------------------
 * r=0 'A': have[A]=1, satisfied=1
 * r=1 'D': no change
 * r=2 'O': no change
 * r=3 'B': have[B]=1, satisfied=2
 * r=4 'E': no change
 * r=5 'C': have[C]=1, satisfied=3 ← VALID
 *   record "ADOBEC"(6), shrink: remove 'A'→ have[A]=0<1 → satisfied=2, left=1
 * r=6..9: no new satisfactions
 * r=10 'A': have[A]=1, satisfied=3 ← VALID
 *   shrink left=1..5 until removing 'C' drops satisfied to 2
 *   best window update at left=9,right=10 not found yet...
 * r=11 'N': no change
 * r=12 'C': have[C]=1, satisfied=3 ← VALID
 *   shrink: record "BANC"(4) at left=9 ← NEW MIN
 *   remove 'A'→ satisfied=2, stop
 * return "BANC" ✓
 */
