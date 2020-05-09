package com.rlapcs.radiotransfer.generic.guis.coordinate;

public class DimensionWidthHeight extends Coordinate {
    public int width, height;

    public DimensionWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public DimensionWidthHeight scale(int factor) {
        return new DimensionWidthHeight(width * factor, height * factor);
    }

    @Override
    public int getDimension1() {
        return width;
    }
    @Override
    public int getDimension2() {
        return height;
    }
    @Override
    public String getDimension1Name() {
        return "width";
    }
    @Override
    public String getDimension2Name() {
        return "height";
    }
}
