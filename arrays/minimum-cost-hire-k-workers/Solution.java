import java.util.*;

class Solution {

    // Approach: Sort by wage/quality ratio + Max-Heap — O(n log n) time, O(n) space
    //
    // Key insight: For a valid group of k workers, the worker with the highest
    // wage/quality ratio sets the payment rate for the entire group.
    // Call this worker the "captain."
    //
    // Proof:
    //   All workers are paid at rate r (wage per unit quality).
    //   Worker i's constraint: r × quality[i] >= wage[i]  →  r >= wage[i]/quality[i]
    //   So r = max(wage[i]/quality[i]) across all hired workers.
    //   The worker with the highest ratio IS the captain — they set r.
    //
    // Strategy: iterate over each worker as the potential captain (sorted by ratio).
    // For each captain, the optimal co-workers are the k-1 with LOWEST quality
    // (minimum total quality = minimum total pay = r × sum_of_qualities).
    //
    // Use a max-heap of size k to maintain the k smallest qualities seen so far.
    // When heap exceeds k, evict the largest quality.
    public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
        int n = quality.length;

        // Build (ratio, quality) pairs and sort by ratio ascending
        double[][] workers = new double[n][2];
        for (int i = 0; i < n; i++)
            workers[i] = new double[]{(double) wage[i] / quality[i], (double) quality[i]};
        Arrays.sort(workers, (a, b) -> Double.compare(a[0], b[0]));

        // Max-heap: tracks k smallest qualities seen so far
        PriorityQueue<Double> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        double qualitySum = 0;
        double minCost = Double.MAX_VALUE;

        for (double[] worker : workers) {
            double ratio = worker[0], qual = worker[1];

            // Add current worker to the pool
            maxHeap.offer(qual);
            qualitySum += qual;

            // Maintain only k smallest qualities
            if (maxHeap.size() > k) {
                qualitySum -= maxHeap.poll();  // remove the largest quality (greedy)
            }

            // Current worker is the captain (highest ratio in group)
            // Total pay = rate × sum_of_qualities
            if (maxHeap.size() == k) {
                minCost = Math.min(minCost, ratio * qualitySum);
            }
        }

        return minCost;
    }
}

/*
 * Complexity
 * ----------
 * Time:  O(n log n) — sorting + O(n log k) heap operations
 * Space: O(n) for sorted array + O(k) for max-heap
 *
 * Why max-heap of size k for MINIMUM quality sum?
 *   We want the k SMALLEST qualities. A max-heap of size k:
 *   - Contains k elements at all times (once full)
 *   - When a new element arrives: add it, then remove the MAX
 *   - Result: heap always holds the k smallest elements seen so far
 *   This is the standard "k smallest elements" trick.
 *
 * Why is it safe to evict the largest quality?
 *   We've already seen all workers with lower or equal ratio than the captain.
 *   Those workers' wage constraints are satisfied because:
 *   r = captain's ratio >= their individual ratio = wage/quality
 *   So any subset of previously-seen workers can legally join the group.
 *   Among them, we greedily keep the k with smallest quality.
 *
 * Correctness argument:
 *   Any optimal group of k workers has exactly one "captain" (max ratio worker).
 *   When we process that captain in sorted order, all other group members
 *   have already been seen (lower ratio). The heap contains the k smallest
 *   qualities from all workers seen so far — the optimal k-1 co-workers
 *   for this captain are among them. So the optimal is always evaluated.
 *
 * Trace — quality=[10,20,5], wage=[70,50,30], k=2
 * -------------------------------------------------
 * Ratios: w0=7.0, w1=2.5, w2=6.0
 * Sorted: [(2.5, 20), (6.0, 5), (7.0, 10)]
 *
 * Process (2.5, 20): heap=[20], sum=20, size=1 < k
 * Process (6.0, 5):  heap=[20,5], sum=25, size=2 == k
 *   cost = 6.0 × 25 = 150
 * Process (7.0, 10): heap=[20,5,10], sum=35, size=3 > k
 *   remove max(20): heap=[10,5], sum=15
 *   cost = 7.0 × 15 = 105
 *   minCost = min(150, 105) = 105
 *
 * return 105.0 ✓
 * (pay worker0=70, worker2=35; ratio=7.0, total=105)
 */
