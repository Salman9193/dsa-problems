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

### Strings
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 23 | [Valid Palindrome](./strings/valid-palindrome) | Two Pointers | Easy |
| 24 | [Longest Palindromic Substring](./strings/longest-palindromic-substring) | DP/Expand | Medium |
| 25 | [Longest Substring Without Repeating](./strings/longest-substring-without-repeating) | Sliding Window | Medium |
| 26 | [Longest Repeating Character Replacement](./strings/longest-repeating-character-replacement) | Sliding Window | Medium |
| 27 | [Minimum Window Substring](./strings/minimum-window-substring) | Sliding Window | Hard |
| 28 | [Find All Anagrams](./strings/find-all-anagrams) | Sliding Window | Medium |
| 29 | [First Unique Character](./strings/first-unique-character) | HashMap | Easy |
| 30 | [Encode and Decode Strings](./strings/encode-and-decode-strings) | Design | Medium |
| 31 | [Decode String](./strings/decode-string) | Stack | Medium |
| 32 | [Minimum Remove to Make Valid Parentheses](./strings/minimum-remove-valid-parentheses) | Stack | Medium |
| 33 | [Implement Trie](./strings/implement-trie) | Trie | Medium |

### Linked List
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 34 | [Reverse Linked List](./linked-list/reverse-linked-list) | Linked List | Easy |
| 35 | [Linked List Cycle](./linked-list/linked-list-cycle) | Fast/Slow Pointers | Easy |
| 36 | [Linked List Random Node](./linked-list/linked-list-random-node) | Reservoir Sampling | Medium |

### Binary Search
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 37 | [Search in Rotated Sorted Array](./binary-search/search-in-rotated-sorted-array) | Binary Search | Medium |
| 38 | [Find in Mountain Array](./binary-search/find-in-mountain-array) | Binary Search | Hard |
| 39 | [Sqrt(x)](./binary-search/sqrt-x) | Binary Search | Easy |
| 40 | [Online Election](./binary-search/online-election) | Binary Search/Design | Medium |

### Stacks
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 41 | [Valid Parentheses](./stacks/valid-parentheses) | Stack | Easy |
| 42 | [Daily Temperatures](./stacks/daily-temperatures) | Monotonic Stack | Medium |
| 43 | [Largest Rectangle in Histogram](./stacks/largest-rectangle-histogram) | Monotonic Stack | Hard |

### Dynamic Programming
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 44 | [Climbing Stairs](./dynamic-programming/climbing-stairs) | 1D DP | Easy |
| 45 | [Coin Change](./dynamic-programming/coin-change) | 1D DP | Medium |
| 46 | [Word Break](./dynamic-programming/word-break) | 1D DP | Medium |
| 47 | [House Robber](./dynamic-programming/house-robber) | 1D DP / Take-or-Skip | Medium |
| 48 | [Jump Game](./dynamic-programming/jump-game) | Greedy/DP | Medium |
| 49 | [Unique Paths](./dynamic-programming/unique-paths) | 2D Grid DP | Medium |
| 50 | [Cherry Pickup II](./dynamic-programming/cherry-pickup-ii) | 2D DP | Hard |
| 51 | [Number of Ways to Paint N×3 Grid](./dynamic-programming/number-of-ways-paint-grid) | DP/Combinatorics | Hard |
| 52 | [Partition Equal Subset Sum](./dynamic-programming/partition-equal-subset-sum) | 0-1 Knapsack | Medium |
| 53 | [Longest Increasing Subsequence](./dynamic-programming/longest-increasing-subsequence) | DP / Patience Sorting | Medium |
| 54 | [Burst Balloons](./dynamic-programming/burst-balloons) | Interval DP | Hard |
| 55 | [Subsets](./dynamic-programming/subsets) | Backtracking | Medium |
| 56 | [Permutations](./dynamic-programming/permutations) | Backtracking | Medium |
| 57 | [Combination Sum](./dynamic-programming/combination-sum) | Backtracking | Medium |
| 58 | [Word Search](./dynamic-programming/word-search) | Backtracking / 2D Grid | Medium |

### Trees
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 59 | [Populating Next Right Pointers II](./trees/populating-next-right-pointers-ii) | BFS | Medium |
| 60 | [Delete Nodes and Return Forest](./trees/delete-nodes-and-return-forest) | DFS | Medium |
| 61 | [LCA of Deepest Leaves](./trees/lca-deepest-leaves) | DFS | Medium |
| 62 | [All Possible Full Binary Trees](./trees/all-possible-full-binary-trees) | Recursion/Memo | Medium |
| 63 | [Binary Search Tree Iterator](./trees/binary-search-tree-iterator) | Stack/Design | Medium |
| 64 | [Lowest Common Ancestor](./trees/lowest-common-ancestor) | DFS | Medium |
| 65 | [Binary Tree Maximum Path Sum](./trees/binary-tree-max-path-sum) | DFS | Hard |
| 66 | [Validate BST](./trees/validate-bst) | DFS / Range Passing | Medium |

### Graphs
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 67 | [Clone Graph](./graphs/clone-graph) | BFS/DFS | Medium |
| 68 | [Find if Path Exists](./graphs/find-if-path-exists) | BFS/DFS/DSU | Easy |
| 69 | [Number of Islands](./graphs/number-of-islands) | BFS/DFS/DSU | Medium |
| 70 | [Rotting Oranges](./graphs/rotting-oranges) | Multi-Source BFS | Medium |
| 71 | [01 Matrix](./graphs/01-matrix) | Multi-Source BFS | Medium |
| 72 | [Shortest Path in Binary Matrix](./graphs/shortest-path-binary-matrix) | BFS | Medium |
| 73 | [Word Ladder](./graphs/word-ladder) | BFS/Implicit Graph | Hard |
| 74 | [Max Area of Island](./graphs/max-area-of-island) | DFS/BFS | Medium |
| 75 | [Pacific Atlantic Water Flow](./graphs/pacific-atlantic-water-flow) | Multi-Source BFS | Medium |
| 76 | [Surrounded Regions](./graphs/surrounded-regions) | BFS/Union-Find | Medium |
| 77 | [Number of Provinces](./graphs/number-of-provinces) | DFS/BFS/DSU | Medium |
| 78 | [Course Schedule](./graphs/course-schedule) | Topological Sort | Medium |
| 79 | [Course Schedule II](./graphs/course-schedule-ii) | Topological Sort | Medium |
| 80 | [Find Eventual Safe States](./graphs/find-eventual-safe-states) | DFS/Cycle Detection | Medium |
| 81 | [Alien Dictionary](./graphs/alien-dictionary) | Topological Sort | Hard |
| 82 | [Is Graph Bipartite?](./graphs/is-graph-bipartite) | BFS/DFS | Medium |
| 83 | [Possible Bipartition](./graphs/possible-bipartition) | BFS/DFS/DSU | Medium |
| 84 | [Redundant Connection](./graphs/redundant-connection) | Union-Find | Medium |
| 85 | [Accounts Merge](./graphs/accounts-merge) | Union-Find | Medium |
| 86 | [Most Stones Removed](./graphs/most-stones-removed) | Union-Find | Medium |
| 87 | [Evaluate Division](./graphs/evaluate-division) | BFS/Weighted | Medium |
| 88 | [Network Delay Time](./graphs/network-delay-time) | Dijkstra's | Medium |
| 89 | [Path with Minimum Effort](./graphs/path-minimum-effort) | Dijkstra's/BST/DSU | Medium |
| 90 | [Cheapest Flights Within K Stops](./graphs/cheapest-flights-k-stops) | Bellman-Ford | Medium |
| 91 | [Swim in Rising Water](./graphs/swim-in-rising-water) | Dijkstra's/BST/DSU | Hard |
| 92 | [Min Cost to Connect All Points](./graphs/min-cost-connect-points) | MST/Prim's/Kruskal's | Medium |
| 93 | [Connecting Cities With Min Cost](./graphs/connecting-cities-min-cost) | MST/Kruskal's | Medium |
| 94 | [Critical Connections](./graphs/critical-connections) | Tarjan's Bridge | Hard |
| 95 | [Minimum Height Trees](./graphs/minimum-height-trees) | BFS/Topological | Medium |

### Design
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 96 | [LRU Cache](./design/lru-cache) | DLL + HashMap | Medium |
| 97 | [Exam Room](./design/exam-room) | Sorted Set | Medium |
| 98 | [Find Median from Data Stream](./design/find-median-data-stream) | Two Heaps | Hard |

### Bit Manipulation
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 99 | [Number of 1 Bits](./bit-manipulation/hamming-weight) | Bit Manipulation | Easy |

### Binary Search (Advanced)
| # | Problem | Topic | Difficulty |
|---|---------|-------|------------|
| 100 | [Lowest Common Ancestor (Binary Tree)](./trees/lowest-common-ancestor) | DFS | Medium |

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
> 100 problems · 16 guides · Complete Staff/Principal Engineer preparation resource

> Last updated: 2026-07-04 — added 4 staff-level problems + segment tree guide
