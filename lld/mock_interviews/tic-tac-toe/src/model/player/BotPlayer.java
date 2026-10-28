package model.player;

import botStrategy.BotDifficultyLevel;
import model.Board;
import model.Position;
import model.enums.PlayerSymbol;

public class BotPlayer extends Player {
    private BotDifficultyLevel botDifficultyLevel;

    public BotPlayer(String name, PlayerSymbol playerSymbol, BotDifficultyLevel botDifficultyLevel) {
        super(name, playerSymbol);
        this.botDifficultyLevel = botDifficultyLevel;
    }

    @Override
    public Position chooseMove(Board board) {
        return this.botDifficultyLevel.chooseNextMoveCell(board);
    }
}
