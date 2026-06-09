import java.util.*;

class Solution {

    // Approach 1: Min-Heap — O(n log k) time, O(n + k) space
    //
    // Maintain a min-heap of size k keyed by frequency.
    // For each unique element, push to heap — if size exceeds k, pop the minimum.
    // At the end the heap contains exactly the k most frequent elements.
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        // min-heap: root = least frequent among current top-k
        PriorityQueue<Integer> heap = new PriorityQueue<>(
            (a, b) -> freq.get(a) - freq.get(b)
        );

        for (int num : freq.keySet()) {
            heap.offer(num);
            if (heap.size() > k) heap.poll();  // evict least frequent
        }

        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) result[i] = heap.poll();
        return result;
    }

    // Approach 2: Bucket Sort — O(n) time, O(n) space  ← OPTIMAL
    //
    // Key insight: frequency of any element is bounded by n (array length).
    // Use bucket[f] = list of elements with frequency f.
    // Scan buckets from high to low frequency, collect top k.
    //
    // Why this beats the heap for large n:
    //   Heap: O(n log k) — log factor from heap operations
    //   Bucket: O(n)     — pure array indexing, no comparisons
    public int[] topKFrequentBucket(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        // bucket[i] holds all numbers with frequency i
        @SuppressWarnings("unchecked")
        List<Integer>[] bucket = new List[nums.length + 1];

        for (int num : freq.keySet()) {
            int f = freq.get(num);
            if (bucket[f] == null) bucket[f] = new ArrayList<>();
            bucket[f].add(num);
        }

        int[] result = new int[k];
        int idx = 0;
        for (int f = bucket.length - 1; f >= 0 && idx < k; f--) {
            if (bucket[f] != null) {
                for (int num : bucket[f]) {
                    result[idx++] = num;
                    if (idx == k) return result;
                }
            }
        }
        return result;
    }
}

/*
 * Complexity
 * ----------
 * Sort by freq:  Time O(n log n), Space O(n)   — baseline
 * Min-heap:      Time O(n log k), Space O(n+k) — good when k << n
 * Bucket sort:   Time O(n),       Space O(n)   — optimal
 *
 * Why bucket sort is O(n):
 *   Frequencies are in range [1, n] — bounded, known in advance.
 *   Array indexing is O(1) vs O(log k) for heap push/pop.
 *   Same principle as counting sort beating comparison sort
 *   for bounded integer ranges.
 *
 * Trace — nums=[1,1,1,2,2,3], k=2
 * ----------------------------------
 * freq = {1:3, 2:2, 3:1}
 *
 * bucket[1] = [3]
 * bucket[2] = [2]
 * bucket[3] = [1]
 *
 * Scan f=6→1:
 *   f=3: bucket[3]=[1] → result=[1], idx=1
 *   f=2: bucket[2]=[2] → result=[1,2], idx=2 → return ✓
 */
