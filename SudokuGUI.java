import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI {
    private JFrame frame;
    private JTextField[][] cells; 
    private SudokuLogic logic; 

    public SudokuGUI() {
        logic = new SudokuLogic();
        frame = new JFrame("Sudoku Final Project");
        frame.setSize(600, 700); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // --- TOP: THE TITLE ---
        JLabel titleLabel = new JLabel("The Sudoku", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(new Color(200, 0, 0)); 
        frame.add(titleLabel, BorderLayout.NORTH);

        // --- CENTER: THE GRID ---
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        cells = new JTextField[9][9];
        Font numberFont = new Font("SansSerif", Font.BOLD, 24);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(numberFont);

                // Add Keyboard typing logic
                final int r = row;
                final int c = col;
                cells[row][col].addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        String input = cells[r][c].getText();
                        if (input.matches("[1-9]")) { 
                            int num = Integer.parseInt(input);
                            if (logic.isValidMove(r, c, num)) {
                                cells[r][c].setForeground(Color.BLUE); 
                                logic.updateBoard(r, c, num);
                                
                                if (logic.isBoardFull()) {
                                    JOptionPane.showMessageDialog(frame, "🎉 Congratulations! You solved the Sudoku!");
                                }
                            } else {
                                cells[r][c].setForeground(Color.RED); 
                                logic.updateBoard(r, c, 0); 
                            }
                        } else {
                            cells[r][c].setText(""); 
                            logic.updateBoard(r, c, 0); 
                        }
                    }
                });

                // Checkboard coloring
                if (((row / 3) + (col / 3)) % 2 == 0) {
                    cells[row][col].setBackground(new Color(220, 220, 220));
                } else {
                    cells[row][col].setBackground(Color.WHITE);
                }
                gridPanel.add(cells[row][col]);
            }
        }
        frame.add(gridPanel, BorderLayout.CENTER);

        // Load the numbers into the visual grid!
        refreshBoardVisuals();

        // --- BOTTOM: THE BUTTONS ---
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton checkBtn = new JButton("Check Answer");
        JButton solveBtn = new JButton("Solve for me");
        JButton newGameBtn = new JButton("New Game");

        solveBtn.addActionListener(e -> {
            logic.solveBoard();
            refreshBoardVisuals(); // This now resets the red colors!
            JOptionPane.showMessageDialog(frame, "The board has been solved for you!");
        });

        newGameBtn.addActionListener(e -> {
            logic.loadNewGame(); // Load a brand new puzzle
            refreshBoardVisuals(); // Redraw it
        });

        checkBtn.addActionListener(e -> {
            boolean isFull = true;
            boolean hasErrors = false;

            // Scan the visual board to see exactly what the user sees
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    String text = cells[r][c].getText();
                    
                    if (text.equals("")) {
                        isFull = false; // Found a truly empty box
                    } else if (cells[r][c].getForeground() == Color.RED) {
                        hasErrors = true; // Found a box with a red mistake
                    }
                }
            }

            // Show the correct message based on what we found
            if (!isFull) {
                JOptionPane.showMessageDialog(frame, "The board is not completely filled yet. Keep going!");
            } else if (hasErrors) {
                JOptionPane.showMessageDialog(frame, "The board is full, but you have some red mistakes. Fix them to win!");
            } else {
                JOptionPane.showMessageDialog(frame, "🎉 Everything looks correct! You win!");
            }
        });

        controlPanel.add(checkBtn);
        controlPanel.add(solveBtn);
        controlPanel.add(newGameBtn);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // This method now perfectly handles color resets and locking/unlocking boxes
    private void refreshBoardVisuals() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int num = logic.getNumberAt(r, c);
                
                if (logic.isStartingNumber(r, c)) {
                    // It's a hardcoded starting number: Lock it and make it black
                    cells[r][c].setText(String.valueOf(num));
                    cells[r][c].setEditable(false);
                    cells[r][c].setForeground(Color.BLACK);
                } else {
                    // It's an empty box: Unlock it and make it blue!
                    cells[r][c].setEditable(true);
                    cells[r][c].setForeground(Color.BLUE); // This fixes the Red bug!
                    
                    if (num != 0) {
                        cells[r][c].setText(String.valueOf(num));
                    } else {
                        cells[r][c].setText("");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new SudokuGUI();
    }
}