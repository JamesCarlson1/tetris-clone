package com.tetris;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        int[][] testGrid = new int[10][5];
        Board board = new Board(testGrid);

        Tetromino piece1 = new Tetromino(TetrominoType.T, 0, 0, 0);
        System.out.println(board.isValidPosition(piece1));

        Tetromino piece2 = new Tetromino(TetrominoType.T, 0, 0, -2);
        System.out.println(board.isValidPosition(piece2));

        Tetromino piece3 = new Tetromino(TetrominoType.T, 0, 3, 0);
        System.out.println(board.isValidPosition(piece3));

        testGrid[2][2] = 1;
        Tetromino piece4 = new Tetromino(TetrominoType.T, 0, 0, 0);
        System.out.println(board.isValidPosition(piece4));

        JFrame frame = new JFrame();

        frame.add(new GamePanel(500, 500));
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