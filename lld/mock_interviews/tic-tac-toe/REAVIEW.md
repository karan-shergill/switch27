# Tic Tac Toe LLD Review

## Verdict

**Good baseline for a 45-minute human-vs-human round.** For 8 YOE, strengthen
validation, state ownership, and input recovery before adding more patterns.
Bot algorithms are intentionally deferred.

## What works

- Runnable human-vs-human flow with turn, win, and draw handling.
- Clear separation: players choose a `Position`; the game applies the move.
- Correct coordinate handling, safe cell reads, and pluggable bot strategies.

## Fix next

| Priority | Gap | Recommended fix |
| --- | --- | --- |
| 1 | Invalid configuration can produce false wins. | Require distinct players/symbols, non-null dependencies, valid dimensions, and non-null placement symbols. |
| 2 | Public board mutations bypass game rules. | Make `Board.fillCell()` / `clearCell()` package-private; give each game exclusive board ownership. |
| 3 | Invalid input terminates the console game. | Report errors, consume malformed input, retry the same player, and exit cleanly on EOF. |

Optional polish: display the board; use `final` fields and `Deque` instead of
`Stack`. Undo restores state correctly but is private; expose it only if required.

## Interview follow-ups

| Question | Answer direction |
| --- | --- |
| Why pass the board to a human player? | One shared selection contract; implementations need not use every argument. Avoid casts in `Main`. |
| Why separate `Position` and `Move`? | Position proposes coordinates; Move records an accepted action and its player. |
| How would you support N x N and K-in-a-row? | Configure K and count contiguous symbols through the latest move along four axes. |
| What is the current complexity? | `makeMove()`: O(R*C), dominated by fullness scanning. Win detection: O(R+C). Space: O(R*C + M) for board and history. |
| How could you optimize it? | Track occupied cells for O(1) fullness detection; bound directional scans for O(K) win detection. Row totals do not prove contiguous K. |
| How would you implement an unbeatable bot? | Minimax for 3x3; supply its symbol and search a copy, not the live board. |
| How would undo/redo work? | Restore cell, turn, outcome, and counters. A new move clears redo history. Clarify one move versus a human/bot turn. |
| How would you handle concurrent moves? | Make the complete per-game transition atomic. A thread-safe history stack alone is insufficient. |

**Interview focus:** finish one working flow, explain invariants and tradeoffs,
and explicitly scope out unfinished features.
