// MountainArray interface (provided by LeetCode)
interface MountainArray {
    int get(int index);
    int length();
}

class Solution {

    // Three-Step Strategy — O(log n) API calls total
    //
    // Step 1: Find peak index (binary search on slope direction)
    // Step 2: Binary search ascending left half  [0, peak]
    // Step 3: Binary search descending right half [peak+1, n-1]
    //
    // Why search left first?
    //   We want the MINIMUM index where target appears.
    //   Left half indices are always smaller than right half indices.
    //   If target found in left half → return immediately (guaranteed minimum).
    //
    // Total API calls: ~3 * log2(n) ≤ 42 for n=10000 — well within 100 limit.
    public int findInMountainArray(int target, MountainArray mountainArr) {
        int n = mountainArr.length();

        // Step 1: Find peak
        int peak = findPeak(mountainArr, n);

        // Step 2: Search ascending left half [0, peak]
        int left = binarySearchAsc(mountainArr, target, 0, peak);
        if (left != -1) return left;  // found at smaller index → return immediately

        // Step 3: Search descending right half [peak+1, n-1]
        return binarySearchDesc(mountainArr, target, peak + 1, n - 1);
    }

    // Peak: largest index where arr[mid] > arr[mid-1] and arr[mid] > arr[mid+1]
    // Binary search on slope: arr[mid] < arr[mid+1] means still ascending → peak is right
    private int findPeak(MountainArray arr, int n) {
        int left = 0, right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr.get(mid) < arr.get(mid + 1)) {
                left = mid + 1;  // still ascending → peak is to the right
            } else {
                right = mid;     // descending or at peak → peak is here or left
            }
        }
        return left;  // left == right == peak index
    }

    // Standard binary search on ascending (sorted) array
    private int binarySearchAsc(MountainArray arr, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int val = arr.get(mid);
            if      (val == target) return mid;
            else if (val < target)  left  = mid + 1;
            else                    right = mid - 1;
        }
        return -1;
    }

    // Binary search on descending array — comparisons are flipped:
    //   Going RIGHT means going to SMALLER values (opposite of normal)
    private int binarySearchDesc(MountainArray arr, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int val = arr.get(mid);
            if      (val == target) return mid;
            else if (val > target)  left  = mid + 1;  // need smaller → go right
            else                    right = mid - 1;  // need larger  → go left
        }
        return -1;
    }
}

/*
 * Complexity
 * ----------
 * Time:     O(log n) — three binary searches in sequence
 * API calls: O(3 log n) ≈ 42 calls for n=10000 (limit: 100)
 * Space:    O(1)
 *
 * Binary Search Family — Peak Finding:
 *   #162  Find Peak Element      → same peak-finding binary search (any local peak)
 *   #1095 Find in Mountain Array → find global peak + search both halves
 *   #33   Search Rotated Array   → find which half is sorted
 *   #153  Find Min in Rotated    → find the rotation breakpoint
 *
 * Trace — mountainArr=[1,2,3,4,5,3,1], target=3
 * -----------------------------------------------
 * n=7
 *
 * Step 1 — Find Peak:
 *   left=0, right=6
 *   mid=3: arr[3]=4 < arr[4]=5 → left=4
 *   mid=4: arr[4]=5 > arr[5]=3 → right=4
 *   left==right=4 → peak=4 (value=5) ✓
 *
 * Step 2 — Binary Search [0,4] ascending for 3:
 *   left=0, right=4, mid=2: arr[2]=3 == target → return 2 ✓
 *
 * Trace — mountainArr=[0,1,2,4,2,1,0], target=3
 * -----------------------------------------------
 * Peak = index 3 (value=4)
 * Step 2: arr[1]=1<3→left=2; arr[2]=2<3→left=3; arr[3]=4>3→right=2 → not found
 * Step 3 [4,6] descending: arr[5]=1<3→right=4; arr[4]=2<3→right=3 → not found
 * return -1 ✓
 */
