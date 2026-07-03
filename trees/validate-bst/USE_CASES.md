# Validate BST — Real-World Use Cases

## 1. Database Index Integrity Checking

B-trees and B+-trees (used as database indexes in PostgreSQL, MySQL) must
maintain BST ordering invariants after operations. Validate BST is the core
check in database consistency verification tools.

## 2. Compiler Symbol Table Validation

Symbol tables implemented as BSTs must maintain ordering for O(log n) lookup.
After deserialization or import, BST validation confirms the structure is intact.

## 3. Interval Tree Validation

Interval trees (used in range query systems) build on BST ordering of interval
starts. Validating an interval tree's BST property is the same algorithm.
