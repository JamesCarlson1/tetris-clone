package com.tetris;

import javax.swing.JFrame;

public class Main {
    private static final int cellSize = 25;
    private static final int rows = 20;
    private static final int cols = 10;
    public static void main(String[] args) {
        Board board = new Board(rows, cols);
        GamePanel panel = new GamePanel(board, cellSize);
        JFrame frame = new JFrame();

        panel.setCurrentPiece(new Tetromino(TetrominoType.T, 0, 0, 4));
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        
        int rotationIndex = 0;
        int row = 3;
        int col = 5;
        Tetromino piece = new Tetromino(TetrominoType.T, rotationIndex, row, col);
        int[][] cells = piece.getCells();
        for (int i = 0; i < cells.length; i++) {
            System.out.println(cells[i][0] + " " + cells[i][1]);
        }
    }
}