import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SudokuGUI extends JFrame {
    private SudokuLogic logic;
    private JTextField[][] cells = new JTextField[9][9];
    private JButton solveBtn;
    private JButton newGameBtn;
    private JButton clearBtn;  
    private JButton submitBtn; 
    
    // Tracker to count how many times the user clicks "ATE the puzzle"
    private int submitClickCount = 0;

    public SudokuGUI() {
        logic = new SudokuLogic();

        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 580); 
        setLayout(new BorderLayout());
        
        // ENABLE FULL SCREEN
        setResizable(true);

        initBoardPanel();
        initButtonPanel();
        refreshBoardVisuals();

        setLocationRelativeTo(null); 
    }

    private void initBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(9, 9));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        Color sudokuBg = Color.decode("#FFC94D");
        boardPanel.setBackground(sudokuBg);

        Font cellFont = new Font("SansSerif", Font.BOLD, 20);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(cellFont);
                cells[row][col].setBackground(sudokuBg);
                
                int top = (row % 3 == 0) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;
                int bottom = (row == 8) ? 2 : 0;
                int right = (col == 8) ? 2 : 0;
                cells[row][col].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                ((AbstractDocument) cells[row][col].getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                        
                        if (newText.isEmpty() || (newText.length() == 1 && newText.matches("[1-9]"))) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });

                cells[row][col].getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) { checkColors(); }
                    @Override
                    public void removeUpdate(DocumentEvent e) { checkColors(); }
                    @Override
                    public void changedUpdate(DocumentEvent e) { checkColors(); }

                    private void checkColors() {
                        SwingUtilities.invokeLater(() -> validateBoardColors());
                    }
                });

                final int r = row;
                final int c = col;
                cells[row][col].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        int nextRow = r;
                        int nextCol = c;

                        if (e.getKeyCode() == KeyEvent.VK_UP && r > 0) {
                            nextRow--;
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && r < 8) {
                            nextRow++;
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && c > 0) {
                            nextCol--;
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && c < 8) {
                            nextCol++;
                        }

                        cells[nextRow][nextCol].requestFocus();
                    }
                });

                boardPanel.add(cells[row][col]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void initButtonPanel() {
        JPanel buttonPanel = new JPanel();
        
        newGameBtn = new JButton("New Game");
        solveBtn = new JButton("Answer");
        clearBtn = new JButton("Clear");
        submitBtn = new JButton("ATE the Puzzle"); 

        Color babyPink = Color.decode("#FA6781");
        Color textTan = Color.decode("#FAE7CB");
        Font btnFont = new Font("SansSerif", Font.BOLD, 14);
        
        JButton[] buttons = {newGameBtn, solveBtn, clearBtn, submitBtn};
        for (JButton btn : buttons) {
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
            btn.setBackground(babyPink);
            btn.setForeground(textTan);
            btn.setFont(btnFont);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); 
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // --- BUTTON ACTION LISTENERS ---
        
        solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                syncGuiToLogic(); 
                if (logic.solve()) {
                    refreshBoardVisuals();
                    ImageIcon solveGif = new ImageIcon("solve.gif");
                    JOptionPane.showMessageDialog(SudokuGUI.this, 
                        "Board Solved.", 
                        "Complete", 
                        JOptionPane.INFORMATION_MESSAGE, 
                        solveGif);
                } else {
                    JOptionPane.showMessageDialog(SudokuGUI.this, "This puzzle cannot be solved based on current inputs.", "Unsolvable", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.generateNewBoard(); 
                refreshBoardVisuals();
                submitClickCount = 0; 
            }
        });
        
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.resetBoard(); 
                refreshBoardVisuals();
            }
        });

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitClickCount++; 
                syncGuiToLogic(); 

                if (!logic.isBoardFull()) {
                    JOptionPane.showMessageDialog(SudokuGUI.this, "The board is not complete yet! Fill in all empty spaces.", "Incomplete Board", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean isCorrect = true;
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        int val = logic.getNumberAt(r, c);
                        if (!logic.isValidMove(r, c, val)) {
                            isCorrect = false;
                            break;
                        }
                    }
                }

                if (isCorrect) {
                    if (submitClickCount >= 2) {
                        showCelebrationPage("ATE AND LEFT NO CRUMBS", "");
                    } else {
                        showCelebrationPage("CONGRATULATIONS!", "You have successfully solved the Sudoku puzzle!");
                    }
                } else {
                    showFailurePage("Math isn't mathing.", "Try again.");
                }
            }
        });

        // --- SWAPPED POSITIONS HERE ---
        buttonPanel.add(newGameBtn);
        buttonPanel.add(submitBtn); // Swapped: "ATE the Puzzle" is now second
        buttonPanel.add(clearBtn);
        buttonPanel.add(solveBtn);  // Swapped: "Solve" is now last
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void validateBoardColors() {
        syncGuiToLogic(); 

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!logic.isStartingNumber(r, c)) {
                    String text = cells[r][c].getText().trim();
                    if (text.isEmpty()) {
                        cells[r][c].setForeground(Color.BLUE);
                    } else {
                        int val = Integer.parseInt(text);
                        if (logic.isValidMove(r, c, val)) {
                            cells[r][c].setForeground(Color.BLUE); 
                        } else {
                            cells[r][c].setForeground(Color.RED);  
                        }
                    }
                }
            }
        }
    }

    private void showCelebrationPage(String mainTitle, String subtext) {
        JDialog celebDialog = new JDialog(this, "Victory!", true);
        celebDialog.setSize(500, 450); 
        celebDialog.setLayout(new BorderLayout());
        celebDialog.getContentPane().setBackground(new Color(240, 255, 240));

        JLabel textLabel = new JLabel("<html><center><br><h2 style='color:#1e7b1e; margin:0;'>" + mainTitle + "</h2>"
                + "<p style='font-size:13px; color:#333;'>" + subtext + "</p></center></html>", SwingConstants.CENTER);

        ImageIcon gifIcon = new ImageIcon("success.gif");
        JLabel imageLabel = new JLabel();
        
        if (gifIcon.getIconWidth() == -1) {
            imageLabel.setText("🏆");
            imageLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        } else {
            imageLabel.setIcon(gifIcon);
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton dismissBtn = new JButton("Woohoo!");
        dismissBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        dismissBtn.setFocusPainted(false);
        dismissBtn.addActionListener(e -> celebDialog.dispose());

        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        actionPanel.add(dismissBtn);

        celebDialog.add(textLabel, BorderLayout.NORTH);
        celebDialog.add(imageLabel, BorderLayout.CENTER);
        celebDialog.add(actionPanel, BorderLayout.SOUTH);

        celebDialog.setLocationRelativeTo(this);
        celebDialog.setVisible(true);
    }

    private void showFailurePage(String mainTitle, String subtext) {
        JDialog failDialog = new JDialog(this, "Try Again!", true);
        failDialog.setSize(500, 450); 
        failDialog.setLayout(new BorderLayout());
        failDialog.getContentPane().setBackground(new Color(255, 240, 240)); 

        JLabel textLabel = new JLabel("<html><center><br><h2 style='color:#b22222; margin:0;'>" + mainTitle + "</h2>"
                + "<p style='font-size:13px; color:#333;'>" + subtext + "</p></center></html>", SwingConstants.CENTER);

        ImageIcon gifIcon = new ImageIcon("wrong.gif");
        JLabel imageLabel = new JLabel();
        
        if (gifIcon.getIconWidth() == -1) {
            imageLabel.setText("❌");
            imageLabel.setFont(new Font("Serif", Font.PLAIN, 70));
        } else {
            imageLabel.setIcon(gifIcon);
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton dismissBtn = new JButton("Let me try again");
        dismissBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        dismissBtn.setFocusPainted(false);
        dismissBtn.addActionListener(e -> failDialog.dispose());

        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        actionPanel.add(dismissBtn);

        failDialog.add(textLabel, BorderLayout.NORTH);
        failDialog.add(imageLabel, BorderLayout.CENTER);
        failDialog.add(actionPanel, BorderLayout.SOUTH);

        failDialog.setLocationRelativeTo(this);
        failDialog.setVisible(true);
    }

    private void refreshBoardVisuals() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = logic.getNumberAt(row, col);
                
                if (value == 0) {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                    cells[row][col].setForeground(Color.BLUE);
                } else {
                    cells[row][col].setText(String.valueOf(value));
                    if (logic.isStartingNumber(row, col)) {
                        cells[row][col].setEditable(false);
                        cells[row][col].setForeground(Color.BLACK);
                    } else {
                        cells[row][col].setEditable(true);
                        cells[row][col].setForeground(Color.BLUE); 
                    }
                }
            }
        }
    }

    private void syncGuiToLogic() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = cells[row][col].getText().trim();
                if (!text.isEmpty()) {
                    logic.updateBoard(row, col, Integer.parseInt(text));
                } else {
                    logic.updateBoard(row, col, 0);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new SudokuGUI().setVisible(true);
        });
    }
}