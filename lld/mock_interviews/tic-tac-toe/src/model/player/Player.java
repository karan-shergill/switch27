package model.player;

import model.Board;
import model.Position;
import model.enums.PlayerSymbol;

public abstract class Player {
    private String name;
    private PlayerSymbol playerSymbol;

    public Player(String name, PlayerSymbol playerSymbol) {
        this.name = name;
        this.playerSymbol = playerSymbol;
    }

    public abstract Position chooseMove(Board board);

    public String getName() {
        return name;
    }

    public PlayerSymbol getPlayerSymbol() {
        return playerSymbol;
    }
}
