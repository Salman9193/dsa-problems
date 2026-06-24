import java.util.ArrayDeque;
import java.util.Deque;

class Solution {

    // Approach: Two-Stack Iterative — O(maxK^depth × n) time, O(depth) space
    //
    // Two stacks track context across bracket levels:
    //   countStack  — repeat count k at each bracket level
    //   stringStack — string accumulated before each '[' (the prefix)
    //
    // When we hit '[': save current state (k and current string) onto stacks; reset
    // When we hit ']': pop saved state; repeat current string k times; prepend prefix
    //
    // This is structurally identical to the stack used in:
    //   - LZW decompression (nested symbol resolution)
    //   - RLE decoding (count + value pairs)
    //   - Recursive descent parsers (call stack simulation)
    public String decodeString(String s) {
        Deque<Integer>       countStack  = new ArrayDeque<>();
        Deque<StringBuilder> stringStack = new ArrayDeque<>();
        StringBuilder        current     = new StringBuilder();
        int k = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                k = k * 10 + (c - '0');       // build multi-digit number: "123" → 1→12→123

            } else if (c == '[') {
                countStack.push(k);            // save repeat count
                stringStack.push(current);     // save accumulated string before '['
                current = new StringBuilder(); // start fresh inside bracket
                k = 0;                         // reset for next number

            } else if (c == ']') {
                int    repeat = countStack.pop();
                StringBuilder prev = stringStack.pop();
                String inner  = current.toString();
                current = prev;
                for (int i = 0; i < repeat; i++) current.append(inner); // repeat and append

            } else {
                current.append(c);             // regular character: accumulate
            }
        }

        return current.toString();
    }

    // Alternative: Recursive Descent — O(maxK^depth × n) time, O(depth) call stack
    //
    // More elegant but uses O(n) system call stack (risk of overflow for deep nesting).
    // The iterative version above simulates this call stack explicitly.
    private int idx = 0;

    public String decodeStringRecursive(String s) {
        StringBuilder sb = new StringBuilder();
        while (idx < s.length() && s.charAt(idx) != ']') {
            if (!Character.isDigit(s.charAt(idx))) {
                sb.append(s.charAt(idx++));
            } else {
                int repeat = 0;
                while (idx < s.length() && Character.isDigit(s.charAt(idx)))
                    repeat = repeat * 10 + s.charAt(idx++) - '0';
                idx++; // skip '['
                String inner = decodeStringRecursive(s);
                idx++; // skip ']'
                sb.append(inner.repeat(repeat));
            }
        }
        return sb.toString();
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(maxK^depth × n) — building the decoded string
 *   Worst case: k=300, depth=5, n=30 → 300^5 chars (problem guarantees output ≤ 10^5)
 * Space: O(depth) for stacks + O(output length) for string buffers
 *
 * Why multi-digit k = k*10 + (c-'0')?
 *   Accumulates digits left-to-right into an integer.
 *   '1','2','3' → k=1 → k=12 → k=123
 *   The k is reset to 0 after each '['.
 *
 * Why TWO stacks (not one)?
 *   When we encounter ']', we need two pieces of state:
 *     1. The repeat count (how many times to repeat the inner string)
 *     2. The string accumulated BEFORE this '[' (the prefix to prepend)
 *   These are independent — one stack can't hold both without encoding tricks.
 *
 * Trace — "3[a2[c]]"
 * --------------------
 * c='3': k=3
 * c='[': push(3), push(""), current="", k=0
 * c='a': current="a"
 * c='2': k=2
 * c='[': push(2), push("a"), current="", k=0
 * c='c': current="c"
 * c=']': repeat=2, prev="a", inner="c"
 *         current = "a" + "c"×2 = "acc"
 * c=']': repeat=3, prev="", inner="acc"
 *         current = "" + "acc"×3 = "accaccacc"
 * return "accaccacc" ✓
 *
 * Trace — "2[abc]3[cd]ef"
 * -------------------------
 * c='2': k=2
 * c='[': push(2), push(""), current=""
 * c='a','b','c': current="abc"
 * c=']': current="" + "abc"×2 = "abcabc"
 * c='3': k=3
 * c='[': push(3), push("abcabc"), current=""
 * c='c','d': current="cd"
 * c=']': current="abcabc" + "cd"×3 = "abcabccdcdcd"
 * c='e','f': current="abcabccdcdcdef"
 * return "abcabccdcdcdef" ✓
 */
