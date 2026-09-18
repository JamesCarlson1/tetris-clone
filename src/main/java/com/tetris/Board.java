package com.tetris;

public class Board {
    public int[][] grid;

    public Board (int row, int col) {
        this.grid = new int[row][col];
    }

    // Checks if the cell is free on the board.
    public boolean isCellFree (int row, int col) {
        return false;
    }

    // Checks if the piece is a valid position on the board.
    public boolean isValidPosition (Tetromino piece) {
        int[][] cells = piece.getCells();
        for (int[] cell: cells) {
            int r = cell[0];
            int c = cell[1];
            if (r < 0 || r >= grid.length) {
                return false;
            }
            if (c < 0 || c >= grid[0].length) {
                return false;
            }
            if (grid[r][c] != 0) {
                return false;
            }
        }
        return true;
    }

    // Locks the piece in place on the board.
    public void lockPiece (Tetromino piece) {

    }

    // Clears the row if it is full.
    public int clearFullLines() {
        return 5;
    }
}
