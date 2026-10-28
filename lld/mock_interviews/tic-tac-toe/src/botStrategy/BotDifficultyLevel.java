package botStrategy;

import model.Board;
import model.Position;

public interface BotDifficultyLevel {
    public Position chooseNextMoveCell(Board board);
}
