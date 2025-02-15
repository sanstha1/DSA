package assignment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TetrisGame extends JPanel implements ActionListener {
    private static final int ROWS = 20, COLS = 10, BLOCK_SIZE = 30;
    private int[][] board = new int[ROWS][COLS];
    private Timer timer;
    private Queue<int[][]> blockQueue = new LinkedList<>();
    private Stack<Integer> filledRows = new Stack<>();
    private int[][] currentBlock;
    private int blockRow = 0, blockCol = COLS / 2;
    private Random rand = new Random();
    private boolean gameOver = false;
    private int score = 0; // Added score tracking

    public TetrisGame() {
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
            }
        });
        generateBlock();
        timer = new Timer(500, this);
        timer.start();
    }

    private void generateBlock() {
        int[][] block = {{1, 1, 1}, {0, 1, 0}};
        blockQueue.add(block);
        currentBlock = blockQueue.poll();
        blockRow = 0; // Reset block position to top
        blockCol = COLS / 2;

        // Check if the new block can be placed at the starting position
        if (!canMove(blockRow, blockCol)) {
            gameOver = true;
            timer.stop();
            repaint();
        }
    }

    private boolean canMove(int newRow, int newCol) {
        if (gameOver) return false; // Prevent movement if game is over
        
        for (int r = 0; r < currentBlock.length; r++) {
            for (int c = 0; c < currentBlock[0].length; c++) {
                if (currentBlock[r][c] == 1) {
                    int boardRow = newRow + r, boardCol = newCol + c;
                    if (boardRow >= ROWS || boardCol < 0 || boardCol >= COLS || 
                        (boardRow >= 0 && board[boardRow][boardCol] == 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeBlock() {
        for (int r = 0; r < currentBlock.length; r++) {
            for (int c = 0; c < currentBlock[0].length; c++) {
                if (currentBlock[r][c] == 1) {
                    int boardRow = blockRow + r;
                    if (boardRow < 0) {
                        gameOver = true;
                        timer.stop();
                        repaint();
                        return;
                    }
                    board[boardRow][blockCol + c] = 1;
                }
            }
        }
        checkRows();
        generateBlock();
    }

    private void checkRows() {
        int rowsCleared = 0;
        for (int r = 0; r < ROWS; r++) {
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                filledRows.push(r);
                rowsCleared++;
            }
        }
        
        // Update score based on rows cleared
        score += rowsCleared * 100;
        
        while (!filledRows.isEmpty()) {
            int rowToClear = filledRows.pop();
            for (int r = rowToClear; r > 0; r--) {
                System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
            }
            board[0] = new int[COLS];
        }
    }

    private void handleInput(int key) {
        if (gameOver) {
            if (key == KeyEvent.VK_R) { // Reset game when 'R' is pressed
                resetGame();
                return;
            }
            return;
        }
        
        if (key == KeyEvent.VK_LEFT && canMove(blockRow, blockCol - 1)) blockCol--;
        if (key == KeyEvent.VK_RIGHT && canMove(blockRow, blockCol + 1)) blockCol++;
        if (key == KeyEvent.VK_DOWN && canMove(blockRow + 1, blockCol)) blockRow++;
        if (key == KeyEvent.VK_SPACE) { // Hard drop
            while (canMove(blockRow + 1, blockCol)) {
                blockRow++;
            }
            placeBlock();
        }
        repaint();
    }

    private void resetGame() {
        // Reset all game variables
        board = new int[ROWS][COLS];
        blockQueue.clear();
        filledRows.clear();
        gameOver = false;
        score = 0;
        blockRow = 0;
        blockCol = COLS / 2;
        generateBlock();
        timer.start();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        
        if (canMove(blockRow + 1, blockCol)) {
            blockRow++;
        } else {
            placeBlock();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the game board
        g.setColor(Color.WHITE);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 1) {
                    g.fillRect(c * BLOCK_SIZE, r * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                }
            }
        }

        // Draw the current block
        if (!gameOver) {
            g.setColor(Color.RED);
            for (int r = 0; r < currentBlock.length; r++) {
                for (int c = 0; c < currentBlock[0].length; c++) {
                    if (currentBlock[r][c] == 1) {
                        g.fillRect((blockCol + c) * BLOCK_SIZE, (blockRow + r) * BLOCK_SIZE, 
                                 BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                    }
                }
            }
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);

        // Display Game Over message and instructions
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOverMsg = "GAME OVER";
            String scoreMsg = "Final Score: " + score;
            String restartMsg = "Press 'R' to Restart";
            
            int gameOverX = (COLS * BLOCK_SIZE - g.getFontMetrics().stringWidth(gameOverMsg)) / 2;
            int scoreX = (COLS * BLOCK_SIZE - g.getFontMetrics().stringWidth(scoreMsg)) / 2;
            int restartX = (COLS * BLOCK_SIZE - g.getFontMetrics().stringWidth(restartMsg)) / 2;
            
            int centerY = (ROWS * BLOCK_SIZE) / 2;
            g.drawString(gameOverMsg, gameOverX, centerY - 40);
            g.drawString(scoreMsg, scoreX, centerY);
            g.drawString(restartMsg, restartX, centerY + 40);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        TetrisGame game = new TetrisGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}