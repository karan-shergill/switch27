# Connect Four – LLD Review

## Verdict
Solid, correct, interview-ready baseline. Clean separation (model.Board / model.Game / model.player.Player), correct win detection (only checks around the last move, O(1)), correct tie/turn handling. Remaining feedback is mostly about extensibility, error signalling, and missing pieces interviewers usually probe.

## What's good
- Win check is local to the last placed disk using direction vectors, with both directions counted (`1 + forward + backward >= 4`). Efficient and compact.
- `model.Game` owns state transitions (`IN_PROGRESS/WON/TIE`) and rejects moves after game end or out of turn.
- Win is checked before tie, so a winning last move is not misreported as a tie.
- Gravity logic (bottom-up scan) and `isFull` (check top row only) are correct.

## Issues / improvements

### 1. Error handling via `System.out.println` + magic return values (High)
`model.Board.canPlace`/`placeDisk` print to stdout and return `-1`; `model.Game.makeMove` returns `false` with no reason. The model layer should not do I/O, and callers can't tell *why* a move failed (wrong turn, game over, bad column, full column).
- Throw domain exceptions (`InvalidMoveException`, `ColumnFullException`) or return a `MoveResult` enum (`OK, NOT_YOUR_TURN, GAME_OVER, INVALID_COLUMN, COLUMN_FULL`).
- Remove printing from `model.Board`.

### 2. Redundant validation (Low)
`placeDisk` re-checks bounds, then calls `canPlace` (which checks bounds again and scans the column). Simplify: validate once, and use `grid[0][col] != null` for "full" (O(1)) instead of scanning the column.

### 3. model.Board dimensions and win length are hardcoded (Medium)
`ROW`, `COL` are instance `final` fields (should be `private static final`, and constants are conventionally named in UPPER_CASE – fine – but static). `4` is a magic number. Interviewers often ask "what if the board is 8x8 or connect-5?" → take `rows, cols, winLength` in the constructor (or a `GameConfig`).

### 4. Leaky encapsulation (Medium)
`model.Board.getGrid()` exposes the mutable internal array; anyone can alter the board and bypass rules. Remove it (you already have `getCell`) or return a copy. `model.Game.getBoard()` also exposes the mutable `model.Board`; consider a read-only view/interface.

### 5. model.player.Player identity and model.Color coupling (Medium)
- `model.Game` compares players by reference (`player != playerTurn`). Works, but a different `model.player.Player` object with the same name/colour is rejected; consider `equals` or player IDs.
- Constructor doesn't validate: both players same colour, null players, same instance. Add checks.
- `model.Color` enum is really a "disc/piece type"; naming it `Disc`/`Piece` avoids confusion with UI colours. Using `null` for an empty cell is OK but fragile; an `EMPTY` value (or `Optional`) is more explicit and avoids NPE-style bugs.

### 6. `model.Game` details (Low)
- `model.Game` already knows `player.getColor()`; fine, but `makeMove(model.player.Player, col)` could be `makeMove(col)` for the current player, since the `model.player.Player` arg is only used for a turn check. Keep it if you want to model "who is trying to move" (multi-client scenario) – be ready to justify.
- `winner` is redundant with `gameState`+`playerTurn`; fine, but `this.winner = null` in the tie branch is a no-op.
- Fields that never change should be `final` (`board`, `player1`, `player2`).
- Minor style: stray double spaces (`return  false;`, `return  true;`), inconsistent spacing (`col +=nextColInc`, `i=0`).

### 7. Missing features interviewers commonly ask about (Medium)
- **Undo / move history** – store a `model.Move(player, row, col)` stack; undo just clears the cell and reverts turn/state. Your design supports it easily.
- **Reset / new game**.
- **Observer/event listeners** for UI (move made, game won).
- **Pluggable win strategy** (Strategy pattern) if variants are asked for.
- **Bot / AI player** → make `model.player.Player` an interface/abstract class with `HumanPlayer` and `BotPlayer`.
- **Thread-safety** if two clients can call `makeMove` concurrently: make `makeMove` `synchronized`.
- **model.Board display** (`toString`/`print`) for demo and debugging.

### 8. Tests / driver (High for a mock)
No `Main` or tests. Add a driver and unit tests for: horizontal / vertical / both diagonals wins, win at board edge, win on the very last cell (must be WON not TIE), full column rejected, move after game over, wrong turn, out-of-range column, full-board tie.

## Suggested refactor sketch
```java
public enum MoveResult { OK, NOT_YOUR_TURN, GAME_OVER, INVALID_COLUMN, COLUMN_FULL }

public class model.Board {
    private final int rows, cols, winLength;
    private final Disc[][] grid;
    ...
    public int dropDisc(int col, Disc d) { // throws on invalid, returns row
        if (col < 0 || col >= cols) throw new IllegalArgumentException("Invalid column");
        if (grid[0][col] != null) throw new IllegalStateException("Column full");
        ...
    }
}
```

## Priority summary
1. Remove `System.out` / `-1` signalling → exceptions or `MoveResult`.
2. Parameterise rows/cols/win length; make constants static.
3. Stop exposing the mutable grid.
4. Add a driver + tests for edge cases.
5. Be ready to discuss undo, bot players, and concurrency as extensions.
