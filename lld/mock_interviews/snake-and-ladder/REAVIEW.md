# Snake and Ladder: Review

## Summary

Good entity separation and queue-based turns. Initialization, position updates,
and ordinary overshoots are fixed; the game now completes.

## Fix first

| Priority | Issue | Action |
|---|---|---|
| High | `Game.java:35-50`: forfeiture happens after four sixes, not three. | Count consecutive sixes explicitly; stop at three. |
| High | `Game.java:39-70`: at 94, rolling `6, 1` misses the win at 100. | Resolve movement and victory after each roll under the stated win rule; document any aggregate-turn variant. |
| Medium | `Board.java:15-16`: duplicate starts at 10 and 20 silently replace snakes with ladders. | Reject duplicate starts and fix the sample board. |
| Medium | `Board.java:11-16`, `Game.java:53-62`: invalid endpoints can turn an overshoot into a win. | Validate board bounds; check overshoot before entity lookup. |
| Medium | `Game.java:17-31`: empty/single-player games are accepted. | Require at least two players and non-null dependencies/players. |
| Medium | `Dice.java:16`: `(3, 6)` produces 3 through 8. | Use `nextInt(maxValue - minValue + 1) + minValue` after validating the small dice range. |

## Design feedback

- Extract `playTurn()` and keep console interaction separate from game rules.
- Define three-sixes forfeiture, chained entities, and entities starting at 100.
- If chaining is supported, detect cycles; the sample contains `20 -> 33 -> 20`.
- Add scripted-dice tests for bonus rolls, immediate wins, overshoots, and invalid configuration.
- Keep the design simple; no extra patterns are needed yet.

## Validation from the latest review

Compilation and a `Main` smoke test passed. Scripted checks verified normal
gameplay and reproduced the turn/configuration defects above; the dice issue
was established from its formula. Three exception serialization warnings remain.
Implementation files were unchanged.

## Interview follow-up questions

1. **Multiple dice:** Support two dice instead of one. What changes?
2. **Rankings:** Continue after the first winner until all but one player finish; display rankings.
3. **Entry rule:** Add an optional rule requiring a six before entering the board.
4. **Crocodile cell:** Landing on it makes the player skip their next turn.
5. **Save/resume:** Allow players to pause a game and resume it later.