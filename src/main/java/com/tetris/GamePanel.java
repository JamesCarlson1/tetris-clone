package com.tetris;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

public class GamePanel extends JPanel {
    private Board board;
    private int cellSize;
    private Tetromino currentPiece;

    public GamePanel (Board board, int cellSize) {
        this.board = board;
        this.cellSize = cellSize;
        int width = board.grid[0].length * cellSize;
        int height = board.grid.length * cellSize;
        setPreferredSize(new Dimension(width, height));
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
}
