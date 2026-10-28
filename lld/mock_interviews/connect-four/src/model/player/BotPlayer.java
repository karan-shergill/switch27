package model.player;

import model.Board;
import model.Color;
import moveStrategy.MoveStrategy;

public class BotPlayer extends Player {
    private MoveStrategy moveStrategy;
    public BotPlayer(Color color, String name, MoveStrategy moveStrategy) {
        super(color, name);
        this.moveStrategy = moveStrategy;
    }

    @Override
    public int chooseColumn(Board board) {
        return moveStrategy.chooseColumn(board, this.getColor());
    }
}
