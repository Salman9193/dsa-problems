import java.util.ArrayDeque;
import java.util.Deque;

class Solution {

    // Approach: Two-Stack Iterative — O(output_length) time, O(depth) space
    //
    // Key insight: nested brackets mirror recursive calls.
    //   '[' = push current context (string built so far, pending repeat count)
    //   ']' = pop context, repeat inner string k times, prepend prefix
    //
    // Two stacks because on ']' we need TWO independent pieces of state:
    //   1. How many times to repeat the inner string → countStack
    //   2. What was accumulated before this '[' → stringStack
    // A single stack cannot hold both without encoding tricks.
    //
    // Why k = k*10 + (c-'0'):
    //   Multi-digit numbers arrive one digit at a time.
    //   '1','2','3' → k=1 → k=12 → k=123.
    //   k resets to 0 after each '[' so it never bleeds across brackets.
    public String decodeString(String s) {
        Deque<Integer>       countStack  = new ArrayDeque<>();
        Deque<StringBuilder> stringStack = new ArrayDeque<>();
        StringBuilder        current     = new StringBuilder();
        int k = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                k = k * 10 + (c - '0');

            } else if (c == '[') {
                countStack.push(k);       // save repeat count for this level
                stringStack.push(current); // save string accumulated before this '['
                current = new StringBuilder();
                k = 0;

            } else if (c == ']') {
                int           repeat = countStack.pop();
                StringBuilder prefix = stringStack.pop();
                String        inner  = current.toString();
                // String.repeat() is O(repeat × inner.length) — acceptable
                // since problem guarantees total output length ≤ 10^5
                prefix.append(inner.repeat(repeat));
                current = prefix;

            } else {
                current.append(c);
            }
        }

        return current.toString();
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(output_length) — each character in the output is written once
 *        Worst case: k=300, depth=5 → 300^5 chars (bounded to 10^5 by problem)
 * Space: O(depth) for stacks + O(output_length) for string buffers
 *
 * Correctness argument:
 *   At any point, 'current' holds the fully decoded string for the current
 *   bracket level. On ']', we pop the saved prefix and repeat count,
 *   reconstruct the decoded string for this level, and assign it to 'current'.
 *   By induction on nesting depth, 'current' at depth 0 is the full answer.
 *
 * Why NOT a recursive solution with 'idx' as instance state:
 *   An instance field 'idx' is shared across calls — not reusable and not
 *   thread-safe. The iterative approach is stateless between calls and
 *   avoids O(n) call-stack depth risk for deeply nested inputs.
 *
 * Trace — "3[a2[c]]"
 * --------------------
 * c='3': k=3
 * c='[': push(3), push(""), current="", k=0
 * c='a': current="a"
 * c='2': k=2
 * c='[': push(2), push("a"), current="", k=0
 * c='c': current="c"
 * c=']': repeat=2, prefix="a", inner="c"
 *         prefix += "c".repeat(2) = "acc" → current="acc"
 * c=']': repeat=3, prefix="", inner="acc"
 *         prefix += "acc".repeat(3) = "accaccacc" → current="accaccacc"
 * return "accaccacc" ✓
 *
 * Trace — "2[abc]3[cd]ef"
 * -------------------------
 * After "2[abc]": current="abcabc"
 * After "3[cd]":  current="abcabccdcdcd"
 * After "ef":     current="abcabccdcdcdef"
 * return "abcabccdcdcdef" ✓
 */
