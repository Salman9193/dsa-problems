import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

class Solution {

    // Approach: Stack of indices + substring extraction — O(n) time, O(n) space
    //
    // Key insight: a parenthesis is invalid iff it has no matching partner.
    //   Unmatched ')': a ')' with no prior unmatched '('
    //   Unmatched '(': a '(' with no later unmatched ')'
    //
    // We use a stack to track indices of unmatched parentheses:
    //   On '(': push its index (candidate for removal if never matched)
    //   On ')': if stack top is '(' → they match, pop (both stay)
    //            else → this ')' is unmatched, push its index
    //
    // Why a stack (not a deque)?
    //   We always match a ')' with the MOST RECENT unmatched '('.
    //   LIFO access is exactly what we need — only the top matters.
    //   A deque exposes both ends, adding complexity without benefit here.
    //
    // After scanning: stack contains exactly the indices of all unmatched
    // parentheses, in ascending order (after sorting).
    //
    // Substring extraction avoids a HashSet membership check per character:
    //   Instead of marking indices and filtering, extract substrings
    //   between removal points and concatenate — cleaner and cache-friendly.
    public String minRemoveToMakeValid(String s) {
        Deque<Integer> stack = new ArrayDeque<>();  // indices of unmatched parens

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);  // candidate for removal if never matched
            } else if (c == ')') {
                if (!stack.isEmpty() && s.charAt(stack.peek()) == '(') {
                    stack.pop();   // matched pair — neither is removed
                } else {
                    stack.push(i); // unmatched ')' — mark for removal
                }
            }
            // letters: no action needed
        }

        // Stack is already valid — early exit if no removals needed
        if (stack.isEmpty()) return s;

        // Sort removal indices ascending to extract substrings left to right
        List<Integer> removeIdx = new ArrayList<>(stack);
        Collections.sort(removeIdx);

        // Extract substrings between each removal point and concatenate
        StringBuilder result = new StringBuilder();
        int prev = 0;
        for (int idx : removeIdx) {
            result.append(s, prev, idx);  // keep s[prev..idx-1]
            prev = idx + 1;               // skip s[idx] (the removed char)
        }
        result.append(s, prev, s.length()); // tail after last removal

        return result.toString();
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n log k) — O(n) scan + O(k log k) sort of k removal indices
 *        In practice k << n, so effectively O(n).
 *        Can be made O(n) by using a stack that produces ascending indices
 *        (process '(' left-to-right, '(' indices are already ascending in stack).
 * Space: O(n) — stack + result string
 *
 * Why stack matches the MOST RECENT '(' with each ')':
 *   Parentheses are a context-free language — the innermost unclosed '(' must
 *   be closed first. This LIFO matching is the formal definition of correct
 *   nesting and is exactly what a pushdown automaton (stack machine) implements.
 *
 * Why the check s.charAt(stack.peek()) == '(':
 *   The stack holds BOTH unmatched '(' and unmatched ')' indices.
 *   We only match a ')' with a prior '(' — not with another ')'.
 *   Without this check, we'd incorrectly pair two unmatched ')' together.
 *
 * Why substring extraction > HashSet membership:
 *   HashSet approach: for each of n characters, do a HashSet lookup → O(n) ops
 *   but with hash computation overhead per character.
 *   Substring approach: for each of k removal points, copy a contiguous
 *   substring → cache-friendly sequential memory access, no hashing.
 *   Both are O(n) total; substring approach has better constant factor.
 *
 * Trace — "a)b(c)d"
 * -------------------
 * i=1 ')': stack empty → push 1. stack=[1]
 * i=3 '(': push 3. stack=[1,3]
 * i=5 ')': top=3 is '(' → pop. stack=[1]
 * removeIdx=[1]
 * result: s[0,1)="a" + s[2,7)="b(c)d" = "ab(c)d" ✓
 *
 * Trace — "(a(b(c)d)"
 * --------------------
 * i=0 '(': push 0. stack=[0]
 * i=2 '(': push 2. stack=[0,2]
 * i=4 '(': push 4. stack=[0,2,4]
 * i=6 ')': top=4 is '(' → pop. stack=[0,2]
 * i=8 ')': top=2 is '(' → pop. stack=[0]
 * removeIdx=[0]
 * result: s[0,0)="" + s[1,9)="a(b(c)d)" = "a(b(c)d)" ✓
 *
 * Trace — "))(("
 * ---------------
 * i=0 ')': stack empty → push 0. stack=[0]
 * i=1 ')': top=0 is ')' (not '(') → push 1. stack=[0,1]
 * i=2 '(': push 2. stack=[0,1,2]
 * i=3 '(': push 3. stack=[0,1,2,3]
 * removeIdx=[0,1,2,3] → all characters removed → "" ✓
 */
