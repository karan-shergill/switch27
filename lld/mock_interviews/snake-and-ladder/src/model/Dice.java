package model;

import java.util.Random;

public class Dice {
    private int minValue;
    private int maxValue;

    public Dice(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int rollDice() {
        Random random = new Random();
        return random.nextInt(this.maxValue) + this.minValue;
    }
}
