# Exam Room — Real-World Use Cases

The maximum distance seat placement algorithm — choosing seats to maximise
distance to the nearest occupied seat — was studied extensively during the
COVID-19 pandemic as the **Maximum Diversity Social Distancing Problem (MDPs)**.

---

## 1. Classroom Social Distancing — Max Diversity Social Distancing Problem

Universities and schools worldwide needed to assign students to seats such
that the minimum distance between any two students was maximised. This is
the exact problem modelled by ExamRoom, extended to 2D classroom layouts.

### The 1D connection

For row-by-row seating (cinema rows, exam rows, train carriages), each row
is an independent 1D instance of the ExamRoom problem. The greedy midpoint
algorithm — sit at the midpoint of the largest gap — is the same algorithm
used in production social distancing seat allocation systems.

### Two-phase optimisation

These studies typically aimed to maximize the minimum distance between students for a given number of students to be assigned. This problem is a two-phased problem: the first phase produces the maximum of minimum distance (max-min) between students; the second phase maximises average distance. The first phase is solved by a new algorithm which effectively determines the max-min distance for each student allocation scenario.

Phase 1 — finding the max-min distance — IS the ExamRoom problem: place k
students such that the minimum pairwise distance is maximised. The greedy
midpoint approach gives the optimal solution for the 1D case.

A greedy random-based algorithm is presented to determine efficiently an initial feasible solution. The new neighborhood search procedure based on graph theory is introduced, in which the dominated, dominating, and nondominated seats are determined based on social distance. The proposed SA-MDPs is evaluated on classrooms with varying capacities and benchmarked against an off-the-shelf optimization solver.

### References

- **Paper:** *A Simulated Annealing with Graph-Based Search for the
  Social-Distancing Problem in Enclosed Areas During Pandemics*,
  PMC11813157 (2024).
  https://www.ncbi.nlm.nih.gov/pmc/articles/PMC11813157/
  "Depending on the number of students in the classroom, maintaining the
  minimum social distance between students and assigning them to seats as
  far away from each other as possible decreases the risk of virus
  transmission."

- **Paper:** *A Robust Seating Arrangement for Future Pandemics*,
  ResearchGate (2024).
  https://www.researchgate.net/publication/383072143_A_Robust_Seating_Arrangement_for_Future_Pandemics
  "The greedy approximation MDPs model mostly returns optimal solutions
  across all tested classrooms in less than a second, increasing the
  average distance by over 40 centimeters compared to the max-min
  distance approach of the literature."

---

## 2. Airplane Seat Assignment — Greedy Maximum Separation

Airlines used the same greedy maximum-separation principle to assign
passengers during the COVID-19 pandemic, separating potentially infectious
from susceptible passengers.

This paper presents three greedy methods that assign passengers to airplane seats so that those passengers most likely to be susceptible to infectious diseases are separated from those passengers who are most likely to be infectious. Stochastic simulation results show that the performance of the proposed greedy methods provide much higher values for the average distance of separation between susceptible and infectious passengers compared to a random seat assignment. The improvements range from 152% to 343% across the selected scenarios.

The greedy assignment — place each new passenger in the seat maximising
distance to existing passengers — is the same algorithm as `seat()` in the
ExamRoom problem, applied to a 2D aircraft layout.

### Reference

- **Paper:** *Airplane Seating Assignment Greedy Algorithms that Separate
  Passengers Likely to be Susceptible to Infectious Disease from those
  Likely to be Infectious*, IEEE Access (2024).
  https://www.researchgate.net/publication/379422870

---

## 3. Railway Carriage Seat Allocation

The social distance rule in enclosed areas was implemented by educational institutions in many countries. In this study, the problem of assigning students to seats considering the social distancing constraint is found to be similar to the Maximum Diversity Problem (MDP) in the literature. The problem is named the Maximum Diversity Social Distancing problem (MDPs). A simulated annealing algorithm framework for MDPs is proposed to identify an optimal or near-optimal solution within a reasonable computational time.

For railway carriages with linear seat rows, the 1D ExamRoom algorithm
(midpoint of largest gap) gives the optimal single-row placement —
used as the base case in more complex 2D carriage optimisation models.

### Reference

- **Paper:** *Seat allocation optimization for railways considering social
  distancing during the post-pandemic period*, ResearchGate (2023).
  https://www.researchgate.net/publication/375170862

---

## 4. The Max-Min Distance Placement Algorithm

The general problem — place k items in n positions to maximise the minimum
gap between any two items — is a classical combinatorial optimisation problem
with applications beyond seating:

| Domain | Items | Positions | Maximise |
|--------|-------|-----------|---------|
| Exam room (#855) | Students | Seats 0..n-1 | Min distance to nearest student |
| Classroom seating | Students | Grid cells | Min pairwise distance |
| Cell tower placement | Towers | Locations | Min inter-tower distance (avoid interference) |
| Disk sector allocation | Read heads | Sectors 0..n-1 | Min head-to-head distance |
| Frequency assignment | Transmitters | Frequency slots | Min spectral separation |

The greedy midpoint algorithm gives the exact optimum for the 1D case.
For 2D and higher dimensions, simulated annealing or ILP are needed.

---

## Further Reading

- Social distancing seating (PMC): https://www.ncbi.nlm.nih.gov/pmc/articles/PMC11813157/
- Robust seating arrangement: https://www.researchgate.net/publication/383072143
- Airplane seating greedy: https://www.researchgate.net/publication/379422870
- Railway seat allocation: https://www.researchgate.net/publication/375170862
- Maximum diversity problem (Wikipedia): https://en.wikipedia.org/wiki/Maximum_diversity_problem
