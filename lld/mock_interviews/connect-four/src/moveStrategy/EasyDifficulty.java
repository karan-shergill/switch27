package moveStrategy;

import model.Board;
import model.Color;

public class EasyDifficulty implements MoveStrategy{
    @Override
    public int chooseColumn(Board board, Color color) {
        // choose randomly 1st available valid column
        return 0;
    }
}
