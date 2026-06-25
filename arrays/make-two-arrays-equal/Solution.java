import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {

    // Approach 1: Sort and compare — O(n log n) time, O(n) space (or O(1) with in-place sort)
    //
    // Key insight: reversing any subarray any number of times is equivalent to
    // performing any permutation of the array.
    //
    // Proof: any permutation decomposes into adjacent transpositions.
    // An adjacent swap of positions i and i+1 is a reversal of subarray [i, i+1].
    // Since we can reverse any subarray any number of times, we can perform any
    // sequence of adjacent swaps → any permutation is reachable.
    //
    // Therefore: arr can be made equal to target iff they are permutations of each
    // other iff they have the same multiset of elements (same values, same frequencies).
    //
    // Checking multiset equality via sorting: sort both, compare element-by-element.
    public boolean canBeEqual(int[] target, int[] arr) {
        Arrays.sort(target);
        Arrays.sort(arr);
        return Arrays.equals(target, arr);
    }

    // Approach 2: Frequency map — O(n) time, O(n) space
    //
    // Preferred when:
    //   - Elements are not comparable (no natural ordering)
    //   - n is very large and O(n log n) sort is too slow
    //   - Input must not be mutated (sort modifies the array in-place)
    //
    // Increment count for every element in target, decrement for every element in arr.
    // If any count is non-zero, the multisets differ.
    public boolean canBeEqualFreqMap(int[] target, int[] arr) {
        if (target.length != arr.length) return false;

        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : target) freq.merge(x,  1, Integer::sum);
        for (int x : arr)    freq.merge(x, -1, Integer::sum);

        for (int count : freq.values()) {
            if (count != 0) return false;
        }
        return true;
    }
}

/*
 * Complexity
 * ----------
 * Sort and compare:  Time O(n log n), Space O(n) for sort buffer (O(1) with in-place)
 * Frequency map:     Time O(n),       Space O(distinct values) ≤ O(n)
 *
 * Why the problem LOOKS harder than it is:
 *   "Reversing subarrays" sounds like a constrained operation. The key is to ask:
 *   "Does this operation generate ALL permutations?" — if yes, the problem reduces
 *   to multiset equality. Recognising this collapses a seemingly complex problem
 *   into a trivial one.
 *
 *   Contrast with the HARD version: "sort arr into target using the MINIMUM number
 *   of reversals" — that is NP-hard for unsigned permutations (genome rearrangement
 *   problem). Unlimited reversals makes it trivially easy.
 *
 * The general pattern:
 *   "Any number of [operation X]" → ask if X generates all permutations.
 *   - Any subarray reversal   → yes → multiset equality
 *   - Any adjacent swap       → yes → multiset equality
 *   - Any rotation            → no  → string rotation check (Knuth's trick)
 *   - Swap elements at i,j    → yes → multiset equality
 *
 * Trace — target=[1,2,3,4], arr=[2,4,1,3]
 * -----------------------------------------
 * Approach 1 (sort): [1,2,3,4] == [1,2,3,4] → true ✓
 * Approach 2 (freq): freq after target: {1:1,2:1,3:1,4:1}
 *                    freq after arr:    {1:0,2:0,3:0,4:0} → all zero → true ✓
 *
 * Trace — target=[3,7,9], arr=[3,7,11]
 * --------------------------------------
 * Approach 1: [3,7,9] != [3,7,11] → false ✓
 * Approach 2: freq after target: {3:1,7:1,9:1}
 *             freq after arr:    {3:0,7:0,9:1,11:-1}
 *             count[9]=1 != 0 → false ✓
 */
