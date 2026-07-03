# Word Search — Real-World Use Cases

## 1. Boggle Solver

The classic Boggle word game (find all valid words in a 4×4 letter grid)
is Word Search II (#212). Each cell connects to all 8 neighbours (not 4).
Production Boggle solvers combine backtracking with a Trie to prune invalid
prefixes — the combination gives practical O(m×n×8^L) with heavy pruning.

## 2. DNA Sequence Matching in Genomic Grids

2D grid pattern matching is used in spatial genomics (spatial transcriptomics)
where gene expression patterns are searched in spatial grids of tissue samples.
Backtracking DFS finds connected regions matching a target expression sequence.

## 3. Maze Solving and Pathfinding

Grid backtracking is the foundation of maze solving algorithms. Word Search
generalises to arbitrary constraint-based path finding: find a path through
a grid matching a sequence of properties, without revisiting cells.
