package model;

public class Position {
    private int chosenRow;
    private int chosenCol;

    public Position(int chosenRow, int chosenCol) {
        this.chosenCol = chosenCol;
        this.chosenRow = chosenRow;
    }

    public int getChosenRow() {
        return chosenRow;
    }

    public int getChosenCol() {
        return chosenCol;
    }
}
