# Implement Trie — Real-World Use Cases

## 1. Autocomplete / Search Suggestion

Every search engine's autocomplete (Google Search, IDE IntelliSense, command
completion in terminals) uses a Trie. Given a prefix, enumerate all words in
the Trie's subtree below the prefix node — O(L + results) vs O(W×L) for a set.

## 2. IP Routing — Longest Prefix Match

Internet routers use a binary Trie (Patricia trie) for IP address lookup.
Given a destination IP, find the longest matching routing prefix in the routing
table. The Trie structure makes this O(32) for IPv4 (32-bit addresses).

## 3. Spell Checker and Dictionary

Spell checkers use Tries for fast exact word lookup and prefix-based suggestion
generation. Edit distance search within a Trie prunes branches that can't produce
valid corrections — much faster than checking all dictionary words individually.
