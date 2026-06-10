# Product of Array Except Self — Real-World Use Cases

The prefix × suffix pattern — computing a value at each position using
everything to its left and everything to its right — is foundational in
machine learning and probabilistic inference.

---

## 1. Autodiff — Backpropagation Through Multiplication Layers

When a neural network contains a layer that multiplies inputs element-wise
(e.g. gating mechanisms, attention dot-products, element-wise product layers),
the gradient of the loss with respect to each input `xᵢ` is the product of
all other inputs — exactly `productExceptSelf(x)`.

Autodiff frameworks (PyTorch, JAX, TensorFlow) compute this using the
prefix-suffix pattern during the backward pass:

```
Forward:  y = x[0] * x[1] * x[2] * ... * x[n-1]
Backward: grad[i] = prefix_product[i] * suffix_product[i]
          (product of all x[j] where j ≠ i)
```

This is called the **VJP (Vector-Jacobian Product)** of the elementwise
multiplication operation — and it is implemented as two passes (forward
prefix, backward suffix) in every major autodiff engine.

- **JAX Autodiff Cookbook:**
  https://jax.readthedocs.io/en/latest/notebooks/autodiff_cookbook.html
  Covers custom VJPs and how JAX computes gradients through product operations
  using forward and reverse mode passes.

- **Paper:** Blondel et al. — *The Elements of Differentiable Programming*,
  arXiv:2403.14606 (2024)
  https://arxiv.org/pdf/2403.14606
  "The key idea of the forward-backward algorithm is to use distributivity
  of multiplication over addition to compute prefix and suffix products
  recursively — either forward or backward."

- **Dive into Deep Learning — Automatic Differentiation:**
  http://d2l.ai/chapter_preliminaries/autograd.html

---

## 2. HMM Forward-Backward Algorithm (Speech Recognition / NLP)

The forward-backward algorithm for Hidden Markov Models (HMMs) computes
the posterior marginal probability of each hidden state given all observations.
It uses two passes over the sequence:

- **Forward pass (α):** prefix products of transition and emission probabilities
  going left-to-right
- **Backward pass (β):** suffix products going right-to-left

Combining them: `P(state at time t | all observations) ∝ α[t] × β[t]`

This is the probability analogue of `answer[i] = prefix[i] * suffix[i]`.

The forward-backward algorithm is the core of:
- Training speech recognition systems (HMM-based ASR)
- Named entity recognition (NLP)
- Biological sequence analysis (gene finding, protein structure)

- **Canonical paper:** Rabiner, L.R. — *A Tutorial on Hidden Markov Models
  and Selected Applications in Speech Recognition*,
  Proceedings of the IEEE, 77(2):257–286, 1989.
  (The most-cited paper on HMMs — forward pass = prefix products,
  backward pass = suffix products, combined for posterior inference.)

- **Jurafsky & Martin — Speech and Language Processing, Appendix A:**
  https://web.stanford.edu/~jurafsky/slp3/A.pdf
  "The numerator of the forward-backward re-estimation equations is the
  product of the forward probability α and the backward probability β —
  the two prefix/suffix probability arrays combined at each time step."

- **Wikipedia — Forward-Backward Algorithm:**
  https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
  "The algorithm makes use of dynamic programming to efficiently compute
  the values required to obtain the posterior marginal distributions in
  two passes — one forward in time, one backward."

---

## The Unified Pattern

Both use cases share the same mathematical structure:

```
For each position i:
    result[i] = f(everything left of i) × g(everything right of i)

Computed in two O(n) passes:
    Pass 1 (left-to-right):  accumulate prefix values
    Pass 2 (right-to-left):  multiply in suffix values
```

| Domain | Left pass | Right pass | Combined |
|--------|-----------|------------|---------|
| Product except self | Prefix product | Suffix product | answer[i] |
| Autodiff backward pass | Left-context gradient | Right-context gradient | grad[i] for each input |
| HMM forward-backward | Forward prob α[t] | Backward prob β[t] | Posterior P(state t \| obs) |

---

## Further Reading

- JAX Autodiff Cookbook: https://jax.readthedocs.io/en/latest/notebooks/autodiff_cookbook.html
- Elements of Differentiable Programming: https://arxiv.org/pdf/2403.14606
- Rabiner HMM tutorial (1989): search "Rabiner 1989 HMM tutorial IEEE"
- Jurafsky & Martin Appendix A (HMM): https://web.stanford.edu/~jurafsky/slp3/A.pdf
- Forward-backward algorithm (Wikipedia): https://en.wikipedia.org/wiki/Forward%E2%80%93backward_algorithm
