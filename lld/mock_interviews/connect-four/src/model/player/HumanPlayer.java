package model.player;

import model.Board;
import model.Color;

import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(Color color, String name, Scanner scanner) {
        super(color, name);
        this.scanner = scanner;
    }

    @Override
    public int chooseColumn(Board board) {
        // logic to take input via scanner from human player
        return 0;
    }
}
