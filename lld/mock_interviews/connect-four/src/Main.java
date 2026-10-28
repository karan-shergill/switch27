import exception.GameException;
import model.Color;
import model.Game;
import model.GameState;
import model.player.BotPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import moveStrategy.EasyDifficulty;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player human = new HumanPlayer(Color.RED, "Human", new Scanner(System.in));
        Player bot = new BotPlayer(Color.BLUE, "Bot", new EasyDifficulty());
        Game game = new Game(human, bot);

        while (game.getGameState() == GameState.IN_PROGRESS) {
            Player current = game.getCurrentPlayer();
            int col = current.chooseColumn(game.getBoard());
            try {
                game.makeMove(current, col);
                System.out.println(current.getName() + " played column " + col);
            } catch (GameException e) {
                // a human would be re-prompted; stop here so stub strategies can't loop forever
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                break;
            }
        }

        if (game.getGameState() == GameState.WON) {
            System.out.println("Winner: " + game.getWinner().getName());
        } else if (game.getGameState() == GameState.TIE) {
            System.out.println("Game tied");
        }
    }
}
