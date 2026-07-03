import java.util.*;

class MedianFinder {

    // Find Median from Data Stream — the canonical two-heap design problem.
    //
    // Key insight: maintain two heaps:
    //   lo: max-heap of the smaller half
    //   hi: min-heap of the larger half
    //
    // Invariant: lo.size() == hi.size() OR lo.size() == hi.size() + 1
    // Median: lo.size() == hi.size() → average of tops
    //         lo.size() == hi.size()+1 → lo.peek()
    //
    // This gives O(log n) add, O(1) findMedian.

    private PriorityQueue<Integer> lo; // max-heap (smaller half)
    private PriorityQueue<Integer> hi; // min-heap (larger half)

    public MedianFinder() {
        lo = new PriorityQueue<>(Collections.reverseOrder()); // max-heap
        hi = new PriorityQueue<>();                            // min-heap
    }

    public void addNum(int num) {
        lo.offer(num);               // always add to lo first
        hi.offer(lo.poll());         // balance: move lo's max to hi

        // Re-balance sizes: lo should have >= elements than hi
        if (lo.size() < hi.size())
            lo.offer(hi.poll());
    }

    public double findMedian() {
        if (lo.size() > hi.size())
            return lo.peek();
        return (lo.peek() + hi.peek()) / 2.0;
    }
}

/*
 * Complexity: addNum O(log n), findMedian O(1)
 * Space: O(n) — both heaps together store all elements
 *
 * Why add to lo first, then move lo's max to hi?
 *   Even if num > hi.peek(), adding to lo then moving ensures hi always
 *   contains elements >= lo's elements. The "route through lo" trick
 *   maintains the heap invariant without explicitly comparing num vs peaks.
 *
 * Variants:
 *   If data stream has many integers in [0,100]: use bucket counting → O(1) add
 *   If only last k elements matter: sliding window median → more complex (two heaps + lazy deletion)
 */
