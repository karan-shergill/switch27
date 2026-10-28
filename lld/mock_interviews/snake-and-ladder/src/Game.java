import model.Board;
import model.Dice;
import model.GameStatus;
import model.Player;

import java.util.Queue;
import java.util.Scanner;

public class Game {
    private Queue<Player> players;
    private Board board;
    private Dice dice;
    private GameStatus gameStatus;
    private Player winner;
    private Scanner scanner;

    public Game(Queue<Player> players, Board board, Dice dice, Scanner scanner) {
        this.players = players;
        this.board = board;
        this.dice = dice;
        this.winner = null;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.scanner = scanner;
    }

    public void play() {
        while (gameStatus == GameStatus.IN_PROGRESS) {
            this.board.displayBoard();

            Player playerTurn = this.players.poll();
            System.out.println("Player turn: " + playerTurn.getName() + "Enter key to roll dice");

            scanner.nextLine();

            int diceResult = this.dice.rollDice();
            System.out.println("Dice roll value: " + diceResult);
            int total = diceResult;

            while (diceResult == 6) {
                diceResult = this.dice.rollDice();
                System.out.println("Dice roll value: " + diceResult);
                total += diceResult;

                if (diceResult != 6) {
                    break;
                }
                if (total == 24) {
                    total = 0;
                    break;
                }
            }

            int currPlayerPosition = playerTurn.getPosition();
            int newPlayerPosition = currPlayerPosition + total;

            System.out.println("currPlayerPosition: " + currPlayerPosition + " newPlayerPosition: " + newPlayerPosition);

            newPlayerPosition = board.getFinalPositions(newPlayerPosition);

            if (newPlayerPosition > this.board.getSize() * this.board.getSize()) {
                System.out.println("newPlayerPosition: " + newPlayerPosition + " is more than board size");
                newPlayerPosition = currPlayerPosition;
            }

            playerTurn.setPosition(newPlayerPosition);

            if (playerTurn.getPosition() == this.board.getSize() * this.board.getSize()) {
                this.winner = playerTurn;
                this.gameStatus = GameStatus.FINISHED;
            }

            this.players.add(playerTurn);
        }

        this.board.displayBoard();
        System.out.println("Winner of game is:" + winner.getName());
    }
}
