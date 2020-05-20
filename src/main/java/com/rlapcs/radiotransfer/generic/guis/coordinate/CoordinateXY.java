package com.rlapcs.radiotransfer.generic.guis.coordinate;

public class CoordinateXY extends Coordinate{
    public int x, y;

    public CoordinateXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public CoordinateXY scale(int factor) {
        return new CoordinateXY(x * factor, y * factor);
    }

    @Override
    public CoordinateXY addTo(Coordinate coordinate) {
        return new CoordinateXY(x + coordinate.getDimension1(), y + coordinate.getDimension2());
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
