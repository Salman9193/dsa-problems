# Longest Consecutive Sequence — Real-World Use Cases

The core pattern — *find the longest unbroken run of consecutive integers
from an unordered set* — appears across networking, OS internals, genomics,
and product analytics.

---

## 1. TCP Packet Reassembly

TCP assigns every byte of data a sequence number. Packets can arrive out of
order; the receiver must reconstruct the original stream.

The receiver maintains a set of received sequence numbers and looks for the
longest consecutive run to determine how much contiguous data is ready to
hand to the application layer. Gaps indicate missing packets that must be
retransmitted.

- **RFC 793** — *Transmission Control Protocol*, IETF (1981)  
  https://www.ietf.org/rfc/rfc793.html  
  The foundational spec. Section 3.3 defines how sequence numbers are used
  to bind segments to contiguous byte ranges and detect duplicates/gaps.

> "Each segment is bound to as many consecutive sequence numbers as there
> are octets of data in the segment." — RFC 793, §3.3

---

## 2. Linux Buddy Memory Allocator

The Linux kernel's physical page allocator must find the largest block of
**contiguous** free pages to satisfy an allocation request. Free page frame
numbers are integers; the largest allocatable block = longest consecutive
sequence of free page IDs.

- **Linux kernel documentation** — *Physical Page Allocation (Chapter 6)*  
  https://www.kernel.org/doc/gorman/html/understand/understand009.html

- **How it works:**  
  Linux maintains free lists indexed by "order" (block size = 2^order pages).
  When a request arrives, it walks the free lists from the best-fit order
  upward until a contiguous block is found. If a larger block must be split,
  the unused half (the "buddy") is placed on the lower-order free list.

- **Source code:**  
  `mm/page_alloc.c` in the Linux kernel — search for `__alloc_pages` and
  `find_suitable_fallback`.  
  https://github.com/torvalds/linux/blob/master/mm/page_alloc.c

---

## 3. Genomics — BEDTools

DNA sequencing reads are mapped to integer chromosome positions. Identifying
consecutive mapped positions reveals contiguous gene regions, exon boundaries,
and coverage gaps — which is the longest-consecutive-sequence problem applied
to chromosome coordinates.

- **Original paper:**  
  Quinlan & Hall — *BEDTools: a flexible suite of utilities for comparing
  genomic features*, Bioinformatics, 26(6):841–842, 2010  
  https://academic.oup.com/bioinformatics/article/26/6/841/244688

- **Tool:** `mergeBed` collapses overlapping/consecutive intervals into single
  contiguous regions; `coverageBed` measures depth across consecutive positions.  
  https://bedtools.readthedocs.io

- **The connection:**  
  A genome interval is defined as "a consecutive stretch of nucleotides on a
  chromosome." Finding the longest such stretch from a set of mapped read
  positions is structurally identical to LeetCode #128.

---

## 4. Netflix — Episode Streak & Engagement Tracking

Netflix assigns each episode in a season an integer ID. A user's watch history
is a set of episode IDs. The longest consecutive run of watched episodes = the
user's uninterrupted viewing streak — a key signal for engagement scoring,
churn prediction, and "continue watching" ranking.

- **Netflix Tech Blog** — *Netflix's Viewing Data: How We Know Where You Are*
  https://techblog.netflix.com/2015/01/netflixs-viewing-data.html  
  Describes the architecture for tracking per-episode completion events at
  billions-of-events-per-day scale.

- **Netflix Tech Blog** — *FM-Intent: Predicting User Session Intent* (2025)  
  https://netflixtechblog.com/fm-intent-predicting-user-session-intent-with-hierarchical-multi-task-learning-94c75e18f4b8  
  Explicitly models "continue watching" intent — triggered when a member
  plays the follow-up episode of something already started — as a distinct
  signal derived from consecutive episode completion.

---

## Summary

| Domain | Integer sequence | Longest run means |
|--------|-----------------|-------------------|
| TCP networking | Byte sequence numbers | Contiguous data ready for delivery |
| Linux kernel | Free page frame numbers | Largest allocatable contiguous block |
| Genomics | Chromosome base positions | Contiguous gene region / coverage |
| Streaming (Netflix) | Episode IDs per season | Uninterrupted viewing streak |

---

## Further Reading

- RFC 793 (TCP): https://www.ietf.org/rfc/rfc793.html
- Linux page allocator source: https://github.com/torvalds/linux/blob/master/mm/page_alloc.c
- BEDTools paper: https://academic.oup.com/bioinformatics/article/26/6/841/244688
- BEDTools docs: https://bedtools.readthedocs.io
- Netflix viewing data blog: https://techblog.netflix.com/2015/01/netflixs-viewing-data.html
