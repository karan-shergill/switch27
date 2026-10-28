package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int size;
    private Map<Integer, Integer> snakesAndLadders;

    public Board(int size, List<BoardEntity> listOfSnakesAndLadders) {
        this.size = size;
        this.snakesAndLadders = new HashMap<>();

        for(BoardEntity boardEntity: listOfSnakesAndLadders) {
            snakesAndLadders.put(boardEntity.getStart(), boardEntity.getEnd());
        }
    }

    public int getSize() {
        return size;
    }

    public int getFinalPositions(int start) {
        return this.snakesAndLadders.getOrDefault(start, start);
    }

    public void displayBoard() {
        // logic to show board
    }
}
