import model.Board;
import model.Position;
import model.TicTacToeGame;
import model.enums.GameState;
import model.enums.PlayerSymbol;
import model.player.HumanPlayer;
import model.player.Player;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(3, 3);
        try (Scanner scanner = new Scanner(System.in)) {
            TicTacToeGame ticTacToeGame = new TicTacToeGame(
                    board,
                    new HumanPlayer("A", PlayerSymbol.X, scanner),
                    new HumanPlayer("B", PlayerSymbol.O, scanner)
            );

            while (ticTacToeGame.getGameState() == GameState.IN_PROGRESS) {
                Player currentPlayer = ticTacToeGame.getPlayerTurnToPlay();
                System.out.println(currentPlayer.getName() + " (" + currentPlayer.getPlayerSymbol()
                        + "), enter row and column (0-2):");
                Position position = currentPlayer.chooseMove(board);
                ticTacToeGame.makeMove(
                        currentPlayer,
                        position.getChosenRow(),
                        position.getChosenCol()
                );
            }

            if (ticTacToeGame.getGameState() == GameState.WON) {
                Player winner = ticTacToeGame.getWinner();
                System.out.println("Winner: " + winner.getName() + " (" + winner.getPlayerSymbol() + ")");
            } else {
                System.out.println("Game ended in a tie.");
            }
        }
    }
}