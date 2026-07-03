# Find Median from Data Stream — Real-World Use Cases

## 1. Network Latency Monitoring — P50/Median Latency

Production monitoring systems (Datadog, Prometheus) track P50 (median)
latency from streaming request logs. The two-heap approach maintains the
running median as requests arrive without storing the full history.

## 2. Financial Trading — Rolling Median Price

In algorithmic trading, the rolling median price (more robust than mean to
outliers/spikes) is computed from a streaming price feed. The two-heap
structure supports O(log n) updates and O(1) median queries per tick.

## 3. IoT Sensor Data — Outlier-Robust Aggregation

IoT platforms (AWS IoT, Azure IoT) aggregate sensor readings in real time.
Median is more robust than mean for noisy sensor data. The streaming median
(two heaps) maintains the running median without buffering all readings.
