# Daily Temperatures — Real-World Use Cases

## 1. Stock Price — Next Higher Price Day

Finding the next day when a stock's price exceeds today's price is exactly
Daily Temperatures on stock price arrays. Used in options pricing (when will
the stock first hit a higher price?) and momentum trading signals.

## 2. Server Load Monitoring — Next Peak

In infrastructure monitoring, finding the next time server load exceeds the
current level triggers auto-scaling decisions. The monotonic stack processes
load readings in O(n), resolving each reading's "next higher load" event.

## 3. Compiler — Next Use of Variable

In register allocation, for each variable use, finding the "next use" of
that variable (to determine when to spill from register to memory) uses the
same "next greater occurrence" pattern on the variable occurrence timeline.
