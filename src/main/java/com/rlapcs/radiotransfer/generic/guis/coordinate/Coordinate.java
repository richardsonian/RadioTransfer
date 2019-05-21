package com.rlapcs.radiotransfer.generic.guis.coordinate;

public abstract class Coordinate {
    public abstract int getDimension1();
    public abstract int getDimension2();

    public abstract String getDimension1Name();
    public abstract String getDimension2Name();

    @Override
    public String toString() {
        return String.format("(%s:%d, %s:%d)", getDimension1Name(), getDimension1(), getDimension2Name(), getDimension2());
    }
}
