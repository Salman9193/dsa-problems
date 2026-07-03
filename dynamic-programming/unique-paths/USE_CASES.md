# Unique Paths — Real-World Use Cases

Grid path counting appears in robotics, image processing, and combinatorics.

## 1. Robotics — Grid Navigation

A robot on a warehouse grid moving only right/down: how many valid
routes exist from depot to destination? Used in automated warehouse
routing (Amazon Robotics, Ocado) to count and distribute load across routes.

## 2. Combinatorics — Lattice Path Counting

The answer C(m+n-2, m-1) counts lattice paths — a fundamental result
in combinatorics used in probability theory (random walk analysis) and
computational biology (sequence alignment scoring matrices).

## 3. Dynamic Programming Foundation

Unique Paths is the entry point for 2D grid DP. Every grid DP problem
(minimum path sum, obstacle avoidance, falling path) uses the same
top-left → bottom-right recurrence structure.
