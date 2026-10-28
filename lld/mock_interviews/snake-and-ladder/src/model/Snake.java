package model;

import exceptions.IllegalSizeOfSnake;

public class Snake extends BoardEntity {
    public Snake(int start, int end) {
        super(start, end);
        if (start <= end) {
            throw new IllegalSizeOfSnake("Snake start needs to be greater than end");
        }
    }
}
