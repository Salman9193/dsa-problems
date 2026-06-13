class TopVotedCandidate {

    // Precompute leader[i] = who was leading right after the i-th vote.
    // For any query t, binary search times[] for the rightmost index <= t,
    // then return leader[index]. O(log n) per query vs O(n) naive scan.
    //
    // Tie-breaking: most recent voter wins (>= in the update condition).

    private int[] times;
    private int[] leader;

    public TopVotedCandidate(int[] persons, int[] times) {
        this.times = times;
        int n = persons.length;
        leader = new int[n];

        int[] votes = new int[n];   // votes[p] = total votes for person p
        int curLeader = -1;

        for (int i = 0; i < n; i++) {
            votes[persons[i]]++;

            // >= ensures that a tie with the current leader transfers lead
            // to the most recent voter (the problem's tie-breaking rule)
            if (curLeader == -1 || votes[persons[i]] >= votes[curLeader]) {
                curLeader = persons[i];
            }

            leader[i] = curLeader;
        }
    }

    // Binary search: find rightmost index where times[index] <= t
    // Upper-bound pattern: mid biased upward to avoid infinite loop
    // when left = right - 1 and times[mid] <= t.
    public int q(int t) {
        int left = 0, right = times.length - 1;
        while (left < right) {
            int mid = left + (right - left + 1) / 2;  // upper-bias
            if (times[mid] <= t) left = mid;
            else right = mid - 1;
        }
        return leader[left];
    }
}

/*
 * Complexity
 * ----------
 * Constructor: O(n) — single pass to build leader[]
 * Query q(t):  O(log n) — binary search on times[]
 * Space:       O(n) — leader[] array
 *
 * Why precompute instead of scan per query?
 *   Naive: O(n) per query → O(nQ) for Q queries
 *   Precompute + binary search: O(n) build + O(log n) per query → O(n + Q log n)
 *
 * Why upper-bias mid = left + (right - left + 1) / 2?
 *   Standard mid = left + (right - left) / 2 biases DOWN.
 *   When left = right - 1:
 *     Standard mid = left → if times[left] <= t, left stays (infinite loop!)
 *     Upper-bias mid = right → correctly advances left or shrinks right
 *   This is the canonical "rightmost element <= t" binary search pattern.
 *
 * Why >= for tie-breaking?
 *   votes[persons[i]] >= votes[curLeader]:
 *     If the new voter ties the current leader, the new voter wins.
 *     This implements "most recent voter wins in a tie."
 *     Using > would NOT update the leader on a tie → wrong answer.
 *
 * Trace — persons=[0,1,1,0,0,1,0], times=[0,5,10,15,20,25,30]
 * --------------------------------------------------------------
 * i=0: p0 votes=1, leader=0,  leader[0]=0
 * i=1: p1 votes=1, 1>=1 → leader=1, leader[1]=1
 * i=2: p1 votes=2, 2>=1 → leader=1, leader[2]=1
 * i=3: p0 votes=2, 2>=2 → leader=0, leader[3]=0
 * i=4: p0 votes=3, 3>=2 → leader=0, leader[4]=0
 * i=5: p1 votes=3, 3>=3 → leader=1, leader[5]=1
 * i=6: p0 votes=4, 4>=3 → leader=0, leader[6]=0
 *
 * leader = [0,1,1,0,0,1,0]
 *
 * q(3):  binary search times=[0,5,10,15,20,25,30] for <= 3 → index 0 → leader[0]=0 ✓
 * q(12): binary search for <= 12 → index 2 (t=10) → leader[2]=1 ✓
 * q(25): binary search for <= 25 → index 5 (t=25) → leader[5]=1 ✓
 */
