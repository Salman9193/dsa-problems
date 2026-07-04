# Find Duplicate Subtrees — Notes & Intuition

**LeetCode #652** | Trees / Hashing | Medium

---

## Problem

Given the root of a binary tree, return **one representative root** for every
subtree that appears **more than once**. Two subtrees are duplicates when they
have **identical structure and identical node values**.

```
        1
       / \
      2   3
     /   / \
    4   2   4
       /
      4
```
Here the leaf `4` appears three times, and the subtree `2 -> 4` appears twice —
so the answer is one root for each: `[4, subtree(2->4)]`.

---

## The Key Insight

Give every subtree a canonical **signature** — a string that uniquely encodes
its shape and values. Then two subtrees are duplicates **exactly when their
signatures match**. The problem collapses to: *find signatures that occur two
or more times.*

Two design choices make the signature correct:

1. **Postorder** (left, right, root). A node's signature is built from its
   children's signatures, so children must be serialized first.
2. **Null markers.** Include a sentinel (`#`) for null children — otherwise a
   left-only child and a right-only child could produce the same string and
   collide.

---

## Approach 1 — String Signatures

```java
Map<String, Integer> count = new HashMap<>();
List<TreeNode> result = new ArrayList<>();

String serialize(TreeNode node) {
    if (node == null) return "#";                 // null marker

    String left  = serialize(node.left);          // postorder: children first
    String right = serialize(node.right);
    String sig = node.val + "," + left + "," + right;

    int seen = count.getOrDefault(sig, 0) + 1;
    count.put(sig, seen);
    if (seen == 2) result.add(node);              // record once, when it first dups
    return sig;
}
```

- **Why `seen == 2` and not `>= 2`?** We want *one* representative root per
  duplicated shape. Recording at the moment the count first hits 2 adds it
  exactly once; later occurrences (3rd, 4th, ...) don't re-add.

This is `O(n^2)` because each signature can be `O(n)` long and we build one per
node.

---

## Approach 2 — Subtree IDs (Optimal, O(n))

The string blowup is the only inefficiency. Replace each **unique signature**
with a small integer **ID**, so a node's signature becomes
`(val, leftId, rightId)` — an `O(1)`-size key.

```java
int idCounter = 1;
Map<String,Integer> sigToId  = new HashMap<>();  // "val,leftId,rightId" -> id
Map<Integer,Integer> idCount = new HashMap<>();  // id -> count

int encode(TreeNode node) {
    if (node == null) return 0;                   // id 0 = null

    int leftId  = encode(node.left);
    int rightId = encode(node.right);

    String sig = node.val + "," + leftId + "," + rightId;   // O(1)-size
    int id = sigToId.computeIfAbsent(sig, k -> idCounter++);

    int seen = idCount.getOrDefault(id, 0) + 1;
    idCount.put(id, seen);
    if (seen == 2) result.add(node);
    return id;
}
```

Now every key is constant size, so the whole pass is `O(n)` time and space.

---

## Full Trace (String Version)

```
Tree:        1
            / \
           2   3
          /   / \
         4   2   4
            /
           4

postorder, signature = "val,leftSig,rightSig":

  4 (under left 2)    -> "4,#,#"      count=1
  2 (left)            -> "2,4,#,#,#"  count=1
  4 (under inner 2)   -> "4,#,#"      count=2  => add this 4     ← dup leaf
  2 (inner, under 3)  -> "2,4,#,#,#"  count=2  => add this 2->4  ← dup subtree
  4 (right of 3)      -> "4,#,#"      count=3  (already recorded)
  3                   -> unique
  1                   -> unique

result = [ subtree(4), subtree(2->4) ]
```

---

## Why Null Markers Are Non-Negotiable

Without a `#` for nulls, these two different subtrees serialize identically:

```
   2          2
  /            \
 4              4
```
Both would become `"2,4"` — a false duplicate. With markers they become
`"2,4,#,#,#"` vs `"2,#,4,#,#"`, which correctly differ.

---

## Edge Cases

| Input | Output |
|-------|--------|
| `null` (empty tree) | `[]` |
| Single node | `[]` (nothing repeats) |
| All nodes identical value, same shape twice | One representative root |
| Duplicate appears 3+ times | Recorded once (guard on `seen == 2`) |
| Duplicates at different depths | Each shape counted independently |

---

## Extensions

| Variant | Change | Approach |
|---------|--------|---------|
| **Serialize/Deserialize** (#297) | Round-trip a whole tree | Same postorder + null markers, but rebuild from the string |
| **Subtree of Another Tree** (#572) | Does t occur in s? | Serialize both, substring/KMP match, or per-node compare |
| **Most Frequent Subtree Sum** (#508) | Group by sum not shape | Postorder returning subtree sums into a HashMap |
| Count total duplicates (not just distinct) | Sum extra occurrences | Sum `(count - 1)` over signatures with count > 1 |
| **Merkle-tree dedup** | Content-address subtrees | Hash the signature (SHA) instead of storing full strings |
| Return **all** roots of each dup (not one) | Every occurrence | Drop the `seen == 2` guard; collect every node per signature |

**Trade-off:** string signatures are trivial to write under interview pressure
but `O(n^2)`; the subtree-ID version is `O(n)` and is what production
deduplication (Merkle-style content addressing) actually uses. Mention the ID
optimization proactively — it's the "can you do better than O(n^2)?" follow-up.
