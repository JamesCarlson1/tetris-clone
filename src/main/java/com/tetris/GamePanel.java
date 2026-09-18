package com.tetris;

// For Panel
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

// For timer.
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Board board;
    private int cellSize;
    private Tetromino currentPiece;
    private Timer timer;
    private boolean gameOver = false;

    private final int delayMs = 500;

    public GamePanel (Board board, int cellSize) {
        this.board = board;
        this.cellSize = cellSize;
        int width = board.grid[0].length * cellSize;
        int height = board.grid.length * cellSize;
        setPreferredSize(new Dimension(width, height));

        timer = new Timer(delayMs, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);
    }

    public void setCurrentPiece (Tetromino currentPiece) {
        this.currentPiece = currentPiece; 
    }

    public void keyPressed (KeyEvent e) {
        if (gameOver) return;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                tryMove(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                tryMove(0, +1);
                break;
            case KeyEvent.VK_DOWN:
                tryMove(1, 0);
                break;
            case KeyEvent.VK_UP:
                tryRotate(1);
                break;
        }
    }

    public void tryMove (int dRow, int dCol) {
        Tetromino candidate = new Tetromino(currentPiece.type, currentPiece.rotationIndex, currentPiece.row + dRow, currentPiece.col + dCol);
        if (board.isValidPosition(candidate)) {
            currentPiece = candidate;
            repaint();
        }
    }

    public void tryRotate (int direction) {
        int newRotationIndex = (currentPiece.rotationIndex + direction + 4) % 4; // The +4 has to be there in order to handle possible negative cases.
        Tetromino candidate = new Tetromino(currentPiece.type, newRotationIndex, currentPiece.row, currentPiece.col);
        if (board.isValidPosition(candidate)) {
            currentPiece = candidate;
            repaint();
        }
    }

    @Override
    public void keyReleased (KeyEvent e) {
    }
    @Override
    public void keyTyped (KeyEvent e) {
    }



    @Override
    public void paintComponent (Graphics g) {
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
        if (gameOver) return;

        Tetromino candidate = new Tetromino(currentPiece.type, currentPiece.rotationIndex, currentPiece.row + 1, currentPiece.col);
        if (board.isValidPosition(candidate)) {
            currentPiece = candidate;
        } else {
            board.lockPiece(currentPiece);
            board.clearFullLines();
            currentPiece = new Tetromino(TetrominoType.randomType(), 0, 0, 4);
            if (!board.isValidPosition(currentPiece)) {
                gameOver = true;
                timer.stop();
            }
        }
        repaint();
    }
}
