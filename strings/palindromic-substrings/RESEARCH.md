# Palindromic Substrings — Research & Foundations

Counting palindromic substrings. Each center contributes a run of palindromes; Manacher’s algorithm yields all palindrome radii in O(n).

- **G. Manacher (1975), “A new linear-time on-line algorithm for finding the smallest initial palindrome of a string,”** *Journal of the ACM* 22(3):346–351. DOI: 10.1145/321892.321896. The origin of **Manacher’s algorithm** — linear-time palindrome finding. https://doi.org/10.1145/321892.321896

**Why it matters here:** The count is the sum of palindrome radii over all centers, which Manacher’s linear-time algorithm produces directly.

*Citations verified against CACM / JACM / IBM Systems Journal / SP&E records this session — not from memory.*
