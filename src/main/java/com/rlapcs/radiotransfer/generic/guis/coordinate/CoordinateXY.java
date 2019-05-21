package com.rlapcs.radiotransfer.generic.guis.coordinate;

public class CoordinateXY extends Coordinate{
    public int x, y;

    public CoordinateXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getDimension1() {
        return x;
    }
    @Override
    public int getDimension2() {
        return y;
    }
    @Override
    public String getDimension1Name() {
        return "X";
    }
    @Override
    public String getDimension2Name() {
        return "Y";
    }
}
