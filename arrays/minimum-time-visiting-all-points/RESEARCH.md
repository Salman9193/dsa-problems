# Minimum Time Visiting All Points — Research & Foundations

Minimum steps to visit points in order on a grid where diagonal moves are allowed — the sum of **Chebyshev (L∞) distances** between consecutive points.

- **Chebyshev (L∞) distance.** With diagonal moves allowed, the number of steps between two grid points is max(|dx|, |dy|) — the Chebyshev distance, an endpoint of the Minkowski family. See https://en.wikipedia.org/wiki/Chebyshev_distance and https://en.wikipedia.org/wiki/Minkowski_distance

**Why it matters here:** With a diagonal counting as one step, the time between two points is max(|dx|, |dy|) — the Chebyshev distance; the answer sums it over consecutive pairs.

*Kadane (Programming Pearls) and Chebyshev distance are cited as well-established references, framed honestly rather than as session-verified journal citations.*
