# Number of Longest Increasing Subsequence — Notes & Intuition

**LeetCode #673** | Dynamic Programming (counting) | Medium
Not the length of the LIS — **how many** LIS there are.

---

## Problem

```
Input:  nums = [1,3,5,4,7]
Output: 2        →  [1,3,4,7] and [1,3,5,7]

Input:  nums = [2,2,2,2,2]
Output: 5        →  five LIS of length 1
```

---

## The Approach — Two Arrays, One Rule

Carry a **count** array alongside the usual DP:

- `len[i]` = length of the longest increasing subsequence **ending at** `i`
- `cnt[i]` = **how many** such longest subsequences end at `i`

Both start at 1 (the element alone).

### The rule that matters: reset vs. accumulate

For each `j < i` with `nums[j] < nums[i]`:

```java
if (len[j] + 1 > len[i]) {        // strictly LONGER → RESET
    len[i] = len[j] + 1;
    cnt[i] = cnt[j];              // discard old count; inherit j's
} else if (len[j] + 1 == len[i]) { // SAME length → ACCUMULATE
    cnt[i] += cnt[j];             // another distinct way to reach this length
}
```

**This two-case split is the entire problem.** A strictly longer chain **invalidates**
everything counted so far for `i` (so *overwrite*). An equally long chain is a **genuinely
different** way to get there (so *add*). Confuse them and you either overcount or lose branches.

### The answer

```java
int best = max(len);
return sum of cnt[i] for all i where len[i] == best;
```

**Careful:** the answer is **not** `cnt[lastIndex]` or `max(cnt)`. Multiple *positions* can each
end an LIS of the maximum length — you must **sum their counts**.

---

## Full Solution

```java
public int findNumberOfLIS(int[] nums) {
    int n = nums.length;
    int[] len = new int[n], cnt = new int[n];
    Arrays.fill(len, 1);
    Arrays.fill(cnt, 1);

    int best = 1;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                if (len[j] + 1 > len[i]) { len[i] = len[j] + 1; cnt[i] = cnt[j]; }
                else if (len[j] + 1 == len[i]) { cnt[i] += cnt[j]; }
            }
        }
        best = Math.max(best, len[i]);
    }

    int total = 0;
    for (int i = 0; i < n; i++) if (len[i] == best) total += cnt[i];
    return total;
}
```

**O(n²)** time, **O(n)** space.

---

## Full Trace — `nums = [1,3,5,4,7]`

| i | nums[i] | len[i] | cnt[i] | why |
|---|---------|--------|--------|-----|
| 0 | 1 | 1 | 1 | base |
| 1 | 3 | 2 | 1 | extends 1 |
| 2 | 5 | 3 | 1 | extends 1→3 |
| 3 | 4 | 3 | 1 | extends 1→3 (5 is too big) |
| 4 | 7 | 4 | **2** | extends *both* the len-3 chains ending at 5 and at 4 → accumulate 1+1 |

`best = 4`; only `i = 4` has `len = 4` → answer **2**. ✓
(`[1,3,5,7]` and `[1,3,4,7]`.)

Note how `cnt[4] = 2` arises purely from the **accumulate** branch firing twice.

---

## Why the Naive Fixes Fail

| Mistake | Why it breaks |
|---------|---------------|
| `return cnt[n-1]` | The LIS need not end at the last element |
| `return max(cnt)` | Several positions may each end a maximum-length LIS — they must be **summed** |
| Using `>=` instead of `>` for the reset | Would reset on ties, destroying accumulated counts |
| Accumulating on strictly-longer | Would mix counts of different lengths |

---

## Complexity

| Approach | Time | Space |
|----------|------|-------|
| **DP with count[]** | **O(n²)** | **O(n)** |
| Segment tree / BIT (count + max) | O(n log n) | O(n) |

The O(n log n) version stores `(maxLen, count)` pairs in a segment tree keyed by value, but the
O(n²) DP is the expected interview answer.

---

## The Family

| Problem | Question |
|---------|----------|
| [LIS #300](#dynamic-programming/longest-increasing-subsequence) | the **length** |
| **Number of LIS #673** | **how many** |
| [Russian Doll #354](#dynamic-programming/russian-doll-envelopes) | LIS on a 2-D poset |
| [Largest Divisible Subset #368](#dynamic-programming/largest-divisible-subset) | longest chain under divisibility |

**The same reset/accumulate rule counts maximum chains in *any* of these posets** — swap
`nums[j] < nums[i]` for the relevant comparability test (e.g. `nums[i] % nums[j] == 0`) and the
counting logic is unchanged.

**The through-line:** carry `cnt[]` beside `len[]`; **reset** on a strictly longer chain,
**accumulate** on a tie, and **sum** the counts of every position achieving the maximum length.
