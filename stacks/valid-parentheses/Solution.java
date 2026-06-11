import java.util.ArrayDeque;
import java.util.Deque;

class Solution {

    // Approach: Stack — O(n) time, O(n) space
    //
    // Brackets have LIFO nesting: the most recently opened must be the next closed.
    // A stack naturally models this.
    //
    // Variant 1: push the EXPECTED CLOSER when an opener is seen.
    // Then for every closing bracket, just check if it matches the top.
    // The matching logic is encoded at push time — cleaner than checking at pop time.
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if      (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else {
                // closing bracket: must match the expected closer on top
                if (stack.isEmpty() || stack.pop() != c) return false;
            }
        }

        // all openers must be matched — stack must be empty
        return stack.isEmpty();
    }

    // Variant 2: push the OPENER, check at pop time.
    // Equivalent but slightly more verbose — included for reference.
    public boolean isValidV2(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }

        return stack.isEmpty();
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — single pass
 * Space: O(n) — stack holds at most n/2 openers in the worst case
 *               e.g. "(((((...)))))" — n/2 push operations
 *
 * Why a stack (not a counter):
 *   A counter works for a single bracket type (count open, count close).
 *   For multiple types, a counter can't distinguish "(]" from "()" —
 *   both have count[open]=1, count[close]=1. The stack preserves ORDER.
 *
 * Why push the expected closer (Variant 1):
 *   Encoding the match at push time means the closing check is always
 *   a single comparison: stack.pop() != c. No if/else chain needed.
 *
 * Trace — "{[]}"
 *   c='{': push '}'  stack=['}']
 *   c='[': push ']'  stack=['}',']']
 *   c=']': pop ']' == ']' ✓  stack=['}']
 *   c='}': pop '}' == '}' ✓  stack=[]
 *   stack empty → true ✓
 *
 * Trace — "([)]"
 *   c='(': push ')'  stack=[')']
 *   c='[': push ']'  stack=[')',']']
 *   c=')': pop ']' != ')' → return false ✗
 *
 * Edge cases:
 *   ""      → true  (empty stack)
 *   "("     → false (stack not empty at end)
 *   ")"     → false (stack empty on closing bracket)
 *   "([])"  → true  (properly nested)
 *   "([)]"  → false (wrong nesting order)
 */
