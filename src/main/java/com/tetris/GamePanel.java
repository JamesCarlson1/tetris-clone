package com.tetris;

// For Panel
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

// For timer.
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements ActionListener {
    private Board board;
    private int cellSize;
    private Tetromino currentPiece;

    private final int delayMs = 500;

    public GamePanel (Board board, int cellSize) {
        this.board = board;
        this.cellSize = cellSize;
        int width = board.grid[0].length * cellSize;
        int height = board.grid.length * cellSize;
        setPreferredSize(new Dimension(width, height));

        Timer timer = new Timer(delayMs, this);
        timer.start();
    }

    public void setCurrentPiece (Tetromino currentPiece) {
        this.currentPiece = currentPiece; 
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int r = 0; r < board.grid.length; r++) {
            for (int c = 0; c < board.grid[0].length; c++) {
                if (board.grid[r][c] != 0) {
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }
        for (int[] cell: currentPiece.getCells()) {
            int r = cell[0];
            int c = cell[1];
            g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
        }
    }

    @Override
    public void actionPerformed (ActionEvent e) {

    }
}
