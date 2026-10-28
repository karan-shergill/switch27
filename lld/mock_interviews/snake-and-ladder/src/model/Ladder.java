package model;

import exceptions.IllegalSizeOfLadder;

public class Ladder extends BoardEntity{
    public Ladder(int start, int end) {
        super(start, end);
        if (end <= start) {
            throw new IllegalSizeOfLadder("Ladder end needs to be greater than start");
        }
    }
}
