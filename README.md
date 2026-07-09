# DSA Problems

A structured collection of DSA problems with:
- Clean solutions and optimisations
- Intuition behind each technique
- Real-world use cases backed by papers and engineering blogs
- Extensions section in every NOTES.md for Staff Engineer preparation

## Structure

Each problem lives in `topic/problem-name/` and contains:
- `Solution.java` — clean implementation with complexity analysis and trace
- `NOTES.md` — full intuition, worked examples, edge cases, **extensions**
- `USE_CASES.md` — real-world applications with links to papers, docs, and source code

## Problems

### Arrays & Two Pointers
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 1 | [Two Sum II](./arrays/two-sum-ii) | Two Pointers | Medium |
| 2 | [3Sum](./arrays/3sum) | Two Pointers | Medium |
| 3 | [Container With Most Water](./arrays/container-with-most-water) | Two Pointers | Medium |
| 4 | [Trapping Rain Water](./arrays/trapping-rain-water) | Two Pointers | Hard |
| 5 | [Product of Array Except Self](./arrays/product-of-array-except-self) | Prefix Sum | Medium |
| 6 | [Find Pivot Index](./arrays/find-pivot-index) | Prefix Sum | Easy |
| 7 | [Sliding Window Maximum](./arrays/sliding-window-maximum) | Monotonic Deque | Hard |
| 8 | [Car Pooling](./arrays/car-pooling) | Sweep Line | Medium |
| 9 | [Longest Consecutive Sequence](./arrays/longest-consecutive-sequence) | HashMap | Medium |
| 10 | [Group Anagrams](./arrays/group-anagrams) | HashMap | Medium |
| 11 | [Top K Frequent Elements](./arrays/top-k-frequent-elements) | Heap/HashMap | Medium |
| 12 | [Divide Two Integers](./arrays/divide-two-integers) | Bit Manipulation | Medium |
| 13 | [Minimum Cost to Hire K Workers](./arrays/minimum-cost-hire-k-workers) | Greedy/Heap | Hard |
| 14 | [Best Time to Buy and Sell Stock I](./arrays/best-time-to-buy-and-sell-stock) | Greedy | Easy |
| 15 | [Best Time to Buy and Sell Stock II](./arrays/best-time-to-buy-and-sell-stock-ii) | Greedy | Medium |
| 16 | [Best Time to Buy and Sell Stock III](./arrays/best-time-to-buy-and-sell-stock-iii) | DP | Hard |
| 17 | [Best Time to Buy and Sell Stock IV](./arrays/best-time-to-buy-and-sell-stock-iv) | DP | Hard |
| 18 | [Make Two Arrays Equal by Reversing](./arrays/make-two-arrays-equal) | Sorting | Easy |
| 19 | [Minimum Time Visiting All Points](./arrays/minimum-time-visiting-all-points) | Math | Easy |
| 20 | [Merge Intervals](./arrays/merge-intervals) | Intervals | Medium |
| 21 | [Meeting Rooms II](./arrays/meeting-rooms-ii) | Intervals/Heap | Medium |
| 22 | [Kth Largest Element](./arrays/kth-largest-element) | Heap/Quickselect | Medium |
| 23 | [Maximum of Absolute Value Expression](./arrays/maximum-of-absolute-value-expression) | Math/Geometry | Medium |

### Strings
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 24 | [Valid Palindrome](./strings/valid-palindrome) | Two Pointers | Easy |
| 25 | [Longest Palindromic Substring](./strings/longest-palindromic-substring) | DP/Expand | Medium |
| 26 | [Longest Substring Without Repeating](./strings/longest-substring-without-repeating) | Sliding Window | Medium |
| 27 | [Longest Repeating Character Replacement](./strings/longest-repeating-character-replacement) | Sliding Window | Medium |
| 28 | [Minimum Window Substring](./strings/minimum-window-substring) | Sliding Window | Hard |
| 29 | [Find All Anagrams](./strings/find-all-anagrams) | Sliding Window | Medium |
| 30 | [First Unique Character](./strings/first-unique-character) | HashMap | Easy |
| 31 | [Encode and Decode Strings](./strings/encode-and-decode-strings) | Design | Medium |
| 32 | [Decode String](./strings/decode-string) | Stack | Medium |
| 33 | [Minimum Remove to Make Valid Parentheses](./strings/minimum-remove-valid-parentheses) | Stack | Medium |
| 34 | [Implement Trie](./strings/implement-trie) | Trie | Medium |
| 35 | [Sort Characters By Frequency](./strings/sort-characters-by-frequency) | Bucket Sort / Hashing | Medium |

### Linked List
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 36 | [Reverse Linked List](./linked-list/reverse-linked-list) | Linked List | Easy |
| 37 | [Linked List Cycle](./linked-list/linked-list-cycle) | Fast/Slow Pointers | Easy |
| 38 | [Linked List Random Node](./linked-list/linked-list-random-node) | Reservoir Sampling | Medium |
| 39 | [Palindrome Linked List](./linked-list/palindrome-linked-list) | Fast/Slow + Reversal | Easy |
| 40 | [Flatten a Multilevel Doubly Linked List](./linked-list/flatten-a-multilevel-doubly-linked-list) | DFS / Pointer Rewiring | Medium |

### Binary Search
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 41 | [Search in Rotated Sorted Array](./binary-search/search-in-rotated-sorted-array) | Binary Search | Medium |
| 42 | [Find in Mountain Array](./binary-search/find-in-mountain-array) | Binary Search | Hard |
| 43 | [Sqrt(x)](./binary-search/sqrt-x) | Binary Search | Easy |
| 44 | [Online Election](./binary-search/online-election) | Binary Search/Design | Medium |

### Stacks
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 45 | [Valid Parentheses](./stacks/valid-parentheses) | Stack | Easy |
| 46 | [Daily Temperatures](./stacks/daily-temperatures) | Monotonic Stack | Medium |
| 47 | [Largest Rectangle in Histogram](./stacks/largest-rectangle-histogram) | Monotonic Stack | Hard |

### Dynamic Programming
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 48 | [Climbing Stairs](./dynamic-programming/climbing-stairs) | 1D DP | Easy |
| 49 | [Coin Change](./dynamic-programming/coin-change) | 1D DP | Medium |
| 50 | [Word Break](./dynamic-programming/word-break) | 1D DP | Medium |
| 51 | [House Robber](./dynamic-programming/house-robber) | 1D DP / Take-or-Skip | Medium |
| 52 | [Jump Game](./dynamic-programming/jump-game) | Greedy/DP | Medium |
| 53 | [Unique Paths](./dynamic-programming/unique-paths) | 2D Grid DP | Medium |
| 54 | [Cherry Pickup II](./dynamic-programming/cherry-pickup-ii) | 2D DP | Hard |
| 55 | [Number of Ways to Paint N×3 Grid](./dynamic-programming/number-of-ways-paint-grid) | DP/Combinatorics | Hard |
| 56 | [Partition Equal Subset Sum](./dynamic-programming/partition-equal-subset-sum) | 0-1 Knapsack | Medium |
| 57 | [Longest Increasing Subsequence](./dynamic-programming/longest-increasing-subsequence) | DP / Patience Sorting | Medium |
| 58 | [Burst Balloons](./dynamic-programming/burst-balloons) | Interval DP | Hard |
| 59 | [Subsets](./dynamic-programming/subsets) | Backtracking | Medium |
| 60 | [Permutations](./dynamic-programming/permutations) | Backtracking | Medium |
| 61 | [Combination Sum](./dynamic-programming/combination-sum) | Backtracking | Medium |
| 62 | [Word Search](./dynamic-programming/word-search) | Backtracking / 2D Grid | Medium |
| 63 | [Shortest Common Supersequence](./dynamic-programming/shortest-common-supersequence) | LCS / 2D DP | Hard |

### Trees
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 64 | [Populating Next Right Pointers II](./trees/populating-next-right-pointers-ii) | BFS | Medium |
| 65 | [Delete Nodes and Return Forest](./trees/delete-nodes-and-return-forest) | DFS | Medium |
| 66 | [LCA of Deepest Leaves](./trees/lca-deepest-leaves) | DFS | Medium |
| 67 | [All Possible Full Binary Trees](./trees/all-possible-full-binary-trees) | Recursion/Memo | Medium |
| 68 | [Binary Search Tree Iterator](./trees/binary-search-tree-iterator) | Stack/Design | Medium |
| 69 | [Lowest Common Ancestor](./trees/lowest-common-ancestor) | DFS | Medium |
| 70 | [Binary Tree Maximum Path Sum](./trees/binary-tree-max-path-sum) | DFS | Hard |
| 71 | [Validate BST](./trees/validate-bst) | DFS / Range Passing | Medium |

### Graphs
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 72 | [Clone Graph](./graphs/clone-graph) | BFS/DFS | Medium |
| 73 | [Find if Path Exists](./graphs/find-if-path-exists) | BFS/DFS/DSU | Easy |
| 74 | [Number of Islands](./graphs/number-of-islands) | BFS/DFS/DSU | Medium |
| 75 | [Rotting Oranges](./graphs/rotting-oranges) | Multi-Source BFS | Medium |
| 76 | [01 Matrix](./graphs/01-matrix) | Multi-Source BFS | Medium |
| 77 | [Shortest Path in Binary Matrix](./graphs/shortest-path-binary-matrix) | BFS | Medium |
| 78 | [Word Ladder](./graphs/word-ladder) | BFS/Implicit Graph | Hard |
| 79 | [Max Area of Island](./graphs/max-area-of-island) | DFS/BFS | Medium |
| 80 | [Pacific Atlantic Water Flow](./graphs/pacific-atlantic-water-flow) | Multi-Source BFS | Medium |
| 81 | [Surrounded Regions](./graphs/surrounded-regions) | BFS/Union-Find | Medium |
| 82 | [Number of Provinces](./graphs/number-of-provinces) | DFS/BFS/DSU | Medium |
| 83 | [Course Schedule](./graphs/course-schedule) | Topological Sort | Medium |
| 84 | [Course Schedule II](./graphs/course-schedule-ii) | Topological Sort | Medium |
| 85 | [Find Eventual Safe States](./graphs/find-eventual-safe-states) | DFS/Cycle Detection | Medium |
| 86 | [Alien Dictionary](./graphs/alien-dictionary) | Topological Sort | Hard |
| 87 | [Is Graph Bipartite?](./graphs/is-graph-bipartite) | BFS/DFS | Medium |
| 88 | [Possible Bipartition](./graphs/possible-bipartition) | BFS/DFS/DSU | Medium |
| 89 | [Redundant Connection](./graphs/redundant-connection) | Union-Find | Medium |
| 90 | [Accounts Merge](./graphs/accounts-merge) | Union-Find | Medium |
| 91 | [Most Stones Removed](./graphs/most-stones-removed) | Union-Find | Medium |
| 92 | [Evaluate Division](./graphs/evaluate-division) | BFS/Weighted | Medium |
| 93 | [Network Delay Time](./graphs/network-delay-time) | Dijkstra's | Medium |
| 94 | [Path with Minimum Effort](./graphs/path-minimum-effort) | Dijkstra's/BST/DSU | Medium |
| 95 | [Cheapest Flights Within K Stops](./graphs/cheapest-flights-k-stops) | Bellman-Ford | Medium |
| 96 | [Swim in Rising Water](./graphs/swim-in-rising-water) | Dijkstra's/BST/DSU | Hard |
| 97 | [Min Cost to Connect All Points](./graphs/min-cost-connect-points) | MST/Prim's/Kruskal's | Medium |
| 98 | [Connecting Cities With Min Cost](./graphs/connecting-cities-min-cost) | MST/Kruskal's | Medium |
| 99 | [Critical Connections](./graphs/critical-connections) | Tarjan's Bridge | Hard |
| 100 | [Minimum Height Trees](./graphs/minimum-height-trees) | BFS/Topological | Medium |

### Design
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 101 | [LRU Cache](./design/lru-cache) | DLL + HashMap | Medium |
| 102 | [Exam Room](./design/exam-room) | Sorted Set | Medium |
| 103 | [Find Median from Data Stream](./design/find-median-data-stream) | Two Heaps | Hard |

### Bit Manipulation
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 104 | [Number of 1 Bits](./bit-manipulation/hamming-weight) | Bit Manipulation | Easy |

### Binary Search (Advanced)
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 105 | [Lowest Common Ancestor (Binary Tree)](./trees/lowest-common-ancestor) | DFS | Medium |

## Guides

| Guide | Description |
|-------|-------------|
| [Greedy vs DP](./guides/GREEDY_VS_DP.md) | When greedy suffices vs when you need DP |
| [Graph Cycle Detection](./guides/GRAPH_CYCLE_DETECTION.md) | Floyd's, DFS, Kahn's |
| [Complexity Theory](./guides/COMPLEXITY_THEORY.md) | P vs NP, NP-complete, reductions |
| [k-Colouring](./guides/K_COLOURING.md) | Bipartite → k colours, complexity jump at k=3 |
| [Knapsack Variants](./guides/KNAPSACK_VARIANTS.md) | All 6 knapsack scenarios, direction rule |
| [Union-Find](./guides/UNION_FIND.md) | DSU with path compression + rank; Kruskal's MST |
| [Trie](./guides/TRIE.md) | Prefix tree; insert/search/wildcard/Word Search II |
| [Graph Algorithms](./guides/GRAPH_ALGORITHMS.md) | Topological sort + Dijkstra + Bellman-Ford |
| [Intervals](./guides/INTERVALS.md) | Merge, insert, meeting rooms, non-overlapping |
| [Monotonic Stack](./guides/MONOTONIC_STACK.md) | Daily temperatures, histogram, next greater |
| [Binary Search on Answer](./guides/BINARY_SEARCH_ON_ANSWER.md) | Koko, ship packages, split array |
| [Backtracking](./guides/BACKTRACKING.md) | Subsets, permutations, N-Queens, word search |
| [String DP](./guides/STRING_DP.md) | LCS, edit distance, LPS, distinct subsequences |
| [SCC, Bridges & Articulation Points](./guides/SCC_BRIDGES_AP.md) | Kosaraju, Tarjan SCC, bridge/AP |
| [Floyd-Warshall, Max Flow & Bipartite Matching](./guides/FLOW_MATCHING.md) | Tier 3 advanced |

---
> 109 problems · 16 guides · Complete Staff/Principal Engineer preparation resource

> Last updated: 2026-07-05 — added Flatten a Multilevel Doubly Linked List (#430): O(1)-space DFS via pointer splicing
