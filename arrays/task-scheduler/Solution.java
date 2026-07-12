import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

class Solution {

    // Task Scheduler — LeetCode #621
    //
    // Approach 1: greedy max-heap simulation.
    // At each cycle, run the most frequent available tasks first (the highest-count task is
    // the bottleneck, so start it as early and often as possible). Pop up to n+1 distinct
    // tasks per cycle, decrement them, push back any that remain.
    //
    // Time: O(T log 26) ~ O(T).  Space: O(26).
    public int leastIntervalHeap(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char c : tasks) freq[c - 'A']++;

        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);  // max-heap
        for (int f : freq) if (f > 0) heap.offer(f);

        int time = 0;
        while (!heap.isEmpty()) {
            List<Integer> cycle = new ArrayList<>();
            for (int i = 0; i <= n && !heap.isEmpty(); i++) {   // one cooldown cycle: n+1 slots
                cycle.add(heap.poll() - 1);                     // run it once
            }
            for (int remaining : cycle) {
                if (remaining > 0) heap.offer(remaining);
            }
            // a full cycle costs n+1; the final cycle costs only what we actually ran
            time += heap.isEmpty() ? cycle.size() : n + 1;
        }
        return time;
    }

    // Approach 2: the greedy math formula — no simulation needed.
    //
    // The most frequent task lays out a skeleton: (maxFreq - 1) frames of width (n + 1),
    // followed by a tail containing every task tied for the max frequency. All other tasks
    // fill the idle gaps inside those frames.
    //
    //     answer = max( tasks.length,  (maxFreq - 1) * (n + 1) + countOfMaxFreq )
    //
    // The max(...) handles the case where there are so many distinct tasks that they
    // overflow the gaps — then there are no idles at all and the answer is just the count.
    //
    // Time: O(T).  Space: O(1).
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char c : tasks) freq[c - 'A']++;

        int maxFreq = 0, countMax = 0;
        for (int f : freq) {
            if (f > maxFreq) {
                maxFreq = f;
                countMax = 1;
            } else if (f == maxFreq && f > 0) {
                countMax++;
            }
        }
        return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + countMax);
    }
}

/*
 * Trace — tasks = [A,A,A,B,B,B], n = 2
 * ------------------------------------
 *   freq: A=3, B=3  ->  maxFreq = 3, countMax = 2
 *   (3 - 1) * (2 + 1) + 2 = 6 + 2 = 8
 *   max(6, 8) = 8      ->  A B idle | A B idle | A B     correct
 *
 * Why the max(...) matters — same tasks with n = 0:
 *   (3 - 1) * 1 + 2 = 4,  but tasks.length = 6  ->  answer 6 (no cooldown, no idles).
 *
 * Both approaches are greedy: always advance the bottleneck (most frequent) task. The heap
 * version is what a real scheduler's ready-queue does; the formula skips the simulation by
 * reasoning about the idle-slot skeleton directly.
 */
