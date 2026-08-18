# Cracking the Safe — Real-World Use Cases

This problem *is* a real-world use case — it's the literal **keypad-cracking** application of de
Bruijn sequences. The underlying idea (pack every length-`n` pattern into the shortest possible
stream via maximal overlap) shows up wherever you need to cover all combinations compactly or
identify position from a local view.

---

## 1. Brute-Forcing Keypads Without an "Enter" Key

The problem verbatim. A door/safe/garage keypad that checks the **last `n` digits after every
keypress** can be attacked with a de Bruijn sequence instead of trying codes one by one:

```
4-digit PIN, 10 digits:  10,000 codes × 4 = 40,000 keystrokes if tried separately
de Bruijn B(10,4):       all codes in 10,003 keystrokes  (~4× fewer)
```

This is a **genuine physical-security consideration** for keypads without lockout or an explicit
submit step — the de Bruijn sequence is the optimal attack stream.

---

## 2. Minimal-Length Test Vectors

To test that a system behaves correctly for **every** `n`-symbol input combination, a de Bruijn
sequence is the **shortest input that exercises all of them**. Hardware test patterns, fuzzing seeds
that must cover all n-grams, and state-machine coverage all use this to minimize test length while
guaranteeing full combination coverage.

---

## 3. Rotary/Absolute Position Encoders

Print a de Bruijn pattern around a wheel or along a strip; because **every length-`n` window is
unique**, a sensor reading any `n` consecutive marks knows its **absolute position** immediately — no
homing or reference pass. Robotics, motor control, and precision instruments use exactly this.

---

## 4. Structured-Light 3D Scanning

Project a de Bruijn-coded stripe pattern onto an object; from any small captured patch the system
identifies **which** stripes it's seeing (local uniqueness again), letting it triangulate depth. A
standard technique in 3D scanners and depth cameras.

---

## The Unifying Idea

```
need: cover every length-n combination in minimal space, OR identify position from a local window
tool: a de Bruijn sequence = Eulerian circuit on a de Bruijn graph (this problem)
```

| Application | Superpower used |
|-------------|-----------------|
| Keypad cracking (this problem) | maximal overlap (fewest keystrokes) |
| Minimal test vectors | maximal overlap (shortest full-coverage input) |
| Rotary encoders | local uniqueness (position from any window) |
| Structured-light scanning | local uniqueness (identify stripes locally) |

---

## Further Reading

- [Eulerian Path & de Bruijn guide](#guides/EULERIAN_DE_BRUIJN) — the graph theory, existence
  conditions, genome assembly, and the full de Bruijn story.
- [Reconstruct Itinerary #332](#graphs/reconstruct-itinerary) — the given-graph Eulerian twin.
- de Bruijn sequence: https://en.wikipedia.org/wiki/De_Bruijn_sequence
