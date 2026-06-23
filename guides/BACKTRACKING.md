# Backtracking — Complete Pattern Guide

Backtracking is DFS with pruning — explore all possibilities but abandon
paths that can't lead to a valid solution. The canonical approach for
constraint satisfaction problems.

---

## The Template

```java
void backtrack(int start, List<Integer> current, List<List<Integer>> result) {
    // Base case: valid complete solution
    if (isComplete(current)) {
        result.add(new ArrayList<>(current));  // snapshot current state
        return;
    }

    for (int choice = start; choice < n; choice++) {
        // Pruning: skip invalid choices
        if (!isValid(choice, current)) continue;

        // Choose
        current.add(nums[choice]);

        // Explore
        backtrack(choice + 1, current, result);  // +1 for no-repeat, choice for repeats

        // Unchoose (backtrack)
        current.remove(current.size() - 1);
    }
}
```

---

## Core Problems

### #78 Subsets

```java
public List<List<Integer>> subsets(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, 0, new ArrayList<>(), result);
    return result;
}

void backtrack(int[] nums, int start, List<Integer> curr, List<List<Integer>> result) {
    result.add(new ArrayList<>(curr));  // every state is a valid subset
    for (int i = start; i < nums.length; i++) {
        curr.add(nums[i]);
        backtrack(nums, i+1, curr, result);  // i+1: each element used once
        curr.remove(curr.size()-1);
    }
}
```

### #90 Subsets II (with duplicates)

```java
Arrays.sort(nums);  // sort first!
void backtrack(int[] nums, int start, ...) {
    result.add(new ArrayList<>(curr));
    for (int i = start; i < nums.length; i++) {
        if (i > start && nums[i] == nums[i-1]) continue;  // skip duplicates
        curr.add(nums[i]);
        backtrack(nums, i+1, curr, result);
        curr.remove(curr.size()-1);
    }
}
```

---

### #46 Permutations

```java
public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(nums, new boolean[nums.length], new ArrayList<>(), result);
    return result;
}

void backtrack(int[] nums, boolean[] used, List<Integer> curr, List<List<Integer>> result) {
    if (curr.size() == nums.length) { result.add(new ArrayList<>(curr)); return; }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;
        curr.add(nums[i]);
        backtrack(nums, used, curr, result);
        curr.remove(curr.size()-1);
        used[i] = false;
    }
}
```

### #47 Permutations II (with duplicates)

```java
Arrays.sort(nums);
// Add: if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) continue;
```

---

### #77 Combinations

```java
public List<List<Integer>> combine(int n, int k) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(1, n, k, new ArrayList<>(), result);
    return result;
}

void backtrack(int start, int n, int k, List<Integer> curr, List<List<Integer>> result) {
    if (curr.size() == k) { result.add(new ArrayList<>(curr)); return; }
    // Pruning: not enough elements left
    for (int i = start; i <= n - (k - curr.size()) + 1; i++) {
        curr.add(i);
        backtrack(i+1, n, k, curr, result);
        curr.remove(curr.size()-1);
    }
}
```

---

### #51 N-Queens

```java
public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    int[] queens = new int[n];  // queens[row] = col position of queen in that row
    Arrays.fill(queens, -1);
    backtrack(queens, 0, n, new HashSet<>(), new HashSet<>(), new HashSet<>(), result);
    return result;
}

void backtrack(int[] queens, int row, int n,
               Set<Integer> cols, Set<Integer> diag1, Set<Integer> diag2,
               List<List<String>> result) {
    if (row == n) { result.add(buildBoard(queens, n)); return; }
    for (int col = 0; col < n; col++) {
        if (cols.contains(col) || diag1.contains(row-col) || diag2.contains(row+col)) continue;
        queens[row] = col;
        cols.add(col); diag1.add(row-col); diag2.add(row+col);
        backtrack(queens, row+1, n, cols, diag1, diag2, result);
        queens[row] = -1;
        cols.remove(col); diag1.remove(row-col); diag2.remove(row+col);
    }
}
```

---

### #79 Word Search

```java
public boolean exist(char[][] board, String word) {
    int r = board.length, c = board[0].length;
    for (int i = 0; i < r; i++)
        for (int j = 0; j < c; j++)
            if (dfs(board, word, i, j, 0)) return true;
    return false;
}

boolean dfs(char[][] board, String word, int i, int j, int k) {
    if (k == word.length()) return true;
    if (i<0 || i>=board.length || j<0 || j>=board[0].length) return false;
    if (board[i][j] != word.charAt(k)) return false;

    char temp = board[i][j];
    board[i][j] = '#';  // mark visited
    boolean found = dfs(board,word,i+1,j,k+1) || dfs(board,word,i-1,j,k+1)
                 || dfs(board,word,i,j+1,k+1) || dfs(board,word,i,j-1,k+1);
    board[i][j] = temp;  // restore
    return found;
}
```

---

## Key Distinctions

| Problem type | Start parameter | Used array |
|-------------|----------------|-----------|
| Subsets/Combinations | `i+1` (no reuse) | Not needed |
| Combination Sum (reuse allowed) | `i` (reuse) | Not needed |
| Permutations | `0` always | Yes — track used |
| Grid DFS | 4 directions | Mark visited in-place |

---

## Pruning Techniques

1. **Sort first** — enables early termination and duplicate skipping
2. **Size check** — `if (i <= n - (k - curr.size()) + 1)` in combinations
3. **Sum check** — if `currentSum > target`, stop
4. **Duplicate skip** — `if (i > start && nums[i] == nums[i-1]) continue`
5. **Trie pruning** — stop DFS when no word with current prefix exists

---

## Complexity

For n elements, k choices per step: O(k^n) worst case.
Pruning typically reduces this by orders of magnitude in practice.
Subsets: O(2^n × n). Permutations: O(n! × n). N-Queens: O(n!).
