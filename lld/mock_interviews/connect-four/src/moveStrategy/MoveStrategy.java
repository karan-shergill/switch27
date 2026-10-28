package moveStrategy;

import model.Board;
import model.Color;

public interface MoveStrategy {
    public int chooseColumn(Board board, Color color);
}
