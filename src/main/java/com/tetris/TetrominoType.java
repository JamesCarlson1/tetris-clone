package com.tetris;

import java.util.Random;

public enum TetrominoType {
    // Defines the base rotation and the other rotation coords of blocks
    O(new int[][][] {
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 0
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 1 (identical)
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}, // rotation 2 (identical)
        {{1, 1}, {1, 2}, {2, 1}, {2, 2}}  // rotation 3 (identical)
    }), // Need comma in order to separate enum constants.
    T(new int[][][] {
        {{1, 1}, {1, 2}, {1, 3}, {2, 2}}, // rotation 0
        {{1, 1}, {2, 1}, {2, 2}, {3, 1}}, // rotation 1
        {{2, 2}, {3, 1}, {3, 2}, {3, 3}}, // rotation 2
        {{1, 3}, {2, 2}, {2, 3}, {3, 3}}  // rotation 3
    }),
    I(new int[][][] {
        {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, // rotation 0
        {{1, 0}, {1, 1}, {1, 2}, {1, 3}}, // rotation 1
        {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, // rotation 2 (identical to rot0)
        {{1, 0}, {1, 1}, {1, 2}, {1, 3}}  // rotation 3 (identical to rot1)
    }),
    S(new int[][][] {
        {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
        {{0, 2}, {0, 3}, {1, 1}, {1, 2}},
        {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
        {{0, 2}, {0, 3}, {1, 1}, {1, 2}}
    }),
    Z(new int[][][] {
        {{0, 2}, {1, 1}, {1, 2}, {2, 1}},
        {{0, 1}, {0, 2}, {1, 2}, {1, 3}},
        {{0, 2}, {1, 1}, {1, 2}, {2, 1}},
        {{0, 1}, {0, 2}, {1, 2}, {1, 3}}
    }),
    J(new int[][][] {
        {{0, 2}, {1, 2}, {2, 1}, {2, 2}},
        {{0, 1}, {1, 1}, {1, 2}, {1, 3}},
        {{0, 1}, {0, 2}, {1, 1}, {2, 1}},
        {{0, 1}, {0, 2}, {0, 3}, {1, 3}}
    }),
    L(new int[][][] {
        {{0, 1}, {1, 1}, {2, 1}, {2, 2}},
        {{0, 1}, {0, 2}, {0, 3}, {1, 1}},
        {{0, 1}, {0, 2}, {1, 2}, {2, 2}},
        {{0, 3}, {1, 1}, {1, 2}, {1, 3}}
    });

    private final int[][][] rotationOffsets;

    TetrominoType (int[][][] rotationOffsets) {
        this.rotationOffsets = rotationOffsets;
    }

    private static Random random = new Random();

    public static TetrominoType randomType() {
        TetrominoType[] allTypes = values();
        return allTypes[random.nextInt(allTypes.length)];
    }

    public int[][] getOffsets (int rotationIndex) {
        return rotationOffsets[rotationIndex];
    }
}
