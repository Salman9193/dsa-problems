# Longest Palindromic Substring — Research & Foundations

Finding the longest palindromic substring. Expand-around-center is O(n²); Manacher’s algorithm does it in O(n) by reusing mirror symmetry.

- **G. Manacher (1975), “A new linear-time on-line algorithm for finding the smallest initial palindrome of a string,”** *Journal of the ACM* 22(3):346–351. DOI: 10.1145/321892.321896. The origin of **Manacher’s algorithm** — linear-time palindrome finding. https://doi.org/10.1145/321892.321896

**Why it matters here:** Manacher’s 1975 algorithm computes the palindrome radius at every center in linear time — the optimal solution to this problem.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
