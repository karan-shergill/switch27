package botStrategy;

import model.Board;
import model.Position;

public class HardStrategy implements BotDifficultyLevel{
    @Override
    public Position chooseNextMoveCell(Board board) {
        // find ROW and COL value for next move

        return new Position(-1, -1);
    }
}
