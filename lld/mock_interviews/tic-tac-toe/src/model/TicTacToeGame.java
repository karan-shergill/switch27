package model;

import exceptions.GameOver;
import exceptions.NoLastMoveFound;
import exceptions.NotYourTurn;
import model.enums.GameState;
import model.enums.PlayerSymbol;
import model.player.Player;

import java.util.Stack;

public class TicTacToeGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player winner;
    private Player playerTurnToPlay;
    private GameState gameState;
    private Stack<Move> moveHistory;

    public TicTacToeGame(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;
        this.playerTurnToPlay = player1;
        this.gameState = GameState.IN_PROGRESS;
        this.moveHistory = new Stack<>();
    }

    public boolean makeMove(Player player, int row, int col) {
        if (player != this.playerTurnToPlay) {
            throw new NotYourTurn("Not your turn yet");
        }

        if (this.gameState != GameState.IN_PROGRESS) {
            throw new GameOver("Game seems to be over");
        }

        boolean tookMove = this.board.fillCell(row, col, player.getPlayerSymbol());

        if (tookMove) {
            this.moveHistory.push(new Move(row, col, player));
        }

        if (this.board.isGridFilled()) {
            this.gameState = GameState.TIE;
        }

        if (isWinningMove(row, col, player.getPlayerSymbol())) {
            this.winner = player;
            this.gameState = GameState.WON;
        } else {
            this.playerTurnToPlay = this.playerTurnToPlay == this.player1 ? this.player2 : this.player1;
        }

        return  tookMove;
    }

    private boolean isWinningMove(int row, int col, PlayerSymbol symbol) {
        int[][] direction = new int[][]{{0, 1}, {1, 0}, {1, 1}, {-1, 1}};

        for (int[] dir : direction) {
            int count = 1;
            count += getSymbolCount(row, col, dir[0], dir[1], symbol);
            count += getSymbolCount(row, col, -dir[0], -dir[1], symbol);

            if (count >= 3) {
                return true;
            }
        }

        return false;
    }

    private int getSymbolCount(int row, int col, int r, int c, PlayerSymbol symbol) {
        row += r;
        col += c;

        int count = 0;
        while (this.board.isInBound(row, col) && this.board.getGridCellValue(row, col)==symbol) {
            count++;
            row += r;
            col += c;
        }
        return count;
    }

    private boolean undoMove() {
        if (moveHistory.empty()) {
            throw new NoLastMoveFound("No last move found to undo");
        }

        Move lastMove = this.moveHistory.pop();

        this.board.clearCell(lastMove.getRow(), lastMove.getCol());
        this.gameState = GameState.IN_PROGRESS;
        this.winner = null;
        this.playerTurnToPlay = lastMove.getPlayer();

        return true;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getPlayerTurnToPlay() {
        return playerTurnToPlay;
    }

    public GameState getGameState() {
        return gameState;
    }
}
