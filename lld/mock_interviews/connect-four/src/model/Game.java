package model;

import exception.*;
import model.player.Player;

import java.util.Stack;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player playerTurn;
    private Player winner;
    private GameState gameState;
    private Stack<Move> moveHistory;

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.playerTurn = player1;
        this.winner = null;
        this.gameState = GameState.IN_PROGRESS;
        this.moveHistory = new Stack<>();
    }

    public void makeMove(Player player, int col) {
        if (this.gameState != GameState.IN_PROGRESS) {
            throw new GameOverException("model.Game is already over: " + this.gameState);
        }

        if (player != this.playerTurn) {
            throw new NotYourTurnException("It is not " + player.getName() + "'s turn");
        }

        // model.Board throws on invalid/full column, so state is untouched on failure
        int row = this.board.placeDisk(col, player.getColor());

        this.moveHistory.push(new Move(player, row, col));

        if (this.board.checkWinner(row, col, player.getColor())) {
            this.winner = player;
            this.gameState = GameState.WON;
            return;
        }

        if (this.board.isFull()) {
            this.gameState = GameState.TIE;
            return;
        }

        this.playerTurn = this.playerTurn == player1 ? player2 : player1;
    }

    public void undoLastMove() {
        if (this.moveHistory.empty()) {
            throw new NoMoveToUndoException("Unable to undo, as no last move found");
        }

        Move lastMove = this.moveHistory.pop();

        this.board.clearCell(lastMove.getRow(), lastMove.getCol());

        this.playerTurn = lastMove.getPlayer();
        this.winner = null;
        this.gameState = GameState.IN_PROGRESS;
    }

    public Player getCurrentPlayer() {
        return this.playerTurn;
    }

    public Player getWinner() {
        return this.winner;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Board getBoard() {
        return this.board;
    }
}
