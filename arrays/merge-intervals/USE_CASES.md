# Merge Intervals — Real-World Use Cases

## 1. Calendar / Scheduling Systems

Merging overlapping meeting slots is the direct application. Google Calendar's
"find free slots" feature merges busy intervals and finds gaps. Merge intervals
is the first step in any calendar availability algorithm.

## 2. Database Query Optimization — Range Merging

In database engines, merging overlapping index ranges avoids redundant scans.
When a query uses multiple range predicates on the same column, the engine
merges overlapping ranges into minimal non-overlapping ranges before execution.

## 3. Genomics — Overlapping Read Assembly

In genome sequencing, overlapping DNA reads must be merged into contigs.
Sequence reads aligned to a reference genome form intervals; merging them
gives covered regions. Standard step in variant calling pipelines (GATK, BWA).
