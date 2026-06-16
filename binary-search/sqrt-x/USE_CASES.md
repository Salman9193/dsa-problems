# Sqrt(x) — Real-World Use Cases

Integer square root and Newton's method appear in two foundational domains:
real-time 3D graphics and number theory / cryptography.

---

## 1. Computer Graphics — Fast Inverse Square Root (Quake III Arena)

The most famous application of Newton's method for square roots is the
**Fast Inverse Square Root** algorithm used in Quake III Arena (1999)
for real-time vector normalisation in 3D lighting calculations.

### Why sqrt is needed in 3D graphics

In 3D rendering, vector normalisation is performed millions of times per
frame to compute lighting, reflections, and surface normals:

```
normalize(v) = v / |v| = v / sqrt(vx^2 + vy^2 + vz^2)
```

In 1999, `sqrt()` was prohibitively slow on available CPUs. The Quake III
programmers needed an approximation of `1/sqrt(x)` that was fast enough
for real-time rendering.

### The algorithm

```c
float Q_rsqrt(float number) {
    long i;
    float x2, y;
    const float threehalfs = 1.5F;

    x2 = number * 0.5F;
    y  = number;
    i  = * ( long * ) &y;           // bit-level reinterpretation
    i  = 0x5f3759df - ( i >> 1 );   // magic constant initial guess
    y  = * ( float * ) &i;
    y  = y * ( threehalfs - ( x2 * y * y ) );  // Newton-Raphson iteration
    return y;
}
```

The magic constant `0x5f3759df` provides a brilliant initial approximation
using the IEEE 754 floating-point bit representation. A single Newton-Raphson
iteration then refines it to ~0.175% maximum error.

This is the integer version of the same Newton's method used in `mySqrtNewton()`:
both start with an approximation and iterate `r = (r + x/r) / 2` to converge.

### References

- **Wikipedia — Fast inverse square root:**
  https://en.wikipedia.org/wiki/Fast_inverse_square_root
  "Quake III Arena used the algorithm to perform rapid vector normalisation.
  In 2007 the algorithm was implemented in dedicated hardware vertex shaders
  using FPGAs."

- **Paper:** Walczyk et al. — *Simple Effective Fast Inverse Square Root
  Algorithm with Two Magic Constants*, ResearchGate, 2019.
  https://www.researchgate.net/publication/349173096_SIMPLE_EFFECTIVE_FAST_INVERSE_SQUARE_ROOT_ALGORITHM_WITH_TWO_MAGIC_CONSTANTS
  Introduces two magic constants to minimise relative error, with a
  fused multiply-add in the second Newton iteration for higher accuracy.

- **FPGA implementation paper:**
  *Fast Inverse Square Root using FPGA*, ResearchGate, 2024.
  https://www.researchgate.net/publication/378163213_Fast_Inverse_Square_Root_using_FPGA

- **Quantum computing extension:**
  *Optimizing Quantum Circuits for Arithmetic*, arXiv:1805.12445.
  https://arxiv.org/pdf/1805.12445
  "In classical computing, inverse square roots appear in computer graphics.
  The fast inverse square root labels the procedure to approximate the
  inverse square root using bit-operations on the floating-point representation,
  as done in Quake III Arena — the code ultimately performs a Newton-Raphson
  iteration to improve upon an accurate initial guess."

---

## 2. Cryptography & Number Theory — Trial Division Upper Bound

The integer square root determines the exact upper bound for trial division
in primality testing — a foundational operation in RSA key generation and
all public-key cryptography.

### Why sqrt(n) is the upper bound

If n is composite (not prime), it must have at least one factor <= sqrt(n).
Proof: if n = a × b with a > sqrt(n) and b > sqrt(n), then a*b > n —
contradiction. So we only need to check divisors up to floor(sqrt(n)).

```
isPrime(n):
  for i in 2..floor(sqrt(n)):      ← integer sqrt = this problem
    if n % i == 0: return false
  return true
```

The `floor(sqrt(n))` computed by this LeetCode problem is the exact value
used as the loop upper bound in every trial division primality tester.

### Where this matters in production

- **RSA key generation:** generates two large primes p and q. Trial division
  is the preprocessing step that eliminates small prime factors before
  invoking Miller-Rabin probabilistic tests.
- **OpenSSL / BoringSSL:** primality testing pipeline starts with trial
  division up to sqrt(n) for small primes.
- **TLS handshake:** all HTTPS connections depend on prime generation, which
  depends on correct integer sqrt computation.

### References

- **Wikipedia — Trial Division:**
  https://en.wikipedia.org/wiki/Trial_division
  "Trial division tests whether an integer n can be divided by each number
  in turn that is less than or equal to the square root of n. First described
  by Fibonacci in Liber Abaci (1202)."

- **Primality Testing (Brilliant.org):**
  https://brilliant.org/wiki/prime-testing/
  "The simplest primality test is trial division: check whether n is divisible
  by any prime between 2 and sqrt(n). Time complexity is O(sqrt(n)) multiplied
  by the time for division."

- **Miller-Rabin primality test (production algorithm for RSA):**
  Miller 1976, Rabin 1980 — the probabilistic test used after trial division
  in all production cryptographic libraries.

---

## The Newton's Method Connection

Both use cases share the same mathematical core:

```
Newton's method for sqrt:   r_{n+1} = (r_n + x / r_n) / 2
Quake III Fast InvSqrt:     y = y * (1.5 - x2 * y * y)   [same iteration, different form]
Trial division bound:       loop up to floor(sqrt(n))      [binary search finds this]
```

| Domain | Algorithm | Newton used for |
|--------|-----------|----------------|
| Quake III graphics | Fast Inverse Square Root | Approximate 1/sqrt(x) in one iteration |
| mySqrtNewton() | Integer sqrt via Newton | Exact floor(sqrt(x)) via convergence |
| RSA primality (trial division) | Binary search sqrt | Compute trial division upper bound |

---

## Further Reading

- Fast inverse square root (Wikipedia): https://en.wikipedia.org/wiki/Fast_inverse_square_root
- Walczyk et al. two magic constants: https://www.researchgate.net/publication/349173096
- Trial division (Wikipedia): https://en.wikipedia.org/wiki/Trial_division
- Primality testing (Brilliant): https://brilliant.org/wiki/prime-testing/
- Quantum inverse sqrt: https://arxiv.org/pdf/1805.12445
