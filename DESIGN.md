# Tetris Clone — Design Notes

Reference doc for building this yourself. It covers architecture, data
structures, and algorithms as pseudocode/specs — no working Java, so you
write and understand every line of actual game logic.

## 1. Class layout

```
com.tetris
├── Main.java              — creates the JFrame, adds GamePanel, starts it
├── GamePanel.java          — extends JPanel; owns the game loop, rendering,
│                             key listener wiring, and current game state
├── Board.java              — the grid: cell storage, collision checks,
│                             locking, line-clear logic
├── Tetromino.java          — a piece: its type, current rotation, position,
│                             and the shape data for all 7 pieces
├── TetrominoType.java      — enum: I, O, T, S, Z, J, L (+ color per type)
└── InputHandler.java       — (optional) KeyListener/KeyAdapter that
                              translates keypresses into game actions
```

You could merge InputHandler into GamePanel — splitting it out just keeps
`GamePanel` from doing too much.

## 2. The board

- Store as `int[ROWS][COLS]` (or an enum type per cell). `0` = empty,
  any other value = which tetromino color occupies that cell.
- Standard dimensions: **10 columns × 20 rows** (some implementations add
  a few hidden rows above row 0 for spawning — optional).
- `Board` should expose something like:
  - `boolean isCellFree(int row, int col)`
  - `boolean isValidPosition(Tetromino piece)` — checks all 4 occupied
    cells of the piece against board bounds + existing filled cells
  - `void lockPiece(Tetromino piece)` — copies the piece's cells into
    the grid permanently
  - `int clearFullLines()` — finds and removes full rows, returns how
    many were cleared (used for scoring)

Keep `Board` ignorant of *which* piece is currently falling — it just
answers "is this position legal" and "commit these cells." That keeps
collision logic in one place instead of duplicated between movement and
rotation code.

## 3. Tetromino shapes

Each piece has 4 rotation states. The cleanest representation: for each
type and rotation, a list of 4 `(row, col)` offsets relative to the
piece's origin. Example shape data (this is standard Tetris geometry, not
"the game logic" — you still have to write the code that *uses* it):

```
I: rotation 0: (1,0) (1,1) (1,2) (1,3)
   rotation 1: (0,2) (1,2) (2,2) (3,2)
   (rotations 2 and 3 mirror 0 and 1 for I, O, S, Z, T-ish pieces —
    look up the "Super Rotation System" (SRS) spec for exact values,
    or design your own simpler 4x4-grid-per-rotation scheme)

O: single rotation, occupies a fixed 2x2 block

T, S, Z, J, L: 4 distinct rotations each
```

Two implementation strategies, pick one:
- **Coordinate offsets** (above) — compact, but you hand-encode 4 rows ×
  4 rotations of numbers.
- **4×4 boolean grids per rotation** — easier to visualize/debug (you can
  literally draw the shape as `X` and `.` in a comment), slightly more
  memory.

Either is fine for a clone. Look up "SRS tetromino rotation states" if
you want the exact rotations used in modern official Tetris; otherwise
just make sure each shape's 4 rotations look right to the eye and rotate
around a sensible pivot.

## 4. Rendering (Swing)

- `GamePanel extends JPanel`, override `paintComponent(Graphics g)`.
  - Always call `super.paintComponent(g)` first.
  - Cast to `Graphics2D` if you want nicer strokes/antialiasing.
  - Loop over the board grid, draw a filled rectangle per non-empty
    cell (`g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE)`).
  - Then draw the *currently falling* piece on top the same way, using
    its live position (it isn't in the board grid until it locks).
  - Optionally draw grid lines, a "next piece" preview panel, and
    score/level text.
- Swing panels are double-buffered by default, so you don't need to
  manage an offscreen buffer yourself — just call `repaint()` whenever
  state changes and let Swing handle redraw timing.
- Set the panel's preferred size (`COLS * CELL_SIZE` × `ROWS * CELL_SIZE`,
  plus room for a sidebar if you add one) and pack the `JFrame` around it.

## 5. The game loop

Two common approaches:

**A. `javax.swing.Timer` (simplest, recommended for a first pass)**
- A `Timer(delayMillis, actionListener)` fires on the Swing Event
  Dispatch Thread (EDT) at a fixed interval.
- Each tick: try to move the current piece down one row; if that's
  illegal, lock the piece instead (see §6).
- Call `repaint()` after every tick and after every input-driven move.
- Speed up the game by calling `timer.setDelay(newDelay)` as the level
  increases.
- Because it runs on the EDT, your tick handler and your key handler
  never race each other — no synchronization needed. This is the main
  reason it's the easier option to start with.

**B. Manual thread loop with sleep**
- A separate thread loops: sleep, update state, call
  `SwingUtilities.invokeLater(this::repaint)`.
- More flexible (e.g. variable timestep, soft-drop speed independent of
  gravity tick) but you now must guard shared state (the board, current
  piece) against concurrent access from the input thread and the EDT.
- Only worth it once (A) feels limiting.

Start with A. You can always refactor later.

## 6. Movement, collision, and locking

Pseudocode for the core tick:

```
onTick():
    if canMove(currentPiece, dy=+1):
        currentPiece.moveDown()
    else:
        board.lockPiece(currentPiece)
        linesCleared = board.clearFullLines()
        updateScore(linesCleared)
        currentPiece = nextPiece
        nextPiece = randomPiece()
        if not board.isValidPosition(currentPiece):
            gameOver()
    repaint()
```

`canMove(piece, dRow, dCol)` and rotation both funnel through the same
check: compute the piece's *would-be* cells, ask
`board.isValidPosition(thoseCells)`, and only commit the move if true.
Never mutate the piece's position/rotation before confirming legality —
compute a tentative version first, validate, then apply.

**Rotation** is the trickiest part: naive rotation near walls or other
pieces can leave a piece overlapping something illegal. The simplest
fix ("no kick"): if the rotated position is invalid, just don't rotate.
A nicer fix ("wall kick"): if the naive rotation is invalid, try shifting
the rotated piece left/right by 1 (and up/down by 1) and accept the first
shift that becomes valid. Start with "no kick" — add wall kicks later
once the basics work.

## 7. Line clearing

```
clearFullLines():
    fullRows = [r for r in 0..ROWS-1 if all(grid[r][c] != 0 for c in cols)]
    for r in fullRows (process from bottom to top, or rebuild the grid):
        remove row r
        insert a new empty row at the top
    return len(fullRows)
```

Easiest correct implementation: build a new grid by copying over every
row that *isn't* full, then pad the top with empty rows to restore the
original row count. Avoids off-by-one bugs from shifting rows in place.

## 8. Input handling

- Use a `KeyListener` (or better, `KeyAdapter` so you only override what
  you need) added to the `GamePanel` — make sure the panel is focusable
  (`setFocusable(true)`) and has focus, or key events won't arrive.
- Typical bindings: Left/Right = shift, Down = soft drop (speed up
  descent), Up = rotate, Space = hard drop (instantly drop + lock), P =
  pause.
- Each key handler should go through the same `canMove`/`isValidPosition`
  checks as the tick handler — don't special-case input movement vs.
  gravity movement.
- Watch out for **key repeat** — holding a key generates many
  `keyPressed` events; decide if you want that raw behavior (fast DAS-like
  movement) or want to debounce it.

## 9. Scoring & leveling (classic-ish values, adjust to taste)

| Lines cleared at once | Points  |
|---|---|
| 1 | 100 × level |
| 2 | 300 × level |
| 3 | 500 × level |
| 4 (Tetris) | 800 × level |

- Track total lines cleared; every N lines (commonly 10), increment
  `level` and reduce the timer delay (e.g. `delay = max(100, 1000 - level*75)`).

## 10. Game over

- After spawning a new piece, if its spawn position is already invalid
  (collides with existing locked cells), the game is over.
- Stop the timer, show a "Game Over" message (simplest: swap to a
  different render state in `paintComponent`, or pop a `JOptionPane`).

## Suggested build order (test after each step)

1. `Board` + a hardcoded single piece that just falls with a `Timer`,
   rendered as colored squares. No rotation, no input yet — confirm
   gravity and rendering work.
2. Add left/right/rotate input, still no locking (piece falls through
   the floor and respawns at top when it hits bottom — a temporary
   stand-in for locking).
3. Add real locking + collision against previously-locked cells.
4. Add line-clear detection + scoring.
5. Add next-piece preview, level speed-up, game over screen, pause.
6. Polish: wall kicks, hold piece, ghost piece, sound, etc. (optional
   stretch goals once the core loop is solid).

Work through each stage and I'll review your code and help debug as you
go — happy to explain any Swing/Java behavior that surprises you along
the way.
