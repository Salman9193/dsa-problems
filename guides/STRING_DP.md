# String DP — LCS, Edit Distance, and Beyond

String DP problems involve two strings and ask about their relationship:
how similar are they? Can one be transformed into the other? What's the
longest common part?

---

## The General Framework

Most string DP problems use a 2D table:

```
dp[i][j] = answer for s1[0..i-1] and s2[0..j-1]
```

Base cases: `dp[0][j]` and `dp[i][0]` (empty string cases).

---

## Pattern 1: Longest Common Subsequence (#1143)

**LCS:** Longest subsequence appearing in both strings (not necessarily contiguous).

```java
public int longestCommonSubsequence(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m+1][n+1];

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1))
                dp[i][j] = dp[i-1][j-1] + 1;  // characters match: extend LCS
            else
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);  // skip one char
        }
    }
    return dp[m][n];
}
```

**Transition logic:**
- Match: `dp[i][j] = dp[i-1][j-1] + 1`
- No match: `dp[i][j] = max(dp[i-1][j], dp[i][j-1])` (skip from either string)

---

## Pattern 2: Edit Distance (#72)

**Edit Distance (Levenshtein):** Minimum operations (insert, delete, replace)
to transform s1 into s2.

```java
public int minDistance(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m+1][n+1];

    // Base: transform empty string to/from
    for (int i = 0; i <= m; i++) dp[i][0] = i;  // delete all of s1
    for (int j = 0; j <= n; j++) dp[0][j] = j;  // insert all of s2

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1))
                dp[i][j] = dp[i-1][j-1];          // no operation needed
            else
                dp[i][j] = 1 + Math.min(dp[i-1][j-1],   // replace
                                Math.min(dp[i-1][j],      // delete from s1
                                         dp[i][j-1]));    // insert into s1
        }
    }
    return dp[m][n];
}
```

**Three operations:**
- Replace: `dp[i-1][j-1] + 1` (use both chars, cost 1)
- Delete: `dp[i-1][j] + 1` (skip s1[i], cost 1)
- Insert: `dp[i][j-1] + 1` (skip s2[j], cost 1)

---

## Pattern 3: Longest Palindromic Subsequence (#516)

**LPS = LCS(s, reverse(s))**

```java
public int longestPalindromeSubseq(String s) {
    String rev = new StringBuilder(s).reverse().toString();
    return longestCommonSubsequence(s, rev);
}

// Or directly:
public int longestPalindromeSubseq(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) dp[i][i] = 1;  // single char is palindrome

    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            if (s.charAt(i) == s.charAt(j))
                dp[i][j] = dp[i+1][j-1] + 2;
            else
                dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
        }
    }
    return dp[0][n-1];
}
```

---

## Pattern 4: Distinct Subsequences (#115)

**Count ways s2 appears as a subsequence of s1.**

```java
public int numDistinct(String s, String t) {
    int m = s.length(), n = t.length();
    long[][] dp = new long[m+1][n+1];
    for (int i = 0; i <= m; i++) dp[i][0] = 1;  // empty t matches everywhere

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            dp[i][j] = dp[i-1][j];  // skip s[i]: don't use it
            if (s.charAt(i-1) == t.charAt(j-1))
                dp[i][j] += dp[i-1][j-1];  // use s[i] to match t[j]
        }
    }
    return (int) dp[m][n];
}
```

---

## The Relationship Between These Problems

```
LCS(s1, s2) = m + n - EditDistance(s1, s2) - (matches × 2 - LCS × 2)
                                               (rough relationship)

More precisely:
LCS relates to Edit Distance:
  editDistance = m + n - 2 × LCS  (when only inserts and deletes, no replaces)
  With replace: tighter relationship

LPS = LCS(s, reverse(s))
Min deletions to make palindrome = n - LPS(s)
Min insertions to make palindrome = n - LPS(s)
```

---

## Space Optimisation to O(n)

Both LCS and Edit Distance only need the previous row:

```java
// Space-optimised LCS
int[] dp = new int[n+1];
for (int i = 1; i <= m; i++) {
    int prev = 0;
    for (int j = 1; j <= n; j++) {
        int temp = dp[j];
        if (s1.charAt(i-1) == s2.charAt(j-1)) dp[j] = prev + 1;
        else dp[j] = Math.max(dp[j], dp[j-1]);
        prev = temp;
    }
}
```

---

## Summary Table

| Problem | Recurrence | Base cases |
|---------|-----------|------------|
| LCS | match: `dp[i-1][j-1]+1`; else: `max(dp[i-1][j], dp[i][j-1])` | all zeros |
| Edit Distance | match: `dp[i-1][j-1]`; else: `1+min(replace,delete,insert)` | `dp[i][0]=i, dp[0][j]=j` |
| LPS | match: `dp[i+1][j-1]+2`; else: `max(dp[i+1][j], dp[i][j-1])` | `dp[i][i]=1` |
| Distinct Subsequences | `dp[i-1][j]` + (match ? `dp[i-1][j-1]` : 0) | `dp[i][0]=1` |

---

## Further Problems Using These Patterns

| Problem | Core pattern |
|---------|-------------|
| #583 Delete Operation for Two Strings | `m + n - 2 × LCS` |
| #712 Min ASCII Delete Sum | Weighted LCS |
| #97 Interleaving String | 2D DP similar to LCS |
| #44 Wildcard Matching | Edit distance variant |
| #10 Regular Expression Matching | Edit distance with `.` and `*` |
