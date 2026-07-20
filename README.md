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
| 24 | [Max Points on a Line](./arrays/max-points-on-a-line) | Geometry / Hashing | Hard |
| 25 | [Task Scheduler](./arrays/task-scheduler) | Greedy / Heap | Medium |

### Strings
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 26 | [Valid Palindrome](./strings/valid-palindrome) | Two Pointers | Easy |
| 27 | [Longest Palindromic Substring](./strings/longest-palindromic-substring) | DP/Expand | Medium |
| 28 | [Longest Substring Without Repeating](./strings/longest-substring-without-repeating) | Sliding Window | Medium |
| 29 | [Longest Repeating Character Replacement](./strings/longest-repeating-character-replacement) | Sliding Window | Medium |
| 30 | [Minimum Window Substring](./strings/minimum-window-substring) | Sliding Window | Hard |
| 31 | [Find All Anagrams](./strings/find-all-anagrams) | Sliding Window | Medium |
| 32 | [First Unique Character](./strings/first-unique-character) | HashMap | Easy |
| 33 | [Encode and Decode Strings](./strings/encode-and-decode-strings) | Design | Medium |
| 34 | [Decode String](./strings/decode-string) | Stack | Medium |
| 35 | [Minimum Remove to Make Valid Parentheses](./strings/minimum-remove-valid-parentheses) | Stack | Medium |
| 36 | [Implement Trie](./strings/implement-trie) | Trie | Medium |
| 37 | [Sort Characters By Frequency](./strings/sort-characters-by-frequency) | Bucket Sort / Hashing | Medium |

### Linked List
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 38 | [Reverse Linked List](./linked-list/reverse-linked-list) | Linked List | Easy |
| 39 | [Linked List Cycle](./linked-list/linked-list-cycle) | Fast/Slow Pointers | Easy |
| 40 | [Linked List Random Node](./linked-list/linked-list-random-node) | Reservoir Sampling | Medium |
| 41 | [Palindrome Linked List](./linked-list/palindrome-linked-list) | Fast/Slow + Reversal | Easy |
| 42 | [Flatten a Multilevel Doubly Linked List](./linked-list/flatten-a-multilevel-doubly-linked-list) | DFS / Pointer Rewiring | Medium |

### Binary Search
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 43 | [Search in Rotated Sorted Array](./binary-search/search-in-rotated-sorted-array) | Binary Search | Medium |
| 44 | [Find in Mountain Array](./binary-search/find-in-mountain-array) | Binary Search | Hard |
| 45 | [Sqrt(x)](./binary-search/sqrt-x) | Binary Search | Easy |
| 46 | [Online Election](./binary-search/online-election) | Binary Search/Design | Medium |

### Stacks
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 47 | [Valid Parentheses](./stacks/valid-parentheses) | Stack | Easy |
| 48 | [Daily Temperatures](./stacks/daily-temperatures) | Monotonic Stack | Medium |
| 49 | [Largest Rectangle in Histogram](./stacks/largest-rectangle-histogram) | Monotonic Stack | Hard |

### Dynamic Programming
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 50 | [Climbing Stairs](./dynamic-programming/climbing-stairs) | 1D DP | Easy |
| 51 | [Coin Change](./dynamic-programming/coin-change) | 1D DP | Medium |
| 52 | [Word Break](./dynamic-programming/word-break) | 1D DP | Medium |
| 53 | [Word Break II](./dynamic-programming/word-break-ii) | Backtracking + Memo (all paths) | Hard |
| 54 | [House Robber](./dynamic-programming/house-robber) | 1D DP / Take-or-Skip | Medium |
| 55 | [Jump Game](./dynamic-programming/jump-game) | Greedy/DP | Medium |
| 56 | [Unique Paths](./dynamic-programming/unique-paths) | 2D Grid DP | Medium |
| 57 | [Cherry Pickup II](./dynamic-programming/cherry-pickup-ii) | 2D DP | Hard |
| 58 | [Number of Ways to Paint N×3 Grid](./dynamic-programming/number-of-ways-paint-grid) | DP/Combinatorics | Hard |
| 59 | [Partition Equal Subset Sum](./dynamic-programming/partition-equal-subset-sum) | 0-1 Knapsack | Medium |
| 60 | [Longest Increasing Subsequence](./dynamic-programming/longest-increasing-subsequence) | DP / Patience Sorting | Medium |
| 61 | [Longest Increasing Subsequence II](./dynamic-programming/longest-increasing-subsequence-ii) | DP + Segment Tree (range-max) | Hard |
| 62 | [Russian Doll Envelopes](./dynamic-programming/russian-doll-envelopes) | 2-D LIS (sort + patience) | Hard |
| 63 | [Number of LIS](./dynamic-programming/number-of-lis) | Counting DP (reset/accumulate) | Medium |
| 64 | [Largest Divisible Subset](./dynamic-programming/largest-divisible-subset) | LIS / Poset Chain | Medium |
| 65 | [Burst Balloons](./dynamic-programming/burst-balloons) | Interval DP | Hard |
| 66 | [Subsets](./dynamic-programming/subsets) | Backtracking | Medium |
| 67 | [Permutations](./dynamic-programming/permutations) | Backtracking | Medium |
| 68 | [Combination Sum](./dynamic-programming/combination-sum) | Backtracking | Medium |
| 69 | [Combination Sum II](./dynamic-programming/combination-sum-ii) | Backtracking (dedupe) | Medium |
| 70 | [Combination Sum IV](./dynamic-programming/combination-sum-iv) | Counting DP (compositions) | Medium |
| 71 | [Coin Change II](./dynamic-programming/coin-change-ii) | Counting DP (partitions) | Medium |
| 72 | [Inverse Coin Change](./dynamic-programming/inverse-coin-change) | Inverse DP (gen. function) | Medium |
| 73 | [Word Search](./dynamic-programming/word-search) | Backtracking / 2D Grid | Medium |
| 74 | [Shortest Common Supersequence](./dynamic-programming/shortest-common-supersequence) | LCS / 2D DP | Hard |

### Trees
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 75 | [Populating Next Right Pointers II](./trees/populating-next-right-pointers-ii) | BFS | Medium |
| 76 | [Delete Nodes and Return Forest](./trees/delete-nodes-and-return-forest) | DFS | Medium |
| 77 | [LCA of Deepest Leaves](./trees/lca-deepest-leaves) | DFS | Medium |
| 78 | [All Possible Full Binary Trees](./trees/all-possible-full-binary-trees) | Recursion/Memo | Medium |
| 79 | [Binary Search Tree Iterator](./trees/binary-search-tree-iterator) | Stack/Design | Medium |
| 80 | [Lowest Common Ancestor](./trees/lowest-common-ancestor) | DFS | Medium |
| 81 | [Binary Tree Maximum Path Sum](./trees/binary-tree-max-path-sum) | DFS | Hard |
| 82 | [Validate BST](./trees/validate-bst) | DFS / Range Passing | Medium |

### Graphs
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 83 | [Clone Graph](./graphs/clone-graph) | BFS/DFS | Medium |
| 84 | [Find if Path Exists](./graphs/find-if-path-exists) | BFS/DFS/DSU | Easy |
| 85 | [Number of Islands](./graphs/number-of-islands) | BFS/DFS/DSU | Medium |
| 86 | [Rotting Oranges](./graphs/rotting-oranges) | Multi-Source BFS | Medium |
| 87 | [01 Matrix](./graphs/01-matrix) | Multi-Source BFS | Medium |
| 88 | [Shortest Path in Binary Matrix](./graphs/shortest-path-binary-matrix) | BFS | Medium |
| 89 | [Word Ladder](./graphs/word-ladder) | BFS/Implicit Graph | Hard |
| 90 | [Max Area of Island](./graphs/max-area-of-island) | DFS/BFS | Medium |
| 91 | [Pacific Atlantic Water Flow](./graphs/pacific-atlantic-water-flow) | Multi-Source BFS | Medium |
| 92 | [Surrounded Regions](./graphs/surrounded-regions) | BFS/Union-Find | Medium |
| 93 | [Number of Provinces](./graphs/number-of-provinces) | DFS/BFS/DSU | Medium |
| 94 | [Course Schedule](./graphs/course-schedule) | Topological Sort | Medium |
| 95 | [Course Schedule II](./graphs/course-schedule-ii) | Topological Sort | Medium |
| 96 | [Parallel Courses III](./graphs/parallel-courses-iii) | Topological Sort + DP (critical path) | Hard |
| 97 | [Find Eventual Safe States](./graphs/find-eventual-safe-states) | DFS/Cycle Detection | Medium |
| 98 | [Alien Dictionary](./graphs/alien-dictionary) | Topological Sort | Hard |
| 99 | [Is Graph Bipartite?](./graphs/is-graph-bipartite) | BFS/DFS | Medium |
| 100 | [Possible Bipartition](./graphs/possible-bipartition) | BFS/DFS/DSU | Medium |
| 101 | [Redundant Connection](./graphs/redundant-connection) | Union-Find | Medium |
| 102 | [Accounts Merge](./graphs/accounts-merge) | Union-Find | Medium |
| 103 | [Most Stones Removed](./graphs/most-stones-removed) | Union-Find | Medium |
| 104 | [Evaluate Division](./graphs/evaluate-division) | BFS/Weighted | Medium |
| 105 | [Network Delay Time](./graphs/network-delay-time) | Dijkstra's | Medium |
| 106 | [Path with Minimum Effort](./graphs/path-minimum-effort) | Dijkstra's/BST/DSU | Medium |
| 107 | [Cheapest Flights Within K Stops](./graphs/cheapest-flights-k-stops) | Bellman-Ford | Medium |
| 108 | [Swim in Rising Water](./graphs/swim-in-rising-water) | Dijkstra's/BST/DSU | Hard |
| 109 | [Min Cost to Connect All Points](./graphs/min-cost-connect-points) | MST/Prim's/Kruskal's | Medium |
| 110 | [Connecting Cities With Min Cost](./graphs/connecting-cities-min-cost) | MST/Kruskal's | Medium |
| 111 | [Critical Connections](./graphs/critical-connections) | Tarjan's Bridge | Hard |
| 112 | [Minimum Height Trees](./graphs/minimum-height-trees) | BFS/Topological | Medium |

### Design
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 113 | [LRU Cache](./design/lru-cache) | DLL + HashMap | Medium |
| 114 | [Exam Room](./design/exam-room) | Sorted Set | Medium |
| 115 | [Find Median from Data Stream](./design/find-median-data-stream) | Two Heaps | Hard |

### Bit Manipulation
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 116 | [Number of 1 Bits](./bit-manipulation/hamming-weight) | Bit Manipulation | Easy |
| 117 | [Reverse Bits](./bit-manipulation/reverse-bits) | Bit Manipulation | Easy |

### Binary Search (Advanced)
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 118 | [Lowest Common Ancestor (Binary Tree)](./trees/lowest-common-ancestor) | DFS | Medium |

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
> 122 problems · 16 guides · Complete Staff/Principal Engineer preparation resource

> Last updated: 2026-07-05 — added Word Break II (#140); paired with the Chinese Word Segmenter LLD + Text Segmentation Service HLD
