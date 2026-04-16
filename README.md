# Java-Swing-Sudoku
A fully functional Sudoku game built in Java using OOP concepts and Swing UI.
# Java Swing Sudoku 🧩

A fully functional, desktop-based Sudoku game built entirely in Java. This was created as an academic mini-project to demonstrate core Object-Oriented Programming (OOP) concepts and Event-Driven UI design.

## 🚀 Features
* **Interactive UI:** Built using Java Swing with real-time input validation (turns red on incorrect moves).
* **Game Logic Engine:** Backend validation for Sudoku rules (Row, Column, and 3x3 Grid checks).
* **Multiple Puzzles:** Includes a bank of hardcoded puzzles that load randomly on a "New Game".
* **Auto-Solve:** Implemented a one-click solution display.

## 🧠 Concepts Demonstrated
* **Separation of Concerns:** The project strictly separates the visual interface (`SudokuGUI.java`) from the mathematical logic (`SudokuLogic.java`).
* **2D Arrays:** Utilized `int[][]` matrices to store, update, and validate the game state.
* **Encapsulation:** Protected backend data structures with public getter/setter methods to prevent UI tampering.
* **Event Listeners:** Used `KeyAdapter` and `ActionListener` for responsive gameplay.

## 🛠️ How to Run Locally
1. Clone this repository.
2. Open your terminal and navigate to the folder.
3. Compile the code: `javac *.java`
4. Run the application: `java SudokuGUI`
