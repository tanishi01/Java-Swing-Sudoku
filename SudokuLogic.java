import java.util.Random;

public class SudokuLogic {
    private int[][] board = new int[9][9];
    private int[][] initialBoard = new int[9][9];
    private Random rand = new Random();

    public SudokuLogic() {
        loadNewGame(); // Dynamically generates a puzzle on startup
    }

    public boolean isStartingNumber(int row, int col) {
        return initialBoard[row][col] != 0;
    }

    public int getNumberAt(int row, int col) {
        return board[row][col];
    }

    // Preserved your exact constraint logic and comments!
    public boolean isValidMove(int row, int col, int number) {
        // Temporarily clear the cell to check against the rest of the board safely
        int temp = board[row][col];
        board[row][col] = 0;

        for (int i = 0; i < 9; i++) {                                //checks rows and columns to check agar number same hai ya nahi
            if (board[row][i] == number && i != col) {
                board[row][col] = temp;
                return false;
            }
            if (board[i][col] == number && i != row) {
                board[row][col] = temp;
                return false;
            }
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int r = boxRowStart; r < boxRowStart + 3; r++) {        //checks box
            for (int d = boxColStart; d < boxColStart + 3; d++) {                          
                if (board[r][d] == number && r != row && d != col) {
                    board[row][col] = temp;
                    return false;
                }
            }
        }
        board[row][col] = temp; // Restore original number
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

    // --- ALGORITHMIC ENGINE ---

    public boolean solve(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValidMove(row, col, num)) { // Uses your exact logic
                            grid[row][col] = num;
                            if (solve(grid)) {
                                return true;
                            }
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void loadNewGame() {
        board = new int[9][9];
        initialBoard = new int[9][9];

        // Seed random numbers
        for (int i = 0; i < 9; i++) {
            int num;
            do {
                num = rand.nextInt(9) + 1;
            } while (!isValidMove(0, i, num));
            board[0][i] = num;
        }

        solve(board); // Generates a full solved board

        // Remove 45 cells to create the puzzle
        int cellsToRemove = 45;
        while (cellsToRemove > 0) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                cellsToRemove--;
            }
        }

        // Save layout
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                initialBoard[r][c] = board[r][c];
            }
        }
    }

    public void resetBoard() {
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                board[r][c] = initialBoard[r][c];
            }
        }
    }

    public void solveBoard() {
        resetBoard(); 
        solve(board);
    }

    // GUI Wrappers
    public boolean solve() {
        resetBoard();
        return solve(board);
    }

    public void generateNewBoard() {
        loadNewGame();
    }
}