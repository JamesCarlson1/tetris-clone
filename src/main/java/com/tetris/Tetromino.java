package com.tetris;

public class Tetromino {
    public TetrominoType type;
    public int rotationIndex;
    public int row;
    public int col;

    public Tetromino (TetrominoType type, int rotationIndex, int row, int col) {
        this.type = type;
        this.rotationIndex = rotationIndex;
        this.row = row;
        this.col = col;
    }

    public int[][] getCells () {
        int[][] offsets = this.type.getOffsets(this.rotationIndex);
        int[][] cells = new int[4][2];
        for (int i = 0; i <= 3; i++) {
            cells[i][0] = this.row + offsets[i][0];
            cells[i][1] = this.col + offsets[i][1];
        }
        return cells;
    }
}
