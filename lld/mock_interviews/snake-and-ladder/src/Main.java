import model.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // players
        Player player1 = new Player("A");
        Player player2 = new Player("B");
        Queue<Player> players = new LinkedList<>();
        players.add(player1);
        players.add(player2);

        // snakes
        BoardEntity snake1 = new Snake(10, 1);
        BoardEntity snake2 = new Snake(20, 10);
        BoardEntity snake3 = new Snake(33,20);
        BoardEntity snake4 = new Snake(45, 13);
        BoardEntity snake5 = new Snake(99, 1);

        // ladders
        BoardEntity ladder1 = new Ladder(10, 80);
        BoardEntity ladder2 = new Ladder(40, 45);
        BoardEntity ladder3 = new Ladder(15, 55);
        BoardEntity ladder4 = new Ladder(20, 33);
        BoardEntity ladder5 = new Ladder(1, 11);

        List<BoardEntity> boardEntityList = new ArrayList<>();
        boardEntityList.add(snake1);
        boardEntityList.add(snake2);
        boardEntityList.add(snake3);
        boardEntityList.add(snake4);
        boardEntityList.add(snake5);
        boardEntityList.add(ladder1);
        boardEntityList.add(ladder2);
        boardEntityList.add(ladder3);

        boardEntityList.add(ladder4);
        boardEntityList.add(ladder5);

        Game game = new Game(players, new Board(10, boardEntityList), new Dice(1, 6), new Scanner(System.in));
        game.play();
    }
}