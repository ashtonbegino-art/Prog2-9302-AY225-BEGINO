# 📊 Prelim Grade Calculator

A web-based Prelim Grade Calculator that helps students determine whether they can **pass (75)** or achieve an **Excellent grade (100)** based on their **attendance** and **lab work performance**.

---

## 🔢 Grading Formula

### Attendance Percentage

Attendance is based on a maximum of **5 class meetings**.

Attendance Percentage = (Attendance ÷ 5) × 100

---

### Lab Work Average

The average of three lab activities:

Lab Average = (Lab 1 + Lab 2 + Lab 3) ÷ 3

---

### Class Standing

Class Standing is composed of:

- 40% Attendance
- 60% Lab Work Average

Class Standing = (0.40 × Attendance %) + (0.60 × Lab Average)

---

### Prelim Grade Formula

The Prelim Grade is computed as:

Prelim Grade = (0.30 × Prelim Exam) + (0.70 × Class Standing)

The calculator solves this formula to determine the required Prelim Exam score.

---

### Required Exam Score to Pass (75)

Required Exam = (75 − 0.70 × Class Standing) ÷ 0.30

---

### Required Exam Score for Excellent (100)

Required Exam = (100 − 0.70 × Class Standing) ÷ 0.30

---

## 📌 Result Interpretation

- Required score greater than 100 → FAILED (impossible to pass)
- Required score less than or equal to 0 → Already PASSED
- Otherwise → Displays the exact score needed on the Prelim Exam

---

## ⌨️ Keyboard Controls

This calculator supports full keyboard navigation.

- Enter  
  Moves to the next input field.  
  On the last input, it calculates the grade.

- / (Slash)  
  Automatically jumps to the next empty input field in this order:  
  Attendance → Lab 1 → Lab 2 → Lab 3

- <- Backspace  
  When the current input field is empty, pressing Backspace moves focus to the previous input field
  (Lab 3 → Lab 2 → Lab 1 → Attendance).

- F  
  Clears all inputs, hides results, and focuses back on Attendance.

---

## 🛡️ Input Validation

- Attendance is limited to 0-5
- Lab grades are limited to 0–100
- Prevents invalid or out-of-range values
- Displays warnings instead of calculating incorrect results

---
