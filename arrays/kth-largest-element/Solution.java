import java.util.*;

class Solution {

    // Kth Largest Element — two canonical approaches:
    //   Min-heap of size k: O(n log k)
    //   Quickselect:        O(n) average, O(n²) worst case
    //
    // When to use which:
    //   Streaming data (unknown n): min-heap (can't quickselect without full array)
    //   Static array, best average: quickselect
    //   Guaranteed O(n log k): min-heap

    // ── Approach 1: Min-Heap of Size k — O(n log k) ──────────────────────────
    // Maintain a min-heap of the k largest elements seen so far.
    // Heap root = smallest of the k largest = kth largest overall.
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min-heap

        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k)
                minHeap.poll(); // remove smallest — keep only k largest
        }

        return minHeap.peek(); // root = kth largest
    }

    // ── Approach 2: Quickselect — O(n) average ────────────────────────────────
    // Partition around a pivot. If pivot lands at index (n-k), that's the answer.
    // Otherwise recurse into the correct half.
    public int findKthLargestQuickSelect(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    private int quickSelect(int[] nums, int lo, int hi, int target) {
        if (lo == hi) return nums[lo];

        int pivot = nums[hi]; // choose last element as pivot
        int p = lo;
        for (int i = lo; i < hi; i++)
            if (nums[i] <= pivot) swap(nums, p++, i);
        swap(nums, p, hi); // place pivot

        if (p == target) return nums[p];
        if (p < target)  return quickSelect(nums, p + 1, hi, target);
        else             return quickSelect(nums, lo, p - 1, target);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
    }
}

/*
 * Complexity
 * ----------
 * Min-heap: Time O(n log k), Space O(k)
 * Quickselect: Time O(n) avg / O(n²) worst, Space O(1)
 *
 * Min-heap approach is preferred for:
 *   - Streaming data (process elements one at a time)
 *   - Guaranteed O(n log k) bound
 *   - Small k (very efficient when k << n)
 *
 * Quickselect is preferred for:
 *   - Static array, best average-case performance
 *   - k close to n/2 (heap would be large)
 *
 * Randomised quickselect: shuffle array first → O(n) expected with high probability,
 * eliminates worst-case O(n²) from adversarial inputs.
 */
