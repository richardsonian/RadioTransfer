package com.rlapcs.radiotransfer.generic.guis.coordinate;

public abstract class Coordinate {
    public abstract int getDimension1();
    public abstract int getDimension2();

    public abstract String getDimension1Name();
    public abstract String getDimension2Name();

    public abstract Coordinate scale(int factor);

    public Coordinate addTo(Coordinate coordinate) {
        return new CoordinateXY(getDimension1() + coordinate.getDimension1(), getDimension2() + coordinate.getDimension2());
    }

    @Override
    public String toString() {
        return String.format("(%s: %d, %s: %d)", getDimension1Name(), getDimension1(), getDimension2Name(), getDimension2());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate))
            return false;
        return ((Coordinate) obj).getDimension1() == getDimension1() && ((Coordinate) obj).getDimension2() == getDimension2();
    }
}
