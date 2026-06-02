# 🧩 Dynamic Algorithmic Sudoku Solver & Generator

A polished, fully dynamic 9x9 Sudoku application built from scratch using Java Swing (`javax.swing`). This project moves away from static, hardcoded puzzle banks to implement a pure algorithmic backtracking solver and real-time validation engine, wrapped in a high-quality, customized user interface.

---

## 🚀 Core Features

### 🧠 Algorithmic Backend
* **Recursive Backtracking Solver:** Programmed completely in pure Java to traverse and solve any valid 9x9 grid configuration in milliseconds.
* **Dynamic Puzzle Generator:** Utilizes a randomized seeding algorithm to build a fully completed valid Sudoku board first, then strategically strips away 45 numbers to present a unique, solvable challenge every game.

### 🎮 Polished Gameplay UI
* **Real-Time Color Feedback:** Integrated custom `DocumentListener` updates that visually analyze your moves on the fly. Duplicate entries or rule-breaking numbers instantly flash **Red**, while correct/safe numbers stay a sleek **Blue**.
* **Arrow Key Grid Navigation:** Move flawlessly across the board matrix using your keyboard's Up, Down, Left, and Right arrow keys. 
* **Window Adaptability:** Supports fullscreen maximizing layout adjustments seamlessly.
* **Input Interceptors:** Rigid text filtering ensures fields *only* accept a single digit from `1-9`. Alphabets, symbols, or multi-digit spacing are strictly ignored by the text fields.

### 🍍 High-Tier Custom Theme & Memes
* **Visual Palette:** Features an energetic amber-gold grid (`#FFC94D`) contrasted beautifully by flat baby pink buttons (`#FA6781`) with soft tan typography (`#FAE7CB`).
* **SpongeBob Reward Milestones:**
  * **"Solve" Button:** Clears user inputs and applies the optimal backtracking path, displaying a celebratory window with a custom `solve.gif`.
  * **"ATE the Puzzle" Button:** Validates the board. Solving it correctly on your first go triggers a specialized dynamic celebration view.
  * **"ATE AND LEFT NO CRUMBS":** Clicking the submission button a second time upgrades your victory tier with an exclusive title overlay and a fully tracked victory GIF.
  * **"Math isn't mathing":** Facing an incorrect solution pops up a customized error viewport rendering an animated `wrong.gif` to keep the user encouraged.

---

## ⚙️ Compilation and Execution

Ensure you have your code files (`SudokuLogic.java`, `SudokuGUI.java`) and asset files (`solve.gif`, `success.gif`, `wrong.gif`) organized in the same workspace directory.

### Command Line Instructions:

```bash
# Compile both system profiles simultaneously 
javac SudokuLogic.java SudokuGUI.java

# Execute the GUI entrance thread
java SudokuGUI
