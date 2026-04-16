import java.util.Random;

public class SudokuLogic {
    private int[][] board = new int[9][9];
    private int currentPuzzleIndex = 0;

    // A bank of 3 different puzzles!
    private int[][][] puzzles = {
        { // Puzzle 1
            {5, 3, 0, 0, 7, 0, 0, 0, 0}, {6, 0, 0, 1, 9, 5, 0, 0, 0}, {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3}, {4, 0, 0, 8, 0, 3, 0, 0, 1}, {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0}, {0, 0, 0, 4, 1, 9, 0, 0, 5}, {0, 0, 0, 0, 8, 0, 0, 7, 9}
        },
        { // Puzzle 2
            {0, 0, 0, 2, 6, 0, 7, 0, 1}, {6, 8, 0, 0, 7, 0, 0, 9, 0}, {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0}, {0, 0, 4, 6, 0, 2, 9, 0, 0}, {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4}, {0, 4, 0, 0, 5, 0, 0, 3, 6}, {7, 0, 3, 0, 1, 8, 0, 0, 0}
        },
        { // Puzzle 3
            {1, 0, 0, 4, 8, 9, 0, 0, 6}, {7, 3, 0, 0, 0, 0, 0, 4, 0}, {0, 0, 0, 0, 0, 1, 2, 9, 5},
            {0, 0, 7, 1, 2, 0, 6, 0, 0}, {5, 0, 0, 7, 0, 3, 0, 0, 8}, {0, 0, 6, 0, 9, 5, 7, 0, 0},
            {9, 1, 4, 6, 0, 0, 0, 0, 0}, {0, 2, 0, 0, 0, 0, 0, 3, 7}, {8, 0, 0, 5, 1, 2, 0, 0, 4}
        }
    };

    // The solutions for the 3 puzzles
    private int[][][] solutions = {
        { // Solution 1
            {5, 3, 4, 6, 7, 8, 9, 1, 2}, {6, 7, 2, 1, 9, 5, 3, 4, 8}, {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3}, {4, 2, 6, 8, 5, 3, 7, 9, 1}, {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4}, {2, 8, 7, 4, 1, 9, 6, 3, 5}, {3, 4, 5, 2, 8, 6, 1, 7, 9}
        },
        { // Solution 2
            {4, 3, 5, 2, 6, 9, 7, 8, 1}, {6, 8, 2, 5, 7, 1, 4, 9, 3}, {1, 9, 7, 8, 3, 4, 5, 6, 2},
            {8, 2, 6, 1, 9, 5, 3, 4, 7}, {3, 7, 4, 6, 8, 2, 9, 1, 5}, {9, 5, 1, 7, 4, 3, 6, 2, 8},
            {5, 1, 9, 3, 2, 6, 8, 7, 4}, {2, 4, 8, 9, 5, 7, 1, 3, 6}, {7, 6, 3, 4, 1, 8, 2, 5, 9}
        },
        { // Solution 3
            {1, 5, 2, 4, 8, 9, 3, 7, 6}, {7, 3, 9, 2, 5, 6, 8, 4, 1}, {4, 6, 8, 3, 7, 1, 2, 9, 5},
            {3, 8, 7, 1, 2, 4, 6, 5, 9}, {5, 9, 1, 7, 6, 3, 4, 2, 8}, {2, 4, 6, 8, 9, 5, 7, 1, 3},
            {9, 1, 4, 6, 3, 7, 5, 8, 2}, {6, 2, 5, 9, 4, 8, 1, 3, 7}, {8, 7, 3, 5, 1, 2, 9, 6, 4}
        }
    };

    public SudokuLogic() {
        loadNewGame(); // Pick a random puzzle when the app starts!
    }

    // Tells the GUI if a box should be locked or editable
    public boolean isStartingNumber(int row, int col) {
        return puzzles[currentPuzzleIndex][row][col] != 0;
    }

    public int getNumberAt(int row, int col) {
        return board[row][col];
    }

    public boolean isValidMove(int row, int col, int number) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == number && i != col) return false;
            if (board[i][col] == number && i != row) return false;
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int d = boxColStart; d < boxColStart + 3; d++) {
                if (board[r][d] == number && r != row && d != col) return false;
            }
        }
        return true;
    }

    public void updateBoard(int row, int col, int number) {
        board[row][col] = number;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) return false;
            }
        }
        return true;
    }

    // Changes to a completely new puzzle
    public void loadNewGame() {
        Random rand = new Random();
        int newPuzzle;
        do {
            newPuzzle = rand.nextInt(puzzles.length);
        } while (newPuzzle == currentPuzzleIndex); // Make sure it doesn't pick the same one twice!
        
        currentPuzzleIndex = newPuzzle;
        resetBoard();
    }

    // Clears the current puzzle back to its starting state
    public void resetBoard() {
        for(int r=0; r<9; r++) {
            for(int c=0; c<9; c++) {
                board[r][c] = puzzles[currentPuzzleIndex][r][c];
            }
        }
    }

    public void solveBoard() {
        for(int r=0; r<9; r++) {
            for(int c=0; c<9; c++) {
                board[r][c] = solutions[currentPuzzleIndex][r][c];
            }
        }
    }
}