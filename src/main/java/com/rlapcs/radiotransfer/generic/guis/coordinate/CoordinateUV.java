package com.rlapcs.radiotransfer.generic.guis.coordinate;

public class CoordinateUV extends Coordinate {
    public int u, v;

    public CoordinateUV(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public CoordinateUV scale(int factor) {
        return new CoordinateUV(u * factor, v * factor);
    }

    @Override
    public Coordinate addTo(Coordinate coordinate) {
        return new CoordinateUV(u + coordinate.getDimension1(), v + coordinate.getDimension2());
    }

    @Override
    public int getDimension1() {
        return u;
    }
    @Override
    public int getDimension2() {
        return v;
    }
    @Override
    public String getDimension1Name() {
        return "U";
    }
    @Override
    public String getDimension2Name() {
        return "V";
    }
}
