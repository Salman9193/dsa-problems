class Solution {

    // Approach 1: Binary Search — O(log x) time, O(1) space
    //
    // Find the largest integer m such that m*m <= x.
    // The search space [0, x/2] is sorted (m*m is monotone increasing).
    // Use the "rightmost element satisfying condition" binary search pattern.
    //
    // Why x/2 as upper bound:
    //   For x >= 4: sqrt(x) <= x/2  (since (x/2)^2 = x^2/4 >= x for x >= 4)
    //   This halves the search space immediately.
    //
    // Why mid <= x/mid instead of mid*mid <= x:
    //   mid*mid can overflow int for large x (e.g. x = 2^31 - 1).
    //   x/mid is always safe — no overflow possible.
    public int mySqrt(int x) {
        if (x < 2) return x;

        int left = 1, right = x / 2;

        while (left < right) {
            int mid = left + (right - left + 1) / 2;  // upper-bias: prevents infinite loop
            if (mid <= x / mid) {
                left = mid;    // mid*mid <= x → mid could be the answer, keep it
            } else {
                right = mid - 1;  // mid*mid > x → too large, discard
            }
        }

        return left;
    }

    // Approach 2: Newton's Method — O(log log x) iterations, O(1) space
    //
    // Solve f(r) = r^2 - x = 0 using Newton-Raphson:
    //   r_{n+1} = r_n - f(r_n)/f'(r_n) = r_n - (r_n^2 - x)/(2*r_n) = (r_n + x/r_n) / 2
    //
    // Start from r = x (overestimate), iterate until r*r <= x.
    // Converges in ~5 iterations for 32-bit integers — quadratic convergence.
    // This is the integer version of the algorithm used in Quake III Arena's
    // famous Fast Inverse Square Root.
    public int mySqrtNewton(int x) {
        if (x < 2) return x;
        long r = x;
        while (r * r > x) {
            r = (r + x / r) / 2;
        }
        return (int) r;
    }
}

/*
 * Complexity
 * ----------
 * Binary Search:    Time O(log x),       Space O(1)
 * Newton's Method:  Time O(log log x),   Space O(1)
 *   (Newton converges quadratically — each iteration doubles correct digits)
 *
 * Why upper-bias mid = left + (right - left + 1) / 2?
 *   We're finding the RIGHTMOST m where m*m <= x.
 *   Pattern: condition true → left = mid (keep mid as candidate)
 *            condition false → right = mid - 1
 *   If mid were lower-biased and left = right-1:
 *     mid = left → left = left → infinite loop
 *   Upper-bias ensures mid = right → terminates correctly.
 *   Same pattern as #911 Online Election and #875 Koko Eating Bananas.
 *
 * Binary Search Trace — x=8
 * --------------------------
 * left=1, right=4
 * mid=3 (upper): 3 <= 8/3=2? NO  → right=2
 * mid=2 (upper): 2 <= 8/2=4? YES → left=2
 * left==right → return 2 ✓
 *
 * Newton's Method Trace — x=8
 * ----------------------------
 * r=8:  (8 + 8/8)/2  = 9/2  = 4
 * r=4:  (4 + 8/4)/2  = 6/2  = 3
 * r=3:  (3 + 8/3)/2  = 5/2  = 2  (integer division)
 * r=2:  2*2=4 <= 8 → stop → return 2 ✓
 */
