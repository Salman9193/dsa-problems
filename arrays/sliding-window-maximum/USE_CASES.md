# Sliding Window Maximum — Real-World Use Cases

The sliding window maximum appears in two very different domains: image
processing (morphological dilation) and quantitative finance (Donchian Channel).

---

## 1. Image Processing — Morphological Dilation (OpenCV)

Morphological dilation is the 2D generalisation of sliding window maximum.
It replaces each pixel with the maximum pixel value in its neighbourhood
(defined by a structuring element / kernel). Used for:

- Expanding bright regions in binary images
- Filling holes and connecting broken edges
- Noise removal and feature enhancement
- Preprocessing for object detection and OCR

### How it maps to this problem

For a 1D row-wise dilation with a flat structuring element of width k:

```
input row:    [1, 3, -1, -3, 5, 3, 6, 7]
kernel size:  k = 3
output row:   [3,  3,  5,  5, 6, 7]   ← sliding window maximum ✓
```

2D dilation is implemented as two separable 1D passes:
1. Row-wise sliding window maximum (this algorithm)
2. Column-wise sliding window maximum

The monotonic deque makes each 1D pass O(n) instead of O(nk) — critical
for real-time video processing.

### References

- **OpenCV official documentation — Eroding and Dilating:**
  https://docs.opencv.org/3.4/db/df6/tutorial_erosion_dilatation.html
  "Morphological dilation: as the kernel is scanned over the image, we
  compute the maximal pixel value overlapped by the kernel and replace
  the image pixel in the anchor point position with that maximal value."

- **OpenCV source — cv::dilate():**
  Implements separable 1D max-filter passes using a sliding window approach.
  https://github.com/opencv/opencv/blob/master/modules/imgproc/src/morph.dispatch.cpp

- **USPTO Patent 9704228 — System and method for fractional pixel dilation:**
  https://image-ppubs.uspto.gov/dirsearch-public/print/downloadPdf/9704228
  "Morphological dilation implements a sliding window operation which
  selects a maximum pixel value from image pixel data within a defined
  window structure of the sliding window operation."

- **Paper:** *GSWO: A programming model for GPU-enabled parallelization of
  sliding window operations in image processing*, ScienceDirect, 2016.
  https://www.sciencedirect.com/science/article/abs/pii/S0923596516300601
  Covers GPU parallelisation of morphological dilation (sliding window max)
  using CUDA — the same algorithm, accelerated to millions of pixels/second.

---

## 2. Quantitative Finance — Donchian Channel (Sliding Window Maximum)

The Donchian Channel, developed by Richard Donchian in the 1970s, is a
trend-following technical indicator where:

```
Upper Band = max(close[i-k+1 .. i])   ← sliding window maximum (this problem)
Lower Band = min(close[i-k+1 .. i])   ← sliding window minimum
Middle Line = (Upper + Lower) / 2
```

The upper band is computed by exactly the sliding window maximum algorithm.
A price breaking above the upper band signals a breakout and triggers long
entries in trend-following strategies.

### The Turtle Trading Connection

Donchian Channels were famously used in the "Turtle Traders" experiment
(Richard Dennis, 1983) — a group of novice traders who used a 20-day
Donchian breakout strategy and generated over $100 million in profits.

### References

- **Academic paper:** *Testing a price breakout strategy using Donchian Channels*,
  Academia.edu, 2016.
  https://www.academia.edu/68475740/Testing_a_price_breakout_strategy_using_Donchian_Channels
  Empirically tests the Donchian Channel breakout (Turtle method) on the
  South African Futures Exchange — the upper band is the rolling max over k periods.

- **TrendSpider — Donchian Channel Trading Strategies:**
  https://trendspider.com/learning-center/donchian-channel-trading-strategies/
  "The Upper Band is the highest high over the selected period. Donchian
  Channels can be applied to any market: stocks, forex, commodities, crypto,
  or indices. They work across any timeframe, from intraday to weekly."

- **Donchian Channel Breakout backtesting (1990-2025):**
  https://algomatictrading.substack.com/p/strategy-8-the-easiest-trend-system
  35 years of Donchian Channel backtests on NASDAQ and Gold Futures —
  demonstrates the practical value of computing sliding window max efficiently.

---

## The Algorithm in Both Domains

```
Sliding window maximum using monotonic deque:

Morphological dilation:     result[i] = max(pixels[i-k+1 .. i])
Donchian upper band:        upper[i]  = max(close[i-k+1 .. i])
LeetCode #239:              result[i-k+1] = max(nums[i-k+1 .. i])
```

All three compute the same rolling maximum — the deque makes it O(n)
instead of O(nk), enabling real-time video processing and live trading feeds.

---

## Summary

| Domain | Input array | Window k = | Output = |
|--------|------------|------------|---------|
| OpenCV morphological dilation | Pixel intensity row | Kernel width | Max pixel in neighbourhood |
| Donchian Channel (trading) | Stock close prices | Lookback period (e.g. 20) | Upper band (rolling high) |
| LeetCode #239 | Integer array | Window size | Maximum per window |

---

## Further Reading

- OpenCV dilation tutorial: https://docs.opencv.org/3.4/db/df6/tutorial_erosion_dilatation.html
- OpenCV morph source: https://github.com/opencv/opencv/blob/master/modules/imgproc/src/morph.dispatch.cpp
- Donchian Channel paper: https://www.academia.edu/68475740/Testing_a_price_breakout_strategy_using_Donchian_Channels
- Donchian strategies guide: https://trendspider.com/learning-center/donchian-channel-trading-strategies/
