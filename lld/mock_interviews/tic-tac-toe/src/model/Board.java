package model;

import exceptions.CellNonEmpty;
import exceptions.CellOutOfBound;
import model.enums.PlayerSymbol;

public class Board {
    private int row;
    private int col;
    private PlayerSymbol[][] grid;

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        this.grid = new PlayerSymbol[row][col];
    }

    public boolean fillCell(int row, int col, PlayerSymbol playerSymbol) {
        if (col<0 || col>=this.col || row<0 || row>=this.row) {
            throw new CellOutOfBound("selected row/col is out of bound");
        }

        if (this.grid[row][col] != null) {
            throw new CellNonEmpty("Chosen row and col are for a cell that is already occupied");
        }

        grid[row][col] = playerSymbol;
        return true;
        // do you think we should return true or method is better void
    }

    public boolean isGridFilled() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (this.grid[i][j] == null) {
                    return  false;
                }
            }
        }
        return true;
    }

    public PlayerSymbol getGridCellValue(int row, int col) {
        if (col<0 || col>=this.col || row<0 || row>=this.row) {
            throw new CellOutOfBound("selected row/col is out of bound");
        }

        return grid[row][col];
    }

    public boolean isInBound(int row, int col) {
        return row>=0 && row<this.row && col>=0 && col<this.col;
    }

    public void clearCell(int row, int col) {
        this.grid[row][col] = null;
    }
}
