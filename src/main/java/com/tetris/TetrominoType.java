package com.tetris;

public enum TetrominoType {
    // Defines the base rotation and the other rotation coords of blocks
    O(new int[][][] {
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 0
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 1 (identical)
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 2 (identical)
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}  // rotation 3 (identical)
    });



    private final int[][][] rotationOffsets;

    TetrominoType (int[][][] rotationOffsets) {
        this.rotationOffsets = rotationOffsets;
    }

    public int[][] getOffsets (int rotationIndex) {
        return rotationOffsets[rotationIndex];
    }
}
