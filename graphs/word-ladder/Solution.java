import java.util.*;

class Solution {

    // Approach 1: Single-Source BFS — O(n × L × 26) time, O(n × L) space
    //
    // Key insight: model as an implicit graph.
    //   Nodes  = words in wordList + beginWord
    //   Edges  = pairs of words differing by exactly one letter
    //   Goal   = shortest path from beginWord to endWord
    //
    // We never build the graph explicitly — generate neighbours on the fly
    // by trying all 26 letter substitutions at each position.
    // This gives O(L × 26) per word instead of O(n × L) naive comparison.
    //
    // Use wordSet both as the dictionary lookup AND as the visited set:
    // removing a word after visiting it prevents revisiting and avoids a
    // separate HashSet<String> — correct because BFS finds the shortest path
    // to each word on first visit; no shorter path can exist later.
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        wordSet.remove(beginWord); // mark beginWord as visited

        int steps = 1;

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // process one BFS level per step

            for (int i = 0; i < levelSize; i++) {
                String word = queue.poll();
                char[] chars = word.toCharArray();

                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];

                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == original) continue;
                        chars[j] = c;
                        String next = new String(chars);

                        if (next.equals(endWord)) return steps + 1;

                        if (wordSet.contains(next)) {
                            wordSet.remove(next); // mark visited on enqueue
                            queue.offer(next);
                        }
                    }

                    chars[j] = original; // restore before trying next position
                }
            }

            steps++;
        }

        return 0; // endWord unreachable
    }

    // Approach 2: Bidirectional BFS — O(n × L × 26 / 2) time in practice
    //
    // Expand from BOTH beginWord and endWord simultaneously.
    // Always expand the SMALLER frontier — this minimises total nodes explored.
    //
    // Why bidirectional?
    //   Standard BFS explores a sphere of radius r ≈ steps/2 from the source.
    //   Bidirectional BFS: two spheres of radius r/2 meeting in the middle.
    //   Area reduction: π(r/2)² × 2 = πr²/2 — 2× fewer nodes in 2D grids.
    //   For sparse word graphs the saving can be much larger.
    //
    // Always swap to expand the smaller set: this ensures the smaller frontier
    // is expanded first at each step, minimising the total work per level.
    public int ladderLengthBidirectional(String beginWord, String endWord,
                                          List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Set<String> beginSet = new HashSet<>(List.of(beginWord));
        Set<String> endSet   = new HashSet<>(List.of(endWord));
        int steps = 1;

        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // Always expand the smaller frontier
            if (beginSet.size() > endSet.size()) {
                Set<String> tmp = beginSet; beginSet = endSet; endSet = tmp;
            }

            Set<String> nextSet = new HashSet<>();
            for (String word : beginSet) {
                char[] chars = word.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[j] = c;
                        String next = new String(chars);
                        if (endSet.contains(next)) return steps + 1; // frontiers met
                        if (wordSet.contains(next)) {
                            nextSet.add(next);
                            wordSet.remove(next);
                        }
                    }
                    chars[j] = original;
                }
            }

            beginSet = nextSet;
            steps++;
        }

        return 0;
    }
}

/*
 * Complexity
 * ----------
 * Standard BFS:      Time O(n × L × 26), Space O(n × L)
 * Bidirectional BFS: Time O(n × L × 26 / branching_factor), Space O(n × L)
 *   where n = dictionary size, L = word length
 *
 * Why O(L × 26) per word for neighbour generation?
 *   For each of L positions, try 26 letters → at most 26L candidate words.
 *   Each candidate: HashSet lookup O(L) for string hashing.
 *   Total per word: O(L² × 26) — but in practice L is small (≤ 10).
 *   This beats the naive O(n × L) comparison against all dictionary words.
 *
 * Why remove from wordSet instead of a separate visited set?
 *   BFS guarantees first-visit = shortest path. Once a word is reached,
 *   no shorter path to it can exist. Removing it from wordSet prevents
 *   re-enqueueing and avoids allocating a separate visited HashSet.
 *
 * Why mark visited on enqueue (not dequeue)?
 *   The same word can be generated by multiple current-level words.
 *   Marking on enqueue ensures it enters the queue exactly once.
 *   Without it, the same word would be enqueued k times (k = its BFS in-degree).
 *
 * Trace — beginWord="hit", endWord="cog", wordList=["hot","dot","dog","lot","log","cog"]
 * --------------------------------------------------------------------------------------
 * wordSet = {hot,dot,dog,lot,log,cog}
 *
 * Step 1: queue=["hit"]
 *   hit → try all positions:
 *     h_t: aat..zzt — "hot" in wordSet → enqueue, remove. others not in set.
 *     hi_: "hia".."hiz" — none in wordSet
 *     _it: "ait".."zit" — none in wordSet
 *   queue=["hot"], steps=2
 *
 * Step 2: queue=["hot"]
 *   hot → "dot" ✓, "lot" ✓ → enqueue both
 *   queue=["dot","lot"], steps=3
 *
 * Step 3: queue=["dot","lot"]
 *   dot → "dog" ✓ → enqueue
 *   lot → "log" ✓ → enqueue
 *   queue=["dog","log"], steps=4
 *
 * Step 4: queue=["dog","log"]
 *   dog → "cog" == endWord → return 4+1 = 5 ✓
 */
