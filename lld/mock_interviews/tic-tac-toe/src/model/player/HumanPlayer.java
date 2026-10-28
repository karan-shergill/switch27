package model.player;

import model.Board;
import model.Position;
import model.enums.PlayerSymbol;

import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name, PlayerSymbol playerSymbol, Scanner scanner) {
        super(name, playerSymbol);
        this.scanner = scanner;
    }

    @Override
    public Position chooseMove(Board board) {
        int row = this.scanner.nextInt();
        int col = this.scanner.nextInt();

        return new Position(row, col);
    }
}
