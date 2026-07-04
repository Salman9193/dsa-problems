# Serialize and Deserialize Binary Tree — Notes & Intuition

**LeetCode #297** | Trees | Hard

---

## Problem

Design an algorithm to convert a binary tree into a string (**serialize**),
and convert that string back into the identical tree (**deserialize**). You
have complete freedom over the string format — the only requirement is that
your `deserialize` can rebuild exactly what your `serialize` produced.

Google likes this problem because it tests **both** algorithmic thinking
(tree traversal, reconstruction) **and** API-design instincts (choosing a
format, handling the round-trip contract).

---

## The Key Insight

A single traversal is normally ambiguous — preorder `[1,2,3]` could describe
many different trees. The ambiguity comes from **not knowing where each
subtree ends**. Fix it by recording a marker for every `null` child:

```
preorder WITHOUT nulls:  1 2 3        ← ambiguous
preorder WITH nulls:     1 2 # # 3 # #  ← unique
```

Once nulls are explicit, preorder alone reconstructs the tree uniquely, with
no need for a second (inorder) traversal.

---

## Serialize — Preorder DFS

```java
void serializeHelper(TreeNode node, StringBuilder sb) {
    if (node == null) { sb.append("#,"); return; }  // record null, stop
    sb.append(node.val).append(",");                // root
    serializeHelper(node.left, sb);                 // left
    serializeHelper(node.right, sb);                // right
}
```

Order matters: **root, then left, then right**. Deserialize must consume
tokens in the exact same order.

---

## Deserialize — Consume Tokens in Preorder

```java
TreeNode deserializeHelper(Queue<String> tokens) {
    String token = tokens.poll();
    if (token.equals("#")) return null;             // null leaf edge

    TreeNode node = new TreeNode(Integer.parseInt(token));
    node.left  = deserializeHelper(tokens);         // build left FIRST
    node.right = deserializeHelper(tokens);         // then right
    return node;
}
```

A `Queue` is the trick: it hands back tokens left-to-right in the same
preorder they were written. Each recursive call consumes exactly one
complete subtree before returning to its parent.

---

## Why the Queue Works

The recursion mirrors the structure. When `deserializeHelper` reads `"1"`,
it knows the *next* tokens are its entire left subtree, followed by its
entire right subtree. The `poll()` calls naturally walk the flattened
preorder in the right order — the tree's shape is encoded purely in the
sequence of values and `#` markers.

---

## Full Trace

```
Tree:        1
            / \
           2   3
              / \
             4   5

serialize -> "1,2,#,#,3,4,#,#,5,#,#,"

deserialize:
  poll "1" -> node(1)
    left  = deserialize:
      poll "2" -> node(2)
        left  = poll "#" -> null
        right = poll "#" -> null      => leaf 2
    right = deserialize:
      poll "3" -> node(3)
        left  = poll "4" -> node(4), "#"->null, "#"->null   => leaf 4
        right = poll "5" -> node(5), "#"->null, "#"->null   => leaf 5
  => tree rebuilt exactly ✓
```

---

## DFS Preorder vs BFS Level-Order

| | Preorder DFS | BFS Level-Order |
|--|-------------|-----------------|
| Format | `1,2,#,#,3,4,#,#,5,#,#` | `1,2,3,#,#,4,5` (LeetCode display style) |
| Code length | Shorter, recursive | Longer, explicit queue of parents |
| Space | O(h) call stack | O(w) width of tree |
| Interview | Easiest to write correctly | Matches LeetCode's shown format |

Both are O(n) time and O(n) space. Preorder DFS is the safer pick under
time pressure; mention BFS as the alternative that matches the display format.

---

## Edge Cases

| Input | Serialized | Notes |
|-------|-----------|-------|
| `null` (empty tree) | `"#,"` | Deserialize returns null immediately |
| Single node | `"1,#,#,"` | Root with two null children |
| Left-skewed chain | `"1,2,3,#,#,#,#,"` | Every right child is null |
| Negative / large values | `"-5,#,#,"` | `Integer.parseInt` handles them |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| Serialize a **BST** (#449) | Values are ordered | Can drop null markers — preorder + BST bounds suffice, shorter output |
| Serialize an **N-ary tree** (#428) | Variable children | Record child-count per node, or use a sentinel after each child list |
| Serialize a **graph** (#297 graph variant) | Cycles + shared nodes | BFS with node IDs + adjacency list; visited set breaks cycles |
| Encode to the smallest string | Compression goal | Bit-pack structure; use LEB128 for values |
| Streaming deserialize | Data arrives in chunks | Push-based parser consuming tokens as they arrive |
| Serialize with parent pointers | Extra back-edges | Serialize children only; rebuild parent links on the deserialize pass |

**Trade-off:** DFS preorder uses O(h) stack — fine for balanced trees, but a
skewed tree of 10⁵ nodes risks stack overflow. For adversarial inputs, an
explicit stack or BFS level-order avoids the recursion-depth limit.
