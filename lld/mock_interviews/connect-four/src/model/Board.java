package model;

import exception.*;

public class Board {
    private final int ROW = 6;
    private final int COL = 7;
    private Color[][] grid;

    public Board() {
        this.grid = new Color[ROW][COL];
    }

    public Color getCell(int row, int col) {
        return  grid[row][col];
    }

    public boolean canPlace(int col) {
        return col >= 0 && col < COL && this.grid[0][col] == null;
    }

    public int placeDisk(int col, Color color) {
        if (col < 0 || col >= COL) {
            throw new InvalidColumnException("Column " + col + " is out of range [0, " + (COL - 1) + "]");
        }
        if (this.grid[0][col] != null) {
            throw new ColumnFullException("Column " + col + " is full");
        }

        for (int i = ROW - 1; i >= 0; i--) {
            if (this.grid[i][col] == null) {
                this.grid[i][col] = color;
                return i;
            }
        }
        throw new IllegalStateException("Unreachable: column has space but no empty cell found");
    }

    public boolean isFull() {
        for (int j = 0; j < COL; j++) {
            if (this.grid[0][j] == null) {
                return false;
            }
        }
        return true;
    }

    public boolean checkWinner(int row, int col, Color color) {
        int[][] direction = new int[][]{
                {0, 1}, // horizontal
                {1, 0}, // vertical
                {1, 1}, // down-right
                {-1, 1} // up-right
        };

        for (int[] dir: direction) {
            int colorCount = 1;
            colorCount += countColorCell(row, col, color, dir[0], dir[1]);
            colorCount += countColorCell(row, col, color, -dir[0], -dir[1]);

            if (colorCount >= 4) {
                return true;
            }
        }
        return false;
    }

    private int countColorCell(int row, int col, Color color, int nextRowInc, int nextColInc) {
        row += nextRowInc;
        col +=nextColInc;

        int count = 0;
        while (cellInBound(row, col) && this.grid[row][col] == color) {
            count++;
            row += nextRowInc;
            col += nextColInc;
        }
        return  count;
    }

    private boolean cellInBound(int row, int col) {
        return row >= 0 && row < ROW && col >= 0 && col < COL;
    }

    public void clearCell(int row, int col) {
        if (col < 0 || col >= COL || row < 0 || row >= ROW) {
            throw new InvalidColumnException("Row/Col is out of range");
        }
        this.grid[row][col] = null;
    }
}
