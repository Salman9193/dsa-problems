class Solution {

    // Approach: Transfer Matrix DP — O(n) time, O(1) space
    //
    // Key insight: instead of tracking individual cell colours, track ROW TYPES.
    // With 3 colours and 3 cells per row, valid rows (no two adjacent cells same
    // colour) fall into exactly two structural categories:
    //
    //   ABA: first == third, e.g. 010, 020, 101, 121, 202, 212  → 6 patterns
    //   ABC: all different,  e.g. 012, 021, 102, 120, 201, 210  → 6 patterns
    //
    // Compatibility between consecutive rows (same column must differ):
    //   ABA above → 3 ABA + 2 ABC compatible rows below
    //   ABC above → 2 ABA + 2 ABC compatible rows below
    //
    // This gives a 2-variable recurrence (the 2×2 transfer matrix):
    //   new_aba = aba * 3 + abc * 2
    //   new_abc = aba * 2 + abc * 2
    //
    // Why only two variables suffice:
    //   By symmetry, all 6 ABA patterns are equivalent (same transition counts).
    //   All 6 ABC patterns are equivalent. So we only need aggregate counts.
    //
    // This is exactly the transfer matrix method from algebraic graph theory:
    // each DP iteration multiplies the state vector [aba, abc] by the matrix
    //   T = [[3, 2],
    //        [2, 2]]
    // so after n-1 iterations: [aba_n, abc_n] = T^(n-1) * [6, 6].
    public int numOfWays(int n) {
        final int MOD = 1_000_000_007;

        // Base case: n=1 row, 6 ABA patterns + 6 ABC patterns
        long aba = 6, abc = 6;

        for (int row = 1; row < n; row++) {
            long newAba = (aba * 3 + abc * 2) % MOD;
            long newAbc = (aba * 2 + abc * 2) % MOD;
            aba = newAba;
            abc = newAbc;
        }

        return (int) ((aba + abc) % MOD);
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n) — n-1 iterations of the recurrence
 * Space: O(1) — two variables, no array needed
 *
 * Why ABA→ABA gives 3 compatible rows (not 4 or 5):
 *   Take ABA = 010. Enumerate all valid rows below it:
 *   Constraints: col0_below ≠ 0, col1_below ≠ 1, col2_below ≠ 0
 *                col0_below ≠ col1_below, col1_below ≠ col2_below
 *
 *   (col0, col1, col2) with col0,col2 ∈ {1,2}, col1 ∈ {0,2}:
 *     (1,0,1) → ABA ✓    (1,0,2) → ABC ✓
 *     (1,2,1) → ABA ✓
 *     (2,0,1) → ABC ✓    (2,0,2) → ABA ✓
 *   Total: 3 ABA + 2 ABC ✓
 *   By symmetry, every ABA row has exactly 3 ABA + 2 ABC compatible successors.
 *
 * Why ABC→ABC gives only 2 (not 3):
 *   Take ABC = 012. col0_below ≠ 0, col1_below ≠ 1, col2_below ≠ 2:
 *     col0 ∈ {1,2}, col1 ∈ {0,2}, col2 ∈ {0,1}
 *   Valid rows (adjacent cells differ):
 *     (1,0,?) → col2 ∈ {0,1} ∩ {≠0} = {1} → (1,0,1) ABA ✓ wait 1≠0≠1: ABA ✓
 *              wait: need col2 ≠ col1=0, so col2 ≠ 0 → col2=1 → (1,0,1) ABA
 *     (1,2,0) → 1≠2✓, 2≠0✓ → ABC ✓
 *     (1,2,1) → 1≠2✓, 2≠1✓ → ABA ✓ but col2=1≠2✓
 *     (2,0,1) → 2≠0✓, 0≠1✓ → ABC ✓
 *     Check more carefully: total = 2 ABA + 2 ABC
 *   Total: 2 ABA + 2 ABC ✓
 *
 * Verification:
 *   n=1: aba=6,  abc=6.  total=12 ✓
 *   n=2: aba=30, abc=24. total=54 ✓
 *   n=3: aba=138,abc=108.total=246 ✓
 *
 * Transfer matrix form (for matrix exponentiation to get O(log n)):
 *   [aba_n]   [3 2]^(n-1)   [6]
 *   [abc_n] = [2 2]       * [6]
 *
 * For n ≤ 1000 (problem constraint), O(n) is perfectly fine.
 * For very large n (n = 10^18), use matrix exponentiation → O(log n).
 */
